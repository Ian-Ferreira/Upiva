/**
 * Authored By: IanF on 14/06/13 17:02
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 14/06/13 17:02: Created, IanF, ...
 *
 */

package com.upiva.manna.server.bus.db4o;

public class DiscardReason {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private Persona m_persona = null;
	private String m_reason = null;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DiscardReason( final Persona persona, final String reason ) {
		m_persona = persona;
		m_reason = reason;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public Persona getPersona() {
		return m_persona;
	}

	public String getReason() {
		return m_reason;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
