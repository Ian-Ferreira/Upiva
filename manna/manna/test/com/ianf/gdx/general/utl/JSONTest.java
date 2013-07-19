package com.upiva.gdx.general.utl; /**
 * Authored By: IanF on 20/05/13 15:54
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 20/05/13 15:54: Created, IanF, ...
 *
 */

import com.upiva.manna.game.utl.JSON;

import java.util.HashMap;

public class JSONTest {

	private String m_test;
	private HashMap<String, Object> m_map;

	//@Before
	public void setUp() throws Exception {
		m_test = "{javaName:\"Java\",koos:{piet:\"gert\",doos:\"voos\"},javaV:\"23.0-b16\"}";
		m_map = new HashMap<String,Object>();
	}

	//@Test
	public void testParse() throws Exception {
		JSON.parse( m_test, m_map );
	}

}
