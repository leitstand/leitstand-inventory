/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.json.SerializableJsonObject.serializable;
import static io.leitstand.inventory.service.AlertRuleState.ACTIVE;
import static io.leitstand.inventory.service.AlertRuleState.CANDIDATE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.json.JsonObject;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.AlertRuleIdConverter;
import io.leitstand.inventory.service.AlertRuleId;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.AlertRuleState;

@Entity
@Table(schema="inventory", name="metric_alertrule_definition")
@IdClass(Metric_AlertRule_DefinitionPK.class)
@NamedQuery(name="Metric_AlertRule_Definition.findByNameDateModified",
			query="SELECT r FROM Metric_AlertRule_Definition r WHERE r.rule.metric=:metric AND r.rule.name=:rule AND r.tsmodified=:dateModified")
@NamedQuery(name="Metric_AlertRule_Definition.findByName",
			query="SELECT r FROM Metric_AlertRule_Definition r WHERE r.rule.metric=:metric AND r.rule.name=:rule ORDER BY r.tsmodified DESC")
@NamedQuery(name="Metric_AlertRule_Definition.findByRuleId",
			query="SELECT r FROM Metric_AlertRule_Definition r WHERE r.ruleId=:ruleId")
@NamedQuery(name="Metric_AlertRule_Definition.findLatestDefinition",
		    query="SELECT r,max(r.tsmodified) FROM Metric_AlertRule_Definition r WHERE r.rule=:rule ")
@NamedQuery(name="Metric_AlertRule_Definition.removeAllRevisions",
			query="DELETE FROM Metric_AlertRule_Definition r WHERE r.rule=:rule")
public class Metric_AlertRule_Definition implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static Update removeAllRevisions(Metric_AlertRule rule) {
		return em -> em.createNamedQuery("Metric_AlertRule_Definition.removeAllRevisions",int.class)
					   .setParameter("rule", rule)
					   .executeUpdate();
	}

	public static Query<Metric_AlertRule_Definition> findAlertRuleDefinition(Metric metric, 
																			AlertRuleName ruleName, 
																			Date dateModified){
		return em -> em.createNamedQuery("Metric_AlertRule_Definition.findByNameDateModified", Metric_AlertRule_Definition.class)
					   .setParameter("metric",metric)
					   .setParameter("rule", ruleName)
					   .setParameter("dateModified", dateModified)
					   .getSingleResult();
	}
	
	public static Query<Metric_AlertRule_Definition> findAlertRuleDefinition(AlertRuleId ruleId){
		return em -> em.createNamedQuery("Metric_AlertRule_Definition.findByRuleId", Metric_AlertRule_Definition.class)
				       .setParameter("ruleId",ruleId)
				       .getSingleResult();
	}
	
	public static Query<List<Metric_AlertRule_Definition>> findAlertRuleDefinitions(Metric metric, AlertRuleName ruleName){
		return em -> em.createNamedQuery("Metric_AlertRule_Definition.findByName",Metric_AlertRule_Definition.class)
					   .setParameter("metric", metric)
					   .setParameter("rule", ruleName)
					   .getResultList();
	}

	public static Query<Metric_AlertRule_Definition> findLatestDefinition(Metric_AlertRule alertRule) {
		return em ->(Metric_AlertRule_Definition) em.createNamedQuery("Metric_AlertRule_Definition.findLatestDefinition",Object[].class)
													.setParameter("rule", alertRule)
													.getSingleResult()[0];
		
	}
	
	@Id
	@JoinColumn(name="metric_alertrule_id")
	private Metric_AlertRule rule;
	@Column(unique=true)
	@Convert(converter=AlertRuleIdConverter.class)
	private AlertRuleId ruleId;
	private String type;
	@Enumerated(STRING)
	private AlertRuleState state;
	@Id
	@Temporal(TIMESTAMP)
	private Date tsmodified;
	private String creator;
	@Basic(fetch=LAZY)
	private SerializableJsonObject ruleDefinition;
	
	protected Metric_AlertRule_Definition() {
		// JPA
	}
	
	protected Metric_AlertRule_Definition(Metric_AlertRule rule) {
		this.rule = rule;
		this.tsmodified = new Date();
	}
	
	public AlertRuleId getRuleId() {
		return ruleId;
	}
	
	public void setRuleId(AlertRuleId ruleId) {
		this.ruleId = ruleId;
	}
	
	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getRuleType() {
		return type;
	}
	
	public void setRuleType(String type) {
		this.type = type;
	}
	
	public void setRuleState(AlertRuleState state) {
		this.state = state;
	}
	
	public AlertRuleState getRuleState() {
		return state;
	}
	
	public AlertRuleName getRuleName() {
		return rule.getRuleName();
	}
	
	public String getDescription() {
		return rule.getDescription();
	}
	
	public String getCategory() {
		return rule.getCategory();
	}
	
	public Date getDateModified() {
		return new Date(tsmodified.getTime());
	}
	
	public SerializableJsonObject getRuleDefinition() {
		return ruleDefinition;
	}
	
	public void setRuleDefinition(JsonObject ruleDefinition) {
		this.ruleDefinition = serializable(ruleDefinition);
	}
	
	public Metric getMetric() {
		return rule.getMetric();
	}

	public boolean isActive() {
		return state == ACTIVE;
	}

	public boolean isCandidate() {
		return state == CANDIDATE;
	}




	
}
