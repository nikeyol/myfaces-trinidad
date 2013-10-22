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
package org.apache.myfaces.trinidadinternal.skin.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.myfaces.trinidad.logging.TrinidadLogger;
import org.apache.myfaces.trinidad.skin.Skin;
import org.apache.myfaces.trinidad.skin.SkinMetadata;
import org.apache.myfaces.trinidad.skin.SkinProvider;
import org.apache.myfaces.trinidad.skin.SkinVersion;
import org.apache.myfaces.trinidad.util.ClassLoaderUtils;
import org.apache.myfaces.trinidadinternal.renderkit.core.xhtml.TrinidadRenderingConstants;
import org.apache.myfaces.trinidadinternal.skin.SkinUtils;

/**
 * Internal implementation of SkinProvider which is exposed using SkinProvider.getCurrentInstance()
 * This class collates all SkinProvider SPIs, Trinidad SkinProviders
 * and SkinFactory to return the best match for a Skin requested
 * This class reads all registered SPIs using org.apache.myfaces.trinidad.skin.SkinProvider
 *<p/>
 * Instance of this class is created and put into the ExternalContext during
 * bootstrap in GlobalConfiguratorImpl and retrieved in the static factory inside SkinProvider
 * <p/>
 */
public class SkinProviderRegistry extends SkinProvider
{

  public SkinProviderRegistry()
  {
    List<SkinProvider> services = ClassLoaderUtils.getServices(SkinProvider.class.getName());

    if (_LOG.isFine())
      _LOG.fine("Adding providers from registered SPIs... " + services.size());

    List<SkinProvider> providers = new ArrayList<SkinProvider>(services.size() + 2);
    providers.addAll(services);

    // add internal skin providers at the end
    // this is to give the other SPIs a chance before the internal skin providers
    if (_LOG.isFine())
      _LOG.fine("Adding TrinidadSkinProvider... ");

    providers.add(new TrinidadSkinProvider());

    if (_LOG.isFine())
      _LOG.fine("Adding TrinidadBaseSkinProvider... ");

    providers.add(new TrinidadBaseSkinProvider());

    if (_LOG.isFine())
      _LOG.fine("Adding ExternalSkinProvider... ");

    providers.add(SkinUtils.getExternalSkinProvider(null));

    _providers = Collections.unmodifiableList(providers);
  }

  @Override
  public Collection<SkinMetadata> getSkinMetadata(FacesContext context)
  {
    Collection<SkinMetadata> metadata = new ArrayList<SkinMetadata>();

    for (SkinProvider provider : _providers)
      metadata.addAll(provider.getSkinMetadata(context));

    return Collections.unmodifiableCollection(metadata);
  }

  /**
   * @param context
   * @param skinMetadata   search criteria object containing the information of skin to be queried
   *                       id, family, renderKit, version are the information used from skin metadata
   *                       to perform search for the skin requested.
   *                       Other fields do not participate in the search.
   * @return matching skin for the search criteria passed,
   *         if none found, return the simple skin for the renderKit in search criteria
   *         if renderKit not specified in search criteria, skinMetadata automatically assumes it as
   *         SkinMetadata.RenderKitId.DESKTOP
   */
  @Override
  public Skin getSkin(FacesContext context, SkinMetadata skinMetadata)
  {
    _handleCircularDependency(context, skinMetadata);

    List<Skin> matchingSkins = new ArrayList<Skin>();
    Skin matchingSkin = null;

    if (_LOG.isFine())
      _LOG.fine("Skin request for metadata: " + skinMetadata);

    for (SkinProvider provider : _providers)
    {
      matchingSkin = provider.getSkin(context, skinMetadata);

      if (matchingSkin != null)
      {
        // log which provider got skin etc
        if (_LOG.isFine())
          _LOG.fine("Skin obtained for metadata: " + skinMetadata + " from provider: " + provider);

        matchingSkins.add(matchingSkin);
      }
    }

    if (_LOG.isFine())
      _LOG.fine("Matches obtained for " + skinMetadata + ": " + matchingSkins.size());

    // now we are done with asking all skin providers
    // ensure that the matching skins returned are indeed matching the search criteria
    // onus is on SkinProviderRegistry to serve the skin requested, even if a SkinProvider
    // returns a wrong skin.

    // filter the results obtained from skin providers based on id, family and renderKit
    // filtering on version is more complicated and taken up subsequently
    matchingSkins = _filterSkins(matchingSkins, skinMetadata);

    if (matchingSkins.isEmpty())
    {
      if (_LOG.isFine())
        _LOG.fine("NO MATCH. Will return a simple skin or null for skin metadata: " + skinMetadata);

      assert  (skinMetadata.getRenderKitId() != null);
      // if renderKit is available return the simple skin for that renderKit
      // skinMetadata.getRenderKitId() can never be null, default renderKit will always be DESKTOP
      return _returnSkin(context, skinMetadata, _getSimpleSkinForRenderKit(context, skinMetadata.getRenderKitId()));
    }


    // we have at least one match now
    // if we have only one match
    // return that skin
    if (matchingSkins.size() == 1)
    {
      if (_LOG.isFine())
        _LOG.fine("returning ONLY match for skin metadata: " + skinMetadata);

      return _returnSkin(context, skinMetadata, matchingSkins.get(0));
    }

    // at this point we have more than one matches for the search criteria passed
    // so, extract the best match based on version
    return _versionFilter(context, skinMetadata, matchingSkins);
  }

