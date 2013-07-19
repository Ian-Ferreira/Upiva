/**
 * By: IanF on 26/03/13 19:50
 */
package com.upiva.manna.game.utl.gen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.upiva.manna.game.gam.MannaProperties;

public class GamePreferences {

	// constants
	private static final String PREF_MUSIC_ENABLED = "music_enabled";
	private static final String PREF_MUSIC_VOLUME = "music_volume";
	private static final String PREF_SOUND_ENABLED = "sound_enabled";
	private static final String PREF_SOUND_VOLUME = "sound_volume";

	private final String m_name;

	public GamePreferences( final String name, final MannaProperties props ) {
		m_name = name;
		final Preferences prefs = Gdx.app.getPreferences( m_name );
		prefs.clear();
		if( prefs == null )
			throw new IllegalStateException( "GamePreferences must be created in the host gam's 'create' override!" );
		if( !prefs.contains( PREF_MUSIC_ENABLED ) )
			this.setMusicEnabled( props.getBoolean( MannaProperties.eKey.MUSIC_ENABLED ) );
		if( !prefs.contains( PREF_MUSIC_VOLUME ) )
			this.setMusicVolume( props.getFloat( MannaProperties.eKey.MUSIC_VOLUME) );
		if( !prefs.contains( PREF_SOUND_ENABLED ) )
			this.setSoundEnabled( props.getBoolean( MannaProperties.eKey.SOUND_ENABLED) );
		if( !prefs.contains( PREF_SOUND_VOLUME ) )
			this.setSoundVolume( props.getFloat( MannaProperties.eKey.SOUND_VOLUME ) );
	}

	protected Preferences getPrefs() {
		return Gdx.app.getPreferences( m_name );
	}

	public boolean isMusicEnabled() {
		return getPrefs().getBoolean( PREF_MUSIC_ENABLED, true );
	}

	public void setMusicEnabled( final boolean musicEnabled ) {
		getPrefs().putBoolean( PREF_MUSIC_ENABLED, musicEnabled );
		getPrefs().flush();
	}

	public float getMusicVolume() {
		return getPrefs().getFloat( PREF_MUSIC_VOLUME, 0.5f );
	}

	public void setMusicVolume( final float volume ) {
		getPrefs().putFloat( PREF_MUSIC_VOLUME, volume );
		getPrefs().flush();
	}

	public boolean isSoundEnabled() {
		return getPrefs().getBoolean( PREF_SOUND_ENABLED, true );
	}

	public void setSoundEnabled( final boolean soundEnabled ) {
		getPrefs().putBoolean( PREF_SOUND_ENABLED, soundEnabled );
		getPrefs().flush();
	}

	public float getSoundVolume() {
		return getPrefs().getFloat( PREF_SOUND_VOLUME, 0.5f );
	}

	public void setSoundVolume( final float volume ) {
		getPrefs().putFloat( PREF_SOUND_VOLUME, volume );
		getPrefs().flush();
	}

}
