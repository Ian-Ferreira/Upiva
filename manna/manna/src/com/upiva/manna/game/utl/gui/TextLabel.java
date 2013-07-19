/**
 * Authored By: IanF on 16/06/13 19:16
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 16/06/13 19:16: Created, IanF, ...
 *
 */

package com.upiva.manna.game.utl.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.upiva.common.utl.GEN;

public class TextLabel extends Label {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public TextLabel( final String text, final Skin skin ) {
		super( GEN.charSequence( text ), skin );
	}

	public TextLabel( final String text, final Skin skin, final String styleName ) {
		super( GEN.charSequence( text ), skin, styleName );
	}

	public TextLabel( final String text, final Skin skin, final String fontName, final Color color ) {
		super( GEN.charSequence( text ), skin, fontName, color );
	}

	public TextLabel( final String text, final Skin skin, final String fontName, final String colorName ) {
		super( GEN.charSequence( text ), skin, fontName, colorName );
	}

	public TextLabel( final String text, final LabelStyle style ) {
		super( GEN.charSequence( text ), style );
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
