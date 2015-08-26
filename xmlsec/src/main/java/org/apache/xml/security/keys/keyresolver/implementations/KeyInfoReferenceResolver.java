/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.xml.security.keys.keyresolver.implementations;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.crypto.SecretKey;
import toolkit.xml.namespace.QName;
import toolkit.xml.parsers.ParserConfigurationException;

import org.apache.xml.security.c14n.CanonicalizationException;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.KeyInfoReference;
import org.apache.xml.security.keys.keyresolver.KeyResolverException;
import org.apache.xml.security.keys.keyresolver.KeyResolverSpi;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.XMLUtils;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * KeyResolverSpi implementation which resolves public keys, private keys, secret keys, and X.509 certificates from a 
 * <code>dsig11:KeyInfoReference</code> element.
 * 
 * @author Brent Putman (putmanb@georgetown.edu)
 */
public class KeyInfoReferenceResolver extends KeyResolverSpi {

    private static org.slf4j.Logger log =
        org.slf4j.LoggerFactory.getLogger(KeyInfoReferenceResolver.class);

    /** {@inheritDoc}. */
    public boolean engineCanResolve(Element element, String baseURI, StorageResolver storage) {
        return XMLUtils.elementIsInSignature11Space(element, Constants._TAG_KEYINFOREFERENCE);
    }

    /** {@inheritDoc}. */
    public PublicKey engineLookupAndResolvePublicKey(Element element, String baseURI, StorageResolver storage)
        throws KeyResolverException {

        if (log.isDebugEnabled()) {
            log.debug("Can I resolve " + element.getTagName());
        }

        if (!engineCanResolve(element, baseURI, storage)) {
            return null;
        }

        try {
            KeyInfo referent = resolveReferentKeyInfo(element, baseURI, storage);
            if (referent != null) {
                return referent.getPublicKey();
            }
        } catch (XMLSecurityException e) {
            if (log.isDebugEnabled()) {
                log.debug("XMLSecurityException", e);
            }
        }

        return null;
    }

    /** {@inheritDoc}. */
    public X509Certificate engineLookupResolveX509Certificate(Element element, String baseURI, StorageResolver storage)
        throws KeyResolverException {

        if (log.isDebugEnabled()) {
            log.debug("Can I resolve " + element.getTagName());
        }

        if (!engineCanResolve(element, baseURI, storage)) {
            return null;
        }

        try {
            KeyInfo referent = resolveReferentKeyInfo(element, baseURI, storage);
            if (referent != null) {
                return referent.getX509Certificate();
            }
        } catch (XMLSecurityException e) {
            if (log.isDebugEnabled()) {
                log.debug("XMLSecurityException", e);
            }
        }

        return null;
    }

    /** {@inheritDoc}. */
    public SecretKey engineLookupAndResolveSecretKey(Element element, String baseURI, StorageResolver storage)
        throws KeyResolverException {

        if (log.isDebugEnabled()) {
            log.debug("Can I resolve " + element.getTagName());
        }

        if (!engineCanResolve(element, baseURI, storage)) {
            return null;
        }

        try {
            KeyInfo referent = resolveReferentKeyInfo(element, baseURI, storage);
            if (referent != null) {
                return referent.getSecretKey();
            }
        } catch (XMLSecurityException e) {
            if (log.isDebugEnabled()) {
                log.debug("XMLSecurityException", e);
            }
        }

        return null;
    }

    /** {@inheritDoc}. */
    public PrivateKey engineLookupAndResolvePrivateKey(Element element, String baseURI, StorageResolver storage)
        throws KeyResolverException {

        if (log.isDebugEnabled()) {
            log.debug("Can I resolve " + element.getTagName());
        }

        if (!engineCanResolve(element, baseURI, storage)) {
            return null;
        }

        try {
            KeyInfo referent = resolveReferentKeyInfo(element, baseURI, storage);
            if (referent != null) {
                return referent.getPrivateKey();
            }
        } catch (XMLSecurityException e) {
            if (log.isDebugEnabled()) {
                log.debug("XMLSecurityException", e);
            }
        }

        return null;
    }

