/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.model.Metric.findMetricById;
import static io.leitstand.inventory.service.AlertRule.newAlertRule;
import static io.leitstand.inventory.service.AlertRuleState.ACTIVE;
import static io.leitstand.inventory.service.AlertRuleState.CANDIDATE;
import static io.leitstand.inventory.service.MetricId.randomMetricId;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT2000E_ALERT_RULE_NOT_FOUND;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.isEmptyList;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static javax.json.Json.createObjectBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Date;

import javax.json.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.AlertRule;
import io.leitstand.inventory.service.AlertRuleId;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.AlertRuleRevisionInfo;
import io.leitstand.inventory.service.AlertRuleRevisions;
import io.leitstand.inventory.service.MetricAlertRuleService;
import io.leitstand.inventory.service.MetricAlertRules;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;

public class MetricAlertRulesIT extends InventoryIT{

	private static final MetricId UNKNOWN_METRIC_ID = randomMetricId();
	private static final MetricName UNKNOWN_METRIC_NAME = MetricName.valueOf("UNKNOWN");
	private static final MetricId METRIC_ID = randomMetricId();
	private static final MetricName METRIC_NAME = MetricName.valueOf(MetricAlertRulesIT.class.getSimpleName());
	private static final AlertRuleId RDV1_RULE_ID = AlertRuleId.valueOf("JUNITRULEID_V1");
	private static final AlertRuleId RDV2_RULE_ID = AlertRuleId.valueOf("JUNITRULEID_V2");
	private static final AlertRuleName RULE_NAME = AlertRuleName.valueOf("RULE_NAME");
	private static final JsonObject RULE_DEFINITION_V1 = createObjectBuilder()
														 .add("version", "1")
														 .build();
	private static final JsonObject RULE_DEFINITION_V2 = createObjectBuilder()
														 .add("version", "2")
														 .build();
	private static final String RULE_TYPE = "dummy";
	
	private MetricAlertRuleService service;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	
	@Before
	public void initTestEnvironment() {
		DatabaseService database = getDatabase();
		Repository repository = new Repository(getEntityManager());
		MetricProvider metrics = new MetricProvider(repository);
		MetricAlertRuleManager manager = new MetricAlertRuleManager(repository, 
												  					database);
		this.service = new DefaultMetricAlertRuleService(manager, metrics);
		
		transaction(()->{
			repository.addIfAbsent(findMetricById(METRIC_ID), 
								   () -> new Metric(METRIC_ID, METRIC_NAME));
		});
		
	}
	
	@After
	public void clearTestEnvironment() {
		
		// Remove all revisions
		getDatabase().executeUpdate(prepare("DELETE FROM inventory.metric_alertrule_definition"));
		// Remoce all rules.
		getDatabase().executeUpdate(prepare("DELETE FROM inventory.metric_alertrule"));
		
		
	}
	
	@Test
	public void get_empty_alert_rules_if_no_alert_rules_exist_for_metric_id() {
		MetricAlertRules rules = service.getMetricAlertRules(METRIC_ID);
		assertThat(rules.getRules(),isEmptyList());
	}
	
	@Test
	public void get_empty_alert_rules_if_no_alert_rules_exist_for_metric_name() {
		MetricAlertRules rules = service.getMetricAlertRules(METRIC_NAME);
		assertThat(rules.getRules(),isEmptyList());
	}


