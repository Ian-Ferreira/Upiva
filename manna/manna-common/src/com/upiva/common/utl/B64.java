/**
 * Authored By: IanF on 17/05/13 15:32
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 17/05/13 15:32: Created, IanF, ...
 *
 */

package com.upiva.common.utl;

public class B64 {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	// Base64 criteria
	private static final char[] BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private static final int[] BASE64_INDEXER = new int[ 128 ];

	static {
		for( int i = 0; i < BASE64_ALPHABET.length; i++ ) {
			BASE64_INDEXER[ BASE64_ALPHABET[ i ] ] = i;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Construction

	// STATIC ONLY CLASSES HAS NO CONSTRUCTORS

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static String encode( final String message ) {
		final String data = message.trim();
		char[] buf = data.toCharArray();
		int size = buf.length;
		char[] ar = new char[ ( ( size + 2 ) / 3 ) * 4 ];
		int a = 0;
		int i = 0;
		while( i < size ) {
			byte b0 = ( byte )buf[ i++ ];
			byte b1 = ( byte )( ( i < size ) ? buf[ i++ ] : 0 );
			byte b2 = ( byte )( ( i < size ) ? buf[ i++ ] : 0 );

			int mask = 0x3F;
			ar[ a++ ] = BASE64_ALPHABET[ ( b0 >> 2 ) & mask ];
			ar[ a++ ] = BASE64_ALPHABET[ ( ( b0 << 4 ) | ( ( b1 & 0xFF ) >> 4 ) ) & mask ];
			ar[ a++ ] = BASE64_ALPHABET[ ( ( b1 << 2 ) | ( ( b2 & 0xFF ) >> 6 ) ) & mask ];
			ar[ a++ ] = BASE64_ALPHABET[ b2 & mask ];
		}
		switch( size % 3 ) {
			case 1:
			case 2:
				ar[ --a ] = '=';
				break;
		}
		return new String( ar );
	}

	public static String decode( final String message ) throws Throwable {
		final String data = message.trim();
		int delta = data.endsWith( "==" ) ? 2 : data.endsWith( "=" ) ? 1 : 0;
		byte[] buffer = new byte[ data.length() * 3 / 4 - delta ];
		int mask = 0xFF;
		int index = 0;
		for( int i = 0; i < data.length(); i += 4 ) {
			int c0 = BASE64_INDEXER[ data.charAt( i ) ];
			int c1 = BASE64_INDEXER[ data.charAt( i + 1 ) ];
			buffer[ index++ ] = ( byte )( ( ( c0 << 2 ) | ( c1 >> 4 ) ) & mask );
			if( index >= buffer.length ) {
				break;
			}
			int c2 = BASE64_INDEXER[ data.charAt( i + 2 ) ];
			buffer[ index++ ] = ( byte )( ( ( c1 << 4 ) | ( c2 >> 2 ) ) & mask );
			if( index >= buffer.length ) {
				break;
			}
			int c3 = BASE64_INDEXER[ data.charAt( i + 3 ) ];
			buffer[ index++ ] = ( byte )( ( ( c2 << 6 ) | c3 ) & mask );
		}
		final String retval = new String( buffer );
		return retval.trim();
	}


}
