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

import static io.leitstand.inventory.service.AlertRuleState.ACTIVE;
import static io.leitstand.inventory.service.AlertRuleState.SUPERSEDED;
import static io.leitstand.inventory.service.ReasonCode.IVT2000E_ALERT_RULE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT2006E_ALERT_RULE_DEFINITION_REQUIRED;
import static io.leitstand.inventory.service.ReasonCode.IVT2008E_ALERT_RULE_INVALID_STATE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.AlertRule;
import io.leitstand.inventory.service.AlertRuleId;

@RunWith(MockitoJUnitRunner.class)
public class MetricAlertRuleManagerTest {
	
	private static final AlertRuleId UNKNOWN_RULE_ID = AlertRuleId.valueOf("UNKNOWN");
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private Metric metric;
	
	@Mock
	private AlertRule rule;
	
	@Mock
	private Repository repository;
	
	@InjectMocks
	private MetricAlertRuleManager manager = new MetricAlertRuleManager();
	
	
	@Test
	public void cannot_store_null_rule_definition_for_active_rule() {
		when(rule.getRuleState()).thenReturn(ACTIVE);
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(IVT2006E_ALERT_RULE_DEFINITION_REQUIRED));
		
		manager.storeAlertRule(metric, rule);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_ruleid_is_unknown() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT2000E_ALERT_RULE_NOT_FOUND));
		
		manager.getMetricAlertRule(UNKNOWN_RULE_ID);
	}
	
	@Test
	public void cannot_store_rule_revision_with_SUPERSEDED_state() {
		when(rule.getRuleState()).thenReturn(SUPERSEDED);
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(IVT2008E_ALERT_RULE_INVALID_STATE));
		
		manager.storeAlertRule(metric, rule);
		
	}
}
