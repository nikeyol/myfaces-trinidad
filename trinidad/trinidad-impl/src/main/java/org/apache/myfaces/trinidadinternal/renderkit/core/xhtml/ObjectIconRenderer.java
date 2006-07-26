/*
 * Copyright  2005-2006 The Apache Software Foundation.
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

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.trinidad.bean.FacesBean;
import org.apache.myfaces.trinidad.bean.PropertyKey;
import org.apache.myfaces.trinidad.component.core.output.CoreObjectIcon;
import org.apache.myfaces.trinidad.util.ArrayMap;

import org.apache.myfaces.trinidadinternal.renderkit.AdfRenderingContext;
import org.apache.myfaces.trinidadinternal.skin.icon.Icon;

public class ObjectIconRenderer extends XhtmlRenderer
{
  public ObjectIconRenderer()
  {
    super(CoreObjectIcon.TYPE);
  }

  protected void findTypeConstants(FacesBean.Type type)
  {
    super.findTypeConstants(type);
    _nameKey = type.findKey("name");
  }

  protected void encodeBegin(
    FacesContext        context,
    AdfRenderingContext arc,
    UIComponent         comp,
    FacesBean           bean) throws IOException
  {
    String name = getName(bean);
    
    if ("logo".equals(name))
    {
      // special case for extra skinning of margins in the PanelPage branding
      // The span is written out here because the writer
      // needs to see the UIComponent.
      ResponseWriter rw = context.getResponseWriter();

      rw.startElement("img", comp);
      renderId(context, comp);
      renderAllAttributes(context, arc, bean, false);
      renderStyleAttributes(context, arc, bean, "AFLogo");
      rw.writeAttribute(
        "src",
        context.getExternalContext().getRequestContextPath() +
        "/adf/images/t.gif",
        null);
      rw.endElement("img");
    }
    else
    {
      Icon icon = arc.getIcon(name);
  
      if (icon == null)
      {
        StringBuffer fullName = new StringBuffer(_ICON_NAME_PREFIX.length() +
                                                 name.length() +
                                                 _ICON_NAME_SUFFIX.length());
        fullName.append(_ICON_NAME_PREFIX);
        char firstChar = Character.toUpperCase(name.charAt(0));
        fullName.append(firstChar);
        fullName.append(name.substring(1));
        fullName.append(_ICON_NAME_SUFFIX);
        icon = arc.getIcon(fullName.toString());
      }
  
      // If we've got an Icon, and it has content, render it
      if ((icon != null) && !icon.isNull())
      {
        // The span is written out here because the writer
        // needs to see the UIComponent.
        ResponseWriter rw = context.getResponseWriter();
  
        // If this icon renders as an image, don't embed it in a span
        boolean embed = icon.getImageURI(context, arc) == null;
        if (embed)
        {
          rw.startElement("span", comp);
          renderId(context, comp);
          renderAllAttributes(context, arc, bean);
        }
  
        icon.renderIcon(context, arc,
                        _getNodeAttributeMap(context, comp, bean, embed));
        if (embed)
          rw.endElement("span");
      }
    }
  }

  protected void renderShortDescAttribute(
    FacesContext        context,
    AdfRenderingContext arc,
    FacesBean           bean) throws IOException
  {
    // do nothing, handled by the icon renderer
  }

  protected String getName(FacesBean bean)
  {
    return toString(bean.getProperty(_nameKey));
  }

  private Map _getNodeAttributeMap(
    FacesContext        context,
    UIComponent         comp,
    FacesBean           bean,
    boolean             embed)
  {
    Map attrs = null;
    attrs = new ArrayMap(1);

    attrs.put(Icon.SHORT_DESC_KEY, getShortDesc(bean));

    if (embed)
      attrs.put(Icon.EMBEDDED_KEY, Boolean.TRUE);
    else
    {
      attrs.put(Icon.STYLE_CLASS_KEY, getStyleClass(bean));
      attrs.put(Icon.ID_KEY, getClientId(context, comp));
    }

    return attrs;
  }

  private PropertyKey _nameKey;

  private static String _ICON_NAME_PREFIX = "AF";
  private static String _ICON_NAME_SUFFIX = "Icon";
}
