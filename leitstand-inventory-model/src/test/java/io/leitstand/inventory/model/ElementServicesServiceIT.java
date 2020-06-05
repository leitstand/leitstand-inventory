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
import static io.leitstand.inventory.model.Element_Service.removeServices;
import static io.leitstand.inventory.model.Platform.findByPlatformId;
import static io.leitstand.inventory.model.Service.findService;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementServiceReference.newElementServiceReference;
import static io.leitstand.inventory.service.ElementServiceSubmission.newElementServiceSubmission;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.ServiceName.serviceName;
import static io.leitstand.inventory.service.ServiceType.CONTAINER;
import static io.leitstand.inventory.service.ServiceType.DAEMON;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import javax.enterprise.event.Event;
import javax.inject.Provider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ElementServiceContext;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServicesService;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.ServiceInfo;
import io.leitstand.inventory.service.ServiceName;

public class ElementServicesServiceIT extends InventoryIT {

	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("unittest");
	private static final ElementGroupName GROUP_NAME = groupName(ElementServicesServiceIT.class.getName());
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementRoleName ROLE_NAME = elementRoleName(ElementServicesServiceIT.class.getSimpleName());
	private static final PlatformName PLATFORM_NAME = platformName(ElementServicesServiceIT.class.getName());
	private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("unittest");
	private static final PlatformId PLATFORM_ID = randomPlatformId();
	private static final ServiceName CONTAINER_SERVICE = serviceName(ElementServicesServiceIT.class.getName()+".container");
	private static final ServiceName DAEMON_SERVICE = serviceName(ElementServicesServiceIT.class.getName()+".daemon");
	
	
	private ElementServicesService service;
	
	private Messages messages;
	private ElementProvider elements;
	private ElementGroupProvider groups;
	private ElementRoleProvider roles;
	private Repository repository;
	
	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		DatabaseService database = getDatabase();
		this.elements = new ElementProvider(repository);
		this.groups = new ElementGroupProvider(repository);
		this.roles = new ElementRoleProvider(repository);
		Event event = mock(Event.class);
		messages = mock(Messages.class);
		
		ElementServicesManager manager = new ElementServicesManager(repository, getDatabase(), new SubtransactionService() {
			
			@Override
			protected Provider<SubtransactionService> getServiceProvider() {
				return () -> this;
			}
			
			@Override
			protected Repository getRepository() {
				return repository;
			}
		}, elements, messages);
		
		service = new DefaultElementServicesService(elements, manager);
		
		// Prepare group, role, platform, metric, services and the element
		transaction(() -> {
			repository.addIfAbsent(findService(CONTAINER_SERVICE),
								   () -> new Service(CONTAINER, CONTAINER_SERVICE));

			repository.addIfAbsent(findService(DAEMON_SERVICE),
								   () -> new Service(DAEMON, DAEMON_SERVICE));

			
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID),
														() -> new ElementGroup(GROUP_ID, 
																			   GROUP_TYPE, 
																			   GROUP_NAME));
	
			Platform platform = repository.addIfAbsent(findByPlatformId(PLATFORM_ID),
													   () -> new Platform(PLATFORM_ID,
															   			  PLATFORM_NAME,
															   			  PLATFORM_CHIPSET));
	
			ElementRole role = repository.addIfAbsent(findRoleByName(ROLE_NAME),
													  () -> new ElementRole(ROLE_NAME,
										   				 				    DATA));
			
