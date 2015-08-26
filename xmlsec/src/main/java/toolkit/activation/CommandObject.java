/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
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

/*
 * @(#)CommandObject.java	1.10 07/05/14
 */

package toolkit.activation;

import java.io.IOException;

/**
 * JavaBeans components that are Activation Framework aware implement
 * this interface to find out which command verb they're being asked
 * to perform, and to obtain the DataHandler representing the
 * data they should operate on.  JavaBeans that don't implement
 * this interface may be used as well.  Such commands may obtain
 * the data using the Externalizable interface, or using an
 * application-specific method.<p>
 */
public interface CommandObject {

    /**
     * Initialize the Command with the verb it is requested to handle
     * and the DataHandler that describes the data it will
     * operate on. <b>NOTE:</b> it is acceptable for the caller
     * to pass <i>null</i> as the value for <code>DataHandler</code>.
     *
     * @param verb The Command Verb this object refers to.
     * @param dh The DataHandler.
     */
    public void setCommandContext(String verb, DataHandler dh)
						throws IOException;
}
