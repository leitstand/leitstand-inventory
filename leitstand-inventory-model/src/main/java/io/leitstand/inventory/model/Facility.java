package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.FacilityId.facilityId;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.FacilityNameConverter;
import io.leitstand.inventory.jpa.FacilityTypeConverter;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;
import io.leitstand.inventory.service.Geolocation;

@Entity
@Table(schema="inventory", name="facility")
@NamedQuery(name="Facility.findById",
			query="SELECT f FROM Facility f WHERE f.uuid=:uuid")
@NamedQuery(name="Facility.findByName",
			query="SELECT f FROM Facility f WHERE f.name=:name")
@NamedQuery(name="Facility.findByNamePattern",
			query="SELECT f FROM Facility f WHERE CAST(f.name AS TEXT) REGEXP :name ORDER BY f.name ASC")
@NamedQuery(name="Facility.countGroups",
			query="SELECT count(g) FROM ElementGroup g WHERE g.facility=:facility")
@NamedQuery(name="Facility.countRacks",
			query="SELECT count(r) FROM Rack r WHERE r.facility=:facility")
public class Facility extends VersionableEntity {
	
	private static final long serialVersionUID = 1L;

	public static Query<Facility> findFacilityById(FacilityId id){
		return em -> em.createNamedQuery("Facility.findById",Facility.class)
					   .setParameter("uuid",id.toString())
					   .getSingleResult();
	}
	
	public static Query<Facility> findFacilityByName(FacilityName name){
		return em -> em.createNamedQuery("Facility.findByName",Facility.class)
					   .setParameter("name",name)
					   .getSingleResult();
	}
	
	public static Query<List<Facility>> findFacilityByName(String pattern){
		return em -> em.createNamedQuery("Facility.findByNamePattern",Facility.class)
					   .setParameter("name",pattern)
					   .getResultList();
	}
	
	public static Query<Long> countGroups(Facility facility){
		return em -> em.createNamedQuery("Facility.countGroups",Long.class)
					   .setParameter("facility",facility)
					   .getSingleResult();
	}
	
	public static Query<Long> countRacks(Facility facility){
		return em -> em.createNamedQuery("Facility.countRacks",Long.class)
				   	   .setParameter("facility",facility)
				   	   .getSingleResult();
	}
	
	@Convert(converter=FacilityNameConverter.class)
	private FacilityName name;
	@Convert(converter=FacilityTypeConverter.class)
	private FacilityType type;
	private String category;
	private String description;
	private String location;
	
	@AttributeOverrides({
		@AttributeOverride(name="longitude", column=@Column(name="geolon")),
		@AttributeOverride(name="latitude",column=@Column(name="geolat"))
	})
	private Geolocation geolocation;
	
	protected Facility() {
		// JPA
	}
	
	public Facility(FacilityId id, FacilityType type, FacilityName name) {
		super(id.toString());
		this.name = name;
		this.type = type;
	}
	
	public FacilityId getFacilityId() {
		return facilityId(getUuid());
	}
	
	public FacilityName getFacilityName() {
		return name;
	}
	
	public void setFacilityName(FacilityName name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public FacilityType getFacilityType() {
		return type;
	}
	
	public void setFacilityType(FacilityType type) {
		this.type = type;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setGeolocation(Geolocation geolocation) {
		this.geolocation = geolocation;
	}
	
	public Geolocation getGeolocation() {
		return geolocation;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
