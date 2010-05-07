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
package org.apache.myfaces.trinidaddemo.components.output.legend;

import org.apache.myfaces.trinidaddemo.support.impl.AbstractComponentDemo;
import org.apache.myfaces.trinidaddemo.support.impl.ComponentVariantDemoImpl;
import org.apache.myfaces.trinidaddemo.support.ComponentDemoId;
import org.apache.myfaces.trinidaddemo.support.IComponentDemoVariantId;

/**
 *
 */
public class LegendDemo extends AbstractComponentDemo {

    private static final long serialVersionUID = -1982371956886498710L;

    private enum VARIANTS implements IComponentDemoVariantId {
        Default
    }

	/**
	 * Constructor.
	 */
	public LegendDemo() {
		super(ComponentDemoId.legend, "Legend");

        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.Default, "Default", this,
            new String[]{
                    "/components/output/legend/legend.xhtml"
            }));

        setDefaultVariant(VARIANTS.Default);
	}

    public String getSummaryResourcePath() {
        return "/components/output/legend/summary.xhtml";
    }

    public String getSkinDocumentationLink(){
        return null;
    }    
}
