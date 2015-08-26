package id.or.rootca.sivion.toolkit.xml;

import java.io.File;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import id.or.rootca.sivion.toolkit.Signer;
import toolkit.xml.bind.JAXBContext;
import toolkit.xml.bind.Marshaller;
import toolkit.xml.namespace.QName;

public class XmlObjectSigner implements Signer<Object, File> {
	private XmlFileSigner fileSigner;
	private final List<QName> namesToSign = Arrays.asList(new QName("", XmlDataWrapper.SIGNED_QNAME));
	
	public XmlObjectSigner(KeyStore keyStore, String alias, char[] password) throws Exception {
		this.fileSigner = new XmlFileSigner(keyStore, alias, password, namesToSign);
	}

	public XmlObjectSigner(KeyStore keyStore, char[] password) throws Exception {
		this.fileSigner = new XmlFileSigner(keyStore, password, namesToSign);
	}
	
	@Override
	public void sign(Object input, File output) throws Exception {
		File tempFile = new File(output.getAbsolutePath() + ".temp");

		JAXBContext context = JAXBContext.newInstance(XmlDataWrapper.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.marshal(new XmlDataWrapper(input), tempFile);

		fileSigner.sign(tempFile, output);
	}

	@Override
	public Collection<? extends Certificate> getCertificates(Object input) {
		throw new RuntimeException("Signing only, use getFileSigner().getCertificates(i) instead");
	}

	@Override
	public boolean isCertificateExist(Object input, Certificate certificate) {
		throw new RuntimeException("Signing only, use getFileSigner().isCertificateExist(i, c) instead");
	}
	
	public XmlFileSigner getFileSigner() {
		return fileSigner;
	}
}
