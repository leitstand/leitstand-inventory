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
import static io.leitstand.inventory.service.RackId.rackId;
import static java.util.Collections.unmodifiableList;
import static javax.persistence.CascadeType.ALL;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import io.leitstand.commons.jpa.BooleanConverter;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Scalar;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.AdministrativeStateConverter;
import io.leitstand.inventory.jpa.RackNameConverter;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackName;

@Entity
@Table(schema="inventory", name="rack")
@NamedQuery(name = "Rack.findById",
			query = "SELECT r FROM Rack r WHERE r.uuid = :uuid")
@NamedQuery(name = "Rack.findByName",
			query = "SELECT r FROM Rack r WHERE r.name = :name")
@NamedQuery(name = "Rack.findRacksByName",
			query = "SELECT r FROM Rack r WHERE CAST(r.name AS TEXT) REGEXP :name ORDER BY r.name ASC")
@NamedQuery(name = "Rack.findRacksByFacility",
			query = "SELECT r FROM Rack r WHERE r.facility=:facility AND  CAST(r.name AS TEXT) REGEXP :name ORDER BY r.name ASC")

public class Rack extends VersionableEntity {

	private static final long serialVersionUID = 1L;
	
	public static Query<Rack> findRackByName(RackName rackName) {
		return em -> em.createNamedQuery("Rack.findByName",Rack.class)
					   .setParameter("name", rackName)
					   .getSingleResult();
	}
	
	public static Query<Rack> findRackById(RackId rackId) {
		return em -> em.createNamedQuery("Rack.findById",Rack.class)
					   .setParameter("uuid", Scalar.toString(rackId))
					   .getSingleResult();
	}
	
	public static Query<List<Rack>> findRacksByName(String filter) {
		return em -> em.createNamedQuery("Rack.findRacksByName",Rack.class)
					   .setParameter("name", filter)
					   .getResultList();
	}
	
	public static Query<List<Rack>> findRacksByFacility(Facility facility, String filter) {
		return em -> em.createNamedQuery("Rack.findRacksByFacility",Rack.class)
					   .setParameter("facility", facility)
					   .setParameter("name", filter)
					   .getResultList();
	}
	
	public static Query<Integer> countRackItems(Rack rack){
		return null;
	}
	

	@Column(name="name")
	@Convert(converter=RackNameConverter.class)
	private RackName name;
	@Column(name="serial")
	private String serialNumber;
	private String assetId;
	private String type;
	private Facility facility;
	private String location;
	private String description;
	@Convert(converter=AdministrativeStateConverter.class)
	private AdministrativeState admstate;
	private int units;
	@Convert(converter=BooleanConverter.class)
	private boolean ascending;
	
  	@OneToMany(cascade=ALL, orphanRemoval=true)
  	@JoinColumn(name="rack_id", referencedColumnName="id")
  	private List<Rack_Item> items;
	
	protected Rack() {
		// JPA
	}
	
	public Rack(RackId rackId, RackName rackName) {
		super(rackId.toString());
		this.name = rackName;
  		this.items = new LinkedList<>();
	}
	
	public RackId getRackId() {
		return rackId(getUuid());
	}
	
	public RackName getRackName() {
		return name;
	}
	
	public void setRackName(RackName rackName) {
		this.name = rackName;
	}
	
	public Facility getFacility() {
		return facility;
	}
	
	public FacilityId getFacilityId() {
		return optional(facility, Facility::getFacilityId);
	}

	public FacilityType getFacilityType() {
		return optional(facility, Facility::getFacilityType);
	}
	
	public FacilityName getFacilityName() {
		return optional(facility, Facility::getFacilityName);
	}
	
	
	public void setFacility(Facility facility) {
		this.facility = facility;
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

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public String getAssetId() {
		return assetId;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public void setAdministrativeState(AdministrativeState state) {
		this.admstate = state;
	}
	
	public AdministrativeState getAdministrativeState() {
		return admstate;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	protected void addElement(Rack_Item item) {
  		this.items.add(item);
	}
	
	protected void removeElement(Rack_Item item) {
  		this.items.remove(item);
	}
	
	public List<Rack_Item> getItems(){
  		return unmodifiableList(this.items);
	}

	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

}
