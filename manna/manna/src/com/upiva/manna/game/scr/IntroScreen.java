/**
 * By: IanF on 04/05/13 19:43
 */

package com.upiva.manna.game.scr;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.upiva.manna.game.dlg.AbstractDialog;
import com.upiva.manna.game.dlg.EnrollDialog;
import com.upiva.manna.game.dlg.LogonDialog;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.gam.MannaProperties;
import com.upiva.manna.game.utl.gui.ProgressBar;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class IntroScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Constant data

	private static enum eState {
		STARTED,
		LOADING,
		FACEBOOK,
		GOOGLE,
		ENROLL,
		LOGON,
		FADEOUT,
		FINISH,
	}

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private eState m_state = eState.STARTED;

	private ProgressBar m_progress;

	private LogonDialog m_facebookDialog;
	private LogonDialog m_googleDialog;
	private EnrollDialog m_enrollDialog;
	private LogonDialog m_logonDialog;

	// current active dialog or null if none
	private AbstractDialog m_dialog = null;

	private AlphaAction m_actionFadeIn;
	private DelayAction m_actionDelay;
	private AlphaAction m_actionFadeOut;

	private final Action m_actionDialog = new Action() {
		@Override
		public boolean act( float delta ) {
			m_state = eState.LOADING;
			return true;
		}
	};

	private final Action m_actionFinish = new Action() {
		@Override
		public boolean act( float delta ) {
			m_state = eState.FINISH;
			return true;
		}
	};

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public IntroScreen( final AbstractGame game ) {
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
		final String music = p_game.getProperties().getString( MannaProperties.eKey.INTRO_MUSIC );
		if( !music.isEmpty() ) {
			p_game.getMusicManager().play( music );
		}

		// preload dialogs
		m_facebookDialog = new LogonDialog( p_game, IntroScreen.this.getSkin() );
		m_googleDialog = new LogonDialog( p_game, IntroScreen.this.getSkin() );
		m_enrollDialog = new EnrollDialog( p_game, IntroScreen.this.getSkin() );
		m_logonDialog = new LogonDialog( p_game, IntroScreen.this.getSkin() );

		// get texture from loader
		final TextureRegion region = p_game.getLoader().getAtlasRegion(
				p_game.getProperties().getString( MannaProperties.eKey.DEFAULT_ATLAS ),
				p_game.getProperties().getString( MannaProperties.eKey.INTRO_IMAGE ) );

		// here we create the splash image actor; its size is set when the
		// resize() method gets called
		final Image image = new Image( new TextureRegionDrawable( region ), Scaling.stretch );
		image.setFillParent( true );

		// add the image actor to the stage
		this.getStage().addActor( image );

		// configure the fade-in/out effect on the stage
		this.getStage().getRoot().getColor().a = 0f;
		m_actionFadeIn = Actions.fadeIn( p_game.getProperties().getFloat( MannaProperties.eKey.INTRO_FADEIN ) );
		m_actionDelay = Actions.delay( p_game.getProperties().getFloat( MannaProperties.eKey.INTRO_DELAY ) );
		m_actionFadeOut = Actions.fadeOut( p_game.getProperties().getFloat( MannaProperties.eKey.INTRO_FADEOUT ) );

		// use default table
		final Table table = this.getTable();

		// show loader
		m_progress = new ProgressBar( 0f, 1f, 0.1f, false, this.getSkin(), "loader" );
		m_progress.setValue( 0.1f );
		m_progress.setHeight( 20f );
		table.add( m_progress ).width( 400f ).height( 50f ).bottom().expand();
		table.row();

		final String message = p_game.getProperties().getString( MannaProperties.eKey.INTRO_MESSAGE );
		final Label label = new Label( message.subSequence( 0, message.length() ), this.getSkin(), "caption-font", new Color( 0.75f, 0.75f, 0.75f, 1f) );
		table.add( label );
		table.row();

		table.add( " " ).height( 80f );
		table.row();

		// prepare loading sequence
		final SequenceAction action;
		action = sequence( m_actionFadeIn, m_actionDelay, m_actionDialog );

		// finally add action to stage
		this.getStage().addAction( action );
	}

	@Override
	public void render( final float delta ) {
		super.render( delta );

		// handle action states
		switch( m_state ) {
			case STARTED: {
				p_game.getLoader().update();
				final float progress = p_game.getLoader().getProgress();
				m_progress.setValue( progress );
				break;
			}
			case LOADING: {
				if( p_game.getLoader().isLoading() ) {
					p_game.getLoader().update();
					final float progress = p_game.getLoader().getProgress();
					m_progress.setValue( progress );
				} else {
					m_progress.setValue( 1f );
					m_state = eState.LOGON;
				}
				break;
			}
			case FACEBOOK:
				m_dialog = m_facebookDialog.start();
				m_state = eState.FADEOUT;
				break;
			case GOOGLE:
				m_dialog = m_googleDialog.start();
				m_state = eState.FADEOUT;
				break;
			case ENROLL:
				m_dialog = m_enrollDialog.start();
				m_state = eState.FADEOUT;
				break;
			case LOGON: {
				m_dialog = m_logonDialog.start();
				m_state = eState.FADEOUT;
				break;
			}
			case FADEOUT: {
				switch( m_dialog.getState() ) {
					case "DEFAULT":
					default:
						// still showing
						break;
					case "FACEBOOK": {
						m_dialog.resetState();
						m_state = eState.FACEBOOK;
						break;
					}
					case "GOOGLE": {
						m_dialog.resetState();
						m_state = eState.GOOGLE;
						break;
					}
					case "ENROLL": {
						m_dialog.resetState();
						m_state = eState.ENROLL;
						break;
					}
					case "SUCCESS": {
						// load finished - set fade out
						this.getStage().addAction( sequence( m_actionFadeOut, m_actionFinish ) );
						break;
					}
					case "FAILED":
						// start dialog again
						m_dialog.resetState();
						m_state = eState.LOGON;
						break;
					case "ESCAPED":
						// user escaped - signal exit
						m_dialog.resetState();
						m_state = eState.STARTED; // reset for possible rerun
						p_game.processInput( IntroScreen.this, "OutroScreen", false );
						break;
				}
				break;
			}
			case FINISH: {
				m_dialog.resetState();
				m_state = eState.STARTED; // reset for possible rerun
				p_game.processInput( IntroScreen.this, "LobbyScreen", false );
				break;
			}
			default:
				break;
		}
	}

	@Override
	public void dispose() {
		super.dispose();

	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
