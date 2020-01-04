/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.Element_Metric.findElementMetric;
import static io.leitstand.inventory.model.Element_Metric.findElementMetrics;
import static io.leitstand.inventory.service.ElementMetric.newElementMetric;
import static io.leitstand.inventory.service.ElementMetrics.newElementMetrics;
import static io.leitstand.inventory.service.MetricSettings.newMetricSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0350I_ELEMENT_METRIC_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementMetric;
import io.leitstand.inventory.service.ElementMetrics;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;

@Dependent
public class ElementMetricManager {
	
	private static final Logger LOG = Logger.getLogger(ElementMetricManager.class.getName());

	
	private Repository repository;
	
	private Messages messages;
	
	private MetricProvider metrics;
	
	@Inject
	protected ElementMetricManager(@Inventory Repository repository,
								   MetricProvider metrics,
						 		   Messages messages){
		this.repository = repository;
		this.messages = messages;
		this.metrics = metrics;
	}

	public ElementMetrics getElementMetrics(Element element, 
											MetricScope scope) {
		List<Element_Metric> metrics = repository.execute(findElementMetrics(element, scope));
		return elementMetrics(element,metrics);
	} 
	
	public ElementMetrics getElementMetrics(Element element) {
		List<Element_Metric> metrics = repository.execute(findElementMetrics(element));
		return elementMetrics(element,metrics);
	}
	
	private ElementMetrics elementMetrics(Element element, List<Element_Metric> metrics) {
		return newElementMetrics()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementRole(element.getElementRoleName())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withMetrics(metrics
					   		.stream()
					   		.map(metric -> newMetricSettings()
					   					   .withMetricId(metric.getMetricId())
					   					   .withMetricName(metric.getMetricName())
					   					   .withDisplayName(metric.getDisplayName())
					   					   .withMetricScope(metric.getMetricScope())
					   					   .withMetricUnit(metric.getMetricUnit())
					   					   .withDescription(metric.getDescription())
					   					   .build())
					   		.collect(toList()))
			   .build();
	}
	
	public ElementMetric getElementMetric(Element element, 
										  MetricName metricName) {
		Element_Metric metric = repository.execute(findElementMetric(element, 
																	 metricName));
		if(metric == null) {
			LOG.fine(() -> format("%s: Metric %s not defined on element %s.",
					  			  IVT0600E_METRIC_NOT_FOUND.getReasonCode(),
					  			  metricName,
					  			  element.getElementName()));	
			throw new EntityNotFoundException(IVT0600E_METRIC_NOT_FOUND, 
											  element.getElementName(),
											  metricName);
		}
	
		return newElementMetric()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withMetric(newMetricSettings()
					   	   .withMetricId(metric.getMetricId())
					   	   .withMetricName(metric.getMetricName())
					   	   .withDisplayName(metric.getDisplayName())
					   	   .withMetricScope(metric.getMetricScope())
					   	   .withMetricUnit(metric.getMetricUnit())
					   	   .withDescription(metric.getDescription()))
			   .build();
	}

	public void storeElementMetric(Element element, ElementMetric elementMetric) {
		Element_Metric _metric = repository.execute(findElementMetric(element, 
																	  elementMetric.getMetric().getMetricName()));
		
		if(_metric == null) {
			_metric = new Element_Metric(element,
										 metrics.fetchMetric(elementMetric.getMetric().getMetricName()));
			repository.add(_metric);
		}
	}
	
	public void registerElementMetrics(Element element, List<MetricName> enabledMetrics) {
		// Create a temporary map of all configured metrics.
		Map<MetricName,Element_Metric> configuredMetrics = repository.execute(findElementMetrics(element))
																	 .stream()
																	 .collect(toMap(Element_Metric::getMetricName, identity()));
		// Remove all already configured metrics. Add new metric if the enabled metric was not configured.
		for(MetricName enabledMetric : enabledMetrics) {
			Element_Metric elementMetric = configuredMetrics.remove(enabledMetric);
			if(elementMetric != null) {
				LOG.fine(() -> format("Metric %s already configured on element %s. No action required.",
				  	  	  			   enabledMetric,
				  	  	  			   element.getElementName()));
				messages.add(createMessage(IVT0350I_ELEMENT_METRIC_STORED, 
										   element.getElementName(),
										   enabledMetric));
				// Merge next metric
				continue;
			}

			// Attempt to register new metric
			Metric metric = metrics.tryFetchMetric(enabledMetric);
			if(metric == null) {
				LOG.fine(() -> format("Metric %s reported by element %s is unknown and will be ignored.",
									  enabledMetric,
									  element.getElementName()));
				messages.add(createMessage(IVT0600E_METRIC_NOT_FOUND,
										   enabledMetric));	
			
				// Merge next metric
				continue;
			}
			repository.add(new Element_Metric(element,metric));
			
		}
		
		// Remove all remaining metrics as shall not enabled anymore.
		for(Element_Metric elementMetric : configuredMetrics.values()) {
			repository.remove(elementMetric);
		}
		
	}


}