/**
 * Authored By: IanF on 11/06/13 05:53
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 11/06/13 05:53: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.dom;

import com.upiva.manna.server.svr.cmd.ICommandService;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XCommandException;

public interface IDomainManager {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	ICommandService accesorCommand( String command, String session ) throws XAccessException, XCommandException;

	ICommandService acquireCommand( String command, String session ) throws XAccessException, XCommandException;

	void disposeCommand( ICommandService command );

}
