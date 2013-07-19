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

package com.upiva.manna.server.svr.qlf;

import java.io.UnsupportedEncodingException;

public interface IMessageQualifier {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	MessageQualifierResult qualify( final String message );

	String prepare( final String message ) throws UnsupportedEncodingException;
}
