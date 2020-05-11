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

import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;

import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.PlatformId;

public class ElementSettingsMother {
	
	private static final ElementGroupName ELEMENT_SETTINGS_MOTHER_POD_NAME = new ElementGroupName(ElementSettings.class.getName());
	private static final ElementGroupId POD_ID = randomGroupId();
	private static final PlatformId PLATFORM_ID = randomPlatformId();
	
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
			   .withPlatformId(PLATFORM_ID)
			   .withPlatformName(platformName("elementplatform"))
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
			   .withPlatformId(PLATFORM_ID)
			   .withPlatformName(platformName("elementplatform"))
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
