/**
 * Authored By: IanF on 05/06/13 21:47
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 05/06/13 21:47: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.drv;

public interface IDriverService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	void start();

	void stop();

	boolean isAlive();
}
