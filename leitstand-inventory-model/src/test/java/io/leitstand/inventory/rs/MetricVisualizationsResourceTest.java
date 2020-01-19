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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.MetricId.randomMetricId;
import static io.leitstand.inventory.service.VisualizationConfig.newVisualizationConfig;
import static io.leitstand.inventory.service.VisualizationConfigId.randomVisualizationId;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricVisualizationService;
import io.leitstand.inventory.service.VisualizationConfig;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

@RunWith(MockitoJUnitRunner.class)
public class MetricVisualizationsResourceTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private MetricVisualizationService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private MetricVisualizationsResource resource = new MetricVisualizationsResource();
	
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_the_visualization_id_of_a_metric_identified_by_name() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.build();
		resource.storeMetricVisualization(MetricName.valueOf("any-metric"), 
										  randomVisualizationId(), 
										  visualization);
		
	}
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_the_visualization_id_of_a_metric_identified_by_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(LeitstandCoreMatchers.reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.build();
		resource.storeMetricVisualization(randomMetricId(), 
										  randomVisualizationId(), 
										  visualization);
		
	}
	
	@Test
	public void send_created_response_when_new_visualization_was_added_for_metric_identified_by_id() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.build();
		MetricId metricId = randomMetricId();
		when(service.storeMetricVisualization(metricId, visualization)).thenReturn(true);
		
		Response response = resource.storeMetricVisualization(metricId, 
															 visualization.getVisualizationId(),
															 visualization);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_created_response_when_new_visualization_was_added_for_metric_identified_by_name() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.build();
		MetricName metricId = MetricName.valueOf("any-metric");
		when(service.storeMetricVisualization(metricId, visualization)).thenReturn(true);
		
		Response response = resource.storeMetricVisualization(metricId, 
															 visualization.getVisualizationId(),
															 visualization);
		
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_existing_visualization_was_updated_for_metric_identified_by_id() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.build();
		MetricId metricId = randomMetricId();
		when(service.storeMetricVisualization(metricId, visualization)).thenReturn(false);
		
		Response response = resource.storeMetricVisualization(metricId, 
															 visualization.getVisualizationId(),
															 visualization);
		
		assertEquals(200,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_existing_visualization_was_updated_for_metric_identified_by_name() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.build();
		MetricName metricId = MetricName.valueOf("any-metric");
		when(service.storeMetricVisualization(metricId, visualization)).thenReturn(false);
		
		Response response = resource.storeMetricVisualization(metricId, 
															 visualization.getVisualizationId(),
															 visualization);
		
		assertEquals(200,response.getStatus());
	}
	
}
