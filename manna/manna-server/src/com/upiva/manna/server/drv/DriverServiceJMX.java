/**
 * Authored By: IanF on 05/06/13 22:07
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 05/06/13 22:07: Created, IanF, ...
 *
 */

package com.upiva.manna.server.drv;

import com.upiva.manna.server.exc.XDriverException;
import com.upiva.manna.server.svr.drv.AbstractDriverService;
import org.w3c.dom.Node;

public class DriverServiceJMX extends AbstractDriverService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DriverServiceJMX( final String name, final String qualifier, final String consumer, final Node context ) throws XDriverException {
		super( name, qualifier, consumer, context );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public void run() {
	}

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
