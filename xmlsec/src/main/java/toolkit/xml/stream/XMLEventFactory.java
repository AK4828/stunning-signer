/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// $Id: XMLEventFactory.java 730320 2008-12-31 06:20:06Z mrglavas $

package toolkit.xml.stream;

import java.util.Iterator;

import toolkit.xml.namespace.NamespaceContext;
import toolkit.xml.stream.events.ProcessingInstruction;
import toolkit.xml.namespace.QName;
import toolkit.xml.stream.events.Characters;
import toolkit.xml.stream.events.Comment;
import toolkit.xml.stream.events.DTD;
import toolkit.xml.stream.events.EndElement;
import toolkit.xml.stream.events.EntityDeclaration;
import toolkit.xml.stream.events.Namespace;
import toolkit.xml.stream.events.Attribute;
import toolkit.xml.stream.events.EndDocument;
import toolkit.xml.stream.events.EntityReference;
import toolkit.xml.stream.events.StartDocument;
import toolkit.xml.stream.events.StartElement;

public abstract class XMLEventFactory {

    private static final String PROPERTY_NAME = "XMLEventFactory";
    private static final String DEFAULT_FACTORY = "org.apache.xerces.stax.XMLEventFactoryImpl";

    protected XMLEventFactory() {}

    public static XMLEventFactory newInstance()
        throws FactoryConfigurationError {
        try {
            return (XMLEventFactory) FactoryFinder.find(PROPERTY_NAME, DEFAULT_FACTORY);
        }
        catch (FactoryFinder.ConfigurationError e) {
            throw new FactoryConfigurationError(e.getException(), e.getMessage());
        }
    }

    public static XMLEventFactory newInstance(String factoryId,
            ClassLoader classLoader) throws FactoryConfigurationError {
        if (classLoader == null) {
            classLoader = SecuritySupport.getContextClassLoader();
        }
        try {
            return (XMLEventFactory) FactoryFinder.find(factoryId, classLoader, DEFAULT_FACTORY);
        }
        catch (FactoryFinder.ConfigurationError e) {
            throw new FactoryConfigurationError(e.getException(), e.getMessage());
        }
    }

    public abstract void setLocation(Location location);

    public abstract Attribute createAttribute(QName name, String value);

    public abstract Attribute createAttribute(String localName, String value);

    public abstract Attribute createAttribute(String prefix,
            String namespaceURI, String localName, String value);

    public abstract Namespace createNamespace(String namespaceUri);

    public abstract Namespace createNamespace(String prefix, String namespaceUri);

    public abstract StartElement createStartElement(QName name,
            Iterator attributes, Iterator namespaces);

    public abstract StartElement createStartElement(String prefix,
            String namespaceUri, String localName);

    public abstract StartElement createStartElement(String prefix,
            String namespaceUri, String localName, Iterator attributes,
            Iterator namespaces);

    public abstract StartElement createStartElement(String prefix,
            String namespaceUri, String localName, Iterator attributes,
            Iterator namespaces, NamespaceContext context);

    public abstract EndElement createEndElement(QName name, Iterator namespaces);

    public abstract EndElement createEndElement(String prefix,
            String namespaceUri, String localName);

    public abstract EndElement createEndElement(String prefix,
            String namespaceUri, String localName, Iterator namespaces);

    public abstract Characters createCharacters(String content);

    public abstract Characters createCData(String content);

    public abstract Characters createSpace(String content);

    public abstract Characters createIgnorableSpace(String content);

    public abstract StartDocument createStartDocument();

    public abstract StartDocument createStartDocument(String encoding);

    public abstract StartDocument createStartDocument(String encoding,
            String version);

    public abstract StartDocument createStartDocument(String encoding,
            String version, boolean standalone);

    public abstract EndDocument createEndDocument();

    public abstract EntityReference createEntityReference(String name,
            EntityDeclaration declaration);

    public abstract Comment createComment(String text);

    public abstract ProcessingInstruction createProcessingInstruction(
            String target, String data);

    public abstract DTD createDTD(String dtd);
}
