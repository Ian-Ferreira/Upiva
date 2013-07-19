/**
 * Authored By: IanF on 02/06/13 13:04
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 13:04: Created, IanF, ...
 *
 */

package com.upiva.manna.server;

import com.upiva.manna.server.svr.ServerConfig;

public interface IMannaServer {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	String getHomePath();

	ServerConfig getConfig();

	void shutdown();
}
