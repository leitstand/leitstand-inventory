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

import static java.util.Collections.unmodifiableList;
import static javax.persistence.CascadeType.ALL;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.leitstand.commons.model.AbstractEntity;
import io.leitstand.commons.model.Query;
import io.leitstand.inventory.jpa.RackNameConverter;
import io.leitstand.inventory.service.RackName;

@Entity
@Table(schema="inventory", name="elementgroup_rack")
@NamedQuery(name = "ElementGroup_Rack.findByRackName",
			query = "SELECT r FROM ElementGroup_Rack r WHERE r.group=:group AND r.rackName=:rackName")
@NamedQuery(name = "ElementGroup_Rack.findRacksOfGroup",
		    query="SELECT r FROM ElementGroup_Rack r WHERE r.group=:group")	
public class ElementGroup_Rack extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	
	public static Query<ElementGroup_Rack> findByRackName(ElementGroup group, RackName rackName) {
		return em -> em.createNamedQuery("ElementGroup_Rack.findByRackName",ElementGroup_Rack.class)
					   .setParameter("group",group)
					   .setParameter("rackName", rackName)
					   .getSingleResult();
	}

	public static Query<List<ElementGroup_Rack>> findRacksOfGroup(ElementGroup group) {
		return em -> em.createNamedQuery("ElementGroup_Rack.findRacksOfGroup",ElementGroup_Rack.class)
					     .setParameter("group", group)
					   .getResultList();
	}
	
	@JoinColumn(name="elementgroup_id")
	@ManyToOne
	private ElementGroup group;

	@Column(name="name")
	@Convert(converter=RackNameConverter.class)
	private RackName rackName;
	
	private String location;
	private String description;
	private int units;
	
  	@OneToMany(cascade=ALL, orphanRemoval=true)
  	@JoinColumn(name="rack_id", referencedColumnName="id")
  	private List<ElementGroup_Rack_Element> elements;
	
	protected ElementGroup_Rack() {
		// JPA
	}
	
	public ElementGroup_Rack(ElementGroup group, 
				RackName rackName) {
		this.group = group;
		this.rackName = rackName;
  		this.elements = new LinkedList<>();
	}
	
	public ElementGroup getGroup() {
		return group;
	}
	
	public RackName getRackName() {
		return rackName;
	}
	
	public void setRackName(RackName rackName) {
		this.rackName = rackName;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public int getUnits() {
		return units;
	}
	
	public void setUnits(int units) {
		this.units = units;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	protected void addElement(ElementGroup_Rack_Element element) {
  		this.elements.add(element);
	}
	
	protected void removeElement(ElementGroup_Rack_Element element) {
  		this.elements.remove(element);
	}
	
	public List<ElementGroup_Rack_Element> getElements(){
  		return unmodifiableList(this.elements);
	}

	public void setGroup(ElementGroup group) {
		this.group = group;
	}

	
}