  /**
   * ensure sanity in the list of matches received by filtering the skins based on id, family and renderKit
   * @param skins
   * @param metadata
   * @return
   */
  private List<Skin> _filterSkins(List<Skin> skins, SkinMetadata metadata)
  {
    if (skins == null || skins.isEmpty() || metadata == null)
      return Collections.emptyList();

    List<Skin> filterList = _idFilter(skins, metadata.getId());

    // if the id based filtering resulted in only match then return that
    // provided id was mentioned in search criteria
    if (filterList.size() == 1 && metadata.getId() != null)
    {
      return filterList;
    }

    // if more than one match for id or if id is not specified, then filter by family
    filterList = _familyFilter(filterList, metadata.getFamily());

    // if the family based filtering resulted in only match then return that
    // provided family was mentioned in search criteria
    if (filterList.size() == 1 && metadata.getFamily() != null)
    {
      return filterList;
    }

    // if family based filtering resulted in multiple matches, or if family and id are not specified,
    // proceed with renderKit based filtering.
    filterList = _renderKitFilter(filterList, metadata.getRenderKitId());

    return filterList;
  }

  /**
   * filter based on skin id, if mentioned. Otherwise return the list as is
   * @param skins
   * @param id
   * @return
   */
  private List<Skin> _idFilter(List<Skin> skins, String id)
  {
    if (id == null || id.isEmpty())
      return skins;

    List<Skin> filterList = new ArrayList<Skin>(skins.size());

    for (Skin skin : skins)
      if (id.equals(skin.getId()))
        filterList.add(skin);

    return filterList;
  }

  /**
   * filter based on skin family, if mentioned. Otherwise return the list as is
   * @param skins
   * @param family
   * @return
   */
  private List<Skin> _familyFilter(List<Skin> skins, String family)
  {
    if (family == null || family.isEmpty())
      return skins;

    List<Skin> filterList = new ArrayList<Skin>(skins.size());

    for (Skin skin : skins)
      if (family.equalsIgnoreCase(skin.getFamily()))
        filterList.add(skin);

    return filterList;
  }

  /**
   * filter based on skin renderkit id, if mentioned. Otherwise return the list as is
   * @param skins
   * @param renderKitId
   * @return
   */
  private List<Skin> _renderKitFilter(List<Skin> skins, String renderKitId)
  {
    if (renderKitId == null || renderKitId.isEmpty())
      return skins;

    List<Skin> filterList = new ArrayList<Skin>(skins.size());

    for (Skin skin : skins)
      if (renderKitId.equalsIgnoreCase(skin.getRenderKitId()))
        filterList.add(skin);

    return filterList;
  }

  /**
   * filter based on version
   * if version is mentioned try to return exact match
   * else if version is not mentioned or version is default try to return default skin
   * else try to return leaf skin
   * else return first match
   * @param context
   * @param searchCriteria
   * @param matchingSkins
   * @return
   */
  private Skin _versionFilter(FacesContext context, SkinMetadata searchCriteria, List<Skin> matchingSkins)
  {
    SkinVersion version = searchCriteria.getVersion();
    Skin matchingSkin;
    // we can now find the best match based on version
    // now that there is a version mentioned and skins matching is > 1
    // look for exact name match
    matchingSkin = _findSkinForVersionName(matchingSkins, version);

    // if we found exact match in version, return it.
    if (matchingSkin != null)
    {
      if (_LOG.isFine())
        _LOG.fine("returning exact version match.");

      return _returnSkin(context, searchCriteria, matchingSkin);
    }

    // either user is looking for default version or did not find the version requested
    // try to find a default skin
    matchingSkin = _findSkinWithDefaultVersion(matchingSkins);

    // if we found the default version skin, return it.
    if (matchingSkin != null)
    {
      if (_LOG.isFine())
        _LOG.fine("returning DEFAULT version match.");
      return _returnSkin(context, searchCriteria, matchingSkin);
    }

    // the version that user asked for is not available
    // so find leaf skin
    matchingSkin = _findLeafSkin(matchingSkins);

    // if we found a leaf skin return that
    if (matchingSkin != null)
    {
      if (_LOG.isFine())
        _LOG.fine("return LEAF skin or one of the matches.");

      return _returnSkin(context, searchCriteria, matchingSkin);
    }

    // we failed to find any better result for the given version, so return first match
    if (_LOG.isFine())
      _LOG.fine("nothing worked so return first match.");

    return _returnSkin(context, searchCriteria, matchingSkins.get(0));
  }

