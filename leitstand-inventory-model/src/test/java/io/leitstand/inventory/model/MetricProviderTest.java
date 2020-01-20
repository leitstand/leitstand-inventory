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
import static org.junit.Assert.assertNull;
import static org.junit.rules.ExpectedException.none;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.testing.ut.LeitstandCoreMatchers;

@RunWith(MockitoJUnitRunner.class)
public class MetricProviderTest {

	@Rule
	public ExpectedException exception = none();
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private MetricProvider metrics = new MetricProvider();
	
	@Test
	public void tryFetchMetric_returns_null_for_unknown_metric() {
		assertNull(metrics.tryFetchMetric(randomMetricId()));
		assertNull(metrics.tryFetchMetric(MetricName.valueOf("unknown-metric")));
	}
	
	@Test
	public void fetchMetric_throws_EntityNotFoundException_when_attempting_to_read_unknown_metric_by_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0600E_METRIC_NOT_FOUND));
		metrics.fetchMetric(MetricName.valueOf("unknown-metric"));
	}
	
	@Test
	public void fetchMetric_throws_EntityNotFoundException_when_attempting_to_read_unknown_metric_by_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(LeitstandCoreMatchers.reason(IVT0600E_METRIC_NOT_FOUND));
		metrics.fetchMetric(randomMetricId());
	}
	
}
