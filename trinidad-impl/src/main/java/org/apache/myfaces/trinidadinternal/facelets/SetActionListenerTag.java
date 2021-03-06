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
package org.apache.myfaces.trinidadinternal.facelets;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;

import javax.faces.view.facelets.ComponentHandler;

import org.apache.myfaces.trinidad.event.SetActionListener;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.FaceletException;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

/**
 */
public class SetActionListenerTag extends TagHandler
{
  
  public SetActionListenerTag(TagConfig tagConfig)
  {
    super(tagConfig);
    _from = getRequiredAttribute("from");
    _to   = getRequiredAttribute("to");
  }
  
  public void apply(FaceletContext faceletContext,
                    UIComponent parent) throws FaceletException, ELException
  {
    if (ComponentHandler.isNew(parent))
    {
      // =-=AEW Couldn't this be cached?
      ValueExpression fromExpression = _from.getValueExpression(faceletContext,
                                                                Object.class);
      ValueExpression toExpression=  _to.getValueExpression(faceletContext,
                                                            Object.class);
      ActionSource actionSource= (ActionSource) parent;
      SetActionListener listener = new SetActionListener();
      listener.setValueExpression("from", fromExpression);
      listener.setValueExpression("to", toExpression);
      actionSource.addActionListener(listener);
    }
  }

  private final TagAttribute _from;
  private final TagAttribute _to;
}

