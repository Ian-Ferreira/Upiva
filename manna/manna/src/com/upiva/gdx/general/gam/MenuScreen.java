/**
 * By: IanF on 05/05/13 09:51
 */

package com.upiva.gdx.general.gam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.scr.AbstractScreen;

public class MenuScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public MenuScreen( final AbstractGame game ) throws AssertionError {
		super( game );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public void show() {
		super.show();

		// use default table
		final Table table = this.getTable();

		// caption
		final Label label = new Label( GEN.charSequence( "MENU" ), this.getSkin(), "caption-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( label ).top().spaceBottom( 50 );
		table.row();

		// register the button "start gam"
		final TextButton buttonGame = new TextButton( "Start Game", getSkin() );
		buttonGame.addListener( new ClickListener() {
			//@Override
			public void clicked( final InputEvent event, final float x, final float y ) {
				super.clicked( event, x, y );
				p_game.processInput( MenuScreen.this, "GameScreen", true );
			}
		} );
		table.add( buttonGame ).size( 300, 50 ).uniform().spaceBottom( 10 );
		table.row();

		// register the button "high scores"
		final TextButton highScoresButton = new TextButton( "High Scores", getSkin() );
		highScoresButton.addListener( new ClickListener() {
			//@Override
			public void clicked( final InputEvent event, final float x, final float y ) {
				super.clicked( event, x, y );
				p_game.processInput( MenuScreen.this, "ScoresScreen", true );
			}
		} );
		table.add( highScoresButton ).size( 300, 50 ).uniform().spaceBottom( 10 );
		table.row();

		// register the button "options"
		final TextButton optionsButton = new TextButton( "Options", getSkin() );
		optionsButton.addListener( new ClickListener() {
			//@Override
			public void clicked( final InputEvent event, final float x, final float y ) {
				super.clicked( event, x, y );
				p_game.processInput( MenuScreen.this, "OptionsScreen", true );
			}
		} );
		table.add( optionsButton ).size( 300, 50 ).uniform().spaceBottom( 10 );
		table.row();

		// register the button "about"
		final TextButton aboutButton = new TextButton( "About", getSkin() );
		aboutButton.addListener( new ClickListener() {
			//@Override
			public void clicked( final InputEvent event, final float x, final float y ) {
				super.clicked( event, x, y );
				p_game.processInput( MenuScreen.this, "AboutScreen", true );
			}
		} );
		table.add( aboutButton ).size( 300, 50 ).uniform().spaceBottom( 10 );
		table.row();

		// register the button "exit"
		final TextButton exitButton = new TextButton( "Exit", getSkin() );
		exitButton.addListener( new ClickListener() {
			//@Override
			public void clicked( final InputEvent event, final float x, final float y ) {
				super.clicked( event, x, y );
				p_game.processInput( MenuScreen.this, "OutroScreen", true );
			}
		} );
		table.add( exitButton ).size( 300, 50 ).uniform().spaceBottom( 10 );
		table.row();

	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
