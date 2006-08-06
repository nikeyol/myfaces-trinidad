/*
 * Copyright  2003-2006 The Apache Software Foundation.
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
package org.apache.myfaces.trinidadinternal.renderkit.uix;

import java.util.Collections;
import java.util.Map;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.trinidad.component.UIXTree;

import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.table.TreeUtils;
import org.apache.myfaces.trinidadinternal.ui.UIConstants;
import org.apache.myfaces.trinidadinternal.uinode.UINodeRendererBase;

/**
 * Renderer for tree
 * <p>
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/renderkit/uix/TreeRenderer.java#0 $) $Date: 10-nov-2005.19:00:38 $
 * @author The Oracle ADF Faces Team
 */
public class TreeRenderer extends UINodeRendererBase
{
  /**
   * @todo do not mess with selection here. queue an event.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void decode(
    FacesContext context, 
    UIComponent component)
  {
    Map<String, String> parameters = 
      context.getExternalContext().getRequestParameterMap();
    
    String source = parameters.get(UIConstants.SOURCE_PARAM);

    if (!component.getClientId(context).equals(source))
      return;

    TreeUtils.decodeExpandEvents(parameters, component, Collections.EMPTY_LIST);
    String currencyStrParam = 
      source + NamingContainer.SEPARATOR_CHAR + SELECTED_PARAM;
    String currencyStr = parameters.get(currencyStrParam);
    if ((currencyStr != null) && (!"".equals(currencyStr)))
    {
      UIXTree tree = (UIXTree) component;
      Object oldPath = tree.getRowKey();
      tree.setCurrencyString(currencyStr);
      tree.getSelectedRowKeys().clear();
      tree.getSelectedRowKeys().add();
      tree.setRowKey(oldPath);
    }
  }  

  public static final String SELECTED_PARAM = "_selected";
}
