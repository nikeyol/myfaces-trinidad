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
package org.apache.myfaces.trinidaddemo.components.navigation.train;

import org.apache.myfaces.trinidaddemo.support.impl.AbstractComponentDemo;
import org.apache.myfaces.trinidaddemo.support.impl.ComponentVariantDemoImpl;
import org.apache.myfaces.trinidaddemo.support.ComponentDemoId;
import org.apache.myfaces.trinidaddemo.support.IComponentDemoVariantId;

/**
 *
 */
public class TrainDemo extends AbstractComponentDemo {

    private static final long serialVersionUID = -1982060956387098910L;

    static enum VARIANTS implements IComponentDemoVariantId {
		PlusOne,
        MaxVisited
	}

	/**
	 * Constructor.
	 */
	public TrainDemo() {
		super(ComponentDemoId.train, "Train",
            new String[]{
                "/components/navigation/train/generalInfo.xhtml",
                "/components/navigation/train/companyInfo.xhtml",
                "/components/navigation/train/jsfSurvey.xhtml",
                "/components/navigation/train/trinidadSurvey.xhtml",
                "/components/navigation/train/youAreDone.xhtml"
            });
        
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.PlusOne, "Plus One", this,
                new String[]{
                    "/components/navigation/train/generalInfo.xhtml",
                    "/components/navigation/train/companyInfo.xhtml",
                    "/components/navigation/train/jsfSurvey.xhtml",
                    "/components/navigation/train/trinidadSurvey.xhtml",
                    "/components/navigation/train/youAreDone.xhtml"
                }, getSummaryResourcePath(), getBackingBeanResourcePath()));

        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.MaxVisited, "Max Visited", this,
                new String[]{
                    "/components/navigation/train/generalInfo.xhtml",
                    "/components/navigation/train/companyInfo.xhtml",
                    "/components/navigation/train/jsfSurvey.xhtml",
                    "/components/navigation/train/trinidadSurvey.xhtml",
                    "/components/navigation/train/youAreDone.xhtml"
                }, getSummaryResourcePath(), getBackingBeanResourcePath()));
	}

    public String getSummaryResourcePath() {
        return "/components/navigation/train/summary.xhtml";
    }
    
    public String getBackingBeanResourcePath() {
		return "/org/apache/myfaces/trinidaddemo/components/navigation/train/TrainBean.java";
	}
}
