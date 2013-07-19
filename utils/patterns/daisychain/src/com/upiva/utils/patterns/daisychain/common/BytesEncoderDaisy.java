/**
 * Authored By: IanF on 18/07/13 18:26
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 18:26: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain.common;

import com.upiva.utils.patterns.daisychain.IDaisyChain;
import com.upiva.utils.patterns.daisychain.XDaisyException;

import java.nio.charset.Charset;

public class BytesEncoderDaisy implements IDaisyChain {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final Charset m_charset;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public BytesEncoderDaisy( final Charset charset ) {
		m_charset = charset;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public Object process( final Object input ) throws XDaisyException {
		return ( ( String )input ).getBytes( m_charset );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
