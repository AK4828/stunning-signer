package id.or.rootca.sivion.toolkit.xml;

import org.apache.commons.io.IOUtils;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.stax.ext.InboundXMLSec;
import org.apache.xml.security.stax.ext.OutboundXMLSec;
import org.apache.xml.security.stax.ext.SecurePart;
import org.apache.xml.security.stax.ext.XMLSec;
import org.apache.xml.security.stax.ext.XMLSecurityConstants;
import org.apache.xml.security.stax.ext.XMLSecurityProperties;
import org.apache.xml.security.stax.impl.securityToken.X509SecurityToken;
import org.apache.xml.security.stax.securityEvent.SecurityEventConstants;
import org.apache.xml.security.stax.securityEvent.SignedElementSecurityEvent;
import org.apache.xml.security.stax.securityEvent.X509TokenSecurityEvent;
import org.apache.xml.security.stax.securityToken.SecurityTokenConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import id.or.rootca.sivion.toolkit.Signer;
import id.or.rootca.sivion.toolkit.commons.KeyPairUtils;
import id.or.rootca.sivion.toolkit.commons.KeyStoreUtils;
import toolkit.xml.namespace.QName;
import toolkit.xml.stream.XMLInputFactory;
import toolkit.xml.stream.XMLStreamException;
import toolkit.xml.stream.XMLStreamReader;
import toolkit.xml.stream.XMLStreamWriter;

public class XmlFileSigner implements Signer<File, File> {
	private KeyPair keyPair;
	private X509Certificate certificate;
	private List<QName> namesToSign;

	public XmlFileSigner(KeyStore keyStore, String alias, char[] password, 
			List<QName> namesToSign) throws Exception {
		this.keyPair = KeyPairUtils.getKeyPair(keyStore, alias, password);
		this.certificate = KeyStoreUtils.getCertificate(keyStore, alias);
		this.namesToSign = namesToSign;
	}

	public XmlFileSigner(KeyStore keyStore, char[] password, List<QName> namesToSign) throws Exception {
		this(keyStore, null, password, namesToSign);
	}
	
	@Override
	public void sign(File input, File output) throws FileNotFoundException, XMLSecurityException, XMLStreamException {
		InputStream inputStream = new FileInputStream(input);
		OutputStream outputStream = new FileOutputStream(output);
		
		// Set up the Configuration
		XMLSecurityProperties properties = new XMLSecurityProperties();
		List<XMLSecurityConstants.Action> actions = new ArrayList<XMLSecurityConstants.Action>();
		actions.add(XMLSecurityConstants.SIGNATURE);
		properties.setActions(actions);

		properties.setSignatureCerts(new X509Certificate[] { certificate });
		properties.setSignatureKey(keyPair.getPrivate());
		properties.setSignatureKeyIdentifier(SecurityTokenConstants.KeyIdentifier_X509KeyIdentifier);

		for (QName nameToSign : namesToSign) {
			SecurePart securePart = new SecurePart(nameToSign, SecurePart.Modifier.Content);
			properties.addSignaturePart(securePart);
		}

		OutboundXMLSec outboundXMLSec = XMLSec.getOutboundXMLSec(properties);
		XMLStreamWriter xmlStreamWriter = outboundXMLSec.processOutMessage(outputStream, "UTF-8");

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);

		XmlReaderToWriter.writeAll(xmlStreamReader, xmlStreamWriter);
		xmlStreamReader.close();
		xmlStreamWriter.close();
		
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
	}

	@Override
	public Collection<? extends Certificate> getCertificates(File input) 
			throws XMLSecurityException, FileNotFoundException, XMLStreamException {
		// Set up the Configuration
		XMLSecurityProperties properties = new XMLSecurityProperties();
		List<XMLSecurityConstants.Action> actions = new ArrayList<XMLSecurityConstants.Action>();
		actions.add(XMLSecurityConstants.SIGNATURE);
		properties.setActions(actions);

		properties.setSignatureVerificationKey(certificate.getPublicKey());

		InboundXMLSec inboundXMLSec = XMLSec.getInboundWSSec(properties);

		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		InputStream inputStream = new FileInputStream(input);
		final XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);

		XmlSecurityEventListener eventListener = new XmlSecurityEventListener();
		XMLStreamReader securityStreamReader = inboundXMLSec.processInMessage(xmlStreamReader, null, eventListener);

		while (securityStreamReader.hasNext()) {
			securityStreamReader.next();
		}

		xmlStreamReader.close();
		IOUtils.closeQuietly(inputStream);

		List<SignedElementSecurityEvent> signedElementEvents = eventListener
				.getSecurityEvents(SecurityEventConstants.SignedElement);

		if (signedElementEvents.isEmpty()) {
			return new ArrayList<Certificate>();
		}

		X509TokenSecurityEvent tokenEvent = (X509TokenSecurityEvent) eventListener
				.getSecurityEvent(SecurityEventConstants.X509Token);

		X509SecurityToken x509SecurityToken = (X509SecurityToken) tokenEvent.getSecurityToken();
		return Arrays.asList(x509SecurityToken.getX509Certificates());
	}

	@Override
	public boolean isCertificateExist(File input, Certificate certificate) 
			throws FileNotFoundException, XMLSecurityException, XMLStreamException {
		for (Certificate cert : getCertificates(input)) {
			if (cert.equals(certificate)) {
				return true;
			}
		}
		
		return false;
	}
}
