/**
 * Authored By: IanF on 16/05/13 19:40
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 16/05/13 19:40: Created, $(USER), ...
 *
 */

package com.upiva.manna.server.bus.db4o;

import com.db4o.config.annotations.Indexed;
import com.upiva.common.dbo.Token;

import java.io.Serializable;
import java.util.Date;

public class Session implements Serializable {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	@Indexed
	private final Date m_created = new Date( System.currentTimeMillis() );
	private final String m_ipaddress;
	private final Persona m_persona;

	private IUserExtra m_extra;

	private Date m_disposed = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public Session( final String ipaddress ) {
		m_ipaddress = ipaddress;
		m_persona = null;
	}

	public Session( final String ipaddress, final Persona persona ) {
		m_ipaddress = ipaddress;
		m_persona = persona;
	}

	public Session( final String ipaddress, final Persona persona, final IUserExtra extra ) {
		m_ipaddress = ipaddress;
		m_persona = persona;
		m_extra = extra;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public String toString() {
		return Long.toString( this.getCreated().getTime() );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public Date getCreated() {
		return m_created;
	}

	public String getIPAddress() {
		return m_ipaddress;
	}

	public Persona getPersona() {
		return m_persona;
	}

	public IUserExtra getExtra() {
		return m_extra;
	}

	public void setExtra( final IUserExtra extra ) {
		m_extra = extra;
	}

	public Date getDisposed() {
		return m_disposed;
	}

	public void setDisposed() {
		if( m_disposed == null )
			m_disposed = new Date( System.currentTimeMillis() );
	}

	public Token getToken() {
		return new Token( this.toString(), this.getPersona().getUsername(), this.getPersona().getEmail() );
	}

	public boolean isActive() {
		return m_disposed == null;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
