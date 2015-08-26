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
package org.apache.xml.security.stax.ext.stax;

import toolkit.xml.stream.events.Characters;

/**
 * @author $Author: giger $
 * @version $Revision: 1398806 $ $Date: 2012-10-16 15:01:40 +0100 (Tue, 16 Oct 2012) $
 */
public interface XMLSecCharacters extends XMLSecEvent, Characters {

    @Override
    XMLSecCharacters asCharacters();

    char[] getText();
}
