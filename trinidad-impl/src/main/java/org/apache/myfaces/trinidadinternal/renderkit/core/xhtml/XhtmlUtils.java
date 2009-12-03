/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.myfaces.trinidadinternal.renderkit.core.xhtml;

import java.io.IOException;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.apache.myfaces.trinidad.component.UIXComponentRef;
import org.apache.myfaces.trinidad.component.UIXIterator;
import org.apache.myfaces.trinidad.component.UIXSubform;
import org.apache.myfaces.trinidad.component.UIXSwitcher;
import org.apache.myfaces.trinidad.context.Agent;
import org.apache.myfaces.trinidad.context.RenderingContext;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.render.RenderUtils;
import org.apache.myfaces.trinidadinternal.agent.TrinidadAgent;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.jsLibs.Scriptlet;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.jsLibs.XhtmlScriptletFactory;
import org.apache.myfaces.trinidadinternal.share.util.FastMessageFormat;


/**
 * XHTML rendering utilities.
 */
public class XhtmlUtils
{
  /**
   * Skip over pure iteration components to find a "structural" parent.
   * This code is not guaranteed to work, but will work well enough.
   * @return a structural parent, or null if none exists
   */
  static public UIComponent getStructuralParent(UIComponent component)
  {
    while (true)
    {
      component = component.getParent();
      if (component == null)
        return null;

      if (_NON_STRUCTURAL_COMPONENT_FAMILIES.contains(component.getFamily()))
        continue;

      return component;
    }
  }


  /**
   * Returns true if the agent has enough support for Trinidad
   * to launch separate windows.  We require both multiple window
   * support and PPR support.
   */
  static public final boolean supportsSeparateWindow(
    Agent agent)
  {
    return (Boolean.TRUE.equals(
             agent.getCapabilities().get(TrinidadAgent.CAP_MULTIPLE_WINDOWS)) &&
            Boolean.TRUE.equals(
             agent.getCapabilities().get(TrinidadAgent.CAP_PARTIAL_RENDERING)));
  }

  /** Library key for the locale lib */
  public static final String CORE_LIB =
    XhtmlScriptletFactory.CORE_LIB;

  /**
   * Returns a composite ID based on a base and a suffix.
   */
  public static String getCompositeId(String baseid, String suffix)
  {
    int length = baseid.length() +
                 XhtmlConstants.COMPOSITE_ID_EXTENSION.length();
    if (suffix != null)
      length += suffix.length();
    StringBuilder compID
      = new StringBuilder(length);
    compID.append(baseid);
    compID.append(XhtmlConstants.COMPOSITE_ID_EXTENSION);
    if (suffix != null)
      compID.append(suffix);
    return compID.toString();
  }


  /**
   * Registers a scriptlet.
   */
  public static  void registerScriptlet(Object key, Scriptlet scriptlet)
  {
    _sScriptletTable.put(key, scriptlet);
  }

  /**
   */
  static public void addLib(
    FacesContext        context,
    RenderingContext arc,
    Object              libKey) throws IOException
  {
    if ((XhtmlRenderer.supportsScripting(arc)) && (libKey != null))
    {
      Scriptlet scriptlet = _sScriptletTable.get(libKey);
      if (scriptlet == null)
      {
        if (_LOG.isWarning())
          _LOG.warning("CANNOT_FIND_SCRIPTLET", libKey);
      }
      else
      {
        scriptlet.outputScriptlet(context, arc);
      }
    }
  }


  /**
   * Write out a script element importing a library.
   * The given URL will only be written once to the page.
   */
  public static void writeLibImport(
    FacesContext     context,
    RenderingContext rc,
    Object           libURL) throws IOException
  {
    // check if it's already been written
    Map<Object, Object> props = rc.getProperties();
    if (props.containsKey(libURL)) { return; }

    // put the lib name in the property map so it won't be written out again
    props.put(libURL, Boolean.TRUE);

    ResponseWriter writer = context.getResponseWriter();

    writer.startElement("script", null);
    XhtmlRenderer.renderScriptDeferAttribute(context, rc);
    // Bug #3426092:
    // render the type="text/javascript" attribute in accessibility mode
    XhtmlRenderer.renderScriptTypeAttribute(context, rc);

    // For portlets, we want to make sure that we only import
    // each script once.  Employ document.write() to achieve this
    // effect.  (Note that on Netscape 4.x this caused major
    // problems when resizing windows - but we're done with Netscape 4)
    libURL = context.getExternalContext().encodeResourceURL(libURL.toString());

    if (XhtmlConstants.FACET_PORTLET.equals(rc.getOutputMode()))
    {
      if (rc.getProperties().get(_PORTLET_LIB_TABLE_KEY) == null)
      {
        rc.getProperties().put(_PORTLET_LIB_TABLE_KEY, Boolean.TRUE);
        writer.writeText("var _uixJSL;" +
                         "if(!_uixJSL)_uixJSL={};" +
                         "function _addJSL(u)" +
                         "{" +
                           "if(!_uixJSL[u])" +
                           "{" +
                             "_uixJSL[u]=1;" +
                             "document.write(\"<scrip\"+" +
                                            "\"t src=\\\"\"+u+" +
                                            "\"\\\"></scrip\"+" +
                                            "\"t>\")" +
                           "}" +
                         "}",
             null);
      }
      writer.writeText("_addJSL(\"", null);
      writer.writeText(libURL, null);
      writer.writeText("\")", null);
    }
    else
    {
      // The "safe" case: just write out the source
      writer.writeURIAttribute("src", libURL, null);
    }

    writer.endElement("script");
  }

