/**
 * Authored By: IanF on 25/06/13 19:40
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 25/06/13 19:40: Created, IanF, ...
 *
 */

package src;

import java.awt.*;

public class ServerConsoleTester extends Frame {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ServerConsoleTester( final String title ) throws HeadlessException {
		super( title );

		this.pack();
		this.setVisible( true );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Main

	public static void main( String[] args ) {
		new ServerConsoleTester( "MannaServer-Console" );
	}
}
