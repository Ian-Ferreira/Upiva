/**
 * By: IanF on 05/05/13 10:18
 */

package com.upiva.gdx.general.gam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.scr.AbstractScreen;

import java.util.Locale;

public class OptionsScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private Label m_musicVolume;
	private Label m_soundVolume;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public OptionsScreen( final AbstractGame game ) throws AssertionError {
		super( game );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public void show() {
		super.show();

		// use default table
		final Table table = this.getTable();

		// caption
		final Label label = new Label( GEN.charSequence( "OPTIONS" ), this.getSkin(), "caption-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( label ).colspan( 3 ).top().spaceBottom( 50 );
		table.row();

		// MUSIC

		// label
		table.add( "Music Enabled" ).left();
		//checkbox
		final CheckBox musicCheckbox = new CheckBox( "", getSkin() );
		musicCheckbox.setChecked( p_game.getMusicManager().isEnabled() );
		musicCheckbox.addListener( new ChangeListener() {
			@Override
			public void changed( ChangeEvent event, Actor actor ) {
				boolean enabled = musicCheckbox.isChecked();
				p_game.getMusicManager().setEnabled( enabled );
				p_game.getSoundManager().playDefault();

				// if the music is now enabled, start playing the menu music
				if( enabled ) {
					p_game.getMusicManager().playDefault();
				}
			}
		} );
		table.add( musicCheckbox ).colspan( 2 ).center();
		table.row();

		// label
		table.add( "Music Volume" ).left();
		// slider
		final Slider musicSlider = new Slider( 0f, 1f, 0.1f, false, this.getSkin() );
		musicSlider.setValue( p_game.getPreferences().getMusicVolume() );
		musicSlider.addListener( new ChangeListener() {
			@Override
			public void changed( ChangeEvent event, Actor actor ) {
				float value = ( ( Slider )actor ).getValue();
				p_game.getMusicManager().setVolume( value );
				_updateMusicVolumeLabel();
			}
		} );
		table.add( musicSlider );
		// strength
		m_musicVolume = new Label( "", getSkin() );
		_updateMusicVolumeLabel();
		table.add( m_musicVolume ).width( 50 );
		table.row();

		table.add( " " ).colspan( 3 );
		table.row();

		// SOUND

		// label
		table.add( "Sound Effects" ).left();
		// sound checkbox
		final CheckBox soundCheckbox = new CheckBox( "", getSkin() );
		soundCheckbox.setChecked( p_game.getSoundManager().isEnabled() );
		soundCheckbox.addListener( new ChangeListener() {
			@Override
			public void changed( ChangeEvent event, Actor actor ) {
				boolean enabled = soundCheckbox.isChecked();
				p_game.getSoundManager().setEnabled( enabled );
				p_game.getSoundManager().playDefault();
			}
		} );
		table.add( soundCheckbox ).colspan( 2 ).center();
		table.row();

		// label
		table.add( "Sound Volume" ).left();
		final Slider soundSlider = new Slider( 0f, 1f, 0.1f, false, getSkin() );
		soundSlider.setValue( p_game.getPreferences().getSoundVolume() );
		soundSlider.addListener( new ChangeListener() {
			@Override
			public void changed( ChangeListener.ChangeEvent event, Actor actor ) {
				float value = ( ( Slider )actor ).getValue();
				p_game.getSoundManager().setVolume( value );
				_updateSoundVolumeLabel();
			}
		} );
		table.add( soundSlider );
		// strength
		m_soundVolume = new Label( "", getSkin() );
		_updateSoundVolumeLabel();
		table.add( m_soundVolume ).width( 50 );
		table.row();

		// exit message
		final Label prompt = new Label( GEN.charSequence( "Press BACK or ESCAPE to exit..." ), this.getSkin(), "message-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( prompt ).colspan( 3 ).bottom().spaceTop( 50 );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private void _updateMusicVolumeLabel() {
		float volume = ( p_game.getMusicManager().getVolume() * 100 );
		m_musicVolume.setText( String.format( Locale.US, "%1.0f%%", volume ) );
	}

	private void _updateSoundVolumeLabel() {
		float volume = ( p_game.getSoundManager().getVolume() * 100 );
		m_soundVolume.setText( String.format( Locale.US, "%1.0f%%", volume ) );
	}

}
