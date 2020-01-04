/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
	public void remove(ElementGroupId id) {
		ElementGroup group = groups.tryFetchElementGroup(id);
		if(group != null) {
			inventory.removeElementGroup(group);
		}
	}

	@Override
	public void remove(ElementGroupType groupType,
					   ElementGroupName groupName) {
		ElementGroup group = groups.tryFetchElementGroup(groupType,
														 groupName);
		if(group != null) {
			inventory.removeElementGroup(group);
		}
	}

}