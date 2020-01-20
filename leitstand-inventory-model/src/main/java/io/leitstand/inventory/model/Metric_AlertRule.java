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

import static javax.persistence.GenerationType.TABLE;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.model.Query;
import io.leitstand.inventory.jpa.AlertRuleNameConverter;
import io.leitstand.inventory.service.AlertRuleName;

@Entity 
@Table(schema="inventory", name="metric_alertrule", uniqueConstraints=@UniqueConstraint(columnNames= {"metric_id","name"}))
@NamedQuery(name="Metric_AlertRule.findByMetric",
			query="SELECT r FROM Metric_AlertRule r WHERE r.metric=:metric")
@NamedQuery(name="Metric_AlertRule.findByMetricAndName",
			query="SELECT r FROM Metric_AlertRule r WHERE r.metric=:metric AND r.name=:name")
public class Metric_AlertRule {
	
	public static Query<List<Metric_AlertRule>> findAlertRules(Metric metric){
		return em -> em.createNamedQuery("Metric_AlertRule.findByMetric",Metric_AlertRule.class)
					   .setParameter("metric",metric)
					   .getResultList();
	}
	
	public static Query<Metric_AlertRule> findAlertRule(Metric metric, 
														AlertRuleName ruleName) {
		
		return em -> em.createNamedQuery("Metric_AlertRule.findByMetricAndName",Metric_AlertRule.class)
					   .setParameter("metric",metric)
					   .setParameter("name", ruleName)
					   .getSingleResult();
	}

	@Id
	@TableGenerator(name = "Entity.Sequence",
				    schema="leitstand",
				    table = "sequence", 
				    initialValue = 1, 
				    allocationSize = 100, 
				    pkColumnName = "name", 
				    pkColumnValue = "id", 
				    valueColumnName = "count")
	@GeneratedValue(strategy=TABLE , generator="Entity.Sequence")
	private Long id;
	private Metric metric;
	@Convert(converter=AlertRuleNameConverter.class)
	private AlertRuleName name; 
	private String category;
	private String description;
	
	protected Metric_AlertRule() {
		// JPA
	}

	public Metric_AlertRule( Metric metric,
							 AlertRuleName ruleName) {
		this.metric = metric;
		this.name = ruleName;
	}
	
	public Long getId() {
		return id;
	}

	public Metric getMetric() {
		return metric;
	}
	
	public void setRuleName(AlertRuleName ruleName) {
		this.name = ruleName;
	}
	
	public AlertRuleName getRuleName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public void setMetric(Metric metric) {
		this.metric = metric;
	}

	


}
