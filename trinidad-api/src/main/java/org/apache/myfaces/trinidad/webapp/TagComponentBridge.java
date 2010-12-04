/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.myfaces.trinidad.webapp;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

import javax.servlet.jsp.PageContext;


/**
 * Internal class to handle communication between iterating tags
 * ({@link TrinidadIterationTag}) and component tags
 * ({@link UIXComponentELTag}).
 */
final class TagComponentBridge
{
  /**
   * Private as this class is implemented as a singleton.
   * @see #getInstance(PageContext)
   */
  private TagComponentBridge() {}

  /**
   * Get (or create) an instance from the page context.
   *
   * @param pageContext The JSP page context
   * @return the existing, or new, instance of this class
   */
  final static TagComponentBridge getInstance(PageContext pageContext)
  {
    TagComponentBridge bridge =
      (TagComponentBridge) pageContext.getAttribute(_PAGE_CONTEXT_KEY,
        PageContext.REQUEST_SCOPE);

    if (bridge == null)
    {
      bridge = new TagComponentBridge();
      pageContext.setAttribute(_PAGE_CONTEXT_KEY, bridge,
          PageContext.REQUEST_SCOPE);
    }

    return bridge;
  }

  /**
   * Call the {@link TrinidadIterationTag#childComponentProcessed(UIComponent)}
   * method on all registered tags.
   *
   * @param component The subject component.
   */
  void notifyComponentProcessed(
    UIComponent component)
  {
    for (TrinidadIterationTag tag : _tags)
    {
      tag.childComponentProcessed(component);
    }
  }

  /**
   * Call the {@link TrinidadIterationTag#afterChildComponentProcessed(UIComponent)}
   * method on all registered tags.
   *
   * @param component The subject component.
   */
  void notifyAfterComponentProcessed(
    UIComponent component)
  {
    for (TrinidadIterationTag tag : _tags)
    {
      tag.afterChildComponentProcessed(component);
    }
  }

  /**
   * Register a tag for callbacks.
   * @param tag the tag to register
   */
  void registerTag(TrinidadIterationTag tag)
  {
    _tags.add(0, tag);
  }

  /**
   * Unregister a tag for callbacks.
   * @param tag the tag to unregister
   */
  void unregisterTag(TrinidadIterationTag tag)
  {
    _tags.remove(tag);
  }

  private List<TrinidadIterationTag> _tags =
    new ArrayList<TrinidadIterationTag>();

  private final static String _PAGE_CONTEXT_KEY =
    TagComponentBridge.class.getName() + ".PAGE_CONTEXT";
}