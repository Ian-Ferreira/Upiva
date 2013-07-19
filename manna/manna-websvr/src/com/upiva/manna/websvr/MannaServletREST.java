/**
 * Authored By: IanF on 04/06/13 08:26
 * <p/>
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 * <p/>
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain
 * the property of the author. All rights reserved globally.
 * <p/>
 * Revisions:-
 * 04/06/13 08:26: Created, IanF, ...
 */
package com.upiva.manna.websvr;

import com.upiva.manna.server.IMannaServer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet( name = "MannaServletREST" )
public class MannaServletREST extends HttpServlet {

	final IMannaServer m_server = null;

	@Override
	public void init( final ServletConfig config ) throws ServletException {
		super.init( config );
	}

	@Override
	protected void doHead( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException, IOException {
		super.doHead( request, response );
	}

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		super.doGet( request, response );
	}

	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
		super.doPost( request, response );
	}

	@Override
	protected void doPut( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException, IOException {
		super.doPut( request, response );
	}

	@Override
	protected void doOptions( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException, IOException {
		super.doOptions( request, response );
	}

	@Override
	protected void doTrace( final HttpServletRequest request, final HttpServletResponse response ) throws ServletException, IOException {
		super.doTrace( request, response );
	}

	@Override
	protected void doDelete( final HttpServletRequest req, final HttpServletResponse resp ) throws ServletException, IOException {
		super.doDelete( req, resp );
	}
}
