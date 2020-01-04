/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.inventory.model.Metric_AlertRule.findAlertRule;
import static io.leitstand.inventory.model.Metric_AlertRule_Definition.findAlertRuleDefinition;
import static io.leitstand.inventory.model.Metric_AlertRule_Definition.findAlertRuleDefinitions;
import static io.leitstand.inventory.service.AlertRule.newAlertRule;
import static io.leitstand.inventory.service.AlertRuleInfo.newAlertRuleInfo;
import static io.leitstand.inventory.service.AlertRuleRevisionInfo.newAlertRuleRevisionInfo;
import static io.leitstand.inventory.service.AlertRuleRevisions.newAlertRuleRevisions;
import static io.leitstand.inventory.service.AlertRuleState.ACTIVE;
import static io.leitstand.inventory.service.AlertRuleState.CANDIDATE;
import static io.leitstand.inventory.service.AlertRuleState.SUPERSEDED;
import static io.leitstand.inventory.service.MetricAlertRule.newMetricAlertRule;
import static io.leitstand.inventory.service.MetricAlertRuleRevisions.newMetricAlertRuleRevisions;
import static io.leitstand.inventory.service.MetricAlertRules.newMetricAlertRules;
import static io.leitstand.inventory.service.ReasonCode.IVT2000E_ALERT_RULE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT2003I_ALERT_RULE_REVISION_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT2006E_ALERT_RULE_DEFINITION_REQUIRED;
import static io.leitstand.inventory.service.ReasonCode.IVT2007E_ALERT_RULE_IMMUTABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT2008E_ALERT_RULE_INVALID_STATE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.AlertRule;
import io.leitstand.inventory.service.AlertRuleId;
import io.leitstand.inventory.service.AlertRuleInfo;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.AlertRuleRevisionInfo;
import io.leitstand.inventory.service.AlertRuleState;
import io.leitstand.inventory.service.MetricAlertRule;
import io.leitstand.inventory.service.MetricAlertRuleRevisions;
import io.leitstand.inventory.service.MetricAlertRules;

@Dependent
public class MetricAlertRuleManager {

	private static final Logger LOG = Logger.getLogger(MetricAlertRuleManager.class.getName());
	
	private Repository repository;

	private DatabaseService database;
	
	

	
	protected MetricAlertRuleManager() {
		//CDI
	}
	
	@Inject
	protected MetricAlertRuleManager(@Inventory Repository repository,
									@Inventory DatabaseService database) {
		this.repository = repository;
		this.database = database;
	}
	

	public void removeAlertRule(Metric metric, AlertRuleName ruleName) {
		Metric_AlertRule rule = repository.execute(findAlertRule(metric,ruleName));
		if(rule != null) {
			// TODO LOg
			repository.lock(metric, OPTIMISTIC_FORCE_INCREMENT);
			repository.execute(Metric_AlertRule_Definition.removeAllRevisions(rule));
			repository.remove(rule);
		}
		
	}

	public void removeAlertRuleRevision(Metric metric,
										AlertRuleName ruleName, 
									    Date dateModified) {
		
		Metric_AlertRule_Definition rule = repository.execute(findAlertRuleDefinition(metric,ruleName,dateModified));
		
		if(rule != null) {
			// Increment metric modification counter.
			repository.lock(metric, OPTIMISTIC_FORCE_INCREMENT);
			//TODO LOG
			repository.remove(rule);
		}
		
	}

