/**
 * Authored By: IanF on 18/07/13 18:24
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 18:24: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain.common;

import com.upiva.utils.patterns.daisychain.IDaisyChain;
import com.upiva.utils.patterns.daisychain.XDaisyException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPrependerDaisy implements IDaisyChain {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_sharedKey;
	private final MessageDigest m_digest;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public HashPrependerDaisy( final String sharedKey ) throws NoSuchAlgorithmException {
		m_sharedKey = sharedKey;
		m_digest = MessageDigest.getInstance( "MD5" );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public Object process( final Object input ) throws XDaisyException {
		final String match = input + m_sharedKey;
		final byte[] digest = m_digest.digest( match.getBytes() );
		final BigInteger bigInt = new BigInteger( 1, digest );
		return String.format( "%032x:%s", bigInt, input );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
