/**
 * By: IanF on 12/05/13 08:57
 */

package com.upiva.gdx.general.gam;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.upiva.common.utl.GEN;
import com.upiva.manna.game.gam.AbstractGame;
import com.upiva.manna.game.scr.AbstractScreen;

public class ScoresScreen extends AbstractScreen {


	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ScoresScreen( final AbstractGame game ) throws AssertionError {
		super( game );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	public void show() {
		super.show();

		// use default table
		final Table table = this.getTable();

		// caption
		final Label label = new Label( GEN.charSequence( "High Scores" ), this.getSkin(), "caption-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( label ).top().spaceBottom( 50 );
		table.row();

		// exit message
		final Label prompt = new Label( GEN.charSequence( "Press BACK or ESCAPE to exit..." ), this.getSkin(), "message-font", new Color( 1f, 1f, 1f, 1f ) );
		table.add( prompt ).bottom();
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers


}
