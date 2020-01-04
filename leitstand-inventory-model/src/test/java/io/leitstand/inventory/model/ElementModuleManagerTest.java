/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.ModuleDataMother.testModule;
import static io.leitstand.inventory.service.ReasonCode.IVT0312I_ELEMENT_MODULE_REMOVED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ModuleData;
import io.leitstand.inventory.service.ModuleName;

public class ElementModuleManagerTest {

	private Repository repository;
	private Messages messages;
	private ArgumentCaptor<Message> messageCaptor;
	private ElementModuleManager manager;
	
	@Before
	public void initTestEnvironment() {
		repository = mock(Repository.class);
		messages   = mock(Messages.class);
		manager = new ElementModuleManager(repository, messages);
		messageCaptor = forClass(Message.class);
		doNothing().when(messages).add(messageCaptor.capture());
	}
	
	@Test
	public void attempt_to_remove_a_non_existen_module_raises_no_error() {
		manager.removeElementModule(mock(Element.class),ModuleName.valueOf("dummy"));
		verifyZeroInteractions(messages);
	}
	
	@Test
	public void remove_module_and_create_message() {
		Element_Module module = mock(Element_Module.class);
		when(repository.execute(any(Query.class))).thenReturn(module);
		manager.removeElementModule(mock(Element.class),ModuleName.valueOf("dummy"));
		verify(repository).remove(module);
		assertEquals(IVT0312I_ELEMENT_MODULE_REMOVED.getReasonCode(),messageCaptor.getValue().getReason());
	}
	
	@Test
	public void create_module_if_not_exists() {
		ModuleData moduleData = testModule("unittest");
		ArgumentCaptor<Element_Module> moduleCaptor = forClass(Element_Module.class);
		doNothing().when(repository).add(moduleCaptor.capture());
		boolean created = manager.storeElementModule(mock(Element.class), moduleData.getModuleName(), moduleData );
		assertTrue(created);
		Element_Module module = moduleCaptor.getValue();
		assertEquals(moduleData.getModuleName(),module.getModuleName());
		assertEquals(moduleData.getModuleClass(),module.getModuleClass());
		assertEquals(moduleData.getAdministrativeState(),module.getAdministrativeState());
		assertEquals(moduleData.getAssetId(),module.getAssetId());
		assertEquals(moduleData.getDateManufactured(),module.getDateManufactured());
		assertEquals(moduleData.getDescription(),module.getDescription());
		assertEquals(moduleData.getFirmwareRevision(),module.getFirmwareRevision());
		assertEquals(moduleData.getHardwareRevision(),module.getHardwareRevision());
		assertEquals(moduleData.getManufacturerName(),module.getManufacturerName());
		assertEquals(moduleData.getModelName(),module.getModelName());
		assertEquals(moduleData.getParentModule(),module.getParentModule());
		assertEquals(moduleData.getLocation(),module.getLocation());
		assertEquals(moduleData.getSerialNumber(),module.getSerialNumber());
		assertEquals(moduleData.getSoftwareRevision(),module.getSoftwareRevision());
		assertEquals(moduleData.getVendorType(),module.getVendorType());
		assertEquals(moduleData.isFieldReplaceableUnit(),module.isFieldReplaceableUnit());
	}
	
	@Test
	public void update_existing_module() {
		ModuleData moduleData = testModule("unittest");
		Element_Module module = mock(Element_Module.class);
		when(repository.execute(any(Query.class))).thenReturn(module);
		boolean created = manager.storeElementModule(mock(Element.class), moduleData.getModuleName(), moduleData );
		assertFalse(created);
		verify(module).setAdministrativeState(moduleData.getAdministrativeState());
		verify(module).setAssetId(moduleData.getAssetId());
		verify(module).setDateManufactured(moduleData.getDateManufactured());
		verify(module).setDescription(moduleData.getDescription());
		verify(module).setFirmwareRevision(moduleData.getFirmwareRevision());
		verify(module).setHardwareRevision(moduleData.getHardwareRevision());
		verify(module).setManufacturerName(moduleData.getManufacturerName());
		verify(module).setModelName(moduleData.getModelName());
		verify(module).setModuleClass(moduleData.getModuleClass());
		verify(module).setParentModule(null); // This is important to nullify parents in case of an update!
		verify(module).setLocation(moduleData.getLocation());
		verify(module).setSerialNumber(moduleData.getSerialNumber());
		verify(module).setSoftwareRevision(moduleData.getSoftwareRevision());
		verify(module).setVendorType(moduleData.getVendorType());
		verify(module).setFieldReplaceableUnit(moduleData.isFieldReplaceableUnit());
	}
	
	
	@Test
	public void create_stub_parent_module_when_parent_and_child_modules_do_not_exist() {
		ModuleData parentData = testModule("parent");
		ModuleData moduleData = testModule(parentData,"unittest");
		ArgumentCaptor<Element_Module> moduleCaptor = forClass(Element_Module.class);
		doNothing().when(repository).add(moduleCaptor.capture());
		boolean created = manager.storeElementModule(mock(Element.class), moduleData.getModuleName(), moduleData );
		assertTrue(created);
		List<Element_Module> modules = moduleCaptor.getAllValues();
		Element_Module module = modules.get(0);
		Element_Module parent = modules.get(1);
		verify(repository).add(module);
		verify(repository).add(parent);
		assertEquals(module.getParentModule(),parent);
	}
	
	
	@Test
	public void create_stub_parent_module_when_parent_module_does_not_exist() {
		ModuleData parent = testModule("parent");
		ModuleData moduleData = testModule(parent,"unittest");
		Element_Module module = mock(Element_Module.class);
		ArgumentCaptor<Element_Module> parentCaptor = forClass(Element_Module.class);
		when(repository.execute(any(Query.class))).thenReturn(module).thenReturn(null);
		doNothing().when(module).setParentModule(parentCaptor.capture());
		boolean created = manager.storeElementModule(mock(Element.class), moduleData.getModuleName(), moduleData );
		assertFalse(created);
		verify(module).setAdministrativeState(moduleData.getAdministrativeState());
		verify(module).setAssetId(moduleData.getAssetId());
		verify(module).setDateManufactured(moduleData.getDateManufactured());
		verify(module).setDescription(moduleData.getDescription());
		verify(module).setFirmwareRevision(moduleData.getFirmwareRevision());
		verify(module).setHardwareRevision(moduleData.getHardwareRevision());
		verify(module).setManufacturerName(moduleData.getManufacturerName());
		verify(module).setModuleClass(moduleData.getModuleClass());
		verify(module).setModelName(moduleData.getModelName());
		verify(module).setLocation(moduleData.getLocation());
		verify(module).setSerialNumber(moduleData.getSerialNumber());
		verify(module).setSoftwareRevision(moduleData.getSoftwareRevision());
		verify(module).setVendorType(moduleData.getVendorType());
		verify(module).setFieldReplaceableUnit(moduleData.isFieldReplaceableUnit());
		assertEquals(parentCaptor.getValue().getModuleName(),parent.getModuleName());
	}
}
