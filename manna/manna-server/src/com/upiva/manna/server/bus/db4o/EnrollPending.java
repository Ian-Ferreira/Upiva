/**
 * Authored By: IanF on 25/05/13 18:15
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 25/05/13 18:15: Created, IanF, ...
 *
 */

package com.upiva.manna.server.bus.db4o;

public class EnrollPending {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final long m_created;
	private final String m_username;
	private final String m_password;
	private final String m_email;
	private final String m_pincode;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public EnrollPending( final String username, final String password, final String email, final String pincode ) {
		m_created  = System.currentTimeMillis();
		m_username = username;
		m_password = password;
		m_email = email;
		m_pincode = pincode;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public long getCreated() {
		return m_created;
	}

	public String getUsername() {
		return m_username;
	}

	public String getPassword() {
		return m_password;
	}

	public String getEmail() {
		return m_email;
	}

	public boolean match( final String username ) {
		if( m_username.equals( username ) )
			return true;
		return false;
	}

	public boolean match( final String username, final String password ) {
		if( m_username.equals( username ) && m_password.equals( password ) )
			return true;
		return false;
	}

	public boolean match( final String username, final String password, final String pincode ) {
		if( m_username.equals( username ) && m_password.equals( password ) && m_pincode.equals( pincode ) )
			return true;
		return false;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
