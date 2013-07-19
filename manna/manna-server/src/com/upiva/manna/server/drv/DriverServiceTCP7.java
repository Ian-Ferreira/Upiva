/**
 * Authored By: IanF on 04/06/13 15:48
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 15:48: Created, IanF, ...
 *
 */

package com.upiva.manna.server.drv;

import com.upiva.common.utl.XmlDom;
import com.upiva.manna.server.exc.*;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.cns.IConsumerService;
import com.upiva.manna.server.svr.drv.AbstractDriverService;
import org.w3c.dom.Node;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketOption;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class DriverServiceTCP7 extends AbstractDriverService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final AsynchronousChannelGroup m_group;
	private final InetSocketAddress m_address;
	private final int m_solinger;
	private final int m_toservice;
	private final int m_sorvcbuf;
	private final int m_sosndbuf;
	private final int m_backlog;
	private final long m_sleeptime;
	private final long m_killtime;
	private final int m_rxbufsize;
	private final long m_rxtimeout;
	private final long m_txtimeout;
	private final boolean m_nodelay;
	private final boolean m_broadcast;
	private final boolean m_keepalive;
	private final boolean m_reuseaddr;

	private volatile boolean m_running = true;
	private ExecutorService m_executor = null;

	// floater
	//private Future<AsynchronousSocketChannel> m_future = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DriverServiceTCP7( final String name, final String qualifier, final String consumer, final Node context ) throws XDriverException {
		super( name, qualifier, consumer, context );

		// extract config
		final String hostname = XmlDom.getValue( context, "hostname" );
		final int hostport = XmlDom.getInt( context, "hostport" );
		final int poolsize = XmlDom.getInt( context, "poolsize" );
		m_solinger = XmlDom.getInt( context, "solinger" );
		m_toservice = XmlDom.getInt( context, "toservice" );
		m_sorvcbuf = XmlDom.getInt( context, "sorvcbuf" );
		m_sosndbuf = XmlDom.getInt( context, "sosndbuf" );
		m_backlog = XmlDom.getInt( context, "backlog" );
		m_rxbufsize = XmlDom.getInt( context, "rxbufsize" );
		m_sleeptime = XmlDom.getLong( context, "sleeptime" );
		m_killtime = XmlDom.getLong( context, "killtime" );
		m_rxtimeout = XmlDom.getLong( context, "rxtimeout" );
		m_txtimeout = XmlDom.getLong( context, "txtimeout" );
		m_nodelay = XmlDom.getBoolean( context, "nodelay" );
		m_broadcast = XmlDom.getBoolean( context, "broadcast" );
		m_keepalive = XmlDom.getBoolean( context, "keepalive" );
		m_reuseaddr = XmlDom.getBoolean( context, "reuseaddr" );

		// our naming factory
		final ThreadFactory threadFactory = new ThreadFactory() {
			private AtomicLong counter = new AtomicLong( 0l );

			@Override
			public Thread newThread( final Runnable runnable ) {
				final Thread thread = new Thread( runnable );
				thread.setName( String.format( "%s.Thread.%d", DriverServiceTCP7.class.getSimpleName(), counter.incrementAndGet() ) );
				return thread;
			}
		};

		try {
			// channel threads pools
			final ExecutorService executor;
			switch( poolsize ) {
				case -1:
					executor = Executors.newCachedThreadPool( threadFactory );
					break;
				case 0:
					executor = Executors.newSingleThreadExecutor( threadFactory );
					break;
				default:
					executor = Executors.newFixedThreadPool( poolsize, threadFactory );
					break;
			}

			// channel threads group
			m_group = AsynchronousChannelGroup.withThreadPool( executor );

			// channel binding address
			m_address = new InetSocketAddress( hostname, hostport );

		} catch( IOException e ) {
			throw new XDriverException( e.toString() );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public void run() {
		System.out.println( "DriverServiceTCP7.call: started" );

		// twr channel
		try( final AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open( m_group ) ) {

			// configure channel
			final Iterator<SocketOption<?>> iter = serverChannel.supportedOptions().iterator();
			while( iter.hasNext() ) {
				final SocketOption<?> item = iter.next();
				// switch don't work here
				if( item.name().equals( StandardSocketOptions.SO_RCVBUF.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.SO_RCVBUF, m_sorvcbuf );
				} else if( item.name().equals( StandardSocketOptions.SO_SNDBUF.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.SO_SNDBUF, m_sosndbuf );
				} else if( item.name().equals( StandardSocketOptions.IP_TOS.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.IP_TOS, m_toservice );
				} else if( item.name().equals( StandardSocketOptions.SO_LINGER.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.SO_LINGER, m_solinger );
				} else if( item.name().equals( StandardSocketOptions.TCP_NODELAY.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.TCP_NODELAY, m_nodelay );
				} else if( item.name().equals( StandardSocketOptions.SO_BROADCAST.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.SO_BROADCAST, m_broadcast );
				} else if( item.name().equals( StandardSocketOptions.SO_KEEPALIVE.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.SO_KEEPALIVE, m_keepalive );
				} else if( item.name().equals( StandardSocketOptions.SO_REUSEADDR.name() ) ) {
					serverChannel.setOption( StandardSocketOptions.SO_REUSEADDR, m_reuseaddr );
				}
			}

			// bind to hostport
			serverChannel.bind( m_address, m_backlog );

			// future to use
			Future<AsynchronousSocketChannel> future = null;
			boolean pending = false;

			// run until stopped
			while( m_running ) {
				// has channel data
				try {
					future = serverChannel.accept();
					System.err.print( "+" );
				} catch( AcceptPendingException e ) {
					// n/a
					System.err.print( "." );
					pending = true;
				}

				// ? pending - try read
				if( pending ) {
					try( final AsynchronousSocketChannel socketChannel = future.get( m_rxtimeout, TimeUnit.MILLISECONDS ) ) {
						// got data here
						//System.out.println( "\nIncoming connection from: " + socketChannel.getRemoteAddress() );

						// record time
						final long t0 = System.nanoTime();

						// read request
						final ByteBuffer rxBuffer = ByteBuffer.allocateDirect( m_rxbufsize );
						Future<Integer> futureRx = socketChannel.read( rxBuffer );

						// await result
						int result = futureRx.get( m_rxtimeout, TimeUnit.MILLISECONDS );
						if( result == - 1 )
							throw new UnsupportedOperationException( "Socket channel read failure: -1" );

						// extract data
						rxBuffer.flip();
						final String input = Charset.defaultCharset().decode( rxBuffer ).toString();
						//System.out.println( "Msg received from the client : " + input );

						// process message
						String output = this._process( input );

						// write response
						final ByteBuffer txBuffer = ByteBuffer.wrap( output.getBytes() );
						socketChannel.write( txBuffer ).get();

						// reset pending
						pending = false;

						// report time
						final long t1 = System.nanoTime();
						//System.out.printf( "Delta-nano's: %d\n", t1 - t0 );

					} catch( ExecutionException e ) {
						System.err.println( e.getCause().toString() );
					} catch( UnsupportedOperationException e ) {
						System.err.println( e.toString() );
					} catch( TimeoutException e ) {
						System.err.print( "-" );
						// reset pending
						pending = false;
					} catch( InterruptedException e ) {
						// we are stopped
						m_running = false;
						break;
					}
				}

				// give other threads some chance
				try {
					Thread.sleep( m_sleeptime );
				} catch( InterruptedException e ) {
					// we are stopped
					m_running = false;
					break;
				}

			} // end while

		} // try open channel
		catch( IOException e ) {
			e.printStackTrace();
		}

		// Void callable
		System.out.println( "DriverServiceTCP7.call: stopped" );
	}

	/// IDriverService

	@Override
	public void start() {
		// was executor stopped
		if( ( m_executor == null ) || m_executor.isShutdown() ) {
			m_executor = Executors.newSingleThreadExecutor();
		}
		// volatile flag
		m_running = true;
		// submit task
		m_executor.submit( this );
	}

	@Override
	public void stop() {
		// volatile flag
		m_running = false;
		// not already called
		if( !m_executor.isShutdown() ) {
			// request shutdown
			m_executor.shutdown();
			try {
				// block await death
				m_executor.awaitTermination( m_killtime, TimeUnit.MILLISECONDS );
			} catch( InterruptedException e ) {
				// just cull it
				m_executor.shutdownNow();
			}
		}
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private String _process( final String input ) {
		// broker to provide new consumer
		IConsumerService consumer = null;
		try {
			// acquire consumer
			consumer = Broker.acquireConsumer( p_consumer );

			// consumer to process request
			final String output = consumer.process( input );

			// return output;
			return output;
		} catch( XProcessException e ) {
			return e.getCause().toString();
		} catch( XAccessException | XTimeoutException | XDomainException | XThrowableException | XCommandException |
				XMaxpoolException | XConsumerException | XInterruptException | XParsingException e ) {
			return e.toString();
		} finally {
			// always dispose consumer
			if( consumer != null ) {
				Broker.disposeConsumer( consumer );
			}
		}
	}

}
