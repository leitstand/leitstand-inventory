/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementMetric;
import io.leitstand.inventory.service.ElementMetricService;
import io.leitstand.inventory.service.ElementMetrics;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;

@Service
public class DefaultElementMetricService implements ElementMetricService{

	@Inject
	private ElementMetricManager inventory;
	
	@Inject
	private ElementProvider elements;
	
	public DefaultElementMetricService() {
		// Toolconstructor
	}
	
	DefaultElementMetricService(ElementMetricManager inventory, ElementProvider elements){
		this.inventory = inventory;
		this.elements  = elements;
	}
	
	@Override
	public ElementMetric getElementMetric(ElementId elementId, MetricName metricName) {
		Element element = elements.fetchElement(elementId);
		return inventory.getElementMetric(element,metricName);
	}

	@Override
	public ElementMetric getElementMetric(ElementName elementName, MetricName metricName) {
		Element element = elements.fetchElement(elementName);
		return inventory.getElementMetric(element,metricName);
	}
	
	@Override
	public void storeElementMetric(ElementName elementName,ElementMetric metric) {
		Element element = elements.fetchElement(elementName);
		inventory.storeElementMetric(element, metric);
	}

	@Override
	public void storeElementMetric(ElementId elementId, ElementMetric metric) {
		Element element = elements.fetchElement(elementId);
		inventory.storeElementMetric(element,metric);
	}

	@Override
	public void registerElementMetrics(ElementId elementId, List<MetricName> metrics) {
		Element element = elements.fetchElement(elementId);
		inventory.registerElementMetrics(element,metrics);
		
	}

	@Override
	public void registerElementMetrics(ElementName elementName, List<MetricName> metrics) {
		Element element = elements.fetchElement(elementName);
		inventory.registerElementMetrics(element,metrics);
		
	}

	@Override
	public ElementMetrics findElementMetrics(ElementId elementId, MetricScope scope) {
		Element element = elements.fetchElement(elementId);
		return inventory.getElementMetrics(element, scope);
	}

	@Override
	public ElementMetrics findElementMetrics(ElementName elementName, MetricScope scope) {
		Element element = elements.fetchElement(elementName);
		return inventory.getElementMetrics(element, scope);
	}

}
