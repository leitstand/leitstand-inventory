package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.FacilityId.facilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.RackId.randomRackId;
import static io.leitstand.inventory.service.RackName.rackName;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0809E_RACK_NAME_ALREADY_IN_USE;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertSame;
import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackItemData;
import io.leitstand.inventory.service.RackItems;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackService;
import io.leitstand.inventory.service.RackSettings;

@RunWith(MockitoJUnitRunner.class)
public class RackResourceTest {
	
	private static final RackId    RACK_ID     = randomRackId();
	private static final RackName  RACK_NAME   = rackName("rack");

	@Rule
	public ExpectedException exception = none();
	
	@Mock
	private RackService service;
	
	@InjectMocks
	private RackResource resource = new RackResource();
	
	@Test
	public void filter_racks_by_name() {
		resource.findRacks(null,".*");
		verify(service).findRacks(".*");
	}
	
	@Test
	public void filter_racks_by_facility_id() {
		String uuid = UUID.randomUUID().toString();
		
		resource.findRacks(uuid,".*");
		verify(service).findRacks(facilityId(uuid), ".*");
	}
	
	@Test
	public void filter_racks_by_facility_name() {
		resource.findRacks("foo",".*");
		verify(service).findRacks(facilityName("foo"), ".*");
	}
	
	@Test
	public void get_rack_settings_by_id() {
		resource.getRackSettings(RACK_ID);
		verify(service).getRackSettings(RACK_ID);
	}
	
	@Test
	public void get_rack_settings_by_name() {
		resource.getRackSettings(RACK_NAME);
		verify(service).getRackSettings(RACK_NAME);
	}
	
	@Test
	public void remove_rack_by_id() {
		resource.removeRack(RACK_ID, false);
		verify(service).removeRack(RACK_ID);
	}
	
	@Test
	public void remove_rack_by_name() {
		resource.removeRack(RACK_NAME, false);
		verify(service).removeRack(RACK_NAME);
	}
	
	@Test
	public void force_remove_rack_by_id() {
		resource.removeRack(RACK_ID, true);
		verify(service).forceRemoveRack(RACK_ID);
	}
	
	@Test
	public void force_remove_rack_by_name() {
		resource.removeRack(RACK_NAME, true);
		verify(service).forceRemoveRack(RACK_NAME);
	}
	
	@Test
	public void cannot_change_rack_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		RackSettings settings = newRackSettings().build();
		resource.storeRack(RACK_ID,settings);
		verify(service,never()).storeRack(settings);
	}
	
	
	@Test
	public void store_rack_by_id() {
		RackSettings settings = newRackSettings()
								.withRackId(RACK_ID)
								.withRackName(RACK_NAME)
							    .build();
		resource.storeRack(RACK_ID,settings);
		verify(service).storeRack(settings);
	}
	
	
	@Test
	public void store_rack_by_name() {
		RackSettings settings = newRackSettings()
								.withRackId(RACK_ID)
								.withRackName(RACK_NAME)
							    .build();
		resource.storeRack(RACK_NAME,settings);
		verify(service).storeRack(settings);
	}
	
	
	@Test
	public void store_rack_item_by_id() {
		RackItemData item = mock(RackItemData.class);
		when(item.getPosition()).thenReturn(10);
		resource.storeRackItem(RACK_ID,10,item);
		verify(service).storeRackItem(RACK_ID,item);
	}
	
	
	@Test
	public void store_rack_item_by_name() {
		RackItemData item = mock(RackItemData.class);
		when(item.getPosition()).thenReturn(10);
		resource.storeRackItem(RACK_NAME,10,item);
		verify(service).storeRackItem(RACK_NAME,item);
	}
	
	@Test
	public void get_rack_item_by_id() {
		resource.getRackItem(RACK_ID,1);
		verify(service).getRackItem(RACK_ID,1);
	}
	
	
	@Test
	public void get_rack_item_by_name() {
		resource.getRackItem(RACK_NAME,1);
		verify(service).getRackItem(RACK_NAME,1);
	}
	
	@Test
	public void remove_rack_item_by_id() {
		resource.removeRackItem(RACK_ID,1);
		verify(service).removeRackItem(RACK_ID,1);
	}
	
	
	@Test
	public void remove_rack_item_by_name() {
		resource.removeRackItem(RACK_NAME,1);
		verify(service).removeRackItem(RACK_NAME,1);
	}
	
	@Test
	public void report_unique_key_constraint_violation() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0809E_RACK_NAME_ALREADY_IN_USE));
	    
	    RackSettings rack = mock(RackSettings.class);
	    when(rack.getRackName()).thenReturn(RACK_NAME);
	    when(service.storeRack(rack)).then(ROLLBACK);
	    when(service.getRackSettings(RACK_NAME)).thenReturn(rack);
	    
	    resource.storeRack(rack);
	}
	
	@Test
	public void get_rack_items_by_rack_id() {
	    RackItems items = mock(RackItems.class);
	    
	    when(service.getRackItems(RACK_ID)).thenReturn(items);
	    
	    assertSame(items, resource.getRackItems(RACK_ID));
	}
	    
    @Test
    public void get_rack_items_by_rack_name() {
        RackItems items = mock(RackItems.class);
        
        when(service.getRackItems(RACK_NAME)).thenReturn(items);
        
        assertSame(items, resource.getRackItems(RACK_NAME));
    }
    
    @Test
    public void find_rack_item_by_element_id() {
        ElementId element = randomElementId();
        
        resource.findElementRackItem(element.toString());
        
        verify(service).findElementRackItem(element);
    }
    
    @Test
    public void find_rack_item_by_element_name() {
        ElementName element = ElementName.elementName("element");
        
        resource.findElementRackItem(element.toString());
        
        verify(service).findElementRackItem(element);
    }
	
	
}
