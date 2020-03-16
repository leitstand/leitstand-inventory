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

import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Metric.findMetricByName;
import static io.leitstand.inventory.model.Platform.findByPlatformId;
import static io.leitstand.inventory.model.Service.findService;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.ConfigurationState.CANDIDATE;
import static io.leitstand.inventory.service.DnsName.dnsName;
import static io.leitstand.inventory.service.DnsRecord.newDnsRecord;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static io.leitstand.inventory.service.DnsRecordType.dnsRecordType;
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.DnsZoneSettings.newDnsZoneSettings;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementLogicalInterfaceSubmission.newElementLogicalInterfaceSubmission;
import static io.leitstand.inventory.service.ElementManagementInterface.newElementManagementInterface;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission.newPhysicalInterfaceSubmission;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementServiceReference.newElementServiceReference;
import static io.leitstand.inventory.service.ElementServiceSubmission.newElementServiceSubmission;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.Environment.newEnvironment;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static io.leitstand.inventory.service.EnvironmentName.environmentName;
import static io.leitstand.inventory.service.MetricName.metricName;
import static io.leitstand.inventory.service.ModuleData.newModuleData;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.ServiceName.serviceName;
import static io.leitstand.inventory.service.ServiceType.CONTAINER;
import static io.leitstand.inventory.service.ServiceType.DAEMON;
import static io.leitstand.security.auth.UserName.userName;
import static java.util.Arrays.asList;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.event.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.AddressInterface;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneService;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementDnsRecordSetService;
import io.leitstand.inventory.service.ElementEnvironmentService;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementLogicalInterfaceService;
import io.leitstand.inventory.service.ElementMetricService;
import io.leitstand.inventory.service.ElementModuleService;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementPhysicalInterfaceService;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementService;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServicesService;
import io.leitstand.inventory.service.ElementSettings;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.IPvxPrefix;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.RoutingInstanceName;
import io.leitstand.inventory.service.ServiceName;
import io.leitstand.security.auth.UserContext;

public class ForceRemoveElementIT extends InventoryIT{

	private static final DnsZoneId ZONE_ID = randomDnsZoneId();
	private static final DnsZoneName ZONE_NAME = dnsZoneName(ForceRemoveElementIT.class.getName());
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName(ForceRemoveElementIT.class.getName());
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName(ForceRemoveElementIT.class.getName());
	private static final ElementRoleName ROLE_NAME = elementRoleName(ForceRemoveElementIT.class.getSimpleName());
	private static final String VENDOR_NAME = ForceRemoveElementIT.class.getName();
	private static final PlatformId PLATFORM_ID = randomPlatformId();
	private static final MetricName METRIC_NAME = metricName(ForceRemoveElementIT.class.getSimpleName());
	private static final ServiceName CONTAINER_SERVICE = serviceName(ForceRemoveElementIT.class.getName()+".container");
	private static final ServiceName DAEMON_SERVICE = serviceName(ForceRemoveElementIT.class.getName()+".daemon");
	
	private ElementService service;
	private Messages messages;
	private ElementProvider elements;
	private ElementGroupProvider groups;
	private ElementRoleProvider roles;
	private PlatformProvider platforms;
	private MetricProvider metrics;
	private DnsZoneProvider zones;
	
	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		DatabaseService database = getDatabase();
		this.elements = new ElementProvider(repository);
		this.groups = new ElementGroupProvider(repository);
		this.roles = new ElementRoleProvider(repository);
		this.platforms = new PlatformProvider(repository);
		this.metrics = new MetricProvider(repository);
		this.zones = new DnsZoneProvider(repository);
		Event event = mock(Event.class);
		messages = mock(Messages.class);
		
		// Prepare group, role, platform, metric, services and the element
		transaction(() -> {
			repository.addIfAbsent(findService(CONTAINER_SERVICE),
								   () -> new Service(CONTAINER, CONTAINER_SERVICE));

			repository.addIfAbsent(findService(DAEMON_SERVICE),
								   () -> new Service(DAEMON, DAEMON_SERVICE));

			
			Metric metric = repository.addIfAbsent(findMetricByName(METRIC_NAME), 
												   () -> new Metric(METRIC_NAME));
			
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID),
														() -> new ElementGroup(GROUP_ID, 
																			   GROUP_TYPE, 
																			   GROUP_NAME));
	
