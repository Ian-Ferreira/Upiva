/**
 * By: IanF on 04/05/13 18:33
 */

package com.upiva.manna.game.gam;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.FPSLogger;
import com.upiva.manna.game.scr.AbstractScreen;
import com.upiva.manna.game.utl.gen.GamePreferences;
import com.upiva.manna.game.utl.gen.IGameCallback;
import com.upiva.manna.game.utl.ldr.AssetsLoader;
import com.upiva.manna.game.utl.mgr.MusicManager;
import com.upiva.manna.game.utl.mgr.SoundManager;
import com.upiva.manna.game.utl.svr.BrokerAgent;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class AbstractGame extends Game implements InputProcessor, IGameCallback {

	///////////////////////////////////////////////////////////////////////////
	// Constant & statics data

	///////////////////////////////////////////////////////////////////////////
	// Data members

	protected final MannaProperties m_properties;

	private BrokerAgent m_broker;
	private AssetsLoader m_loader;
	private GamePreferences m_prefs;
	private MusicManager m_music;
	private SoundManager m_sound;
	private FPSLogger m_fpsLogger;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public AbstractGame( final MannaProperties properties ) throws AssertionError {
		super();

		// preserve args
		m_properties = properties;

		// initialize broker outside of gdx gam init to be thread independent
		try {
			m_broker = BrokerAgent.newInstance( this );
		} catch( ClassNotFoundException e ) {
			e.printStackTrace();
		} catch( NoSuchMethodException e ) {
			e.printStackTrace();
		} catch( InvocationTargetException e ) {
			e.printStackTrace();
		} catch( InstantiationException e ) {
			e.printStackTrace();
		} catch( IllegalAccessException e ) {
			e.printStackTrace();
		} catch( UnknownHostException e ) {
			e.printStackTrace();
		} catch( SocketException e ) {
			e.printStackTrace();
		} catch( MalformedURLException e ) {
			e.printStackTrace();
		}


	}

	///////////////////////////////////////////////////////////////////////////
	// Enforced

	public abstract String getDomain();

	public abstract String getVersion();

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// Game

	@Override
	public void create() {
		log( "Creating game on " + Gdx.app.getType() );

		// loader
		m_loader = new AssetsLoader( m_properties.getString( MannaProperties.eKey.ASSETS_PATH ), m_properties.getBoolean( MannaProperties.eKey.DEBUG_MODE ) );
		final String[] assets = m_properties.getStringArray( MannaProperties.eKey.ASSETS_PRELOAD, ";" );
		for( String asset : assets ) {
			final String[] tokens = asset.split( ":" );
			m_loader.preload( tokens[ 0 ], tokens[ 1 ] );
		}

		// create managers
		m_prefs = new GamePreferences( m_properties.getString( MannaProperties.eKey.GAME_NAME ), m_properties );
		m_music = new MusicManager( m_loader, m_prefs, m_properties.getString( MannaProperties.eKey.MUSIC_DEFAULT ) );
		m_sound = new SoundManager( m_loader, m_prefs, m_properties.getString( MannaProperties.eKey.SOUND_DEFAULT) );

		// create the helper objects
		if( m_properties.getBoolean( MannaProperties.eKey.DEBUG_MODE ) && m_properties.getBoolean( MannaProperties.eKey.DEBUG_SHOW_FPS ) ) {
			m_fpsLogger = new FPSLogger();
		}

		// set input capture handlers
		Gdx.input.setCatchBackKey( true );
		Gdx.input.setCatchMenuKey( true );
		final InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor( this );
		Gdx.input.setInputProcessor( multiplexer );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	/// Game

	@Override
	public void resize( final int width, final int height ) {
		log( "Resizing gam to: " + width + " * " + height );

		// let super resize
		super.resize( width, height );

		// show the splash screen when the gam is resized for the first time;
		// this approach avoids calling the screen's resize method repeatedly
		if( this.getScreen() == null ) {
			this.processInput( null, "IntroScreen", false );
		}
	}

	@Override
	public void render() {
		super.render();  // this calls render into the screen

		// output the current FPS
		if( m_fpsLogger != null )
			m_fpsLogger.log();

	}

	@Override
	public void pause() {
		log( "Pausing gam" );
		super.pause();

		// persist the profile, because we don't know if the player will come back
		this.doPersistProfile();
	}

	@Override
	public void resume() {
		log( "Resuming gam" );
		super.resume();
	}

	@Override
	public void dispose() {
		log( "Disposing gam" );
		super.dispose();

		// dispose screens
		this.doDisposeGame();

		// dispose locals
		if( m_loader != null )
			m_loader.dispose();
	}

	//// - finals

	@Override
	public final void setScreen( final Screen screen ) {
		// set new screen
		log( "Setting screen: " + screen );
		// screen is null ? request to exit
		if( screen == null ) {
			super.setScreen( null );
			Gdx.app.exit();
		}
		// if current screen - ignore TODO: this might be a bug because it adds to the menu screen table
		final Screen current = super.getScreen();
		if( ( current != null ) && current.toString().equals( screen.toString() ) )
			return;
		// set new screen
		super.setScreen( screen );
	}

	/// InputProcessor

	@Override
	public boolean keyDown( final int key ) {
		return false;
	}

	@Override
	public boolean keyUp( final int key ) {
		// extract current screen
		final AbstractScreen curscr = ( AbstractScreen )this.getScreen();
		// filter for screen change keys
		switch( key ) {
			case Input.Keys.BACK:
			case Input.Keys.ESCAPE: {
				processInput( curscr, "OutroScreen", false );
				return true;
			}
			case Input.Keys.MENU: {
				processInput( curscr, "MenuScreen", false );
				return true;
			}
			default:
				break;
		}
		return false;
	}

	@Override
	public boolean keyTyped( final char chr ) {
		return false;
	}

	@Override
	public boolean touchDown( final int x, final int y, final int pointer, final int button ) {
		return false;
	}

	@Override
	public boolean touchUp( final int x, final int y, final int pointer, final int button ) {
		return false;
	}

	@Override
	public boolean touchDragged( final int x, final int y, final int pointer ) {
		return false;
	}

	@Override
	public boolean mouseMoved( final int x, final int y ) {
		return false;
	}

	@Override
	public boolean scrolled( final int amount ) {
		return false;
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public void processInput( final AbstractScreen screen, final String target, final boolean sound ) {
		// want default sound
		if( sound ) {
			m_sound.playDefault();
		}
		// gam callback for new screen
		final AbstractScreen newscr = this.doProgressScreen( screen, target );
		// set screen, will exit gam if null...
		this.setScreen( newscr );
	}

	public AssetsLoader getLoader() {
		return m_loader;
	}

	public BrokerAgent getBroker() {
		return BrokerAgent.getInstance();
	}

	public InputMultiplexer getMultiplexer() {
		return ( ( InputMultiplexer )Gdx.input.getInputProcessor() );
	}

	public MannaProperties getProperties() {
		return m_properties;
	}

	public GamePreferences getPreferences() {
		return m_prefs;
	}

	public MusicManager getMusicManager() {
		return m_music;
	}

	public SoundManager getSoundManager() {
		return m_sound;
	}

	///////////////////////////////////////////////////////////////////////////
	// Protected methods

	/**
	 * AbstractGame derived's can log a gam tagged message
	 *
	 * @param msg
	 */
	protected final void log( final String msg ) {
		Gdx.app.log( this.getClass().getSimpleName(), msg );
	}

	public String getIPAddress() {
		// todo: find real ip address
		return "127.0.0.1";
	}

	/////////////////////////////////////////////////////////////////////////
	// Private helpers


	///////////////////////////////////////////////////////////////////////////
	// Static utilities

}
