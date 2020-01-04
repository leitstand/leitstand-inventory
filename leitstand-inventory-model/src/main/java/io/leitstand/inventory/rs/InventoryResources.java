/*

 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
						   ElementLinksResource.class,
						   ElementMetricsResource.class,
						   ElementModulesResource.class,
						   ElementServicesResource.class,
						   ElementSettingsResource.class,
						   ImageExportResource.class,
						   ImagesResource.class,
						   MetricsResource.class,
						   MetricVisualizationsResource.class,
						   MetricAlertRulesResource.class,
						   MetricExportResource.class,
						   PackageResource.class,
						   PhysicalInterfacesResource.class,
						   PlatformResource.class,
						   RackSettingsResource.class,
						   OperationalStateReader.class);
	}

}
