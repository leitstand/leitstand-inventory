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
 * A service for discovering physical interfaces in the network.
 */
public interface PhysicalInterfaceService {

    /**
     * Lists all matching physical interfaces.
     * @param locationFilter a regular expression to filter for network facilities.
     * @param interfaceFilter a regular expression to filter for interface name and alias.
     * @param offset the search offset
     * @param limit the maximum number of returned matching interfaces.
     * @return a list of matching physical interfaces.
     */
	List<PhysicalInterfaceData> findPhysicalInterfaces(String locationFilter,
	                                                   String interfaceFilter, 
	                                                   int offset, 
	                                                   int limit);

}