    /**
     * Resolve the KeyInfoReference Element's URI attribute into a KeyInfo instance.
     * 
     * @param element
     * @param baseURI
     * @param storage
     * @return the KeyInfo which is referred to by this KeyInfoReference, or null if can not be resolved
     * @throws XMLSecurityException
     */
    private KeyInfo resolveReferentKeyInfo(Element element, String baseURI, StorageResolver storage) throws XMLSecurityException {
        KeyInfoReference reference = new KeyInfoReference(element, baseURI);
        Attr uriAttr = reference.getURIAttr();

        XMLSignatureInput resource = resolveInput(uriAttr, baseURI, secureValidation);

        Element referentElement = null;
        try {
            referentElement = obtainReferenceElement(resource);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("XMLSecurityException", e);
            }
            return null;
        }

        if (referentElement == null) {
            log.debug("De-reference of KeyInfoReference URI returned null: " + uriAttr.getValue());
            return null;
        }

        validateReference(referentElement);

        KeyInfo referent = new KeyInfo(referentElement, baseURI);
        referent.addStorageResolver(storage);
        return referent;
    }

    /**
     * Validate the Element referred to by the KeyInfoReference.
     * 
     * @param referentElement
     * 
     * @throws XMLSecurityException
     */
    private void validateReference(Element referentElement) throws XMLSecurityException {
        if (!XMLUtils.elementIsInSignatureSpace(referentElement, Constants._TAG_KEYINFO)) {
            Object exArgs[] = { new QName(referentElement.getNamespaceURI(), referentElement.getLocalName()) };
            throw new XMLSecurityException("KeyInfoReferenceResolver.InvalidReferentElement.WrongType", exArgs);
        }

        KeyInfo referent = new KeyInfo(referentElement, "");
        if (referent.containsKeyInfoReference()) {
            if (secureValidation) {
                throw new XMLSecurityException("KeyInfoReferenceResolver.InvalidReferentElement.ReferenceWithSecure");
            } else {
                // Don't support chains of references at this time. If do support in the future, this is where the code
                // would go to validate that don't have a cycle, resulting in an infinite loop. This may be unrealistic
                // to implement, and/or very expensive given remote URI references.
                throw new XMLSecurityException("KeyInfoReferenceResolver.InvalidReferentElement.ReferenceWithoutSecure");
            }
        }

    }

    /**
     * Resolve the XML signature input represented by the specified URI.
     * 
     * @param uri
     * @param baseURI
     * @param secureValidation
     * @return the XML signature input represented by the specified URI. 
     * @throws XMLSecurityException 
     */
    private XMLSignatureInput resolveInput(Attr uri, String baseURI, boolean secureValidation)
        throws XMLSecurityException {
        ResourceResolver resRes = ResourceResolver.getInstance(uri, baseURI, secureValidation);
        return resRes.resolve(uri, baseURI, secureValidation);
    }

    /**
     * Resolve the Element effectively represented by the XML signature input source.
     * 
     * @param resource
     * @return the Element effectively represented by the XML signature input source.
     * @throws CanonicalizationException
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws KeyResolverException
     */
    private Element obtainReferenceElement(XMLSignatureInput resource) 
        throws CanonicalizationException, ParserConfigurationException, 
        IOException, SAXException, KeyResolverException {

        Element e;
        if (resource.isElement()){
            e = (Element) resource.getSubNode();
        } else if (resource.isNodeSet()) {
            log.debug("De-reference of KeyInfoReference returned an unsupported NodeSet");
            return null;
        } else {
            // Retrieved resource is a byte stream
            byte inputBytes[] = resource.getBytes();
            e = getDocFromBytes(inputBytes, this.secureValidation);
        }
        return e;
    }
}
