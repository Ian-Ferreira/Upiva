/**
 * Authored By: IanF on 28/06/13 18:56
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 28/06/13 18:56: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.utl;

import java.lang.reflect.Field;
import java.util.concurrent.*;

public class MappedThreadPoolExecutor<R extends IMapBlockingQueueProcess> extends ThreadPoolExecutor {

	///////////////////////////////////////////////////////////////////////////
	// Inners

	public interface IBeforeExecutionHandler {
		void beforeExecution( final Thread thread, final IMapBlockingQueueProcess process );
	}

	public interface IAfterExecutionHandler {
		void afterExecution( final IMapBlockingQueueProcess process, final Throwable t );
	}

	public interface IRejectedExecutionHandler {
		void rejectedExecution(  final IMapBlockingQueueProcess process, final ThreadPoolExecutor executor );
	}

	///////////////////////////////////////////////////////////////////////////
	// Constants

	// the 'map
	private final ConcurrentHashMap<Integer, IMapBlockingQueueProcess> m_hashmap;

	// handler events
	private final IBeforeExecutionHandler m_beforeHandler;
	private final IAfterExecutionHandler m_afterHandler;

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public MappedThreadPoolExecutor( final int corePoolSize, final int maximumPoolSize, final float factor, final int level, final long keepAliveTime, final TimeUnit unit, final BlockingQueue<Runnable> workQueue, final ThreadFactory threadFactory, final IBeforeExecutionHandler beforeHandler, final IAfterExecutionHandler afterHandler, final IRejectedExecutionHandler rejectedHandler ) {
		super( corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution( final Runnable runnable, final ThreadPoolExecutor executor ) {
				// fire client
				if( rejectedHandler == null )
					return;
				rejectedHandler.rejectedExecution( _extractProcess( runnable ), executor );
			}
		} );
		// preserve args
		m_beforeHandler = beforeHandler;
		m_afterHandler = afterHandler;
		// the 'map
		m_hashmap = new ConcurrentHashMap<>( corePoolSize, factor, level );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public Future<?> submit( final Runnable task ) {
		IMapBlockingQueueProcess process = ( IMapBlockingQueueProcess )task;
		// add to our map
		m_hashmap.put( process.getKey(), process );
		// call super
		return super.submit( task );
	}

	@Override
	protected void beforeExecute( final Thread thread, final Runnable runnable ) {
		// fire client
		if( m_beforeHandler == null )
			return;
		m_beforeHandler.beforeExecution( thread, _extractProcess( runnable ) );
	}

	@Override
	protected void afterExecute( final Runnable runnable, final Throwable throwable ) {
		// extract process out of runnable
		IMapBlockingQueueProcess process = _extractProcess( runnable );
		// un-map - ready for free store
		m_hashmap.remove( process.getKey(), process );
		// fire client
		if( m_afterHandler == null )
			return;
		m_afterHandler.afterExecution( process, throwable );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public R get( final int i ) {
		return ( R )m_hashmap.get( i );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Static helpers

	private static IMapBlockingQueueProcess _extractProcess( final Runnable runnable ) {
		try {
			final FutureTask<?> task = ( FutureTask<?> )runnable;
			final Field fld1 = task.getClass().getDeclaredField( "sync" );
			boolean hack = fld1.isAccessible();
			fld1.setAccessible( true );
			final Object sync = fld1.get( task );
			fld1.setAccessible( hack );
			final Field fld2 = sync.getClass().getDeclaredField( "callable" );
			hack = fld2.isAccessible();
			fld2.setAccessible( true );
			final Object clbl = fld2.get( sync );
			fld2.setAccessible( hack );
			final Field fld3 = clbl.getClass().getDeclaredField( "task" );
			hack = fld3.isAccessible();
			fld3.setAccessible( true );
			final IMapBlockingQueueProcess process = ( IMapBlockingQueueProcess )fld3.get( clbl );
			fld3.setAccessible( hack );
			return process;
		} catch( NoSuchFieldException | IllegalAccessException e ) {
			// this should not happen
			e.printStackTrace();
		}
		return null;
	}

}
