package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilitySettings.newFacilitySettings;
import static io.leitstand.inventory.service.FacilityType.facilityType;
import static io.leitstand.inventory.service.Geolocation.newGeolocation;
import static io.leitstand.inventory.service.RackId.randomRackId;
import static io.leitstand.inventory.service.RackName.rackName;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_FACILITY_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0602I_FACILITY_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0605E_FACILITY_NOT_REMOVABLE;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityService;
import io.leitstand.inventory.service.FacilitySettings;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackName;

public class FacilityServiceIT extends InventoryIT{
	
	private static final FacilityId   	  FACILITY_ID   = randomFacilityId();
	private static final FacilityName 	  FACILITY_NAME = facilityName("facility");
	private static final RackId		  	  RACK_ID		= randomRackId();
	private static final RackName	 	  RACK_NAME		= rackName("rack");
	private static final ElementGroupId	  GROUP_ID		= randomGroupId();
	private static final ElementGroupType GROUP_TYPE	= groupType("pod");
	private static final ElementGroupName GROUP_NAME	= groupName("FacilityServiceIT");
	
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private FacilityService service;
	private Messages messages;
	private Repository repository;
	
	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		this.messages = mock(Messages.class);
		this.service = new DefaultFacilityService(new FacilityProvider(repository),
												  repository,
												  messages);
	}
	
	@After
	public void cleanupDatabase() {
		transaction(()->{
			getDatabase().executeUpdate(prepare("DELETE FROM inventory.rack WHERE uuid=?",RACK_ID));
			getDatabase().executeUpdate(prepare("DELETE FROM inventory.elementgroup WHERE uuid=?",GROUP_ID));
			getDatabase().executeUpdate(prepare("DELETE FROM inventory.facility"));
		});
	}
	
	@Test
	public void create_facility() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityType(facilityType("Data Center"))
									.withFacilityName(FACILITY_NAME)
									.withCategory("unittest")
									.withDescription("A description")
									.withLocation("123 Fake Street Springfield")
									.withGeolocation(newGeolocation()
													 .withLongitude(43.28299)
													 .withLatitude(-72.483813))
									.build();
		
		transaction(()->{
			boolean created = service.storeFacility(facility);
			assertTrue(created);
		});
		
		transaction(()->{
			FacilitySettings reloaded = service.getFacility(FACILITY_ID);
			assertNotSame(facility,reloaded);
			assertEquals(facility,reloaded);
		});
		
	}
	
	
	@Test
	public void update_facility() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();

		transaction(()->{
			boolean created = service.storeFacility(facility);
			assertTrue(created);
		});
		
		FacilitySettings updated = newFacilitySettings()
								   .withFacilityId(FACILITY_ID)
								   .withFacilityType(facilityType("Data Center"))
								   .withFacilityName(facilityName("UPDATED NAME"))
								   .withCategory("unittest")
								   .withDescription("A description")
								   .withLocation("123 Fake Street Springfield")
								   .withGeolocation(newGeolocation()
										   			.withLongitude(43.28299)
										   			.withLatitude(-72.483813))
								   .build();
		
		transaction(()->{
			boolean created = service.storeFacility(updated);
			assertFalse(created);
		});
		
		
		transaction(()->{
			FacilitySettings reloaded = service.getFacility(FACILITY_ID);
			assertNotSame(updated,reloaded);
			assertEquals(updated,reloaded);
		});
	}
	
	@Test
	public void remove_facility_by_id() {
		FacilitySettings facility = newFacilitySettings()
									.withFacilityId(FACILITY_ID)
									.withFacilityName(FACILITY_NAME)
									.build();

		transaction(()->{
			boolean created = service.storeFacility(facility);
			assertTrue(created);
		});
		
		transaction(()->{
			ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(messageCaptor.capture());
			service.removeFacility(FACILITY_ID);
			Message message = messageCaptor.getValue();
			assertEquals(message.getReason(),IVT0602I_FACILITY_REMOVED.getReasonCode());
		});
		
	}
	
	@Test
	public void remove_facility_by_name() {
		FacilitySettings facility = newFacilitySettings()
								    .withFacilityId(FACILITY_ID)
								    .withFacilityName(FACILITY_NAME)
								    .build();
		
		transaction(()->{
			boolean created = service.storeFacility(facility);
			assertTrue(created);
		});
		
		transaction(()->{
			ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
			doNothing().when(messages).add(messageCaptor.capture());
			service.removeFacility(FACILITY_NAME);
			Message message = messageCaptor.getValue();
			assertEquals(message.getReason(),IVT0602I_FACILITY_REMOVED.getReasonCode());
		});
	}
	
	@Test
	public void cannot_remove_facility_with_assigned_racks() {
		
		transaction(()->{
			Facility facility = new Facility(FACILITY_ID, 
											 facilityType("Dummy"), 
											 FACILITY_NAME);
			repository.add(facility);
			Rack rack = new Rack(RACK_ID,RACK_NAME);
			rack.setFacility(facility);
			repository.add(rack);
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0605E_FACILITY_NOT_REMOVABLE));
		
		transaction(()->{
			service.removeFacility(FACILITY_ID);
		});
		
	}
	
	@Test
	public void cannot_remove_facility_with_assigned_groups() {
		transaction(()->{
			Facility facility = new Facility(FACILITY_ID, 
											 facilityType("Dummy"), 
											 FACILITY_NAME);
			repository.add(facility);
			
			
			ElementGroup group = new ElementGroup(GROUP_ID, 
												  GROUP_TYPE,
												  GROUP_NAME);
			group.setFacility(facility);
			repository.add(group);
		});
		
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0605E_FACILITY_NOT_REMOVABLE));
		
		transaction(()->{
			service.removeFacility(FACILITY_ID);
		});
	}
	
	@Test
	public void throw_entity_not_found_for_unknown_facility_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
		
		transaction(()->{
			service.getFacility(randomFacilityId());
		});
	}
	
	@Test
	public void throw_entity_not_found_for_unknown_facility_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_FACILITY_NOT_FOUND));
		
		transaction(()->{
			service.getFacility(facilityName("foo"));
		});
	}
	
	
}
