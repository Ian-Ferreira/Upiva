/**
 * Authored By: IanF on 20/05/13 13:13
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 20/05/13 13:13: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cmd;

import com.upiva.common.utl.JSON;
import com.upiva.manna.server.exc.XParsingException;

import java.util.Date;
import java.util.HashMap;

public class ClientRequest {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private String m_domain = null;
	private String m_command = null;
	private String m_session = null;
	private String m_version = null;

	private final HashMap<String,Object> m_content = new HashMap<>( 16 );

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ClientRequest( final String message ) throws XParsingException {
		// check validity
		if( !message.startsWith( "{" ) || !message.endsWith( "}" ) )
			throw new XParsingException( "JSON parsing failed" );

		// NOTE: we always expect a json with single header node

		// strip ends
		final  String target = message.substring( 1, message.length() - 1 );

		// custom strip off header
		final int slit = target.indexOf( ':' );
		final String header = target.substring( 0, slit );
		final String contex = target.substring( slit + 1 );

		// tokenize headers and store
		final String[] tokens = header.split( "-" );
		m_domain = tokens[ 0 ];
		m_command = tokens[ 1 ];
		m_session = tokens[ 2 ];
		m_version = tokens[ 3 ];

		// call generic json parser
		try {
			JSON.parse( contex, m_content );
		} catch( Exception e ) {
			throw new XParsingException( e.getLocalizedMessage() );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public String toString() {
		return String.format( "%s-%s-%s:%s", m_domain, m_command, m_session, m_content.toString() );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getDomain() {
		return m_domain;
	}

	public String getCommand() {
		return m_command;
	}

	public String getSession() {
		return m_session;
	}

	public void fixSession() {
		if( m_session.charAt( 0 ) == '#' )
			m_session = m_session.substring( 1 );

	}

	public String getVersion() {
		return m_version;
	}

	public Object get( final String key ) {
		return m_content.get( key );
	}

	public String getString( final String key ) {
		return ( String )this.get( key );
	}

	public char getChar( final String key ) {
		return ( Character )this.get( key );
	}

	public byte getByte( final String key ) {
		return ( Byte )this.get( key );
	}

	public short getShort( final String key ) {
		return ( Short )this.get( key );
	}

	public int getInt( final String key ) {
		return ( Integer )this.get( key );
	}

	public long getLong( final String key ) {
		return ( Long )this.get( key );
	}

	public float getFloat( final String key ) {
		return ( Float )this.get( key );
	}

	public double getDouble( final String key ) {
		return ( Double )this.get( key );
	}

	public Date getDate( final String key ) {
		return ( Date )this.get( key );
	}

	public boolean getBoolean( final String key ) {
		return ( Boolean )this.get( key );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
