/**
 * Authored By: IanF on 03/06/13 21:49
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 03/06/13 21:49: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cmd;


import com.upiva.manna.server.exc.*;

public interface ICommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	String getCommand();

	void process( final ClientRequest request, final ClientResponse response ) throws XProcessException, XInterruptException, XTimeoutException, XThrowableException, XAccessException;

}
