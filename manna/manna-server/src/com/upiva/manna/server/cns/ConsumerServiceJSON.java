/**
 * Authored By: IanF on 02/06/13 13:42
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 13:42: Created, IanF, ...
 *
 */

package com.upiva.manna.server.cns;

import com.upiva.manna.server.svr.cns.AbstractConsumerService;
import com.upiva.manna.server.svr.cmd.ClientRequest;
import com.upiva.manna.server.svr.cmd.ClientResponse;
import com.upiva.manna.server.exc.*;

public final class ConsumerServiceJSON extends AbstractConsumerService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ConsumerServiceJSON( final long sequence ) {
		super( sequence );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	@Override
	protected ClientRequest doParseRequest( final String request ) throws XParsingException {
		return new ClientRequest( request );
	}

	@Override
	protected String doPrintResponse( final ClientResponse response ) {
		if( response == null )
			return null;
		return response.toStringJSON();
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	@Override
	public final String process( final String request ) throws XParsingException, XDomainException, XAccessException, XCommandException, XProcessException, XTimeoutException, XThrowableException, XInterruptException {
		// todo: base64 decode

		// call super to process request
		String response = super.process( request );

		// ? valid response
		if( ( response == null ) || response.isEmpty() )
			throw new XThrowableException( "Unexpected null response" );

		if( !response.startsWith( "{" ) )
			response = String.format( "{%s}", response );

 		// todo: return base64 encoded
		return response;
	}

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
