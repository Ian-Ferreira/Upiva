/**
 * Authored By: IanF on 18/07/13 18:21
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 18:21: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain.common;

import com.upiva.utils.patterns.daisychain.IDaisyChain;
import com.upiva.utils.patterns.daisychain.XDaisyException;

public class SizeMatcherDaisy implements IDaisyChain {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public Object process( final Object input ) throws XDaisyException {
		final String text = ( String )input;
		final int slit = text.indexOf( '|' );
		if( slit < 0 )
			throw new XDaisyException( this, input, new IllegalArgumentException( "Not a size prefixed message" ) );
		final int size = Integer.parseInt( text.substring( 0, slit ) );
		final int lgth = text.length() - ( slit + 1 );
		if( size > lgth )
			throw new XDaisyException( this, input, new IllegalArgumentException( "Invalid message size given: " + size + ", actual: " + lgth  ) );
		return text.substring( slit + 1 );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
