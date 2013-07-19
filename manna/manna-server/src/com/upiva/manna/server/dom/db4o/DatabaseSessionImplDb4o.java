/**
 * Authored By: IanF on 08/06/13 07:06
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 08/06/13 07:06: Created, IanF, ...
 *
 */

package com.upiva.manna.server.dom.db4o;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.ExtObjectContainer;
import com.db4o.query.Predicate;
import com.db4o.query.Query;
import com.db4o.query.QueryComparator;
import com.upiva.manna.server.svr.dom.IDatabaseSession;

import java.util.Comparator;

public class DatabaseSessionImplDb4o implements IDatabaseSession, ObjectContainer {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	private final ObjectContainer m_container;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public DatabaseSessionImplDb4o( final ObjectContainer container ) {
		// preserve args
		m_container = container;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	/// deligates

	@Override
	public void activate( final Object o, final int i ) throws Db4oIOException, DatabaseClosedException {
		m_container.activate( o, i );
	}

	@Override
	public boolean close() throws Db4oIOException {
		return m_container.close();
	}

	@Override
	public void commit() throws Db4oIOException, DatabaseClosedException, DatabaseReadOnlyException {
		m_container.commit();
	}

	@Override
	public void deactivate( final Object o, final int i ) throws DatabaseClosedException {
		m_container.deactivate( o, i );
	}

	@Override
	public void delete( final Object o ) throws Db4oIOException, DatabaseClosedException, DatabaseReadOnlyException {
		m_container.delete( o );
	}

	@Override
	public ExtObjectContainer ext() {
		return m_container.ext();
	}

	@Override
	public <T> ObjectSet<T> queryByExample( final Object o ) throws Db4oIOException, DatabaseClosedException {
		return m_container.queryByExample( o );
	}

	@Override
	public Query query() throws DatabaseClosedException {
		return m_container.query();
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query( final Class<TargetType> targetTypeClass ) throws Db4oIOException, DatabaseClosedException {
		return m_container.query( targetTypeClass );
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query( final Predicate<TargetType> targetTypePredicate ) throws Db4oIOException, DatabaseClosedException {
		return m_container.query( targetTypePredicate );
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query( final Predicate<TargetType> targetTypePredicate, final QueryComparator<TargetType> targetTypeQueryComparator ) throws Db4oIOException, DatabaseClosedException {
		return m_container.query( targetTypePredicate, targetTypeQueryComparator );
	}

	@Override
	public <TargetType> ObjectSet<TargetType> query( final Predicate<TargetType> targetTypePredicate, final Comparator<TargetType> targetTypeComparator ) throws Db4oIOException, DatabaseClosedException {
		return m_container.query( targetTypePredicate, targetTypeComparator );
	}

	@Override
	public void rollback() throws Db4oIOException, DatabaseClosedException, DatabaseReadOnlyException {
		m_container.rollback();
	}

	@Override
	public void store( final Object o ) throws DatabaseClosedException, DatabaseReadOnlyException {
		m_container.store( o );
	}

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
