/**
 * Authored By: IanF on 08/06/13 20:32
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 08/06/13 20:32: Created, IanF, ...
 *
 */

package com.upiva.manna.websvr;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import static org.mockito.Mockito.when;

public class MannaServletJSONTest {

	private final MannaServletJSON m_servlet;

	@Mock
	private ServletContext m_mContext;

	@Mock
	private HttpServletRequest m_mRequest;

	@Mock
	private HttpServletResponse m_mResponse;

	public MannaServletJSONTest() throws IOException {
		// servlet to test
		m_servlet = new MannaServletJSON();
	}

	@org.junit.Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks( this );
		when( m_mContext.getRealPath( "" ) ).thenReturn( "C:/dev/and/upiva/manna/manna-server/" );
		when( m_mRequest.getParameter( "json" ) ).thenReturn( "{MannaGame-LogonUser-192.168.22.195:{username:\"guest\",password:\"guest\"}}" );
		when( m_mResponse.getWriter() ).thenReturn( new PrintWriter( System.out ) );
	}

	@org.junit.Test
	public void testInit() throws Exception {
		final ServletConfig config = new ServletConfig() {
			@Override
			public String getServletName() {
				return "MannaServletJSON-Dummy";
			}
			@Override
			public ServletContext getServletContext() {
				return m_mContext;
			}
			@Override
			public String getInitParameter( final String s ) {
				switch( s ) {
					case "serverConnectorContext":
						return "embed:com.upiva.manna.server.MannaServer%./cfg/server.xml";
					case "consumerServiceClass":
						return "com.upiva.manna.server.cns.ConsumerServiceJSON";
				}
				return null;
			}
			@Override
			public Enumeration<String> getInitParameterNames() {
				return null;
			}
		};
		m_servlet.init( config );
	}

	@org.junit.Test
	public void testDoGet() throws Exception {
		m_servlet.doGet( m_mRequest, m_mResponse );
		System.out.printf( ">>>" + m_mResponse.toString() );
	}

}
