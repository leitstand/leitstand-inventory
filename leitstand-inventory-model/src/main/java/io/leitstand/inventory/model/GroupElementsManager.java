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

import static io.leitstand.inventory.model.ElementGroup.findPlaneElements;
import static io.leitstand.inventory.service.ElementGroupElements.newElementGroupElements;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupElements;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.Plane;

@Dependent
class GroupElementsManager{	
	
	private static final Logger LOG = Logger.getLogger(GroupElementsManager.class.getName());
	
	private Repository repository;
	
	@Inject
	protected GroupElementsManager(@Inventory Repository repository) {
		this.repository = repository;
	}

	public ElementGroupElements getGroupElements(ElementGroup group, Plane plane) {
		List<ElementSettings> elements = findElementsByPlane(group,plane)
										 .stream()
										 .sorted((a,b) -> a.getElementName().compareTo(b.getElementName()))
										 .map(ElementSettingsManager::settingsOf)
										 .collect(toList());
		
		return groupElements(group, elements);
	}

	private ElementGroupElements groupElements(ElementGroup group, List<ElementSettings> elements) {
		return newElementGroupElements()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(group.getGroupType())
			   .withDescription(group.getDescription())
			   .withElements(elements)
			   .build();
	}

	private List<Element> findElementsByPlane(ElementGroup group, Plane plane) {
		if(plane == null){
			return group.getElements();
		}
		return repository.execute(findPlaneElements(group, plane));
	}

	public ElementGroupElements getGroupElements(ElementGroup group, ElementRoleName elementRole) {
		ElementRole role = repository.execute(ElementRole.findRoleByName(elementRole));
		if(role == null) {
			LOG.fine(() -> format("%s: Element role %s does not exist.",
							 	  IVT0400E_ELEMENT_ROLE_NOT_FOUND.getReasonCode(),
							 	  elementRole));
			throw new EntityNotFoundException(IVT0400E_ELEMENT_ROLE_NOT_FOUND,
											  elementRole);
		}
		
		List<ElementSettings> elements = repository.executeMapListItem(Element.findElementsByGroupAndRole(group, role), 
																	   ElementSettingsManager::settingsOf);
		return groupElements(group, elements);
	}



}
