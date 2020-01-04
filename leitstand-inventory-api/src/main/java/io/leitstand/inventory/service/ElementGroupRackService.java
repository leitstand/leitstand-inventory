/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface ElementGroupRackService {

	ElementGroupRacks findRacks(ElementGroupId groupId);
	ElementGroupRacks findRacks(ElementGroupType groupType, 
								 ElementGroupName groupName);
	ElementGroupRack getRack(ElementGroupId groupId, 
							 RackName rackName);
	ElementGroupRack getRack(ElementGroupType groupType,
							 ElementGroupName groupName, 
							 RackName rackName);
	
	boolean storeRack(ElementGroupId groupId,
					  RackName rackName,
					  RackSettings settings);
	
	boolean storeRack(ElementGroupType groupType,
					  ElementGroupName groupName,
					  RackName rackName,
					  RackSettings settings);
	void removeRack(ElementGroupId groupId, 
					RackName rackName);
	
	void removeRack(ElementGroupType groupType,
					ElementGroupName groupName, 
					RackName rackName);
}