	/**
	 * Test creation of alert rule without rule definitions.
	 */
	@Test
	public void create_candidate_alert_rule_stub_for_metric_id() {
		transaction(()->{
			AlertRule rule = newAlertRule()
							.withRuleName(RULE_NAME)
							.withCategory("unittest")
							.withDescription("Dummy junit rule")
							.withRuleState(CANDIDATE)
							.build();
			boolean created = service.storeAlertRule(METRIC_ID, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_ID, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("unittest",reloaded.getCategory());
			assertEquals("Dummy junit rule",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertNull(revision.getRuleType());
			assertNull(revision.getRuleId());

			AlertRule rule = service.getMetricAlertRule(METRIC_NAME,RULE_NAME,revision.getDateModified()).getRule();
			assertNull(rule.getRuleDefinition());
		});
		
	}
	
	/**
	 * Test creation of alert rule without rule definitions.
	 */
	@Test
	public void create_initial_candidate_alert_rule_for_metric_name() {
		transaction(()->{
			AlertRule rule = newAlertRule()
							.withRuleId(RDV1_RULE_ID)
							.withRuleName(RULE_NAME)
							.withCategory("unittest")
							.withDescription("Dummy junit rule")
							.withRuleState(CANDIDATE)
							.withRuleType(RULE_TYPE)
							.withRuleDefinition(RULE_DEFINITION_V1)
							.build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("unittest",reloaded.getCategory());
			assertEquals("Dummy junit rule",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertEquals(RDV1_RULE_ID,revision.getRuleId());
			assertEquals(RULE_TYPE,revision.getRuleType());

			AlertRule rule = service.getMetricAlertRule(RDV1_RULE_ID).getRule();
			assertEquals(RULE_DEFINITION_V1, rule.getRuleDefinition());
		});
		
	}

	@Test
	public void create_initial_candidate_alert_rule_for_metric_id() {
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID)
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(CANDIDATE)
							 .withRuleType("dummy")
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_ID, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_ID, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("unittest",reloaded.getCategory());
			assertEquals("Dummy junit rule",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertEquals(RDV1_RULE_ID,revision.getRuleId());
			assertEquals(RULE_TYPE,revision.getRuleType());

			AlertRule rule = service.getMetricAlertRule(RDV1_RULE_ID).getRule();
			assertEquals(RULE_DEFINITION_V1,rule.getRuleDefinition());
			
		});
		
	}
	
	/**
	 * Test creation of alert rule without rule definitions.
	 */
	@Test
	public void create_candidate_alert_rule_stub_for_metric_name() {
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withCreator("junit")
							 .withRuleState(CANDIDATE)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("unittest",reloaded.getCategory());
			assertEquals("Dummy junit rule",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertNull(revision.getRuleId());
			assertNull(revision.getRuleType());
			
			AlertRule rule = service.getMetricAlertRule(METRIC_NAME,RULE_NAME,revision.getDateModified()).getRule();
			assertNull(rule.getRuleDefinition());
		});
		
	}
	
