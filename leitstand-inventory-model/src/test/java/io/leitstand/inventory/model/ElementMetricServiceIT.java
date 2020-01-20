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

import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Element_Metric.removeMetrics;
import static io.leitstand.inventory.model.Metric.findMetricByName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.MetricName.metricName;
import static io.leitstand.inventory.service.MetricScope.ALL;
import static io.leitstand.inventory.service.MetricScope.ELEMENT;
import static io.leitstand.inventory.service.MetricScope.IFP;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0300E_ELEMENT_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementMetricService;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricSettings;

public class ElementMetricServiceIT extends InventoryIT{

	private static final MetricName IFP_DATA_RATE = metricName("emit_ifp_data_rate");
	private static final MetricName CPU_CORE_UTILIZATION = metricName("emit_cpu_core_utilization");
	private static final MetricName MEMORY_UTILIZATION = metricName("emit_cpu_memory_utilization");

	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element_metrics");
	private static final ElementRoleName ELEMENT_ROLE = elementRoleName("unittest");
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupName GROUP_NAME = groupName("elementmetrics");
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	
	private Messages messages;
	private Repository repository;
	private ElementMetricService service;
	
	@Before
	public void initTestEnvironment() {
		repository = new Repository(getEntityManager());
		messages = mock(Messages.class);
		ElementMetricManager manager = new ElementMetricManager(repository, 
																new MetricProvider(repository),
																messages);
		
		service = new DefaultElementMetricService(manager,new ElementProvider(repository));
		
		transaction(()->{
			repository.addIfAbsent(findMetricByName(CPU_CORE_UTILIZATION),
								   () -> {
									   Metric metric = new Metric(CPU_CORE_UTILIZATION);
									   metric.setMetricScope(ELEMENT);
									   metric.setMetricUnit("%");
									   metric.setDescription("CPU core utilization in %");
									   return metric;
								   });

			repository.addIfAbsent(findMetricByName(MEMORY_UTILIZATION),
					   () -> {
						   Metric metric = new Metric(MEMORY_UTILIZATION);
						   metric.setMetricScope(ELEMENT);
						   metric.setMetricUnit("%");
						   metric.setDescription("Memory utilization in %");
						   return metric;
					   });			
			
			repository.addIfAbsent(findMetricByName(IFP_DATA_RATE),
					   			  () -> {
					   				  Metric metric = new Metric(IFP_DATA_RATE);
					   				  metric.setMetricScope(IFP);
					   				  metric.setMetricUnit("bps");
					   				  metric.setDescription("Physical Interface Utilization");
					   				  return metric;
					   });
			
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE, 
																			   GROUP_NAME), 
																			   () -> new ElementGroup(GROUP_ID,
																					   				  GROUP_TYPE, 
																					   				  GROUP_NAME));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
													  () -> new ElementRole(ELEMENT_ROLE,DATA));
			
			repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
								   () -> new Element(group,
										   			 role,
										   			 ELEMENT_ID,
										   			 ELEMENT_NAME));
			
		});
	}

	@After
	public void remove_all_created_metric_bindings() {
		transaction(()->{
			Element element = repository.execute(findElementByName(ELEMENT_NAME));
			repository.execute(removeMetrics(element));
		});
	}
	
	@Test
	public void raise_entity_not_found_exception_when_element_name_is_unknown() {
		transaction(()->{
			try {
				service.findElementMetrics(randomElementId(),ELEMENT);
				fail("EntityNotFoundException expected");
			} catch(EntityNotFoundException e) {
				assertEquals(IVT0300E_ELEMENT_NOT_FOUND,e.getReason());
			}
		});
	}
	
	@Test
	public void raise_entity_not_found_exception_when_element_id_is_unknown() {
		
		transaction(()->{
			try {
				service.findElementMetrics(elementName("non-existent"), ELEMENT);
				fail("EntityNotFoundException expected");
			} catch(EntityNotFoundException e) {
				assertEquals(IVT0300E_ELEMENT_NOT_FOUND,e.getReason());
			}
		});
	}
	
	@Test
	public void raise_entity_not_found_exception_when_metric_does_not_exist() {
		transaction(()->{
			try {
				service.getElementMetric(ELEMENT_ID, metricName("unknown"));
				fail("EntityNotFoundException expected");
			} catch(EntityNotFoundException e) {
				assertEquals(IVT0600E_METRIC_NOT_FOUND,e.getReason());
			}
		});
	}
	
	@Test
	public void register_element_metrics() {
		transaction(()->{
			assertTrue(service.findElementMetrics(ELEMENT_ID, ALL).getMetrics().isEmpty());
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION,MEMORY_UTILIZATION,IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME,ALL).getMetrics();
			assertNotNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNotNull(metrics.get(MEMORY_UTILIZATION));
			assertNotNull(metrics.get(IFP_DATA_RATE));
		});
	}
	
	@Test
	public void remove_element_metrics() {
		transaction(()->{
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION,MEMORY_UTILIZATION,IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME,ALL).getMetrics();
			assertNotNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNotNull(metrics.get(MEMORY_UTILIZATION));
			assertNotNull(metrics.get(IFP_DATA_RATE));
			service.registerElementMetrics(ELEMENT_ID, emptyList());
		});
		
		transaction(()->{
			assertTrue(service.findElementMetrics(ELEMENT_NAME, ELEMENT).getMetrics().isEmpty());
		});
	}
	
	@Test
	public void merge_element_metrics() {
		transaction(()->{
			assertTrue(service.findElementMetrics(ELEMENT_ID, ALL).getMetrics().isEmpty());
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME,ALL).getMetrics();
			assertNotNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNull(metrics.get(MEMORY_UTILIZATION));
			assertNull(metrics.get(IFP_DATA_RATE));
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION,MEMORY_UTILIZATION,IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME,ALL).getMetrics();
			assertNotNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNotNull(metrics.get(MEMORY_UTILIZATION));
			assertNotNull(metrics.get(IFP_DATA_RATE));
			service.registerElementMetrics(ELEMENT_ID, emptyList());
		});
	}
	
	
	@Test
	public void replace_element_metrics() {
		transaction(()->{
			assertTrue(service.findElementMetrics(ELEMENT_ID, ALL).getMetrics().isEmpty());
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME,ALL).getMetrics();
			assertNotNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNull(metrics.get(MEMORY_UTILIZATION));
			assertNull(metrics.get(IFP_DATA_RATE));
			service.registerElementMetrics(ELEMENT_ID,asList(MEMORY_UTILIZATION,IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME, ALL).getMetrics();
			assertNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNotNull(metrics.get(MEMORY_UTILIZATION));
			assertNotNull(metrics.get(IFP_DATA_RATE));
			service.registerElementMetrics(ELEMENT_ID, emptyList());
		});
	}
	
	@Test
	public void repeatedly_register_element_metrics() {
		transaction(()->{
			service.registerElementMetrics(ELEMENT_ID,
										   asList(CPU_CORE_UTILIZATION,
												  MEMORY_UTILIZATION,
												  IFP_DATA_RATE));
		});
		
		transaction(()->{
			service.registerElementMetrics(ELEMENT_ID,
										   asList(CPU_CORE_UTILIZATION,
												  MEMORY_UTILIZATION,
												  IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME,ALL)
													 	    .getMetrics();
			assertNotNull(metrics.get(CPU_CORE_UTILIZATION));
			assertNotNull(metrics.get(MEMORY_UTILIZATION));
			assertNotNull(metrics.get(IFP_DATA_RATE));
			service.registerElementMetrics(ELEMENT_ID, emptyList());
		});
	}
	
	@Test
	public void fetch_element_scoped_metrics() {
		transaction(()->{
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION,MEMORY_UTILIZATION,IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME, ELEMENT)
															.getMetrics();
			assertTrue(metrics.containsKey(CPU_CORE_UTILIZATION));
			assertTrue(metrics.containsKey(MEMORY_UTILIZATION));
			assertFalse(metrics.containsKey(IFP_DATA_RATE));
		});
	}
	
	@Test
	public void fetch_ifp_scoped_metrics() {
		transaction(()->{
			service.registerElementMetrics(ELEMENT_ID,asList(CPU_CORE_UTILIZATION,MEMORY_UTILIZATION,IFP_DATA_RATE));
		});
		
		transaction(()->{
			Map<MetricName,MetricSettings> metrics = service.findElementMetrics(ELEMENT_NAME, IFP)
														  .getMetrics();
			assertFalse(metrics.containsKey(CPU_CORE_UTILIZATION));
			assertFalse(metrics.containsKey(MEMORY_UTILIZATION));
			assertTrue(metrics.containsKey(IFP_DATA_RATE));
		});
	}
	

	
}