			Platform platform = repository.addIfAbsent(findByPlatformId(PLATFORM_ID),
													   () -> new Platform(PLATFORM_ID, 
															   			  VENDOR_NAME, 
															   			  "unittest"));
	
			ElementRole role = repository.addIfAbsent(findRoleByName(ROLE_NAME),
													  () -> new ElementRole(ROLE_NAME,
										   				 				    DATA));
			
			Element element = repository.addIfAbsent(findElementById(ELEMENT_ID),
													 () -> {
														 Element newElement = new Element(group,role,ELEMENT_ID,ELEMENT_NAME);
														 newElement.setPlatform(platform);
														 return newElement;
													 });
	
			// Create element including management interfaces
			ElementSettings settings = newElementSettings()
									   .withGroupId(GROUP_ID)
									   .withGroupType(GROUP_TYPE)
									   .withGroupName(GROUP_NAME)
									   .withElementRole(ROLE_NAME)
									   .withElementId(ELEMENT_ID)
									   .withElementName(ELEMENT_NAME)
									   .withElementRole(ROLE_NAME)
									   .withAdministrativeState(RETIRED)
									   .withOperationalState(DOWN)
									   .withManagementInterfaces(newElementManagementInterface()
												  				 .withName("REST")
												  				 .withHostname("localhost"))
									   .build();
		
			ElementSettingsManager manager = new ElementSettingsManager(repository, 
																		groups, 
																		roles, 
																		platforms, 
																		elements, 
																		messages, 
																		event);
			
