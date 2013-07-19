/**
 * Authored By: IanF on 16/05/13 10:07
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 16/05/13 10:07: Created, $(USER), ...
 *
 */

package com.upiva.manna.server.bus.db4o;

import com.db4o.config.annotations.Indexed;
import com.upiva.common.dbo.UserRights;

import java.util.Date;

public class Persona {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final Date m_created = new Date( System.currentTimeMillis() );

	@Indexed
	private final String m_username;
	private final String m_password;

	private String m_email = null;
	private long m_rights = 0l;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public Persona( final String username, final String password ) {
		m_username = username;
		m_password = password;
	}

	public Persona( final String username, final String password, final String email ) {
		m_username = username;
		m_password = password;
		m_email = email;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getUsername() {
		return m_username;
	}

	public String getPassword() {
		return m_password;
	}

	public String getEmail() {
		return m_email;
	}

	public void setEmail( final String email ) {
		this.m_email = email;
	}

	public Date getCreated() {
		return m_created;
	}

	public boolean match( final String username, final String password ) {
		if( m_username.equals( username ) && m_password.equals( password ) )
			return true;
		return false;
	}

	public boolean isActive() {
		return !UserRights.isMask( UserRights.DISEASED, m_rights ) || !UserRights.isMask( UserRights.SUSPENDED, m_rights ) ;
	}

	public void setSuspended() {
		m_rights = UserRights.SUSPENDED.getMask();
	}

	public boolean isDeseased() {
		return UserRights.isMask( UserRights.DISEASED, m_rights );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
