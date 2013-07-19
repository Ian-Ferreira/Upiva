/**
 * Authored By: IanF on 02/07/13 10:17
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/07/13 10:17: Created, IanF, ...
 *
 */

package com.upiva.manna.server.drv.tcp6s;

import com.upiva.manna.server.exc.XDriverException;
import com.upiva.manna.server.svr.drv.AbstractDriverService;
import org.w3c.dom.Node;

public class DriverServiceTCP6s extends AbstractDriverService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DriverServiceTCP6s( final String name, final String qualifier, final String consumer, final Node context ) throws XDriverException {
		super( name, qualifier, consumer, context );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// Runnable

	@Override
	public void run() {
	}

	/// IDriverService

	@Override
	public void start() {
	}

	@Override
	public void stop() {
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

}
