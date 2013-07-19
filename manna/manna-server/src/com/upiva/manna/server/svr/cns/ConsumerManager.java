/**
 * Authored By: IanF on 09/06/13 16:02
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 09/06/13 16:02: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cns;

import com.upiva.manna.server.exc.XConsumerException;
import com.upiva.manna.server.exc.XInterruptException;
import com.upiva.manna.server.exc.XMaxpoolException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConsumerManager implements IConsumerManager {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final LinkedBlockingQueue<IConsumerService> m_freePool;
	private final LinkedBlockingQueue<IConsumerService> m_workPool;

	private final Class<? extends IConsumerService> m_clzz;
	private final long m_timeout;

	private long m_sequence = 0l;
	private Constructor<? extends IConsumerService> m_cons;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ConsumerManager( final String clazz, final int freeMax, final int workMax, final long timeout ) throws XConsumerException {
		// preserve args
		try {
			// preserve class
			m_clzz = ( Class<? extends IConsumerService> )Class.forName( clazz );
			// extract constructor
			m_cons = m_clzz.getConstructor( long.class );
		} catch( Exception e ) {
			throw new XConsumerException( String.format( "Illegal consumer class - %s", clazz ) );
		}
		m_timeout = timeout;
		// construct queues
		m_freePool = new LinkedBlockingQueue<>( freeMax );
		m_workPool = new LinkedBlockingQueue<>( workMax );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public IConsumerService acquireConsumer() throws XMaxpoolException, XConsumerException, XInterruptException {
		// free pool available
		final IConsumerService consumer;
		if( !m_freePool.isEmpty() ) {
			// extract from free poll
			try {
				consumer = m_freePool.take();
			} catch( InterruptedException e ) {
				throw new XInterruptException( "Freepool take interrupted" );
			}
			// add to work pool
			try {
				m_workPool.offer( consumer, m_timeout, TimeUnit.MILLISECONDS );
			} catch( InterruptedException e ) {
				throw new XInterruptException( "Workpool offer interrupted" );
			}
			// then return it
			return consumer;
		}
		// invoke constructor
		try {
			consumer = m_cons.newInstance( m_sequence++ );
			// add to work pool
			m_workPool.offer( consumer, m_timeout, TimeUnit.MILLISECONDS );
			// then return it
			return consumer;
		} catch( InstantiationException e ) {
			throw new XConsumerException( e.toString() );
		} catch( IllegalAccessException e ) {
			throw new XConsumerException( e.toString() );
		} catch( InvocationTargetException e ) {
			throw new XConsumerException( e.getCause().toString() );
		} catch( InterruptedException e ) {
			throw new XInterruptException( "Workpool offer interrupted" );
		}
	}

	public void disposeConsumer( final IConsumerService consumer ) {
		// extract from work pool
		m_workPool.remove( consumer );
		// add to free pool
		try {
			m_freePool.offer( consumer, m_timeout, TimeUnit.MILLISECONDS );
		} catch( InterruptedException e ) {
			// n/a
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
