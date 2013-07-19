/**
 * By: IanF on 05/05/13 10:20
 */

package com.upiva.manna.game.scr;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.gam.MannaProperties;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class OutroScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public OutroScreen( final AbstractGame game ) throws AssertionError {
		super( game );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public void show() {
		super.show();

		// outro sound
		final String music = p_game.getProperties().getString( MannaProperties.eKey.OUTRO_MUSIC );
		if( !music.isEmpty() ) {
			p_game.getMusicManager().play( music );
		}

		// get texture from loader
		final TextureRegion region = p_game.getLoader().getAtlasRegion(
				p_game.getProperties().getString( MannaProperties.eKey.DEFAULT_ATLAS ),
				p_game.getProperties().getString( MannaProperties.eKey.OUTRO_IMAGE) );
		final Drawable drawable = new TextureRegionDrawable( region );

		// here we create the splash image actor; its size is set when the
		// resize() method gets called
		final Image image = new Image( drawable, Scaling.stretch );
		image.setFillParent( true );

		// add the image actor to the stage
		this.getStage().addActor( image );

		// configure the fade-in/out effect on the stage
		this.getStage().getRoot().getColor().a = 0f;
		final AlphaAction fadeIn = Actions.fadeIn( p_game.getProperties().getFloat( MannaProperties.eKey.OUTRO_FADEIN ) );
		final DelayAction delay = Actions.delay( p_game.getProperties().getFloat( MannaProperties.eKey.OUTRO_DELAY ) );
		final AlphaAction fadeOut = Actions.fadeOut( p_game.getProperties().getFloat( MannaProperties.eKey.OUTRO_FADEOUT ) );

		// sequence to control existence
		final SequenceAction action;
		action = sequence( fadeIn, delay, fadeOut, new Action() {
			@Override
			public boolean act( float delta ) {
				p_game.processInput( OutroScreen.this, null, false );
				return true;
			}
		} );

		// finally add action to stage
		this.getStage().addAction( action );

	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers


}
