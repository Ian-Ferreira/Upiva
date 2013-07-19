/**
 * Authored By: IanF on 02/06/13 17:20
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 17:20: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr.cns;

import com.upiva.manna.server.exc.*;
import com.upiva.manna.server.svr.Broker;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.svr.cmd.ICommandService;
import com.upiva.manna.server.svr.dom.IDomainManager;

public abstract class AbstractConsumerService implements IConsumerService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final long m_sequence;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	protected AbstractConsumerService( final long sequence ) {
		// preserve args
		m_sequence = sequence;
	}

	///////////////////////////////////////////////////////////////////////////
	// Enforced

	protected abstract ClientRequest doParseRequest( final String request ) throws XParsingException;

	protected abstract String doPrintResponse( final ClientResponse response );

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// IConsumerService

	@Override
	public final long getSequence() {
		return m_sequence;
	}

	@Override
	public String process( final String request ) throws XParsingException, XDomainException, XAccessException, XCommandException, XProcessException, XTimeoutException, XThrowableException, XInterruptException {
		// new client request - throws XParsingException
		final ClientRequest parse = this.doParseRequest( request );

		// obtain domain from broker
		final IDomainManager domain = Broker.obtainDomain( parse.getDomain() );

		// NOTE: we need a quick trigger here for logon and enrolls
		// therefore all real sessions are prefixed with an # where logons and enrolls cary the users ip address
		ICommandService command = null;
		if( parse.getSession().charAt( 0 ) == '#' ) {
			// trim hash from request
			parse.fixSession();
			// acquire process from controller
			command = domain.acquireCommand( parse.getCommand(), parse.getSession() ); // from here onwards sessions are not prefixed anymore
		} else {
			// call special logon and enroll filter
			command = domain.accesorCommand( parse.getCommand(), parse.getSession() ); // session is user raw ip address
		}

		// command to process request
		ClientResponse response = new ClientResponse( command.getCommand() );
		try {
			command.process( parse, response );
		} finally {
			domain.disposeCommand( command );
		}

		// return response
		return this.doPrintResponse( response );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
