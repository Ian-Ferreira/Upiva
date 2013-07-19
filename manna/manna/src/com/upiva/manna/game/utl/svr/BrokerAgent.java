/**
 * Authored By: IanF on 15/05/13 20:21
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 15/05/13 20:21: Created, $(USER), ...
 *
 */

package com.upiva.manna.game.utl.svr;

import com.upiva.common.dbo.GamerProfile;
import com.upiva.common.dbo.Token;
import com.upiva.common.utl.B64;
import com.upiva.common.utl.SYS;
import com.upiva.common.utl.XOR;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.gam.MannaProperties;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

public class BrokerAgent {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final AbstractGame m_game;
	private final String m_address;
	private final String m_method;
	private final String m_language;
	private final String m_content;
	private final String m_charset;
	private final String m_accept;
	private final boolean m_doInput;
	private final boolean m_doOutput;
	private final boolean m_useCaches;
	private final int m_timeout;
	private final String m_dbopath;
	private final boolean m_b64values;

	private String m_ipaddr = null;
	private Token m_token = null;
	private GamerProfile m_profile = null;

	///////////////////////////////////////////////////////////////////////////
	// Singleton factory

	private static BrokerAgent s_instance = null;

	private BrokerAgent( final AbstractGame game ) throws UnknownHostException, SocketException, MalformedURLException {
		// preserve args
		m_game = game;

		// serve properties
		m_address = m_game.getProperties().getString( MannaProperties.eKey.SERVER_REMOTE_ADDRESS );
		m_method = m_game.getProperties().getString( MannaProperties.eKey.SERVER_CONNECT_METHOD );
		m_content = m_game.getProperties().getString( MannaProperties.eKey.SERVER_CONNECT_CONTENT );
		m_language = m_game.getProperties().getString( MannaProperties.eKey.SERVER_CONNECT_LANGUAGE );
		m_charset = m_game.getProperties().getString( MannaProperties.eKey.SERVER_CONNECT_CHARSET );
		m_accept = m_game.getProperties().getString( MannaProperties.eKey.SERVER_CONNECT_ACCEPT );
		m_doInput = m_game.getProperties().getBoolean( MannaProperties.eKey.SERVER_CONNECT_DO_INPUT );
		m_doOutput = m_game.getProperties().getBoolean( MannaProperties.eKey.SERVER_CONNECT_DO_OUTPUT );
		m_useCaches = m_game.getProperties().getBoolean( MannaProperties.eKey.SERVER_CONNECT_USE_CACHES );
		m_timeout = m_game.getProperties().getInt( MannaProperties.eKey.SERVER_CONNECT_TIMEOUT );
		m_dbopath = m_game.getProperties().getString( MannaProperties.eKey.SERVER_DBO_ENTITY_CLASSPATH );
		m_b64values = m_game.getProperties().getBoolean( MannaProperties.eKey.SERVER_BASE64_ENCODE_VALUES );

		// hack
		final InetAddress ipa = InetAddress.getLocalHost();
		final NetworkInterface nic = NetworkInterface.getByInetAddress( ipa );
		byte[] mac = nic.getHardwareAddress();
		// mac can be null
		if( mac != null ) {
			m_ipaddr = new String( mac );
		} else {
			// ipa can be null
			if( ipa != null )
				m_ipaddr = new String( ipa.toString() ); // toString provides best identifier
			else
				m_ipaddr = new String( "127.0.0.1" );
		}

	}

	public static BrokerAgent getInstance() {
		if( s_instance == null )
			throw new IllegalStateException( "Broker not initialized" );
		return s_instance;
	}

