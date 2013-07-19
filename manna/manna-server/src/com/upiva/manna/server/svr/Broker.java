/**
 * Authored By: IanF on 04/06/13 09:31
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 09:31: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr;

import com.upiva.common.utl.XmlDom;
import com.upiva.manna.server.IMannaServer;
import com.upiva.manna.server.exc.*;
import com.upiva.manna.server.svr.drv.DriversManager;
import com.upiva.manna.server.svr.cns.ConsumerManager;
import com.upiva.manna.server.svr.cns.IConsumerManager;
import com.upiva.manna.server.svr.cns.IConsumerService;
import com.upiva.manna.server.svr.dom.AbstractDomain;
import com.upiva.manna.server.svr.dom.IDomainManager;
import org.w3c.dom.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Broker {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	final Map<String, IConsumerManager> m_consumers = Collections.synchronizedMap( new HashMap<String, IConsumerManager>() );
	final Map<String, DriversManager> m_drivers = Collections.synchronizedMap( new HashMap<String, DriversManager>() );
	final Map<String, IDomainManager> m_domains = Collections.synchronizedMap( new HashMap<String, IDomainManager>() );

	private final IMannaServer m_server;

	///////////////////////////////////////////////////////////////////////////
	// Singleton factory

	private static Broker s_instance = null;

	private Broker( final IMannaServer server ) {
		m_server = server;
	}

	public static Broker getInstance() {
		if( s_instance == null )
			throw new IllegalStateException( "Broker not initialized" );
		return s_instance;
	}

	public static Broker newInstance( final IMannaServer server ) {
		if( s_instance != null )
			throw new IllegalStateException( "Broker already initialized" );
		return s_instance = new Broker( server );
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static void installConsumer( final Node context ) throws XConsumerException {
		// extract params
		final String clzz = XmlDom.getAttr( context, "class" );
		final int freeMax = Integer.parseInt( XmlDom.getAttr( context, "freeMax" ) );
		final int workMax = Integer.parseInt( XmlDom.getAttr( context, "workMax" ) );
		final int timeout = Integer.parseInt( XmlDom.getAttr( context, "timeout" ) );
		// create manager
		final IConsumerManager manager = new ConsumerManager( clzz, freeMax, workMax, timeout );
		// install to map
		s_instance.m_consumers.put( clzz, manager );
	}

	public static void installDriver( final Node context ) {
		// extract params
		final String clzz = XmlDom.getAttr( context, "class" );
		final String name = XmlDom.getAttr( context, "name" );
		final String clzzQualifier = XmlDom.getAttr( context, "classQualifier" );
		final String clzzConsumer = XmlDom.getAttr( context, "classConsumer" );
		final Node innerContext;
		innerContext = XmlDom.getNode( context, "./context" );
		// create manager
		final DriversManager manager = new DriversManager( clzz, name, clzzQualifier, clzzConsumer, innerContext );
		// install to map
		s_instance.m_drivers.put( name, manager );
	}

	public static void installDomain( final Node context ) throws XInvocationException {
		// extract params
		final String clzz = XmlDom.getAttr( context, "class" );
		final String name = XmlDom.getAttr( context, "name" );
		final int poolmin = Integer.parseInt( XmlDom.getAttr( context, "poolmin" ) );
		final int poolmax = Integer.parseInt( XmlDom.getAttr( context, "poolmax" ) );
		final String cmdpath = XmlDom.getAttr( context, "cmdclspath" );
		final int cmdpoolmax = Integer.parseInt( XmlDom.getAttr( context, "cmdpoolmax" ) );
		final long cmdtimeout = Long.parseLong( XmlDom.getAttr( context, "cmdtimeout" ) );
		final Node innerContext = XmlDom.getNode( context, "./context" );
		// create manager
		final IDomainManager manager = AbstractDomain.Factory.construct( clzz, name, innerContext, poolmin, poolmax, cmdpath, cmdpoolmax, cmdtimeout );
		// install to map
		s_instance.m_domains.put( name, manager );
	}

	public static IConsumerService acquireConsumer( final String clazz ) throws XConsumerException, XMaxpoolException, XInterruptException {
		// extract consumer manager
		final IConsumerManager manager = s_instance.m_consumers.get( clazz );
		if( manager == null )
			throw new XConsumerException( String.format( "Unknown consumer - %s", clazz ) );
		// acquire consumer from manager
		final IConsumerService service = manager.acquireConsumer();
		// return it
		return service;
	}

	public static void disposeConsumer( final IConsumerService consumer ) {
		// extract consumer manager
		final IConsumerManager manager = s_instance.m_consumers.get( consumer.getClass().getName() );
		// manager to dispose consumer
		manager.disposeConsumer( consumer );
	}

	public static IDomainManager obtainDomain( final String domain ) throws XDomainException {
		// extract domain manager
		final IDomainManager manager = s_instance.m_domains.get( domain );
		if( manager == null )
			throw new XDomainException( String.format( "Unknown domain - %s", domain ) );
		return manager;
	}

	public static String getHomePath() {
		return s_instance.m_server.getHomePath();
	}

	public static ServerConfig getConfig() {
		return s_instance.m_server.getConfig();
	}

	public static void postMessage( final String driver, final String message ) {
		System.out.println( "todo! Broker.postMessage: " + driver + "; " + message );
	}

	public static void shutdown() {
		System.out.println( "todo! Broker.shutdown " );
	}
}
