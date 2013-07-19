/**
 * Authored By: IanF on 18/07/13 17:39
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 17:39: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain;

public interface IDaisyChain {

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/**
	 * Processes the given message and produce resulting output.   This will
	 * inherently also call process on the embedded next IDaisyChain object,
	 * who will recursively continue calling process methods until the chain
	 * is exhausted.   When the last object in the chain has been reached,
	 * the processing is considered complete and the result will be returned.
	 *
	 * @param input user defined Object message to process
	 * @return user defined Object result of processed message
	 * @throws XDaisyException thrown when a node in the chain produces a null
	 * result, could be used by the derived classes to produce output
	 */
	Object process( Object input ) throws XDaisyException;

}
