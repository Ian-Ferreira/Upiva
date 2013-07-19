/**
 * Authored By: IanF on 02/06/13 12:36
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 12:36: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cns;

import com.upiva.manna.server.exc.*;

public interface IConsumerService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	long getSequence();

	String process( final String request ) throws XParsingException, XDomainException, XAccessException, XCommandException, XProcessException, XTimeoutException, XThrowableException, XInterruptException;

}
