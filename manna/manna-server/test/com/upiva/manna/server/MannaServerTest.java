/**
 * Authored By: IanF on 21/06/13 21:57
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 21/06/13 21:57: Created, IanF, ...
 *
 */

package com.upiva.manna.server;

import com.upiva.common.dbo.Token;
import com.upiva.common.utl.SYS;
import com.upiva.manna.server.svr.IServerConnector;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

public class MannaServerTest {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	private static IMannaServer s_server = null;
	private static boolean s_completed = false;
	private static Token s_guest = null;
	private static Token s_user1;

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private String m_output = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	@Before
	public void setUp() throws Exception {
		// permanent server
		if( s_server == null ) {
			s_server = IServerConnector.Factory.connector().connect( "embed:com.upiva.manna.server.MannaServer%./cfg/server.xml", SYS.getUserDir() );
		}
	}

	@Test
	public void testName() throws Exception {

		for( int i = 0; i < 100; i++ ) {
			String input = String.format( "{MannaGames-Fibonaci-192.168.22.195-1.0.0.1:{request:%d,random:%d}}", i, ThreadLocalRandom.current().nextInt( 25 ) + 1 );
			System.out.println( "\n>>>" + input );
			this._process( input );
			Thread.sleep( 1000 );
		}

	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private String _process( final String input ) {

		s_completed = false;

		ByteBuffer txBuffer = ByteBuffer.wrap( input.getBytes() );
		ByteBuffer rxBuffer = ByteBuffer.allocateDirect( 1024 );

		//create asynchronous socket channel
		try( final AsynchronousSocketChannel asynchronousSocketChannel = AsynchronousSocketChannel.open() ) {
			if( asynchronousSocketChannel.isOpen() ) {

				//connect this channel's socket
				Void connect = asynchronousSocketChannel.connect( new InetSocketAddress( "localhost", 4545 ) ).get();
				if( connect == null ) {
					//System.out.println( "Local address: " + asynchronousSocketChannel.getLocalAddress() );

					//sending data
					asynchronousSocketChannel.write( txBuffer ).get();
					asynchronousSocketChannel.read( rxBuffer, rxBuffer, new CompletionHandler<Integer, ByteBuffer>() {
						public void completed( Integer result, ByteBuffer buffer ) {
							buffer.flip();
							String msgReceived = Charset.defaultCharset().decode( buffer ).toString();
							System.out.println( "<<<" + msgReceived );
							s_completed = true;
						}

						public void failed( Throwable exc, ByteBuffer buffer ) {
							s_completed = false;
							throw new UnsupportedOperationException( "read failed!" );
						}
					} );

					while( !s_completed ) {
						try {
							Thread.sleep( 1000 );
						} catch( Exception e ) {
						}
						//System.out.println( "Waiting for response from the server" );
					}

				} else {
					System.out.println( "The connection cannot be established!" );
				}
			} else {
				System.out.println( "The asynchronous socket channel cannot be opened!" );
			}
		} catch( IOException | InterruptedException | ExecutionException ex ) {
			System.err.println( ex );
		}

		return null;
	}

}
