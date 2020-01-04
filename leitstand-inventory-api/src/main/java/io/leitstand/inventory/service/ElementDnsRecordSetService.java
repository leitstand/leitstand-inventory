/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
