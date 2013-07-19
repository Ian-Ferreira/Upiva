/**
 * Authored By: IanF on 18/07/13 18:23
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 18:23: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain.common;

import com.upiva.utils.patterns.daisychain.IDaisyChain;
import com.upiva.utils.patterns.daisychain.XDaisyException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashMatcherDaisy implements IDaisyChain {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_sharedKey;
	private final MessageDigest m_digest;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public HashMatcherDaisy( final String sharedKey ) throws NoSuchAlgorithmException {
		m_sharedKey = sharedKey;
		m_digest = MessageDigest.getInstance( "MD5" );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public Object process( final Object input ) throws XDaisyException {
		final String text = ( String )input;
		final int slit = text.indexOf( ':' );
		if( slit < 0 )
			throw new XDaisyException( this, input, new IllegalArgumentException( "Not a hash prefixed message" ) );
		final String test = text.substring( 0, slit );
		final String mesg = text.substring( slit + 1 );
		final String match = mesg + m_sharedKey;
		byte[] digest = m_digest.digest( match.getBytes() );
		final BigInteger bigInt = new BigInteger( 1, digest );
		final String hash = String.format( "%032x", bigInt );
		if( !test.equals( hash ) )
			throw new XDaisyException( this, input, new IllegalArgumentException( "Invalid message hash value" ) );
		return text.substring( slit + 1 );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
