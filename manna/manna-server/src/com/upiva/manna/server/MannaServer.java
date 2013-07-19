/**
 * Authored By: IanF on 02/06/13 13:03
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 13:03: Created, IanF, ...
 *
 */

package com.upiva.manna.server;

import com.sun.xml.internal.ws.util.xml.NodeListIterator;
import com.upiva.common.utl.SYS;
import com.upiva.manna.server.exc.XConsumerException;
import com.upiva.manna.server.exc.XInvocationException;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.ServerConfig;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class MannaServer implements IMannaServer {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	/**
	 * Server config xml context verification schema data (xsd)
	 */
	// todo: build a proper schema
	public static final String SERVER_CONFIG_XML_SCHEMA = ""
			+ ServerConfig.SCHEMA_CONTEXT_HEAD
			+ ServerConfig.SCHEMA_ELEMENT_ANY
			+ ServerConfig.SCHEMA_CONTEXT_TAIL;

	///////////////////////////////////////////////////////////////////////////
	// Data members

	// NOTE: this is a pure convenience to make the intellij uml draw the links - however it still does not see it as a singleton like the simpleUML plugin does; ianf
	private Broker m_broker;


	private final String m_homepath;
	private final ServerConfig m_config;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	/**
	 * Constructor for invocation by server connectors - must use source
	 */
	public MannaServer( final String homepath, final String source ) throws IOException, SAXException, ParserConfigurationException, TransformerException, XConsumerException, XInvocationException {

		// preserve args
		m_homepath = homepath;

		// prep broker
		if( m_broker == null ) {
			Broker.newInstance( this );
		}

		// create new default server config
		m_config = new ServerConfig( source, SERVER_CONFIG_XML_SCHEMA );

		// extract identity
		final String title = m_config.getContextTitle();
		final String version = m_config.getContextVersion();
		final String copyright = m_config.getContextCopyright();
		final String author = m_config.getContextAuthor();
		System.out.printf( String.format( "Starting: %s %s; %s - %s\n", title, version, copyright, author ) );

		// construct consumers
		final NodeListIterator consumers = m_config.getConsumers();
		while( consumers.hasNext() ){
			final Node consumer = ( Node )consumers.next();
			Broker.installConsumer( consumer );
		}

		// construct drivers
		final NodeListIterator drivers = m_config.getDrivers();
		while( drivers.hasNext() ){
			final Node driver = ( Node )drivers.next();
			Broker.installDriver( driver );
		}

		// construct domains
		final NodeListIterator domains = m_config.getDomains();
		while( domains.hasNext() ){
			final Node domain = ( Node )domains.next();
			Broker.installDomain( domain );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public String getHomePath() {
		return m_homepath;
	}

	public ServerConfig getConfig() {
		return m_config;
	}

	@Override
	public void shutdown() {
		Broker.shutdown();
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Main entry point

	public static void main( String[] args ) {
		// create new server
		MannaServer server = null;
		try {
			server = new MannaServer( SYS.getUserDir(), args[ 0 ] );

			// console
			int chr = 0;
			while( ( chr = System.in.read() ) != 0 ) {

			}

		} catch( IOException e ) {
			e.printStackTrace();
		} catch( SAXException e ) {
			e.printStackTrace();
		} catch( ParserConfigurationException e ) {
			e.printStackTrace();
		} catch( TransformerException e ) {
			e.printStackTrace();
		} catch( XConsumerException e ) {
			e.printStackTrace();
		} catch( XInvocationException e ) {
			e.printStackTrace();
		} finally {
			if( server != null )
				server.shutdown();
		}
	}
}
