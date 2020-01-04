/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.jpa.BooleanConverter;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.service.ElementPlatformInfo;
import io.leitstand.inventory.service.PlatformId;

@Entity
@Table(schema="inventory",
	   name="platform",
	   uniqueConstraints=@UniqueConstraint(columnNames= {"vendor","model"}))
@NamedQueries({
	@NamedQuery(name="Platform.findByPlatformId", 
				query="SELECT p FROM Platform p WHERE p.uuid=:uuid"),
	@NamedQuery(name="Platform.findByVendor", 
				query="SELECT p FROM Platform p WHERE p.vendor=:vendor"),
	@NamedQuery(name="Platform.findByVendorAndModel", 
				query="SELECT p FROM Platform p WHERE p.vendor=:vendor AND p.model=:model"),
	@NamedQuery(name="Platform.findByElementGroupAndElementRole", 
				query="SELECT p FROM Element e JOIN e.platform p WHERE e.group=:group AND e.role=:role "),
	@NamedQuery(name="Platform.findAll", 
				query="SELECT p FROM Platform p"),
	@NamedQuery(name="Platform.countElements",
				query="SELECT count(e) FROM Element e WHERE e.platform=:platform")
})
public class Platform extends VersionableEntity{

	private static final long serialVersionUID = 1L;
	
	public static Query<Long> countElements(Platform platform){
		return em -> em.createNamedQuery("Platform.countElements",Long.class)
					.setParameter("platform", platform)
					.getSingleResult();
	}

	public static Query<List<Platform>> findAll() {
		return em -> em.createNamedQuery("Platform.findAll",Platform.class)
					   .getResultList();
	}
	
	public static Query<Platform> findByVendor(ElementPlatformInfo vendor){
		return em -> em.createNamedQuery("Platform.findByVendorAndModel",Platform.class)
					   .setParameter("vendor",vendor.getVendorName())
					   .setParameter("model",vendor.getModelName())
					   .getSingleResult();
	}
	
	public static Query<Platform> findByModel(String vendor, String model){
		return em -> em.createNamedQuery("Platform.findByVendorAndModel",Platform.class)
					   .setParameter("vendor",vendor)
					   .setParameter("model",model)
					   .getSingleResult();
	}
	
	public static Query<List<Platform>> findByElementGroupAndElementRole(ElementGroup group, ElementRole role){
		return em -> em.createNamedQuery("Platform.findByElementGroupAndElementRole",Platform.class)
					   .setParameter("group", group)
					   .setParameter("role", role)
					   .getResultList();
	}
	
	public static Query<Platform> findByPlatformId(PlatformId platformId){
		return em -> em.createNamedQuery("Platform.findByPlatformId", Platform.class)
					   .setParameter("uuid", platformId.toString())
					   .getSingleResult();
	}
	
	public static Query<List<Platform>> findByVendor(String vendor){
		return em -> em.createNamedQuery("Platform.findByVendor",Platform.class)
					   .setParameter("vendor",vendor)
					   .getResultList();
	}
	
	private String model; // AS7712-23X
	private String vendor; //EdgeCore
	private String description;	
	private int rackUnits;
	@Convert(converter=BooleanConverter.class)
	private boolean halfRack;

	protected Platform() {
		// JPA
	}
	
	public Platform(PlatformId platformId, String vendor, String model) {
		super(platformId.toString());
		this.vendor = vendor;
		this.model = model;
		this.rackUnits = 1;
	}
	
	public String getVendor() {
		return vendor;
	}
	
	public String getModel() {
		return model;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	void setVendor(String vendorName) {
		this.vendor = vendorName;
	}
	
	void setModel(String modelName) {
		this.model = modelName;
	}

	void setRackUnits(int rackUnits) {
		this.rackUnits = rackUnits;
	}
	
	public int getHeight() {
		return this.rackUnits;
	}
	
	public void setHalfRack(boolean halfRack) {
		this.halfRack = halfRack;
	}
	
	public boolean isHalfRackSize() {
		return this.halfRack;
	}

	public PlatformId getPlatformId() {
		return PlatformId.valueOf(getUuid());
	}

}
