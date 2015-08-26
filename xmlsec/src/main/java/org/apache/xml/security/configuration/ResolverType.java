//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.10 at 02:51:36 PM IST 
//


package org.apache.xml.security.configuration;

import toolkit.xml.bind.annotation.XmlAccessType;
import toolkit.xml.bind.annotation.XmlAccessorType;
import toolkit.xml.bind.annotation.XmlAttribute;
import toolkit.xml.bind.annotation.XmlType;
import toolkit.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for ResolverType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResolverType"&gt;
 *   &lt;simpleContent&gt;
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *       &lt;attribute name="JAVACLASS" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="DESCRIPTION" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/simpleContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResolverType", namespace = "http://www.xmlsecurity.org/NS/configuration", propOrder = {
    "value"
})
public class ResolverType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "JAVACLASS", required = true)
    protected String javaclass;
    @XmlAttribute(name = "DESCRIPTION", required = true)
    protected String description;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the javaclass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJAVACLASS() {
        return javaclass;
    }

    /**
     * Sets the value of the javaclass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJAVACLASS(String value) {
        this.javaclass = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDESCRIPTION() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDESCRIPTION(String value) {
        this.description = value;
    }

}