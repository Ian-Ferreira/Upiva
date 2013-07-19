/**
 * Authored By: IanF on 22/06/13 09:50
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 22/06/13 09:50: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cmd.db4o;

import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XProcessException;
import com.upiva.manna.server.exc.XSessionException;
import com.upiva.manna.server.svr.cmd.AbstractCommandService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.IManagerAccessor;
import com.upiva.manna.server.svr.dom.IDatabaseSession;

public class CommandFibonaci extends AbstractCommandService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private int m_recurse = 0;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public CommandFibonaci( final IManagerAccessor manager ) {
		super( manager );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected boolean doProcess( final IDatabaseSession database, final ClientRequest request, final ClientResponse response ) throws XAccessException, XProcessException, XSessionException {

		// extract random
		final int index = ( int )request.getLong( "index" );
		response.add( "" + index );

		final int value = ( int )request.getLong( "value" );

		System.out.println( "\n>>> " + index + " - " + value );

		// record start
		final long t0 = System.nanoTime();

		// recurse
		m_recurse = 0;
		final long result;
		try {
			result = _fibonaci( value, m_recurse = 0 );
		} catch( Throwable e ) {
			response.add( e.toString() );
			response.add( "" + m_recurse );
			// report time
			final long t1 = System.nanoTime();
			response.add( t1 - t0 );
			return false;
		}

		// pack response
		response.add( "" + result );
		response.add( "" + m_recurse );

		// report time
		final long t1 = System.nanoTime();
		response.add( "" + ( t1 - t0 ) );

		System.out.println( "<<< " + index + " - " + result + ", " + m_recurse );

		// all good
		return true;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private long _fibonaci( final int value, /*non-final*/ int _recurse ) throws Exception {
		//System.out.println( "CommandFibonaci._fibonaci:" + value + "," + _recurse );
		if( value == 0 ) {
			return 0;
		}
		if( value == 1 ) {
			return 1;
		}
		_recurse++;
		return _fibonaci( value - 1, _recurse ) + _fibonaci( value - 2, _recurse );
	}
}
