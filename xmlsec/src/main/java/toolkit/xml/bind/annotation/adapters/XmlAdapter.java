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

package toolkit.xml.bind.annotation.adapters;

import toolkit.xml.bind.ValidationEventHandler;

/**
 * Adapts a Java type for custom marshaling.
 *
 * <p> <b> Usage: </b> </p>
 *
 * <p>
 * Some Java types do not map naturally to a XML representation, for
 * example <tt>HashMap</tt> or other non JavaBean classes. Conversely,
 * a XML repsentation may map to a Java type but an application may
 * choose to accesss the XML representation using another Java
 * type. For example, the schema to Java binding rules bind
 * xs:DateTime by default to XmlGregorianCalendar. But an application
 * may desire to bind xs:DateTime to a custom type,
 * MyXmlGregorianCalendar, for example. In both cases, there is a
 * mismatch between <i> bound type </i>, used by an application to
 * access XML content and the <i> value type</i>, that is mapped to an
 * XML representation.  
 *
 * <p>
 * This abstract class defines methods for adapting a bound type to a value
 * type or vice versa. The methods are invoked by the JAXB binding
 * framework during marshaling and unmarshalling:
 *
 * <ul>
 *   <li> <b> XmlAdapter.marshal(...): </b> During marshalling, JAXB
 *        binding framework invokes XmlAdapter.marshal(..) to adapt a
 *        bound type to value type, which is then marshaled to XML 
 *        representation. </li> 
 *
 *   <li> <b> XmlAdapter.unmarshal(...): </b> During unmarshalling,
 *        JAXB binding framework first unmarshals XML representation
 *        to a value type and then invokes XmlAdapter.unmarshal(..) to
 *        adapt the value type to a bound type. </li> 
 * </ul>
 *
 * Writing an adapter therefore involves the following steps:
 * 
 * <ul>
 *   <li> Write an adapter that implements this abstract class. </li>
 *   <li> Install the adapter using the annotation {@link
 *        XmlJavaTypeAdapter} </li>
 * </ul>
 *
 * <p><b>Example:</b> Customized mapping of <tt>HashMap</tt></p>
 * <p> The following example illustrates the use of 
 * <tt>&#64;XmlAdapter</tt> and <tt>&#64;XmlJavaTypeAdapter</tt> to
 * customize the mapping of a <tt>HashMap</tt>.
 *
 * <p> <b> Step 1: </b> Determine the desired XML representation for HashMap.
 *
 * <pre>
 *     &lt;hashmap&gt;
 *         &lt;entry key="id123"&gt;this is a value&lt;/entry&gt;
 *         &lt;entry key="id312"&gt;this is another value&lt;/entry&gt;
 *         ...
 *       &lt;/hashmap&gt;
 * </pre>
 *
 * <p> <b> Step 2: </b> Determine the schema definition that the
 * desired XML representation shown above should follow.
 *
 * <pre>
 *     
 *     &lt;xs:complexType name="myHashMapType"&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="entry" type="myHashMapEntryType"
 *                        minOccurs = "0" maxOccurs="unbounded"/&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexType&gt;
 *
 *     &lt;xs:complexType name="myHashMapEntryType"&gt;
 *       &lt;xs:simpleContent&gt;
 *         &lt;xs:extension base="xs:string"&gt;
 *           &lt;xs:attribute name="key" type="xs:int"/&gt;
 *         &lt;/xs:extension&gt;
 *       &lt;/xs:simpleContent&gt;
 *     &lt;/xs:complexType&gt;
 *
 * </pre>
 *
 * <p> <b> Step 3: </b> Write value types that can generate the above
 * schema definition.
 *
 * <pre>
 *     public class MyHashMapType {
 *         List&lt;MyHashMapEntryType&gt; entry;
 *     }
 *
 *     public class MyHashMapEntryType {
 *         &#64;XmlAttribute
 *         public Integer key; 
 *
 *         &#64;XmlValue
 *         public String value;
 *     }
 * </pre>
 * 
 * <p> <b> Step 4: </b> Write the adapter that adapts the value type,
 * MyHashMapType to a bound type, HashMap, used by the application.
 *
 * <pre>
 *     public final class MyHashMapAdapter extends
 *                        XmlAdapter&lt;MyHashMapType,HashMap&gt; { ... }
 *      
 * </pre>
 *
 * <p> <b> Step 5: </b> Use the adapter.
 *
 * <pre>
 *     public class Foo {
 *         &#64;XmlJavaTypeAdapter(MyHashMapAdapter.class)
 *         HashMap hashmap;
 *         ...
 *     }
 * </pre>
 *
 * The above code fragment will map to the following schema:
 * 
 * <pre>
 *     &lt;xs:complexType name="Foo"&gt;
 *       &lt;xs:sequence&gt;
 *         &lt;xs:element name="hashmap" type="myHashMapType"&gt;
 *       &lt;/xs:sequence&gt;
 *     &lt;/xs:complexType&gt;
 * </pre>
 *
 * @param <BoundType>
 *      The type that JAXB doesn't know how to handle. An adapter is written
 *      to allow this type to be used as an in-memory representation through
 *      the <tt>ValueType</tt>.
 * @param <ValueType>
 *      The type that JAXB knows how to handle out of the box.
 *
 * @author <ul><li>Sekhar Vajjhala, Sun Microsystems Inc.</li> <li> Kohsuke Kawaguchi, Sun Microsystems Inc.</li></ul>
 * @see XmlJavaTypeAdapter
 * @since 1.6, JAXB 2.0
 */
public abstract class XmlAdapter<ValueType,BoundType> {

    /**
     * Do-nothing constructor for the derived classes.
     */
    protected XmlAdapter() {}

    /**
     * Convert a value type to a bound type.
     *
     * @param v
     *      The value to be converted. Can be null.
     * @throws Exception
     *      if there's an error during the conversion. The caller is responsible for
     *      reporting the error to the user through {@link ValidationEventHandler}.
     */
    public abstract BoundType unmarshal(ValueType v) throws Exception;

    /**
     * Convert a bound type to a value type.
     *
     * @param v
     *      The value to be convereted. Can be null.
     * @throws Exception
     *      if there's an error during the conversion. The caller is responsible for
     *      reporting the error to the user through {@link ValidationEventHandler}.
     */
    public abstract ValueType marshal(BoundType v) throws Exception;
}
