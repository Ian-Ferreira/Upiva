/**
 * Authored By: IanF on 02/06/13 08:56
 * <p/>
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 * <p/>
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain
 * the property of the author. All rights reserved globally.
 * <p/>
 * Revisions:-
 * 02/06/13 08:56: Created, IanF, ...
 */
package com.upiva.manna.websvr;

import com.upiva.common.utl.B64;
import com.upiva.manna.server.IMannaServer;
import com.upiva.manna.server.exc.*;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.IServerConnector;
import com.upiva.manna.server.svr.cns.IConsumerService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MannaServletJSON extends javax.servlet.http.HttpServlet {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private IMannaServer m_server = null;
	private String m_consumerClass = null;
	private boolean m_base64DecodeValues = false;

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public void init( final ServletConfig config ) throws ServletException {
		super.init( config );

		// report
		System.out.printf( config.getServletName() +  "; Servlet started\n" );

		// create only one server connection
		// - protection if web-server goes insane
		if( m_server == null ) {
			// extract our params fom web.xml
			final String serverConnectorContext = config.getInitParameter( "serverConnectorContext" );
			m_consumerClass = config.getInitParameter( "consumerServiceClass" );
			m_base64DecodeValues = Boolean.parseBoolean( config.getInitParameter( "base64DecodeValues" ) );

			// the only way to get our deployed war root - Tomcat6
			final String contextHomePath = config.getServletContext().getRealPath( "" );

			// create connector
			try {
				// connect server
				m_server = IServerConnector.Factory.connector().connect( serverConnectorContext, contextHomePath );
			} catch( Exception e ) {
				throw new ServletException( e.toString() );
			}
		}
		System.out.printf( config.getServletName() +  "; Servlet configured\n" );
	}

	protected void doGet( javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response ) throws javax.servlet.ServletException, IOException {
		// prep output
		response.setContentType( "text/html" ); // for exceptions

		// has command
		final String command = request.getParameter( "command" );
		if( ( command != null ) && !command.isEmpty() ) {
			String value = null;
			try {
				value = m_base64DecodeValues ? B64.decode( command ) : command;
			} catch( Throwable e ) {
				response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.toString() );
				return;
			}
			// call helper
			try {
				this._processCommand( value, response.getWriter() );
				response.setStatus( HttpServletResponse.SC_OK );
				return;
			} catch( Exception e ) {
				System.err.println( e.toString() );
				switch( e.getClass().getSimpleName() ) {
					case "XParsingException":
					case "XConsumerException":
					case "XDomainException":
					case "XCommandException":
						response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.getLocalizedMessage() );
						return;
					case "XAccessException":
						response.sendError( HttpServletResponse.SC_UNAUTHORIZED, e.getLocalizedMessage() );
						return;
					case "XTimeoutException":
					case "XInterruptException":
					case "XMaxpoolException":
						// todo: redirect
						response.sendError( HttpServletResponse.SC_TEMPORARY_REDIRECT, e.getLocalizedMessage() );
						return;
					case "XProcessException":
					case "XThrowableException":
						response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getCause().toString() );
						return;
					default:
						response.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage() );
						return;
				}
			}
		}

		// has resource
		final String resource = request.getParameter( "resource" );
		if( ( resource != null ) && !resource.isEmpty() ) {
			String value = null;
			try {
				value = m_base64DecodeValues ? B64.decode( resource ) : resource;
			} catch( Throwable e ) {
				response.sendError( HttpServletResponse.SC_BAD_REQUEST, e.toString() );
				return;
			}
			this._processResource( value, response.getOutputStream() );
			response.setStatus( HttpServletResponse.SC_OK );
			return;
		}

		// unknown request
		response.sendError( HttpServletResponse.SC_BAD_REQUEST, "Unknown request" ); // bad request
		return;

	}

	protected void doPost( javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response ) throws javax.servlet.ServletException, IOException {
		this.doGet( request, response );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	private void _processCommand( final String query, final PrintWriter response ) throws XConsumerException, XMaxpoolException, XCommandException, XProcessException, XInterruptException, XDomainException, XThrowableException, XParsingException, XAccessException, XTimeoutException {
		// no server
		if( m_server == null )
			throw new IllegalStateException( "Server not connected" );

		// server to provide new consumer
		IConsumerService consumer = null;
		try {
			// acquire consumer
			consumer = Broker.acquireConsumer( m_consumerClass );

			// consumer to process request
			final String output = consumer.process( query );

			// prep response
			response.println( output );

		} finally {
			// server to disposeConsumer consumer
			if( consumer != null )
				Broker.disposeConsumer( consumer );
		}
	}

	private void _processResource( final String resource, final ServletOutputStream response ) {

	}

}
