/**
 * Authored By: IanF on 24/06/13 17:41
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 24/06/13 17:41: Created, IanF, ...
 *
 */

package com.upiva.manna.server.drv.tcp6c;

import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.cns.IConsumerService;
import com.upiva.manna.server.svr.qlf.IMessageQualifier;
import com.upiva.manna.server.svr.qlf.MessageQualifierResult;
import com.upiva.manna.server.svr.utl.IMapBlockingQueueProcess;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.rmi.UnexpectedException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class DriverProcessTCP6c implements Runnable, IMapBlockingQueueProcess {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	private static enum eState { DEFAULT, READING, DONEREAD, COMPLETE, EXCEPTION};

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final IMessageQualifier m_qualifier;
	private final String m_consumer;
	private final int m_rxbufsize;
	private final long m_sleeptime;
	private final StringBuffer m_buffer;
	private final ByteBuffer m_rxbuffer;
	private final CharsetDecoder m_decoder;

	// tread crossers
	private volatile SocketChannel v_channel = null;
	private volatile eState v_state = eState.DEFAULT;
	private volatile AtomicBoolean v_canwrite = new AtomicBoolean( false );
	private AtomicBoolean v_canread = new AtomicBoolean( false );

	// thread holder
	private volatile Future<?> m_future = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DriverProcessTCP6c( final IMessageQualifier qualifier, final String consumer, final int rxbufsize, final long sleeptime ) {
		// preserve args
		m_qualifier = qualifier;
		m_consumer = consumer;
		m_rxbufsize = rxbufsize;
		m_sleeptime = sleeptime;
		// prep run vars
		m_rxbuffer = ByteBuffer.allocateDirect( m_rxbufsize );
		m_decoder = Charset.forName( "ISO-8859-1" ).newDecoder();
		// thread-safe output buffer
		m_buffer = new StringBuffer( rxbufsize );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public void run() {
		System.out.println( "DriverProcessTCP6c.run:started" );
		// trap all and print to buffer
		try {
			// loop until break or interrupted
			while( true ) {

				// await can-read flag
				if( v_state.equals( eState.DEFAULT ) && v_canread.get() ) {
					// read data
					final int count = v_channel.read( m_rxbuffer );
					// has data
					if( count > 0 ) {
						// pack local buffer
						m_rxbuffer.flip();
						m_buffer.append( m_decoder.decode( m_rxbuffer ).toString() );
						m_rxbuffer.clear();

						// qualify message
						final MessageQualifierResult result = m_qualifier.qualify( m_buffer.toString() );
						if( result.getState().equals( MessageQualifierResult.eState.SUCCESS ) ) {
							// acquire consumer from broker
							IConsumerService consumer = null;
							try {
								consumer = Broker.acquireConsumer( m_consumer );
								System.out.println( ">>>" + m_buffer );
								// consumer to process request
								final String output = consumer.process( result.getMessage() );
								// preserve output for write phase
								m_buffer.delete( 0, m_buffer.length() );
								m_buffer.append( output );
								// done reading
								v_state = eState.DONEREAD;
							} catch( Exception e ) {
								// set error flag
								v_state = eState.EXCEPTION;
								// return error info in buffer
								m_buffer.delete( 0, m_buffer.length() );
								m_buffer.append( e.toString() );
							} finally {
								// must do
								if( consumer != null ) {
									Broker.disposeConsumer( consumer );
								}
							}
						} else {
							// preserve output for write phase
							m_buffer.delete( 0, m_buffer.length() );
							m_buffer.append( result.getState().name() );

						}

					}
					// else - fall through - sleep a bit
				}

				// await can-write flag
				if( ( v_state.equals( eState.DONEREAD ) || v_state.equals( eState.EXCEPTION ) ) && v_canwrite.get() ) {
					// format output
					final String output = m_qualifier.prepare( m_buffer.toString() );
					// wrap output
					final ByteBuffer buffer = ByteBuffer.wrap( output.getBytes() );
					// write it
					System.out.println( "<<<" + m_buffer );
					v_channel.write( buffer );
					// set complete
					if( v_state.equals( eState.DONEREAD ) )
						v_state = eState.COMPLETE;
					// else - stay in execption
					// were done - get out
					return;
				}

				// sleep a bit - await can write
				try {
					Thread.sleep( m_sleeptime );
				} catch( InterruptedException e ) {
					// were done - get out
					return;
				}
			}
		} catch( Exception e ) {
			System.err.println( e.toString() );
			// set error flag
			v_state = eState.EXCEPTION;
			// return error info in buffer
			m_buffer.delete( 0, m_buffer.length() );
			m_buffer.append( e.toString() );
		}
		System.out.println( "DriverProcessTCP6c.run:done" );
	}

	/// IMapBlockingQueueProcess

	@Override
	public int getKey() {
		return v_channel.hashCode();
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public String toString() {
		return m_buffer.toString();
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public void reset( final SocketChannel channel ) {
		// reset run vars
		v_channel = channel;
		v_state = eState.DEFAULT;
		v_canread = new AtomicBoolean( false );
		v_canwrite = new AtomicBoolean( false );
		m_future = null;
		m_buffer.delete( 0, m_buffer.length() );
	}

	public void read( final SocketChannel channel ) throws IllegalStateException, UnexpectedException {
		System.out.println( "DriverProcessTCP6c.read:" + v_state );
		// match channel
		if( !v_channel.equals( channel ) )
			throw new IllegalArgumentException( "Different channels" );
		// set can-read flag
		synchronized( v_canread ) {
			v_canread = new AtomicBoolean( true );
		}
	}

	public void write( final SocketChannel channel ) throws IllegalStateException, UnexpectedException {
		System.out.println( "DriverProcessTCP6c.write:" + v_state );
		// match channel
		if( !v_channel.equals( channel ) )
			throw new IllegalArgumentException( "Different channels" );
		// set can-write flag
		synchronized( v_canwrite ) {
			v_canwrite = new AtomicBoolean( true );
		}
	}

	public boolean isComplete() {
		synchronized( v_state ) {
			return v_state.equals( eState.COMPLETE ) || v_state.equals( eState.EXCEPTION );
		}
	}

	public boolean isInError() {
		synchronized( v_state ) {
			return v_state.equals( eState.EXCEPTION );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
