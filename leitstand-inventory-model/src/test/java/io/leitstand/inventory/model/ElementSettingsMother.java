/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPlatformInfo.newPlatformInfo;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.Plane.DATA;

import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;

public class ElementSettingsMother {
	
	private static final ElementGroupName ELEMENT_SETTINGS_MOTHER_POD_NAME = new ElementGroupName(ElementSettings.class.getName());
	private static final ElementGroupId POD_ID = randomGroupId();
	
	public static ElementSettings elementWithoutElementId() {
		return newElementSettings()
			   .withElementName(elementName("TEST"))
			   .withGroupId(POD_ID)
			   .withGroupName(ELEMENT_SETTINGS_MOTHER_POD_NAME)
			   .withGroupType(groupType("JUNIT"))
			   .withElementRole(new ElementRoleName("JUNIT"))
			   .withPlane(DATA)
			   .withOperationalState(OperationalState.OPERATIONAL)
			   .withAdministrativeState(AdministrativeState.ACTIVE)
			   .build();
	}
	
	public static ElementSettings element() {
		return element("TEST");
	}

	public static ElementSettings element(ElementId elementId) {
		return element(elementId, 
					   "TEST");
	}

	
	public static ElementSettings element(String name){
		return newElementSettings()
			   .withElementId(randomElementId())
			   .withElementName(elementName(name))
			   .withGroupId(POD_ID)
			   .withGroupName(ELEMENT_SETTINGS_MOTHER_POD_NAME)
			   .withGroupType(groupType("JUNIT"))
			   .withElementRole(elementRoleName("JUNIT"))
			   .withPlane(DATA)
			   .withOperationalState(OperationalState.OPERATIONAL)
			   .withAdministrativeState(AdministrativeState.ACTIVE)
			   .withPlatform(newPlatformInfo()
					   		.withVendorName("net.rtbrick")
					   		.withModelName("JUNIT"))
			   .build();
	}
	
	public static ElementSettings element(ElementId elementId, String elementName ) {
		return element(elementId,elementName(elementName));
	}
	
	public static ElementSettings element(ElementId elementId, ElementName elementName){
		return newElementSettings()
			   .withElementId(elementId)
			   .withElementName(elementName)
			   .withGroupId(POD_ID)
			   .withGroupName(ELEMENT_SETTINGS_MOTHER_POD_NAME)
			   .withGroupType(groupType("JUNIT"))
			   .withElementRole(elementRoleName("JUNIT"))
			   .withPlane(DATA)
			   .withOperationalState(OperationalState.OPERATIONAL)
			   .withAdministrativeState(AdministrativeState.ACTIVE)
			   .withPlatform(newPlatformInfo()
					   		.withVendorName("net.rtbrick")
					   		.withModelName("JUNIT"))
			   .build();
	}
	
	
	public static ElementSettings.Builder element(ElementSettings source){
		return newElementSettings()
			   .withElementId(source.getElementId())
			   .withElementName(source.getElementName())
			   .withGroupId(source.getGroupId())
			   .withGroupName(source.getGroupName())
			   .withGroupType(source.getGroupType())
			   .withElementRole(source.getElementRole())
			   .withPlane(source.getPlane())
			   .withOperationalState(source.getOperationalState())
			   .withAdministrativeState(source.getAdministrativeState());
	}
	
	
}
