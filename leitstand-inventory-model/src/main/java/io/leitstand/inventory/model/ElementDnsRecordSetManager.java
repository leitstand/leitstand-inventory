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

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.inventory.event.DnsRecordSetUpdate.newDnsRecordSetUpdate;
import static io.leitstand.inventory.event.ElementDnsRecordSetModifiedEvent.newDnsRecordSetChangedEvent;
import static io.leitstand.inventory.model.ElementValueObjects.elementValueObject;
import static io.leitstand.inventory.model.Element_DnsRecordSet.findDnsRecordSet;
import static io.leitstand.inventory.model.Element_DnsRecordSet.findDnsRecordSets;
import static io.leitstand.inventory.service.DnsRecord.newDnsRecord;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.ElementDnsRecordSet.newElementDnsRecordSet;
import static io.leitstand.inventory.service.ElementDnsRecordSets.newElementDnsRecordSets;
import static io.leitstand.inventory.service.ReasonCode.IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT3002I_ELEMENT_DNS_RECORD_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT;
import static io.leitstand.inventory.service.ReasonCode.IVT3003I_ELEMENT_DNS_RECORD_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT3004E_ELEMENT_DNS_RECORD_ZONE_MISMATCH;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.DnsRecordSetUpdate;
import io.leitstand.inventory.event.ElementDnsRecordSetModifiedEvent;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecord;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
import io.leitstand.inventory.service.ElementDnsRecordSet;
import io.leitstand.inventory.service.ElementDnsRecordSets;

@Dependent
public class ElementDnsRecordSetManager {

	private static final Logger LOG = Logger.getLogger(ElementDnsRecordSetManager.class.getName());
	
	private static DnsRecordSet dnsRescordSet(Element_DnsRecordSet set) {
		return newDnsRecordSet()
               .withDnsZoneId(set.getDnsZoneId())
               .withDnsZoneName(set.getDnsZoneName())
			   .withDnsRecordSetId(set.getDnsRecordSetId())
			   .withDnsName(set.getDnsName())
			   .withDnsRecordType(set.getDnsRecordType())
			   .withDnsRecordTimeToLive(set.getTimeToLoveInSeconds())
			   .withDescription(set.getDescription())
			   .withDnsRecords(set.getDnsRecords())
			   .build();
	}
	
	
	private Repository repository;
	private Event<ElementDnsRecordSetModifiedEvent> event;
	private Messages messages;
	private DnsZoneProvider zones;
	
	protected ElementDnsRecordSetManager() {
		// CDI
	}
	
	@Inject
	protected ElementDnsRecordSetManager(DnsZoneProvider zones,
										 @Inventory Repository repository,
										 Event<ElementDnsRecordSetModifiedEvent> event,
										 Messages messages) {
		this.zones = zones;
		this.repository = repository;
		this.event = event;
		this.messages = messages;
	}
	
	
	public ElementDnsRecordSets getElementDnsRecordSets(Element element) {
		
		List<DnsRecordSet> records = repository.execute(findDnsRecordSets(element))
											   .stream()
											   .map( set -> dnsRescordSet(set))
											   .collect(toList());
				
		return elementValueObject(newElementDnsRecordSets(),element)
			   .withDnsRecordSets(records)
			   .build();
	}

	public ElementDnsRecordSet getElementDnsRecordSet(DnsRecordSetId id) {
		Element_DnsRecordSet record = repository.execute(findDnsRecordSet(id));
		if(record == null) {
			LOG.fine(() -> format("%s: DNS record set ID %s is unknown.",
						   		  IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND.getReasonCode(),
						   		  id));
			throw new EntityNotFoundException(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND,
											  id);
		}
		Element element = record.getElement();
		return elementValueObject(newElementDnsRecordSet(),element)
			   .withDnsRecordSet(dnsRescordSet(record))
			   .build();	

	}
	
	public ElementDnsRecordSet getElementDnsRecordSet(Element element, 
													  DnsName dnsName,
													  DnsRecordType dnsType) {
		Element_DnsRecordSet record = repository.execute(findDnsRecordSet(element, 
																		  dnsName,
																		  dnsType));
		if(record == null) {
			LOG.fine(() -> format("%s: %s DNS record does not exist for %s %s (%s).",
						   		  IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND.getReasonCode(),
						   		  dnsName,
						   		  element.getElementRoleName(),
						   		  element.getElementName(),
						   		  element.getElementId()));
			throw new EntityNotFoundException(IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND,
											  dnsName);
		}
		return elementValueObject(newElementDnsRecordSet(),element)
			   .withDnsRecordSet(dnsRescordSet(record))
			   .build();
		
	}
	
	public void removeElementDnsRecordSet(Element element, 
										  DnsName dnsName,
										  DnsRecordType dnsType) {
		Element_DnsRecordSet record = repository.execute(findDnsRecordSet(element, 
																		  dnsName, 
																		  dnsType));
		if(record != null) {
			removeElementDnsRecordSet(record);
		}
	}

	private void removeElementDnsRecordSet(Element_DnsRecordSet record) {
		Element element = record.getElement();
		
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		repository.remove(record);
		LOG.fine(()->format("%s: Removed %s DNS record for %s %s (%s).",
							IVT3003I_ELEMENT_DNS_RECORD_REMOVED.getReasonCode(),
							record.getDnsName(),
							element.getElementRoleName(),
							element.getElementName(),
							element.getElementId()));
		messages.add(createMessage(IVT3003I_ELEMENT_DNS_RECORD_REMOVED, 
								   record.getDnsName()));
		
		DnsRecordSetUpdate dnsUpdate = newDnsRecordSetUpdate()
									   .withDnsZoneId(record.getDnsZoneId())
									   .withDnsZoneName(record.getDnsZoneName())
									   .withDnsRecordSetId(record.getDnsRecordSetId())
									   .withDnsRecordType(record.getDnsRecordType())
				   					   .withDnsWithdrawnName(record.getDnsName())
				   					   .withDescription(record.getDescription())
				   					   .withDnsRecords(records(record))
				   					   .build();

		
		event.fire(dnsRecordSetStoredEvent(element, dnsUpdate));
	}
	
