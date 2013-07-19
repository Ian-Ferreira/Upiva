/**
 * Authored By: IanF on 17/06/13 04:49
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 17/06/13 04:49: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.db4o.ObjectContainer;
import com.db4o.query.Predicate;
import com.upiva.common.dbo.GamerProfile;
import com.upiva.manna.server.bus.db4o.EnrollPending;
import com.upiva.manna.server.bus.db4o.Persona;

import java.util.List;

/*public*/ class Accessor {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static Persona getPersona( final ObjectContainer database, final String username ) {
		List<Persona> list = database.query( new Predicate<Persona>() {
			@Override
			public boolean match( final Persona matcher ) {
				if( matcher.getUsername().equals( username ) )
					return true;
				return false;
			}
		} );
		return ( list.size() > 0 ) ? list.get( 0 ) : null;
	}

	public static Persona addPersona( final ObjectContainer database, final EnrollPending pending ) {
		final Persona persona = new Persona( pending.getUsername(), pending.getPassword(), pending.getEmail() );
		database.store( persona );
		return persona;
	}

	public static EnrollPending getEnrollPending( final ObjectContainer database, final String username ) {
		List<EnrollPending> list = database.query( new Predicate<EnrollPending>() {
			@Override
			public boolean match( final EnrollPending matcher ) {
				if( matcher.getUsername().equals( username ) )
					return true;
				return false;
			}
		} );
		return ( list.size() > 0 ) ? list.get( 0 ) : null;
	}

	public static void addEnrollPending( final ObjectContainer database, final String username, final String password, final String email, final String pincode ) {
		database.store( new EnrollPending( username, password, email, pincode ) );
	}

	public static void delEnrollPending( final ObjectContainer database, final EnrollPending pending ) {
		database.delete( pending );
	}

	public static GamerProfile getProfile( final ObjectContainer database, final String username ) {
		List<GamerProfile> list = database.query( new Predicate<GamerProfile>() {
			@Override
			public boolean match( final GamerProfile matcher ) {
				if( matcher.getUsername().equals( username ))
					return true;
				return false;
			}
		} );
		return ( list.size() > 0 ) ? list.get( 0 ) : null;
	}

	public static GamerProfile setProfile( final ObjectContainer database, final String username, final long coins, final int[] levels ) {
		GamerProfile item = Accessor.getProfile( database, username );
		if( item != null ) {
			item.setCoins( coins );
			item.setLevels( levels );
		} else
			item = new GamerProfile( username, coins, levels );
		database.store( item );
		return item;
	}

}
