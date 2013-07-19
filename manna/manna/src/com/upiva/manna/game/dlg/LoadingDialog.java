/**
 * Authored By: IanF on 16/06/13 20:17
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 16/06/13 20:17: Created, IanF, ...
 *
 */

package com.upiva.manna.game.dlg;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.upiva.manna.game.gam.AbstractGame;

public class LoadingDialog extends AbstractDialog {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public LoadingDialog( final String title, final Skin skin, final AbstractGame game ) {
		super( title, skin, game );
		this.setModal( true );
		this.setWidth( 200f );
		this.setHeight( 50f );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
