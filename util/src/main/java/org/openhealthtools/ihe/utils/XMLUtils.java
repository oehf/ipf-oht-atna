/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.openhealthtools.ihe.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLUtils {
	/**
	 * Simple method to create an empty well-formed DOM Document
	 * @return	Empty, well-formed DOM Document
	 * @throws Exception
	 */
	public static Document createDomDocument() throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    return builder.newDocument();
	}
	
	/**
	 * Simple method to serialize a DOM-bound XML object to a byte array (string)
	 * 
	 * @param inputNode	DOM Node to serialize
	 * @return Byte array of XML in ASCII form
	 * @throws Exception
	 */
	public static byte[] serialize(Node inputNode) throws Exception
	{
		// Initialize sources and targets
		ByteArrayOutputStream serializerOutput = new ByteArrayOutputStream();
		Source sourceObject = new DOMSource(inputNode); 
		Result targetObject = new StreamResult(serializerOutput);
		
		// Create XML Transformer
		TransformerFactory serializerFactory = TransformerFactory.newInstance();
		Transformer serializer = serializerFactory.newTransformer();

		// Set output properties
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		
		// Run serialization transformation
		serializer.transform(sourceObject, targetObject);
		
		// Return output as a byte array
		return serializerOutput.toByteArray();
	}
	
	public static Element deserialize(byte[] data) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(new ByteArrayInputStream(data));
	    return doc.getDocumentElement();

	}
}
