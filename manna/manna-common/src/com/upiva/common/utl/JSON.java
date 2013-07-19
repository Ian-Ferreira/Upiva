/**
 * Authored By: IanF on 17/05/13 17:14
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 17/05/13 17:14: Created, IanF, ...
 *
 */

package com.upiva.common.utl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

public class JSON {

	///////////////////////////////////////////////////////////////////////////
	// Inner classes

	private interface iParse {

		int openBrace( final char ch, /*non-final*/ int _idx );

		void closeBrace( final char ch );

		int openArray( final String value, final char ch, int _idx );

		void closeArray( final String value, final char ch );

		void newToken( final String token );

		void newValue( final String value );

		void gotSpace();
	}

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Construction

	// STATIC ONLY CLASSES HAVE NO CONSTRUCTORS

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static void print( final StringBuilder builder, final Object object ) {
		// todo - whats realy needed here is a proper reflexion print writer or stream that can step the types and trow-out into an interface; IanF
		// class stepper
		Class<?> clss = object.getClass();
		Object item = object; // load top level
		// print header
		builder.append( clss.getSimpleName() );
		builder.append( ":{" );
		// extract fields
		final Field[] fields = clss.getDeclaredFields();
		for( Field field : fields ) {
			// don't want defers, annotations or constants, etc...
			if( field.isSynthetic() || field.isEnumConstant() || Modifier.isTransient( field.getModifiers() ) )
				continue;
			final String name = field.getName().substring( 2 ); // exclude the 'm_' prefix
			builder.append( name );
			builder.append( ":" );
			// hack it again
			final boolean hack = field.isAccessible();
			field.setAccessible( true );
			try {
				// match type
				switch( field.getType().getSimpleName() ) {
					// todo - its possible i forgot some types here
					case "String":
						builder.append( "\"" );
						builder.append( field.get( item ) );
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
						builder.append( field.get( item ) );
						break;
					case "ArrayList":
					case "Vector": {
						Vector vector = ( Vector )field.get( item );
						if( vector.size() < 1 ) {
							builder.append( "[]" );
							break;
						}
						Object elem = vector.get( 0 );
						builder.append( field.getType().getSimpleName() );
						builder.append( "%" );
						builder.append( elem.getClass().getSimpleName() );
						builder.append( "[" );
						for( int i = 0; i < vector.size(); i++ ) {
							builder.append( vector.get( i ).toString() );
							builder.append( ";" );
						}
						if( builder.charAt( builder.length() - 1 ) == ',' )
							builder.replace( builder.length() - 1, builder.length(), "]" );
						else
							builder.append( "]" );
						break;
					}
					case "<todo>": // todo native arrays
					case "<todo1>": // todo Array and Vectors types
					case "<todo2>": // todo Map types
						break;
					// got here - must be a real object field
					default: {
						// open set
						builder.append( "{" );
						// single msg like in error
						if( item.toString().indexOf( ":" ) > 0 ) {
							// recurse on item
							JSON.print( builder, item );
						} else {
							builder.append( item.toString() );
						}
						// print closure
						builder.append( "}" );
						break;
					}
				}
				builder.append( "," );
			} catch( IllegalAccessException e ) {
				// not anymore
			}
			// restore hack
			field.setAccessible( hack );
		}// end-for
		// print closure
		if( builder.charAt( builder.length() - 1 ) == ',' ) {
			builder.replace( builder.length() - 1, builder.length(), "}" );
		} else {
			builder.append( "}" );
		}
	}

	public static int parse( final String json, final HashMap<String, Object> output ) {
		// check validity
		if( !json.startsWith( "{" ) || !json.endsWith( "}" ) )
			throw new IllegalArgumentException( "JSON.parse; Not JSON input: " + json );
		// builder for output
		final StringBuilder builder = new StringBuilder();
		final String text = json.substring( 1 );
		// sax parser callback
		final iParse parse = new iParse() {
			String label = null;
			@Override
			public int openBrace( final char ch, int idx ) {
				final HashMap<String, Object> newmap = new HashMap<>();
				output.put( label, newmap );
				label = null;
				builder.delete( 0, builder.length() );
				return JSON.parse( json.substring( ++idx ), newmap );
			}

			@Override
			public void closeBrace( final char ch ) {
				return;
			}

			@Override
			public int openArray( final String value, final char ch, final int _idx ) {
				return 0;
			}

			@Override
			public void closeArray( final String value, final char ch ) {
			}

			@Override
			public void newToken( final String token ) {
				if( label == null ){
					label = token;
					builder.delete( 0, builder.length() );
				} else
					builder.append( ':' );
			}

			@Override
			public void newValue( final String value ) {
				if( label == null )
					return;
				if( value.startsWith( "\"" ) ) {
					output.put( label, value.substring( 1, value.length() - 1 ) );
				} else if( value.indexOf( '.' ) > 0 ) {
					output.put( label, Double.parseDouble( value ) );
				} else if( value.equals( "true" ) ) {
					output.put( label, true );
				} else if( value.equals( "false" ) ) {
					output.put( label, false );
				} else if( value.equals( "null" ) ) {
					output.put( label, null );
				} else {
					output.put( label, Long.parseLong( value ) );
				}
				label = null;
				builder.delete( 0, builder.length() );
			}

			@Override
			public void gotSpace() {
				if( label != null )
					builder.append( ' ' );
			}
		};
		// step chars
		int index = 0;
		while( index < text.length() ) {
			final char ch = text.charAt( index );
			switch( ch ) {
				case '{':
					index += parse.openBrace( ch, index );
					break;
				case '}':
					parse.newValue( builder.toString() );
					parse.closeBrace( ch );
					return ++index;
				case ':':
					parse.newToken( builder.toString() );
					break;
				case ',':
					parse.newValue( builder.toString() );
					break;
				case ' ':
					parse.gotSpace();
					break;
				default:
					builder.append( ch );
					break;
			}
			index++;
		}
		return index;
	}

