/**
 * By: IanF on 01/05/13 19:43
 */

package com.upiva.manna.game.scr;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.gam.MannaProperties;

public abstract class AbstractScreen implements Screen {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	protected final AbstractGame p_game;
	protected final Stage p_stage;

	private float m_width;
	private float m_height;
	private Color m_background;

	private String m_music;
	private SpriteBatch m_batch;
	private Skin m_skin;
	private Table m_table;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public AbstractScreen( final AbstractGame game ) throws AssertionError {
		super();

		// preserve args
		p_game = game;

		// default music
		m_music = p_game.getProperties().getString( MannaProperties.eKey.MUSIC_DEFAULT );

		// background color
		m_background = GEN.colorFromHexString( p_game.getProperties().getString( MannaProperties.eKey.STAGE_RGBA ) );

		// prep sizes
		m_width = Gdx.graphics.getWidth();
		m_height = Gdx.graphics.getHeight();
		if( Gdx.app.getType().equals( Application.ApplicationType.Android ) ) {
			m_width = p_game.getProperties().getFloat( MannaProperties.eKey.STAGE_WIDTH );
			m_height = p_game.getProperties().getFloat( MannaProperties.eKey.STAGE_HEIGHT);
		}

		// create stage
		p_stage = new Stage( m_width, m_height, true );

	}

	///////////////////////////////////////////////////////////////////////////
	// Enforced

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	/// Screen

	@Override
	public void show() {
		log( "Showing screen" );

		// start play music - if not playing already
		p_game.getMusicManager().play( m_music );

		// set the stage as the input processor
		p_game.getMultiplexer().addProcessor( p_stage );
	}

	@Override
	public void resize( final int width, final int height ) {
		log( "Resizing screen to: " + width + " x " + height );
	}

	@Override
	public void render( final float delta ) {
		//log( "Rendering screen" ); // don't waste fps and disk gotSpace

		// update the actors
		p_stage.act( delta );

		// clear screen
		Gdx.gl.glClearColor( m_background.r, m_background.g, m_background.b, m_background.a );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		// draw the actors
		p_stage.draw();

		// draw the m_table debug lines
		if( p_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_TABLE ) )
			Table.drawDebug( p_stage );
	}

	@Override
	public void hide() {
		log( "Hiding screen" );

		// dispose the screen when leaving the screen;
		// note that the dispose() method is not called automatically by the
		// framework, so we must figure out when it's appropriate to call it
		this.dispose();
	}

	@Override
	public void pause() {
		log( "Pausing screen" );
	}

	@Override
	public void resume() {
		log( "Resuming screen" );
	}

	@Override
	public void dispose() {
		log( "Disposing screen" );

		// uninstall input
		p_game.getMultiplexer().removeProcessor( p_stage );

		// the following call disposes the screen's stage, but on my computer it
		// crashes the gam so I commented it out; more info can be found at:
		// http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3624
		//p_stage.dispose();
	}

	/// Object

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public AbstractGame getGame() {
		return p_game;
	}

	public float getWidth() {
		return m_width;
	}

	public void setWidth( final float width ) {
		m_width = width;
	}

	public float getHeight() {
		return m_height;
	}

	public void setHeight( final float height ) {
		m_height = height;
	}

	public Stage getStage() {
		return p_stage;
	}

	public String getMusic() {
		return m_music;
	}

	public void setMusic( final String music ) {
		m_music = music;
	}

	public Color getBackground() {
		return m_background;
	}

	public void setBackground( final Color background ) {
		m_background = background;
	}

	public SpriteBatch getBatch() {
		if( m_batch == null ) {
			m_batch = new SpriteBatch();
		}
		return m_batch;
	}

	protected Skin getSkin() {
		if( m_skin == null ) {
			final String asset = p_game.getProperties().getString( MannaProperties.eKey.DEFAULT_SKIN );
			if( !asset.isEmpty() ) {
				m_skin = p_game.getLoader().getSkin( asset );
			}
		}
		return m_skin;
	}

	protected Table getTable() {
		if( m_table == null ) {
			m_table = new Table( this.getSkin() );
			m_table.setFillParent( true );
			if( p_game.getProperties().getBoolean( MannaProperties.eKey.DEBUG_TABLE ) ) {
				m_table.debug();
			}
			p_stage.addActor( m_table );
		}
		return m_table;
	}

	public final void log( final String msg ) {
		Gdx.app.log( this.getClass().getSimpleName(), msg );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
