/**
 * Authored By: IanF on 02/07/13 10:44
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/07/13 10:44: Created, IanF, ...
 *
 */

package com.upiva.manna.server.qlf;

import com.upiva.manna.server.svr.qlf.IMessageQualifier;
import com.upiva.manna.server.svr.qlf.MessageQualifierResult;

public class MessageQualifierDefault implements IMessageQualifier {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public MessageQualifierResult qualify( final String message ) {
		return new MessageQualifierResult( MessageQualifierResult.eState.SUCCESS, message.length(), null, message );
	}

	@Override
	public String prepare( final String message ) {
		return message;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
