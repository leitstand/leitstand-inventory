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
import static io.leitstand.inventory.model.Element_Metric.countElementMetricBindings;
import static io.leitstand.inventory.model.Element_Metric.findElementMetric;
import static io.leitstand.inventory.model.Metric.findMetricByName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.MetricName.metricName;
import static io.leitstand.inventory.service.MetricScope.ELEMENT;
import static io.leitstand.inventory.service.MetricScope.IFP;
import static io.leitstand.inventory.service.MetricSettings.newMetricSettings;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0601I_METRIC_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0602I_METRIC_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0603E_CANNOT_REMOVE_BOUND_METRIC;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricSettings;
import io.leitstand.inventory.service.MetricSettingsService;

public class MetricServiceIT extends InventoryIT{

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private MetricSettingsService service;
	private ArgumentCaptor<Message> messageCaptor;
	private Repository repository;
	
	@Before
	public void initTestEnvironment() {
		Messages messages = mock(Messages.class);
		messageCaptor = forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
		repository = new Repository(getEntityManager());
		service = new DefaultMetricSettingsService(new MetricSettingsManager(repository,
																			 new ElementRoleProvider(repository),
																			 messages),
												   new MetricProvider(repository));
	}
	
	@Test
	public void raise_exception_when_metric_does_not_exist() {
		try {
			service.getMetricSettings(MetricName.valueOf("non-existent"));
			fail("EntitNotFoundException expected");
		} catch(EntityNotFoundException e) {
			assertEquals(IVT0600E_METRIC_NOT_FOUND,e.getReason());
		}
	}
	