	@Test
	public void update_active_alert_rule() {
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID)
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
	}
	
	@Test
	public void add_activate_rule_revision_to_existing_active_rule() {
		
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID) 
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV2_RULE_ID) // Assign new rule ID
							 .withRuleName(RULE_NAME)
							 .withCategory("new category")
							 .withDescription("new description")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1) // Definition remains the same!!
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertFalse(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("new category",reloaded.getCategory());
			assertEquals("new description",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(ACTIVE,revision.getRuleState());
			assertEquals(RDV2_RULE_ID, revision.getRuleId());
			AlertRule rule = service.getMetricAlertRule(RDV2_RULE_ID).getRule();
			assertEquals(RULE_DEFINITION_V1,rule.getRuleDefinition());
			assertEquals("new category",rule.getCategory());
			assertEquals("new description",rule.getDescription());
		});
		
	}

	@Test
	public void add_new_candidate_rule_revision_to_active_rule() {

		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID) 
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV2_RULE_ID) // Assign new rule ID
							 .withRuleName(RULE_NAME)
							 .withCategory("new category")
							 .withDescription("new description")
							 .withRuleState(CANDIDATE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("new category",reloaded.getCategory());
			assertEquals("new description",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(2));
			
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertEquals(RDV2_RULE_ID, revision.getRuleId());
			
			revision = reloaded.getRevisions().get(1);
			assertEquals(ACTIVE,revision.getRuleState());
			assertEquals(RDV1_RULE_ID, revision.getRuleId());

		});
		
	}
	
	@Test
	public void convert_candidate_rule_revision_to_active_rule() {

		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID) 
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(CANDIDATE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV2_RULE_ID) // Assign new rule ID
							 .withRuleName(RULE_NAME)
							 .withCategory("new category")
							 .withDescription("new description")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V2)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertFalse(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("new category",reloaded.getCategory());
			assertEquals("new description",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(ACTIVE,revision.getRuleState());
			assertEquals(RDV2_RULE_ID, revision.getRuleId());

		});
		
	}
	
	@Test
	public void remove_alert_rule_including_all_revisions() {

		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID) 
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV2_RULE_ID) // Assign new rule ID
							 .withRuleName(RULE_NAME)
							 .withCategory("new category")
							 .withDescription("new description")
							 .withRuleState(CANDIDATE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("new category",reloaded.getCategory());
			assertEquals("new description",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(2));
			
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertEquals(RDV2_RULE_ID, revision.getRuleId());
			
			service.removeAlertRule(METRIC_ID, 
									RULE_NAME);

		});
		
	
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT2000E_ALERT_RULE_NOT_FOUND));
			
			service.getMetricAlertRuleRevisions(METRIC_ID,RULE_NAME);
		});
	
	}
	
	@Test
	public void remove_alert_rule_revision() {
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV1_RULE_ID) 
							 .withRuleName(RULE_NAME)
							 .withCategory("unittest")
							 .withDescription("Dummy junit rule")
							 .withRuleState(ACTIVE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRule rule = newAlertRule()
							 .withRuleId(RDV2_RULE_ID) // Assign new rule ID
							 .withRuleName(RULE_NAME)
							 .withCategory("new category")
							 .withDescription("new description")
							 .withRuleState(CANDIDATE)
							 .withRuleType(RULE_TYPE)
							 .withRuleDefinition(RULE_DEFINITION_V1)
							 .build();
			boolean created = service.storeAlertRule(METRIC_NAME, rule);
			assertTrue(created);
		});
		
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("new category",reloaded.getCategory());
			assertEquals("new description",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(2));
			
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(CANDIDATE,revision.getRuleState());
			assertEquals(RDV2_RULE_ID, revision.getRuleId());
			
			service.removeAlertRuleRevision(METRIC_ID, 
											RULE_NAME, 
											revision.getDateModified());

		});
		
	
		transaction(()->{
			AlertRuleRevisions reloaded = service.getMetricAlertRuleRevisions(METRIC_NAME, RULE_NAME)
												 .getRevisions();
			assertEquals(RULE_NAME,reloaded.getRuleName());
			assertEquals("new category",reloaded.getCategory());
			assertEquals("new description",reloaded.getDescription());
			assertThat(reloaded.getRevisions(),hasSizeOf(1));
			
			AlertRuleRevisionInfo revision = reloaded.getRevisions().get(0);
			assertEquals(ACTIVE,revision.getRuleState());
			assertEquals(RDV1_RULE_ID, revision.getRuleId());

		});
	
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_store_rule_for_unknown_metrid_ic() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.storeAlertRule(UNKNOWN_METRIC_ID, mock(AlertRule.class));
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_store_rule_for_unknown_metric_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.storeAlertRule(UNKNOWN_METRIC_NAME, mock(AlertRule.class));
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_rules_of_unknown_metrid_ic() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricAlertRules(UNKNOWN_METRIC_ID);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_rules_of_unknown_metric_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricAlertRules(UNKNOWN_METRIC_NAME);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_rule_of_unknown_metrid_ic() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricAlertRule(UNKNOWN_METRIC_ID,RULE_NAME,new Date());
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_rule_of_unknown_metric_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricAlertRule(UNKNOWN_METRIC_NAME,RULE_NAME,new Date());
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_rule_revisions_of_unknown_metrid_ic() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricAlertRuleRevisions(UNKNOWN_METRIC_ID,RULE_NAME);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_rule_revisions_of_unknown_metric_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricAlertRuleRevisions(UNKNOWN_METRIC_NAME,RULE_NAME);
	}
	
}
