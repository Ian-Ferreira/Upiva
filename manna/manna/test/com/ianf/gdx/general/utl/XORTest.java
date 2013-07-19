/**
 * Authored By: IanF on 25/05/13 12:23
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 25/05/13 12:23: Created, IanF, ...
 *
 */

package com.upiva.gdx.general.utl;

import com.upiva.manna.game.utl.XOR;

public class XORTest {

	public static void main( String[] args ) {

		String aaa = "123.456.789.0";
		String bbb = "1234567890";

		String ccc = XOR.encrypt( aaa, bbb );
		System.out.println( ccc );

		String ddd = XOR.decrypt( ccc, bbb );
		System.out.println( ddd );
	}
}
