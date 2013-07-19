/**
 * Authored By: IanF on 14/06/13 17:14
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 14/06/13 17:14: Created, IanF, ...
 *
 */

package com.upiva.common.dbo;

public enum UserRights {

	///////////////////////////////////////////////////////////////////////////
	// Enum keys

	DISEASED        ( 0x0000000000000000L ),
	SUSPENDED       ( 0x0000000000000001L ),
	GUEST           ( 0x0000000000000002L ),
	GAMER           ( 0x0000000000000004L ),
	ENROLL_USERS    ( 0x0000000000000008L ),
	CONFIRM_USERS   ( 0x0000000000000010L ),
	UPDATE_USERS    ( 0x0000000000000020L ),
	DELETE_USERS    ( 0x0000000000000040L ),

	ADMINISTRATOR   ( 0x1111111111111110L );

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final long m_mask;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	UserRights( final long mask ) {
		m_mask = mask;
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public long getMask() {
		return m_mask;
	}

	public boolean isAndMask( final long test ) {
		return ( m_mask == ( m_mask & test ) );
	}

	public long andMask( final long test ) {
		return m_mask & test;
	}

	public long orMask( final long test ) {
		return m_mask | test;
	}

	public long xorMask( final long test ) {
		return m_mask ^ test;
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static boolean isMask( final UserRights mask, final long test ) {
		return mask.isAndMask( test );
	}

	public static long and( final UserRights mask, final long test ) {
		return mask.andMask( test );
	}

	public static long or( final UserRights mask, final long test ) {
		return mask.orMask( test );
	}

	public static long xor( final UserRights mask, final long test ) {
		return mask.xorMask( test );
	}

}
