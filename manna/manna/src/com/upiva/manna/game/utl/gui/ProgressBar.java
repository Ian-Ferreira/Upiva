/**
 * Authored By: IanF on 02/06/13 00:54
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 00:54: Created, IanF, ...
 *
 */

package com.upiva.manna.game.utl.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class ProgressBar extends Slider {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ProgressBar( final float min, final float max, final float stepSize, final boolean vertical, final Skin skin ) {
		super( min, max, stepSize, vertical, skin );
		this.setTouchable( Touchable.disabled );
	}

	public ProgressBar( final float min, final float max, final float stepSize, final boolean vertical, final Skin skin, final String styleName ) {
		super( min, max, stepSize, vertical, skin, styleName );
		this.setTouchable( Touchable.disabled );
	}

	public ProgressBar( final float min, final float max, final float stepSize, final boolean vertical, final SliderStyle style ) {
		super( min, max, stepSize, vertical, style );
		this.setTouchable( Touchable.disabled );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public void draw( final SpriteBatch batch, final float parentAlpha ) {
		super.draw( batch, parentAlpha );

//		ShapeRenderer renderer = new ShapeRenderer();
//		renderer.begin( ShapeRenderer.ShapeType.FilledRectangle );
//		renderer.setColor( 1f, 1f, 0f, 1f );
//		float x = 0f;
//		float y = 0f;
//		float w = this.getWidth() * this.getValue();
//		float h = this.getHeight();
//		renderer.filledRect( x, y, w, h );
//		renderer.end();
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
