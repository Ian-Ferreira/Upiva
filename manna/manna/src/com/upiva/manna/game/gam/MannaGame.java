package com.upiva.manna.game.gam;

import com.upiva.gdx.general.gam.*;
import com.upiva.manna.game.scr.AbstractScreen;
import com.upiva.manna.game.scr.IntroScreen;
import com.upiva.manna.game.scr.LobbyScreen;
import com.upiva.manna.game.scr.OutroScreen;
import com.upiva.manna.game.utl.gen.IGameCallback;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MannaGame extends AbstractGame implements IGameCallback {

	///////////////////////////////////////////////////////////////////////////
	// Constant data

	public static final String s_domain = "MannaGames";
	public static final String s_version = "1.0.0.0";

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public MannaGame( final MannaProperties properties ) {
		super( properties );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// GdxGame

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	/// GdxGame

	@Override
	public String getDomain() {
		return s_domain;
	}

	@Override
	public String getVersion() {
		return s_version;
	}

	@Override
	public AbstractScreen doProgressScreen( final AbstractScreen source, final String target ) {
		// check request exit
		if( target == null )
			return null;

		// select by target
		switch( target ) {
			case "IntroScreen": {
				return new IntroScreen( this );
			}
			case "LobbyScreen": {
				return new LobbyScreen( this );
			}
			case "ScoresScreen": {
				return new ScoresScreen( this );
			}
			case "OptionsScreen": {
				return new OptionsScreen( this );
			}
			case "AboutScreen": {
				return new AboutScreen( this );
			}
			case "OutroScreen": {
				// request from intro screen - user escaped on logon
				if( source.toString().equals( "IntroScreen" ) ) {
					// todo: show outro or just exit
					// signal exit
					return new OutroScreen( this );
				}

				// request from outro screen
				if( source.toString().equals( "OutroScreen" ) ) {
					// signal exit
					return null;
				}

				// request from menu screen
				if( source.toString().equals( "MenuScreen" ) ) {
					// todo: prompt user for exit
					//...
					// want splash outro
					if( !m_properties.getBoolean( MannaProperties.eKey.DEBUG_MODE) && m_properties.getBoolean( MannaProperties.eKey.OUTRO_ENABLED ) ) {
						return new OutroScreen( this );
					} else {
						// some delay - ist all to fast gone
						try {
							Thread.sleep( 100 );
						} catch( InterruptedException e ) {
							// n/a
						}
					}
					// signal exit
					return null;
				}
				// else return to menu
				return new MenuScreen( this );
			}
			case "GameScreen": {
				try {
					Class<? extends GameScreen> clss = ( Class<? extends GameScreen> )Class.forName( m_properties.getString( MannaProperties.eKey.GAME_MAINSCREENCLASS ) );
					Constructor<? extends GameScreen> cons = clss.getConstructor( AbstractGame.class );
					return cons.newInstance( this );
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
				}
				// fall-through
			}
			default:
				log( "Undefined screen request '" + target + "' called from '" + source + "'" );
				break;
		}
		// continue screen
		return source;
	}

	@Override
	public void doPersistProfile() {
		// store user stuff and gam state
		//...
	}

	@Override
	public void doDisposeGame() {
		// be kind to the environment - clean up
		//...
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers
}
