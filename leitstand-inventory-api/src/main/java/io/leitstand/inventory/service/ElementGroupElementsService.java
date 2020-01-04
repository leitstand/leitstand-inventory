/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface ElementGroupElementsService {

	ElementGroupElements getGroupElements(ElementGroupId groupId);

	ElementGroupElements getGroupElements(ElementGroupId groupid, 
										  Plane plane);

	ElementGroupElements getGroupElements(ElementGroupType groupType,
										  ElementGroupName groupName);

	ElementGroupElements getGroupElements(ElementGroupType groupType,
										  ElementGroupName groupName, 
										  Plane plane);
	
	ElementGroupElements getGroupElements(ElementGroupId groupId, 
										  ElementRoleName elementRole);
	
	ElementGroupElements getGroupElements(ElementGroupType groupType,
										  ElementGroupName groupName,
										  ElementRoleName elementRole);

}