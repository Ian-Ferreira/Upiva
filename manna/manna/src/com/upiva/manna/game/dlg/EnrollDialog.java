/**
 * Authored By: IanF on 17/05/13 17:24
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 17/05/13 17:24: Created, IanF, ...
 *
 */

package com.upiva.manna.game.dlg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.gam.MannaProperties;

import java.io.IOException;

public class EnrollDialog extends AbstractDialog {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	public enum eState {
		DEFAULT,
		SUCCESS,
		FAILED,
		ESCAPED,
	}

	///////////////////////////////////////////////////////////////////////////
	// Data members

	// host
	private final AbstractGame m_game;

	// data widgets
	private final TextField m_username;
	private final TextField m_password1;
	private final TextField m_password2;
	private final TextField m_email;
	private final TextButton m_buttonOk;
	private final TextButton m_buttonCancel;


	// event handlers
	private final ClickListener m_clickedOk = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			// NOTE: this still get fired even if button disabled
			if( !m_buttonOk.isDisabled() ) {
				m_game.getSoundManager().playDefault();
				// register user
				if( m_password1.getText().equals( m_password2.getText() ) ) {
					boolean result = false;
					try {
						result = m_game.getBroker().registerUser( m_username.getText(), m_password1.getText(), m_email.getText() );
					} catch( IOException e ) {
						e.printStackTrace();
					}
					if( result )
						p_state = AbstractDialog.eState.SUCCESS;
					else
						p_state = AbstractDialog.eState.FAILED;
					super.clicked( event, x, y );
				}
			}
		}
	};
	private final ClickListener m_clickedCancel = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			super.clicked( event, x, y );
			m_game.getSoundManager().playDefault();
			// user escaped
			p_state = AbstractDialog.eState.ESCAPED;
		}
	};

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public EnrollDialog( final AbstractGame game, final Skin skin ) {
		super( "", skin, game );

		// preserve args
		m_game = game;

		// TODO: sort this skin graphics out
		this.getContentTable().add( new Label( GEN.charSequence( "Please Register New User?" ), skin, "caption-font", new Color( 1f, 1f, 1f, 1f ) ) ).top().colspan( 2 );
		this.getContentTable().row();

		this.getContentTable().add( new Label( GEN.charSequence( "Username:" ), skin ) );
		m_username = new TextField( "", skin );
		this.getContentTable().add( m_username ).width( 300 );
		this.getContentTable().row();

		this.getContentTable().add( new Label( GEN.charSequence( "Password 1:" ), skin ) );
		m_password1 = new TextField( "", skin );
		m_password1.setPasswordMode( true );
		m_password1.setPasswordCharacter( '*' );
		this.getContentTable().add( m_password1 ).width( 300 );
		this.getContentTable().row();

		this.getContentTable().add( new Label( GEN.charSequence( "Password 2:" ), skin ) );
		m_password2 = new TextField( "", skin );
		m_password2.setPasswordMode( true );
		m_password2.setPasswordCharacter( '*' );
		this.getContentTable().add( m_password2 ).width( 300 );
		this.getContentTable().row();

		this.getContentTable().add( new Label( GEN.charSequence( "Email:" ), skin ) );
		m_email = new TextField( "", skin );
		m_email.setPasswordMode( true );
		this.getContentTable().add( m_email ).width( 300 );
		this.getContentTable().row();

		m_buttonOk = new TextButton( "OK", skin );
		m_buttonOk.addListener( m_clickedOk );
		m_buttonOk.setColor( new Color( 1f, 0f, 0f, 1f ) );
		this.getButtonTable().add( m_buttonOk ).width( 100 );

		m_buttonCancel = new TextButton( "Cancel", skin );
		m_buttonCancel.addListener( m_clickedCancel );
		this.getButtonTable().add( m_buttonCancel );

		// debug stuff
		if( m_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_MODE ) ) {
			this.getContentTable().debugTable();
			this.getButtonTable().debugTable();
		}
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
