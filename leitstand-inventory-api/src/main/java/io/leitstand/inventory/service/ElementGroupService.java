/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.List;

import javax.validation.Valid;

public interface ElementGroupService {

	List<ElementGroupSettings> findGroups(ElementGroupType groupType,
										  String filter, 
										  int offset, 
										  int items);

	List<ElementGroupStatistics> getGroupStatistics(@Valid ElementGroupType groupType, 
													String filter);



}