  /**
   * Return the chained JavaScript
   * @deprecated use method in RenderUtils instead
   */
  @Deprecated
  public static String getChainedJS(
    String evh1,
    String evh2,
    boolean shortCircuit
    )
  {
    return RenderUtils.getChainedJS(shortCircuit, evh1, evh2);
  }

  /**
   * Handle escaping '/', and single quotes, plus escaping text inside of
   * quotes with just a String for input.  If a String in and a String out is
   * all that is required, this version is more efficient if the String
   * does not need to be escaped.
   * @deprecated use method in RenderUtils instead
   */
  @Deprecated
  public static String escapeJS(
    String inString
    )
  {
    return RenderUtils.escapeJS(inString);
  }

  /**
   * Handle escaping '/', and single quotes, plus escaping text inside of
   * quotes with just a String for input.  If a String in and a String out is
   * all that is required, this version is more efficient if the String
   * does not need to be escaped.
   * @deprecated use method in RenderUtils instead
   */
  @Deprecated
  public static String escapeJS(
    String  inString,
    boolean inQuotes
    )
  {
    return RenderUtils.escapeJS(inString, inQuotes);
  }

  /**
   * Handle escaping '/', and single quotes, plus escaping text inside of
   * quotes.
   * @deprecated use method in RenderUtils instead
   */
  @Deprecated
  public static void escapeJS(
    StringBuilder outBuilder,
    String       inString
    )
  {
    RenderUtils.escapeJS(outBuilder, inString);
  }

  /**
   * Handle escaping '/', and single quotes, plus escaping text inside of
   * quotes.
   * @deprecated use method in RenderUtils instead
   */
  @Deprecated
  public static void escapeJS(
    StringBuilder outBuilder,
    String       inString,
    boolean      inQuotes)
  {
    RenderUtils.escapeJS(outBuilder, inString, inQuotes);
  }

  /**
   * Handle escaping '/', and single quotes, plus escaping text inside of
   * quotes.
   * @deprecated use method in RenderUtils instead
   */
  @Deprecated
  public static void escapeJS(
    StringBuilder outBuilder,
    String       inString,
    boolean      inQuotes,
    int          escapeCount
    )
  {
    RenderUtils.escapeJS(outBuilder, inString, inQuotes, escapeCount);
  }

  public static String getJSIdentifier(String clientId)
  {
    if (clientId == null)
      return null;

    // Bug 3931544:  don't use colons in Javascript variable names.
    // We'll just replace colons with underscores;  not perfect, but adequate
    return clientId.replace(':','_');
  }

  public static String getFormattedString(String pattern, String[] parameters)
  {
    FastMessageFormat formatter = new FastMessageFormat(pattern);
    return formatter.format(parameters);
  }

  /*
   * This method returns the encoded parameter name or paramater value
   * for the Non-JavaScript browsers
   */
  public static String getEncodedParameter(String param)
  {
    return param + XhtmlConstants.NO_JS_PARAMETER_KEY;
  }

  /*
   * This method returns the name attribute of HTML elements for Non-JavaScript
   * browsers. It is encoded with parameter name and value pair.
   */
  public static String getEncodedNameAttribute(String param[])
  {
    // The incoming array(param[]) must contain parameter name and value pair
    // in the order of <<name1>>, <<value1>>, <<name2>>, <<value2>>,...
    // The encoded parameter name and value for the above would be
    // <<name1>><<encodingKey>><<value1>><<encodingKey>>
    // <<name2>><<encodingKey>><<value2>>

    int noOfParam = param.length;
    int bufferLen = 0;

    // Calculate what would be the length of the encoded param name and
    // value pair. We need it to initialize the buffer size of StringBuilder.
    for(int i = 0; i < noOfParam; i++)
    {
      bufferLen += param[i].length();
    }
    // If there are N parameter names and values, there would be N-1
    // encoding key so add its length also
    bufferLen  += (noOfParam -1) * XhtmlConstants.NO_JS_PARAMETER_KEY.length();

    StringBuilder nameAttri = new StringBuilder(bufferLen);

    //Encode all the parameter names and values except the last parameter value
    for(int i = 0; i < noOfParam-1; i++)
    {
      nameAttri.append(getEncodedParameter(param[i]));
    }

    nameAttri.append(param[noOfParam-1]);

    return(nameAttri.toString());
  }


  /** HashMap mapping names to their scriptlets */
  private static Map<Object, Scriptlet> _sScriptletTable =
    Collections.synchronizedMap(new HashMap<Object, Scriptlet>(37));

  // Key for storing whether we've written out the script
  // for storing loaded libraries
  static private final Object _PORTLET_LIB_TABLE_KEY = new Object();
  static private final Set<String> _NON_STRUCTURAL_COMPONENT_FAMILIES;

  static
  {
    _NON_STRUCTURAL_COMPONENT_FAMILIES = new HashSet<String>();
    _NON_STRUCTURAL_COMPONENT_FAMILIES.add(UIXIterator.COMPONENT_FAMILY);
    _NON_STRUCTURAL_COMPONENT_FAMILIES.add(UIXComponentRef.COMPONENT_FAMILY);
    _NON_STRUCTURAL_COMPONENT_FAMILIES.add(UIXSubform.COMPONENT_FAMILY);
    _NON_STRUCTURAL_COMPONENT_FAMILIES.add(UIXSwitcher.COMPONENT_FAMILY);
  }

  static
  {
    XhtmlScriptletFactory.registerAllScriptlets();
  }

  private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(XhtmlUtils.class);

}
