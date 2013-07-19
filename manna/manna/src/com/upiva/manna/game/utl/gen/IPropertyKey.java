/**
 * Authored By: IanF on 18/05/13 10:19
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/05/13 10:19: Created, IanF, ...
 *
 */

package com.upiva.manna.game.utl.gen;

public interface IPropertyKey {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/**
	 * Return the property default newValue
	 *
	 * @return property default newValue
	 */
	Object getDefault();

	/**
	 * Returns the property key element name as a string
	 *
	 * @return key element name
	 */
	String toString();

}
