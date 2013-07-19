/**
 * Authored By: IanF on 14/06/13 17:55
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 14/06/13 17:55: Created, IanF, ...
 *
 */

package com.upiva.common.dbo;

import java.util.Vector;

public class GamerProfile {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_username;
	private long m_coins = 0;
	private Vector<Integer> m_levels = new Vector<>( 4 );

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public GamerProfile() {
		m_username = null;
	}

	public GamerProfile( final String username, final long coins, final int[] levels ) {
		// preserve args
		m_username = username;
		m_coins = coins;
		this.setLevels( levels );
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

	public float getCoins() {
		return m_coins;
	}

	public void setCoins( final long coins ) {
		m_coins = coins;
	}

	public int getLevel( final int index ) {
		return m_levels.get( index );
	}

	public void setlevel( final int index, final int level ) {
		m_levels.set( index, level );
	}

	public void setLevels( final int[] levels ) {
		for( int i = 0; i < levels.length; i++ ) {
			m_levels.add( i, levels[ i ] );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
