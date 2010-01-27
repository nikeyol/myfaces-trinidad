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
package org.apache.myfaces.trinidaddemo.components.input.inputColor;

import org.apache.myfaces.trinidaddemo.support.ComponentDemoId;
import org.apache.myfaces.trinidaddemo.support.IComponentDemoVariantId;
import org.apache.myfaces.trinidaddemo.support.impl.AbstractComponentDemo;
import org.apache.myfaces.trinidaddemo.support.impl.ComponentVariantDemoImpl;

/**
 *
 */
public class InputColorDemo extends AbstractComponentDemo {
    
    private static final long serialVersionUID = -1982061956883498710L;

    private enum VARIANTS implements IComponentDemoVariantId {
		Compact,
		Detailed,
        Simple
	}

	/**
	 * Constructor.
	 */
	public InputColorDemo() {
		super(ComponentDemoId.inputColor, "Input Color");

        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.Compact, this,
                "/components/input/inputColor/inputColorCompact.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.Detailed, this,
                "/components/input/inputColor/inputColorDetailed.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.Simple, this,
                "/components/input/inputColor/inputColorSimple.xhtml", getSummaryResourcePath()));
	}

	public String getJsfResourcePath() {
		return "/components/input/inputColor/inputColor.xhtml";
	}

    public String getSummaryResourcePath() {
        return "/components/input/inputColor/summary.xhtml";
    }
}