	public boolean storeAlertRule(Metric metric,
								  AlertRule rule) {
		
		if(rule.getRuleState() == SUPERSEDED) {
			LOG.fine(() -> format("%s: Rule state of rule %s of metric %s cannot be set to SUPERSEDED. SUPERSEDED state is set implicitly when adding new rule revisions.",
								  IVT2008E_ALERT_RULE_INVALID_STATE.getReasonCode(),
								  rule.getRuleName(),
								  metric.getMetricName()));		
			throw new UnprocessableEntityException(IVT2008E_ALERT_RULE_INVALID_STATE, 
												   metric.getMetricName(), 
												   rule.getRuleName());
		}
		
		if(rule.getRuleState() == ACTIVE && rule.getRuleDefinition() == null) {
			LOG.fine(()->format("%s: Rule definition for rule %s of metric %s is required and must not be omitted.",
								IVT2006E_ALERT_RULE_DEFINITION_REQUIRED.getReasonCode(),
								metric.getMetricName(),
								rule.getRuleName()));
			
			throw new UnprocessableEntityException(IVT2006E_ALERT_RULE_DEFINITION_REQUIRED, 
												   metric.getMetricName(),
												   rule.getRuleName());
		}
		

		
		
		Metric_AlertRule alertRule = repository.execute(findAlertRule(metric, rule.getRuleName()));
		if(alertRule == null) {
			alertRule = new Metric_AlertRule(metric, rule.getRuleName());
			alertRule.setRuleName(rule.getRuleName());
			alertRule.setCategory(rule.getCategory());
			alertRule.setDescription(rule.getDescription());
			repository.add(alertRule);
			Metric_AlertRule_Definition definition = new Metric_AlertRule_Definition(alertRule);
			populateAlertRuleRevision(rule, definition);
			repository.add(definition);
			return true;
		}
		
		alertRule.setRuleName(rule.getRuleName());
		alertRule.setCategory(rule.getCategory());
		alertRule.setDescription(rule.getDescription());
		
		Metric_AlertRule_Definition latest = repository.execute(Metric_AlertRule_Definition.findLatestDefinition(alertRule));
		if(latest.isCandidate()) {
			populateAlertRuleRevision(rule, latest);
			return false;
		}
		
		if(rule.getRuleState() == CANDIDATE) {
			Metric_AlertRule_Definition definition = new Metric_AlertRule_Definition(alertRule);
			populateAlertRuleRevision(rule, definition);
			repository.add(definition);
			return true;
		}
		
		if(latest.isActive()) {
			if(isDifferent(rule.getRuleDefinition(), latest.getRuleDefinition())) {
				latest.setRuleState(SUPERSEDED);
				Metric_AlertRule_Definition definition = new Metric_AlertRule_Definition(alertRule);
				populateAlertRuleRevision(rule, definition);
				repository.add(definition);
				return true;
			} 
			populateAlertRuleRevision(rule, latest);
			return false;
		}

		LOG.fine(() -> format("%s: Rule %s of metric %s is immutable and cannot be modified!",
							  IVT2007E_ALERT_RULE_IMMUTABLE.getReasonCode(),
							  metric.getMetricName(),
							  rule.getRuleName()));
		throw new ConflictException(IVT2007E_ALERT_RULE_IMMUTABLE, 
									metric.getMetricName(),
									rule.getRuleName());
	}

	private void populateAlertRuleRevision(AlertRule rule, Metric_AlertRule_Definition definition) {
		definition.setRuleId(rule.getRuleId());
		definition.setRuleType(rule.getRuleType());
		definition.setRuleState(rule.getRuleState());
		definition.setRuleDefinition(rule.getRuleDefinition());
	}

	public MetricAlertRule getMetricAlertRule(Metric metric, 
											  AlertRuleName ruleName, 
											  Date dateModified) {
		Metric_AlertRule_Definition rule = repository.execute(Metric_AlertRule_Definition.findAlertRuleDefinition(metric, ruleName, dateModified));
		if(rule == null) {
			//TODO Log
			throw new EntityNotFoundException(IVT2003I_ALERT_RULE_REVISION_NOT_FOUND, metric.getMetricName(), ruleName, dateModified);
		}
		
		return newMetricAlertRule()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withAlertRule(newAlertRule()
					   		  .withRuleId(rule.getRuleId())
					   		  .withRuleName(rule.getRuleName())
					   		  .withRuleState(rule.getRuleState())
					   		  .withCategory(rule.getCategory())
					   		  .withDescription(rule.getDescription())
					   		  .withRuleType(rule.getRuleType())
					   		  .withRuleDefinition(rule.getRuleDefinition())
					   		  .withDateModified(rule.getDateModified()))
			   .build();
	}

