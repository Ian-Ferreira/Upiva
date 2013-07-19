/**
 * Authored By: IanF on 02/06/13 13:35
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 02/06/13 13:35: Created, IanF, ...
 *
 */

package com.upiva.manna.server.svr;

import com.sun.xml.internal.ws.util.xml.NodeListIterator;
import com.upiva.common.utl.XmlDom;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class ServerConfig extends XmlDom {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	///////////////////////////////////////////////////////////////////////////
	// Data members

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public ServerConfig( final String source, final String schema ) throws ParserConfigurationException, SAXException, IOException {
		super( source, schema );
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	public String getContextTitle() throws TransformerException {
		return XmlDom.getAttr( XmlDom.getNode( p_root, "context" ), "title" );
	}

	public String getContextVersion() throws TransformerException {
		return XmlDom.getAttr( XmlDom.getNode( p_root, "context" ), "version" );
	}

	public String getContextCopyright() throws TransformerException {
		return XmlDom.getAttr( XmlDom.getNode( p_root, "context" ), "copyright" );
	}

	public String getContextAuthor() throws TransformerException {
		return XmlDom.getAttr( XmlDom.getNode( p_root, "context" ), "author" );
	}

	public boolean isDebugMode() {
		final Node node = XmlDom.getNode( p_root, "/context/properties/property[@name='debug.mode']" );
		return Boolean.parseBoolean( node.getFirstChild().getNodeValue() );
	}

	public boolean isDebugDeleteDatabase() {
		final Node node = XmlDom.getNode( p_root, "/context/properties/property[@name='debug.delete.database']" );
		return Boolean.parseBoolean( node.getFirstChild().getNodeValue() );
	}

	public NodeListIterator getConsumers() throws TransformerException {
		return XmlDom.getNodes( XmlDom.getNode( p_root, "context" ), "consumers/consumer" );
	}

	public NodeListIterator getDrivers() throws TransformerException {
		return XmlDom.getNodes( XmlDom.getNode( p_root, "context" ), "drivers/driver" );
	}

	public NodeListIterator getDomains() throws TransformerException {
		return XmlDom.getNodes( XmlDom.getNode( p_root, "context" ), "domains/domain" );
	}

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

}
