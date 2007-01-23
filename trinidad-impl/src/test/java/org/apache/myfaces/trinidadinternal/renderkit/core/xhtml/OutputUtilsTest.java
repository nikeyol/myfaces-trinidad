/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;

public class OutputUtilsTest extends TestCase
{
  public OutputUtilsTest(String name)
  {
    super(name);
  }

  public void testStyleClassListParsing() throws Exception
  {
    assertEquals(OutputUtils.parseStyleClassList("a"), null);
    assertEquals(OutputUtils.parseStyleClassList(" a "),
                 Arrays.asList("a"));
    assertEquals(OutputUtils.parseStyleClassList("a b c"),
                 Arrays.asList("a", "b", "c"));
    assertEquals(OutputUtils.parseStyleClassList("a   b   c"),
                 Arrays.asList("a", "b", "c"));
    assertEquals(OutputUtils.parseStyleClassList("ab c"),
                 Arrays.asList("ab", "c"));
  }
}

