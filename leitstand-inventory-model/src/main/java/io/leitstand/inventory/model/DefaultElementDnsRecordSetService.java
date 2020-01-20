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

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
import io.leitstand.inventory.service.ElementDnsRecordSet;
import io.leitstand.inventory.service.ElementDnsRecordSetService;
import io.leitstand.inventory.service.ElementDnsRecordSets;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@Service
public class DefaultElementDnsRecordSetService implements ElementDnsRecordSetService{

	private ElementDnsRecordSetManager manager;
	private ElementProvider elements;
	
	protected DefaultElementDnsRecordSetService() {
		// CDI
	}
	
	@Inject
	protected DefaultElementDnsRecordSetService(ElementProvider elements, 
												ElementDnsRecordSetManager manager) {
		this.elements = elements;
		this.manager = manager;
	}
	
	@Override
	public ElementDnsRecordSets getElementDnsRecordSets(ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementDnsRecordSets(element);
	}

	@Override
	public ElementDnsRecordSets getElementDnsRecordSets(ElementName elementName) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementDnsRecordSets(element);
	}

	@Override
	public ElementDnsRecordSet getElementDnsRecordSet(DnsRecordSetId id) {
		return manager.getElementDnsRecordSet(id);
	}
	
	@Override
	public ElementDnsRecordSet getElementDnsRecordSet(ElementId elementId, DnsName dnsName, DnsRecordType type) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementDnsRecordSet(element,dnsName,type);
	}

	@Override
	public ElementDnsRecordSet getElementDnsRecordSet(ElementName elementName, DnsName dnsName, DnsRecordType type) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementDnsRecordSet(element,dnsName,type);
	}

	@Override
	public boolean storeElementDnsRecordSet(ElementId elementId, DnsRecordSet recordSet) {
		Element element = elements.fetchElement(elementId);
		return manager.storeElementDnsRecordSet(element,recordSet);
	}

	@Override
	public boolean storeElementDnsRecordSet(ElementName elementName, DnsRecordSet recordSet) {
		Element element = elements.fetchElement(elementName);
		return manager.storeElementDnsRecordSet(element,recordSet);
	}

	@Override
	public void removeElementDnsRecordSet(ElementId elementId, DnsName dnsName, DnsRecordType type) {
		Element element = elements.fetchElement(elementId);
		manager.removeElementDnsRecordSet(element,dnsName,type);
		
	}

	@Override
	public void removeElementDnsRecordSet(ElementId elementId, DnsRecordSetId dnsId) {
		Element element = elements.fetchElement(elementId);
		manager.removeElementDnsRecordSet(element, dnsId);
		
	}

	@Override
	public void removeElementDnsRecordSet(ElementName elementName, DnsRecordSetId dnsId) {
		Element element = elements.fetchElement(elementName);
		manager.removeElementDnsRecordSet(element, dnsId);
	}

	@Override
	public void removeElementDnsRecordSet(ElementName elementName, DnsName dnsName, DnsRecordType type) {
		Element element = elements.fetchElement(elementName);
		manager.removeElementDnsRecordSet(element, dnsName,type);
	}

	
	
}
