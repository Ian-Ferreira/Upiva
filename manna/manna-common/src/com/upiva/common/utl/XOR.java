/**
 * Authored By: IanF on 23/05/13 21:39
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 23/05/13 21:39: Created, IanF, ...
 *
 */

package com.upiva.common.utl;

public class XOR {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Construction

	// STATIC ONLY CLASSES HAVE NO CONSTRUCTORS

	/////////////////////////////////////////////////////////////////////////
	// Static Utilities

	public static String encrypt( final String target, final String prvkey ) {
		// TODO: need a base64 encryptor in the main loop
		// validate
		if( ( target == null ) || target.isEmpty() || ( prvkey == null ) || prvkey.isEmpty() )
			throw new IllegalArgumentException( "Arguments must not be empty" );
		// new builder
		final StringBuilder sb = new StringBuilder( target.length() );
		final char[] aaa = target.toCharArray();
		final char[] bbb = prvkey.toCharArray();
		int j = 0; // key stepper
		// loop target
		for( int i = 0; i < aaa.length; i++ ) {
			// extract
			final char a = ( char )(aaa[ i ] - 49);
			final char b = ( char )(bbb[ j ] - 49);
			// xor
			final int c = ( a ^ b ) + 49;
			sb.append( (char)c );
			// step and check
			j++;
			if( j >= bbb.length )
				j = 0;
		}
		// reply result
		return B64.encode( sb.toString() );
	}

	public static String decrypt( final String target, final String prvkey ) throws Throwable {
		// TODO: need a base64 decryptor in the main loop
		// validate
		if( ( target == null ) || target.isEmpty() || ( prvkey == null ) || prvkey.isEmpty() )
			throw new IllegalArgumentException( "Arguments must not be empty" );
		// new builder
		final StringBuilder sb = new StringBuilder( target.length() );
		final char[] aaa = B64.decode( target ).toCharArray();
		final char[] bbb = prvkey.toCharArray();
		int j = 0; // key stepper
		// loop target
		for( int i = 0; i < aaa.length; i++ ) {
			// extract
			final char a = ( char )(aaa[ i ] - 49);
			final char b = ( char )(bbb[ j ] - 49);
			// xor
			final int c = ( a ^ b ) + 49;
			sb.append( (char)c );
			// step and check
			j++;
			if( j >= bbb.length )
				j = 0;
		}
		// reply result
		return sb.toString();
	}
}
