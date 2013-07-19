/**
 * Authored By: IanF on 02/07/13 10:34
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/07/13 10:34: Created, IanF, ...
 *
 */

package com.upiva.manna.server.qlf;

import com.upiva.manna.server.svr.qlf.IMessageQualifier;
import com.upiva.manna.server.svr.qlf.MessageQualifierResult;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageQualifierSizeMD5 implements IMessageQualifier {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final String m_sharedKey;
	private final MessageDigest m_digest;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public MessageQualifierSizeMD5() throws NoSuchAlgorithmException {
		m_sharedKey = "3578eba8-f484-11de-aa57-0018f319a7ae";
		m_digest = MessageDigest.getInstance( "MD5" );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public MessageQualifierResult qualify( final String message ) {
		// parse message
		try {
			// extract size
			int slit0 = message.indexOf( '|' );
			if( slit0 == -1 )
				throw new NumberFormatException( "Invalid size" );
			int size = Integer.parseInt( message.substring( 0, slit0 ) );
			// ? hash value
			slit0++;
			if( !message.substring( slit0 ).startsWith( "HASH:" ) )
				throw new IllegalArgumentException( "Invalid checksum" );
			// extract checksum
			slit0 += 5;
			int slit1 = message.indexOf( ":", slit0 );
			final String checksum = message.substring( slit0, slit1 );
			slit1++;
			// extract payload
			final String payload;
			if( message.endsWith( "\0" ) )
				payload = message.substring( slit1, message.length() -1 );
			else
				payload = message.substring( slit1 );
			// calc & match digest
			final String match = payload + m_sharedKey;
			byte[] digest = m_digest.digest( match.getBytes() );
			final BigInteger bigInt = new BigInteger( 1, digest );
			final String md5hash = String.format( "%032x", bigInt );
			System.out.println( md5hash );
			//if( !md5hash.equals( checksum ) )
			//	throw new IllegalArgumentException( "Checksum not match" );
			// return success
			return new MessageQualifierResult( MessageQualifierResult.eState.SUCCESS, size, checksum, payload );
		} catch( NumberFormatException e ) {
			return new MessageQualifierResult( MessageQualifierResult.eState.SIZE_ERROR, 0, null, e.toString() );
		} catch( Exception e ) {
			return new MessageQualifierResult( MessageQualifierResult.eState.CHECKSUM_ERROR, 0, null, e.toString() );
		}
	}

	@Override
	public String prepare( final String message ) throws UnsupportedEncodingException {
		final String match = message + m_sharedKey;
		final byte[] digest = m_digest.digest( match.getBytes( "UTF-8" ) );
		final BigInteger bigInt = new BigInteger( 1, digest );
		final String output = String.format( "%d|HASH:%032x:%s\0", message.length() + 38, bigInt, message );
		return output;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
