/**
 * Authored By: IanF on 08/06/13 21:47
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 08/06/13 21:47: Created, IanF, ...
 *
 */

package com.upiva.manna.server;

import com.upiva.common.dbo.Token;
import com.upiva.common.svr.ServerResponse;
import com.upiva.common.utl.SYS;
import com.upiva.common.utl.XOR;
import com.upiva.manna.server.cns.ConsumerServiceJSON;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.IServerConnector;
import com.upiva.manna.server.svr.cns.IConsumerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsumerJSONTest {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	private static IMannaServer s_server = null;
	private static Token s_guest = null;
	private static Token s_user1;

	private IConsumerService m_consumer = null;
	private String m_output = null;

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	@Before
	public void setUp() throws Exception {
		// permanent server
		if( s_server == null )
			s_server = IServerConnector.Factory.connector().connect( "embed:com.upiva.manna.server.MannaServer%./cfg/server.xml", SYS.getUserDir() );
		// consumer
		m_consumer = Broker.acquireConsumer( ConsumerServiceJSON.class.getName() );
	}

	@After
	public void tearDown() throws Exception {
		Broker.disposeConsumer( m_consumer );
		m_consumer = null;
	}

	@Test
	public void testMain() throws Exception {
		System.out.println( "\n=== LogonGuest-Good" );
		m_output = m_consumer.process( "{MannaGame-LogonUser-192.168.22.195-1.0.0.0:{username:\"guest\",password:\"guest\"}}" );
		System.out.println( "<<< " + m_output );

		// extract guest token from m_output
		ServerResponse response = new ServerResponse( m_output, "com.upiva.common.dbo" );
		s_guest = ( Token )response.get( 0 );

		// check consumer multi run
		System.out.println( "\n=== LogonGuest-Fail-Exist" );
		try {
			m_output = "";
			m_output = m_consumer.process( "{MannaGame-LogonUser-192.168.22.195-1.0.0.0:{username:\"guest\",password:\"guest\"}}" );
		} catch( Exception e ) {
			m_output = e.getLocalizedMessage();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== LogoffGuest-Fail-BadSession" );
		try {
			m_output = "";
			m_output = m_consumer.process( "{MannaGame-LogoffUser-#MTs4MzczNSk3MS8wPjU=:{}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== LogoffGuest-Good" );
		String input = String.format( "{MannaGame-LogoffUser-#%s-1.0.0.0:{}}", XOR.encrypt( "192.168.22.195", s_guest.getSession() ) );
		try {
			m_output = m_consumer.process( input );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== LogonGuest-Fail-BadPassword" );
		try {
			m_output = m_consumer.process( "{MannaGame-LogonUser-192.168.22.195-1.0.0.0:{username:\"guest\",password:\"12345\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollGuest-Fail-Exist" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"guest\",password:\"12345\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Fail-MinUsername" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"usr1\",password:\"12345\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Fail-MaxUsername" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"user1user1user1user1user1\",password:\"12345\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Fail-MinPassword" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"123\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Fail-MaxPassword" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345678901234567890\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Fail-NoEmail" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Good" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== EnrollUser1-Fail-Pending" );
		try {
			m_output = m_consumer.process( "{MannaGame-EnrollUser-192.168.22.195:{username:\"user1\",password:\"12345\",email:\"user1@upiva.com\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== LogonUser1-Fail-NotExist" );
		try {
			m_output = m_consumer.process( "{MannaGame-LogonUser-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== ConfirmUser1-Fail-NoMatch" );
		try {
			m_output = m_consumer.process( "{MannaGame-ConfirmEnroll-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345\",pincode:\"000000\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== ConfirmUser1-Good" );
		try {
			m_output = m_consumer.process( "{MannaGame-ConfirmEnroll-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345\",pincode:\"989796\"}}" );
		} catch( Exception e ) {
			m_output = e.toString();
		}
		System.out.println( "<<< " + m_output );

		System.out.println( "\n=== LogonUser1-Good" );
		m_output = m_consumer.process( "{MannaGame-LogonUser-192.168.22.195-1.0.0.0:{username:\"user1\",password:\"12345\"}}" );
		System.out.println( "<<< " + m_output );

		// extract guest token from m_output
		response = new ServerResponse( m_output, "com.upiva.common.dbo" );
		s_user1 = ( Token )response.get( 0 );



		System.out.println( "\n=== LogoffUser1-Good" );
		input = String.format( "{MannaGame-LogoffUser-#%s-1.0.0.0:{}}", XOR.encrypt( "192.168.22.195", s_user1.getSession() ) );
		m_output = m_consumer.process( input );
		System.out.println( "<<< " + m_output );

	}


}
