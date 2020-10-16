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
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.event.DnsZoneCreatedEvent.newDnsZoneCreatedEvent;
import static io.leitstand.inventory.event.DnsZoneRemovedEvent.newDnsZoneRemovedEvent;
import static io.leitstand.inventory.model.DnsZone.countDnsZoneRecords;
import static io.leitstand.inventory.model.DnsZone.findDnsZoneElements;
import static io.leitstand.inventory.model.DnsZone.findDnsZones;
import static io.leitstand.inventory.model.DnsZone.removeDnsZoneRecords;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.DnsZoneElement.newDnsZoneElement;
import static io.leitstand.inventory.service.DnsZoneElements.newDnsZoneElements;
import static io.leitstand.inventory.service.DnsZoneSettings.newDnsZoneSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0951I_DNS_ZONE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0952I_DNS_ZONE_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0953E_DNS_ZONE_NOT_REMOVABLE;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.DnsZoneEvent;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsZoneElement;
import io.leitstand.inventory.service.DnsZoneElements;
import io.leitstand.inventory.service.DnsZoneSettings;

@Dependent
public class DnsZoneManager {
	
	private static final Logger LOG = Logger.getLogger(DnsZoneManager.class.getName());
	
	private Repository repository;
	private Messages messages;
	private Event<DnsZoneEvent> event;
	
	protected DnsZoneManager() {
		// CDI
	}

	@Inject
	protected DnsZoneManager(@Inventory Repository repository,
						     Messages messages,
						     Event<DnsZoneEvent> event) {
		this.repository = repository;
		this.messages = messages;
		this.event = event;
	}

	
	public List<DnsZoneSettings> getDnsZones(String filter, int offset, int limit) {
		return repository.execute(findDnsZones(filter,offset,limit))
						 .stream()
						 .map(zone -> settingsOf(zone))
						 .collect(toList());
	}

	public DnsZoneSettings getDnsZoneSettings(DnsZone zone) {
		return settingsOf(zone);
	}

	private DnsZoneSettings settingsOf(DnsZone zone) {
		return newDnsZoneSettings()
			   .withDnsZoneId(zone.getDnsZoneId())
			   .withDnsZoneName(zone.getDnsZoneName())
			   .withDescription(zone.getDescription())
			   .withDnsZoneConfigType(zone.getDnsZoneConfigType())
			   .withDnsZoneConfig(zone.getDnsZoneConfig())
			   .build();
	}
	
	public DnsZoneElements getDnsZoneElements(DnsZone zone) {
		
		Map<Element,List<DnsRecordSet>> elements = new HashMap<>();
 		for(Element_DnsRecordSet record : repository.execute(findDnsZoneElements(zone))) {
 			Element element = record.getElement();
			List<DnsRecordSet> sets = elements.get(element);
			if(sets == null) {
				sets = new LinkedList<>();
				elements.put(element,sets);
			}
			sets.add(newDnsRecordSet()
					 .withDnsRecordSetId(record.getDnsRecordSetId())
					 .withDnsName(record.getDnsName())
					 .withDnsRecordType(record.getDnsRecordType())
					 .withDnsRecordTimeToLive(record.getTimeToLoveInSeconds())
					 .withDescription(record.getDescription())
					 .withDnsRecords(record.getDnsRecords())
					 .build());
		}
		
 		List<DnsZoneElement> records = new LinkedList<>();
 		for(Map.Entry<Element, List<DnsRecordSet>> entry : elements.entrySet()  ) {
 			Element element = entry.getKey();
 			List<DnsRecordSet> sets = entry.getValue();
 			records.add(newDnsZoneElement()
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
 						.withDnsRecordSets(sets)
 						.build());
 		}
 		
		return newDnsZoneElements()
			   .withDnsZoneId(zone.getDnsZoneId())
			   .withDnsZoneName(zone.getDnsZoneName())
			   .withDescription(zone.getDescription())
			   .withDnsEntries(records).build();
	}
	
	
	public void removeDnsZone(DnsZone zone) {
		int records = repository.execute(countDnsZoneRecords(zone));
		if(records > 0) {
			LOG.fine(() -> format("%s: DNS Zone %s contains %d recoreds and cannot be removed, unless force remove option is set", 
								  IVT0953E_DNS_ZONE_NOT_REMOVABLE.getReasonCode(),
								  zone.getDnsZoneName(),
								  records));
					
			throw new ConflictException(IVT0953E_DNS_ZONE_NOT_REMOVABLE,
									    zone.getDnsZoneId(),
									    zone.getDnsZoneName());
		}
		
		repository.remove(zone);
		
		LOG.fine(() -> format("%s: DNS zone %s removed",
							  IVT0952I_DNS_ZONE_REMOVED.getReasonCode(),
							  zone.getDnsZoneName()));

		messages.add(createMessage(IVT0952I_DNS_ZONE_REMOVED, 
								   zone.getDnsZoneId(), 
								   zone.getDnsZoneName()));
		
		event.fire(newDnsZoneRemovedEvent()
				   .withDnsZoneId(zone.getDnsZoneId())
				   .withDnsZoneName(zone.getDnsZoneName())
				   .build());
		
		
	}
	
	public void forceRemoveDnsZone(DnsZone zone) {
		int records = repository.execute(removeDnsZoneRecords(zone));
		LOG.fine(() -> format("Removed %d DNS records from zone %s",
							  records,
							  zone.getDnsZoneName()));
		repository.remove(zone);
		
		LOG.fine(() -> format("%s: DNS zone %s removed",
							  IVT0952I_DNS_ZONE_REMOVED.getReasonCode(),
							  zone.getDnsZoneName()));

		messages.add(createMessage(IVT0952I_DNS_ZONE_REMOVED, 
								   zone.getDnsZoneId(), 
								   zone.getDnsZoneName()));
		
		event.fire(newDnsZoneRemovedEvent()
				   .withDnsZoneId(zone.getDnsZoneId())
				   .withDnsZoneName(zone.getDnsZoneName())
				   .build());
	}

	public void storeDnsZoneSettings(DnsZone zone, DnsZoneSettings settings) {
		if(isDifferent(zone.getDnsZoneName(),settings.getDnsZoneName())) {
			LOG.fine(() -> format("%s: DNS zone %s cannot be renamed to %s",
					  			  VAL0003E_IMMUTABLE_ATTRIBUTE.getReasonCode(),
					  			  zone.getDnsZoneName(),
					  			  settings.getDnsZoneName()));
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "dns_zone_name",
												   settings.getDnsZoneName(),
												   zone.getDnsZoneName());
		}
		
		zone.setDescription(settings.getDescription());
		zone.setDnsZoneConfigType(settings.getDnsZoneConfigType());
		zone.setDnsZoneConfig(settings.getDnsZoneConfig());
		LOG.fine(() -> format("%s: DNS zone %s stored",
							  IVT0951I_DNS_ZONE_STORED.getReasonCode(),
				  			  zone.getDnsZoneName()));

		messages.add(createMessage(IVT0951I_DNS_ZONE_STORED,
					   			   zone.getDnsZoneId(), 
					   			   zone.getDnsZoneName()));


	}
	
	public void createDnsZone(DnsZoneSettings settings) {
		DnsZone zone = new DnsZone(settings.getDnsZoneId(),
								   settings.getDnsZoneName());
		storeDnsZoneSettings(zone, settings);
		repository.add(zone);
		event.fire(newDnsZoneCreatedEvent()
				   .withDnsZoneId(zone.getDnsZoneId())
				   .withDnsZoneName(zone.getDnsZoneName())
				   .build());
	}


	
}
