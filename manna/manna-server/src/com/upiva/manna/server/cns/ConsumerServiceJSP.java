/**
 * Authored By: IanF on 03/06/13 22:24
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 03/06/13 22:24: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cns;

import com.upiva.manna.server.svr.cns.AbstractConsumerService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.exc.XParsingException;

public class ConsumerServiceJSP extends AbstractConsumerService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ConsumerServiceJSP( final long sequence ) {
		super( sequence );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected ClientRequest doParseRequest( final String request ) throws XParsingException {
		return null;
	}

	@Override
	protected String doPrintResponse( final ClientResponse response ) {
		return null;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}