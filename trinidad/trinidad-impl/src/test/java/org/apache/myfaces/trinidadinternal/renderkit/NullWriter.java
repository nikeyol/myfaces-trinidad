/*
* Copyright 2006 The Apache Software Foundation.
*
* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.apache.myfaces.trinidadinternal.renderkit;

import java.io.Writer;

public class NullWriter extends Writer
{
  public void write(char[] buffer)
  {
  }

  public void write(char[] buffer, int off, int len)
  {
  }

  public void write(String str)
  {
  }

  public void write(int c)
  {
  }

  public void write(String str, int off, int len)
  {
  }

  public void close()
  {
  }

  public void flush()
  {
  }
}