	public static int build( final String classpath, final String json, final Vector<Object> vector ) {
		// check validity
		if( !json.startsWith( "{" ) || !json.endsWith( "}" ) )
			throw new IllegalArgumentException( "JSON.build; Not JSON input: " + json );
		// builder for output
		final StringBuilder builder = new StringBuilder();
		final String text = json.substring( 1 );
		// sax parser callback
		final iParse parse = new iParse() {
			String label = null;
			Object object = null;

			@Override
			public int openBrace( final char ch, int idx ) {
				// extract object class
				final String name = String.format( "%s.%s", classpath.endsWith( "." ) ? classpath.substring( 0, classpath.length() - 1 ) : classpath , label );
				try {
					final Class<?> clss = Class.forName( name );
					// new object
					object = clss.newInstance();
					// add to vector
					vector.add( object );
					// prep rerun
					label = null;
					builder.delete( 0, builder.length() );
					// recurse build
					return JSON.inject( classpath, json.substring( ++idx ), object );
				} catch( ClassNotFoundException e ) {
					throw new IllegalArgumentException( "JSON.build; ClassNotFoundException: " + name + " class not found in path " + classpath );
				} catch( InstantiationException e ) {
					throw new IllegalArgumentException( "JSON.build; InstantiationException: " + name + " - no default constructor" );
				} catch( IllegalAccessException e ) {
					throw new IllegalArgumentException( "JSON.build; IllegalAccessException: " + name + " - constructor need public scope" );
				}
			}

			@Override
			public void closeBrace( final char ch ) {
				return;
			}

			@Override
			public int openArray( final String value, final char ch, final int _idx ) {
				return 0;
			}

			@Override
			public void closeArray( final String value, final char ch ) {
			}

			@Override
			public void newToken( final String token ) {
				if( label == null ){
					label = token;
					builder.delete( 0, builder.length() );
				}
			}

			@Override
			public void newValue( final String value ) {
				if( label == null )
					return;
				label = null;
				builder.delete( 0, builder.length() );
				throw new IllegalArgumentException( "JSON.build; Not a valid top level array of given classpath objects" );
			}

			@Override
			public void gotSpace() {
				if( label != null )
					builder.append( ' ' );
			}
		};
		// step chars
		int index = 0;
		while( index < text.length() ) {
			final char ch = text.charAt( index );
			switch( ch ) {
				case '{':
					index += parse.openBrace( ch, index );
					break;
				case '}':
					parse.newValue( builder.toString() );
					parse.closeBrace( ch );
					return ++index;
				case ':':
					parse.newToken( builder.toString() );
					break;
				case ',':
					parse.newValue( builder.toString() );
					break;
				case ' ':
					parse.gotSpace();
					break;
				default:
					builder.append( ch );
					break;
			}
			index++;
		}
		return index;
	}

