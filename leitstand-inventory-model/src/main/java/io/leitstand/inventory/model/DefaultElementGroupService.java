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
	public List<ElementGroupStatistics> getGroupStatistics(ElementGroupType groupType, 
														   String filter) {
		return manager.getGroupStatistics(groupType,
										  filter);
	}

	
}
