/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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