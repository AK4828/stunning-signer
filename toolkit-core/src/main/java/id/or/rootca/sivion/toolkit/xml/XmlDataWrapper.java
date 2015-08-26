package id.or.rootca.sivion.toolkit.xml;

import toolkit.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class XmlDataWrapper {
	public static final String SIGNED_QNAME = "content";
	
	private Object content;

	public XmlDataWrapper(Object content) {
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	public String type() {
		return content.getClass().getSimpleName();
	}
}
