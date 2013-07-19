/**
 * Authored By: IanF on 04/06/13 16:40
 *
 * Copyright (c) 2013, Ian Ferreira; IanFerreira@yahoo.co.uk
 *
 * This software and codebase is protected by South African and international copyright legislation.
 * The intellectual ownership of this source, artifacts and/or any products there-off remain 
 * the property of the author. All rights reserved globally.
 *
 * Revisions:-
 * 04/06/13 16:40: Created, IanF, ...
 *
 */

package com.upiva.common.utl;

import com.sun.org.apache.xpath.internal.XPathAPI;
import com.sun.xml.internal.ws.util.xml.NodeListIterator;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

public class XmlDom {

	///////////////////////////////////////////////////////////////////////////
	// Constants

	/**
	 * XML file processing instruction
	 */
	public static final String XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>";

	/**
	 * SCHEMA_PROPERTY_LANGUAGE - XmlDom Factory attribute/property to set the schema _common.
	 * Typicaly this will be set with the SCHEMA_REFERENCE_LANGUAGE constant reference.
	 */
	public static final String SCHEMA_PROPERTY_LANGUAGE = "http://java.sun.com/xml/properties/jaxp/schemaLanguage";

	/**
	 * SCHEMA_PROPERTY_SOURCE - XmlDom Factory attribute/property to set the schema source document.
	 * Typical a url/path to a '***.xsd' file
	 */
	public static final String SCHEMA_PROPERTY_SOURCE = "http://java.sun.com/xml/properties/jaxp/schemaSource";

	/** SCHEMA_REFERENCE - Currently (JAXP 1.3) W3C Reference schema for XSD's */
	public static final String SCHEMA_REFERENCE_LANGUAGE = "http://www.w3.org/2001/XMLSchema";

	/** SCHEMA_HEAD - No-namespace generalized XmlDom XSD schema document initializing tag. */
	public static final String SCHEMA_HEAD = ""
			+ XML_HEADER
			+ "<xsd:schema xmlns:xsd='" + SCHEMA_REFERENCE_LANGUAGE + "' elementFormDefault='qualified' version='1.0'>";

	/** SCHEMA_TAIL - End tag for SCHEMA_HEAD. */
	public static final String SCHEMA_TAIL = "</xsd:schema>";

	/**
	 *
	 */
	public static final String SCHEMA_CONTEXT_HEAD = ""
			+ SCHEMA_HEAD
			+ "	<xsd:element name='context'>"
			+ "		<xsd:complexType>"
			+ "			<xsd:sequence>"
			+ "<!-- Userdata insertion point ! -->";

	/**
	 *
	 */
	public static final String SCHEMA_ELEMENT_ANY = ""
			+ "				<xsd:any minOccurs='0' maxOccurs='unbounded' processContents='lax'/>";


	/**
	 *
	 */
	public static final String SCHEMA_CONTEXT_TAIL = ""
			+ "<!-- End userdata inserts ! -->"
			+ "			</xsd:sequence>"
			+ "		</xsd:complexType>"
			+ "	</xsd:element>"
			+ SCHEMA_TAIL;

	/**
	 * SCHEMA_CONTEXT_BASE - Convenience XSD schema document for verifying DOM's confirming to
	 * a 'context' root-node definition.
	 */
	public static final String SCHEMA_CONTEXT_BASE = ""
			+ SCHEMA_CONTEXT_HEAD
			+ SCHEMA_ELEMENT_ANY
			+ SCHEMA_CONTEXT_TAIL;

	/**
	 * XML_CONTEXT - Convenience XmlDom source template to create new empty source documents.
	 */
	public static final String XML_CONTEXT = ""
			+ "<?xml version='1.0' encoding='UTF-8'?>"
			+ "<context>"
			//+ " <!-- Userdata insertion point ! -->"
			+ "</context>";

	///////////////////////////////////////////////////////////////////////////
	// Data members

	protected final Node p_root;

	///////////////////////////////////////////////////////////////////////////
	// Construction

