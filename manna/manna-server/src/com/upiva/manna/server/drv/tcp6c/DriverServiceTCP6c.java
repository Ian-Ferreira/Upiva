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

package com.upiva.manna.server.drv.tcp6c;

import com.upiva.common.utl.XmlDom;
import com.upiva.manna.server.exc.XDriverException;
import com.upiva.manna.server.svr.drv.AbstractDriverService;
import com.upiva.manna.server.svr.utl.IMapBlockingQueueProcess;
import com.upiva.manna.server.svr.utl.MappedThreadPoolExecutor;
import org.w3c.dom.Node;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.rmi.UnexpectedException;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class DriverServiceTCP6c extends AbstractDriverService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final ConcurrentLinkedQueue<DriverProcessTCP6c> m_freeQueue;
	private final ArrayBlockingQueue<DriverProcessTCP6c> m_workQueue;

	private final MappedThreadPoolExecutor<DriverProcessTCP6c> m_processExecutor;
	private final InetSocketAddress m_address;
	private final int m_hostport;
	private final String m_hostname;
	private final int m_poolmin;
	private final int m_poolmax;
	private final float m_factor;
	private final int m_level;
	private final long m_accepttime;
	private final int m_acptlimit;
	private final int m_readlimit;
	private final int m_wrtelimit;
	private final long m_sleeptime;
	private final long m_killtime;
	private final int m_rxbufsize;
	private final long m_keepalive;
	private final boolean m_fairness;

	private ExecutorService m_serviceExecutor = null;
	private Future<Boolean> m_stopped = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DriverServiceTCP6c( final String name, final String qualifier, final String consumer, final Node context ) throws XDriverException {
		super( name, qualifier, consumer, context );

		// extract config
		m_hostname = XmlDom.getValue( context, "hostname" );
		m_hostport = XmlDom.getInt( context, "hostport" );
		m_poolmin = XmlDom.getInt( context, "poolmin" );
		m_poolmax = XmlDom.getInt( context, "poolmax" );
		m_keepalive = XmlDom.getLong( context, "keepalive" );
		m_accepttime = XmlDom.getLong( context, "accepttime" );
		m_acptlimit = XmlDom.getInt( context, "acptlimit" );
		m_readlimit = XmlDom.getInt( context, "readlimit" );
		m_wrtelimit = XmlDom.getInt( context, "wrtelimit" );
		m_sleeptime = XmlDom.getLong( context, "sleeptime" );
		m_killtime = XmlDom.getLong( context, "killtime" );
		m_rxbufsize = XmlDom.getInt( context, "rxbufsize" );
		m_factor = XmlDom.getFloat( context, "loadfactor" );
		m_level = XmlDom.getInt( context, "concrtlevel" );
		m_fairness = XmlDom.getBoolean( context, "fairness" );

		// driver thread executioner
		m_serviceExecutor = Executors.newSingleThreadExecutor();

		// process thread group
		final ThreadGroup group = new ThreadGroup( String.format( "%s.ThreadGroup.%s", DriverServiceTCP6c.class.getSimpleName(), name ) );

		// process thread naming factory
		final ThreadFactory threadFactory = new ThreadFactory() {
			private AtomicLong counter = new AtomicLong( 0l );
			@Override
			public Thread newThread( final Runnable runnable ) {
				final Thread thread = new Thread( group, runnable );
				thread.setName( String.format( "%s.Thread.%s.%d", DriverServiceTCP6c.class.getSimpleName(), name, counter.incrementAndGet() ) );
				return thread;
			}
		};

		// before execute handler
		final MappedThreadPoolExecutor.IBeforeExecutionHandler beforeHandler = new MappedThreadPoolExecutor.IBeforeExecutionHandler() {
			@Override
			public void beforeExecution( final Thread thread, final IMapBlockingQueueProcess process ) {
				System.out.println( "DriverServiceTCP6c.beforeExecution" );
			}
		};

		// after execute handler
		final MappedThreadPoolExecutor.IAfterExecutionHandler afterHandler = new MappedThreadPoolExecutor.IAfterExecutionHandler() {
			@Override
			public void afterExecution( final IMapBlockingQueueProcess process, final Throwable t ) {
				System.out.println( "DriverServiceTCP6c.afterExecution" );
				// add to free pool - discard if full
				if( m_freeQueue.size() < m_poolmax )
					m_freeQueue.offer( ( DriverProcessTCP6c )process );
			}
		};

		// rejection handler
		final MappedThreadPoolExecutor.IRejectedExecutionHandler rejectedHandler = new MappedThreadPoolExecutor.IRejectedExecutionHandler() {
			@Override
			public void rejectedExecution( final IMapBlockingQueueProcess process, final ThreadPoolExecutor executor ) {
				System.out.println( "DriverServiceTCP6c.rejectedExecution" );
			}
		};

		// process pools
		m_freeQueue = new ConcurrentLinkedQueue<>();
		m_workQueue = new ArrayBlockingQueue<>( m_poolmax, m_fairness );

		// process executor
		m_processExecutor = new MappedThreadPoolExecutor( m_poolmin, m_poolmax, m_factor, m_level, m_keepalive, TimeUnit.MILLISECONDS, m_workQueue, threadFactory, beforeHandler, afterHandler, rejectedHandler );

		// channel binding address
		m_address = new InetSocketAddress( m_hostname, m_hostport );

	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public void run() {
		System.out.println( "DriverServiceTCP6c.run; started" );

		// our server
		ServerSocketChannel server = null;
		try {
			// open channel server
			server = ServerSocketChannel.open();

			// config server
			server.configureBlocking( false );
			server.bind( m_address );

			Selector selector = null;
			try {
				// run indefinite
				while( server.isOpen() ) {

					// no/bad selector - attempt create - exception catch outside while loop
					if( ( selector == null ) || !selector.isOpen() ) {
						// open selector
						selector = Selector.open();
						// register for accepts
						server.register( selector, SelectionKey.OP_ACCEPT );
					}

					try {
						// await event
						selector.select( m_accepttime );

						// loop action keys
						for( final Iterator iter = selector.selectedKeys().iterator(); iter.hasNext(); ) {

							// extract item
							final SelectionKey item = ( SelectionKey )iter.next();

							// remove key from current set
							iter.remove();

							// ? accept key
							if( item.isAcceptable() && item.isValid() ) {
								System.out.print( "+++Accept: " );
								// accept socket
								final SocketChannel channel = server.accept();
								// set non-blocking
								channel.configureBlocking( false );
								// ? available free pool
								DriverProcessTCP6c process = m_freeQueue.poll();
								// must create if pool empty
								if( process == null )
									process = new DriverProcessTCP6c( p_qualifier, p_consumer, m_rxbufsize, m_sleeptime );
								// reset process
								process.reset( channel );
								// let it run - await read
								m_processExecutor.submit( process );
								// register for read - will only be invoked on next iteration
								channel.register( selector, SelectionKey.OP_READ );
								// continue with next key
								//System.out.println( "" + channel.toString() );
								continue;
							}

							// ? read ready
							if( item.isReadable() && item.isValid() ) {
								// extract channel
								final SocketChannel channel = ( SocketChannel )item.channel();
								// extract process
								final DriverProcessTCP6c process = m_processExecutor.get( channel.hashCode() );
								// process to read channel data
								//System.out.print( ">>>Read: " + channel.toString() );
								process.read( channel );
								//System.out.println( ", " + process.toString() );
								// register for write - will only be invoked on next iteration
								item.channel().register( selector, SelectionKey.OP_WRITE );
								// continue with next key
								continue;
							}

							// ? write ready
							if( item.isWritable() && item.isValid() ) {
								// extract channel
								final SocketChannel channel = ( SocketChannel )item.channel();
								// extract process
								final DriverProcessTCP6c process = m_processExecutor.get( channel.hashCode() );
								// process stop due error
								if( process == null ) {
									// cancel key
									item.cancel();
									throw new NullPointerException( "Process null, run complete" );
								}
								// process to write channel data
								//System.out.print( "<<<Write: " + channel.toString() + ", " + process.toString() );
								process.write( channel );
								//System.out.println( " = " + process.toString() );
								// cancel key
								item.cancel();
							}
						} // for( actions )

					} catch( ClosedSelectorException e ) {
						System.err.println( "DriverServiceTCP6c.run; selector unexpectedly closed: " + e.toString() );
						// continue...
					} catch( IllegalStateException | UnexpectedException e ) {
						System.err.println( "DriverServiceTCP6c.run; process read or write failed: " + e.toString() );
						// continue...
					} catch( Exception e ) {
						System.err.println( "DriverServiceTCP6c.run; process exception: " + e.toString() );
						//e.printStackTrace();
						// continue...
					}

					// sleep a bit
					try {
						Thread.sleep( m_sleeptime );
					} catch( InterruptedException e ) {
						System.out.println( "DriverServiceTCP6c.run; interrupted" );
						// done with this run()
						break;
					}

				} // while( true )
			} catch( Exception e ) {
				System.err.println( "DriverServiceTCP6c.run; selector failed: " + e.toString() );
			} finally {
				if( selector != null ) {
					try {
						selector.close();
					} catch( IOException e ) {
						// n/a
					}
				}
			}
		} catch( Exception e ) {
			System.err.println( "DriverServiceTCP6c.run; server channel failed: " + e.toString() );
		} finally {
			if( server != null ) {
				try {
					server.close();
				} catch( IOException e ) {
					// n/a
				}
			}
		}

		System.out.println( "DriverServiceTCP6c.run; stopped" );
	}

	/// IDriverService

	@Override
	public void start() {
		// was executor stopped
		if( ( m_serviceExecutor == null ) || m_serviceExecutor.isShutdown() ) {
			m_serviceExecutor = Executors.newSingleThreadExecutor();
		}
		// submit task
		m_stopped = m_serviceExecutor.submit( this, true );
	}

	@Override
	public void stop() {
		// not already called
		if( !m_serviceExecutor.isShutdown() ) {
			System.out.println( this.getClass().getSimpleName() + "; shutting down after max-millisecs: " + m_killtime );
			// request shutdown
			m_serviceExecutor.shutdown();
			try {
				// block await death
				m_serviceExecutor.awaitTermination( m_killtime, TimeUnit.MILLISECONDS );
			} catch( InterruptedException e ) {
				// just cull it
				m_serviceExecutor.shutdownNow();
			}
		}
	}

	@Override
	public boolean isAlive() {
		try {
			return m_stopped.get();
		} catch( InterruptedException | ExecutionException e ) {
			return false;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
