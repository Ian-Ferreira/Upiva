/**
 * Authored By: IanF on 12/06/13 17:53
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 12/06/13 17:53: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cmd;

import com.upiva.common.dbo.Token;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XInterruptException;
import com.upiva.manna.server.exc.XProcessException;
import com.upiva.manna.server.exc.XSessionException;
import com.upiva.manna.server.svr.dom.IDatabaseSession;
import org.w3c.dom.Node;

import java.util.concurrent.Future;

public interface IManagerAccessor {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	//// accessors from within the command or base

	IDatabaseSession newDatabase();

	String getDomain();

	long getTimeout();

	Future<Boolean> execute( final AbstractCommandService service );

	Boolean complete( final Future<Boolean> future, final long timeout ) throws XInterruptException, XProcessException;

	String obtainSession( final String username );

	String appendSession( final String username, final String password, final String ipaddress ) throws XAccessException;

	String deleteSession( final String session ) throws XSessionException;

	Token obtainToken( final String session );

	/// xml config accessors

	String getConfigString( final String key);

	boolean getConfigBoolean( final String key);

	char getConfigChar( final String key);

	byte getConfigByte( final String key);

	short getConfigShort( final String key);

	int getConfigInt( final String key);

	long getConfigLong( final String key);

	float getConfigFloat( final String key);

	double getConfigDouble( final String key);

	Node getConfigNested( final String key);

}
