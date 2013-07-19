/**
 * Authored By: IanF on 24/05/13 18:59
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 24/05/13 18:59: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.db4o.ObjectContainer;
import com.upiva.common.dbo.Token;
import com.upiva.manna.server.bus.db4o.EnrollPending;
import com.upiva.manna.server.bus.db4o.Persona;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.cmd.AbstractCommandService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.IManagerAccessor;
import com.upiva.manna.server.svr.dom.IDatabaseSession;

public class CommandConfirmEnroll extends AbstractCommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandConfirmEnroll( final IManagerAccessor manager ) {
		super( manager );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected boolean doProcess( final IDatabaseSession database, final ClientRequest request, final ClientResponse response ) throws XAccessException {
		System.out.println( ">>> " + request.toString() );

		// extract temp data for convenience
		final String username = request.getString( "username" );
		final String password = request.getString( "password" );
		final String pincode = request.getString( "pincode" );

		// check pending enroll
		EnrollPending pending = Accessor.getEnrollPending( ( ObjectContainer )database, username );
		if( pending == null )
			throw new XAccessException( "Unknown user" );

		if( Broker.getConfig().isDebugMode() && !p_manager.getConfigString( "debugBypassConfirmPincode" ).isEmpty() ) {
			// check valid pin and password
			if( !p_manager.getConfigString( "debugBypassConfirmPincode" ).equals( pincode ) )
				throw new XAccessException( "Invalid pincode" );
		} else {
			// check valid pin and password
			if( !pending.match( username, password, pincode ) )
				throw new XAccessException( "Invalid pincode" );
		}

		// check expired
		if( ( pending.getCreated() / 1000l / 60l / 60l / 24l ) > p_manager.getConfigInt( "enrollPendingExpireDays" ) )
			throw new XAccessException( "Enroll confirmation expired" );

		// remove record so user can re-enroll
		Accessor.delEnrollPending( ( ObjectContainer )database, pending );

		// feature: match against barred list again, might be barred on first session

		// create new persona
		final Persona persona = Accessor.addPersona( ( ObjectContainer )database, pending );

		// user must re-logon
		if( p_manager.getConfigBoolean( "grantConfirmAccess" ) ) {
			// install our session into the domain
			String session = p_manager.appendSession( username, password, this.getSession() );

			// extract token - only sessions can create tokens
			final Token token = p_manager.obtainToken( session );
			response.add( token );
		} else {
			response.add( "Confirmed, please logon" );
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