	public boolean storeElementDnsRecordSet(Element element, DnsRecordSet record) {
		boolean created = false;
		DnsZone zone = zones.fetchDnsZone(record.getDnsZoneName());
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		Element_DnsRecordSet _record = repository.execute(findDnsRecordSet(record.getDnsRecordSetId()));
		if(_record == null) {
			_record = new Element_DnsRecordSet(zone,
											   element, 
											   record.getDnsRecordSetId(), 
											   record.getDnsName());
			repository.add(_record);
			created = true;
		} 
		
		if(!record.getDnsName().endsWidth(zone.getDnsZoneName())) {
			LOG.fine(() -> format("%s: DNS record %s cannot become a member of DNS zone %s.",
								  IVT3004E_ELEMENT_DNS_RECORD_ZONE_MISMATCH.getReasonCode(),
								  record.getDnsName(),
								  record.getDnsZoneName()));
			
			throw new ConflictException(IVT3004E_ELEMENT_DNS_RECORD_ZONE_MISMATCH, 
										record.getDnsName(), 
										zone.getDnsZoneName());
			
		}
		
		
		Element owner = _record.getElement();
		if(isDifferent(element, owner)) {
			LOG.fine(() -> format("Moved %s DNS record from %s %s (%s) to %s %s (%s)",
								  record.getDnsName(),
								  owner.getElementRoleName(),
								  owner.getElementName(),
								  owner.getElementId(),
								  element.getElementRoleName(),
								  element.getElementName(),
								  element.getElementId()));
			repository.lock(owner, OPTIMISTIC_FORCE_INCREMENT);
			_record.setElement(element);
			created = true;
		}
		
		DnsName dnsWithDrawnName = null;
		if(isDifferent(record.getDnsName(),_record.getDnsName())) {
			dnsWithDrawnName = _record.getDnsName();
		}
		
		_record.setDnsName(record.getDnsName());
		_record.setDescription(record.getDescription());
		_record.setTimeToLiveInSeconds(record.getDnsTtl());
		_record.setDnsRecordType(record.getDnsType());
		_record.setDnsRecords(record.getDnsRecords());
		_record.setDnsZone(zone);
		
		LOG.fine(() -> format("%s: Stored %s DNS records for %s %s (%s)",
							  IVT3002I_ELEMENT_DNS_RECORD_STORED.getReasonCode(),
							  record.getDnsName(),
							  element.getElementRoleName(),
							  element.getElementName(),
							  element.getElementId()));
		messages.add(createMessage(IVT3002I_ELEMENT_DNS_RECORD_STORED, 
		 			   			   record.getDnsName()));

		DnsRecordSetUpdate dnsUpdate = newDnsRecordSetUpdate()
									   .withDnsZoneId(zone.getDnsZoneId())
									   .withDnsZoneName(zone.getDnsZoneName())
									   .withDnsRecordSetId(_record.getDnsRecordSetId())
									   .withDnsRecordType(_record.getDnsRecordType())
									   .withDnsName(_record.getDnsName())
									   .withDnsWithdrawnName(dnsWithDrawnName)
									   .withDescription(_record.getDescription())
									   .withDnsRecords(records(_record))
									   .build();
		
		
		event.fire(dnsRecordSetStoredEvent(element, dnsUpdate));
		
		
		return created;
	}

	private static List<DnsRecord> records(Element_DnsRecordSet _record) {
		return _record.getDnsRecords()
				   	  .stream()
				   	  .map(r -> newDnsRecord()
				   		  		.withRecordValue(r.getDnsRecordValue())
				   		  		.withDisabled(r.isDisabled())
				   		  		.withSetPtr(r.isSetPtr())
				   				.build())
				   	  .collect(toList());
	}

	private static ElementDnsRecordSetModifiedEvent dnsRecordSetStoredEvent(Element element, DnsRecordSetUpdate update) {
		return newDnsRecordSetChangedEvent()
			   .withGroupId(element.getGroupId())
			   .withGroupType(element.getGroupType())
			   .withGroupName(element.getGroupName())
			   .withElementRole(element.getElementRoleName())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withAdministrativeState(element.getAdministrativeState())
			   .withOperationalState(element.getOperationalState())
			   .withDateModified(element.getDateModified())
			   .withDnsRecordSetUpdate(update)
			   .build();
	}

	public void removeElementDnsRecordSet(Element element, DnsRecordSetId dnsId) {
		Element_DnsRecordSet record = repository.execute(findDnsRecordSet(dnsId));
		if(record == null) {
			return;
		}
		if(isDifferent(element, record.getElement())) {
			LOG.fine(()->format("%s: Cannot remove DNS %s because it is owned %s %s (%s) and not by %s %s (%s).",
								IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND.getReasonCode(),
								dnsId,
								record.getElement().getElementRole(),
								record.getElement().getElementName(),
								record.getElement().getElementId(),
								element.getElementRole(),
								element.getElementName(),
								element.getElementId()));
			throw new ConflictException(IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT, 
										dnsId, 
										record.getElement().getElementId(), 
										element.getElementName());
		}
		
		removeElementDnsRecordSet(record);
	}



	
	
}
