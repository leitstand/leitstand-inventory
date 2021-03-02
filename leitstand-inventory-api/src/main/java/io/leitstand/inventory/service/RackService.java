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

/**
 * A transactional service to manage racks and rack items.
 */
public interface RackService {

    /**
     * Finds the rack item where an element is installed     
     * @param elementId the element ID
     * @return the rack item where the element is installed.
     * @throws EntityNotFoundException when the element does not exist or no rack item for the element is specified.
     */
	RackItem findElementRackItem(ElementId elementId);

    /**
     * Finds the rack item where an element is installed     
     * @param elementName the element name
     * @return the rack item where the element is installed.
     * @throws EntityNotFoundException when the element does not exist or no rack item for the element is specified.
     */
	RackItem findElementRackItem(ElementName elementName);
	
    /**
     * Finds all racks of a network facility filtered by the rack name.
     * Returns all matching racks ordered by rack name. 
     * @param facilityId the facility ID
     * @param filter an optional regular expression to filter racks by name
     * @return all matching racks ordered by rack name.
     * @throws EntityNotFoundException if the facility does not exist.
     */
	List<RackSettings> findRacks(FacilityId facilityId, String filter);

    /**
     * Finds all racks of a network facility filtered by the rack name.
     * Returns all matching racks ordered by rack name. 
     * @param facilityName the facility name
     * @param filter an optional regular expression to filter racks by name
     * @return all matching racks ordered by rack name.
     * @throws EntityNotFoundException if the facility does not exist.
     */
	List<RackSettings> findRacks(FacilityName facilityName, String filter);
	
    /**
     * Queries racks filtered by the rack name.
     * Returns all matching racks ordered by rack name. 
     * @param filter a regular expression to filter racks by name
     * @return all matching racks ordered by rack name.
     * @throws EntityNotFoundException if the facility does not exist.
     */
	List<RackSettings> findRacks(String filter);
	
	/**
	 * Removes a rack and all its rack items.
	 * @param rackId the rack ID.
	 */
	void forceRemoveRack(RackId rackId);
	
	/**
	 * Removes a rack and all its rack items.
	 * @param rackName the rack name.
	 */
	void forceRemoveRack(RackName rackName);
	
	/**
	 * Returns a rack item.
	 * @param rackId the rack ID
	 * @param unit the rack unit
	 * @return the rack item
	 * @throws EntityNotFoundException if the rack or the rack unit does not exist.
	 */
	RackItem getRackItem( RackId rackId, int unit);
    
	/**
     * Returns a rack item.
     * @param rackName the rack name
     * @param unit the rack unit
     * @return the rack item
     * @throws EntityNotFoundException if the rack or the rack unit does not exist.
     */
	RackItem getRackItem( RackName rackName, int unit);
	
	/**
	 * Returns all rack items of a rack.
	 * @param rackId the rack ID
	 * @return all rack items of the rack.
	 * @throws EntityNotFoundException if the rack does not exist.
	 */
	RackItems getRackItems(RackId rackId);

	/**
     * Returns all rack items of a rack.
     * @param rackName the rack name
     * @return all rack items of the rack.
     * @throws EntityNotFoundException if the rack does not exist.
     */
	RackItems getRackItems(RackName rackName);
	
	/**
	 * Returns the rack settings.
	 * @param rackId the rack ID
	 * @return the rack settings.
	 * @throws EntityNotFoundException if the rack does not exist.
	 */
	RackSettings getRackSettings(RackId rackId);
	
    /**
     * Returns the rack settings.
     * @param rackName the rack name
     * @return the rack settings.
     * @throws EntityNotFoundException if the rack does not exist.
     */
	RackSettings getRackSettings(RackName rackName);
	
	/**
	 * Removes an empty rack.
	 * @param rackId the rack ID
	 * @throws ConflictException if the rack is not empty.
	 */
	void removeRack(RackId rackId);
    /**
     * Removes an empty rack.
     * @param rackName the rack name
     * @throws ConflictException if the rack is not empty.
     */
	void removeRack(RackName rackName);
	
	/**
	 * Removes a rack item.
	 * @param rackId the rack ID
	 * @param unit the rack item unit
	 */
	void removeRackItem(RackId rackId, int unit);
	
	/**
	 * Removes a rack item.
	 * @param rackName the rack name.
	 * @param unit the rack item unit.
	 */
	void removeRackItem(RackName rackName, int unit);
	
	/**
	 * Stores the rack settings.
	 * Returns <code>true</code> if a new rack is created and <code>false</code> otherwise.
	 * @param settings the rack settings.
	 * @return <code>true</code> if a new rack is created and <code>false</code> otherwise.
	 */
	boolean storeRack(RackSettings settings);
	
	/**
	 * Stores a rack item.
	 * @param rackId the rack ID
	 * @param item the rack item settings
	 * @throws EntityNotFoundException if the rack does not exist.
	 */
	void storeRackItem(RackId rackId, RackItemData item);
	
	/**
     * Stores a rack item.
     * @param rackName the rack name
     * @param item the rack item settings
     * @throws EntityNotFoundException if the rack does not exist.
     */
	void storeRackItem(RackName rackName, RackItemData item);
}
