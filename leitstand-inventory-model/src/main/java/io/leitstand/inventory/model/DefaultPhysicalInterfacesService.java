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

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.jpa.AdministrativeStateConverter.toAdministrativeState;
import static io.leitstand.inventory.jpa.OperationalStateConverter.toOperationalState;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.groupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.elementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.PhysicalInterfaceData.newPhysicalInterfaceData;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.PhysicalInterfaceData;
import io.leitstand.inventory.service.PhysicalInterfaceService;

@Service
public class DefaultPhysicalInterfacesService implements PhysicalInterfaceService {
 
    @Inject
    @Inventory
    private DatabaseService db;
    
    @Override
    public List<PhysicalInterfaceData> findPhysicalInterfaces(String filter, int offset, int limit) {
        
        return db.executeQuery(
                  prepare("SELECT g.uuid,g.name,g.type,e.uuid,e.name,e.alias,r.name,e.admstate,e.opstate,e.tsmodified,ifp.name,ifp.alias,ifp.opstate,ifp.admstate "+
                          "FROM inventory.element_ifp ifp "+
                          "JOIN inventory.element e "+
                          "ON ifp.element_id = e.id "+
                          "JOIN inventory.elementgroup g "+
                          "ON e.elementgroup_id = g.id "+
                          "JOIN inventory.elementrole r "+
                          "ON e.elementrole_id = r.id "+
                          "WHERE ifp.name = ? OR ifp.alias ~ ? "+
                          "ORDER BY g.name, e.name "+
                          "OFFSET ? LIMIT ? ",
                          filter,
                          filter,
                          offset,
                          limit), 
                  rs -> newPhysicalInterfaceData()
                        .withGroupId(groupId(rs.getString(1)))
                        .withGroupName(groupName(rs.getString(2)))
                        .withGroupType(groupType(rs.getString(3)))
                        .withElementId(elementId(rs.getString(4)))
                        .withElementName(elementName(rs.getString(5)))
                        .withElementAlias(elementAlias(rs.getString(6)))
                        .withElementRole(elementRoleName(rs.getString(7)))
                        .withAdministrativeState(toAdministrativeState(rs.getString(8)))
                        .withOperationalState(toOperationalState(rs.getString(9)))
                        .withDateModified(rs.getTimestamp(10))
                        .withIfpName(interfaceName(rs.getString(11)))
                        .withIfpAlias(rs.getString(12))
                        .withIfpOperationalState(toOperationalState(rs.getString(13)))
                        .withIfpAdministrativeState(toAdministrativeState(rs.getString(14)))
                        .build());
    
    }

}
