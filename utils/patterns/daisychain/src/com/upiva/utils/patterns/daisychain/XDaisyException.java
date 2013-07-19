/**
 * Authored By: IanF on 18/07/13 17:43
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 18/07/13 17:43: Created, IanF, ...
 *
 */

package com.upiva.utils.patterns.daisychain;

public class XDaisyException extends RuntimeException {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final IDaisyChain m_daisy;
	private final Object m_object;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public XDaisyException( final IDaisyChain daisy, final Object object, final Throwable cause ) {
		super( cause );
		// preserve args
		m_daisy = daisy;
		m_object = object;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public String toString() {
		return String.format( "XDaisyException:{%s:{%s:'%s'}}", this.getCause().toString(), m_daisy, m_object );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public IDaisyChain getDaisy() {
		return m_daisy;
	}

	public Object getObject() {
		return m_object;
	}

	public void printException() {
		System.err.println( this.toString() );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
