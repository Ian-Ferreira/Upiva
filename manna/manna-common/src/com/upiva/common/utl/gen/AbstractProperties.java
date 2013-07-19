/**
 * Authored By: IanF on 18/05/13 10:16
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this this, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/05/13 10:16: Created, IanF, ...
 *
 */

package com.ianf.gdx.general.utl.gen;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AbstractProperties extends Properties {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	/// Hard-code's

	public static final String STR_IOP_INJECTION_FIELD_PREFIX = "m_";

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public AbstractProperties() {
		super();
	}

	public AbstractProperties( final Properties defaults ) {
		super( defaults );
	}

	///////////////////////////////////////////////////////////////////////////
	// Enforced

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	/// java.util.Properties

	@Override
	public synchronized void load( final Reader in ) throws IOException {
		super.load( in );
	}

	@Override
	public synchronized void store( final Writer writer, final String comment ) throws IOException {
		super.store( writer, comment );
	}

	@Override
	public synchronized void loadFromXML( final InputStream in ) throws IOException, InvalidPropertiesFormatException {
		super.loadFromXML( in );
	}

	@Override
	public void storeToXML( final OutputStream os, final String comment ) throws IOException {
		super.storeToXML( os, comment );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	/// state control

	public void reset() {
		throw new IllegalStateException( "<todo>" );
	}

	/// accessors

	public Object get( final IPropertyKey key ) {
		return this.get( key.toString() );
	}

	public void put( final IPropertyKey key, final Object value ) {
		this.put( key.toString(), value );
	}

	public HashMap<String,Object> extract( final String match ) {
		final HashMap<String,Object> map = new HashMap<>( 16 );
		final Set<Map.Entry<Object, Object>> keys = this.entrySet();
		final Iterator<Map.Entry<Object, Object>> iter = keys.iterator();
		while( iter.hasNext() ) {
			final Map.Entry<Object, Object> item = iter.next();
			if( item.getKey().toString().startsWith( match ) ) {
				map.put( item.getKey().toString(), item.getValue() );
			}
		}
		return map;
	}

	/// iop injector

	public void inject( final Class<?> clazz, final Object object, final int index ) {
		// matcher
		String test = clazz.getName();
		// has multiples
		if( index > -1 ) {
			test = String.format( "%s$%d", test, index );
		}
		// extract all properties for class
		final HashMap<String,Object> map = this.extract( test );
		// iterate and inject
		Class<?> clzz = clazz; // stepper
		final Iterator<Map.Entry<String, Object>> iter = map.entrySet().iterator();
		while( iter.hasNext() ) {
			final Map.Entry<String, Object> item = iter.next();
			// get field name at end
			final int slit = item.getKey().toString().lastIndexOf( "@" );
			String name = STR_IOP_INJECTION_FIELD_PREFIX + item.getKey().toString().substring( slit + 1 );
			// ? vector injector
			final int vlit = name.lastIndexOf( "$" );
			int idex = -1;
			if( vlit > 0 ) {
				idex = Integer.parseInt( name.substring( vlit + 1 ) );
				name = name.substring( 0, vlit );
			}
			// deep scan super classes
			boolean done = false;
			do {
				// extract fields
				try {
					final Field field = clzz.getDeclaredField( name );
					final boolean flag = field.isAccessible();
					field.setAccessible( true ); // hack
					// ? map inject
					if( idex > -1 ) {
						// NOTE: because properties are not sequential we cannot guarantee the order and a vector.add(index) can throw an
						// out-of-bounds exception, we simply just have to add them and retrieve them then by index, using vector.addElement
						// shit - this is a train-smash, need to rethink using a xml config file instead of stupid properties
						// REWORKED - broker config must use maps in stead of vectors - IanF
						final Class<?> mcls = field.getType();
						try {
							final Class<?>[] argz = new Class[] { Object.class, Object.class };
							final Method madd = mcls.getDeclaredMethod( "put", argz );
							final Object mfld = field.get( object );
							final Object[] valz = new Object[] { idex, item.getValue() };
							madd.invoke( mfld, valz );
						} catch( Exception e ) {
							// Now what ? does'nt maps have a put method anymore
							e.printStackTrace();
						}
					} else {
						try {
							field.set( object, item.getValue() );
						} catch( IllegalAccessException e ) {
							// not anymore
						}
					}
					// restore hack
					field.setAccessible( flag );
					// get out - were done
					done = true;
					System.out.println( "Injecting '" + item.getKey().toString() + "' with '" + item.getValue().toString() + "'" );
					done = true;
				} catch( NoSuchFieldException e ) {
					// step deep
					clzz = clzz.getSuperclass();
					if( !( clzz instanceof Object ) ) {
						System.err.println( "Field '" + name + "' alias( " + item.getKey().toString() + " ) not found in class '" + clazz.getName() + "'" );
						done = true;
					}
				}
			} while( !done );
			// restore top level class
			clzz = clazz;
		}
	}

	/// type extractors

	public String getString( final IPropertyKey key ) {
		return ( String )this.get( key.toString() );
	}

	public Byte getByte( final IPropertyKey key ) {
		return ( Byte )this.get( key.toString() );
	}

	public Character getChar( final IPropertyKey key ) {
		return ( Character )this.get( key.toString() );
	}

	public Short getShort( final IPropertyKey key ) {
		return ( Short )this.get( key.toString() );
	}

	public int getInt( final IPropertyKey key ) {
		return ( Integer )this.get( key.toString() );
	}

	public Long getLong( final IPropertyKey key ) {
		return ( Long )this.get( key.toString() );
	}

	public Float getFloat( final IPropertyKey key ) {
		return ( Float )this.get( key.toString() );
	}

	public Double getDouble( final IPropertyKey key ) {
		return ( Double )this.get( key.toString() );
	}

	public Boolean getBoolean( final IPropertyKey key ) {
		return ( Boolean )this.get( key.toString() );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Static helpers

}
