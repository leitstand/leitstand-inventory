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

import static io.leitstand.inventory.service.MetricId.randomMetricId;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricVisualizationService;
import io.leitstand.inventory.service.VisualizationConfigName;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMetricVisualizationServiceTest {
	
	private static final VisualizationConfigName VISUALIZATION = VisualizationConfigName.valueOf("visualization");
	private static final AlertRuleName ALERT_RULE = AlertRuleName.valueOf("alertrule");

	@Rule
	public ExpectedException exception = none();

	private static MetricId METRIC_ID = randomMetricId();
	private static MetricName METRIC_NAME = MetricName.valueOf("unittest");
	
	@Mock
	private MetricProvider metrics;
	
	@Mock 
	private MetricVisualizationManager manager;
	
	@InjectMocks
	private MetricVisualizationService service = new DefaultMetricVisualizationService();
	
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_remove_a_visualization_from_an_unknown_metric_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		doThrow(new EntityNotFoundException(IVT0600E_METRIC_NOT_FOUND)).when(metrics).fetchMetric(METRIC_ID);
		
		service.removeMetricVisualization(METRIC_ID, VISUALIZATION);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_remove_a_visualization_from_an_unknown_metric_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		doThrow(new EntityNotFoundException(IVT0600E_METRIC_NOT_FOUND)).when(metrics).fetchMetric(METRIC_NAME);
		
		service.removeMetricVisualization(METRIC_NAME, VISUALIZATION);
	}
	
	
	@Test
	public void remove_visualization_from_metric_identified_by_name() {
		Metric metric = mock(Metric.class);
		when(metrics.fetchMetric(METRIC_NAME)).thenReturn(metric);
		
		service.removeMetricVisualization(METRIC_NAME, VISUALIZATION);
		
		verify(metrics).fetchMetric(METRIC_NAME);
		verify(manager).removeVisualization(metric, VISUALIZATION);
		
	}
	
	@Test
	public void remove_visualization_from_metric_identified_by_id() {
		Metric metric = mock(Metric.class);
		when(metrics.fetchMetric(METRIC_ID)).thenReturn(metric);
		
		service.removeMetricVisualization(METRIC_ID, VISUALIZATION);
		
		verify(metrics).fetchMetric(METRIC_ID);
		verify(manager).removeVisualization(metric, VISUALIZATION);
		
	}
	
}
