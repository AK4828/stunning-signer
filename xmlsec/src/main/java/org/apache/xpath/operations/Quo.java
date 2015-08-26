/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: Quo.java 468655 2006-10-28 07:12:06Z minchau $
 */
package org.apache.xpath.operations;

import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

import toolkit.xml.transform.TransformerException;

/**
 * The 'quo' operation expression executer. (no longer supported by XPath).
 * @deprecated
 */
public class Quo extends Operation
{
    static final long serialVersionUID = 693765299196169905L;

  // Actually, this is no longer supported by xpath...

  /**
   * Apply the operation to two operands, and return the result.
   *
   *
   * @param left non-null reference to the evaluated left operand.
   * @param right non-null reference to the evaluated right operand.
   *
   * @return non-null reference to the XObject that represents the result of the operation.
   *
   * @throws TransformerException
   */
  public XObject operate(XObject left, XObject right)
          throws TransformerException
  {
    return new XNumber((int) (left.num() / right.num()));
  }
}