/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
