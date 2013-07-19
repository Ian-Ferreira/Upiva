/**
 * By: IanF on 04/05/13 18:29
 */

package com.upiva.manna.game.utl.gen;

import com.upiva.manna.game.scr.AbstractScreen;

public interface IGameCallback {

	/**
	 * Called by superclass to provide new progressive screen.
	 * When return newValue equals input parameter screen, no screen change will be invoked.
	 *
	 *
	 * @param source
	 * @param target
	 */
	AbstractScreen doProgressScreen( final AbstractScreen source, final String target );

	/**
	 * Called when screen is paused to persist the user and gam profile because we don't know if the user will return
	 */
	void doPersistProfile();

	/**
	 * Called by superclass to do cleanup of gam and screen data
	 */
	void doDisposeGame();

}
