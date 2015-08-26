//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.10 at 02:51:36 PM IST 
//


package org.apache.xml.security.binding.xmlenc11;

import java.math.BigInteger;
import toolkit.xml.bind.annotation.XmlAccessType;
import toolkit.xml.bind.annotation.XmlAccessorType;
import toolkit.xml.bind.annotation.XmlElement;
import toolkit.xml.bind.annotation.XmlSchemaType;
import toolkit.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PBKDF2ParameterType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PBKDF2ParameterType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Salt"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;choice&gt;
 *                   &lt;element name="Specified" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *                   &lt;element name="OtherSource" type="{http://www.w3.org/2009/xmlenc11#}AlgorithmIdentifierType"/&gt;
 *                 &lt;/choice&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="IterationCount" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/&gt;
 *         &lt;element name="KeyLength" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/&gt;
 *         &lt;element name="PRF" type="{http://www.w3.org/2009/xmlenc11#}PRFAlgorithmIdentifierType"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PBKDF2ParameterType", namespace = "http://www.w3.org/2009/xmlenc11#", propOrder = {
    "salt",
    "iterationCount",
    "keyLength",
    "prf"
})
public class PBKDF2ParameterType {

    @XmlElement(name = "Salt", namespace = "http://www.w3.org/2009/xmlenc11#", required = true)
    protected PBKDF2ParameterType.Salt salt;
    @XmlElement(name = "IterationCount", namespace = "http://www.w3.org/2009/xmlenc11#", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger iterationCount;
    @XmlElement(name = "KeyLength", namespace = "http://www.w3.org/2009/xmlenc11#", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger keyLength;
    @XmlElement(name = "PRF", namespace = "http://www.w3.org/2009/xmlenc11#", required = true)
    protected PRFAlgorithmIdentifierType prf;

    /**
     * Gets the value of the salt property.
     * 
     * @return
     *     possible object is
     *     {@link Salt }
     *     
     */
    public Salt getSalt() {
        return salt;
    }

    /**
     * Sets the value of the salt property.
     * 
     * @param value
     *     allowed object is
     *     {@link Salt }
     *     
     */
    public void setSalt(Salt value) {
        this.salt = value;
    }

    /**
     * Gets the value of the iterationCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIterationCount() {
        return iterationCount;
    }

    /**
     * Sets the value of the iterationCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIterationCount(BigInteger value) {
        this.iterationCount = value;
    }

    /**
     * Gets the value of the keyLength property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getKeyLength() {
        return keyLength;
    }

    /**
     * Sets the value of the keyLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setKeyLength(BigInteger value) {
        this.keyLength = value;
    }

    /**
     * Gets the value of the prf property.
     * 
     * @return
     *     possible object is
     *     {@link PRFAlgorithmIdentifierType }
     *     
     */
    public PRFAlgorithmIdentifierType getPRF() {
        return prf;
    }

    /**
     * Sets the value of the prf property.
     * 
     * @param value
     *     allowed object is
     *     {@link PRFAlgorithmIdentifierType }
     *     
     */
    public void setPRF(PRFAlgorithmIdentifierType value) {
        this.prf = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;choice&gt;
     *         &lt;element name="Specified" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
     *         &lt;element name="OtherSource" type="{http://www.w3.org/2009/xmlenc11#}AlgorithmIdentifierType"/&gt;
     *       &lt;/choice&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "specified",
        "otherSource"
    })
    public static class Salt {

        @XmlElement(name = "Specified", namespace = "http://www.w3.org/2009/xmlenc11#")
        protected byte[] specified;
        @XmlElement(name = "OtherSource", namespace = "http://www.w3.org/2009/xmlenc11#")
        protected AlgorithmIdentifierType otherSource;

        /**
         * Gets the value of the specified property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getSpecified() {
            return specified;
        }

        /**
         * Sets the value of the specified property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setSpecified(byte[] value) {
            this.specified = value;
        }

        /**
         * Gets the value of the otherSource property.
         * 
         * @return
         *     possible object is
         *     {@link AlgorithmIdentifierType }
         *     
         */
        public AlgorithmIdentifierType getOtherSource() {
            return otherSource;
        }

        /**
         * Sets the value of the otherSource property.
         * 
         * @param value
         *     allowed object is
         *     {@link AlgorithmIdentifierType }
         *     
         */
        public void setOtherSource(AlgorithmIdentifierType value) {
            this.otherSource = value;
        }

    }

}
