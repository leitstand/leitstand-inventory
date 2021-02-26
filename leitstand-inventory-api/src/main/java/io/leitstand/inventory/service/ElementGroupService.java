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
 * The <code>ElementGroupService</code> allows querying element groups and group statistics.
 */
public interface ElementGroupService {

    /**
     * Returns the group settings for all matching groups ordered by their name.
     * @param groupType the group type
     * @param filter a regular expression to filter groups by name
     * @param offset the read offset for matching groups. The first offset matching groups are skipped and not included in the result.
     * @param limit the maximum number of groups to return
     * @return all matching groups ordered by their name
     */
    List<ElementGroupSettings> findGroups(ElementGroupType groupType,
										  String filter, 
										  int offset, 
										  int limit);

    /**
     * Returns the group statistics for all matching groups. 
     * The group statistics contain the number of elements grouped by operational state.
     * @param groupType the group type
     * @param filter a regular expression to filter groups by name.
     * @return the group statistics for all matching groups.
     */
	List<ElementGroupStatistics> getGroupStatistics(ElementGroupType groupType, 
													String filter);



}
