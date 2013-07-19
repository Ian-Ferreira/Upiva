/**
 * By: IanF on 15/05/13 19:06
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
import com.upiva.manna.game.utl.gen.IDialogState;

public class LogonDialog extends AbstractDialog {

	///////////////////////////////////////////////////////////////////////////
	// Constant data

	public static enum eState implements IDialogState {
		FACEBOOK(),
		GOOGLE,
		ENROLL,
	}

	///////////////////////////////////////////////////////////////////////////
	// Data members

	// widgets
	private final TextField m_username;
	private final TextField m_password;
	private final TextButton m_buttonFacebook;
	private final TextButton m_buttonGoogle;
	private final TextButton m_buttonEnroll;
	private final TextButton m_buttonOk;
	private final TextButton m_buttonCancel;

	// event handlers
	private final ClickListener m_clickedFacebook = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			// NOTE: this still get fired even if button disabled
			if( !m_buttonFacebook.isDisabled() ) {
				super.clicked( event, x, y );
				p_game.getSoundManager().playDefault();
				p_state = eState.FACEBOOK;
			}
		}
	};
	private final ClickListener m_clickedGoogle = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			// NOTE: this still get fired even if button disabled
			if( !m_buttonGoogle.isDisabled() ) {
				super.clicked( event, x, y );
				p_game.getSoundManager().playDefault();
				p_state = eState.GOOGLE;
			}
		}
	};

	private final ClickListener m_clickedEnroll = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			// NOTE: this still get fired even if button disabled
			if( !m_buttonEnroll.isDisabled() ) {
				super.clicked( event, x, y );
				p_game.getSoundManager().playDefault();
				p_state = eState.ENROLL;
			}
		}
	};

	private final ClickListener m_clickedOk = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			// NOTE: this still get fired even if button disabled
			if( !m_buttonOk.isDisabled() ) {
				super.clicked( event, x, y );
				p_game.getSoundManager().playDefault();
				// authenticate user
				final String password = m_password.getText().equals( "*****" ) ? "guest" : m_password.getText();
				try {
					p_game.getBroker().acquireToken( m_username.getText(), password );
					p_state = AbstractDialog.eState.SUCCESS;
				} catch( Exception e ) {
					e.printStackTrace();
					p_state = AbstractDialog.eState.FAILED;
				}
			}
		}
	};

	private final ClickListener m_clickedCancel = new ClickListener() {
		@Override
		public void clicked( final InputEvent event, final float x, final float y ) {
			super.clicked( event, x, y );
			p_game.getSoundManager().playDefault();
			// user escaped
			p_state = AbstractDialog.eState.ESCAPED;
		}
	};

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public LogonDialog( final AbstractGame game, final Skin skin ) {
		super( "", skin, game );

		// preserve args

		// TODO: sort this skin graphics out
		final Label label = new Label( GEN.charSequence( "Please Login?" ), skin, "caption-font", new Color( 1f, 1f, 1f, 1f ) );
		this.getContentTable().add( label ).top().colspan( 2 );
		this.getContentTable().row();

		this.getContentTable().add( new Label( GEN.charSequence( "Username:" ), skin ) );
		m_username = new TextField( "", skin );
		m_username.setText( "guest" );
		this.getContentTable().add( m_username ).width( 300 );
		this.getContentTable().row();

		this.getContentTable().add( new Label( GEN.charSequence( "Password:" ), skin ) );
		m_password = new TextField( "", skin );
		m_password.setText( "*****" );
		m_password.setPasswordMode( true );
		m_password.setPasswordCharacter( '*' );
		this.getContentTable().add( m_password ).width( 300 );
		this.getContentTable().row();

		m_buttonFacebook = new TextButton( "Facebook", skin );
		m_buttonFacebook.addListener( m_clickedFacebook );
		m_buttonFacebook.setDisabled( true );
		m_buttonFacebook.setColor( new Color( 0.25f, 0.25f, 0.25f, 1f ) );
		this.getButtonTable().add( m_buttonFacebook );

		m_buttonGoogle = new TextButton( "Google+", skin );
		m_buttonGoogle.addListener( m_clickedGoogle );
		m_buttonGoogle.setDisabled( true );
		m_buttonGoogle.setColor( new Color( 0.3f, 0.3f, 0.3f, 1f ) );
		this.getButtonTable().add( m_buttonGoogle );

		m_buttonEnroll = new TextButton( "Register", skin );
		m_buttonEnroll.addListener( m_clickedEnroll );
		this.getButtonTable().add( m_buttonEnroll );

		m_buttonOk = new TextButton( "OK", skin );
		m_buttonOk.addListener( m_clickedOk );
		m_buttonOk.setColor( new Color( 1f, 0f, 0f, 1f ) );
		this.getButtonTable().add( m_buttonOk ).width( 100 );

		m_buttonCancel = new TextButton( "Cancel", skin );
		m_buttonCancel.addListener( m_clickedCancel );
		m_buttonCancel.setColor( new Color( 0f, 0f, 1f, 1f ) );
		this.getButtonTable().add( m_buttonCancel );

		// debug stuff
		if( p_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_TABLE ) ) {
			this.getContentTable().debugTable();
			this.getButtonTable().debugTable();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public String resetState() {
		m_password.setText( "*****" );
		return super.resetState();
	}


	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
