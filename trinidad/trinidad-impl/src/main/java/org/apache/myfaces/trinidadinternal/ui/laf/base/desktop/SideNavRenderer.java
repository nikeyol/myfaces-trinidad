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
import org.apache.myfaces.trinidadinternal.util.IntegerUtils;


/**
 * Renderer for sideNav
 *
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/ui/laf/base/desktop/SideNavRenderer.java#0 $) $Date: 10-nov-2005.18:56:16 $
 * @author The Oracle ADF Faces Team
 */
public class SideNavRenderer extends SideBarRenderer
{

  protected void prerender(
    RenderingContext context,
    UINode           node
  )throws IOException
  {
    super.prerender(context, node);

    int selectedIndex =  getResolvedSelectedIndex( context, node);

    context.setLocalProperty( _SELECTED_INDEX_KEY,
                              IntegerUtils.getInteger(selectedIndex));
  }



  protected void renderIndexedChild(
    RenderingContext context,
    UINode           node,
    int              childIndex
  )throws IOException
  {


    int selectedIndex = ((Number)context.getLocalProperty(0,
                                      _SELECTED_INDEX_KEY,
                                      IntegerUtils.getInteger(-1))).intValue();

    ResponseWriter writer = context.getResponseWriter();

    writer.startElement(DIV_ELEMENT, null);
    if ( selectedIndex >= 0 &&
         selectedIndex == childIndex )
      renderStyleClassAttribute(context, NAV_3_SELECTED_STYLE_CLASS);
    super.renderIndexedChild(context, node, childIndex);
    writer.endElement(DIV_ELEMENT);

  }

  private static final Object _SELECTED_INDEX_KEY = new Object();
}