	public MetricAlertRuleRevisions getMetricAlertRuleRevisions(Metric metric, 
																AlertRuleName ruleName) {
		
		Metric_AlertRule rule = repository.execute(findAlertRule(metric,ruleName));
		if(rule == null) {
			//TODO LOG
			throw new EntityNotFoundException(IVT2000E_ALERT_RULE_NOT_FOUND, metric.getMetricName(), ruleName);
		}
		
		List<AlertRuleRevisionInfo> revisions = repository.execute(findAlertRuleDefinitions(metric, ruleName))
													  	  .stream()
													  	  .map(revision -> newAlertRuleRevisionInfo()
															  		   	   .withRuleId(revision.getRuleId())
															  		   	   .withRuleState(revision.getRuleState())
															  		   	   .withRuleType(revision.getRuleType())
															  		   	   .withDateModified(revision.getDateModified())
															  		   	   .build())
													  	  .collect(toList());
		
		return newMetricAlertRuleRevisions()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withRule(newAlertRuleRevisions()
					   	 .withRuleName(ruleName)
					   	 .withCategory(rule.getCategory())
					   	 .withDescription(rule.getDescription())
					   	 .withRevisions(revisions)) 
			   .build();
	}

	public MetricAlertRules getMetricAlertRules(Metric metric) {
		List<AlertRuleInfo> rules = database.executeQuery(prepare("WITH history AS ("+ 
																  "SELECT d.metric_alertrule_id, max(d.tsmodified) AS tsmodified "+
														   		  "FROM inventory.metric_alertrule_definition d "+
														   		  "JOIN inventory.metric_alertrule r "+
														   		  "ON d.metric_alertrule_id = r.id "+
														   		  "WHERE r.metric_id = ? "+
														   		  "GROUP BY d.metric_alertrule_id ) "+
														   		  "SELECT r.name, r.category, r.description, d.state, d.type, d.creator, d.tsmodified "+
														   		  "FROM inventory.metric_alertrule r "+
														   		  "JOIN history h "+
														   		  "ON r.id = h.metric_alertrule_id "+
														   		  "JOIN inventory.metric_alertrule_definition d "+
														   		  "ON h.metric_alertrule_id = d.metric_alertrule_id "+
														   		  "AND h.tsmodified = d.tsmodified "+
														   		  "ORDER BY r.name",
														   		  metric.getId()),
															rs -> newAlertRuleInfo()
																  .withRuleName(AlertRuleName.valueOf(rs.getString(1)))
																  .withCategory(rs.getString(2))
																  .withDescription(rs.getString(3))
																  .withRuleState(AlertRuleState.valueOf(rs.getString(4)))
																  .withRuleType(rs.getString(5))
																  .withCreator(rs.getString(6))
																  .withDateModified(rs.getDate(7))
																  .build());
		return newMetricAlertRules()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withRules(rules).build();
	}

	public MetricAlertRule getMetricAlertRule(AlertRuleId ruleId) {
		Metric_AlertRule_Definition rule = repository.execute(findAlertRuleDefinition(ruleId));
		if(rule == null) {
			LOG.fine(()->format("%s: No rule with rule ID %s found.",
							    IVT2000E_ALERT_RULE_NOT_FOUND.getReasonCode(),
							    ruleId));
			throw new EntityNotFoundException(IVT2000E_ALERT_RULE_NOT_FOUND,ruleId);
		}
		return newMetricAlertRule()
			   .withMetricId(rule.getMetric().getMetricId())
			   .withMetricName(rule.getMetric().getMetricName())
			   .withMetricScope(rule.getMetric().getMetricScope())
			   .withDisplayName(rule.getMetric().getDisplayName())
			   .withAlertRule(newAlertRule()
					   		  .withRuleId(rule.getRuleId())
					   		  .withRuleName(rule.getRuleName())
					   		  .withRuleState(rule.getRuleState())
					   		  .withRuleType(rule.getRuleType())
					   		  .withRuleDefinition(rule.getRuleDefinition())
					   		  .withCategory(rule.getCategory())
					   		  .withCreator(rule.getCreator())
					   		  .withDescription(rule.getDescription())
					   		  .withDateModified(rule.getDateModified()))
			   .build();
	}

	public void removeAlertRuleRevision(AlertRuleId ruleId) {
		Metric_AlertRule_Definition rule = repository.execute(findAlertRuleDefinition(ruleId));
		if(rule != null) {
			repository.remove(rule);
		}
	}
	
}
