/**
 * Authored By: IanF on 20/05/13 13:05
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 20/05/13 13:05: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.upiva.manna.server.svr.cmd.AbstractCommandService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.IManagerAccessor;
import com.upiva.manna.server.svr.dom.IDatabaseSession;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XProcessException;

public class CommandAvailableUsername extends AbstractCommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandAvailableUsername( final IManagerAccessor manager ) {
		super( manager );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected boolean doProcess( final IDatabaseSession database, final ClientRequest request, final ClientResponse response ) throws XAccessException, XProcessException {
		return false;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
