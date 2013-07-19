/**
 * Authored By: IanF on 04/06/13 09:59
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 09:59: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr;

import com.upiva.manna.server.IMannaServer;

import java.lang.reflect.Constructor;

public interface IServerConnector {

	///////////////////////////////////////////////////////////////////////////
	// Inner classes

	public static final class Factory {

		public static IServerConnector connector() {
			final IServerConnector connector = new IServerConnector() {
				@Override
				public IMannaServer connect( final String context, final String homepath ) {
					// validate
					final int split;
					if( ( context == null ) || context.isEmpty() || ( ( split = context.indexOf( ":" ) ) < 1 ) || ( ( split > context.length() - 1 ) ) )
						throw new IllegalArgumentException( "Illegal server connector context: " + context );
					// response
					IMannaServer server = null;
					// extract action
					final String action = context.substring( 0, split );
					switch( action ) {
						case "embed": {
							/// construct new server locally
							// extract args
							final String params = context.substring( split + 1 );
							final String[] tokens = params.split( "%" );
							if( ( tokens == null ) || ( tokens.length != 2 ) )
								throw new IllegalArgumentException( "Illegal server connector context: " + context );
							// extract params
							final String clzz = tokens[ 0 ];
							final String cnfg = homepath + "/" + tokens[ 1 ];
							try {
								// extract server class
								final Class<?> clss = Class.forName( clzz );
								// extract constructor
								final Constructor<?> cons = clss.getConstructor( String.class, String.class );
								// construct server using constructor
								server = ( IMannaServer )cons.newInstance( homepath, cnfg );
							} catch( Exception e ) {
								e.printStackTrace();
								throw new IllegalArgumentException( e.toString() + "; Illegal server connector context: " + context );
							}
							break;
						}
						// todo: udp, rpc, amqp, etc...
						default:
							throw new IllegalArgumentException( "Illegal server connector action: " + context );
					}
					// return new instance
					return server;
				}
			};
			return connector;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Implements

	IMannaServer connect( final String context, final String homepath );

}
