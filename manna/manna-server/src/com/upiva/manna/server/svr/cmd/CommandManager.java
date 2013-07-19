/**
 * Authored By: IanF on 11/06/13 19:38
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 11/06/13 19:38: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cmd;

import com.upiva.common.dbo.Token;
import com.upiva.manna.server.exc.*;
import com.upiva.manna.server.svr.dom.IDatabaseSession;
import com.upiva.manna.server.svr.dom.IDomainService;
import org.w3c.dom.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.*;

public class CommandManager implements ICommandManager, IManagerAccessor {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final LinkedBlockingQueue<ICommandService> m_commands;

	private final IDomainService m_domain;
	private final long m_timeout;
	private final ExecutorService m_executor;
	private final Constructor<?> m_constructor;
	private final Field m_field;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandManager( final IDomainService domain, final String command, final String cmdpath, final int poolmax, final long timeout ) throws XCommandException {
		// preserve args
		m_domain = domain;
		m_timeout = timeout;

		// commands free store
		m_commands = new LinkedBlockingQueue<ICommandService>( poolmax );

		// m_executor
		m_executor = Executors.newFixedThreadPool( poolmax );

		// command constructor
		final String clzz = String.format( "%s.Command%s", cmdpath, command );
		try {
			// extract class
			final Class<?> clss = Class.forName( clzz );
			// extract constructor
			m_constructor = clss.getConstructor( IManagerAccessor.class );
			// extract field - we know its in the base class
			m_field = clss.getSuperclass().getDeclaredField( "m_session" );
		} catch( Exception e ) {
			throw new XCommandException( String.format( "Invalid command object - %s", clzz ) );
		}

		// session field

	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// ICommandManager


	@Override
	public String getDomain() {
		return m_domain.getName();
	}

	@Override
	public ICommandService acquireCommand( final String command, final String session ) throws XCommandException {
		// check free store
		ICommandService service = null;
		if( m_commands.peek() != null ) {
			try {
				// extract head
				service = m_commands.take();
			} catch( InterruptedException e ) {
				e.printStackTrace(); // now what?
			}
		}
		// create new
		if( service == null ) {
			try {
				service = ( ICommandService )m_constructor.newInstance( this );
			} catch( Exception e ) {
				throw new XCommandException( String.format( "Corrupt command object - %s", e.getCause().toString() ) );
			}
		}
		// inject new session
		try {
			final boolean hack = m_field.isAccessible();
			m_field.setAccessible( true );
			m_field.set( service, session );
			m_field.setAccessible( hack );
		} catch( IllegalAccessException e ) {
			// n/a
		}
		// return command
		return service;
	}

	@Override
	public void disposeCommand( final ICommandService command ) {
		// append to free store
		try {
			m_commands.offer( command, m_timeout, TimeUnit.MILLISECONDS );
		} catch( InterruptedException e ) {
			e.printStackTrace(); // not much we can do - free store is full
		}
	}

	/// IManagerAccessor

	@Override
	public IDatabaseSession newDatabase() {
		return m_domain.newDatabaseSession();
	}

	@Override
	public long getTimeout() {
		return m_timeout;
	}

	@Override
	public Future<Boolean> execute( final AbstractCommandService service ) {
		return m_executor.submit( service );
	}

	@Override
	public Boolean complete( final Future<Boolean> future, final long timeout ) throws XInterruptException, XProcessException {
		try {
			return future.get();
		} catch( InterruptedException e ) {
			throw new XInterruptException( "Command service interrupted" );
		} catch( ExecutionException e ) {
			throw new XProcessException( e.getCause().toString() );
		}
	}

	@Override
	public String obtainSession( final String username ) {
		return m_domain.obtainSession( username );
	}

	@Override
	public String appendSession( final String username, final String password, final String ipaddress ) throws XAccessException {
		return m_domain.appendSession( username, password, ipaddress );
	}

	@Override
	public String deleteSession( final String session ) throws XSessionException {
		return m_domain.deleteSession( session );
	}

	@Override
	public Token obtainToken( final String session ) {
		return m_domain.obtainToken( session );
	}

	@Override
	public String getConfigString( final String key ) {
		return m_domain.getConfigString( key );
	}

	@Override
	public boolean getConfigBoolean( final String key ) {
		return m_domain.getConfigBoolean( key );
	}

	@Override
	public char getConfigChar( final String key ) {
		return m_domain.getConfigChar( key );
	}

	@Override
	public byte getConfigByte( final String key ) {
		return m_domain.getConfigByte( key );
	}

	@Override
	public short getConfigShort( final String key ) {
		return m_domain.getConfigShort( key );
	}

	@Override
	public int getConfigInt( final String key ) {
		return m_domain.getConfigInt( key );
	}

	@Override
	public long getConfigLong( final String key ) {
		return m_domain.getConfigLong( key );
	}

	@Override
	public float getConfigFloat( final String key ) {
		return m_domain.getConfigFloat( key );
	}

	@Override
	public double getConfigDouble( final String key ) {
		return m_domain.getConfigDouble( key );
	}

	@Override
	public Node getConfigNested( final String key ) {
		return m_domain.getConfigNested( key );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