	private static int inject( final String classpath, final String json, final Object object ) {
		// check validity
		if( !json.startsWith( "{" ) || !json.endsWith( "}" ) )
			throw new IllegalArgumentException( "JSON.build; Not JSON input: " + json );
		// builder for output
		final StringBuilder builder = new StringBuilder();
		final String text = json.substring( 1 );
		// extract object class
		final Class<?> clazz = object.getClass();
		// sax parser callback
		final iParse parse = new iParse() {
			private String m_label = null; // also a condition check
			public String m_value = null;
			private Field m_field = null;

			@Override
			public int openBrace( final char ch, /*non-final*/ int _idx ) {
				// NOTE: we expect nested complex types to be also objects on our path else fully qualified class identifiers
				final String name;
				if( m_label.indexOf( "." ) > 0 )
					name = m_label;
				else
					name = String.format( "%s.%s", classpath, m_label );
				// extract field object class
				Object fldobj = null;
				// hack the field
				final boolean hack = m_field.isAccessible();
				m_field.setAccessible( true );
				try {
					final Class<?> clss = Class.forName( name );
					// construct field object and hope for hell its got a default constructor
					fldobj = clss.newInstance();
					// add object as our field value
					m_field.set( object, fldobj );
				} catch( ClassNotFoundException e ) {
					e.printStackTrace();
				} catch( InstantiationException e ) {
					e.printStackTrace();
				} catch( IllegalAccessException e ) {
					e.printStackTrace();
				}
				// restore hack
				m_field.setAccessible( hack );
				// recurse inject our field object
				return JSON.inject( classpath, json.substring( ++_idx ), fldobj );
			}

			@Override
			public void closeBrace( final char ch ) {
				return;
			}

			@Override
			public int openArray( final String value, final char ch, final int _idx ) {
				m_value = value;
				builder.delete( 0, builder.length() ); // prep for args
				return 0;
			}

			@Override
			public void closeArray( final String value, final char ch ) {
				// parse class
				final String[] tokens = m_value.split( "%" );
				switch( tokens[ 0 ] ) {
					case "Vector": {
						final String[] values = value.split( ";" );
						final Vector vector = new Vector( values.length );
						for( String item : values ) {
							switch( tokens[ 1 ] ) {
								case "Integer": {
									vector.add( Integer.valueOf( item ) );
									break;
								}
							}
						}
						// hack the field
						final boolean hack = m_field.isAccessible();
						m_field.setAccessible( true );
						try {
							m_field.set( object, vector );
						} catch( IllegalAccessException e ) {
							// n/a
						}
						m_field.setAccessible( hack );
						break;
					}
					default:
						// todo - other collections
						break;
				}

				return;
			}

			@Override
			public void newToken( final String token ) {
				if( m_label != null )
					return;
				// prep rerun
				m_label = token;
				builder.delete( 0, builder.length() );
				// new field name
				final String name = String.format( "m_%s", m_label );
				try {
					m_field = clazz.getDeclaredField( name );
				} catch( NoSuchFieldException e ) {
					throw new IllegalArgumentException( "JSON.inject; Field not found '" + name + "' for object: " + object.getClass().getName() );
				}
			}

			@Override
			public void newValue( final String value ) {
				// exit check
				if( m_label == null )
					return;
				// prep rerun
				m_label = null;
				builder.delete( 0, builder.length() );
				// hack the field
				final boolean hack = m_field.isAccessible();
				m_field.setAccessible( true );
				try {
					// switch and pack
					switch( m_field.getType().getSimpleName() ) {
						case "String":
							final String text =value.substring( 1, value.length() - 1 );
							if( text.equals( "null" ) )
								m_field.set( object, null );
							else
								m_field.set( object, text );
							break;
						case "Date":
							final long lng = Long.parseLong( value );
							m_field.set( object, new Date( lng ) );
							break;
						case "Character":
						case "char":
							m_field.set( object, value.charAt( 0 ) );
							break;
						case "Byte":
						case "byte":
							m_field.set( object, Byte.parseByte( value ) );
							break;
						case "Short":
						case "short":
							m_field.set( object, Short.parseShort( value ) );
							break;
						case "Integer":
						case "int":
							m_field.set( object, Integer.parseInt( value ) );
							break;
						case "Long":
						case "long":
							m_field.set( object, Long.parseLong( value ) );
							break;
						case "Float":
						case "float":
							m_field.set( object, Float.parseFloat( value ) );
							break;
						case "Double":
						case "double":
							m_field.set( object, Double.parseDouble( value ) );
							break;
						case "Boolean":
						case "boolean":
							m_field.set( object, Boolean.parseBoolean( value ) );
							break;
						default:
							throw new IllegalArgumentException( "JSON.inject; Parse error, no compound {...} for field, not a simple type: " + m_field.getName() );
					}
				} catch( IllegalAccessException e ) {
					// should not happen - hacked
				}
				// restore hack
				m_field.setAccessible( hack );
			}

			@Override
			public void gotSpace() {
				if( m_label != null )
					builder.append( ' ' );
			}
		};
		// step chars
		int index = 0;
		while( index < text.length() ) {
			final char ch = text.charAt( index );
			switch( ch ) {
				case '{':
					index += parse.openBrace( ch, index );
					break;
				case '}':
					parse.newValue( builder.toString() );
					parse.closeBrace( ch );
					return ++index;
				case '[':
					index += parse.openArray( builder.toString(), ch, index );
					break;
				case ']':
					parse.closeArray( builder.toString(), ch );
					return ++index;
				case ':':
					parse.newToken( builder.toString() );
					break;
				case ',':
					parse.newValue( builder.toString() );
					break;
				case ' ':
					parse.gotSpace();
					break;
				default:
					builder.append( ch );
					break;
			}
			index++;
		}
		return index;
	}

}
