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
import static io.leitstand.inventory.service.ModuleData.newModuleData;

import java.util.Date;

import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;

final class ModuleDataMother {

	
	static ModuleData testModule(String testName) {
		return newModuleData()
			   .withAdministrativeState(ACTIVE)
			   .withAssetId("asset-id")
			   .withDescription("Module description")
			   .withFieldReplaceableUnit(true)
			   .withFirmwareRevision("firmware-rev")
			   .withHardwareRevision("hardware-rev")
			   .withManufacturerName("mfg-name")
			   .withManufacturingDate(new Date())
			   .withModelName("model-name")
			   .withModuleName(ModuleName.valueOf(testName))
			   .withModuleClass("UNIT_TEST")
			   .withSerialNumber("s-e-r-i-a-l "+testName)
			   .withSoftwareRevision("software-rev")
			   .withVendorType("vendor-type")
			   .build();
	}
	
	static ModuleData testModule(ModuleData parent,String testName) {
		return newModuleData()
			   .withAdministrativeState(ACTIVE)
			   .withAssetId("asset-id")
			   .withDescription("Module description")
			   .withFieldReplaceableUnit(true)
			   .withFirmwareRevision("firmware-rev")
			   .withHardwareRevision("hardware-rev")
			   .withManufacturerName("mfg-name")
			   .withManufacturingDate(new Date())
			   .withModelName("model-name")
			   .withModuleName(ModuleName.valueOf(testName))
			   .withModuleClass("UNIT_TEST")
			   .withSerialNumber("s-e-r-i-a-l "+testName)
			   .withSoftwareRevision("software-rev")
			   .withVendorType("vendor-type")
			   .withLocation("10")
			   .withParentModule(parent.getModuleName())
			   .build();
	}
	
	private ModuleDataMother() {
		// No instances allowed
	}
}
