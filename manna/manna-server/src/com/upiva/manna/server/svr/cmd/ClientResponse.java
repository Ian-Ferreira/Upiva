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

import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

public class ClientResponse {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private String m_command = null;
	private Vector<Object> m_content = new Vector<>();

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ClientResponse( final String command ) {
		// preserve args
		m_command = command;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	public String getCommand() {
		return m_command;
	}

	public Object get( final int index ) {
		return m_content.get( index );
	}

	public boolean add( final Object object ) {
		return m_content.add( object );
	}

	public void put( final Object object, final int index ) {
		m_content.setElementAt( object, index );
	}

	public String toStringJSON() {
		final StringBuilder builder = new StringBuilder( 128 );
		builder.append( "{" );
		builder.append( "success" );
		builder.append( "-" );
		builder.append( m_command );
		builder.append( ":{" );
		final Iterator<Object> iter = m_content.iterator();
		while( iter.hasNext() ) {
			final Object item = iter.next();
			if( item == null )
				break;
			// check type
			final String clss = item.getClass().getSimpleName();
			switch( clss ) {
				case "String":
					builder.append( "\"" );
					builder.append( item );
					builder.append( "\"" );
					break;
				case "Date": // special case - print as long
					builder.append( ( ( Date )item ).getTime() );
					break;
				case "Character":
				case "Byte":
				case "Short":
				case "Integer":
				case "Long":
				case "Float":
				case "Double":
				case "Boolean":
				case "char":
				case "byte":
				case "short":
				case "int":
				case "long":
				case "float":
				case "double":
				case "boolean":
					builder.append( item );
					break;
				case "<todo>": // todo native arrays
				case "<todo1>": // todo Array and Vectors types
				case "<todo2>": // todo Map types
					break;
				// got here - must be a real object field
				default: {
					JSON.print( builder, item );
					builder.append( "," );
					break;
				}
			}
		}
		if( builder.charAt( builder.length() - 1 ) == ',' ) {
			builder.replace( builder.length() - 1, builder.length(), "}" );
			builder.append( "}" );
		} else {
			builder.append( "}}" );
		}
		return builder.toString();
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