			repository.addIfAbsent(findElementById(ELEMENT_ID),
								   () -> {
										 Element newElement = new Element(group,role,ELEMENT_ID,ELEMENT_NAME);
										 newElement.setPlatform(platform);
										 return newElement;
								   });
	

		});
	}
	
	@After
	public void clearTestEnvironment() {
		transaction(()->{
			Element element = elements.fetchElement(ELEMENT_ID);
			repository.execute(removeServices(element));
			for(Service service : repository.execute(Service.findAllServices())) {
				repository.remove(service);
			}
		});
	}
	
	
	@Test
	public void store_element_service_for_a_new_service_of_an_element_identified_by_id() {
		
		transaction(() -> {
			ElementServiceSubmission submission = newElementServiceSubmission()
					.withServiceName(serviceName("new-service"))
					.withOperationalState(UP)
					.build();
			
			service.storeElementService(ELEMENT_ID, submission);
		});
		
		transaction(() -> {
			ElementServiceContext serviceContext = service.getElementService(ELEMENT_ID, serviceName("new-service"));
			assertEquals(GROUP_ID,serviceContext.getGroupId());
			assertEquals(GROUP_TYPE,serviceContext.getGroupType());
			assertEquals(GROUP_NAME,serviceContext.getGroupName());
			assertEquals(ROLE_NAME,serviceContext.getElementRole());
			assertEquals(ELEMENT_ID,serviceContext.getElementId());
			assertEquals(ELEMENT_NAME,serviceContext.getElementName());
			assertEquals(serviceName("new-service"), serviceContext.getService().getServiceName());
			
		});
		
	}
	
	@Test
	public void store_element_service_for_a_new_service_of_an_element_identified_by_name() {
		
		transaction(() -> {
			ElementServiceSubmission submission = newElementServiceSubmission()
					.withServiceName(serviceName("new-service"))
					.withOperationalState(UP)
					.build();
			
			service.storeElementService(ELEMENT_NAME, submission);
		});
		
		transaction(() -> {
			ElementServiceContext serviceContext = service.getElementService(ELEMENT_NAME, serviceName("new-service"));
			assertEquals(GROUP_ID,serviceContext.getGroupId());
			assertEquals(GROUP_TYPE,serviceContext.getGroupType());
			assertEquals(GROUP_NAME,serviceContext.getGroupName());
			assertEquals(ROLE_NAME,serviceContext.getElementRole());
			assertEquals(ELEMENT_ID,serviceContext.getElementId());
			assertEquals(ELEMENT_NAME,serviceContext.getElementName());
			assertEquals(serviceName("new-service"), serviceContext.getService().getServiceName());
			
		});
		
	}
	
	@Test
	public void store_element_service_for_an_existing_service_of_an_element_identified_by_id() {
		transaction(() -> {
			ElementServiceSubmission submission = newElementServiceSubmission()
					.withServiceName(CONTAINER_SERVICE)
					.withOperationalState(UP)
					.build();
			
			service.storeElementService(ELEMENT_ID, submission);
		});
		
		transaction(() -> {
			ElementServiceContext serviceContext = service.getElementService(ELEMENT_ID, CONTAINER_SERVICE);
			assertEquals(GROUP_ID,serviceContext.getGroupId());
			assertEquals(GROUP_TYPE,serviceContext.getGroupType());
			assertEquals(GROUP_NAME,serviceContext.getGroupName());
			assertEquals(ROLE_NAME,serviceContext.getElementRole());
			assertEquals(ELEMENT_ID,serviceContext.getElementId());
			assertEquals(ELEMENT_NAME,serviceContext.getElementName());
			assertEquals(CONTAINER_SERVICE, serviceContext.getService().getServiceName());
			
		});
	}
	
	@Test
	public void store_element_service_for_an_existing_service_of_an_element_identified_by_name() {
		transaction(() -> {
			ElementServiceSubmission submission = newElementServiceSubmission()
					.withServiceName(CONTAINER_SERVICE)
					.withOperationalState(UP)
					.build();
			
			service.storeElementService(ELEMENT_NAME, submission);
		});
		
		transaction(() -> {
			ElementServiceContext serviceContext = service.getElementService(ELEMENT_NAME, CONTAINER_SERVICE);
			assertEquals(GROUP_ID,serviceContext.getGroupId());
			assertEquals(GROUP_TYPE,serviceContext.getGroupType());
			assertEquals(GROUP_NAME,serviceContext.getGroupName());
			assertEquals(ROLE_NAME,serviceContext.getElementRole());
			assertEquals(ELEMENT_ID,serviceContext.getElementId());
			assertEquals(ELEMENT_NAME,serviceContext.getElementName());
			assertEquals(CONTAINER_SERVICE, serviceContext.getService().getServiceName());
			
		});
	}
	
	@Test
	public void store_element_service_hierarchy_for_an_element_identified_by_id() {
		transaction(() -> {
			ElementServiceSubmission container = newElementServiceSubmission()
												 .withServiceName(CONTAINER_SERVICE)
												 .withOperationalState(UP)
												 .build();
			ElementServiceSubmission daemon = newElementServiceSubmission()
											  .withServiceName(DAEMON_SERVICE)
											  .withOperationalState(UP)
											  .withParentService(newElementServiceReference()
													  			 .withElementName(ELEMENT_NAME)
													  			 .withServiceName(CONTAINER_SERVICE))
											 .build();
			
			service.storeElementServices(ELEMENT_NAME, asList(container,daemon));
		});
		
		transaction(() -> {
			ElementServiceContext stack = service.getElementService(ELEMENT_NAME, DAEMON_SERVICE);
			assertEquals(GROUP_ID,stack.getGroupId());
			assertEquals(GROUP_TYPE,stack.getGroupType());
			assertEquals(GROUP_NAME,stack.getGroupName());
			assertEquals(ROLE_NAME,stack.getElementRole());
			assertEquals(ELEMENT_ID,stack.getElementId());
			assertEquals(ELEMENT_NAME,stack.getElementName());
			assertEquals(DAEMON_SERVICE, stack.getService().getServiceName());
			assertEquals(CONTAINER_SERVICE, stack.getService().getParent().getServiceName());
			
		});
	}
	
	@Test
	public void store_element_service_hierarchy_for_an_element_identified_by_name() {
		transaction(() -> {
			ElementServiceSubmission container = newElementServiceSubmission()
												 .withServiceName(CONTAINER_SERVICE)
												 .withOperationalState(UP)
												 .build();
			ElementServiceSubmission daemon = newElementServiceSubmission()
											  .withServiceName(DAEMON_SERVICE)
											  .withOperationalState(UP)
											  .withParentService(newElementServiceReference()
													  			 .withElementName(ELEMENT_NAME)
													  			 .withServiceName(CONTAINER_SERVICE))
											 .build();
			
			service.storeElementServices(ELEMENT_NAME, asList(container,daemon));
		});
		
		transaction(() -> {
			ElementServiceContext stack = service.getElementService(ELEMENT_NAME, DAEMON_SERVICE);
			assertEquals(GROUP_ID,stack.getGroupId());
			assertEquals(GROUP_TYPE,stack.getGroupType());
			assertEquals(GROUP_NAME,stack.getGroupName());
			assertEquals(ROLE_NAME,stack.getElementRole());
			assertEquals(ELEMENT_ID,stack.getElementId());
			assertEquals(ELEMENT_NAME,stack.getElementName());
			assertEquals(DAEMON_SERVICE, stack.getService().getServiceName());
			assertEquals(CONTAINER_SERVICE, stack.getService().getParent().getServiceName());
			
		});
	}
	
	@Test
	public void update_service_state_for_service_of_element_identified_by_id() {
		transaction(() -> {
			ElementServiceSubmission container = newElementServiceSubmission()
												 .withServiceName(CONTAINER_SERVICE)
												 .withOperationalState(UP)
												 .build();
			ElementServiceSubmission daemon = newElementServiceSubmission()
											  .withServiceName(DAEMON_SERVICE)
											  .withOperationalState(UP)
											  .withParentService(newElementServiceReference()
													  			 .withElementName(ELEMENT_NAME)
													  			 .withServiceName(CONTAINER_SERVICE))
											 .build();
			
			service.storeElementServices(ELEMENT_ID, asList(container,daemon));
		});
		
		transaction(() -> {
			service.updateElementServiceOperationalState(ELEMENT_ID, CONTAINER_SERVICE,DOWN );
		});
		
		
		transaction(() -> {
			ElementServiceContext stack = service.getElementService(ELEMENT_ID, CONTAINER_SERVICE);
			assertEquals(DOWN,stack.getService().getOperationalState());
		});
		
	}
	
	@Test
	public void update_service_state_for_service_of_element_identified_by_name() {
		transaction(() -> {
			ElementServiceSubmission container = newElementServiceSubmission()
												 .withServiceName(CONTAINER_SERVICE)
												 .withOperationalState(UP)
												 .build();
			ElementServiceSubmission daemon = newElementServiceSubmission()
											  .withServiceName(DAEMON_SERVICE)
											  .withOperationalState(UP)
											  .withParentService(newElementServiceReference()
													  			 .withElementName(ELEMENT_NAME)
													  			 .withServiceName(CONTAINER_SERVICE))
											 .build();
			
			service.storeElementServices(ELEMENT_NAME, asList(container,daemon));
		});
		
		transaction(() -> {
			service.updateElementServiceOperationalState(ELEMENT_NAME, CONTAINER_SERVICE,DOWN );
		});
		
		
		transaction(() -> {
			ElementServiceContext stack = service.getElementService(ELEMENT_NAME, CONTAINER_SERVICE);
			assertEquals(DOWN,stack.getService().getOperationalState());
		});
		
	}
	
	@Test
	public void remove_service_of_element_identified_by_id() {
		transaction(() -> {
			ElementServiceSubmission container = newElementServiceSubmission()
												 .withServiceName(CONTAINER_SERVICE)
												 .withOperationalState(UP)
												 .build();
			ElementServiceSubmission daemon = newElementServiceSubmission()
											  .withServiceName(DAEMON_SERVICE)
											  .withOperationalState(UP)
											  .withParentService(newElementServiceReference()
													  			 .withElementName(ELEMENT_NAME)
													  			 .withServiceName(CONTAINER_SERVICE))
											 .build();
			
			service.storeElementServices(ELEMENT_ID, asList(container,daemon));
		});
		
		transaction(() -> {
			service.removeElementService(ELEMENT_ID, DAEMON_SERVICE);
		});
		
		
		transaction(() -> {
			List<ServiceInfo> services = service.getElementServices(ELEMENT_ID).getServices();
			assertThat(services,hasSizeOf(1));
			assertEquals(CONTAINER_SERVICE,services.get(0).getServiceName());
		});
		
		
	}
	
	@Test
	public void remove_service_of_element_identified_by_name() {
		transaction(() -> {
			ElementServiceSubmission container = newElementServiceSubmission()
												 .withServiceName(CONTAINER_SERVICE)
												 .withOperationalState(UP)
												 .build();
			ElementServiceSubmission daemon = newElementServiceSubmission()
											  .withServiceName(DAEMON_SERVICE)
											  .withOperationalState(UP)
											  .withParentService(newElementServiceReference()
													  			 .withElementName(ELEMENT_NAME)
													  			 .withServiceName(CONTAINER_SERVICE))
											 .build();
			
			service.storeElementServices(ELEMENT_NAME, asList(container,daemon));
		});
		
		transaction(() -> {
			service.removeElementService(ELEMENT_NAME, DAEMON_SERVICE);
		});
		
		
		transaction(() -> {
			List<ServiceInfo> services = service.getElementServices(ELEMENT_NAME).getServices();
			assertThat(services,hasSizeOf(1));
			assertEquals(CONTAINER_SERVICE,services.get(0).getServiceName());
		});
		
	}


	
}
