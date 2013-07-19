/**
 * Authored By: IanF on 04/06/13 11:02
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 11:02: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cmd;

import com.upiva.manna.server.exc.*;
import com.upiva.manna.server.svr.dom.IDatabaseSession;

import java.util.concurrent.*;

public abstract class AbstractCommandService implements ICommandService, Callable<Boolean> {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	// our manager
	protected final IManagerAccessor p_manager;

	// injected valid session key
	private String m_session = null;

	// temporary - passing request to call()
	private ClientRequest m_request = null;
	private ClientResponse m_response = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public AbstractCommandService( final IManagerAccessor manager ) {
		// preserve args
		p_manager = manager;
	}

	///////////////////////////////////////////////////////////////////////////
	// Enforced

	protected abstract boolean doProcess( final IDatabaseSession database, final ClientRequest request, final ClientResponse response ) throws XAccessException, XProcessException, XSessionException;

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// ICommandService

	@Override
	public String getCommand() {
		return this.getClass().getSimpleName().substring( "Command".length() );
	}

	@Override
	public final void process( final ClientRequest request, final ClientResponse response ) throws XInterruptException, XTimeoutException, XThrowableException, XProcessException, XAccessException {
		// preserve request for thread call() method
		m_request = request;
		m_response = response;

		// submit callable for execution
		final Future<Boolean> future = p_manager.execute( this );

		// extract configs
		final long timeout = p_manager.getTimeout();

		// await thread finish or throw timeout exception
		p_manager.complete( future, timeout );
	}

	/// Callable

	@Override
	public final Boolean call() throws Exception {
		// obtain db session
		final IDatabaseSession database = p_manager.newDatabase();
		try {
			return this.doProcess( database, m_request, m_response );
		} finally {
			database.close();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getSession() {
		return m_session;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
