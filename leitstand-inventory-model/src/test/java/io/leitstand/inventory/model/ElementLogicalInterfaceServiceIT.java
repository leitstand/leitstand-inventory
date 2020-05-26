package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Element.findElementById;
import static io.leitstand.inventory.model.ElementGroup.findElementGroupById;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Element_ContainerInterface.findIfcByName;
import static io.leitstand.inventory.model.Element_PhysicalInterface.findIfpByName;
import static io.leitstand.inventory.model.Platform.findByPlatformId;
import static io.leitstand.inventory.service.AddressInterface.newAddressInterface;
import static io.leitstand.inventory.service.AddressInterface.AddressType.IPv4;
import static io.leitstand.inventory.service.Bandwidth.bandwidth;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementLogicalInterfaceSubmission.newElementLogicalInterfaceSubmission;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.IPvxPrefix.cidr;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.ReasonCode.IVT0361I_ELEMENT_IFL_STORED;
import static io.leitstand.inventory.service.RoutingInstanceName.routingInstance;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementLogicalInterfaceEvent;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.Bandwidth;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementLogicalInterface;
import io.leitstand.inventory.service.ElementLogicalInterfaceService;
import io.leitstand.inventory.service.ElementLogicalInterfaceSubmission;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;

public class ElementLogicalInterfaceServiceIT extends InventoryIT {

	private static final ElementGroupId   GROUP_ID     = randomGroupId();
	private static final ElementGroupType GROUP_TYPE   = groupType("pod");
	private static final ElementGroupName GROUP_NAME   = groupName("ifl");
	private static final ElementId 		  ELEMENT_ID   = randomElementId();
	private static final ElementName 	  ELEMENT_NAME = elementName("ifl-tests");
	private static final ElementRoleName  ELEMENT_ROLE = elementRoleName("ifl-test");
	private static final ElementAlias	  ELEMENT_ALIAS = elementAlias("ifl-element-alias");
	private static final PlatformId		  PLATFORM_ID  = PlatformId.randomPlatformId();
	private static final PlatformName	  PLATFORM_NAME = platformName("ifl-test");
	private static final InterfaceName 	  IFP_NAME		= interfaceName("ifp-0/0/1");
	private static final InterfaceName 	  IFP2_NAME		= interfaceName("ifp-0/0/2");
	private static final InterfaceName	  IFL_NAME		= interfaceName("ifl-0/0/1/1");
	private static final Bandwidth		  IFP_BANDWIDTH = bandwidth("10.000 Gbps");
	private static final MACAddress		  IFP_MAC		= macAddress("00:11:22:33:44:55");
	
	private ElementLogicalInterfaceService service;
	private Messages messages;
	private Event<ElementLogicalInterfaceEvent> event;
	
	@Before
	public void initTestEnvironment() {
		event = mock(Event.class);
		messages = mock(Messages.class);
		
		Repository repository = new Repository(getEntityManager());
		ElementProvider elements = new ElementProvider(repository);
		ElementLogicalInterfaceManager manager = new ElementLogicalInterfaceManager(repository, elements, messages, event);
		service = new DefaultElementLogicalInterfaceService(elements, manager);
		
		transaction(()->{
			ElementRole role = repository.addIfAbsent(findRoleByName(ELEMENT_ROLE), 
													  () -> new ElementRole(ELEMENT_ROLE, 
															  				DATA));
			ElementGroup group = repository.addIfAbsent(findElementGroupById(GROUP_ID), 
														() -> new ElementGroup(GROUP_ID, GROUP_TYPE, GROUP_NAME));
			Platform platform = repository.addIfAbsent(findByPlatformId(PLATFORM_ID), 
													   () -> new Platform(PLATFORM_ID, PLATFORM_NAME));
			Element element = repository.addIfAbsent(findElementById(ELEMENT_ID), 
													 () -> new Element(group,role,platform,ELEMENT_ID,ELEMENT_NAME));
			element.setElementAlias(ELEMENT_ALIAS);
			
			Element_ContainerInterface ifc = repository.addIfAbsent(findIfcByName(element, IFP_NAME),
								   									() -> new Element_ContainerInterface(element, IFP_NAME));
			
			
			Element_PhysicalInterface ifp = repository.addIfAbsent(findIfpByName(element, IFP_NAME), 
																   () -> new Element_PhysicalInterface(element, IFP_NAME, IFP_BANDWIDTH, ifc));
			ifp.setMacAddress(IFP_MAC);
			
			
			Element_ContainerInterface ifc2 = repository.addIfAbsent(findIfcByName(element, IFP2_NAME),
																	 () -> new Element_ContainerInterface(element, IFP2_NAME));


			repository.addIfAbsent(findIfpByName(element, IFP2_NAME), 
								   () -> new Element_PhysicalInterface(element, IFP2_NAME, IFP_BANDWIDTH, ifc2));
			
		});
		
		
	}
	
