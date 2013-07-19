/**
 * Authored By: IanF on 04/06/13 15:55
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 15:55: Created, IanF, ...
 *
 */

package com.upiva.manna.server.dom.jdbc;

import com.upiva.common.dbo.Token;
import com.upiva.manna.server.svr.dom.AbstractDomain;
import com.upiva.manna.server.svr.dom.IDatabaseSession;
import com.upiva.manna.server.svr.dom.IDomainService;
import com.upiva.manna.server.exc.XAccessException;
import org.w3c.dom.Node;

public class DomainServiceImplJdbc extends AbstractDomain implements IDomainService {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DomainServiceImplJdbc( final String name, final Node context, final int poolmin, final int poolmax, final String cmdpath, final int cmdpoolmax, final long cmdtimeout ) {
		super( name, context, poolmin, poolmax, cmdpath, cmdpoolmax, cmdtimeout );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// AbstractDomain

	@Override
	protected String doMatchSession( final String session ) {
		return null;
	}

	/// IDomainService

	@Override
	public String getName() {
		return p_name;
	}

	@Override
	public IDatabaseSession newDatabaseSession() {
		return null;
	}

	@Override
	protected String doObtainSession( final String username ) {
		return "<todo>";
	}

	@Override
	protected String doAppendSession( final String username, final String password, final String ipaddress ) throws XAccessException {
		return "<todo>";
	}

	@Override
	protected String doDeleteSession( final String session ) {
		return "<todo>";
	}

	@Override
	protected Token doObtainToken( final String session ) {
		return null;
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