			manager.storeElementSettings(element, settings);

		});
		
		// Add physical interfaces
		transaction(()->{
			ElementPhysicalInterfaceManager ifpManager = new ElementPhysicalInterfaceManager(repository, 
																							 elements, 
																							 messages, 
																							 event);
			ElementPhysicalInterfaceService ifpService = new DefaultElementPhysicalInterfaceService(elements, 
																									ifpManager);
			ifpService.storePhysicalInterface(ELEMENT_ID, 
											  newPhysicalInterfaceSubmission()
											  .withIfpName(InterfaceName.valueOf("ifp-0/0/0"))
											  .withIfcName(InterfaceName.valueOf("ifc-0/0/0/0"))
											  .withMacAddress(MACAddress.valueOf("00:00:00:00:00"))
											  .build());
		});
		
		// Add logical interfaces
		transaction(()->{
			ElementLogicalInterfaceManager iflManager = new ElementLogicalInterfaceManager(repository, 
																						   elements, 
																						   messages, 
																						   event);
			ElementLogicalInterfaceService iflService = new DefaultElementLogicalInterfaceService(elements, 
																								  iflManager);
			iflService.storeLogicalInterface(ELEMENT_ID, 
											 newElementLogicalInterfaceSubmission()
											 .withIflName(InterfaceName.valueOf("lo-0/0/0/0/0"))
											 .withIfcName(InterfaceName.valueOf("ifc-0/0/0/0"))
											 .withRoutingInstanceName(RoutingInstanceName.valueOf("default"))
											 .withAddressInterfaces(AddressInterface.newAddressInterface().withAddress(IPvxPrefix.valueOf("10.0.0.1/32")))
											 .build());
		});
		
		// Add module including hierarchical modules
		transaction(()->{
			ElementModuleManager moduleManager = new ElementModuleManager(repository,messages);
			ElementModuleService moduleService = new DefaultElementModuleService(elements, 
																				 moduleManager);
			List<ModuleData> modules = new LinkedList<>();
			modules.add(newModuleData()
						.withModuleName(ModuleName.valueOf("parent"))
						.build());
			modules.add(newModuleData()
						.withModuleName(ModuleName.valueOf("child"))
						.withParentModule(ModuleName.valueOf("parent"))
						.build());
			
			moduleService.storeElementModules(ELEMENT_ID, modules);
		});
		
		// Add services including hierarchical services
		transaction(()->{
			ElementServicesManager serviceManager = new ElementServicesManager(repository,
																			   database,
																			   mock(SubtransactionService.class),
																			   elements,
																			   messages);
			ElementServicesService servicesService = new DefaultElementServicesService(elements, 
																				 	   serviceManager);
			List<ElementServiceSubmission> services = new LinkedList<>();
			services.add(newElementServiceSubmission()
						 .withServiceType(CONTAINER)
						 .withServiceName(CONTAINER_SERVICE)
						 .build());
			services.add(newElementServiceSubmission()
						.withServiceType(DAEMON)
						.withServiceName(DAEMON_SERVICE)
						.withParentService(newElementServiceReference()
										   .build())
						.build());
			
			servicesService.storeElementServices(ELEMENT_ID, services);
		});
		
		// Add configuration
		transaction(()->{
			UserContext userContext = mock(UserContext.class);
			when(userContext.getUserName()).thenReturn(userName("junit"));
			
			ElementConfigManager configManager = new ElementConfigManager(repository, 
																		  database, 
																		  userContext, 
																		  event, 
																		  messages);	
			ElementConfigService configService = new DefaultElementConfigService(elements, 
																				 configManager);
			
			
			configService.storeElementConfig(ELEMENT_ID, 
											 ElementConfigName.valueOf("config"), 
											 TEXT_PLAIN_TYPE, 
											 CANDIDATE, 
											 "dummy", 
											 "dummy configuration");
			
		});
		
		// Add environment
		transaction(()->{
			ElementEnvironmentManager envManager = new ElementEnvironmentManager(repository,
																				 event,
																				 messages);	
			ElementEnvironmentService envService = new DefaultElementEnvironmentService(elements, 
																				 		   envManager);
			
			Environment env = newEnvironment()
							  .withEnvironmentId(randomEnvironmentId())
							  .withEnvironmentName(environmentName("environment"))
							  .build();
			
			envService.storeElementEnvironment(ELEMENT_ID,
										  	   env);
			
		});
		
		// Add DNS record
		transaction(()->{
			DnsZoneService zoneService = new DefaultDnsZoneService(zones, new DnsZoneManager(repository, messages, event));
			
			
			zoneService.storeDnsZoneSettings(newDnsZoneSettings()
									  		 .withDnsZoneId(ZONE_ID)
									  		 .withDnsZoneName(ZONE_NAME)
									  		 .build());
			
			
			ElementDnsRecordSetManager dnsManager = new ElementDnsRecordSetManager(zones,
																				   repository,
																				   event,
																				   messages);	
			ElementDnsRecordSetService dnsService = new DefaultElementDnsRecordSetService(elements, 
																				 		  dnsManager);
			
			DnsRecordSet dns = newDnsRecordSet()
							   .withDnsZoneId(ZONE_ID)
							   .withDnsZoneName(ZONE_NAME)
							   .withDnsRecordSetId(randomDnsRecordSetId())
							   .withDnsName(dnsName("test.leitstand.io."+ZONE_NAME))
							   .withDnsRecordType(dnsRecordType("A"))
							   .withDnsRecords(newDnsRecord()
									  		   .withRecordValue("10.0.0.8"))
							   .build();
			
			dnsService.storeElementDnsRecordSet(ELEMENT_ID,
										  	    dns);
			
		});
		
		
		// Add metric assignment
		transaction(()->{
			ElementMetricManager manager = new ElementMetricManager(repository, 
																	new MetricProvider(repository), 
																	messages);
			ElementMetricService service = new DefaultElementMetricService(manager, elements);
			service.registerElementMetrics(ELEMENT_ID, asList(METRIC_NAME));
		});
		
		
		ElementManager elementManager = new ElementManager(repository,mock(Event.class),messages);

		service = new DefaultElementService(elementManager, 
											elements);


	}

	@Test
	public void force_remove_element_including_all_subresources_by_id() {
		transaction(()->{
			assertNotNull(elements.tryFetchElement(ELEMENT_ID));
			service.forceRemoveElement(ELEMENT_ID);
		});
		transaction(()->{
			assertNull(elements.tryFetchElement(ELEMENT_ID));
		});
	}
	
	@Test
	public void force_remove_element_including_all_subresources_by_name() {
		transaction(()->{
			assertNotNull(elements.tryFetchElement(ELEMENT_NAME));
			service.forceRemoveElement(ELEMENT_NAME);
		});
		transaction(()->{
			assertNull(elements.tryFetchElement(ELEMENT_NAME));
		});
	}
	
	
	@After
	public void verify_group_platform_and_role_were_not_removed() {
		assertNotNull(roles.fetchElementRole(ROLE_NAME));
		assertNotNull(groups.fetchElementGroup(GROUP_ID));
		assertNotNull(platforms.fetchPlatform(PLATFORM_ID));
		assertNotNull(metrics.fetchMetric(METRIC_NAME));
		
	}
	
}
