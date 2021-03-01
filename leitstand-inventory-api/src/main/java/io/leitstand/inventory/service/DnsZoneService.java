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

import java.util.List;

import io.leitstand.commons.ConflictException;

/**
 * A transactional service to manange DNS zones in the inventory.
 */
public interface DnsZoneService {

    /**
     * Filters DNS zones by their name and returns all matching DNS zones.
     * @param filter regular expression to filter DNS zones by their name
     * @param offset the read offset for matching zones
     * @param limit the maximum number of returned matching zones
     * @return list of matching DNS zones or an empty list if no matching zone exist
     */
    List<DnsZoneSettings> getDnsZones(String filter, int offset, int limit);
	
    /**
     * Returns the DNS zone settings.
     * @param zoneId the DNS zone ID
     * @return the DNS zone settings.
     * @throws EntityNotFoundException if the DNS zone does not exist.
     */
	DnsZoneSettings getDnsZoneSettings(DnsZoneId zoneId);
	
	/**
	 * Returns the DNS zone settings.
	 * @param zoneName the DNS zone name
	 * @return the DNS zone settings.
	 * @throws EntityNotFoundException if the DNS zone does not exist.
	 */
	DnsZoneSettings getDnsZoneSettings(DnsZoneName zoneName);
	
	/**
	 * Returns the elements with DNS records for the specified DNS zone.
	 * @param zoneId the DNS zone ID
	 * @return the elements DNS records for the specified DNS zone
	 * @throws EntityNotFoundException if the DNS zone does not exist.
	 */
	DnsZoneElements getDnsZoneElements(DnsZoneId zoneId);
	
	/**
	 * Returns the elements with DNS records for the specified DNS zone.
	 * @param zoneName the DNS zone name
	 * @return the elements DNS records for the specified DNS zone.
	 * @throws EntityNotFoundException if the DNS zone does not exist.
	 */
	DnsZoneElements getDnsZoneElements(DnsZoneName zoneName);
	
	/**
	 * Stores DNS zone settings. 
	 * Returns <code>true</code> if a new DNS zone is created and <code>false</code> otherwise.
	 * @param settings the DNS zone settings.
	 * @return <code>true</code> if a new DNS zone is created, <code>false</code> otherwise.
	 */
	boolean storeDnsZoneSettings(DnsZoneSettings settings);
	
	/**
	 * Removes an empty DNS zone.
	 * @param zoneId the DNS zone ID
	 * @throws ConflictException if the DNS zone contains DNS records.
	 */
	void removeDnsZone(DnsZoneId zoneId);
	
	/**
     * Removes an empty DNS zone.
     * @param zoneName the DNS zone name
     * @throws ConflictException if the DNS zone contains DNS records.
     */
	void removeDnsZone(DnsZoneName zoneName);

	/**
     * Removes a DNS zone and all its records.
     * @param zoneId the DNS zone ID
     */
	void forceRemoveDnsZone(DnsZoneId zoneId);

	/**
     * Removes a DNS zone and all its records.
     * @param zoneId the DNS zone ID
     */
	void forceRemoveDnsZone(DnsZoneName zoneName);
	
}
