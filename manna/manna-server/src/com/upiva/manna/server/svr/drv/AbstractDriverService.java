/**
 * Authored By: IanF on 05/06/13 21:37
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 05/06/13 21:37: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.drv;

import com.upiva.manna.server.exc.XDriverException;
import com.upiva.manna.server.svr.qlf.IMessageQualifier;
import org.w3c.dom.Node;

public abstract class AbstractDriverService implements IDriverService, Runnable {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	protected final String p_name;
	protected final String p_consumer;
	protected final Node p_context;
	protected final IMessageQualifier p_qualifier;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	protected AbstractDriverService( final String name, final String qualifier, final String consumer, final Node context ) throws XDriverException {
		// preserve args
		p_name = name;
		p_consumer = consumer;
		p_context = context;
		// create qualifier
		try {
			Class<?> clss = Class.forName( qualifier );
			p_qualifier = ( IMessageQualifier )clss.newInstance();
		} catch( ClassNotFoundException | InstantiationException | IllegalAccessException e ) {
			throw new XDriverException( e.toString() );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	// concrete to implement run();

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
