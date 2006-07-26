/*
 * Copyright  2000-2006 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.trinidadinternal.ui.laf.base.desktop;

import java.io.IOException;

import javax.faces.context.ResponseWriter;

import org.apache.myfaces.trinidadinternal.ui.RenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UINode;

/**
 * Renderer that renders the separator.
 * <p>
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/ui/laf/base/desktop/SeparatorRenderer.java#0 $) $Date: 10-nov-2005.18:56:14 $
 * @author The Oracle ADF Faces Team
 */
public class SeparatorRenderer extends HtmlLafRenderer
{
  protected void renderContent(
    RenderingContext context,
    UINode           node
    ) throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();
    writer.startElement("hr", node.getUIComponent());
    renderStyleClassAttribute(context, AF_SEPARATOR_STYLE_CLASS);
    super.renderAttributes(context, node); 
    writer.endElement("hr");
  }

  protected void prerender(
    RenderingContext context,
    UINode           node
    ) throws IOException
  { 
    // do nothing
  }


  protected void postrender(
    RenderingContext context,
    UINode           node) throws IOException
  {
    // do nothing
  }
}
