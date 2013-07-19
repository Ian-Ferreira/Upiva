/**
 * Authored By: IanF on 11/06/13 19:38
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 11/06/13 19:38: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cmd;

import com.upiva.manna.server.exc.XCommandException;

public interface ICommandManager {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	ICommandService acquireCommand( final String command, final String session ) throws XCommandException;

	void disposeCommand( final ICommandService command );
}