	public static BrokerAgent newInstance( final AbstractGame game ) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SocketException, UnknownHostException, MalformedURLException {
		if( s_instance != null )
			throw new IllegalStateException( "Broker already initialized" );
		s_instance = new BrokerAgent( game );
		return s_instance;
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getIpaddr() {
		return m_ipaddr;
	}

	/// generic actions

	public ServerResponse sendMessage( final ServerRequest request ) {
		// connect server
		final HttpURLConnection connection;
		try {
			// prep request
			final String encode = m_b64values ? B64.encode( request.toString() ) : URLEncoder.encode( request.toString(), m_charset );
			final String query = String.format( "command=%s", encode );
			// prep url
			final URL url = new URL(  m_address + "?" + query );
			connection = ( HttpURLConnection )url.openConnection();
			// prep workings
			BufferedReader reader = null;
			DataOutputStream stream = null;
			try {
				// set params
				connection.setDoInput( m_doInput );
				connection.setDoOutput( m_doOutput );
				connection.setReadTimeout( m_timeout );
				connection.setDefaultUseCaches( m_useCaches );
				connection.setRequestMethod( m_method );
				connection.addRequestProperty( "Content-Type", m_content );
				connection.addRequestProperty( "Content-Length", "" + Integer.toString( query.getBytes().length ) );
				connection.addRequestProperty( "Content-Language", m_language );
				connection.addRequestProperty( "Accept-Charset", m_charset );
				// write output
				stream = new DataOutputStream( connection.getOutputStream() );
				stream.writeBytes( query );
				stream.flush();
				// connect
				//connection.connect();
				// extract response
				int code = connection.getResponseCode();
				reader = new BufferedReader( new InputStreamReader( connection.getInputStream() ) );
				final StringBuffer buffer = new StringBuffer(  );
				String line = null;
				while( ( line = reader.readLine() ) != null ) {
					buffer.append( line );
				}
				final ServerResponse response = new ServerResponse( buffer.toString(), m_dbopath );
				return response;
			} catch( ClassNotFoundException e ) {
				e.printStackTrace();
			} catch( ProtocolException e ) {
				e.printStackTrace();
			} catch( InstantiationException e ) {
				e.printStackTrace();
			} catch( IllegalAccessException e ) {
				e.printStackTrace();
			} catch( IOException e ) {
				e.printStackTrace();
			} finally {
				if( stream != null )
					stream.close();
				if( reader != null )
					reader.close();
				connection.disconnect();
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}

	/// database accessors

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private void _packSystemInfo( final ServerRequest request ) {
		request.put( "javaUserName", SYS.getUserName() );
		request.put( "javaUserDir", SYS.getUserDir() );
		request.put( "javaUserHome", SYS.getUserHome() );
		request.put( "javaSpecificationName", SYS.getJavaSpecificationName() );
		request.put( "javaSpecificationVendor", SYS.getJavaSpecificationVendor() );
		request.put( "javaSpecificationVersion", SYS.getJavaSpecificationVersion() );
		request.put( "javaVendor", SYS.getJavaVendor() );
		request.put( "javaVendorUrl", SYS.getJavaVendorUrl() );
		request.put( "javaVersion", SYS.getJavaVersion() );
		request.put( "javaVmName", SYS.getJavaVmName() );
		request.put( "javaVmSpecificationName", SYS.getJavaVmSpecificationName() );
		request.put( "javaVmSpecificationVendor", SYS.getJavaVmSpecificationVendor() );
		request.put( "javaVmSpecificationVersion", SYS.getJavaVmSpecificationVersion() );
		request.put( "javaVmVendor", SYS.getJavaVmVendor() );
		request.put( "javaVmVersion", SYS.getJavaVmVersion() );
		request.put( "javaOsArch", SYS.getOsArch() );
		request.put( "javaOsVersion", SYS.getOsVersion() );
		request.put( "javaClassPath", SYS.getJavaClassPath() );
		request.put( "javaClassVersion", SYS.getJavaClassVersion() );
		request.put( "javaPathSeparator", SYS.getPathSeparator() );
		request.put( "javaLineSeparator", SYS.getLineSeparator() );
		request.put( "javaLibraryPath", SYS.getJavaLibraryPath() );
		request.put( "javaExtDirs", SYS.getJavaExtDirs() );
		request.put( "processors", Runtime.getRuntime().availableProcessors() );
		request.put( "freeMemory", Runtime.getRuntime().freeMemory() );
		request.put( "maxMemory", Runtime.getRuntime().maxMemory() );
		request.put( "totalMemory", Runtime.getRuntime().totalMemory() );
	}

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static final Token getToken() {
		return s_instance.m_token;
	}

	public static boolean acquireToken( final String username, final String password ) throws IOException {
		// new request object
		final ServerRequest request = new ServerRequest( s_instance.m_game.getDomain(), "LogonUser", s_instance.m_game.getVersion(), s_instance.m_game.getIPAddress() );
		// populate request
		request.put( "username", username );
		request.put( "password", password );
		s_instance._packSystemInfo( request );
		// send it
		final ServerResponse response = s_instance.sendMessage( request );
		// check valid response
		if( response.isSuccess() ) {
			s_instance.m_token = ( Token )response.get( 0 );
			s_instance.m_profile = ( GamerProfile )response.get( 1 );
			return true;
		}
		return false; // timeout
	}

	public static boolean availableUsername( final String username, final String email ) throws IOException {
		// new request object
		final ServerRequest request = new ServerRequest( s_instance.m_game.getDomain(), "AvailableUsername", s_instance.m_game.getVersion(), s_instance.m_game.getIPAddress() );
		// populate request
		request.put( "username", username );
		request.put( "email", email );
		// send it
		final ServerResponse response = s_instance.sendMessage( request );
		// check valid response
		if( response.isSuccess() ) {
			return ( Boolean )response.get( 0 );
		}
		return false; // timeout - availability unknown
	}

	public static boolean registerUser( final String username, final String password, final String email ) throws IOException {
		// new request object
		final ServerRequest request = new ServerRequest( s_instance.m_game.getDomain(), "EnrollUser", s_instance.m_game.getVersion(), s_instance.m_game.getIPAddress() );
		// populate request
		request.put( "username", username );
		request.put( "password", password );
		request.put( "email", email );
		s_instance._packSystemInfo( request );
		// send it
		final ServerResponse response = s_instance.sendMessage( request );
		// check valid response
		if( response.isSuccess() ) {
			s_instance.m_token = ( Token )response.get( 0 );
			return true;
		}
		return false; // timeout
	}

	public static GamerProfile obtainProfile() {
		final String session = XOR.encrypt( s_instance.getIpaddr(), s_instance.m_token.getSession() );
		final ServerRequest request = new ServerRequest( s_instance.m_game.getDomain(), "UpdateProfile", session, s_instance.m_game.getVersion() );
		// send it
		final ServerResponse response = s_instance.sendMessage( request );
		if( response.isSuccess() ) {
			final GamerProfile profile = ( GamerProfile )response.get( 0 );
			return profile;
		}
		return null;
	}
}
