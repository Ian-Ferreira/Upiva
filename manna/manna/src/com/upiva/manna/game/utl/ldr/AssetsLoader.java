/**
 * By: IanF on 13/04/13 09:20
 */
package com.upiva.manna.game.utl.ldr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetsLoader {

	private final AssetManager m_manager;
	private final String m_path;
	private final boolean m_logAssets;

	public AssetsLoader( final String path, final boolean logAssets ) {
		// preserve args
		m_path = path;
		m_logAssets = logAssets;
		// our file resolver
		m_manager = new AssetManager( new FileHandleResolver() {
			@Override
			public FileHandle resolve( final String s ) {
				if( s.startsWith( m_path ) ) {
					return Gdx.files.internal( s );
				} else {
					final String file = m_path + s;
					if( m_logAssets )
						Gdx.app.log( AssetsLoader.class.getSimpleName(), "asset: " + file );
					return Gdx.files.internal( file );
				}
			}
		} );
	}

	public boolean isLoading() {
		return m_manager.getQueuedAssets() > 0;
	}

	public float getProgress() {
		return m_manager.getProgress();
	}

	public TextureRegion getFileRegion( final String asset ) {
		m_manager.load( asset, Texture.class );
		while( !m_manager.isLoaded( asset ) ) {
			m_manager.finishLoading();
		}
		final Texture texture = m_manager.get( asset, Texture.class );
		texture.setFilter( Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );
		return new TextureRegion( texture, 0, 0, 512, 275 );
	}

	public TextureRegion getAtlasRegion( final String atlas, final String asset ) {
		m_manager.load( atlas, TextureAtlas.class );
		while( !m_manager.isLoaded( atlas ) ) {
			m_manager.finishLoading();
		}
		final TextureAtlas textureAtlas = m_manager.get( atlas, TextureAtlas.class );
		TextureAtlas.AtlasRegion region = textureAtlas.findRegion( asset );
		return new TextureRegion( region, 0, 0, 512, 275 );
	}

	public Music getMusic( final String asset ) {
		m_manager.load( asset, Music.class );
		while( !m_manager.isLoaded( asset ) ) {
			m_manager.finishLoading();
		}
		return m_manager.get( asset, Music.class );
	}

	public Sound getSound( final String asset ) {
		m_manager.load( asset, Sound.class );
		while( !m_manager.isLoaded( asset ) ) {
			m_manager.finishLoading();
		}
		return m_manager.get( asset, Sound.class );
	}

	public TextureAtlas getAtlas( final String asset ) {
		m_manager.load( asset, TextureAtlas.class );
		while( !m_manager.isLoaded( asset ) ) {
			m_manager.finishLoading();
		}
		return m_manager.get( asset, TextureAtlas.class );
	}

	public Skin getSkin( final String asset ) {
		m_manager.load( asset, Skin.class );
		while( !m_manager.isLoaded( asset ) ) {
			m_manager.finishLoading();
		}
		return m_manager.get( asset, Skin.class );
	}

	public void dispose() {
		m_manager.clear();
	}

	public void preload( final String file, final String clzz ) {
		final Class<?> clss;
		try {
			clss = Class.forName( clzz );
			m_manager.load( file, clss );
		} catch( ClassNotFoundException e ) {
			e.printStackTrace();
		}
	}

	public void update() {
		m_manager.update( 100 );
	}
}
