/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.MetricId.randomMetricId;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricSettingsService;

@RunWith(MockitoJUnitRunner.class)
public class DefaultMetricSettingsServiceTest {
	
	@Rule
	public ExpectedException exception = none();

	private static MetricId METRIC_ID = randomMetricId();
	private static MetricName METRIC_NAME = MetricName.valueOf("unittest");
	
	@Mock
	private MetricProvider metrics;
	
	@Mock 
	private MetricSettingsManager manager;
	
	@InjectMocks
	private MetricSettingsService service = new DefaultMetricSettingsService();
	
	
	@Test
	public void do_nothing_when_attempting_to_remove_an_unknown_metric_identified_by_name() {
		service.removeMetric(METRIC_ID);
		verify(metrics).tryFetchMetric(METRIC_ID);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void do_nothing_when_attempting_to_remove_an_unknown_metric_identified_by_id() {
		service.removeMetric(METRIC_NAME);
		verify(metrics).tryFetchMetric(METRIC_NAME);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void do_nothing_when_attempting_to_force_remove_of_an_unknown_metric_identified_by_name() {
		service.forceRemoveMetric(METRIC_ID);
		verify(metrics).tryFetchMetric(METRIC_ID);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void do_nothing_when_attempting_to_force_remove_of_an_unknown_metric_identified_by_id() {
		service.forceRemoveMetric(METRIC_NAME);
		verify(metrics).tryFetchMetric(METRIC_NAME);
		verifyZeroInteractions(manager);
	}
	
	@Test
	public void remove_metric_identified_by_id() {
		Metric metric = mock(Metric.class);
		when(metrics.tryFetchMetric(METRIC_ID)).thenReturn(metric);
		
		service.removeMetric(METRIC_ID);
		
		verify(manager).removeMetric(metric);
	}
	
	@Test
	public void remove_metric_identified_by_name() {
		Metric metric = mock(Metric.class);
		when(metrics.tryFetchMetric(METRIC_NAME)).thenReturn(metric);
		
		service.removeMetric(METRIC_NAME);
		
		verify(manager).removeMetric(metric);
	}
	
	@Test
	public void force_remove_metric_identified_by_id() {
		Metric metric = mock(Metric.class);
		when(metrics.tryFetchMetric(METRIC_ID)).thenReturn(metric);
		
		service.forceRemoveMetric(METRIC_ID);
		
		verify(manager).forceRemoveMetric(metric);
	}
	
	@Test
	public void force_remove_metric_identified_by_name() {
		Metric metric = mock(Metric.class);
		when(metrics.tryFetchMetric(METRIC_NAME)).thenReturn(metric);
		
		service.forceRemoveMetric(METRIC_NAME);
		
		verify(manager).forceRemoveMetric(metric);
	}
	

}
