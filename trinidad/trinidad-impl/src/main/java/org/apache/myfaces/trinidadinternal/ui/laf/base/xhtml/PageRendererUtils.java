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
package org.apache.myfaces.trinidadinternal.ui.laf.base.xhtml;

import org.apache.myfaces.trinidad.component.UIXHierarchy;

import org.apache.myfaces.trinidadinternal.ui.RenderingContext;
import org.apache.myfaces.trinidadinternal.ui.UIConstants;


/**
 * @version $Name:  $ ($Revision: adfrt/faces/adf-faces-impl/src/main/java/oracle/adfinternal/view/faces/ui/laf/base/xhtml/PageRendererUtils.java#0 $) $Date: 10-nov-2005.18:54:08 $
 * @author The Oracle ADF Faces Team
 */
public class PageRendererUtils 
{
  
  public static final Object getFocusPath(
    RenderingContext context
  )
  {
    return context.getProperty(UIConstants.MARLIN_NAMESPACE, 
                                      _FOCUS_PATH_KEY);
  }  
  
  static final void setFocusPath(
    RenderingContext context,
    Object             focusPath
  )
  {
    context.setProperty(UIConstants.MARLIN_NAMESPACE, 
                        _FOCUS_PATH_KEY, 
                        focusPath);
  }

  /**
   * 
   * @param component 
   * @param focusPath 
   * @param startDepth 
   * @return whether or not a path was set.
   */
  public static final boolean setNewPath(
    RenderingContext context, 
    UIXHierarchy    component,
    int              startDepth
  )
  {
    Object focusPath = getFocusPath(context);
    return ModelRendererUtils.setNewPath(component, startDepth, focusPath);
  }  

  /**
   * Checks to see whether the globalHeader is empty (contains no
   * indexed children).
   */
  public static final boolean isEmpty(
    RenderingContext context,
    UIXHierarchy    component,
    int              startDepth
    )
  {                   
    Object focusPath = getFocusPath(context);
    return ModelRendererUtils.isEmpty(component, startDepth, focusPath);
  }  
  private static Object _FOCUS_PATH_KEY = new Object();
}
