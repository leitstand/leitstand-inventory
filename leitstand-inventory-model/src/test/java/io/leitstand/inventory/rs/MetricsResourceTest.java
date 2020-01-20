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
import static io.leitstand.inventory.service.MetricSettings.newMetricSettings;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
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
import io.leitstand.inventory.service.MetricSettings;
import io.leitstand.inventory.service.MetricSettingsService;

@RunWith(MockitoJUnitRunner.class)
public class MetricsResourceTest {
	
	private static final MetricId METRIC_ID = randomMetricId();
	private static final MetricName METRIC_NAME = MetricName.valueOf("metric");
	private static final MetricSettings METRIC = newMetricSettings()
												 .withMetricId(METRIC_ID)
												 .withMetricName(METRIC_NAME)
												 .build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
			
	@Mock
	private Messages messages;
	
	@Mock
	private MetricSettingsService service;
	
	@InjectMocks
	private MetricsResource resource = new MetricsResource();
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_the_metric_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeMetricSettings(randomMetricId(), METRIC);
	}
	
	@Test
	public void send_created_response_when_new_metric_has_been_added() {
		when(service.storeMetricSettings(METRIC)).thenReturn(true);
		
		Response response = resource.storeMetricSettings(METRIC_ID, METRIC);	
		assertEquals(201,response.getStatus());
	}
	
	@Test
	public void send_success_response_when_existing_metric_was_updated() {
		when(service.storeMetricSettings(METRIC)).thenReturn(false);
		
		Response response = resource.storeMetricSettings(METRIC_ID, METRIC);	
		assertEquals(200,response.getStatus());
	}
	
	@Test
	public void force_remove_metric_by_id() {
		resource.removeMetric(METRIC_ID,true);
		verify(service).forceRemoveMetric(METRIC_ID);
	}
	
	@Test
	public void force_remove_metric_by_name() {
		resource.removeMetric(METRIC_NAME,true);
		verify(service).forceRemoveMetric(METRIC_NAME);
	}
	
	@Test
	public void remove_metric_by_id() {
		resource.removeMetric(METRIC_ID,false);
		verify(service).removeMetric(METRIC_ID);
	}
	
	@Test
	public void remove_metric_by_name() {
		resource.removeMetric(METRIC_NAME,false);
		verify(service).removeMetric(METRIC_NAME);
	}
}
