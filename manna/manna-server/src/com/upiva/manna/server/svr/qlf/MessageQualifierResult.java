/**
 * Authored By: IanF on 02/07/13 10:36
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/07/13 10:36: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.qlf;

public class MessageQualifierResult {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	public enum eState { SUCCESS, SIZE_ERROR, CHECKSUM_ERROR }

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final eState m_state;
	private final int m_size;
	private final String m_checksum;
	private final String m_message;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public MessageQualifierResult( final eState state, final int size, final String checksum, final String message ) {
		// preserve args
		m_state = state;
		m_size = size;
		m_checksum = checksum;
		m_message = message;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public eState getState() {
		return m_state;
	}

	public int getSize() {
		return m_size;
	}

	public String getChecksum() {
		return m_checksum;
	}

	public String getMessage() {
		return m_message;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
