package com.upiva.manna.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.MannaGame;
import com.upiva.manna.game.gam.MannaProperties;

public class Manna {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public Manna( final String[] args ) {
		// create default properties and load our gam data
		final MannaProperties props = new MannaProperties();
		props.put( MannaProperties.eKey.GAME_NAME, "Manna-Desktop" );
		//todo...

		// cmd-line config
		if( args.length > 0 ) {
			switch( args[ 0 ] ) {
				case "debug": {
					props.put( MannaProperties.eKey.DEBUG_MODE, true );
					// no break;
				}
				default: {
					break;
				}
			}
		}
		// create libgdx app
		final LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = props.getString( MannaProperties.eKey.GAME_NAME );
		cfg.useGL20 = props.getBoolean( MannaProperties.eKey.GAME_USEGL20 );
		cfg.width = props.getInt( MannaProperties.eKey.GAME_WIDTH );
		cfg.height = props.getInt( MannaProperties.eKey.GAME_HEIGHT );
		cfg.depth = props.getInt( MannaProperties.eKey.GAME_DEPTH );
		cfg.initialBackgroundColor = GEN.colorFromHexString( props.getString( MannaProperties.eKey.GAME_RGBA ) );
		cfg.fullscreen = props.getBoolean( MannaProperties.eKey.GAME_FULLSCREEN );
		cfg.forceExit = props.getBoolean( MannaProperties.eKey.GAME_FORCEEXIT );
		cfg.resizable = props.getBoolean( MannaProperties.eKey.GAME_RESIZABLE);
		cfg.useCPUSynch = props.getBoolean( MannaProperties.eKey.GAME_USECPUSYNC );
		cfg.vSyncEnabled = props.getBoolean( MannaProperties.eKey.GAME_VSYNCENAB );
		new LwjglApplication( new MannaGame( props ), cfg );
	}

	///////////////////////////////////////////////////////////////////////////
	// Main entry - gam startup

	public static void main(String[] args) {

		// cmd-line config
		if( args.length > 0 ) {
			switch( args[ 0 ] ) {
				case "atlas": {
					final TexturePacker2.Settings settings = new TexturePacker2.Settings();
					settings.pot = false;
					//TexturePacker2.process( settings, "./manna-android/etc/images/manna", "./manna-android/assets/atlases", "manna" );
					//TexturePacker2.process( "./manna-android/etc/images/game1", "./manna-android/assets/atlases/game1", "game1" );
					//TexturePacker2.process( "./manna-android/etc/images/game2", "./manna-android/assets/atlases/game2", "game2" );
					//TexturePacker2.process( "./manna-android/etc/images/game3", "./manna-android/assets/atlases/game3", "game3" );
					TexturePacker2.process( "./manna-android/etc/skin", "./manna-android/assets/skins", "manna" );
					return; // Done with app...
				}
				//				case "particle": {
				//					ParticleEditor editor = new ParticleEditor();
				//					editor.setVisible( "Particle Editor", true );
				//					return; // Done with app...
				//				}
				default: {
					break;
				}
			}
		}

		// start game
		new Manna( args );
	}
}
