/**
 * Authored By: IanF on 04/06/13 15:52
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 15:52: Created, IanF, ...
 *
 */

package com.upiva.manna.server.dom.db4o;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.upiva.common.dbo.Token;
import com.upiva.common.utl.XOR;
import com.upiva.common.utl.XmlDom;
import com.upiva.manna.server.bus.db4o.Persona;
import com.upiva.manna.server.bus.db4o.Session;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XSessionException;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.dom.AbstractDomain;
import com.upiva.manna.server.svr.dom.IDatabaseSession;
import com.upiva.manna.server.svr.dom.IDomainService;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Node;

public class DomainServiceImplDb4o extends AbstractDomain implements IDomainService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	// her MAJESTY
	private final ObjectContainer m_database;

	private final ConcurrentHashMap<String,Session> m_sessions;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DomainServiceImplDb4o( final String name, final Node context, final int poolmin, final int poolmax, final String cmdpath, final int cmdpoolmax, final long cmdtimeout ) {
		super( name, context, poolmin, poolmax, cmdpath, cmdpoolmax, cmdtimeout );

		// google builder
		m_sessions = new ConcurrentHashMap<>( poolmin );

		// extract build params from context
		final String datafile = XmlDom.getValue( context, "datafile" );
		final String username = XmlDom.getValue( context, "username" );
		final String password = XmlDom.getValue( context, "password" );

		// prep datafile
		final String source = String.format( "%s/%s", Broker.getHomePath(), datafile );
		if( Broker.getConfig().isDebugDeleteDatabase() ) {
			new File( source ).delete();
		}

		// connect our BABY
		final EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		m_database = Db4oEmbedded.openFile( config, source );

		// ensure we have a guest user
		List<Persona> list = m_database.query( new Predicate<Persona>() {
			@Override
			public boolean match( final Persona persona ) {
				return persona.getUsername().equals( "guest" );
			}
		} );
		if( list.size() < 1 ) {
			m_database.store( new Persona( "guest", "guest" ) );
			m_database.commit();
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// AbstractDomain

	@Override
	protected String doMatchSession( final String session ) {
		// loop sessions and try decrypt incoming session with token key
		final Iterator<Map.Entry<String, Session>> iter = m_sessions.entrySet().iterator();
		while( iter.hasNext() ) {
			final Map.Entry<String, Session> item = iter.next();
			final String test;
			try {
				test = XOR.decrypt( session, item.getValue().getToken().getSession() );
			} catch( Throwable throwable ) {
				return null;
			}
			if( test.equals( item.getValue().getIPAddress() ) )
				return item.getValue().getToken().getSession();
		}
		return null;
	}

	@Override
	protected String doObtainSession( final String username ) {
		final Iterator<Map.Entry<String, Session>> iter = m_sessions.entrySet().iterator();
		while( iter.hasNext() ) {
			final Map.Entry<String, Session> item = iter.next();
			if( item.getValue().getPersona().getUsername().equals( username ) )
				return item.getValue().getToken().getSession();
		}
		return null;
	}

	@Override
	protected String doAppendSession( final String username, final String password, final String ipaddress ) throws XAccessException {
		// extract persona
		final List<Persona> list = m_database.query( new Predicate<Persona>() {
			@Override
			public boolean match( final Persona persona ) {
				if( persona.match( username, password ) )
					return true;
				return false;
			}
		} );
		// valid user
		if( list.size() < 1 )
			throw new XAccessException( "Invalid user access" );
		// create new session
		final Session session = new Session( ipaddress, list.get( 0 ) );
		m_database.store( session );
		m_database.commit();
		//List<Session> check = m_database.query( Session.class );
		// add to map
		final String key = session.getToken().getSession();
		m_sessions.put( key, session );
		return key;
	}

	@Override
	protected String doDeleteSession( final String session ) throws XSessionException {
		// remove from map
		final Session remove = m_sessions.remove( session );
		// update session in db
		//List<Session> check = m_database.query( Session.class );
		final List<Session> list = m_database.query( new Predicate<Session>() {
			@Override
			public boolean match( final Session match ) {
				if( match.toString().equals( session ) && match.isActive() )
					return true;
				return false;
			}
		});
		// should not happen but possible
		if( list.size() < 1 )
			throw new XSessionException( "Unmatched session logoff" );
		// update db
		list.get( 0 ).setDisposed();
		m_database.store( list.get( 0 ) );
		m_database.commit();
		List<Session> check = m_database.query( Session.class );
		// return username from voided session
		return remove.getPersona().getUsername();
	}

	@Override
	protected Token doObtainToken( final String session ) {
		return m_sessions.get( session ).getToken();
	}

	/// IDomainService

	@Override
	public String getName() {
		return p_name;
	}

	@Override
	public IDatabaseSession newDatabaseSession() {
		return new DatabaseSessionImplDb4o( m_database.ext().openSession() );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
