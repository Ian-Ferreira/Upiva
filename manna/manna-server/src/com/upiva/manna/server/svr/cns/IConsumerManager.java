/**
 * Authored By: IanF on 11/06/13 06:38
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 11/06/13 06:38: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cns;

import com.upiva.manna.server.exc.XConsumerException;
import com.upiva.manna.server.exc.XInterruptException;
import com.upiva.manna.server.exc.XMaxpoolException;

public interface IConsumerManager {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	IConsumerService acquireConsumer() throws XConsumerException, XInterruptException, XMaxpoolException;

	void disposeConsumer( final IConsumerService consumer );
}
