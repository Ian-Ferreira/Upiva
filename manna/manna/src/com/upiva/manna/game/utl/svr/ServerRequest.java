/**
 * Authored By: IanF on 20/05/13 09:01
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 20/05/13 09:01: Created, IanF, ...
 *
 */

package com.upiva.manna.game.utl.svr;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class ServerRequest {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_domain;
	private final String m_command;
	private final String m_session;
	private final String m_version;

	private final HashMap<String,Object> m_content = new HashMap<>( 16 );

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ServerRequest( final String domain, final String command, final String version, final String session ) {
		// preserve args
		m_domain = domain;
		m_command = command;
		m_session = session;
		m_version = version;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder( 128 );
		builder.append( "{" );
		builder.append( m_domain );
		builder.append( "-" );
		builder.append( m_command );
		builder.append( "-" );
		builder.append( m_session );
		builder.append( "-" );
		builder.append( m_version );
		builder.append( ":{" );
		Iterator<Map.Entry<String, Object>> iter = m_content.entrySet().iterator();
		while( iter.hasNext() ) {
			Map.Entry<String, Object> item = iter.next();
			this._toStringEntry( builder, item );
			builder.append( "," );
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
	// Public methods

	public Object put( final String key, final Object value ) {
		return m_content.put( key, value );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private void _toStringEntry( final StringBuilder builder, final Map.Entry<String, Object> entry ) {
		builder.append( entry.getKey().toString() );
		builder.append( ":" );
		this._toStringObject( builder, entry.getValue() );
		//builder.append( "," );
	}

	private void _toStringObject( final StringBuilder builder, final Object object ) {
		if( object == null ) {
			builder.append( object );
			return;
		}
		final Class<?> clss = object.getClass();
		switch( clss.getSimpleName() ) {
			case "String":
				builder.append( "\"" );
				builder.append( object.toString() );
				builder.append( "\"" );
				break;
			case "Date":
				builder.append( ( ( Date )object ).getTime() );
				break;
			case "Boolean":
			case "Character":
			case "Byte":
			case "Short":
			case "Integer":
			case "Long":
			case "Float":
			case "Double":
			case "boolean":
			case "char":
			case "byte":
			case "short":
			case "int":
			case "long":
			case "float":
			case "double":
				builder.append( object );
				break;
			default: {
				// is array or map
				if( clss.getComponentType() != null ) {
					// todo
				} else if( clss.isAssignableFrom( List.class ) ) {
					// todo
				} else if( clss.isAssignableFrom( Map.class ) ) {
					// todo
				} else {
					// else must be object
					builder.append( "{" );
					final Class<?> clzz = object.getClass();
					final Field[] fields = clzz.getDeclaredFields();
					for( Field field : fields ) {
						// don't do constants and transient's
						if( field.isEnumConstant() || field.isSynthetic() )
							break;
						int mod = field.getModifiers();
						if( ( mod & Modifier.TRANSIENT ) == Modifier.TRANSIENT )
							break;
						if( field.getName().startsWith( "m_" ) ) {
							builder.append( field.getName().substring( 2 ) );
						} else {
							builder.append( field.getName() );
						}
						builder.append( ":" );
						boolean flag = field.isAccessible();
						field.setAccessible( true );
						try {
							final Object value = field.get( object );
							this._toStringObject( builder, value );
						} catch( IllegalAccessException e ) {
							// not anymore
						}
						field.setAccessible( flag );
						builder.append( "," );
					}
					if( builder.charAt( builder.length() - 1 ) == ',' ) {
						builder.replace( builder.length() - 1, builder.length(), "}" );
					} else {
						builder.append( "}" );
					}
				}
				break;
			}
		}
	}

}
