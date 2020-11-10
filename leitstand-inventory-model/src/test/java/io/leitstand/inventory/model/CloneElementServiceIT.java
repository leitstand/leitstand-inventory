package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Platform.findPlatformById;
import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementCloneRequest.newCloneElementRequest;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.ReasonCode.IVT0306I_ELEMENT_CLONED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.enterprise.event.Event;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementClonedEvent;
import io.leitstand.inventory.event.ElementEvent;
import io.leitstand.inventory.service.CloneElementService;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementCloneRequest;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;

public class CloneElementServiceIT extends InventoryIT {
	
	private static final ElementGroupId GROUP_ID = randomGroupId();
	private static final ElementGroupType GROUP_TYPE = ElementGroupType.groupType("clone-tests");
	private static final ElementGroupName GROUP_NAME = groupName("clone-tests");
	private static final ElementRoleName ELEMENT_ROLE = elementRoleName("CLONE");
	private static final ElementId ELEMENT_ID = randomElementId();
	private static final ElementName ELEMENT_NAME = elementName("source");
	private static final ElementAlias ELEMENT_ALIAS = elementAlias("source-alias");
	private static final PlatformId PLATFORM_ID = randomPlatformId();
	private static final PlatformName PLATFORM_NAME = platformName("clone-platform");
	private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("unittest-chipset");
	
	private CloneElementService service;
	private Event<ElementEvent> sink;
	private Messages messages;
	private Repository repository;
	
	@Before
	public void initTestEnvironment() {
		sink = mock(Event.class);
		messages = mock(Messages.class);
		repository = new Repository(this.getEntityManager());
		DatabaseService db = spy(getDatabase());
		DefaultCloneElementService.SequenceGenerator sequence = mock(DefaultCloneElementService.SequenceGenerator.class);
		when(sequence.acquireCloneId()).thenReturn(1000000000L);
		
		service = new DefaultCloneElementService(new ElementProvider(repository),db,sequence, sink,messages);
		
		transaction(()->{
			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
													  () -> new ElementRole(ELEMENT_ROLE, 
															  				DATA));
			
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID),
														() -> new ElementGroup(GROUP_ID, 
																			   GROUP_TYPE, 
																			   GROUP_NAME));
			
			
			Platform platform = repository.addIfAbsent(findPlatformById(PLATFORM_ID),
													   () -> new Platform(PLATFORM_ID,
															   			  PLATFORM_NAME,
															   			  PLATFORM_CHIPSET));
			
			Element element = repository.addIfAbsent(findElementById(ELEMENT_ID), 
													 () -> new Element(group,
															 		   role,
															 		   platform,
															 		   ELEMENT_ID,
															 		   ELEMENT_NAME));
			
			element.setElementAlias(ELEMENT_ALIAS);
			element.setSerialNumber("source-serial");
			element.setManagementInterfaceMacAddress(macAddress("00:12:34:56:78:9A"));
			element.setAdministrativeState(ACTIVE);
			element.setOperationalState(UP);
			
			
		});
		
	}
	
	@Test
	public void clone_element_stub() {
		ElementCloneRequest cloneRequest = newCloneElementRequest()
										  .withElementId(randomElementId())
										  .withElementName(elementName("clone-name"))
										  .withElementAlias(elementAlias("clone-alias"))
										  .withSerialNumber("clone-serial")
										  .build();

		ArgumentCaptor<ElementClonedEvent> eventCaptor = ArgumentCaptor.forClass(ElementClonedEvent.class);
		doNothing().when(sink).fire(eventCaptor.capture());
		ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
		
		transaction(()->{
			ElementId cloneId = service.cloneElement(ELEMENT_ID, cloneRequest);
			assertEquals(cloneId,cloneRequest.getElementId());
		});
		
		transaction(()->{
			Element clone = repository.execute(findElementById(cloneRequest.getElementId()));
			assertEquals(cloneRequest.getElementId(),clone.getElementId());
			assertEquals(cloneRequest.getElementName(),clone.getElementName());
			assertEquals(cloneRequest.getElementAlias(),clone.getElementAlias());
			assertEquals(cloneRequest.getSerialNumber(),clone.getSerialNumber());
			assertEquals(NEW,clone.getAdministrativeState());
			assertEquals(DOWN,clone.getOperationalState());
			assertEquals(GROUP_ID,clone.getGroupId());
			assertEquals(GROUP_NAME,clone.getGroupName());
			assertEquals(GROUP_TYPE,clone.getGroupType());
			assertNull(clone.getManagementInterfaceMacAddress());
		});
		
		ElementClonedEvent event = eventCaptor.getValue();
		assertThat(event,CoreMatchers.is(CoreMatchers.instanceOf(ElementClonedEvent.class)));
		
		assertEquals(cloneRequest.getElementId(),event.getCloneId());
		assertEquals(cloneRequest.getElementName(),event.getCloneName());
		assertEquals(cloneRequest.getElementAlias(),event.getCloneAlias());
		assertEquals(cloneRequest.getSerialNumber(),event.getCloneSerialNumber());
		assertEquals(GROUP_ID,event.getGroupId());
		assertEquals(GROUP_NAME,event.getGroupName());
		assertEquals(GROUP_TYPE,event.getGroupType());
		assertEquals(ELEMENT_ID,event.getElementId());
		assertEquals(ELEMENT_NAME,event.getElementName());
		assertEquals(ELEMENT_ALIAS,event.getElementAlias());
		assertEquals(ELEMENT_ROLE,event.getElementRole());
		
		Message message = messageCaptor.getValue();
		assertEquals(message.getReason(),IVT0306I_ELEMENT_CLONED.getReasonCode());
		
	}
		
}
