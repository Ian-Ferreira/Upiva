/**
 * By: IanF on 05/05/13 10:19
 */

package com.upiva.gdx.general.gam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.scr.AbstractScreen;

public class AboutScreen extends AbstractScreen {

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public AboutScreen( final AbstractGame game ) throws AssertionError {
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
		final Label label = new Label( GEN.charSequence( "Authored by Ian Ferreira (c) 2013" ), this.getSkin(), "caption-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( label ).top().spaceBottom( 50 );
		table.row();

		// exit message
		final Label prompt = new Label( GEN.charSequence( "Press BACK or ESCAPE to exit..." ), this.getSkin(), "message-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( prompt ).bottom();
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
