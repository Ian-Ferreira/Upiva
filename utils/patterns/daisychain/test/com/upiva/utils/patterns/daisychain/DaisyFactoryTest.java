/**
 * Authored By: IanF on 18/07/13 18:11
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 18:11: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain;

import com.upiva.utils.patterns.daisychain.common.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.concurrent.ThreadLocalRandom;

public class DaisyFactoryTest {

	private static final String S_SHARED_KEY = "38769ba8-f874-8768-dfe7-08967519a9ae";
	private String m_input;
	private byte[] m_bytes;
	private Charset m_charset;

	private class DummyHandlerDaisy implements IDaisyChain {
		@Override
		public Object process( final Object input ) throws XDaisyException {
			System.out.println( "DaisyFactoryTest$DummyHandlerDaisy.process: " + input );
			return input;
		}
	}

	@Before
	public void setUp() throws Exception {
		m_input = String.format( "{%s-%s-%s-%s:{index:%d,value:%d}}", "Upiva.MannaGames", "Fibonaci", "127.0.0.1", "1.0.0.1", 1, ThreadLocalRandom.current().nextInt( 10, 25 ) );
		m_input = ( String )new HashPrependerDaisy( S_SHARED_KEY ).process( m_input );
		m_input = ( String )new SizePrependerDaisy().process( m_input );
		m_bytes = ( byte[] )new BytesEncoderDaisy( Charset.forName( "UTF8" ) ).process( m_input );
		m_charset = Charset.forName( "UTF8" );
	}

	@After
	public void tearDown() throws Exception {
		// n/a
	}

	@Test
	public void testDaisyFactory1() throws Exception {

		// build a chain
		IDaisyChain daisy = DaisyFactory.newChain(
				new BytesDecoderDaisy( Charset.forName( "UTF8" ) ),
				SizeMatcherDaisy.class,
				new HashMatcherDaisy( S_SHARED_KEY ),
				JsonParserDaisy.class.getName(),
				new DummyHandlerDaisy(),
				JsonWriterDaisy.class,
				new HashPrependerDaisy( S_SHARED_KEY ),
				SizePrependerDaisy.class.getName(),
				new BytesEncoderDaisy( Charset.forName( "UTF8" ) )
		);

		try {
			System.out.printf( ">>>%s\n", m_input );

			// and call it
			Object output = daisy.process( m_bytes );

			System.out.printf( "<<<%s\n", ( String )new BytesDecoderDaisy( Charset.forName( "UTF8" ) ).process( output ) );
		} catch( XDaisyException e ) {
			System.err.printf( "!EXCEPTION: %s; %s '%s'\n", e.getCause().toString(), e.getDaisy(), e.getObject() );
			// or - just print it...
			e.printException();
			throw e;
		}
	}

	@Test
	public void testDaisyFactory2() throws Exception {
		// fluent call
		System.out.println( new BytesDecoderDaisy( m_charset ).process(
			DaisyFactory.newChain(
				new IDaisyChain() {
					@Override
					public Object process( final Object input ) throws XDaisyException {
						return new String( ( ( byte[] )input ), m_charset );
					}
				},
				SizeMatcherDaisy.class,
				new HashMatcherDaisy( S_SHARED_KEY ),
				JsonParserDaisy.class,
				new IDaisyChain() {
					@Override
					public Object process( final Object input ) throws XDaisyException {
						// echo input back
						return input;
					}
				},
				JsonWriterDaisy.class,
				new HashPrependerDaisy( S_SHARED_KEY ),
				SizePrependerDaisy.class.getName(),
				new IDaisyChain() {
					@Override
					public Object process( final Object input ) throws XDaisyException {
						return ( ( String )input ).getBytes( m_charset );
					}
				}
		).process( m_bytes ) ) );
	}

}
