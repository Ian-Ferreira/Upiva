/**
 * Authored By: IanF on 11/06/13 06:48
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 11/06/13 06:48: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.dom;

import com.upiva.common.dbo.Token;
import com.upiva.common.utl.XmlDom;
import com.upiva.manna.server.exc.XAccessException;
import com.upiva.manna.server.exc.XCommandException;
import com.upiva.manna.server.exc.XInvocationException;
import com.upiva.manna.server.exc.XSessionException;
import com.upiva.manna.server.svr.cmd.CommandManager;
import com.upiva.manna.server.svr.cmd.ICommandManager;
import com.upiva.manna.server.svr.cmd.ICommandService;
import java.lang.reflect.Constructor;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import org.w3c.dom.Node;

public abstract class AbstractDomain implements IDomainManager, IDomainService {

	///////////////////////////////////////////////////////////////////////////
	// Factory

	public static class Factory {

		public static IDomainManager construct( final String clzz, final String name, final Node context, final int poolmin, final int poolmax, final String cmdpath, final int cmdpoolmax, final long cmdtimeout ) throws XInvocationException {
			try {
				// extract class
				final Class<?> clss = Class.forName( clzz );
				// extract constructor
				final Constructor<?> cons = clss.getConstructor( String.class, Node.class, int.class, int.class, String.class, int.class, long.class );
				// create manager
				final IDomainManager manager = ( IDomainManager )cons.newInstance( name, context, poolmin, poolmax, cmdpath, cmdpoolmax, cmdtimeout );
				// return it
				return manager;
			} catch( Exception e ) {
				throw new XInvocationException( "Construct domain failed - " + e.getLocalizedMessage() );
			}
		}
	}

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final ConcurrentHashMap<String,ICommandManager> m_commands;

	protected final String p_name;
	protected final Node p_context;

	private final String m_cmdpath;
	private final int m_cmdpoolmax;
	private final long m_cmdtimeout;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	protected AbstractDomain( final String name, final Node context, final int poolmin, final int poolmax, final String cmdpath, final int cmdpoolmax, final long cmdtimeout ) {
		// preserve args
		p_name = name;
		p_context = context;
		m_cmdpath = cmdpath;
		m_cmdpoolmax = cmdpoolmax;
		m_cmdtimeout = cmdtimeout;

		m_commands = new ConcurrentHashMap<>( poolmin );
	}

	///////////////////////////////////////////////////////////////////////////
	// Enforced

	protected abstract String doMatchSession( final String session );

	protected abstract String doObtainSession( final String username );

	protected abstract String doAppendSession( final String username, final String password, final String ipaddress ) throws XAccessException;

	protected abstract String doDeleteSession( final String session ) throws XSessionException;

	protected abstract Token doObtainToken( final String session );

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// IDomainManager

	@Override
	public ICommandService accesorCommand( final String command, final String session ) throws XAccessException, XCommandException {
		// check valid ip-address
		try {
			Inet4Address.getByName( session );
		} catch( UnknownHostException e ) {
			throw new XAccessException( String.format( "Invalid domain session - %s", session ) );
		}
		// inner acquire command
		return _acquireCommand( command, session );
	}

	@Override
	public ICommandService acquireCommand( final String command, final String session ) throws XAccessException, XCommandException {
		// verify session
		final String matcher = this.doMatchSession( session );
		if( matcher == null )
			throw new XAccessException( String.format( "Invalid domain session -%s", session ) );
		// inner acquire command
		return _acquireCommand( command, matcher );
	}

	@Override
	public void disposeCommand( final ICommandService command ) {
		m_commands.get( command.getCommand() ).disposeCommand( command );
	}

	@Override
	public String obtainSession( final String username ) {
		return doObtainSession( username );
	}

	@Override
	public String appendSession( final String username, final String password, final String ipaddress ) throws XAccessException {
		return doAppendSession( username, password, ipaddress );
	}

	@Override
	public String deleteSession( final String session ) throws XSessionException {
		return this.doDeleteSession( session );
	}

	@Override
	public Token obtainToken( final String session ) {
		return doObtainToken( session );
	}

	/// IDomainService

	@Override
	public String getConfigString( final String key ) {
		return XmlDom.getValue( p_context, key );
	}

	@Override
	public boolean getConfigBoolean( final String key ) {
		return Boolean.parseBoolean( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public char getConfigChar( final String key ) {
		return (char)Byte.parseByte( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public byte getConfigByte( final String key ) {
		return Byte.parseByte( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public short getConfigShort( final String key ) {
		return Short.parseShort( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public int getConfigInt( final String key ) {
		return Integer.parseInt( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public long getConfigLong( final String key ) {
		return Long.parseLong( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public float getConfigFloat( final String key ) {
		return Float.parseFloat( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public double getConfigDouble( final String key ) {
		return Double.parseDouble( XmlDom.getValue( p_context, key ) );
	}

	@Override
	public Node getConfigNested( final String key ) {
		return XmlDom.getNode( p_context, key );
	}


	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	private ICommandService _acquireCommand( final String command, final String session ) throws XCommandException {
		// check manager
		ICommandManager manager = m_commands.get( command );
		if( manager == null ) {
			// create new manager
			manager = new CommandManager( this, command, m_cmdpath, m_cmdpoolmax, m_cmdtimeout );
			// add to map
			m_commands.put( command, manager );
		}
		return manager.acquireCommand( command, session );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
