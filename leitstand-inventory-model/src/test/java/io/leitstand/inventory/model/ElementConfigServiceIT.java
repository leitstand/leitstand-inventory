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
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.lang.Math.abs;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementConfigEvent;
import io.leitstand.inventory.event.ElementConfigRevisionRemovedEvent;
import io.leitstand.inventory.event.ElementConfigStoredEvent;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigReference;
import io.leitstand.inventory.service.ElementConfigRevisions;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.security.auth.UserContext;
import io.leitstand.security.auth.UserName;

public class ElementConfigServiceIT extends InventoryIT {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    private static final ElementGroupId GROUP_ID = randomGroupId();
    private static final ElementGroupType GROUP_TYPE = groupType("unittest");
    private static final ElementGroupName GROUP_NAME = groupName("group");
    private static final ElementId	 ELEMENT_ID	  = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("element");
	private static final ElementRoleName ELEMENT_ROLE = elementRoleName("role");
	private static final ElementConfigName CONFIG_NAME = elementConfigName("config");
	private static final UserName USER_NAME = userName("unittest");
	private ElementConfigService service;
	private ArgumentCaptor<ElementConfigEvent> eventCaptor;
	private Repository repository;
	private ElementProvider elements;
	
	private void storeElementConfig(ElementId elementId,
									ElementConfigName configName,
									MediaType contentType,
									ConfigurationState configState,
									String content,
									String comment) {
		
		transaction(() -> {
			Element element = elements.fetchElement(elementId);
			Content metadata = new Content(contentType.toString(), "hash"+abs(content.hashCode()), Long.valueOf(content.length()));
			metadata = repository.merge(metadata);
			Element_Config config = new Element_Config( element,
					randomConfigId(),
					configName,
					configState,
					USER_NAME,
					metadata,
					comment);
			repository.add(config);
		});
		
	}
	
