/*
 * Copyright  2005,2006 The Apache Software Foundation.
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
package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.trinidadinternal.agent.AdfFacesAgent;
import org.apache.myfaces.trinidadinternal.renderkit.AdfRenderingContext;

public class HiddenLabelUtils
{
  /**
   * Identifies whether the current agent supports the hidden label
   * trick.
   */
  static public boolean supportsHiddenLabels(AdfRenderingContext arc)
  {
    if (XhtmlRenderer.isInaccessibleMode(arc))
      return false;

    AdfFacesAgent agent = arc.getAgent();
    switch (agent.getAgentApplication())
    {
      case AdfFacesAgent.APPLICATION_IEXPLORER:
        if (agent.getAgentOS() == AdfFacesAgent.OS_WINDOWS)
        {
          // IE 4 doesn't support the label hack.
          if (agent.getAgentMajorVersion() == 4)
            return false;

          // JDev VE masquerades as IE Windows, but doesn't support this
          if (agent.getCapability(AdfFacesAgent.CAP_IS_JDEV_VE) != null)
            return false;

          // IE 5 and 6 do.
          return true;
        }

        // IE on the Mac doesn't support the label hack
        return false;

      // Mozilla does support the label hack
      case AdfFacesAgent.APPLICATION_GECKO:
        return true;

      // Assume everyone else doesn't.
      case AdfFacesAgent.APPLICATION_NETSCAPE:
      default:
        return false;
    }
  }

  /**
   * Returns true if ADF Faces wants a hidden label for
   * a particular ID.
   */
  static public boolean wantsHiddenLabel(
    AdfRenderingContext arc,
    String              id)
  {
    if (id == null)
      return false;

    // Note this shortcut to figuring out if the field already has
    // a label - we assume that labels are close to their fields,
    // and that therefore the last label written out is what
    // counts.
    if (id.equals(arc.getProperties().get(_LAST_LABEL_KEY)))
      return false;

    return true;
  }

  /**
   * Outputs a hidden label. 
   */
  static public void outputHiddenLabelIfNeeded(
    FacesContext        context,
    AdfRenderingContext arc,
    String              id,
    Object              text,
    UIComponent         component
    ) throws IOException
  {
    if (supportsHiddenLabels(arc) && wantsHiddenLabel(arc, id))
    {
      outputHiddenLabel(context, arc, id, text, component);
    }
  }


  /**
   * Outputs a hidden label. 
   */
  static public void outputHiddenLabel(
    FacesContext        context,
    AdfRenderingContext arc,
    String              id,
    Object              text,
    UIComponent         component
    ) throws IOException
  {
    ResponseWriter writer = context.getResponseWriter();
    writer.startElement("label", component);
    writer.writeAttribute("for", id, null);
    XhtmlRenderer.renderStyleClass(context, arc,
                                   XhtmlConstants.HIDDEN_LABEL_STYLE_CLASS);
    writer.writeText(text, null);
    writer.endElement("label");
  }


  /**
   * Remembers that a "normal" hidden label has already been outputted.
   */
  static public void rememberLabel(
    AdfRenderingContext arc,
    Object              id)
  {
    if (id != null)
    {
      arc.getProperties().put(_LAST_LABEL_KEY, id.toString());
    }
  }


  // Key used for storing the "last" label ID
  private static final Object _LAST_LABEL_KEY = new Object();

  private HiddenLabelUtils()
  {
  }
}
