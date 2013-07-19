/**
 * Authored By: IanF on 18/05/13 09:11
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/05/13 09:11: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.db4o.ObjectContainer;
import com.upiva.common.dbo.GamerProfile;
import com.upiva.common.dbo.Token;
import com.upiva.manna.server.bus.db4o.Persona;
import com.upiva.manna.server.svr.cmd.AbstractCommandService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.IManagerAccessor;
import com.upiva.manna.server.svr.dom.IDatabaseSession;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XProcessException;

public class CommandLogonUser extends AbstractCommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandLogonUser( final IManagerAccessor manager ) {
		super( manager );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected boolean doProcess( final IDatabaseSession database, final ClientRequest request, final ClientResponse response ) throws XAccessException, XProcessException {
		System.out.println( ">>> " + request.toString() );

		// extract temp data for convenience
		final String username = request.getString( "username" );
		final String password = request.getString( "password" );

		// since 'LogonUser' has bypassed the session check, ask domain for active session for user & check passwords
		String session = p_manager.obtainSession( username );
		if( session != null ) {
			if( p_manager.getConfigBoolean( "transientAccess" ) ) {
				// return existing token
				response.add( p_manager.obtainToken( session ) );
				// extract profile
				GamerProfile profile = Accessor.getProfile( ( ObjectContainer )database, username );
				response.add( profile );
				( ( ObjectContainer )database ).commit();
				return true;
			} else {
				throw new XAccessException( "Already logged on" );
			}
		}

		// extract valid user
		Persona persona = Accessor.getPersona( ( ObjectContainer )database, username );
		if( persona == null )
			throw new XAccessException( "Unknown user" );

		// match password
		if( !persona.getPassword().equals( password ) )
			throw new XAccessException( "Access denied" );

		// check persona active
		if( !persona.isActive() )
			throw new XAccessException( persona.isDeseased() ? "User deceased" : "User suspended" );

		// feature: match user to shifts and rosters

		// install our session into the domain
		session = p_manager.appendSession( username, password, this.getSession() );

		// extract token - only sessions can create tokens
		final Token token = p_manager.obtainToken( session );
		response.add( token );

		// todo store user-extra payload for user, or do something with it

		// extract profile
		GamerProfile profile = Accessor.getProfile( ( ObjectContainer )database, username );
		if( profile == null )
			profile = Accessor.setProfile( ( ObjectContainer )database, username, p_manager.getConfigLong( "newGamerCoinsGrant" ), new int[] { 1, 1, 1, } );
		response.add( profile );

		// commit db
		( ( ObjectContainer )database ).commit();

		// return success
		return true;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
