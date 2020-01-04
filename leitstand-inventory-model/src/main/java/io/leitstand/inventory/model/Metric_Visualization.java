/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.json.SerializableJsonObject.serializable;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.VisualizationConfigIdConverter;
import io.leitstand.inventory.jpa.VisualizationConfigNameConverter;
import io.leitstand.inventory.service.VisualizationConfigId;
import io.leitstand.inventory.service.VisualizationConfigName;

@Entity
@Table(schema="inventory", 
	   name="metric_visualization",
	   uniqueConstraints=@UniqueConstraint(columnNames= {"metric_id","name"}))
@NamedQuery(name="Metric_Visualization.findMetricVisualizations",
			query="SELECT v FROM Metric_Visualization v WHERE v.metric=:metric")
@NamedQuery(name="Metric_Visualization.findMetricVisualizationById",
			query="SELECT v FROM Metric_Visualization v WHERE v.uuid=:uuid")
@NamedQuery(name="Metric_Visualization.findMetricVisualizationByName",
			query="SELECT v FROM Metric_Visualization v WHERE v.metric=:metric AND v.name=:name")
@NamedQuery(name="Metric_Visualization.removeAll",
			query="DELETE FROM Metric_Visualization v WHERE v.metric=:metric")
public class Metric_Visualization implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static Query<List<Metric_Visualization>> findMetricVisualizations(Metric metric){
		return em -> em.createNamedQuery("Metric_Visualization.findMetricVisualizations", Metric_Visualization.class)
					   .setParameter("metric",metric)
					   .getResultList();
	}

	
	public static Query<Metric_Visualization> findMetricVisualization(Metric metric, VisualizationConfigName name){
		return em -> em.createNamedQuery("Metric_Visualization.findMetricVisualizationByName", Metric_Visualization.class)
					   .setParameter("metric", metric)
					   .setParameter("name", name)
					   .getSingleResult();
	}
	
	public static Query<Metric_Visualization> findMetricVisualization(VisualizationConfigId id){
		return em -> em.createNamedQuery("Metric_Visualization.findMetricVisualizationById", Metric_Visualization.class)
					   .setParameter("uuid", id)
					   .getSingleResult();
	}
	

	public static Update removeMetricVisualizations(Metric metric) {
		return em -> em.createNamedQuery("Metric_Visualization.removeAll",Metric_Visualization.class)
					   .setParameter("metric",metric)
					   .executeUpdate();
	}

	@Id
	@Convert(converter=VisualizationConfigIdConverter.class)
	private VisualizationConfigId uuid;
	
	@JoinColumn(name="metric_id")
	private Metric metric;
	@Convert(converter=VisualizationConfigNameConverter.class)
	private VisualizationConfigName name;
	private String type;
	private String category;
	private String description;
	private SerializableJsonObject config;

	protected Metric_Visualization() {
		// JPA
	}
	
	public Metric_Visualization(Metric metric, 
							    VisualizationConfigId configId) {
		this.metric = metric;
		this.uuid = configId;
	}

	
	public Metric_Visualization(Metric metric, 
								VisualizationConfigId configId,
								VisualizationConfigName name) {
		this.metric = metric;
		this.uuid = configId;
		this.name = name;
	}


	public VisualizationConfigId getVisualizationId() {
		return uuid;
	}
	
	public VisualizationConfigName getVisualizationName() {
		return name;
	}
	
	public void setVisualizationName(VisualizationConfigName name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public JsonObject getVisualizationConfig() {
		return config;
	}
	
	public void setVisualizationConfig(JsonObject config) {
		this.config = serializable(config);
	}
	
	public void setVisualizationType(String type) {
		this.type = type;
	}
	
	public String getVisualizationType() {
		return type;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	protected Metric getMetric() {
		return metric;
	}




}
