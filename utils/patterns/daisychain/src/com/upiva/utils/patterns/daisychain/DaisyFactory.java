/**
 * Authored By: IanF on 18/07/13 17:36
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 17:36: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain;

public class DaisyFactory {

	///////////////////////////////////////////////////////////////////////////
	// Inner classes

	private static final class XDaisyComplete extends Exception {

		private final Object m_object;

		private XDaisyComplete( final Object object ) {
			m_object = object;
		}
	}

	private static final class DaisyProxy implements IDaisyChain {

		private final IDaisyChain m_daisy;
		private final DaisyProxy m_proxy;

		private Object m_output = null;

		private DaisyProxy( final IDaisyChain daisy, final DaisyProxy proxy ) {
			m_daisy = daisy;
			m_proxy = proxy;
		}

		@Override
		public Object process( final Object input ) throws XDaisyException {
			try {
				return this._invoke( input );
			} catch( XDaisyComplete c ) {
				return c.m_object;
			}
		}

		@Override
		public String toString() {
			return String.format( "%s [%s]", m_daisy.getClass().getSimpleName(), m_output );
		}

		private Object _invoke( final Object input ) throws XDaisyComplete {
			m_output = m_daisy.process( input );
			// null result - flag to terminate
			if( m_output == null )
				throw new XDaisyException( m_daisy, input, new NullPointerException() );
			// no proceeded - were done
			if( m_proxy == null )
				throw new XDaisyComplete( m_output );
			// recurse on next
			return m_proxy._invoke( m_output );
		}

	}

	///////////////////////////////////////////////////////////////////////////
	// Static factory

	@SuppressWarnings( "unchecked" )
	public static IDaisyChain newChain( final Object... args ) {
		// validate
		if( args.length < 1 )
			throw new IllegalArgumentException( "DaisyFactory.newChain must be called with at least one IDaisyChain derived class " );

		// reverse loop & create proxies
		DaisyProxy proxy = null;
		for( int i = args.length - 1; i > -1; i-- ) {

			// 1. prepare daisy
			Class<? extends IDaisyChain> clss = null;
			IDaisyChain daisy = null;
			try {
				// try it as a class first
				clss = ( Class<? extends IDaisyChain> )args[ i ];
				daisy = clss.newInstance();
			} catch( Exception e ) {
				try {
					// try it as a string class name
					clss = ( Class<? extends IDaisyChain> )Class.forName( (String)args[ i ] );
					daisy = clss.newInstance();
				} catch( Exception e1 ) {
					try {
						// try it as a object
						daisy = ( IDaisyChain )args[ i ];
					} catch( Exception e2 ) {
						// fall-through to null check
					}
				}
			}
			if( daisy == null )
				throw new IllegalArgumentException( "DaisyFactory.newChain failed parsing IDaisyChain derived object argument [" + i + "]: " + args[ i ] );

			// 2. create proceeding proxy
			proxy = new DaisyProxy( daisy, proxy );
		}

		// return root proxy
		return proxy;
	}
}
