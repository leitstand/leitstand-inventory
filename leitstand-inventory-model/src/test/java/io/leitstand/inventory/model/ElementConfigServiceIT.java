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

import static io.leitstand.inventory.model.Element.findElementByName;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupByName;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.service.ConfigurationState.ACTIVE;
import static io.leitstand.inventory.service.ConfigurationState.CANDIDATE;
import static io.leitstand.inventory.service.ConfigurationState.SUPERSEDED;
import static io.leitstand.inventory.service.ElementConfigId.randomConfigId;
import static io.leitstand.inventory.service.ElementConfigName.elementConfigName;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.ReasonCode.IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND;
import static io.leitstand.security.auth.UserName.userName;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementConfigEvent;
import io.leitstand.inventory.event.ElementConfigRevisionRemovedEvent;
import io.leitstand.inventory.event.ElementConfigStoredEvent;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigReference;
import io.leitstand.inventory.service.ElementConfigRevisions;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.StoreElementConfigResult;

public class ElementConfigServiceIT extends InventoryIT {
	
	private static final ElementName ELEMENT_NAME = elementName("config_test");
	private static final ElementId	 ELEMENT_ID	  = randomElementId();

	private ElementConfigService service;
	private ArgumentCaptor<ElementConfigEvent> eventCaptor;

	@Before
	public void initTestEnvironment() {
		Repository repository = new Repository(getEntityManager());
		ElementProvider elements = new ElementProvider(repository);
		DatabaseService db = getDatabase();
		Event<ElementConfigEvent> event = mock(Event.class);
		eventCaptor = forClass(ElementConfigEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		
		ElementConfigManager configs = new ElementConfigManager(repository,
																db,
																userName("unittest"),
																event,
																mock(Messages.class));
		service = new DefaultElementConfigService(elements,configs);
		
		transaction(()->{
			ElementGroup moduleTestGroup = repository.addIfAbsent(findElementGroupByName(groupType("unittest"), 
																						 groupName("module_test")), 
																  () -> new ElementGroup(randomGroupId(), 
																		  				 groupType("unittest"), 
																		  				 groupName("module_test")));
			
			repository.flush();
			
			ElementRole role = repository.addIfAbsent(findRoleByName(elementRoleName("module_test")), 
							 						   () -> new ElementRole(elementRoleName("module_test"),DATA)); 
			
			repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
								   () -> new Element(moduleTestGroup,
									  			     role, 
													 ELEMENT_ID,
													 ELEMENT_NAME));
		});
		
	}
	
