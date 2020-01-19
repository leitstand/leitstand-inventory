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

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.model.ElementGroup.findByElementGroupName;
import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.AdministrativeState.administrativeState;
import static io.leitstand.inventory.service.ElementGroupId.groupId;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementGroupStatistics.newElementGroupStatistics;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.OperationalState.operationalState;
import static io.leitstand.inventory.service.ReasonCode.IVT0101I_GROUP_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0102I_GROUP_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0103E_GROUP_NOT_REMOVABLE;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;
import io.leitstand.inventory.service.ElementGroupStatistics;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.OperationalState;

@Dependent
class ElementGroupManager{	
	private static final Logger LOG = Logger.getLogger(ElementGroupManager.class.getName());
	private Repository repository;
	private DatabaseService db;
	private Messages messages;

	@Inject
	protected ElementGroupManager(@Inventory Repository repository,
								  @Inventory DatabaseService db,
								  Messages messages ) {
		this.repository = repository;
		this.db = db;
		this.messages = messages;
	}

	public void storeElementGroupSettings(ElementGroup group, ElementGroupSettings settings) {
		if(isDifferent(group.getGroupId(),settings.getGroupId())){
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "group_id",
												   group.getGroupId(),
												   settings.getGroupId());
		}
		group.setElementGroupName(settings.getGroupName());
		group.setDescription(settings.getDescription());
		group.setLocation(settings.getLocation());
		group.setGeolocation(settings.getGeolocation());
		group.setTags(settings.getTags());
		LOG.fine(()->format("%s: Element %s group %s stored", 
							IVT0101I_GROUP_STORED.getReasonCode(),
							settings.getGroupType(),
							settings.getGroupName()));
		messages.add(createMessage(IVT0101I_GROUP_STORED, 
								  settings.getGroupType(), 
								  settings.getGroupName()));
	}
	

	public ElementGroupSettings getGroupSettings(ElementGroup group) {
		return newElementGroupSettings()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(group.getGroupType())
			   .withDescription(group.getDescription())
			   .withLocation(group.getLocation())
			   .withGeolocation(group.getGeolocation())
			   .withTags(group.getTags())
			   .build();
	}
	


	public void removeElementGroup(ElementGroup group){
		if(group.getElements().isEmpty()){
			repository.remove(group);
			LOG.fine(()->format("%s: Element %s group %s removed",
								IVT0102I_GROUP_REMOVED.getReasonCode(),
								group.getGroupType(),
								group.getGroupName()));
			messages.add(createMessage(IVT0102I_GROUP_REMOVED,
									   group.getGroupType(),
									   group.getGroupName()));
			return;
		} 
		LOG.fine(()->format("%s: Element %s group %s cannot be removed. Group is not empty!",
							IVT0102I_GROUP_REMOVED.getReasonCode(),
							group.getGroupType(),
							group.getGroupName()));
		throw new ConflictException(IVT0103E_GROUP_NOT_REMOVABLE,
									group.getGroupType(),
								    group.getGroupName());
	}
	
	public void createElementGroup(ElementGroupSettings settings) {
		ElementGroupId id = settings.getGroupId();
		if(id == null) {
			id = randomGroupId();
		}
		ElementGroup group = new ElementGroup(id,
											  settings.getGroupType(),
											  settings.getGroupName());
		group.setDescription(settings.getDescription());
		group.setLocation(settings.getLocation());
		group.setTags(settings.getTags());
		repository.add(group);
		LOG.fine(()->format("%s: Element %s group %s stored", 
							IVT0101I_GROUP_STORED.getReasonCode(),
							settings.getGroupType(),
							settings.getGroupName()));
		messages.add(createMessage(IVT0101I_GROUP_STORED, 
								  settings.getGroupType(),
								  settings.getGroupName()));

	}
	
	public List<ElementGroupSettings> findGroups(ElementGroupType type,
												 String filter, 
												 int offset, 
												 int items) {
		List<ElementGroupSettings> groups = new LinkedList<>();
		for(ElementGroup group : repository.execute(findByElementGroupName(type,
																		   filter,
																		   offset,
																		   items))){
			groups.add(newElementGroupSettings()
					   .withGroupId(group.getGroupId())
					   .withGroupType(group.getGroupType())
					   .withGroupName(group.getGroupName())
					   .withDescription(group.getDescription())
					   .withLocation(group.getLocation())
					   .withTags(group.getTags())
					   .build());
		}
		return unmodifiableList(groups);
	}

	public List<ElementGroupStatistics> getGroupStatistics(ElementGroupType type, 
			   											   String filter) {
		List<Object> args = new LinkedList<>();
		args.add(type);
		String query = "SELECT g.type, g.name, g.uuid, e.adm_state, e.op_state, count(*) "+
					   "FROM inventory.elementgroup g "+
					   "LEFT OUTER JOIN inventory.element e "+
					   "ON e.elementgroup_id = g.id "+
					   "JOIN inventory.elementrole r "+
					   "ON e.elementrole_id = r.id "+
					   "WHERE r.manageable = 'Y' "+
					   "AND g.type = ? ";
		if(isNonEmptyString(filter)) {
		      query += "AND e.name ~ ? ";
		      args.add(filter);
		}

		query += "GROUP BY  g.type, g.name, g.uuid, e.adm_state, e.op_state "+
				 "ORDER BY  g.name, g.uuid, e.op_state";

		LinkedList<ElementGroupStatistics.Builder> stats = new LinkedList<>();	  

		db.processQuery(prepare(query, args), 
					    rs -> {
							ElementGroupId   groupId   = groupId(rs.getString(3));
							ElementGroupStatistics.Builder last = stats.peekLast();
							if(last == null || !last.hasGroupId(groupId)) {
								ElementGroupType groupType = groupType(rs.getString(1));
								ElementGroupName groupName = groupName(rs.getString(2));
								last = newElementGroupStatistics()
									   .withGroupId(groupId)
									   .withGroupType(groupType)
									   .withGroupName(groupName);
								stats.add(last);
							}

							AdministrativeState admState = administrativeState(rs.getString(4));
							if(admState.is(ACTIVE)) {
								OperationalState opState  = operationalState(rs.getString(5));
								int count = rs.getInt(6);
								last.withCount(opState, count);
							} else if(admState.is(NEW)){
								last.withNewCount(rs.getInt(6));
							} else {
								last.withRetiredCount(rs.getInt(6));
							}
							
					    });

		return stats.stream()
				    .map(ElementGroupStatistics.Builder::build)
				    .collect(toList());
	}	
}
