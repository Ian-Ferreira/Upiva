/**
 * Authored By: IanF on 16/05/13 20:09
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 16/05/13 20:09: Created, IanF, ...
 *
 */

package com.upiva.common.dbo;

import java.io.Serializable;

public class Token implements Serializable {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_session;
	private final String m_username;
	private final String m_email;
	private final long m_rights;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	// default blank constructor - client must inject from json - must have
	public Token() {
		m_session = null;
		m_username = null;
		m_email = null;
		m_rights = 0l;
	}

	// called by session only
	public Token( final String session, final String username, final String email ) {
		m_session = session;
		m_username = username;
		m_email = email;
		m_rights = UserRights.GAMER.getMask();
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getSession() {
		return m_session;
	}

	public String getUsername() {
		return m_username;
	}

	public String getEmail() {
		return m_email;
	}

	public long getRights() {
		return m_rights;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
