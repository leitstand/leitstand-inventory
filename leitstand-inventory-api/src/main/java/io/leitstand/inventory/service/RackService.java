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

	RackItem findElementRackItem(ElementId elementId);
	RackItem findElementRackItem(ElementName elementName);
	List<RackSettings> findRacks(FacilityId facilityId, String filter);
	List<RackSettings> findRacks(FacilityName facilityName, String filter);
	List<RackSettings> findRacks(String filter);
	void forceRemoveRack(RackId rackId);
	void forceRemoveRack(RackName rackName);
	RackItem getRackItem( RackId rackId, int unit);
	RackItem getRackItem( RackName rackName, int unit);
	RackItems getRackItems(RackId rackId);
	RackItems getRackItems(RackName rackName);
	RackSettings getRackSettings(RackId rackId);
	RackSettings getRackSettings(RackName rackName);
	void removeRack(RackId rackId);
	void removeRack(RackName rackName);
	void removeRackItem(RackId rackId, int unit);
	void removeRackItem(RackName rackName, int unit);
	boolean storeRack(RackSettings settings);
	void storeRackItem(RackId rackId, RackItemData item);
	void storeRackItem(RackName rackName, RackItemData item);
}
