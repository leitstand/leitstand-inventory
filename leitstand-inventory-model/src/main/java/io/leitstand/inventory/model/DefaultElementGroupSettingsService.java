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
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupSettingsService;
import io.leitstand.inventory.service.ElementGroupType;

@Service
public class DefaultElementGroupSettingsService implements ElementGroupSettingsService {
	
	@Inject
	private ElementGroupManager inventory;
	
	@Inject
	private ElementGroupProvider groups;
	
	public DefaultElementGroupSettingsService() {
		// EJB
	}
	
	DefaultElementGroupSettingsService(ElementGroupManager inventory,
									   ElementGroupProvider groups){
		this.inventory = inventory;
		this.groups = groups;
	}
	
	@Override
	public ElementGroupSettings getGroupSettings(ElementGroupId id) {
		ElementGroup group = groups.fetchElementGroup(id);
		return inventory.getGroupSettings(group);
	}

	@Override
	public ElementGroupSettings getGroupSettings(ElementGroupType groupType,
												 ElementGroupName groupName) {
		ElementGroup group = groups.fetchElementGroup(groupType,
											   		  groupName);
		return inventory.getGroupSettings(group);
	}

	@Override
	public boolean storeElementGroupSettings(ElementGroupSettings settings) {
		ElementGroup group = groups.tryFetchElementGroup(settings.getGroupId());
		if(group != null) {
			inventory.storeElementGroupSettings(group, settings);
			return false;
		} 
		inventory.createElementGroup(settings);
		return true;
	}

	@Override
	public void removeElementGroup(ElementGroupId id) {
		ElementGroup group = groups.tryFetchElementGroup(id);
		if(group != null) {
			inventory.removeElementGroup(group);
		}
	}

	@Override
	public void removeElementGroup(ElementGroupType groupType,
					   ElementGroupName groupName) {
		ElementGroup group = groups.tryFetchElementGroup(groupType,
														 groupName);
		if(group != null) {
			inventory.removeElementGroup(group);
		}
	}

}
