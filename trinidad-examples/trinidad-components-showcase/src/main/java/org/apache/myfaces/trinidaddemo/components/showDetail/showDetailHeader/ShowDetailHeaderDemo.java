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
package org.apache.myfaces.trinidaddemo.components.showDetail.showDetailHeader;

import org.apache.myfaces.trinidaddemo.support.impl.AbstractComponentDemo;
import org.apache.myfaces.trinidaddemo.support.impl.ComponentVariantDemoImpl;
import org.apache.myfaces.trinidaddemo.support.ComponentDemoId;
import org.apache.myfaces.trinidaddemo.support.IComponentDemoVariantId;

/**
 *
 */
public class ShowDetailHeaderDemo extends AbstractComponentDemo {

    private static final long serialVersionUID = -1989461456014395510L;

    private enum VARIANTS implements IComponentDemoVariantId {
		ConfirmationMessage,
        InfoMessage,
        ErrorMessage,
        WarningMessage
	}

	/**
	 * Constructor.
	 */
	public ShowDetailHeaderDemo(){
		super(ComponentDemoId.showDetailHeader, "Show Detail Header");

        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.InfoMessage, "Info Message", this,
                "/components/showDetail/showDetailHeader/showDetailHeaderInfoMessage.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.ConfirmationMessage, "Confirmation Message", this,
                "/components/showDetail/showDetailHeader/showDetailHeaderConfirmationMessage.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.ErrorMessage, "Error message", this,
                "/components/showDetail/showDetailHeader/showDetailHeaderErrorMessage.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.WarningMessage, "Warning message", this,
                "/components/showDetail/showDetailHeader/showDetailHeaderWarningMessage.xhtml", getSummaryResourcePath()));
	}

	public String getJsfResourcePath() {
		return "/components/showDetail/showDetailHeader/showDetailHeader.xhtml";
	}

    public String getSummaryResourcePath() {
        return "/components/showDetail/showDetailHeader/summary.xhtml";
    }
}
