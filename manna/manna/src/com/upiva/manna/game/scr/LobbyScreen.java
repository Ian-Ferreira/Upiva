/**
 * Authored By: IanF on 31/05/13 20:09
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 31/05/13 20:09: Created, IanF, ...
 *
 */

package com.upiva.manna.game.scr;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.upiva.manna.game.dlg.LoadingDialog;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.gam.MannaProperties;
import com.upiva.manna.game.utl.gui.TextLabel;

public class LobbyScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private TextLabel m_textUsername;
	private TextLabel m_textLevel;
	private TextLabel m_textCoins;
	private Stack m_stackGames;
	private Table m_tableGames;
	private Table m_tableSlots;
	private Table m_tableCards;
	private Table m_tableBingo;
	private TextButton m_buttonSlots;
	private TextButton m_buttonCards;
	private TextButton m_buttonBingo;
	private ButtonGroup m_buttonGroup;
	private LoadingDialog m_dlgLoading;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public LobbyScreen( final AbstractGame game ) throws AssertionError {
		super( game );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public void show() {
		super.show();

		// intro sound
		final String music = p_game.getProperties().getString( MannaProperties.eKey.LOBBY_MUSIC );
		if( !music.isEmpty() ) {
			p_game.getMusicManager().play( music );
		}

		// get texture from loader
		final TextureRegion region = p_game.getLoader().getAtlasRegion(
				p_game.getProperties().getString( MannaProperties.eKey.DEFAULT_ATLAS ),
				p_game.getProperties().getString( MannaProperties.eKey.LOBBY_IMAGE ) );

		// here we create the splash image actor; its size is set when the
		// resize() method gets called
		final Image image = new Image( new TextureRegionDrawable( region ), Scaling.stretch );
		image.setFillParent( true );

		// add the image actor to the stage
		p_stage.addActor( image );

		final Table tableHeader = new Table( this.getSkin() );
		tableHeader.setBounds( 0f, p_stage.getHeight() - 80f, p_stage.getWidth(), 80f );
		if( p_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_TABLE ) )
			tableHeader.debug();
		p_stage.addActor( tableHeader );
		tableHeader.align( Align.left );
		tableHeader.add( "User: ", "label-type" ).left().pad( 5f, 5f, 5f, 0f ).width( 80f );
		m_textUsername = new TextLabel( "...", this.getSkin(), "value-type" );
		tableHeader.add( m_textUsername ).pad( 5f, 0f, 5f, 5f ).width( 160f ).align( Align.center ).spaceRight( 10f );
		tableHeader.add( "Level: ", "label-type" ).pad( 5f, 5f, 5f, 0f ).width( 100f );
		m_textLevel = new TextLabel( "0", this.getSkin(), "value-type" );
		tableHeader.add( m_textLevel ).pad( 5f, 0f, 5f, 5f ).width( 100f ).align( Align.right ).spaceRight( 10f );
		tableHeader.add( "Coins: ", "label-type" ).pad( 5f, 5f, 5f, 0f ).width( 100f );
		m_textCoins = new TextLabel( "0", this.getSkin(), "value-type" );
		tableHeader.add( m_textCoins ).pad( 5f, 0f, 5f, 5f ).width( 100f ).align( Align.right ).spaceRight( 10f );

		final Table tableContex = new Table( this.getSkin() );
		tableContex.setBounds( 0f, 80f, p_stage.getWidth() - 120f, p_stage.getHeight() - 160f );
		if( p_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_TABLE ) )
			tableContex.debug();
		p_stage.addActor( tableContex );
		m_stackGames = new Stack();
		tableContex.add( m_stackGames ).expand().pad( 10f, 10f, 10f, 10f );

		m_tableSlots = new Table( this.getSkin() );
		m_stackGames.add( new ScrollPane( m_tableSlots, this.getSkin() ) );
		m_tableCards = new Table( this.getSkin() );
		m_stackGames.add( new ScrollPane( m_tableCards, this.getSkin() ) );
		m_tableBingo = new Table( this.getSkin() );
		m_stackGames.add( new ScrollPane( m_tableBingo, this.getSkin() ) );

		final Table tableGames = new Table();
		tableGames.setBounds( p_stage.getWidth() - 120f, 80f, 120f, p_stage.getHeight() - 200f );
		tableGames.pad( 10f );
		p_stage.addActor( tableGames );
		m_buttonSlots = new TextButton( "Slots", this.getSkin() );
		m_buttonSlots.setDisabled( true );
		tableGames.add( m_buttonSlots ).pad( 10f, 10f, 10f, 10f ).expandX().uniform();
		tableGames.row();
		m_buttonCards = new TextButton( "Cards", this.getSkin() );
		tableGames.add( m_buttonCards ).pad( 10f, 10f, 10f, 10f );
		tableGames.row();
		m_buttonBingo = new TextButton( "Bingo", this.getSkin() );
		tableGames.add( m_buttonBingo ).pad( 10f, 10f, 10f, 10f );
		m_buttonGroup = new ButtonGroup( m_buttonSlots, m_buttonCards, m_buttonBingo );
		m_buttonGroup.setChecked( "Slots" );

		final Table tableFooter = new Table( this.getSkin() );
		tableFooter.setBounds( 0f, 0f, p_stage.getWidth(), 80f );
		if( p_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_TABLE ) )
			tableFooter.debug();
		this.getStage().addActor( tableFooter );
		tableFooter.add( new TextButton( "Gifts", this.getSkin() ) ).space( 5f, 5f, 5f, 5f ).width( 100f );
		tableFooter.add( new TextButton( "Friends", this.getSkin() ) ).space( 5f, 5f, 5f, 5f ).width( 100f );
		tableFooter.add( new TextButton( "Coins", this.getSkin() ) ).space( 5f, 5f, 5f, 5f ).width( 100f );
		tableFooter.add( new TextButton( "Options", this.getSkin() ) ).space( 5f, 5f, 5f, 5f ).width( 100f );
		tableFooter.add( new TextButton( "About", this.getSkin() ) ).space( 5f, 5f, 5f, 5f ).width( 100f );

		m_dlgLoading = new LoadingDialog( "Loading...", this.getSkin(), p_game );
		m_dlgLoading.start();

		// process profile
	}

	@Override
	public void render( final float delta ) {
		super.render( delta );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