	public XmlDom( final String xmlFile, final String xsdData ) throws ParserConfigurationException, SAXException, IOException {
		// create new builder
		final DocumentBuilder builder;
		{
			// factory
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating( false );
			factory.setCoalescing( true );
			factory.setNamespaceAware( true );
			factory.setIgnoringComments( false );
			factory.setIgnoringElementContentWhitespace( true );
			factory.setExpandEntityReferences( true );
			// builder
			builder = factory.newDocumentBuilder();
			builder.setErrorHandler( new ErrorHandler() {
				public void warning( SAXParseException e ) throws SAXException {
					System.err.println( "XmlDom parser warning: " + e.getLocalizedMessage() );
				}
				public void error( SAXParseException e ) throws SAXException {
					throw e;
				}
				public void fatalError( SAXParseException e ) throws SAXException {
					throw e;
				}
			} );
		}
		// create new validator
		final Validator validator;
		{
			// factory
			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			factory.setErrorHandler( new ErrorHandler() {
				public void warning( SAXParseException e ) throws SAXException {
					throw e;
				}
				public void error( SAXParseException e ) throws SAXException {
					throw e;
				}
				public void fatalError( SAXParseException e ) throws SAXException {
					throw e;
				}
			} );
			factory.setResourceResolver( new LSResourceResolver() {
				public LSInput resolveResource( String type, String namespaceURI, String publicId, String systemId, String baseURI ) {
					return null;
				}
			} );
			// load schema
			final StreamSource stream = new StreamSource( new StringReader( xsdData ) );
			final Schema schema = factory.newSchema( stream );
			validator = schema.newValidator();
		}
		// parse xml
		final Node rootNode = builder.parse( new File( xmlFile ) );
		// verify xml
		// todo: need schema
		//validator.validate( new DOMSource( rootNode ) );
		// preserve root
		p_root = rootNode;
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements

	///////////////////////////////////////////////////////////////////////////
	// Overrides

	///////////////////////////////////////////////////////////////////////////
	// Public methods

	///////////////////////////////////////////////////////////////////////////
	// Private helpers

	///////////////////////////////////////////////////////////////////////////
	// Static utilities

	public static Node getNode( final Node source, final String id ) {
		try {
			return XPathAPI.selectSingleNode( source, id );
		} catch( TransformerException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static NodeListIterator getNodes( final Node source, final String id ) {
		try {
			return new NodeListIterator( XPathAPI.selectNodeList( source, id ) );
		} catch( TransformerException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getText( final Node source, final String id ) {
		try {
			return XPathAPI.selectSingleNode( source, id + "/text()" ).getNodeValue();
		} catch( TransformerException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getAttr( final Node source, final String id ) {
		Node temp = source.getAttributes().getNamedItem( id );
		return temp.getNodeValue();
	}

	public static String getAttr( final Node source, final String id, final String devault ) {
		String temp = XmlDom.getAttr( source, id );
		return ( temp != null ) ? temp : devault;
	}

	public static NamedNodeMap getAttrs( final Node source ) {
		return source.getAttributes();
	}

	public static String getValue( final Node source, final String id ) {
		return XmlDom.getNode( source, id ).getFirstChild().getNodeValue();
	}

	public static boolean getBoolean( final Node context, final String id )  {
		return Boolean.parseBoolean( XmlDom.getValue( context, id ) );
	}

	public static char getChar( final Node context, final String id )  {
		return ( char )XmlDom.getByte( context, id );
	}

	public static byte getByte( final Node context, final String id )  {
		return Byte.parseByte( XmlDom.getValue( context, id ) );
	}

	public static int getInt( final Node context, final String id )  {
		return Integer.parseInt( XmlDom.getValue( context, id ) );
	}

	public static long getLong( final Node context, final String id )  {
		return Long.parseLong( XmlDom.getValue( context, id ) );
	}

	public static float getFloat( final Node context, final String id )  {
		return Float.parseFloat( XmlDom.getValue( context, id ) );
	}

	public static double getDouble( final Node context, final String id )  {
		return Double.parseDouble( XmlDom.getValue( context, id ) );
	}

}
