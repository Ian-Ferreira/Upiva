/**
 * By: IanF on 14/04/13 13:27
 */

package com.upiva.manna.game.gam;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.upiva.common.dbo.Token;
import com.upiva.gdx.general.gam.GameScreen;
import com.upiva.manna.game.utl.gen.IPropertyKey;

import java.util.Properties;

public class MannaProperties extends Properties {

	///////////////////////////////////////////////////////////////////////////
	// Constant data

	public enum eKey implements IPropertyKey {
		// who'ever can't reads this' should not be fiddling here...
		DEBUG_MODE( false ),
		DEBUG_SHOW_FPS( false ),
		DEBUG_TABLE( true ),
		DEBUG_SERVER( true ),

		// os level gam startup options
		GAME_NAME( "<undefined>" ),
		GAME_USEGL20( true ),

		// desktop only
		GAME_WIDTH( 768 ),
		GAME_HEIGHT( 512 ),
		GAME_RGBA( "0x000000ff" ),
		GAME_DEPTH( 16 ),
		GAME_FULLSCREEN( false ),
		GAME_FORCEEXIT( true ),
		GAME_RESIZABLE( true ),
		GAME_USECPUSYNC( true ),
		GAME_VSYNCENAB( true ),

		//android only
		GAME_LIFEWALLPAPER( false ),
		GAME_HIDESTATUSBAR( false ),
		GAME_MAXSIMULTANEOUSSOUNDS( 5 ),
		GAME_NUMSAMPLES( 16 ),
		GAME_STENCIL( 0 ),
		GAME_TOUCHSLEEPTIME( 15 ),
		GAME_USEACCELEROMETER( false ),
		GAME_USECOMPASS( false ),
		GAME_USEWAKELOCK( false ),

		// server connection
		SERVER_REMOTE_ADDRESS( "http://localhost:4444/mannaJson" ),
		SERVER_CONNECT_METHOD( "GET" ),
		SERVER_CONNECT_CONTENT( "application/x-www-form-urlencoded;charset=UTF-8" ),
		SERVER_CONNECT_LANGUAGE( "en-ZA" ),
		SERVER_CONNECT_CHARSET( "UTF-8" ),
		SERVER_CONNECT_ACCEPT( "text/html" ),
		SERVER_CONNECT_DO_INPUT( true ),
		SERVER_CONNECT_DO_OUTPUT( true ),
		SERVER_CONNECT_USE_CACHES( false ),
		SERVER_CONNECT_TIMEOUT( 60000 ),
		SERVER_DBO_ENTITY_CLASSPATH( Token.class.getName().substring( 0, Token.class.getName().lastIndexOf( "." ) ) ),
		SERVER_BASE64_ENCODE_VALUES( true ),

		// main gam screen
		GAME_MAINSCREENCLASS( GameScreen.class.getName() ),

		// loader
		ASSETS_PATH( "./manna-android/assets/" ),
		ASSETS_PRELOAD(
				"atlases/manna.atlas:" + TextureAtlas.class.getName() + ";" +
				"music/default.ogg:" + Music.class.getName() + ";" +
				"sounds/select.mp3:" + Sound.class.getName() + ";" +
				"skins/uiskin.json:" + Skin.class.getName() + ";" +
				"atlases/game1/game1.atlas:" + TextureAtlas.class.getName() + ";" +
				"atlases/game2/game2.atlas:" + TextureAtlas.class.getName() + ";" +
				"atlases/game3/game3.atlas:" + TextureAtlas.class.getName() + ";" ),

		// defaults
		MUSIC_DEFAULT( "music/default.ogg" ),
		MUSIC_ENABLED( true ),
		MUSIC_VOLUME( 0.1f ),
		SOUND_DEFAULT( "sounds/select.mp3" ),
		SOUND_ENABLED( true ),
		SOUND_VOLUME( 0.9f ),

		// default assets
		DEFAULT_SKIN( "skins/manna.json" ),
		DEFAULT_ATLAS( "atlases/manna.atlas" ),

		// default ui stuff
		STAGE_WIDTH( 480 ),
		STAGE_HEIGHT( 320 ),
		STAGE_RGBA( "0x880000ff" ),

		// intro screen
		INTRO_IMAGE( "intro" ),
		INTRO_MUSIC( "music/agent_orange.ogg" ),
		INTRO_FADEIN( 1.5f ),
		INTRO_DELAY( 2f ),
		INTRO_FADEOUT( 2.5f ),
		INTRO_LOADER( true ),
		INTRO_MESSAGE( "LOADING ASSETS..." ), // TODO: process into language packs

		// lobby screen
		LOBBY_IMAGE( "lobby" ),
		LOBBY_MUSIC( "music/socialdistortionballandchain.ogg" ),

		// outro screen
		OUTRO_ENABLED( true ),
		OUTRO_IMAGE( "outro" ),
		OUTRO_MUSIC( "" ),
		OUTRO_FADEIN( 1.5f ),
		OUTRO_DELAY( 2f ),
		OUTRO_FADEOUT( 1.5f ),;

		// default newValue carrier
		private final Object m_value;

		// constructor
		eKey( final Object value ) {
			m_value = value;
		}

		// getter for default on property
		public Object getDefault() {
			return m_value;
		}

	} // End of enum eKey

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public MannaProperties() {
		super();
		// pack our hardkey defaults
		for( eKey key : eKey.class.getEnumConstants() ) {
			this.put( key.name(), key.m_value );
		}
	}

	public MannaProperties( final Properties defaults ) {
		super( defaults );
		// pack our hardkey defaults
		for( eKey key : eKey.class.getEnumConstants() ) {
			this.put( key.name(), key.m_value );
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Overide's

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	/// accessors

	public Object get( final IPropertyKey key ) {
		return this.get( key.toString() );
	}

	public void put( final IPropertyKey key, final Object value ) {
		this.put( key.toString(), value );
	}

	/// type extractors

	public String getString( final IPropertyKey key ) {
		return ( String )this.get( key );
	}

	public String[] getStringArray( final eKey key, final String split ) {
		return this.getString( key ).split( split );
	}

	public Byte getByte( final IPropertyKey key ) {
		return ( Byte )this.get( key );
	}

	public Character getChar( final IPropertyKey key ) {
		return ( Character )this.get( key );
	}

	public Short getShort( final IPropertyKey key ) {
		return ( Short )this.get( key );
	}

	public int getInt( final IPropertyKey key ) {
		return ( Integer )this.get( key );
	}

	public Long getLong( final IPropertyKey key ) {
		return ( Long )this.get( key );
	}

	public Float getFloat( final IPropertyKey key ) {
		return ( Float )this.get( key );
	}

	public Double getDouble( final IPropertyKey key ) {
		return ( Double )this.get( key );
	}

	public Boolean getBoolean( final IPropertyKey key ) {
		return ( Boolean )this.get( key );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
