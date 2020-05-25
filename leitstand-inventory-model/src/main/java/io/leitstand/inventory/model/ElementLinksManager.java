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
import static io.leitstand.inventory.service.ElementGroupId.groupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.elementId;
import static io.leitstand.inventory.service.ElementLinkData.newElementLinkData;
import static io.leitstand.inventory.service.ElementLinks.newElementLinks;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.MACAddress.macAddress;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.db.ResultSetMapping;
import io.leitstand.inventory.service.Bandwidth;
import io.leitstand.inventory.service.Bandwidth.Unit;
import io.leitstand.inventory.service.ElementLinkData;
import io.leitstand.inventory.service.ElementLinks;

@Dependent
public class ElementLinksManager {

	private DatabaseService datasource;
	
	@Inject
	protected ElementLinksManager(@Inventory DatabaseService datasource){
		this.datasource = datasource;
	}
	
	public ElementLinks getElementLinks(Element element) {
		ElementGroup group = element.getGroup();
		ResultSetMapping<ElementLinkData> mapping = rs -> newElementLinkData()
														  .withLocalIfpName(interfaceName(rs.getString(1)))
														  .withLocalMac(macAddress(rs.getString(2)))
														  .withLocalOperationalState(toOperationalState(rs.getString(3)))
														  .withLocalAdministrativeState(toAdministrativeState(rs.getString(4)))
														  .withLocalBandwidth(new Bandwidth(rs.getFloat(5), Unit.valueOf(rs.getString(6))))
														  .withRemoteIfpName(interfaceName(rs.getString(7)))
														  .withRemoteMac(macAddress(rs.getString(8)))
														  .withRemoteOperationalState(toOperationalState(rs.getString(9)))
														  .withRemoteAdministrativeState(toAdministrativeState(rs.getString(10)))
														  .withRemoteBandwidth(new Bandwidth(rs.getFloat(11),Unit.valueOf(rs.getString(12))))
														  .withRemoteElementId(elementId(rs.getString(13)))
														  .withRemoteElementName(elementName(rs.getString(14)))
														  .withRemoteElementGroupId(groupId(rs.getString(15)))
														  .withRemoteElementGroupType(groupType(rs.getString(16)))
														  .withRemoteElementGroupName(groupName(rs.getString(17)))
														  .build();

		List<ElementLinkData> links = datasource.executeQuery(prepare("SELECT local_ifp.name, local_ifp.mac_address, local_ifp.opstate, local_ifp.admstate, local_ifp.bw_value, local_ifp.bw_unit, "+
																		   "neighbor_ifp.name, neighbor_ifp.mac_address, neighbor_ifp.opstate, neighbor_ifp.admstate, neighbor_ifp.bw_value, neighbor_ifp.bw_unit, "+
																		   "neighbor_element.uuid, neighbor_element.name, neighbor_group.uuid, neighbor_group.type, neighbor_group.name " +
																  	  "FROM inventory.element_ifp local_ifp "+ 
																  	  "JOIN inventory.element_ifp neighbor_ifp ON local_ifp.neighbor_element_id = neighbor_ifp.element_id AND local_ifp.neighbor_element_ifp_name = neighbor_ifp.name "+
																  	  "JOIN inventory.element neighbor_element ON neighbor_ifp.element_id = neighbor_element.id "+
																  	  "JOIN inventory.elementgroup neighbor_group ON neighbor_element.elementgroup_id = neighbor_group.id "+
																  	  "WHERE local_ifp.element_id = ?",
																  	  element.getId()), 
								 							  mapping);
		
		return newElementLinks()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withLinks(links)
			   .build();
	}

}
