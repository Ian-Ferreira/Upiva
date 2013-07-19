 /**
 * By: IanF on 24/03/13 10:14
 */
 package com.upiva.manna.game.utl.mgr;

 import com.badlogic.gdx.Gdx;
 import com.badlogic.gdx.audio.Sound;
 import com.badlogic.gdx.utils.Disposable;
 import com.upiva.manna.game.utl.gen.GamePreferences;
 import com.upiva.manna.game.utl.gen.LRUCache;
 import com.upiva.manna.game.utl.ldr.AssetsLoader;

 public class SoundManager implements LRUCache.CacheEntryRemovedListener<SoundManager.SoundKey,Sound>, Disposable {

	public class SoundKey {

		protected final String m_name;

		private SoundKey( final String name ) {
			m_name = name;
		}
	}

	private final AssetsLoader m_loader;
	private final GamePreferences m_prefs;
	private final String m_sound;
	private final LRUCache<SoundKey,Sound> m_cache;

	public SoundManager( final AssetsLoader loader, final GamePreferences prefs, final String sound ) {
		// preserve args
		m_loader = loader;
		m_prefs = prefs;
		m_sound = sound;

		// new sound cache
		m_cache = new LRUCache<SoundKey, Sound>( 10);
		m_cache.setListener( this );
	}

	@Override
	public void notifyEntryRemoved( final SoundKey key, final Sound value ) {
		// dispose cached sound
		value.dispose();
	}

	@Override
	public void dispose() {
		// dispose all cached sound
		for( Sound sound : m_cache.retrieveAll() ) {
			sound.stop();
			sound.dispose();
		}
	}

	public void play( final String asset ) {
		// check if the sounds is enabled
		if( !m_prefs.isSoundEnabled() ) {
			return;
		}

		// parse resource name

		try {
			// start streaming the new sound
			Sound resource = m_loader.getSound( asset );

			// cache new sound
			SoundKey sound = new SoundKey( asset );
			m_cache.add( sound, resource );

			// play sound with current volume
			resource.play( m_prefs.getSoundVolume() );

		} catch( Exception e ) {
			Gdx.app.error( SoundManager.class.getSimpleName(), "Sound resource not found: " + asset );
		}
	}

	public void playDefault() {
		this.play( m_sound );
	}

	public boolean isEnabled() {
		return m_prefs.isSoundEnabled();
	}

	public void setEnabled( final boolean enabled ) {
		// set enabled state
		m_prefs.setSoundEnabled( enabled );
	}

	public float getVolume() {
		return m_prefs.getSoundVolume();
	}

	public void setVolume( final float volume ) {
		// check and set the new volume
		if( volume < 0 || volume > 1f ) {
			throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
		}
		m_prefs.setSoundVolume( volume );
	}

}
