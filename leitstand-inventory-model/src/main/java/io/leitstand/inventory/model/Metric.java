/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.inventory.service.MetricId.randomMetricId;
import static java.util.Collections.unmodifiableSortedSet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static javax.persistence.EnumType.STRING;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.MetricNameConverter;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricScope;

@Entity
@Table(schema="inventory", name="metric")
@NamedQuery(name="Metric.findById", query="SELECT m FROM Metric m WHERE m.uuid=:id")
@NamedQuery(name="Metric.findByName", query="SELECT m FROM Metric m WHERE m.name=:name")
@NamedQuery(name="Metric.findByNameFilterAndScope", query="SELECT m FROM Metric m WHERE m.name REGEXP :filter AND m.scope=:scope")
@NamedQuery(name="Metric.findByNameFilter", query="SELECT m FROM Metric m WHERE CAST(m.name AS TEXT) REGEXP :filter")
public class Metric extends VersionableEntity{

	private static final long serialVersionUID = 1L;
	

	public static Query<Metric> findMetricById(MetricId metricId) {
		return em -> em.createNamedQuery("Metric.findById",Metric.class)
					   .setParameter("id", MetricId.toString(metricId))
					   .getSingleResult();
	}

	public static Query<Metric> findMetricByName(MetricName name) {
		return em -> em.createNamedQuery("Metric.findByName", Metric.class)
					   .setParameter("name",name)
					   .getSingleResult();
	}
	
	public static Query<List<Metric>> findMetricsByName(String filter,
														MetricScope scope) {
		String pattern = isEmptyString(filter) ? ".*" : filter;
		
		if(scope != null) {
			return em -> em.createNamedQuery("Metric.findByNameFilterAndScope", Metric.class)
						   .setParameter("filter", pattern)
						   .setParameter("scope", scope)
						   .getResultList();
		}
		
		return em -> em.createNamedQuery("Metric.findByNameFilter", Metric.class)
					   .setParameter("filter", pattern)
					   .getResultList();
	}
	
	@Column(unique=true)
	@Convert(converter=MetricNameConverter.class)
	private MetricName name;
	
	@Column
	private String unit;
	
	@Column
	private String displayName;
	
	@Column
	private String description;
	
	@Column
	@Enumerated(STRING)
	private MetricScope scope;
	
	@ManyToMany
	@JoinTable(schema="INVENTORY",
			   name="METRIC_ELEMENTROLE", 
			   joinColumns=@JoinColumn(name="metric_id", referencedColumnName="id"),
			   inverseJoinColumns=@JoinColumn(name="elementrole_id", referencedColumnName="id"))
	@MapKey(name="name")
	private Map<ElementRoleName,ElementRole> roles = new HashMap<>();
	
	public Metric() {
		// JPA
	}
	
	public Metric(MetricName metricName) {
		this(randomMetricId(),metricName);
	}
	
	public Metric(MetricId metricId, MetricName name) {
		super(metricId.toString());
		this.name = name;
	}
	
	public MetricId getMetricId() {
		return MetricId.valueOf(getUuid());
	}
	
	public void setMetricScope(MetricScope scope) {
		this.scope = scope;
	}

	public MetricScope getMetricScope() {
		return scope;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public MetricName getMetricName() {
		return name;
	}
	
	public void setMetricName(MetricName name) {
		this.name = name;
		if(isEmptyString(this.displayName)) {
			this.displayName = MetricName.toString(name);
		}
	}
	
	public void setMetricUnit(String unit) {
		this.unit = unit;
	}
	
	public String getMetricUnit() {
		return unit;
	}
	
	public void setElementRoles(Set<ElementRole> roles) {
		setElementRoles(roles.stream()
							 .collect(toMap(ElementRole::getRoleName,
									 		identity())));
	}
	
	public void setElementRoles(Map<ElementRoleName,ElementRole> roles) {
		this.roles = roles;
	}
	
	public SortedSet<ElementRole> getElementRoles(){
		TreeSet<ElementRole> sortedRoles = new TreeSet<>((a,b) -> a.getRoleName().compareTo(b.getRoleName()));
		sortedRoles.addAll(roles.values());
		return unmodifiableSortedSet(sortedRoles);
	}
	

	public SortedSet<ElementRoleName> getElementRoleNames() {
		return unmodifiableSortedSet(new TreeSet<>(roles.keySet()));
	}

	public String getDisplayName() {
		return displayName;
	}
	
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
}