	@After
	public void removeLogicalInterface() {
		transaction(()->{
			service.removeLogicalInterface(ELEMENT_ID, IFL_NAME);
		});
	}
	
	
	@Test
	public void store_logical_interface_without_vlans() {
		
		ElementLogicalInterfaceSubmission ifc = newElementLogicalInterfaceSubmission()
												.withIflName(IFL_NAME)
												.withIfcName(IFP_NAME)
												.withInterfaceAlias("store logical interface test")
												.withAdministrativeState(AdministrativeState.UP)
												.withOperationalState(OperationalState.UP)
												.withRoutingInstanceName(routingInstance("default"))
												.withAddressInterfaces(newAddressInterface()
																	   .withAddress(cidr("192.168.10.1/16")))
												.build();

		ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
		
		transaction(() -> {
			boolean created = service.storeLogicalInterface(ELEMENT_ID, ifc);
			assertTrue(created);
		});
		
		transaction(() -> {
			ElementLogicalInterface ifl = service.getLogicalInterface(ELEMENT_ID, 
																	  IFL_NAME);
			assertEquals(GROUP_ID,ifl.getGroupId());
			assertEquals(GROUP_TYPE,ifl.getGroupType());
			assertEquals(GROUP_NAME,ifl.getGroupName());
			assertEquals(ELEMENT_ID,ifl.getElementId());
			assertEquals(ELEMENT_NAME,ifl.getElementName());
			assertEquals(ELEMENT_ALIAS,ifl.getElementAlias());
			assertEquals(ELEMENT_ROLE,ifl.getElementRole());
			assertEquals(IFL_NAME,ifl.getLogicalInterface().getIflName());
			assertEquals(OperationalState.UP,ifl.getLogicalInterface().getOperationalState());
			assertEquals(AdministrativeState.UP,ifl.getLogicalInterface().getAdministrativeState());
			assertEquals(routingInstance("default"),ifl.getLogicalInterface().getRoutingInstance());
			assertEquals("store logical interface test",ifl.getLogicalInterface().getIflAlias());
			assertEquals(IFP_NAME,ifl.getLogicalInterface().getPhysicalInterfaces().get(0).getIfpName());
			assertEquals(cidr("192.168.10.1/16"),ifl.getLogicalInterface().getAddresses().get(0).getAddress());
			assertEquals(IPv4,ifl.getLogicalInterface().getAddresses().get(0).getAddressType());
			
			Message message = messageCaptor.getValue();
			assertThat(message.getReason(),is(IVT0361I_ELEMENT_IFL_STORED.getReasonCode()));
			
			
		});
		
		
	}
	
	
	@Test
	public void update_logical_interface_without_vlans() {
		
		
		ElementLogicalInterfaceSubmission ifc = newElementLogicalInterfaceSubmission()
												.withIflName(IFL_NAME)
												.withIfcName(IFP_NAME)
												.withInterfaceAlias("store logical interface test")
												.withAdministrativeState(AdministrativeState.UP)
												.withOperationalState(OperationalState.UP)
												.withRoutingInstanceName(routingInstance("default"))
												.withAddressInterfaces(newAddressInterface()
																	   .withAddress(cidr("192.168.10.1/16")))
												.build();

		
		transaction(() -> {
			boolean created = service.storeLogicalInterface(ELEMENT_ID, ifc);
			assertTrue(created);
		});
		
		
		ElementLogicalInterfaceSubmission update = newElementLogicalInterfaceSubmission()
												   .withIflName(IFL_NAME)
												   .withIfcName(IFP2_NAME)
												   .withInterfaceAlias("Updated alias")
												   .withAdministrativeState(AdministrativeState.DOWN)
												   .withOperationalState(OperationalState.DOWN)
												   .withRoutingInstanceName(routingInstance("updated"))
												   .withAddressInterfaces(newAddressInterface()
																	      .withAddress(cidr("10.0.100.1/8")))
												   .build();

		ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());

		transaction(() -> {
			boolean created = service.storeLogicalInterface(ELEMENT_ID, update);
			assertFalse(created);
		});
		
		
		transaction(() -> {
			ElementLogicalInterface ifl = service.getLogicalInterface(ELEMENT_ID, 
																	  IFL_NAME);
			assertEquals(GROUP_ID,ifl.getGroupId());
			assertEquals(GROUP_TYPE,ifl.getGroupType());
			assertEquals(GROUP_NAME,ifl.getGroupName());
			assertEquals(ELEMENT_ID,ifl.getElementId());
			assertEquals(ELEMENT_NAME,ifl.getElementName());
			assertEquals(ELEMENT_ALIAS,ifl.getElementAlias());
			assertEquals(ELEMENT_ROLE,ifl.getElementRole());
			assertEquals(IFL_NAME,ifl.getLogicalInterface().getIflName());
			assertEquals(OperationalState.DOWN,ifl.getLogicalInterface().getOperationalState());
			assertEquals(AdministrativeState.DOWN,ifl.getLogicalInterface().getAdministrativeState());
			assertEquals(routingInstance("updated"),ifl.getLogicalInterface().getRoutingInstance());
			assertEquals("Updated alias",ifl.getLogicalInterface().getIflAlias());
			assertEquals(IFP2_NAME,ifl.getLogicalInterface().getPhysicalInterfaces().get(0).getIfpName());
			assertEquals(cidr("10.0.100.1/8"),ifl.getLogicalInterface().getAddresses().get(0).getAddress());
			assertEquals(IPv4,ifl.getLogicalInterface().getAddresses().get(0).getAddressType());
			
			Message message = messageCaptor.getValue();
			assertThat(message.getReason(),is(IVT0361I_ELEMENT_IFL_STORED.getReasonCode()));
			
		});
		
		
	}
	
	
	@Test
	public void find_logical_interfaces_by_vlan() {
		
	}

	@Test
	public void find_logical_interfaces_by_prefix() {
		
	}
	
	@Test
	public void find_logical_interfaces_by_instance() {
		
	}
	
	@Test
	public void find_logical_interfaces_by_name() {
		
	}


	
}
