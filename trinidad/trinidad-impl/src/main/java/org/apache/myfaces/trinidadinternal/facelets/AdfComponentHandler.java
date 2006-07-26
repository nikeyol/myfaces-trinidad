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
package org.apache.myfaces.trinidadinternal.facelets;

import com.sun.facelets.FaceletContext;
import com.sun.facelets.FaceletViewHandler;
import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;

import com.sun.facelets.tag.MetaRuleset;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.myfaces.trinidad.component.UIXComponent;

/**
 * TagLibrary implementation for Oracle ADF core library.
 * @author Adam Winer
 * @version
 */
public class AdfComponentHandler extends ComponentHandler
{
  public AdfComponentHandler(ComponentConfig config) 
  {
    super(config);
    if (_markInitialState == null)
    {
      // Can't imagine why this wouldn't always run during
      // a Faces request...
      FacesContext context = FacesContext.getCurrentInstance();
      if (context != null)
      {
        ExternalContext external = context.getExternalContext();
        String restoreMode = external.getInitParameter(
               FaceletViewHandler.PARAM_BUILD_BEFORE_RESTORE);
        if ("true".equals(restoreMode))
          _markInitialState = Boolean.TRUE;
        else 
          _markInitialState = Boolean.FALSE;
      }
    }
  }

  protected MetaRuleset createMetaRuleset(Class type)
  {
    MetaRuleset m = super.createMetaRuleset(type);
    
    m.addRule(StringArrayPropertyTagRule.Instance);
    m.addRule(AdfListenersTagRule.Instance);
           
    return m;
  }

  protected void onComponentPopulated(FaceletContext context,
                                     UIComponent component,
                                     UIComponent parent)
  {
    assert (_markInitialState != null);

    if ((component instanceof UIXComponent) &&
        (_markInitialState == Boolean.TRUE))
    {
      if (component.getId() == null)
        component.setId(context.generateUniqueId(UIViewRoot.UNIQUE_ID_PREFIX));

      ((UIXComponent) component).markInitialState();
    }
  }

  static private Boolean _markInitialState;
}
