/**
 * Authored By: IanF on 15/05/13 20:40
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 15/05/13 20:40: Created, $(USER), ...
 *
 */

package com.upiva.manna.game.utl.usr;

import com.upiva.common.utl.GEN;

import java.io.Serializable;

/*public*/ class UserTokenImpl implements IUserToken, Serializable {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_username;
	private final String m_password;
	private final long m_datetime;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	/*package*/ UserTokenImpl( final String username, final String password ) {
		// preserve args
		m_username = username;
		m_password = password;
		m_datetime = System.currentTimeMillis();
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public String getUsername() {
		return m_username;
	}

	@Override
	public String getPasswordHash() {
		return GEN.calcHash( m_password );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
