//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.10 at 02:51:36 PM IST 
//


package org.apache.xml.security.binding.xmldsig11;

import toolkit.xml.bind.annotation.XmlAccessType;
import toolkit.xml.bind.annotation.XmlAccessorType;
import toolkit.xml.bind.annotation.XmlAttribute;
import toolkit.xml.bind.annotation.XmlElement;
import toolkit.xml.bind.annotation.XmlID;
import toolkit.xml.bind.annotation.XmlSchemaType;
import toolkit.xml.bind.annotation.XmlType;
import toolkit.xml.bind.annotation.adapters.CollapsedStringAdapter;
import toolkit.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ECKeyValueType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ECKeyValueType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;element name="ECParameters" type="{http://www.w3.org/2009/xmldsig11#}ECParametersType"/&gt;
 *           &lt;element name="NamedCurve" type="{http://www.w3.org/2009/xmldsig11#}NamedCurveType"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="PublicKey" type="{http://www.w3.org/2009/xmldsig11#}ECPointType"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ECKeyValueType", namespace = "http://www.w3.org/2009/xmldsig11#", propOrder = {
    "ecParameters",
    "namedCurve",
    "publicKey"
})
public class ECKeyValueType {

    @XmlElement(name = "ECParameters", namespace = "http://www.w3.org/2009/xmldsig11#")
    protected ECParametersType ecParameters;
    @XmlElement(name = "NamedCurve", namespace = "http://www.w3.org/2009/xmldsig11#")
    protected NamedCurveType namedCurve;
    @XmlElement(name = "PublicKey", namespace = "http://www.w3.org/2009/xmldsig11#", required = true)
    protected byte[] publicKey;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the ecParameters property.
     * 
     * @return
     *     possible object is
     *     {@link ECParametersType }
     *     
     */
    public ECParametersType getECParameters() {
        return ecParameters;
    }

    /**
     * Sets the value of the ecParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ECParametersType }
     *     
     */
    public void setECParameters(ECParametersType value) {
        this.ecParameters = value;
    }

    /**
     * Gets the value of the namedCurve property.
     * 
     * @return
     *     possible object is
     *     {@link NamedCurveType }
     *     
     */
    public NamedCurveType getNamedCurve() {
        return namedCurve;
    }

    /**
     * Sets the value of the namedCurve property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedCurveType }
     *     
     */
    public void setNamedCurve(NamedCurveType value) {
        this.namedCurve = value;
    }

    /**
     * Gets the value of the publicKey property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the value of the publicKey property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPublicKey(byte[] value) {
        this.publicKey = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
