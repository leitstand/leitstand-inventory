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
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static java.lang.String.format;

import java.util.concurrent.atomic.AtomicInteger;

import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;

public class ElementGroupSettingsMother {
	
	private static AtomicInteger SEQUENCE = new AtomicInteger(0);
	
	public static ElementGroupSettings newTestElementGroupWithoutElementGroupId() {
		return newElementGroupSettings()
				   .withGroupName(unique_group_name())
				   .withGroupType(groupType("junit"))
				   .build();	
	}

	private static ElementGroupName unique_group_name() {
		return new ElementGroupName(format("UNIT-POD-%d",SEQUENCE.incrementAndGet()));
	}
	
	public static ElementGroupSettings newTestElementGroup(){
		return newElementGroupSettings()
			   .withGroupId(randomGroupId())
			   .withGroupName(unique_group_name())
			   .withGroupType(groupType("junit"))
			   .build();
	}
	
	public static ElementGroupSettings.Builder updateSettings(ElementGroupSettings group){
		return newElementGroupSettings()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withDescription(group.getDescription());
	}
	
}
