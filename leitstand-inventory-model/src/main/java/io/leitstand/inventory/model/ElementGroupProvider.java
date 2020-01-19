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

import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0100E_GROUP_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;

@Dependent
public class ElementGroupProvider {
	
	private static final Logger LOG = Logger.getLogger(ElementGroupProvider.class.getName());
	
	private Repository repository;
	
	@Inject
	public ElementGroupProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	
	protected ElementGroupProvider() {
		// CDI
	}

	public ElementGroup tryFetchElementGroup(ElementGroupId groupId) {
		return repository.execute(findElementGroupById(groupId));
	}
	
	public ElementGroup tryFetchElementGroup(ElementGroupType groupType,
											 ElementGroupName groupName) {
		return repository.execute(findElementGroupByName(groupType, 
								  						 groupName));

		
	}
	
	public ElementGroup fetchElementGroup(ElementGroupId groupId){
		ElementGroup group = tryFetchElementGroup(groupId);
		if(group == null){
			LOG.fine(() -> format("%s: Element group %s does not exist.",
					 			  IVT0100E_GROUP_NOT_FOUND.getReasonCode(),
					 			  groupId));
			throw new EntityNotFoundException(IVT0100E_GROUP_NOT_FOUND,groupId);
		}
		return group;
	}
	
	public ElementGroup fetchElementGroup(ElementGroupType groupType,
								   		  ElementGroupName groupName){
		ElementGroup group = repository.execute(findElementGroupByName(groupType,
																	   groupName));
		if(group == null){
			LOG.fine(() -> format("%s: Element group %s of type %s does not exist.",
								  IVT0100E_GROUP_NOT_FOUND.getReasonCode(),
								  groupType,
								  groupName));
			throw new EntityNotFoundException(IVT0100E_GROUP_NOT_FOUND,
											  groupType,
											  groupName);
		}
		return group;	
	}
}
