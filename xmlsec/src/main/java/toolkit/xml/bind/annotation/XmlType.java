/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2004-2014 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package toolkit.xml.bind.annotation;

import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 * <p>
 * Maps a class or an enum type to a XML Schema type.
 *
 * <p><b>Usage</b></p>
 * <p> The <tt>@XmlType</tt> annnotation can be used with the following program
 * elements:
 * <ul>
 *   <li> a top level class </li>
 *   <li> an enum type </li>
 * </ul>
 *
 * <p>See "Package Specification" in toolkit.xml.bind.package javadoc for
 * additional common information.</p>
 *
 * <h3> Mapping a Class </h3> 
 * <p>
 * A class maps to a XML Schema type. A class is a data container for
 * values represented by properties and fields. A schema type is a
 * data container for values represented by schema components within a
 * schema type's content model (e.g. model groups, attributes etc).
 * <p> To be mapped, a class must either have a public no-arg
 * constructor or a static no-arg factory method. The static factory
 * method can be specified in <tt>factoryMethod()</tt> and
 * <tt>factoryClass()</tt> annotation elements. The static factory
 * method or the no-arg constructor is used during unmarshalling to
 * create an instance of this class. If both are present, the static
 * factory method overrides the no-arg constructor.
 * <p>
 * A class maps to either a XML Schema complex type or a XML Schema simple
 * type. The XML Schema type is derived based on the 
 * mapping of JavaBean properties and fields contained within the
 * class. The schema type to which the class is mapped can either be
 * named or anonymous. A class can be mapped to an anonymous schema
 * type by annotating the class with <tt>&#64;XmlType(name="")</tt>.
 * <p>
 * Either a global element, local element or a local attribute can be
 * associated with an anonymous type as follows:
 * <ul>
 *   <li><b>global element: </b> A global element of an anonymous
 *      type can be derived by annotating the class with @{@link
 *      XmlRootElement}. See Example 3 below. </li> 
 *
 *   <li><b>local element: </b> A JavaBean property that references
 *      a class annotated with @XmlType(name="") and is mapped to the
 *      element associated with the anonymous type. See Example 4
 *      below.</li> 
 *
 *   <li><b>attribute: </b> A JavaBean property that references
 *      a class annotated with @XmlType(name="") and is mapped to the
 *      attribute associated with the anonymous type. See Example 5 below. </li>
 * </ul>
 * <b> Mapping to XML Schema Complex Type </b>
 * <ul>
 *   <li>If class is annotated with <tt>@XmlType(name="") </tt>, it
 *   is mapped to an anonymous type otherwise, the class name maps
 *   to a complex type name. The <tt>XmlName()</tt> annotation element
 *   can be used to customize the name.</li>  
 *
 *   <li> Properties and fields that are mapped to elements are mapped to a
 *   content model within a complex type. The annotation element
 *   <tt>propOrder()</tt> can be used to customize the content model to be
 *   <tt>xs:all</tt> or <tt>xs:sequence</tt>.  It is used for specifying
 *   the order of XML elements in <tt>xs:sequence</tt>. </li> 
 *
 *   <li> Properties and fields can be mapped to attributes within the
 *        complex type.  </li>
 *
 *   <li> The targetnamespace of the XML Schema type can be customized
 *        using the annotation element <tt>namespace()</tt>. </li>
 * </ul>
 *
 * <p>
 * <b> Mapping class to XML Schema simple type </b>
 * <p>
 * A class can be mapped to a XML Schema simple type using the
 * <tt>@XmlValue</tt> annotation. For additional details and examples,
 * see @{@link XmlValue} annotation type.
 * <p>
 * The following table shows the mapping of the class to a XML Schema
 * complex type or simple type. The notational symbols used in the table are:
 * <ul>
 *   <li> -&gt;    : represents a mapping </li>
 *   <li> [x]+  : one or more occurances of x </li>
 *   <li> [ <tt>@XmlValue</tt> property ]: JavaBean property annotated with
 *         <tt>@XmlValue</tt></li>
 *   <li> X     : don't care
 * </ul>
 * <blockquote>
 *   <table summary="" border="1" cellpadding="4" cellspacing="3">
 *     <tbody>
 *       <tr>
 *         <td><b>Target</b></td>
 *         <td><b>propOrder</b></td>
 *         <td><b>ClassBody</b></td>
 *         <td><b>ComplexType</b></td>
 *         <td><b>SimpleType</b></td>
 *       </tr>
 * 
 *       <tr valign="top">
 *         <td>Class</td>
 *         <td>{}</td>
 *         <td>[property]+ -&gt; elements</td>
 *         <td>complexcontent<br>xs:all</td>
 *         <td> </td>
 *       </tr>
 * 
 *       <tr valign="top">
 *         <td>Class</td>
 *         <td>non empty</td>
 *         <td>[property]+ -&gt; elements</td>
 *         <td>complexcontent<br>xs:sequence</td>
 *         <td> </td>
 *       </tr>
 * 
 *       <tr valign="top">
 *         <td>Class</td>
 *         <td>X</td>
 *         <td>no property -&gt; element</td>
 *         <td>complexcontent<br>empty sequence</td>
 *         <td> </td>
 *       </tr>
 * 
 *       <tr valign="top">
 *         <td>Class</td>
 *         <td>X</td>
 *         <td>1 [<tt>@XmlValue</tt> property] {@literal &&} <br> [property]+ -&gt; attributes</td>
 *         <td>simplecontent</td>
 *         <td> </td>
 *       </tr>
 * 
 *       <tr valign="top">
 *         <td>Class</td>
 *         <td>X</td>
 *         <td>1 [<tt>@XmlValue</tt> property] {@literal &&} <br> no properties -&gt; attribute</td>
 *         <td> </td>
 *         <td>simpletype</td>
 *       </tr>
 *     </tbody>
 *   </table>
 * </blockquote>
 * 
 * <h3> Mapping an enum type </h3>
 * 
 * An enum type maps to a XML schema simple type with enumeration
 * facets. The following annotation elements are ignored since they
 * are not meaningful: <tt>propOrder()</tt> , <tt>factoryMethod()</tt> , 
 * <tt>factoryClass()</tt> .
 *
 *  <h3> Usage with other annotations </h3>
 * <p> This annotation can be used with the following annotations: 
 * {@link XmlRootElement}, {@link XmlAccessorOrder}, {@link XmlAccessorType},
 * {@link XmlEnum}. However, {@link
 * XmlAccessorOrder} and {@link XmlAccessorType} are ignored when this
 * annotation is used on an enum type.
 * 
 * <p> <b> Example 1: </b> Map a class to a complex type with
 *   xs:sequence with a customized ordering of JavaBean properties. 
 * </p>
 *
 * <pre>
 *   &#64;XmlType(propOrder={"street", "city" , "state", "zip", "name" })
 *   public class USAddress {
 *     String getName() {..};
 *     void setName(String) {..};
 * 
 *     String getStreet() {..};
 *     void setStreet(String) {..};
 *
 *     String getCity() {..}; 
 *     void setCity(String) {..};
 * 
 *     String getState() {..};
 *     void setState(String) {..};
 *
 *     java.math.BigDecimal getZip() {..};
 *     void setZip(java.math.BigDecimal) {..};
 *   }
 *
 *   &lt;!-- XML Schema mapping for USAddress --&gt;
 *   &lt;xs:complexType name="USAddress"&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:element name="street" type="xs:string"/&gt;
 *       &lt;xs:element name="city" type="xs:string"/&gt;
 *       &lt;xs:element name="state" type="xs:string"/&gt;
 *       &lt;xs:element name="zip" type="xs:decimal"/&gt;
 *       &lt;xs:element name="name" type="xs:string"/&gt;
 *     &lt;/xs:all&gt;
 *   &lt;/xs:complexType&gt;
 * </pre>
 * <p> <b> Example 2: </b> Map a class to a complex type with
 *     xs:all </p>
 * <pre>
 * &#64;XmlType(propOrder={})
 * public class USAddress { ...}
 * 
 * &lt;!-- XML Schema mapping for USAddress --&gt;
 * &lt;xs:complexType name="USAddress"&gt;
 *   &lt;xs:all&gt;
 *     &lt;xs:element name="name" type="xs:string"/&gt;
 *     &lt;xs:element name="street" type="xs:string"/&gt;
 *     &lt;xs:element name="city" type="xs:string"/&gt;
 *     &lt;xs:element name="state" type="xs:string"/&gt;
 *     &lt;xs:element name="zip" type="xs:decimal"/&gt;
 *   &lt;/xs:sequence&gt;
 * &lt;/xs:complexType&gt;
 *</pre>
 * <p> <b> Example 3: </b> Map a class to a global element with an
 * anonymous type. 
 * </p>
 * <pre>
 *   &#64;XmlRootElement
 *   &#64;XmlType(name="")
 *   public class USAddress { ...}
 *
 *   &lt;!-- XML Schema mapping for USAddress --&gt;
 *   &lt;xs:element name="USAddress"&gt;
 *     &lt;xs:complexType&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="name" type="xs:string"/&gt;
 *         &lt;xs:element name="street" type="xs:string"/&gt;
 *         &lt;xs:element name="city" type="xs:string"/&gt;
 *         &lt;xs:element name="state" type="xs:string"/&gt;
 *         &lt;xs:element name="zip" type="xs:decimal"/&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexType&gt;
 *   &lt;/xs:element&gt;
 * </pre>
 *
 * <p> <b> Example 4: </b> Map a property to a local element with
 * anonymous type.
 * <pre>
 *   //Example: Code fragment
 *   public class Invoice {
 *       USAddress addr;
 *           ...
 *       }
 *
 *   &#64;XmlType(name="")
 *   public class USAddress { ... }
 *   } 
 *
 *   &lt;!-- XML Schema mapping for USAddress --&gt;
 *   &lt;xs:complexType name="Invoice"&gt;
 *     &lt;xs:sequence&gt;
 *       &lt;xs:element name="addr"&gt;
 *         &lt;xs:complexType&gt;
 *           &lt;xs:element name="name", type="xs:string"/&gt;
 *           &lt;xs:element name="city", type="xs:string"/&gt;
 *           &lt;xs:element name="city" type="xs:string"/&gt;
 *           &lt;xs:element name="state" type="xs:string"/&gt;
 *           &lt;xs:element name="zip" type="xs:decimal"/&gt;
 *         &lt;/xs:complexType&gt;
 *       ...
 *     &lt;/xs:sequence&gt;
 *   &lt;/xs:complexType&gt;
 * </pre>
 *
 * <p> <b> Example 5: </b> Map a property to an attribute with
 * anonymous type.
 * 
 * <pre>
 *
 *     //Example: Code fragment
 *     public class Item {
 *         public String name;
 *         &#64;XmlAttribute 
 *         public USPrice price;
 *     }
 *    
 *     // map class to anonymous simple type. 
 *     &#64;XmlType(name="")
 *     public class USPrice { 
 *         &#64;XmlValue
 *         public java.math.BigDecimal price;
 *     }
 *
 *     &lt;!-- Example: XML Schema fragment --&gt;
 *     &lt;xs:complexType name="Item"&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="name" type="xs:string"/&gt;
 *         &lt;xs:attribute name="price"&gt;
 *           &lt;xs:simpleType&gt;
 *             &lt;xs:restriction base="xs:decimal"/&gt;
 *           &lt;/xs:simpleType&gt;
 *         &lt;/xs:attribute&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexType&gt;
 * </pre>
 *
 *  <p> <b> Example 6: </b> Define a factoryClass and factoryMethod
 *
 * <pre> 
 *      &#64;XmlType(name="USAddressType", factoryClass=USAddressFactory.class,
 *      factoryMethod="getUSAddress")
 *      public class USAddress {
 *
 *          private String city;
 *          private String name;
 *          private String state;
 *          private String street;
 *          private int    zip;
 *
 *      public USAddress(String name, String street, String city, 
 *          String state, int zip) {
 *          this.name = name;
 *          this.street = street;
 *          this.city = city;
 *          this.state = state;
 *          this.zip = zip;
 *      }
 *  }
 *
 *  public class USAddressFactory {
 *      public static USAddress getUSAddress(){
 *       return new USAddress("Mark Baker", "23 Elm St", 
 *          "Dayton", "OH", 90952);
 *  }
 *
 * </pre>
 *
 *  <p> <b> Example 7: </b> Define factoryMethod and use the default factoryClass
 * 
 * <pre>
 *      &#64;XmlType(name="USAddressType", factoryMethod="getNewInstance")
 *      public class USAddress {
 *
 *          private String city;
 *          private String name;
 *          private String state;
 *          private String street;
 *          private int    zip;
 *
 *          private USAddress() {}
 *
 *          public static USAddress getNewInstance(){
 *              return new USAddress();
 *          }
 *      }
 * </pre>
 *
 * @author Sekhar Vajjhala, Sun Microsystems, Inc.
 * @see XmlElement
 * @see XmlAttribute
 * @see XmlValue
 * @see XmlSchema
 * @since 1.6, JAXB 2.0
 */

@Retention(RUNTIME) @Target({TYPE})
public @interface XmlType {
    /**
     * Name of the XML Schema type which the class is mapped.
     */
    String name() default "##default" ;
 
    /**
     * Specifies the order for XML Schema elements when class is
     * mapped to a XML Schema complex type.
     * 
     * <p> Refer to the table for how the propOrder affects the
     * mapping of class </p>
     * 
     * <p> The propOrder is a list of names of JavaBean properties in
     *     the class. Each name in the list is the name of a Java
     *     identifier of the JavaBean property. The order in which
     *     JavaBean properties are listed is the order of XML Schema
     *     elements to which the JavaBean properties are mapped. </p>
     * <p> All of the JavaBean properties being mapped to XML Schema elements
     *     must be listed. 
     * <p> A JavaBean property or field listed in propOrder must not
     *     be transient or annotated with <tt>@XmlTransient</tt>.
     * <p> The default ordering of JavaBean properties is determined
     *     by @{@link XmlAccessorOrder}. 
     */
    String[] propOrder() default {""};

    /**
     * Name of the target namespace of the XML Schema type. By
     * default, this is the target namespace to which the package
     * containing the class is mapped.
     */
    String namespace() default "##default" ;
   
    /**
     * Class containing a no-arg factory method for creating an
     * instance of this class. The default is this class.
     * 
     * <p>If <tt>factoryClass</tt> is DEFAULT.class and 
     * <tt>factoryMethod</tt> is "", then there is no static factory
     * method.
     * 
     * <p>If <tt>factoryClass</tt> is DEFAULT.class and
     * <tt>factoryMethod</tt> is not "", then 
     * <tt>factoryMethod</tt> is the name of a static factory method
     * in this class. 
     *
     * <p>If <tt>factoryClass</tt> is not DEFAULT.class, then 
     * <tt>factoryMethod</tt> must not be "" and must be the name of
     * a static factory method specified in <tt>factoryClass</tt>.
     */
    Class factoryClass() default DEFAULT.class;

    /**
     * Used in {@link XmlType#factoryClass()} to
     * signal that either factory mehod is not used or
     * that it's in the class with this {@link XmlType} itself. 
     */
    static final class DEFAULT {}

    /**
     * Name of a no-arg factory method in the class specified in
     * <tt>factoryClass</tt> factoryClass(). 
     * 
     */
    String factoryMethod() default "";
}


