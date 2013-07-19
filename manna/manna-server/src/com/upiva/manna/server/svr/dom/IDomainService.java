/**
 * Authored By: IanF on 04/06/13 15:57
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 15:57: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.dom;

import com.upiva.common.dbo.Token;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XSessionException;
import org.w3c.dom.Node;

public interface IDomainService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	String getName();

	IDatabaseSession newDatabaseSession();

	String obtainSession( final String username );

	String appendSession( final String username, final String password, final String ipaddress ) throws XAccessException;

	String deleteSession( final String session ) throws XSessionException;

	Token obtainToken( final String session );

	String getConfigString( final String key );

	boolean getConfigBoolean( final String key );

	char getConfigChar( final String key );

	byte getConfigByte( final String key );

	short getConfigShort( final String key );

	int getConfigInt( final String key );

	long getConfigLong( final String key );

	float getConfigFloat( final String key );

	double getConfigDouble( final String key );

	Node getConfigNested( final String key );

}
