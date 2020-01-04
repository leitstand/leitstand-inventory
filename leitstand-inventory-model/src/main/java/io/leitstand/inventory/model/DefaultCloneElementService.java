/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.event.ElementClonedEvent.newElementClonedEvent;
import static io.leitstand.inventory.jpa.AdministrativeStateConverter.administrativeStateDbString;
import static io.leitstand.inventory.jpa.OperationalStateConverter.operationalStateDbString;
import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.ReasonCode.IVT0306I_ELEMENT_CLONED;

import java.util.Date;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.jpa.SerializableJsonObjectConverter;
import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.jpa.EnvironmentIdConverter;
import io.leitstand.inventory.jpa.EnvironmentNameConverter;
import io.leitstand.inventory.service.CloneElementService;
import io.leitstand.inventory.service.ElementCloneRequest;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;

@Service
public class DefaultCloneElementService implements CloneElementService{

	@Inject
	private ElementProvider elements;
	
	@Inject
	private Messages messages;
	
	@Inject
	@Inventory
	private DatabaseService db; 
	
	@Inject
	private Event<ElementEvent> sink;
	
	@Override
	public ElementId cloneElement(ElementId sourceElementId, 
								  ElementCloneRequest request) {
		Element source = elements.fetchElement(sourceElementId);
		return cloneElement(source,
							request);
	}

	@Override
	public ElementId cloneElement(ElementName sourceElementName, ElementCloneRequest request) {
		Element source = elements.fetchElement(sourceElementName);
		return cloneElement(source,
							request);

	}
	
	private ElementId cloneElement(Element source, 
								   ElementCloneRequest request) {
		
		// In order to avoid streaming all data from the database to the server, the cloning is done via SQL
		
		// Acquire a new element ID
		Long id = db.getSingleResult(prepare("UPDATE leitstand.sequence "+
										     "SET count = count + 1"+ 
										     "WHERE name = 'ID' "+
										     "RETURNING count"),
										     rs -> rs.getLong(1));
		
		// Clone the element record
		db.executeUpdate(prepare("INSERT INTO inventory.element (elementgroup_id, elementrole_id, platform_id, id, uuid, name, description, op_state, adm_state, mgmt_hostname, mgmt_mac, serial, location, rack_name, rack_position, modcount, tscreated, tsmodified)"+
								 "SELECT elementgroup_id, elementrole_id, platform_id, ?, ?, ?, description, ?, ?, mgmt_hostname, ?, ?, location, rack_name, rack_position, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP "+
								 "FROM inventory.element "+
								 "WHERE id=?",
								 id,
								 request.getElementId(),
								 request.getElementName(),
								 operationalStateDbString(DOWN),
								 administrativeStateDbString(NEW),
								 request.getMgmtMacAddress(),
								 request.getSerialNumber(),
								 source.getId()));
		
		// Copy tags
		db.executeUpdate(prepare("INSERT INTO inventory.element_tag (element_id, tag) "+
								 "SELECT ?, tag "+
								 "FROM inventory.element_tag "+
								 "WHERE element_id = ?",
								 id,
								 source.getId()));
				
		
		// Copy management interfaces
		db.executeUpdate(prepare("INSERT INTO inventory.element_management_interface (element_id, name, protocol, port, path) "+
								 "SELECT ?, name, protocol, port, path "+
								 "FROM inventory.element_management_interface "+
								 "WHERE element_id = ?",
								 id,
								 source.getId()));

		// Copy configuration series
		db.executeUpdate(prepare("INSERT INTO inventory.element_config (element_id, uuid, name, contenttype, config, tsmodified, comment) "+
								 "SELECT ?, random_uuid(), name, contenttype, config, tsmodified, comment "+
								 "FROM inventory.element_config "+
								 "WHERE element_id = ?",
								 id,
								 source.getId()));
		
		// Copy installed images
		db.executeUpdate(prepare("INSERT INTO inventory.element_image (element_id, image_id, image_state) "+
				 				 "SELECT ?, image_id, 'PULL' "+
				 				 "FROM inventory.element_image "+
				 				 "WHERE element_id = ? "+
				 				 "AND image_state='ACTIVE'",
				 				 id,
				 				 source.getId()));	
		
		// Copy environments
		db.executeUpdate(prepare("INSERT INTO inventory.element_env (element_id, uuid, name, category, type, description, variables, tsmodified "+
							     "SELECT ?, random_uuid(), name, category, type, description, variables, tsmodified "+
								 "FROM inventory.element_env "+
							     "WHERE element_id = ? "));

		
		messages.add(createMessage(IVT0306I_ELEMENT_CLONED,
					 			   source.getElementName(),
					 			   request.getElementName()));
		
		sink.fire(newElementClonedEvent()
				  .withGroupId(source.getGroup().getGroupId())
				  .withGroupName(source.getGroup().getGroupName())
				  .withGroupType(source.getGroup().getGroupType())
				  .withElementId(source.getElementId())
				  .withElementName(source.getElementName())
				  .withElementAlias(source.getElementAlias())
				  .withElementRole(source.getElementRoleName())
				  .withSerialNumber(source.getSerialNumber())
				  .withMacAddress(source.getManagementInterfaceMacAddress())
				  .withCloneElementId(request.getElementId())
				  .withCloneElementName(request.getElementName())
				  .withCloneSerialNumber(request.getSerialNumber())
				  .withCloneMacAddress(request.getMgmtMacAddress())
				  .build());
		
		return request.getElementId();
	}

}
