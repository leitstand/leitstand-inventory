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

import static io.leitstand.commons.model.ObjectUtil.optional;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableSortedSet;
import static javax.persistence.CascadeType.PERSIST;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.ElementGroupNameConverter;
import io.leitstand.inventory.jpa.ElementGroupTypeConverter;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;
import io.leitstand.inventory.service.Plane;

@Entity
@Table(schema="inventory", name="elementgroup")
@NamedQueries({
@NamedQuery(name="ElementGroup.findByGroupId", 
			query="SELECT g FROM ElementGroup g WHERE g.uuid=:id"),
@NamedQuery(name="ElementGroup.findByGroupName",
			query="SELECT g FROM ElementGroup g WHERE g.name=:name AND g.type=:type ORDER BY g.name"),
@NamedQuery(name="ElementGroup.findByGroupNamePattern", 
			query="SELECT g FROM ElementGroup g WHERE CAST(g.name AS TEXT) REGEXP :name AND g.type=:type ORDER BY g.name"),
@NamedQuery(name="ElementGroup.findAll", 
			query="SELECT g FROM ElementGroup g WHERE g.type=:type ORDER BY g.name"),
@NamedQuery(name="ElementGroup.findPlaneElements", 
			query="SELECT e FROM Element e WHERE e.role.plane=:plane AND e.group=:group ORDER BY e.elementName"),
@NamedQuery(name="ElementGroup.findUpdatableElements", 
			query="SELECT e FROM Element e "+
				  "WHERE e.role=:role AND e.group=:group "+
				  "ORDER BY e.elementName"),
@NamedQuery(name="ElementGroup.findGroupsWithElementRoleOnPlatfrom",
		    query="SELECT DISTINCT g FROM ElementGroup g JOIN g.elements e WHERE e.platform=:platform AND e.role=:role")

})
public class ElementGroup extends VersionableEntity {

	private static final long serialVersionUID = 1L;
	
	public static Query<ElementGroup> findElementGroupById(ElementGroupId id){
		return em -> em.createNamedQuery("ElementGroup.findByGroupId",ElementGroup.class)
					   .setParameter("id", id.toString())
					   .getSingleResult();
	}
	
	public static Query<ElementGroup> findElementGroupByName(ElementGroupType groupType, 
															 ElementGroupName groupName){
		return em -> em.createNamedQuery("ElementGroup.findByGroupName",ElementGroup.class)
					   .setParameter("type", groupType)
					   .setParameter("name", groupName)
					   .getSingleResult();
	}
	
	public static Query<List<ElementGroup>> findByElementGroupName(ElementGroupType groupType,
																   String pattern, 
																   int offset, 
																   int limit){
		if(pattern == null || pattern.isEmpty()){
			return findAll(groupType,
						   offset,
						   limit);
		}
		
		return em -> em.createNamedQuery("ElementGroup.findByGroupNamePattern",ElementGroup.class)
					   .setParameter("name", pattern)
					   .setParameter("type", groupType)
					   .setFirstResult(offset)
					   .setMaxResults(100)
					   .getResultList();
	}
	
	public static Query<List<ElementGroup>> findAll(ElementGroupType groupType,
													int offset, 
													int limit){
		return em -> em.createNamedQuery("ElementGroup.findAll",ElementGroup.class)
					   .setParameter("type", groupType)
					   .setMaxResults(limit)
					   .setFirstResult(offset)
					   .getResultList();
	}

	public static Query<List<Element>> findPlaneElements(ElementGroup group, Plane plane){
		return em -> em.createNamedQuery("ElementGroup.findPlaneElements",Element.class)
					   .setParameter("group",group)
					   .setParameter("plane", plane)
					   .getResultList();
	}
	
	public static Query<Set<Element>> findUpdatableElements(ElementGroup group, ElementRole role){
		return em -> new LinkedHashSet<>(em.createNamedQuery("ElementGroup.findUpdatableElements",Element.class)
					   					  .setParameter("group",group)
					   					  .setParameter("role", role)
					   					  .getResultList());
	}
	
	public static Query<Set<ElementGroup>> findGroupsWithElementRoleOnPlatform(ElementRole role, Platform platform){
		return em -> new LinkedHashSet<>(em.createNamedQuery("ElementGroup.findGroupsWithElementRoleOnPlatfrom",ElementGroup.class)
										  .setParameter("role",role)
										  .setParameter("platform", platform)
										  .getResultList());
	}
	
	@Convert(converter=ElementGroupNameConverter.class)
	@Column(length=128, unique=true, nullable=false)
	private ElementGroupName name;

	@Convert(converter=ElementGroupTypeConverter.class)
	@Column(length=128, nullable=false)
	private ElementGroupType type;
	
	@Column(length=1024)
	private String description;
	
	@OneToMany(mappedBy="group", cascade=PERSIST)
	private List<Element> elements;
	
	@ManyToOne
	@JoinColumn(name="facility_id")
	private Facility facility;
	
	
	@ElementCollection
	@CollectionTable(schema="inventory", 
					 name="elementgroup_tag", 
					 joinColumns=@JoinColumn(name="elementgroup_id"))
	@Column(name="tag")
	private Set<String> tags;
	
	protected ElementGroup(){
		// JPA
	}
	
	public ElementGroup(ElementGroupId id,
						ElementGroupType type,
						ElementGroupName name){
		super(id.toString());
		this.type = type;
		this.name = name;
		this.elements = new LinkedList<>();
		this.tags = new TreeSet<String>();
	}
	
	void add(Element elements){
		this.elements.add(elements);
	}
	
	void add(ElementGroup group) {
		elements.addAll(group.elements);
		group.elements.clear();
	}
	
	void remove(Element elements){
		this.elements.remove(elements);
	}
	
	public ElementGroupName getGroupName() {
		return name;
	}
	
	public ElementGroupType getGroupType() {
		return type;
	}
	
	public void setGroupType(ElementGroupType type) {
		this.type = type;
	}
	
	public void addTag(String tag){
		this.tags.add(tag);
	}
	
	public SortedSet<String> getTags(){
		return unmodifiableSortedSet(new TreeSet<>(tags));
	}
	
	public ElementGroupName setElementGroupName(ElementGroupName name) {
		ElementGroupName current = this.name;
		this.name = name;
		return current;
	}
	
	public List<Element> getElements() {
		return unmodifiableList(elements);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ElementGroupId getGroupId() {
		return new ElementGroupId(getUuid());
	}

	public Facility getFacility() {
		return facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}
	
	public void setTags(Set<String> tags) {
		this.tags.clear();
		this.tags.addAll(tags);
	}

	public FacilityId getFacilityId() {
		return optional(facility, Facility::getFacilityId);
	}

	public FacilityName getFacilityName() {
		return optional(facility, Facility::getFacilityName);
	}

    public FacilityType getFacilityType() {
        return optional(facility, Facility::getFacilityType);
    }

	
}
