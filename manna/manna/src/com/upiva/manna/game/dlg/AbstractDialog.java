/**
 * Authored By: IanF on 17/05/13 18:26
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 17/05/13 18:26: Created, IanF, ...
 *
 */

package com.upiva.manna.game.dlg;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.scr.AbstractScreen;
import com.upiva.manna.game.utl.gen.IDialogState;

public abstract class AbstractDialog extends Dialog {

	///////////////////////////////////////////////////////////////////////////
	// Constant data

	public static enum eState implements IDialogState {
		DEFAULT(),
		SUCCESS(),
		FAILED(),
		ESCAPED(),;

		@Override
		public String toString() {
			return this.name().toUpperCase();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Data members

	// host
	protected final AbstractGame p_game;

	// state carrier
	protected IDialogState p_state = eState.DEFAULT;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public AbstractDialog( final String title, final Skin skin, final AbstractGame game ) {
		super( title, skin );
		p_game = game;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public AbstractDialog start() {
		this.show( ( ( AbstractScreen )p_game.getScreen() ).getStage() );
		this.setVisible( true );
		return this;
	}

	public String getState() {
		return p_state.toString();
	}

	public String resetState() {
		p_state = eState.DEFAULT;
		return p_state.toString();
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
