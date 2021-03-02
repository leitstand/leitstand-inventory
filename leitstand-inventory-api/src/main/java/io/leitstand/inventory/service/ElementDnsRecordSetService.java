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

/**
 * A service for managing element DNS records.
 * <p>
 * The <code>ElementDnsRecordSetService</code> allows querying element DNS records as well as adding and removing element DNS records.
 */
public interface ElementDnsRecordSetService {

    /**
     * Returns the element DNS records.
     * @param elementId the element ID
     * @return the element DNS records.
     * @throws EntityNotFoundException when the element does not exist.
     */
	ElementDnsRecordSets getElementDnsRecordSets(ElementId elementId);
	
	/**
	 * Returns the element DNS records.
	 * @param elementName the element name or the element alias
	 * @return the element DNS records.
	 * @throws EntityNotFoundException when the element does not exist.
	 */
	ElementDnsRecordSets getElementDnsRecordSets(ElementName elementName);
	
	/**
	 * Returns the element DNS record.
	 * @param elementId the element ID
	 * @param dnsName the DNS name
	 * @param type the DNS record type
	 * @return the element DNS record
	 * @throws EntityNotFoundException when the element or the DNS record does not exist.
	 */
	ElementDnsRecordSet getElementDnsRecordSet(ElementId elementId, DnsName dnsName, DnsRecordType type);

   /**
     * Returns the element DNS record.
     * @param elementName the element name
     * @param dnsName the DNS name
     * @param type the DNS record type
     * @return the element DNS record
     * @throws EntityNotFoundException when the element or the DNS record does not exist.
     */
	ElementDnsRecordSet getElementDnsRecordSet(ElementName elementName, DnsName dnsName, DnsRecordType type);

	/**
     * Returns the element DNS record.
     * @param elementId the element ID
     * @param dnsName the DNS name
     * @param type the DNS record type
     * @return the element DNS record
     * @throws EntityNotFoundException when the element or the DNS record does not exist.
     */
	ElementDnsRecordSet getElementDnsRecordSet(DnsRecordSetId id);
	
	/**
	 * Stores an element DNS record.
	 * Returns <code>true</code> if a new DNS record is created and <code>false</code> otherwise.
	 * @param elementId the element ID
	 * @param recordSet the DNS record
	 * @return <code>true</code> if a new DNS record is created and <code>false</code> otherwise.
	 * @throws EntityNotFoundException if the element does not exist.
	 */
	boolean storeElementDnsRecordSet(ElementId elementId, DnsRecordSet recordSet);

	/**
     * Stores an element DNS record.
     * Returns <code>true</code> if a new DNS record is created and <code>false</code> otherwise.
     * @param elementName the element name
     * @param recordSet the DNS record
     * @return <code>true</code> if a new DNS record is created and <code>false</code> otherwise.
     * @throws EntityNotFoundException if the element does not exist.
     */
	boolean storeElementDnsRecordSet(ElementName elementName, DnsRecordSet recordSet);

	/**
	 * Removes an element DNS record.
	 * @param elementId the element ID
	 * @param dnsName the DNS name
	 * @param type the DNS record type
	 */
	void removeElementDnsRecordSet(ElementId elementId, DnsName dnsName, DnsRecordType type);
    
	/**
     * Removes an element DNS record.
     * @param elementName the element name
     * @param dnsName the DNS name
     * @param type the DNS record type
     */
	void removeElementDnsRecordSet(ElementName elementName, DnsName dnsName, DnsRecordType type);
	
	
	/**
     * Removes an element DNS record.
     * @param elementId the element ID
     * @param dnsId the DNS record set ID
     */
	void removeElementDnsRecordSet(ElementId elementId, DnsRecordSetId dnsId);

	/**
     * Removes an element DNS record.
     * @param elementName the element name
     * @param dnsId the DNS record set ID
     */
	void removeElementDnsRecordSet(ElementName elementName, DnsRecordSetId dnsId);

}
