/**
 * By: IanF on 12/05/13 08:54
 */

package com.upiva.gdx.general.gam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.scr.AbstractScreen;

public class GameScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public GameScreen( final AbstractGame game ) throws AssertionError {
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
		final Label label = new Label( GEN.charSequence( "<default gam>" ), this.getSkin(), "caption-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( label ).spaceBottom( 50 );
		table.row();

	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