	@Test
	public void raise_exception_if_config_does_not_exist() {
		transaction(()->{
			try {
				service.getElementConfig(ELEMENT_NAME, ElementConfigId.randomConfigId());
				fail("Exception expected!");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND,e.getReason());
			}			
		});
	}
	
	@Test
	public void raise_exception_if_active_config_does_not_exist() {
		transaction(()->{
			try {
				service.getActiveElementConfig(ELEMENT_NAME, elementConfigName("non-existent"));
				fail("Exception expected!");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND,e.getReason());
			}			
		});
		
		transaction(()->{
			try {
				service.getActiveElementConfig(ELEMENT_ID, elementConfigName("non-existent"));
				fail("Exception expected!");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND,e.getReason());
			}			
		});

	}
	
	@Test
	public void add_new_active_configuration() {
		ElementConfigName configName = elementConfigName("new_configuration");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE,
									   ACTIVE,
									   "Config 1", 
									   "First version");
		});
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, 
									 							  elementConfigName("new_configuration"));
			assertNotNull(config);
			assertEquals("text/plain",config.getContentType());
			assertEquals("Config 1",config.getConfig());
			assertEquals("First version",config.getComment());
		});
		
	}
	
	@Test
	public void add_new_candidate_configuration() {
		ElementConfigName configName = elementConfigName("updated_configuration");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE, 
									   ACTIVE,
									   "Config 1", 
									   "First version");
		});
		
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE,
									   ACTIVE,
									   "Config 2", 
									   "Updated version");
		});
		
		assertThat(eventCaptor.getValue(),is(ElementConfigStoredEvent.class));
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, 
									 							  configName);
			assertNotNull(config);
			assertEquals("text/plain",config.getContentType());
			assertEquals("Config 2",config.getConfig());
			assertEquals("Updated version",config.getComment());
		});
		
	}
	
	@Test
	public void activate_candidate_configuration() {
		ElementConfigName configName = elementConfigName("activate_candidate_configuration");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE, 
									   CANDIDATE,
									   "Config 1", 
									   "First version");
		});
		
		transaction(()->{
			try {
				service.getActiveElementConfig(ELEMENT_ID, configName);
				fail("EntityNotFoundException expected");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND, e.getReason() );
			}
			
			StoreElementConfigResult result = service.storeElementConfig(ELEMENT_ID, 
																		 configName, 
																		 TEXT_PLAIN_TYPE,
																		 ACTIVE,
																		 "Config 1", 
																		 "First version");
			assertFalse(result.isCreated());
		});
		
		assertThat(eventCaptor.getValue(),is(ElementConfigStoredEvent.class));
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, 
									 							  configName);
			assertNotNull(config);
			assertEquals(ACTIVE,config.getConfigState());
			assertEquals("text/plain",config.getContentType());
			assertEquals("Config 1",config.getConfig());
			assertEquals("First version",config.getComment());
			
			assertEquals(1,service.getElementConfigRevisions(ELEMENT_ID, configName).getRevisions().size());
		});
		
	}
	
	
	

	
	@Test
	public void read_history_revision() {
		ElementConfigName configName = elementConfigName("history_revision");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE,
									   SUPERSEDED,
									   "Config 1", 
									   "First version");
		});
		
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE, 
									   ACTIVE,
									   "Config 2", 
									   "Second version");
		});
		
		transaction(()->{
			ElementConfigId configId = service.getElementConfigRevisions(ELEMENT_ID,configName)
											  .getRevisions()
											  .get(1)
											  .getConfigId();
			
			ElementConfig config = service.getElementConfig(ELEMENT_ID, 
														    configId);
			assertNotNull(config);
			assertEquals(configName,config.getConfigName());
			assertEquals("text/plain",config.getContentType());
			assertEquals("Config 1",config.getConfig());
			assertEquals("First version",config.getComment());
		});
	}
	
	@Test
	public void do_not_remove_active_config() {
		ElementConfigName configName = elementConfigName("cannot_remove_active_config");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE, 
									   SUPERSEDED,
									   "Config 1", 
									   "First version");
			
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE, 
									   ACTIVE,
									   "Config 2", 
									   "Second version");
			
			
			service.storeElementConfig(ELEMENT_ID, 
					   				   configName, 
					   				   TEXT_PLAIN_TYPE, 
					   				   CANDIDATE,
					   				   "Config 3", 
					   				   "Candidate version");
			
		});
		
		transaction(()->{
			assertEquals(3,service.getElementConfigRevisions(ELEMENT_ID, configName).getRevisions().size());
			for(ElementConfigReference rev : service.getElementConfigRevisions(ELEMENT_ID, configName).getRevisions()) {
				System.out.println(rev.getConfigState());
			}
			service.removeElementConfig(ELEMENT_ID, configName);
		});
		
		
		transaction(()->{
			ElementConfigRevisions revisions = service.getElementConfigRevisions(ELEMENT_ID, configName);
			assertEquals(1,revisions.getRevisions().size());
			assertEquals(ACTIVE,revisions.getRevisions().get(0).getConfigState());
		});
		
	}
	
	
	@Test
	public void remove_config_revision() {
		ElementConfigName configName = elementConfigName("remove_config_revision");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE,
									   SUPERSEDED,
									   "Config 1", 
									   "First version");

			service.storeElementConfig(ELEMENT_ID, 
					   				   configName, 
					   				   TEXT_PLAIN_TYPE,
					   				   SUPERSEDED,
					   				   "Config 2", 
					   				   "Second version");
			
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE,
									   ACTIVE,
									   "Config 3", 
									   "Third version");
		});
		
		transaction(()->{
			ElementConfigRevisions revisions = service.getElementConfigRevisions(ELEMENT_ID, 
									 								 			 configName); 
			assertEquals("Third version",revisions.getRevisions().get(0).getComment());
			assertEquals("Second version",revisions.getRevisions().get(1).getComment());
			assertEquals("First version",revisions.getRevisions().get(2).getComment());
			service.removeElementConfig(ELEMENT_ID, revisions.getRevisions().get(1).getConfigId());
		});
		
		assertThat(eventCaptor.getValue(),is(ElementConfigRevisionRemovedEvent.class));

		
		transaction(()->{
			ElementConfigRevisions revisions = service.getElementConfigRevisions(ELEMENT_ID, 
																				 configName); 
			assertEquals("Third version",revisions.getRevisions().get(0).getComment());
			assertEquals("First version",revisions.getRevisions().get(1).getComment());

		});
		
	}
	
	@Test
	public void update_comment_of_existing_config() {
		ElementConfigName configName = elementConfigName("update_comment_of_existing_config");
		transaction(()->{
			service.storeElementConfig(ELEMENT_ID, 
									   configName, 
									   TEXT_PLAIN_TYPE, 
									   ACTIVE,
									   "Config 1", 
									   "First version");
		});
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, configName);
			assertNotNull(config);
			assertEquals("text/plain",config.getContentType());
			assertEquals("Config 1",config.getConfig());
			assertEquals("First version",config.getComment());
			service.setElementConfigComment(ELEMENT_ID, 
						  				    config.getConfigId(),
											"Initial version");
		});
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, configName);
			assertNotNull(config);
			assertEquals("text/plain",config.getContentType());
			assertEquals("Config 1",config.getConfig());
			assertEquals("Initial version",config.getComment());
		});
		
	}
	
	
	@Test
	public void comment_update_of_unknown_config_raises_exception() {
		transaction(()->{
			try {
				service.setElementConfigComment(ELEMENT_ID, 
										   	  	randomConfigId(), 
												"Initial version");
			} catch (EntityNotFoundException e) {
				assertEquals(IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND,e.getReason());
			}
		});
	}
		
}
