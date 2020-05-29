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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.ObjectUtil.asSortedSet;

import java.util.Set;

import javax.enterprise.context.Dependent;

import io.leitstand.commons.rs.ApiResourceProvider;

/**
 * Provider of all inventory module REST API resources.
 */
@Dependent
public class InventoryResources implements ApiResourceProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Class<?>> getResources() {
		return asSortedSet((a,b) -> a.getName().compareTo(b.getName()),
						   CloneElementResource.class,
						   DnsZoneResource.class,
						   ElementConfigResource.class,
						   ElementDnsRecordSetResource.class,
						   ElementEnvironmentResource.class,
						   ElementGroupElementsResource.class,
						   ElementGroupExportResource.class,
						   ElementGroupSettingsResource.class,
						   ElementGroupsResource.class,
						   ElementGroupRacksResource.class,
						   ElementGroupRackResource.class,
						   ElementRackResource.class,
						   ElementResource.class,
						   ElementRoleResource.class,
						   ElementImageResource.class,
						   ElementPhysicalInterfaceResource.class,
						   ElementLogicalInterfaceResource.class,
						   ElementLinksResource.class,
						   ElementModulesResource.class,
						   ElementServicesResource.class,
						   ElementSettingsResource.class,
						   ImageExportResource.class,
						   ImagesResource.class,
						   PackageResource.class,
						   PhysicalInterfacesResource.class,
						   PlatformResource.class,
						   RackSettingsResource.class,
						   ServiceDefinitionResource.class,
						   OperationalStateReader.class);
	}

}
