/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.RackName.rackName;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupRackService;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackSettings;

@RunWith(MockitoJUnitRunner.class)
public class ElementGroupRackResourceTest {
	
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = groupType("type");
	private static final ElementGroupName GROUP_NAME = groupName("group");
	private static final RackName RACK_NAME = rackName("rack");
	private static final RackSettings RACK = newRackSettings()
											 .withRackName(RACK_NAME)
											 .build();
	
	@Mock
	private ElementGroupRackService service;
	
	@Mock
	private Messages messages;
	
	@InjectMocks
	private ElementGroupRackResource resource = new ElementGroupRackResource();
	
	@Test
	public void send_created_response_when_adding_a_new_rack_for_group_identified_by_id() {
		when(service.storeRack(GROUP_ID, RACK_NAME, RACK)).thenReturn(true);
		Response response = resource.storeRack(GROUP_ID, RACK_NAME, RACK);
		assertEquals(201, response.getStatus());
	}
	
	
	@Test
	public void send_created_response_when_adding_a_new_rack_for_group_identified_by_name() {
		when(service.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, RACK)).thenReturn(true);
		Response response = resource.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, RACK);
		assertEquals(201, response.getStatus());
	}
	
	
	@Test
	public void send_success_response_when_adding_a_new_rack_for_group_identified_by_id() {
		Response response = resource.storeRack(GROUP_ID, RACK_NAME, RACK);
		assertEquals(200, response.getStatus());
	}
	
	
	@Test
	public void send_success_response_when_adding_a_new_rack_for_group_identified_by_name() {
		Response response = resource.storeRack(GROUP_TYPE, GROUP_NAME, RACK_NAME, RACK);
		assertEquals(200, response.getStatus());
	}
	
}
