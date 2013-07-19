 /**
 * By: IanF on 24/03/13 10:14
 */
 package com.upiva.manna.game.utl.mgr;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.audio.Music;
 import com.badlogic.gdx.utils.Disposable;
 import com.upiva.manna.game.utl.gen.GamePreferences;
 import com.upiva.manna.game.utl.ldr.AssetsLoader;

 public class MusicManager implements Disposable {

	private class _Playing {

		private final String m_asset;
		private final Music m_music;

		public _Playing( final String asset, final Music music ) {
			m_asset = asset;
			m_music = music;
		}
	}

	private final AssetsLoader m_loader;
	private final GamePreferences m_prefs;
	private final String m_music;

	private _Playing m_playing = null;

	public MusicManager( final AssetsLoader loader, final GamePreferences prefs, final String music ) {
		// preserve args
		m_loader = loader;
		m_prefs = prefs;
		m_music = music;
	}

	@Override
	public void dispose() {
		// stop will do disposing
		this.stop();
	}

	public void play( final String asset ) {
		// check if the music is enabled
		if( !m_prefs.isMusicEnabled() ) {
			return;
		}

		// check if the given music is already being played
		if( m_playing != null ) {
			if( m_playing.m_asset.equals( asset ) ) {
				return;
			} else {
				// stop any old music being played
				this.stop();
			}
		}

		try {
			// start streaming the new music
			final Music music = m_loader.getMusic( asset );
			music.setVolume( m_prefs.getMusicVolume() );
			music.setLooping( true );
			music.play();

			// play the new music
			m_playing = new _Playing( asset, music );

		} catch( Exception e ) {
			Gdx.app.error( MusicManager.class.getSimpleName(), "Music resource not found: " + asset );
		}
	}

	public void playDefault() {
		this.play( m_music );
	}

	public void stop() {
		// stops and disposes the current music being played, if any.
		if( m_playing != null ) {
			m_playing.m_music.stop();
			m_playing.m_music.dispose();
			m_playing = null;
		}
	}

	public boolean isEnabled() {
		return m_prefs.isMusicEnabled();
	}

	public void setEnabled( final boolean enabled ) {
		m_prefs.setMusicEnabled( enabled );

		// if the music is being deactivated, stop any music being played
		if( ! enabled ) {
			this.stop();
		}
	}

	public float getVolume() {
		return m_prefs.getMusicVolume();
	}

	public void setVolume( final float volume ) {
		// check and set the new volume
		if( volume < 0 || volume > 1f ) {
			throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
		}
		m_prefs.setMusicVolume( volume );

		// if there is a music being played, change its volume
		if( m_playing != null ) {
			m_playing.m_music.setVolume( volume );
		}
	}
}
