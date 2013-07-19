/**
 * Authored By: IanF on 18/05/13 09:18
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/05/13 09:18: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.db4o.ObjectContainer;
import com.upiva.common.dbo.GamerProfile;
import com.upiva.common.dbo.Token;
import com.upiva.manna.server.bus.db4o.EnrollPending;
import com.upiva.manna.server.bus.db4o.Persona;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XProcessException;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.cmd.AbstractCommandService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.IManagerAccessor;
import com.upiva.manna.server.svr.dom.IDatabaseSession;

public class CommandEnrollUser extends AbstractCommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandEnrollUser( final IManagerAccessor manager ) {
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
		final String email = request.getString( "email" );

		// check username
		if( ( username == null ) || ( username.length() < p_manager.getConfigInt( "usernameMinSize" ) ) )
			throw new XAccessException( "Username minimum size - " + p_manager.getConfigInt( "usernameMinSize" ) );
		if( ( username.length() > p_manager.getConfigInt( "usernameMaxSize" ) ) )
			throw new XAccessException( "Username maximum size - " + p_manager.getConfigInt( "usernameMaxSize" ) );

		// check password
		if( ( password == null ) || ( password.length() < p_manager.getConfigInt( "passwordMinSize" ) ) )
			throw new XAccessException( "Password minimum size - " + p_manager.getConfigInt( "passwordMinSize" ) );
		if( ( password.length() > p_manager.getConfigInt( "passwordMaxSize" ) ) )
			throw new XAccessException( "Password maximum size - " + p_manager.getConfigInt( "passwordMaxSize" ) );

		// temp: check email
		if( ( email == null ) || email.isEmpty() || ( email.indexOf( "@" ) < 0 ) || ( email.indexOf( ".", email.indexOf( "@" ) ) < 0 ) )
			throw new XAccessException( "Email not valid" );

		// test existing user - username must be unique
		Persona persona = Accessor.getPersona( ( ObjectContainer )database, username );
		if( persona != null )
			throw new XAccessException( "Username unavailable" );

		// use enroll confirmations
		if( p_manager.getConfigBoolean( "useEnrollConfirmation" ) ) {

			// also test pending enrolls
			EnrollPending pending = Accessor.getEnrollPending( ( ObjectContainer )database, username );
			if( pending != null )
				throw new XAccessException( "Enroll confirmation pending" );

			// create enroll pending record
			final int multiplier = p_manager.getConfigInt( "enrollPincodeMultiplier" );
			String pincode = "";
			final String pintemp = p_manager.getConfigString( "debugBypassConfirmPincode" );
			if( !pintemp.equals( "-1" ) ) {
				do {
					pincode = String.valueOf( ( ( int )( Math.random() * multiplier ) ) );
				} while( pincode.equals( pintemp ) );
			}
			Accessor.addEnrollPending( ( ObjectContainer )database, username, password, email, pincode );

			// post the pincode to user
			Broker.postMessage( "MannaMail1", String.format( "<%s> please use this pincode '%s' to confirm your enrollment at <%s>", username, pincode, request.getDomain() ) );

			// use temp user sessions
			if( p_manager.getConfigBoolean( "grantEnrollAccess" ) ) {
				// install our session into the domain
				String session = p_manager.appendSession( username, password, this.getSession() );

				// extract token - only sessions can create tokens
				final Token token = p_manager.obtainToken( session );
				response.add( token );

				// extract profile
				GamerProfile profile = Accessor.getProfile( ( ObjectContainer )database, username );
				if( profile == null )
					profile = Accessor.setProfile( ( ObjectContainer )database, username, p_manager.getConfigLong( "newGamerCoinsGrant" ), new int[] { 1, 1, 1, } );
				response.add( profile );
			} else {
				response.add( "Enroll accepted, confirmation required" );
			}
		} else {
			// use temp user sessions
			if( p_manager.getConfigBoolean( "grantEnrollAccess" ) ) {
				// install our session into the domain
				String session = p_manager.appendSession( username, password, this.getSession() );

				// extract token - only sessions can create tokens
				final Token token = p_manager.obtainToken( session );
				response.add( token );

				// extract profile
				GamerProfile profile = Accessor.getProfile( ( ObjectContainer )database, username );
				if( profile == null )
					profile = Accessor.setProfile( ( ObjectContainer )database, username, p_manager.getConfigLong( "newGamerCoinsGrant" ), new int[] { 1, 1, 1, } );
				response.add( profile );
			} else {
				response.add( "Enroll accepted, logon required" );
			}
		}

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
