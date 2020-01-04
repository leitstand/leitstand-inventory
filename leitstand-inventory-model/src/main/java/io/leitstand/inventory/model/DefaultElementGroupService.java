/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementGroupService;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupStatistics;
import io.leitstand.inventory.service.ElementGroupType;

@Service
public class DefaultElementGroupService implements ElementGroupService {

	private ElementGroupManager manager;
	
	
	@Inject
	protected DefaultElementGroupService(ElementGroupManager inventory) {
		this.manager = inventory;
	}
	
	protected DefaultElementGroupService() {
		// CDI
	}
	
	@Override
	public List<ElementGroupSettings> findGroups(ElementGroupType groupType, 
												 String filter, 
												 int offset, 
												 int items) {
		return manager.findGroups(groupType,
								  filter, 
								  offset, 
								  items);
	}

	@Override
	public List<ElementGroupStatistics> getGroupStatistics(@Valid ElementGroupType groupType, 
														   String filter) {
		return manager.getGroupStatistics(groupType,
										  filter);
	}

	
}