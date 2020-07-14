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
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * Hardware module data.
 */
public class ModuleData extends ValueObject {

	/**
	 * Returns a builder to create an immutable <code>ModuleData</code> instance.
	 * @return a builder to create an immutable <code>ModuleData</code> instance.
	 */
	public static Builder newModuleData(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ModuleData</code> instance.
	 */
	public static class Builder {
		
		private ModuleData module = new ModuleData();

		/**
		 * Sets the module name 
		 * @param name the module name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModuleName(ModuleName moduleName){
			assertNotInvalidated(getClass(), module);
			module.moduleName = moduleName;
			return this;
		}
		
		/**
		 * Sets the module class 
		 * @param name the module class
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModuleClass(String moduleClass) {
			assertNotInvalidated(getClass(),module);
			module.moduleClass = moduleClass;
			return this;
		}

		/**
		 * Sets the module serial-number 
		 * @param serialNumber the serial number
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withSerialNumber(String serialNumber){
			assertNotInvalidated(getClass(), module);
			module.serialNumber = serialNumber;
			return this;
		}
		
		/**
		 * Sets the module hardware-revision 
		 * @param hardwareRev the hardware revision
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withHardwareRevision(String hardwareRev){
			assertNotInvalidated(getClass(), module);
			module.hardwareRevision = hardwareRev;
			return this;
		}
		
		/**
		 * Sets the module software-revision
		 * @param softwareRev the software revision
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withSoftwareRevision(String softwareRev){
			assertNotInvalidated(getClass(), module);
			module.softwareRevision = softwareRev;
			return this;
		}
		
		/**
		 * Sets the module firmware-revision
		 * @param hardwareRev the hardware revision
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withFirmwareRevision(String firmwareRev){
			assertNotInvalidated(getClass(), module);
			module.firmwareRevision = firmwareRev;
			return this;
		}

		/**
		 * Sets the module manufacturer-name
		 * @param manufacturerName the manufacturer name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withManufacturerName(String manufacturerName){
			assertNotInvalidated(getClass(), module);
			module.manufacturerName = manufacturerName;
			return this;
		}

		/**
		 * Sets the module manufacturing-date
		 * @param manufacturingDate the manufacturer date
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withManufacturingDate(Date dateManufactured){
			assertNotInvalidated(getClass(), module);
			module.dateManufactured = dateManufactured != null ? new Date(dateManufactured.getTime()) : null;
			return this;
		}
		
		/**
		 * Sets the module vendor-type
		 * @param vendorType the vendor type
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withVendorType(String vendorType){
			assertNotInvalidated(getClass(), module);
			module.vendorType = vendorType;
			return this;
		}

		/**
		 * Sets the module model-name
		 * @param modelName the model name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModelName(String modelName){
			assertNotInvalidated(getClass(), module);
			module.modelName = modelName;
			return this;
		}

		/**
		 * Sets the parent-module name.
		 * @param parent the parent-module name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withParentModule(ModuleName parent){
			assertNotInvalidated(getClass(), module);
			module.parentModule = parent;
			return this;
		}
		
		/**
		 * Sets the location of this module
		 * @param parentRelPos parent relative position
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withLocation(String location){
			assertNotInvalidated(getClass(), module);
			module.location = location;
			return this;
		}

		/**
		 * Sets whether the module is a field-replaceable unit.
		 * @param fru whether this module is field-replaceable.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withFieldReplaceableUnit(boolean fru){
			assertNotInvalidated(getClass(), module);
			module.fieldReplaceableUnit = fru;
			return this;
		}
		
		/**
		 * Sets the administrative state of the module
		 * @param state - the administrative state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAdministrativeState(AdministrativeState state){
			assertNotInvalidated(getClass(), module);
			module.administrativeState = state;
			return this;
		}

		/**
		 * Sets the module description
		 * @param description the module description
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), module);
			module.description = description;
			return this;
		}
		

		public Builder withAssetId(String assetId) {
			assertNotInvalidated(getClass(), module);
			module.assetId = assetId;
			return this;
		}
		
		/**
		 * Returns an immutable <code>FieldReplaceableUnit</Code> instance and invalidates this builder.
		 * Any further interaction with this builder raises an exception.
		 * @return an immutable <code>FieldReplaceableUnit</code> instance.
		 */
		public ModuleData build(){
			try{
				assertNotInvalidated(getClass(), module);
				return module;
			} finally {
				this.module = null;
			}
		}


	}
	
	@NotNull(message="{module_name.required}")
	@Valid
	private ModuleName moduleName;
	private String moduleClass;
	
	private String serialNumber;
	private String assetId;
	@JsonbProperty("hardware_rev")
	private String hardwareRevision;
	@JsonbProperty("software_rev")
	private String softwareRevision;
	@JsonbProperty("firmware_rev")
	private String firmwareRevision;
	private String manufacturerName;
	@JsonbProperty("manufacturing_date")
	private Date dateManufactured;
	private String vendorType;
	private String modelName;
	private String location;
	private ModuleName parentModule;
	@JsonbProperty("fru")
	private boolean fieldReplaceableUnit;
	@JsonbProperty("admstate")
	private AdministrativeState administrativeState;
	private String description;

	public String getSerialNumber() {
		return serialNumber;
	}
	
	public String getAssetId() {
		return assetId;
	}
	
	public String getHardwareRevision() {
		return hardwareRevision;
	}
	
	public String getSoftwareRevision() {
		return softwareRevision;
	}
	
	public String getFirmwareRevision() {
		return firmwareRevision;
	}
	
	public String getManufacturerName() {
		return manufacturerName;
	}
	
	public String getModelName() {
		return modelName;
	}
	
	public ModuleName getModuleName() {
		return moduleName;
	}
	
	public Date getDateManufactured() {
		return dateManufactured;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	public String getVendorType() {
		return vendorType;
	}
	
	public ModuleName getParentModule() {
		return parentModule;
	}
	
	public boolean isFieldReplaceableUnit() {
		return fieldReplaceableUnit;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getModuleClass() {
		return moduleClass;
	}

	public String getLocation() {
		return location;
	}
	
}
