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
package io.leitstand.inventory.service;

public interface ElementDnsRecordSetService {

	ElementDnsRecordSets getElementDnsRecordSets(ElementId elementId);
	ElementDnsRecordSets getElementDnsRecordSets(ElementName elementName);
	ElementDnsRecordSet getElementDnsRecordSet(ElementId elementId, DnsName dnsName, DnsRecordType type);
	ElementDnsRecordSet getElementDnsRecordSet(ElementName elementName, DnsName dnsName, DnsRecordType type);
	ElementDnsRecordSet getElementDnsRecordSet(DnsRecordSetId id);
	
	boolean storeElementDnsRecordSet(ElementId elementId, DnsRecordSet recordSet);
	boolean storeElementDnsRecordSet(ElementName elementName, DnsRecordSet recordSet);

	void removeElementDnsRecordSet(ElementId elementId, DnsName dnsName, DnsRecordType type);
	void removeElementDnsRecordSet(ElementName elementName, DnsName dnsName, DnsRecordType type);
	void removeElementDnsRecordSet(ElementId elementId, DnsRecordSetId dnsId);
	void removeElementDnsRecordSet(ElementName elementName, DnsRecordSetId dnsId);

}
