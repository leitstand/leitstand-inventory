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

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementGroupElements;
import io.leitstand.inventory.service.ElementGroupElementsService;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.Plane;

@Service
public class DefaultElementGroupElementsService implements ElementGroupElementsService {
	
	@Inject
	private GroupElementsManager inventory;
	
	@Inject
	private ElementGroupProvider groups;
	
	@Override
	public ElementGroupElements getGroupElements(ElementGroupId id) {
		ElementGroup group = groups.fetchElementGroup(id);
		return inventory.getGroupElements(group,(Plane)null);
	}

	@Override
	public ElementGroupElements getGroupElements(ElementGroupId id, 
												 Plane plane) {
		ElementGroup group = groups.fetchElementGroup(id);
		return inventory.getGroupElements(group, plane);
	}

	@Override
	public ElementGroupElements getGroupElements(ElementGroupType groupType,
												 ElementGroupName groupName) {
		ElementGroup group = groups.fetchElementGroup(groupType,
											   		  groupName);
		return inventory.getGroupElements(group,(Plane)null);
	}

	@Override
	public ElementGroupElements getGroupElements(ElementGroupType groupType,
												 ElementGroupName groupName, 
												 Plane plane) {
		ElementGroup group = groups.fetchElementGroup(groupType,
												      groupName);
		return inventory.getGroupElements(group,plane);
	}

	@Override
	public ElementGroupElements getGroupElements(ElementGroupId groupId, 
												 ElementRoleName elementRole) {
		ElementGroup group = groups.fetchElementGroup(groupId);
		return inventory.getGroupElements(group,elementRole);
	}

	@Override
	public ElementGroupElements getGroupElements(ElementGroupType groupType, 
												 ElementGroupName groupName,
												 ElementRoleName elementRole) {
		ElementGroup group = groups.fetchElementGroup(groupType,
													  groupName);
		return inventory.getGroupElements(group,elementRole);

	}

}
