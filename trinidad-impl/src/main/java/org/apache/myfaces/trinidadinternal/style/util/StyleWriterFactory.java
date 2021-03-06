/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.trinidadinternal.style.util;

import java.io.PrintWriter;

/**
 * Factory that creates {@link PrintWriter} instances for the Skin engine to write content
 * onto. Each call to the create writer function must return a new instance of a print writer.
 * <p>This method is used to write multiple CSS files for a skin to be able to span CSS selectors
 * over multiple files</p>
 */
public interface StyleWriterFactory
{
  /**
   * Creates a new PrintWriter.  The caller is responsible for closing the Writer when done with it
   * @return a new instance of a PrintWriter
   */
  PrintWriter createWriter();
}
