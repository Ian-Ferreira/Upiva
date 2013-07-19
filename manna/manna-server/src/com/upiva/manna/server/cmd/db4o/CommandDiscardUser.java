/**
 * Authored By: IanF on 18/05/13 09:21
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/05/13 09:21: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.upiva.manna.server.bus.db4o.DiscardReason;
import com.upiva.manna.server.bus.db4o.Persona;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XProcessException;
import com.upiva.manna.server.exc.XSessionException;
import com.upiva.manna.server.svr.cmd.AbstractCommandService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.IManagerAccessor;
import com.upiva.manna.server.svr.dom.IDatabaseSession;

import java.util.List;

public class CommandDiscardUser extends AbstractCommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandDiscardUser( final IManagerAccessor manager ) {
		super( manager );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected boolean doProcess( final IDatabaseSession database, final ClientRequest request, final ClientResponse response ) throws XAccessException, XProcessException {
		System.out.println( ">>> " + request.toString() );

		// cast common db session to db4o type
		final ObjectContainer db = ( ObjectContainer )database;

		// extract temp data for convenience
		final String username = request.getString( "username" );
		final String reason = request.getString( "reason" );

		// user exist ot have
		if( reason == null )
			throw new XProcessException( "Discard user reason required" );

		// domain to remove session -if logged-on
		final String deleted = p_manager.obtainSession( username );
		if( deleted != null ) {
			try {
				p_manager.deleteSession( this.getSession() );
			} catch( XSessionException e ) {
				// should not happen
			}
		}

		// extract persona from db
		final List<Persona> personas = ( ( ObjectContainer )database ).query( new Predicate<Persona>() {
			@Override
			public boolean match( final Persona persona ) {
				if( persona.getUsername().equals( username ) )
					return true;
				return false;
			}
		});
		if( personas.size() < 1 )
			throw new XAccessException( "Unknown user" );

		// update db
		personas.get( 0 ).setSuspended();

		// create new discard reason record
		final DiscardReason discard = new DiscardReason( personas.get( 0 ), reason );
		( ( ObjectContainer )database ).store( discard );

		// prep response
		response.add( username + " discarded" );

		// commit db
		db.commit();
		//List<EnrollPending> list = db.query( EnrollPending.class );

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
