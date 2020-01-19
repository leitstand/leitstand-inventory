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

import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.jpa.BooleanConverter;
import io.leitstand.commons.model.AbstractEntity;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.AdministrativeStateConverter;
import io.leitstand.inventory.jpa.ModuleNameConverter;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ModuleName;

@Entity
@Table(schema="inventory", 
	   name="element_module",
	   uniqueConstraints=@UniqueConstraint(columnNames={"element_id","name"}))
@NamedQuery(name="Element_Module.findModules", 
			query="SELECT m FROM Element_Module m WHERE m.element=:element")
@NamedQuery(name="Element_Module.findModuleBySerialNumber", 
			query="SELECT m FROM Element_Module m WHERE m.element=:element AND m.serialNumber=:serialNumber")
@NamedQuery(name="Element_Module.findModuleByName", 
			query="SELECT m FROM Element_Module m WHERE m.element=:element AND m.name=:moduleName")
@NamedQuery(name="Element_Module.removeParentChild",
			query="UPDATE Element_Module m SET m.parent=null WHERE m.element=:element")
@NamedQuery(name="Element_Module.removeAll",
			query="DELETE FROM Element_Module m WHERE m.element=:element")

public class Element_Module extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	public static Query<List<Element_Module>> findModules(Element element){
		return em -> em.createNamedQuery("Element_Module.findModules", 
										 Element_Module.class)
					   .setParameter("element",element)
					   .getResultList();
	}
	
	public static Query<Element_Module> findModule(Element element, String serialNumber) {
		return em -> em.createNamedQuery("Element_Module.findModuleBySerialNumber", 
										 Element_Module.class)
					   .setParameter("element", element)
					   .setParameter("serialNumber",serialNumber)
					   .getSingleResult();
	}
	
	public static Query<Element_Module> findModule(Element element, ModuleName moduleName) {
		return em -> em.createNamedQuery("Element_Module.findModuleByName", 
										 Element_Module.class)
					   .setParameter("element", element)
					   .setParameter("moduleName",moduleName)
					   .getSingleResult();
	}
	
	public static Update removeModules(Element element) {
		return em -> {
			em.createNamedQuery("Element_Module.removeParentChild",int.class)
			  .setParameter("element", element)
			  .executeUpdate();
			
			return  em.createNamedQuery("Element_Module.removeAll",int.class)
					  .setParameter("element",element)
					  .executeUpdate();
		};
		
	}
	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;

	@Convert(converter=ModuleNameConverter.class)
	private ModuleName name;
	
	@Column(name="module_class")
	private String moduleClass;
	
	@Column(name="serial", unique=true)
	private String serialNumber;
	
	@Column(name="asset_id")
	private String assetId;
	
	@Column(name="hardware_rev")
	private String hardwareRevision;
	
	@Column(name="software_rev")
	private String softwareRevision;
	
	@Column(name="firmware_rev")
	private String firmwareRevision;
	
	@Column(name="mfc_name")
	private String manufacturerName;
	
	@Column(name="vendor_type") 
	private String vendorType;
	
	@Column(name="model_name")
	private String modelName;
	
	@Column(name="location")
	private String location;
	
	@Convert(converter=AdministrativeStateConverter.class)
	@Column(name="adm_state")
	private AdministrativeState administrativeState;

	@Convert(converter=BooleanConverter.class)
	private boolean fru;
	
	@Temporal(TIMESTAMP)
	@Column(name="mfc_date")
	private Date dateManufactured;
	
	private String description;
	
	@ManyToOne(cascade=ALL)
	@JoinColumn(name="parent_id", referencedColumnName="id")
	private Element_Module parent;
	
	protected Element_Module(){
		// JPA
	}
	
	protected Element_Module(Element element, 
							 ModuleName name){
		this.element = element;
		this.name = name;
	}

	public Date getDateManufactured() {
		if(dateManufactured == null) {
			return null;
		}
		return new Date(dateManufactured.getTime());
	}
	
	public void setDateManufactured(Date dateManufactured) {
		this.dateManufactured = dateManufactured;
	}
	
	public Element getElement() {
		return element;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}

	public void setAdministrativeState(AdministrativeState administrativeState) {
		this.administrativeState = administrativeState;
	}

	public ModuleName getModuleName() {
		return name;
	}

	public boolean isActive() {
		return ACTIVE.is(administrativeState);
	}
	
	public String getAssetId() {
		return assetId;
	}
	
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	
	public String getFirmwareRevision() {
		return firmwareRevision;
	}
	
	public void setFirmwareRevision(String firmwareRevision) {
		this.firmwareRevision = firmwareRevision;
	}
	
	public String getHardwareRevision() {
		return hardwareRevision;
	}
	
	public void setHardwareRevision(String hardwareRevision) {
		this.hardwareRevision = hardwareRevision;
	}
	
	public void setSoftwareRevision(String softwareRevision) {
		this.softwareRevision = softwareRevision;
	}
	
	public String getSoftwareRevision() {
		return softwareRevision;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public String getManufacturerName() {
		return manufacturerName;
	}
	
	public void setManufacturerName(String name) {
		this.manufacturerName = name;
	}
	
	public String getVendorType() {
		return vendorType;
	}
	
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	
	public boolean isFieldReplaceableUnit() {
		return fru;
	}
	
	public void setFieldReplaceableUnit(boolean fru) {
		this.fru = fru;
	}

	public String getDescription() {
		return description;
	}

	public Element_Module getParentModule() {
		return parent;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setParentModule(Element_Module parent) {
		this.parent = parent;
	}

	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}
	
	public String getModuleClass() {
		return moduleClass;
	}
	
}
