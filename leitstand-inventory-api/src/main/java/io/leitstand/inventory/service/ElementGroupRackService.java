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

public interface ElementGroupRackService {

	ElementGroupRacks findRacks(ElementGroupId groupId);
	ElementGroupRacks findRacks(ElementGroupType groupType, 
								 ElementGroupName groupName);
	ElementGroupRack getRack(ElementGroupId groupId, 
							 RackName rackName);
	ElementGroupRack getRack(ElementGroupType groupType,
							 ElementGroupName groupName, 
							 RackName rackName);
	
	boolean storeRack(ElementGroupId groupId,
					  RackName rackName,
					  RackSettings settings);
	
	boolean storeRack(ElementGroupType groupType,
					  ElementGroupName groupName,
					  RackName rackName,
					  RackSettings settings);
	void removeRack(ElementGroupId groupId, 
					RackName rackName);
	
	void removeRack(ElementGroupType groupType,
					ElementGroupName groupName, 
					RackName rackName);
}