	@Test
	public void cannot_add_metric_for_unknown_elementrole() {
		MetricSettings metric = newMetricSettings()
				.withMetricName(MetricName.valueOf("unknown_role"))
				.withMetricScope(ELEMENT)
				.withDescription("Unittest metric")
				.withMetricUnit("none")
				.withElementRoles(ElementRoleName.valueOf("unknown-role"))
				.build();
		
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT0400E_ELEMENT_ROLE_NOT_FOUND));
			service.storeMetricSettings(metric);
		});
	}
	
	
	@Test
	public void create_new_metric() {
		MetricSettings metric = newMetricSettings()
								.withMetricName(MetricName.valueOf("new_metric"))
								.withMetricScope(ELEMENT)
								.withDescription("Unittest metric")
								.withMetricUnit("none")
								.build();
		transaction(() -> {
			boolean created = service.storeMetricSettings(metric);
			assertTrue(created);
			assertEquals(IVT0601I_METRIC_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
	
		transaction(()->{
			MetricSettings storedMetric = service.getMetricSettings(MetricName.valueOf("new_metric"));
			assertNotSame(metric, storedMetric);
		});
	
	}
	
	
	@Test
	public void rename_existing_metric() {
		MetricSettings metric = newMetricSettings()
								.withMetricName(MetricName.valueOf("rename_metric"))
								.withMetricScope(ELEMENT)
								.withDescription("Unittest metric")
								.withMetricUnit("none")
								.build();
		transaction(() -> {
			boolean created = service.storeMetricSettings(metric);
			assertTrue(created);
			assertEquals(IVT0601I_METRIC_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
	
		transaction(()->{
			MetricSettings renamedMetric = newMetricSettings()
										   .withMetricId(metric.getMetricId())
										   .withMetricName(MetricName.valueOf("renamed_metric"))
										   .withMetricScope(ELEMENT)
										   .withDescription("Unittest metric")
										   .withMetricUnit("none")
										   .build();

			boolean created = service.storeMetricSettings(renamedMetric);
			assertFalse(created);
			assertEquals(IVT0601I_METRIC_STORED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
		
		transaction(()->{
			assertEquals(MetricName.valueOf("renamed_metric"),
						 service.getMetricSettings(MetricName.valueOf("renamed_metric")).getMetricName());
		});
	
	}
	
	@Test
	public void remove_existing_metric() {
		MetricSettings metric = newMetricSettings()
							    .withMetricName(MetricName.valueOf("remove_metric"))
							    .withMetricScope(ELEMENT)
							    .withMetricUnit("none")
							    .withDescription("Unittest metric")
							    .build();
		transaction(() -> {
			service.storeMetricSettings(metric);
		});
	
		transaction(()->{
			assertNotNull(service.getMetricSettings(metric.getMetricName()));
			service.removeMetric(metric.getMetricName());
			assertEquals(IVT0602I_METRIC_REMOVED.getReasonCode(),
						 messageCaptor.getValue().getReason());
		});
		
		transaction(() -> {
			try {
				service.getMetricSettings(metric.getMetricName());
				fail("EntityNotFoundException expected!");
			} catch(EntityNotFoundException e) {
				assertEquals(IVT0600E_METRIC_NOT_FOUND,e.getReason());
			}
		});
	
	}
	
	@Test
	public void cannot_remove_bound_metric() {
		transaction(() -> {
			Metric metric = repository.addIfAbsent(Metric.findMetricByName(metricName("bound_metric")),
												   () -> new Metric(metricName("bound_metric")));
			
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(groupType("unittest"),
																			   groupName("metric")) , 
														() -> new ElementGroup(randomGroupId(),
																			   groupType("unittest"),
																               groupName("metric")));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(elementRoleName("unittest")),
													  () -> new ElementRole(elementRoleName("unittest"),
															  				DATA));
			
			Element element = repository.addIfAbsent(findElementByName(elementName("bound_element")),
													 () -> new Element(group,
															 		   role,
															 		   randomElementId(),
															 		   elementName("bound_element")));
			
			
			repository.addIfAbsent(findElementMetric(element, metric), 
								   () -> new Element_Metric(element,metric));
		});
		
	
		transaction(()->{
			try {
				service.removeMetric(MetricName.valueOf("bound_metric"));
				fail("ConflictException expected");
			} catch (ConflictException e) {
				assertEquals(IVT0603E_CANNOT_REMOVE_BOUND_METRIC,e.getReason());
			}
			assertNotNull(service.getMetricSettings(MetricName.valueOf("bound_metric")));
			Metric metric = repository.execute(findMetricByName(MetricName.valueOf("bound_metric")));
			assertEquals(Long.valueOf(1),repository.execute(countElementMetricBindings(metric)));

		});
	
	}
	
	
	@Test
	public void cannot_force_remove_bound_metric() {
		transaction(() -> {
			Metric metric = repository.addIfAbsent(findMetricByName(MetricName.valueOf("bound_metric")),
												   () -> new Metric(MetricName.valueOf("bound_metric")));
			
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(groupType("unittest"),
																			   ElementGroupName.valueOf("metric")) , 
														() -> new ElementGroup(randomGroupId(),
																			   groupType("unittest"),
																               ElementGroupName.valueOf("metric")));
			
			ElementRole role = repository.addIfAbsent(findRoleByName(ElementRoleName.valueOf("unittest")),
													  () -> new ElementRole(ElementRoleName.valueOf("unittest"),
															  				DATA));
			
			Element element = repository.addIfAbsent(findElementByName(elementName("bound_element")),
													 () -> new Element(group,
															 		   role,
															 		   randomElementId(),
															 		   elementName("bound_element")));
			
			
			repository.addIfAbsent(findElementMetric(element, metric), 
								   () -> new Element_Metric(element,metric));
		});
		
	
		transaction(()->{			
			Metric metric = repository.execute(findMetricByName(MetricName.valueOf("bound_metric")));
			assertEquals(Long.valueOf(1),repository.execute(countElementMetricBindings(metric)));
			service.forceRemoveMetric(MetricName.valueOf("bound_metric"));
			assertEquals(IVT0602I_METRIC_REMOVED.getReasonCode(),messageCaptor.getValue().getReason());
		});
	
		transaction(() -> {
			try {
				service.getMetricSettings(MetricName.valueOf("bound_metric"));
				fail("EntityNotFoundException expected!");
			} catch(EntityNotFoundException e) {
				assertEquals(IVT0600E_METRIC_NOT_FOUND,e.getReason());
			}
			Metric metric = repository.execute(findMetricByName(MetricName.valueOf("bound_metric")));
			assertEquals(Long.valueOf(0),repository.execute(countElementMetricBindings(metric)));
		});
		
	}
	
	@Test
	public void filter_metrics_by_name() {
		transaction(() -> {
			service.storeMetricSettings(newMetricSettings()
										.withMetricName(MetricName.valueOf("filter_metric_a"))
										.withMetricScope(ELEMENT)
										.build());
			service.storeMetricSettings(newMetricSettings()
										.withMetricName(MetricName.valueOf("filter_metric_b"))
										.withMetricScope(IFP)
										.build());
		});
		
		transaction(() -> {
			List<MetricSettings> settings = service.findMetrics("^filter_.*[a|b]$");
			assertEquals(2,settings.size());
		});
	}
	
}
