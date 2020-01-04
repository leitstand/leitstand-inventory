/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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