/**
 * Authored By: IanF on 20/05/13 12:46
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 20/05/13 12:46: Created, IanF, ...
 *
 */

package com.upiva.common.svr;

import com.upiva.common.utl.JSON;

import java.util.Vector;

public class ServerResponse {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private String m_command = null;
	private String m_outcome = null;

	private final Vector<Object> m_content = new Vector<>( 16 );

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ServerResponse( final String message, final String classpath ) throws IllegalArgumentException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		// check validity
		if( ( message == null ) || message.isEmpty() )
			throw new IllegalArgumentException( "ServerResponse: Message void" );
		if( !message.startsWith( "{" ) || !message.endsWith( "}" ) )
			throw new IllegalArgumentException( "ServerResponse: Invalid JSON" );
		if( ( classpath == null ) || classpath.isEmpty() )
			throw new IllegalArgumentException( "ServerResponse: Classpath void" );

		// NOTE: we always expect a json with single header node

		// strip ends
		final  String target = message.substring( 1, message.length() - 1 );

		// custom strip off header
		final int slit = target.indexOf( ':' );
		final String header = target.substring( 0, slit );
		final String contex = target.substring( slit + 1 );

		// tokenize headers and store
		final String[] tokens = header.split( "-" );
		m_outcome = tokens[ 0 ];
		m_command = tokens[ 1 ];

		// call generic json parser
		try {
			JSON.build( classpath, contex, m_content );
		} catch( Exception e ) {
			System.out.println( "ServerResponse: " + e.getClass().getSimpleName() + " - " + e.getLocalizedMessage() );
			throw e;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getCommand() {
		return m_command;
	}

	public boolean isSuccess() {
		return m_outcome.toLowerCase().equals( "success" );
	}

	public String getOutcome() {
		return m_outcome;
	}

	public Object get( final int index ) {
		return m_content.get( index );
	}

	public Vector<Object> getContent() {
		return m_content;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
