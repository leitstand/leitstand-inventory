/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.json.SerializableJsonObject.serializable;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.json.JsonObject;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.jpa.SerializableJsonObjectConverter;
import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.EnvironmentIdConverter;
import io.leitstand.inventory.jpa.EnvironmentNameConverter;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;

@Table(schema="inventory", 
	   name="element_env", 
	   uniqueConstraints=@UniqueConstraint(columnNames= {"element_id","name"}))
@Entity
@NamedQuery(name="Element_Environment.findEnvironmentById",
			query="SELECT e FROM Element_Environment e WHERE e.uuid=:uuid")
@NamedQuery(name="Element_Environment.findEnvironmentByName",
			query="SELECT e FROM Element_Environment e WHERE e.element=:element AND e.name=:name")
@NamedQuery(name="Element_Environment.findEnvironments",
			query="SELECT e FROM Element_Environment e WHERE e.element=:element")
@NamedQuery(name="Element_Environment.removeAll",
			query="DELETE FROM Element_Environment e WHERE e.element=:element")
public class Element_Environment implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Update removeEnvironments(Element element) {
		return em -> em.createNamedQuery("Element_Environment.removeAll",int.class)
					   .setParameter("element",element)
					   .executeUpdate();
	}
	
	public static Query<Element_Environment> findEnvironmentById(EnvironmentId id){
		return em -> em.createNamedQuery("Element_Environment.findEnvironmentById",Element_Environment.class)
					   .setParameter("uuid",id)
					   .getSingleResult();
	}
	
	public static Query<Element_Environment> findEnvironmentByName(Element element, EnvironmentName name){
		return em -> em.createNamedQuery("Element_Environment.findEnvironmentByName",Element_Environment.class)
					   .setParameter("element",element)
					   .setParameter("name",name)
					   .getSingleResult();
	}

	public static Query<List<Element_Environment>> findEnvironments(Element element){
		return em -> em.createNamedQuery("Element_Environment.findEnvironments",Element_Environment.class)
					   .setParameter("element",element)
					   .getResultList();
	}

	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;

	@Id
	@Convert(converter=EnvironmentIdConverter.class)
	private EnvironmentId uuid;
	
	@Convert(converter=EnvironmentNameConverter.class)
	private EnvironmentName name;
	
	private String category;
	
	private String type;
	
	private String description;
	
	@Basic(fetch=LAZY)
	@Convert(converter=SerializableJsonObjectConverter.class)
	private SerializableJsonObject variables;
	
	@Temporal(TIMESTAMP)
	private Date tsmodified;
	
	protected Element_Environment() {
		//JPA
	}
	
	protected Element_Environment(Element element, EnvironmentId id, EnvironmentName name)	{
		this.element = element;
		this.uuid = id;
		this.name = name;
		this.tsmodified = new Date();
	}
	
	protected Element_Environment(Element element, EnvironmentName name) {
		this(element,randomEnvironmentId(),name);
	}
	
	@PreUpdate
	protected void updateDateModified() {
		this.tsmodified = new Date();
	}
	
	public EnvironmentId getEnvironmentId() {
		return uuid;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setEnvironmentName(EnvironmentName name) {
		this.name = name;
	}
	
	public EnvironmentName getEnvironmentName() {
		return name;
	}
	
	public void setVariables(JsonObject variables) {
		this.variables = serializable(variables);
	}
	
	public SerializableJsonObject getVariables() {
		return variables;
	}
	
	public Date getDateModified() {
		return new Date(tsmodified.getTime());
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	
}
