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
package org.apache.myfaces.trinidadinternal.ui.laf.base.pda;

import javax.faces.context.ResponseWriter;

import org.apache.myfaces.trinidadinternal.ui.RenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UINode;
import org.apache.myfaces.trinidadinternal.ui.Renderer;
import org.apache.myfaces.trinidadinternal.ui.partial.PartialPageRendererUtils;

import java.io.IOException;

/**
 */
public class ShowOneTabRenderer extends org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml.XhtmlLafRenderer
{
  protected String getElementName(
    RenderingContext context,
    UINode           node
    )
  {
    return SPAN_ELEMENT;
  }

  protected void prerender(
    RenderingContext context,
    UINode           node
    ) throws IOException
  {
    super.prerender(context, node);
    
    // Check for partial targets
    Object id = getID(context, node);
    String partialTargets = getEncodedPartialTargets(context, node, id);
    setPartialTargets(context, partialTargets);
    Object position = node.getAttributeValue(context, POSITION_ATTR);
    if (!POSITION_BELOW.equals(position))
    {
      SubTabBarUtils.setSelectedIndex(
                        context,
                        new Integer(_getResolvedSelectedIndex(context, node)));
      Renderer subTabBarRenderer =
          context.getRendererManager().getRenderer(
                               MARLIN_NAMESPACE, SUB_TAB_BAR_NAME);
      subTabBarRenderer.render(context, node);
    }
  }
  
  protected void renderContent(
    RenderingContext context,
    UINode           node
    ) throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();
    writer.startElement( DIV_ELEMENT, null );
    renderStyleClassAttribute(context,
                              AF_SHOW_ONE_TAB_BODY_STYLE_CLASS);
    int selectedChildIndex = _getResolvedSelectedIndex(context, node);
    //Render the content for the selected showItem
    if (selectedChildIndex != -1)
      super.renderContent(
             context, node.getIndexedChild(context, selectedChildIndex));
    writer.endElement( DIV_ELEMENT );
  }

  protected void postrender(
    RenderingContext context,
    UINode           node
    ) throws IOException
  {
    Object position = node.getAttributeValue(context, POSITION_ATTR);
    if (POSITION_BELOW.equals(position))
    {
      Renderer subTabBarRenderer =
        context.getRendererManager().getRenderer(
                            MARLIN_NAMESPACE, SUB_TAB_BAR_NAME);
      subTabBarRenderer.render(context, node);
    }
    
    setPartialTargets(context, null);
    super.postrender(context, node);
  }

  // Returns the partial targets for this node, if any,
  // in encoded form.
  protected static String getEncodedPartialTargets(
    RenderingContext context,
    UINode           node,
    Object           id
    )
  {
    String[] partialTargets = PdaHtmlLafUtils.getPartialTargets(context, node, id);
    return PartialPageRendererUtils.encodePartialTargets(partialTargets);
  }

  // Sets the partial targets on the RenderingContext
  protected static void setPartialTargets(
    RenderingContext context,
    String           partialTargets)
  {
    context.setProperty(MARLIN_NAMESPACE,
            PdaHtmlLafConstants.LINK_CONTAINER_PARTIAL_TARGETS_PROPERTY,
                        partialTargets);
  }

  /**
   * Returns the index of the first avilable showItem child that has its
   *  'selected' property set to true.
   * If none of children are selected, the index of first such child that is
   *  enabled is returned.
   * Returns -1 if both of these fail.
   *
   * @todo pudupa: XhtmlLafRenderer.getResolvedSelectedIndex() is not a clear
   *   reusable code for 3.0, consolidate for code reuse at a later stage when
   *   the selection model (whether parent has this info or the children) is clear.
   */
  private static int _getResolvedSelectedIndex(
    RenderingContext context,
    UINode parentNode)
  {
    int childCount = parentNode.getIndexedChildCount(context);
    int firstEnabledChildIndex = -1;
    for (int childIndex=0; childIndex<childCount; childIndex++)
    {
      UINode childNode = parentNode.getIndexedChild(context, childIndex);
      
      if (Boolean.TRUE.equals(
            childNode.getAttributeValue(context, DISCLOSED_ATTR)))
      {
        return childIndex;
      }
      if (firstEnabledChildIndex == -1  &&
          !Boolean.TRUE.equals(
              childNode.getAttributeValue(context, DISABLED_ATTR)))
      {
        firstEnabledChildIndex = childIndex;
      }
    }

    return firstEnabledChildIndex;
  }
}