	@Before
	public void initTestEnvironment() {
		repository = new Repository(getEntityManager());
		elements = new ElementProvider(repository);
		DatabaseService db = getDatabase();
		Event<ElementConfigEvent> event = mock(Event.class);
		eventCaptor = forClass(ElementConfigEvent.class);
		doNothing().when(event).fire(eventCaptor.capture());
		UserContext userContext = mock(UserContext.class);
		when(userContext.getUserName()).thenReturn(USER_NAME);
		
		ElementConfigManager configs = new ElementConfigManager(repository,
																db,
																userContext,
																event,
																mock(Messages.class));
		service = new DefaultElementConfigService(elements,configs);
		
		transaction(()->{
			ElementGroup group = repository.addIfAbsent(findElementGroupByName(GROUP_TYPE, 
																			   GROUP_NAME), 
														() -> new ElementGroup(GROUP_ID, 
																		  	   GROUP_TYPE, 
																		  	   GROUP_NAME));
			
			repository.flush();
			
			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
							 						  () -> new ElementRole(ELEMENT_ROLE,DATA)); 
			
			repository.addIfAbsent(findElementByName(ELEMENT_NAME), 
								   () -> new Element(group,
									  			     role, 
													 ELEMENT_ID,
													 ELEMENT_NAME));
		});
		
	}
	
	@Test
	public void raise_exception_if_config_does_not_exist() {
	    exception.expect(EntityNotFoundException.class);
	    exception.expect(reason(IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND));
	    
		transaction(()->{
			service.getElementConfig(ELEMENT_NAME, randomConfigId());
		});
	}
	
	@Test
	public void raise_exception_if_active_config_does_not_exist_for_specified_element_id() {
	    exception.expect(EntityNotFoundException.class);
	    exception.expect(reason(IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND));
		transaction(()->{
			service.getActiveElementConfig(ELEMENT_ID, CONFIG_NAME);
		});
	}
	
   @Test
    public void raise_exception_if_active_config_does_not_exist_for_specified_element_name() {
        exception.expect(EntityNotFoundException.class);
        exception.expect(reason(IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND));
        transaction(()->{
            service.getActiveElementConfig(ELEMENT_NAME, CONFIG_NAME);
        });
    }
	
	@Test
	public void read_history_revision() {
		storeElementConfig(ELEMENT_ID, 
						   CONFIG_NAME, 
						   TEXT_PLAIN_TYPE,
						   SUPERSEDED,
						   "Config 1",
						   "First version");
		
		storeElementConfig(ELEMENT_ID, 
						   CONFIG_NAME, 
						   TEXT_PLAIN_TYPE, 
						   ACTIVE,
						   "Config 2",
						   "Second version");
		
		transaction(()->{
			ElementConfigId configId = service.getElementConfigRevisions(ELEMENT_ID,CONFIG_NAME)
											  .getRevisions()
											  .get(1)
											  .getConfigId();
			
			ElementConfig config = service.getElementConfig(ELEMENT_ID, 
														    configId);
			assertNotNull(config);
	        assertEquals(GROUP_ID,config.getGroupId());
	        assertEquals(GROUP_TYPE,config.getGroupType());
	        assertEquals(GROUP_NAME,config.getGroupName());
	        assertEquals(ELEMENT_ID,config.getElementId());
	        assertEquals(ELEMENT_NAME,config.getElementName());
	        assertEquals(CONFIG_NAME,config.getConfigName());
			assertEquals(CONFIG_NAME,config.getConfigName());
			assertEquals("text/plain",config.getContentType());
			assertEquals("First version",config.getComment());
		});
	}
	
	@Test
	public void do_not_remove_active_config() {
			storeElementConfig(ELEMENT_ID, 
							   CONFIG_NAME, 
							   TEXT_PLAIN_TYPE, 
							   SUPERSEDED,
							   "Config 1",
							   "First version");
			
			storeElementConfig(ELEMENT_ID, 
							   CONFIG_NAME, 
							   TEXT_PLAIN_TYPE, 
							   ACTIVE,
							   "Config 2",
							   "Second version");
			
			
			storeElementConfig(ELEMENT_ID, 
					   		   CONFIG_NAME, 
					   		   TEXT_PLAIN_TYPE, 
					   		   CANDIDATE,
					   		   "Config 3",
					   		   "Candidate version");
			
		
		transaction(()->{
			assertEquals(3,service.getElementConfigRevisions(ELEMENT_ID, CONFIG_NAME).getRevisions().size());
			for(ElementConfigReference rev : service.getElementConfigRevisions(ELEMENT_ID, CONFIG_NAME).getRevisions()) {
				System.out.println(rev.getConfigState());
			}
			service.removeElementConfigRevisions(ELEMENT_ID, CONFIG_NAME);
		});
		
		
		transaction(()->{
			ElementConfigRevisions revisions = service.getElementConfigRevisions(ELEMENT_ID, CONFIG_NAME);
			assertEquals(1,revisions.getRevisions().size());
			assertEquals(ACTIVE,revisions.getRevisions().get(0).getConfigState());
		});
		
	}
	
	
	@Test
	public void remove_config_revision() {
		storeElementConfig(ELEMENT_ID, 
						   CONFIG_NAME, 
						   TEXT_PLAIN_TYPE,
						   SUPERSEDED,
						   "Config 1",
						   "First version");

		storeElementConfig(ELEMENT_ID, 
				   		   CONFIG_NAME, 
				   		   TEXT_PLAIN_TYPE,
				   		   SUPERSEDED,
				   		   "Config 2",
				   		   "Second version");
		
		storeElementConfig(ELEMENT_ID, 
						   CONFIG_NAME, 
						   TEXT_PLAIN_TYPE,
						   ACTIVE,
						   "Config 3",
						   "Third version");
		
		transaction(()->{
			ElementConfigRevisions revisions = service.getElementConfigRevisions(ELEMENT_ID, 
									 								 			 CONFIG_NAME); 
			assertEquals("Third version",revisions.getRevisions().get(0).getComment());
			assertEquals("Second version",revisions.getRevisions().get(1).getComment());
			assertEquals("First version",revisions.getRevisions().get(2).getComment());
			service.removeElementConfig(ELEMENT_ID, revisions.getRevisions().get(1).getConfigId());
		});
		
		assertThat(eventCaptor.getValue(),is(ElementConfigRevisionRemovedEvent.class));

		
		transaction(()->{
			ElementConfigRevisions revisions = service.getElementConfigRevisions(ELEMENT_ID, 
																				 CONFIG_NAME); 
			assertEquals("Third version",revisions.getRevisions().get(0).getComment());
			assertEquals("First version",revisions.getRevisions().get(1).getComment());

		});
		
	}
	
	@Test
	public void update_comment_of_existing_config() {
		storeElementConfig(ELEMENT_ID, 
						   CONFIG_NAME, 
						   TEXT_PLAIN_TYPE, 
						   ACTIVE,
						   "Config 1",
						   "First version");
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, CONFIG_NAME);
			assertNotNull(config);
			assertEquals("text/plain",config.getContentType());
			assertEquals("First version",config.getComment());
			service.setElementConfigComment(ELEMENT_ID, 
						  				    config.getConfigId(),
											"Initial version");
		});
		
		transaction(()->{
			ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, CONFIG_NAME);
			assertNotNull(config);
			assertEquals("text/plain",config.getContentType());
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
	
	@Test
	public void read_active_configuration() {
	    

		storeElementConfig(ELEMENT_ID, 
                           CONFIG_NAME, 
                           TEXT_PLAIN_TYPE,
                           ACTIVE,
                           "Config 1",
                           "Active version");
            
        storeElementConfig(ELEMENT_ID, 
                           CONFIG_NAME, 
                           TEXT_PLAIN_TYPE,
                           CANDIDATE,
                           "Config 2",
                           "Candidate config");
	    
	    transaction(() -> {
	       ElementConfig config = service.getActiveElementConfig(ELEMENT_ID, CONFIG_NAME);
	       assertThat(config.getConfigState(),is(ACTIVE));
	    });
 	    
	}
	
	@Test
	public void read_candidate_configuration() {
       
        
        storeElementConfig(ELEMENT_ID, 
                           CONFIG_NAME, 
                           TEXT_PLAIN_TYPE,
                           ACTIVE,
                           "Config 1",
        				   "Active version");
        
        storeElementConfig(ELEMENT_ID, 
                           CONFIG_NAME, 
                           TEXT_PLAIN_TYPE,
                           CANDIDATE,
                           "Config 2",
                           "Candidate config");
            
            
        
        transaction(() -> {
           ElementConfig config = service.getElementConfig(ELEMENT_ID, CONFIG_NAME);
           assertThat(config.getConfigState(),is(CANDIDATE));
        });
	}
		
}