  private Skin _returnSkin(FacesContext context, SkinMetadata skinMetadata, Skin skin)
  {
    // nothing to do if context is not available
    if (context == null)
      return skin;

    Object o = context.getAttributes().get(_SKIN_PROVIDER_CONTEXT);
    List<SkinMetadata> requesters = null;

    if (o == null)
    {
      requesters = new ArrayList<SkinMetadata>();
      context.getAttributes().put(_SKIN_PROVIDER_CONTEXT, requesters);
    }
    else
      requesters = (List) o;

    // remove the skinMetadata
    requesters.remove(skinMetadata);

    if (_LOG.isFiner())
    {
      _LOG.finer("Removing " + skinMetadata + " from context");
      _LOG.finer("Context now is " + requesters);
    }

    return skin;
  }

  /**
   * find a skin with version passed
   * @param skins
   * @param version
   * @return
   */
  private Skin _findSkinForVersionName(Collection<Skin> skins, SkinVersion version)
  {
    if (version == null)
      throw new IllegalArgumentException("skin version cannot be null");

    if (version.getName() == null || version.getName().isEmpty())
      return null;

    for (Skin skin : skins)
    {
      // metadata cannot be null and also version inside it cannot be null
      if (version.getName().equals(skin.getVersion().getName()))
      {
        if (_LOG.isFine())
          _LOG.fine("Found version match skin: " + skin);

        return skin;
      }
    }

    return null;
  }

  /**
   * Latest skin is the one which is last in the family hierarchy
   * eg: fusion-v1 -> fusion-v2 -> fusion-v3
   * Among this fusion-v3 is the latest. So we look for a skin that is
   * not extended by any other skin in the family.
   * @param skins
   * @return
   */
  private Skin _findLeafSkin(Collection<Skin> skins)
  {
    List<Skin> leafSkins = new ArrayList<Skin>();
    List<String> parentIds = new ArrayList<String>();

    // collect parents skins among the list
    for (Skin metadata : skins)
    {
      Skin baseSkin = metadata.getBaseSkin();
      if (baseSkin != null)
      {
        String parentId = baseSkin.getId();
        if (parentId != null)
          parentIds.add(parentId);
      }
    }

    // find leaf skins, which is not in parent list
    for (Skin skin : skins)
    {
      String skinId = skin.getId();
      if (skinId != null && !parentIds.contains(skinId))
      {
        leafSkins.add(skin);
      }
    }

    // if there are no leaves, return null
    // this is a rare case, almost impossible since there will be
    // at least one skin which is not parent of another
    // but let us cover the corner case if any
    if (leafSkins.isEmpty())
      return null;

    // if there is one leaf skin return that
    // if there are many, return the last one among the leaves
    return leafSkins.get(leafSkins.size() - 1);
  }

  /**
   * find a skin that has its SkinVersion set to 'default', if it exists.
   * @param matchingSkinList A list of Skins that we will look through to find the 'default'.
   * @return Skin with SkinVersion isDefault true, otherwise, null.
   */
  private Skin _findSkinWithDefaultVersion(Collection<Skin> matchingSkinList)
  {
    for (Skin skin : matchingSkinList)
    {
      SkinVersion skinVersion = skin.getVersion();

      if (skinVersion != null && skinVersion.isDefault())
      {
        if (_LOG.isFine())
          _LOG.fine("Found default skin: " + skin);

        return skin;
      }
    }

    return null;
  }

  private void _handleCircularDependency(FacesContext context, SkinMetadata skinMetadata)
  {
    // nothing to do if context is not available
    if (context == null)
      return;

    Object o = context.getAttributes().get(_SKIN_PROVIDER_CONTEXT);
    List<SkinMetadata> requesters = null;

    if (o == null)
    {
      requesters = new ArrayList<SkinMetadata>();
      context.getAttributes().put(_SKIN_PROVIDER_CONTEXT, requesters);
    }
    else
      requesters = (List) o;

    if (requesters.contains(skinMetadata))
    {
      String message = "Circlular dependency detected whille loading skin: " + skinMetadata
                       + ". Requesters are: " + requesters;
      if (_LOG.isSevere())
        _LOG.severe(message);

      throw new IllegalStateException(message);
    }

    // add the currently requested metadata into list
    requesters.add(skinMetadata);

    if (_LOG.isFiner())
    {
      _LOG.finer("Adding " + skinMetadata + " to context");
      _LOG.finer("Context now is " + requesters);
    }
  }

  private Skin _getSimpleSkinForRenderKit(FacesContext context, String renderKitId)
  {
    if (renderKitId != null && renderKitId.equals(TrinidadRenderingConstants.APACHE_TRINIDAD_PORTLET))
      return getSkin(context, new SkinMetadata.Builder().id(TrinidadRenderingConstants.SIMPLE_PORTLET_ID).build());

    if (renderKitId != null && renderKitId.equals(TrinidadRenderingConstants.APACHE_TRINIDAD_PDA))
      return getSkin(context, new SkinMetadata.Builder().id(TrinidadRenderingConstants.SIMPLE_PDA_ID).build());

    return getSkin(context, new SkinMetadata.Builder().id(TrinidadRenderingConstants.SIMPLE_DESKTOP_ID).build());
  }

  private final List<SkinProvider> _providers;

  private static final String _SKIN_PROVIDER_CONTEXT =
    "org.apache.myfaces.trinidadinternal.skin.provider.SkinProviderRegistry.Context";
  private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(SkinProviderRegistry.class);
}