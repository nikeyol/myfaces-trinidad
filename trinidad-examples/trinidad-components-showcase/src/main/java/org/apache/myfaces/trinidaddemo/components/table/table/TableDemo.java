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
package org.apache.myfaces.trinidaddemo.components.table.table;

import org.apache.myfaces.trinidaddemo.support.impl.AbstractComponentDemo;
import org.apache.myfaces.trinidaddemo.support.impl.ComponentVariantDemoImpl;
import org.apache.myfaces.trinidaddemo.support.ComponentDemoId;
import org.apache.myfaces.trinidaddemo.support.IComponentDemoVariantId;

/**
 *
 */
public class TableDemo extends AbstractComponentDemo {

    private static final long serialVersionUID = -1982064950883398710L;

    private enum VARIANTS implements IComponentDemoVariantId {
		NoGridLines,
        SingleRowSelection,
        MultipleRowSelection,
        Pagination
	}

	/**
	 * Constructor.
	 */
	public TableDemo() {
		super(ComponentDemoId.table, "Table");

        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.SingleRowSelection, "Single Row Selection", this,
                "/components/table/table/tableSingleRowSelection.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.MultipleRowSelection, "Multiple Row Selection", this,
                "/components/table/table/tableMultipleRowSelection.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.NoGridLines, "No grid lines", this,
                "/components/table/table/tableNoGridLines.xhtml", getSummaryResourcePath()));
        addComponentDemoVariant(new ComponentVariantDemoImpl(VARIANTS.Pagination, this,
                "/components/table/table/tablePaginated.xhtml", getSummaryResourcePath()));
	}

	public String getJsfResourcePath() {
		return "/components/table/table/table.xhtml";
	}

    public String getSummaryResourcePath() {
        return "/components/table/table/summary.xhtml";
    }

    public String getBackingBeanResourcePath() {
		return "/org/apache/myfaces/trinidaddemo/components/table/column/TableColumnBean.java";
	}

}
