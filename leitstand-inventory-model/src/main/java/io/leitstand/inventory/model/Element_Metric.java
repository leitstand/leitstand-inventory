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

import static io.leitstand.inventory.service.MetricScope.ALL;
import static javax.persistence.CascadeType.PERSIST;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;

@Entity
@Table(schema="inventory", name="element_metric")
@IdClass(Element_MetricPK.class)
@NamedQuery(name="Element_Metric.findByMetricName", 
			query="SELECT m FROM Element_Metric m WHERE m.element=:element AND m.metric.name=:name")
@NamedQuery(name="Element_Metric.findByElement", 
			query="SELECT m FROM Element_Metric m WHERE m.element=:element")
@NamedQuery(name="Element_Metric.findByScope", 
			query="SELECT m FROM Element_Metric m WHERE m.element=:element AND m.metric.scope=:scope")

@NamedQuery(name="Element_Metric.countBindings",
			query="SELECT count(m) FROM Element_Metric m WHERE m.metric=:metric")	
@NamedQuery(name="Element_Metric.removeBindings",
			query="DELETE FROM Element_Metric m WHERE m.metric=:metric")
@NamedQuery(name="Element_Metric.removeAll",
			query="DELETE FROM Element_Metric m WHERE m.element=:element")
public class Element_Metric {

	public static Query<Element_Metric> findElementMetric(Element element, Metric metric) {
		return em -> em.find(Element_Metric.class, 
						     new Element_MetricPK(element,metric));
	}
	
	public static Query<List<Element_Metric>> findElementMetrics(Element element, MetricScope scope){
		if(scope == ALL) {
			return findElementMetrics(element);
		}
		
		return em -> em.createNamedQuery("Element_Metric.findByScope",Element_Metric.class)
					   .setParameter("element",element)
					   .setParameter("scope", scope)
					   .getResultList();
	}

	public static Query<Element_Metric> findElementMetric(Element element, MetricName metric) {
		return em -> em.createNamedQuery("Element_Metric.findByMetricName",Element_Metric.class)
					   .setParameter("element", element)
					   .setParameter("name", metric)
					   .getSingleResult();
	}
	
	public static Query<List<Element_Metric>> findElementMetrics(Element element) {
		return em -> em.createNamedQuery("Element_Metric.findByElement",Element_Metric.class)
				   	   .setParameter("element", element)
				   	   .getResultList();
	}
	
	public static Query<Long> countElementMetricBindings(Metric metric) {
		return em -> em.createNamedQuery("Element_Metric.countBindings",Long.class)
					   .setParameter("metric",metric)
					   .getSingleResult();
	}
	
	public static Update removeElementMetricBindings(Metric metric) {
		return em -> em.createNamedQuery("Element_Metric.removeBindings",int.class)
					   .setParameter("metric",metric)
					   .executeUpdate();
	}
	
	public static Update removeMetrics(Element element) {
		return em -> em.createNamedQuery("Element_Metric.removeAll",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}

	@Id
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	@Id
	@ManyToOne(cascade=PERSIST)
	@JoinColumn(name="metric_id")
	private Metric metric;
	
	public Element_Metric() {
		//JPA
	}
	
	public Element_Metric(Element element, Metric metric) {
		this.element = element;
		this.metric = metric;
	}
	
	public Element getElement() {
		return element;
	}
	
	public String getDescription() {
		return metric.getDescription();
	}

	public MetricName getMetricName() {
		return metric.getMetricName();
	}

	public String getMetricUnit() {
		return metric.getMetricUnit();
	}

	public MetricScope getMetricScope() {
		return metric.getMetricScope();
	}

	public MetricId getMetricId() {
		return metric.getMetricId();
	}

	public String getDisplayName() {
		return metric.getDisplayName();
	}
	
}
