/**
 * Authored By: IanF on 09/06/13 16:02
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 09/06/13 16:02: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.drv;

import org.w3c.dom.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DriversManager {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	final List<IDriverService> m_drivers = Collections.synchronizedList( new ArrayList<IDriverService>( 4 ) );

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DriversManager( final String clazz, final String name, final String qualifier, final String consumer, final Node context ) {
		try {
			// extract class
			final Class<?> clss = Class.forName( clazz );
			// extract constructor
			final Constructor<?> cons = clss.getConstructor( String.class, String.class, String.class, Node.class );
			// construct service
			final IDriverService driver = ( IDriverService )cons.newInstance( name, qualifier, consumer, context );
			// add to list
			m_drivers.add( driver );
			// and start it
			driver.start();
		} catch( ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e ) {
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
