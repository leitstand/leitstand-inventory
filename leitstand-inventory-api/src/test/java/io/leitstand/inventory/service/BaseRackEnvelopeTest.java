package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AdministrativeState.NEW;
import static io.leitstand.inventory.service.BaseRackEnvelopeTest.RackMessage.newRackMessage;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BaseRackEnvelopeTest {

    private static final RackId RACK_ID = RackId.randomRackId();
    private static final RackName RACK_NAME = RackName.rackName("rack-name");
    private static final String RACK_TYPE = "rack-type";
    private static final FacilityId FACILITY_ID = FacilityId.randomFacilityId();
    private static final FacilityName FACILITY_NAME = FacilityName.facilityName("facility-name");
    private static final FacilityType FACILITY_TYPE = FacilityType.facilityType("facility-type");
   
    
    
    static final class RackMessage extends BaseRackEnvelope {
        
        static Builder newRackMessage() {
            return new Builder();
        }
        
        private static final class Builder extends BaseRackEnvelopeBuilder<RackMessage, Builder>{
            
            protected Builder() {
                super(new RackMessage());
            }
            
        }
        
    }

    @Test
    public void create_base_element_group_envelope() {
        
        RackMessage message = newRackMessage()
                              .withAdministrativeState(NEW)
                              .withAscending(true)
                              .withDescription("description")
                              .withFacilityId(FACILITY_ID)
                              .withFacilityName(FACILITY_NAME)
                              .withFacilityType(FACILITY_TYPE)
                              .withRackId(RACK_ID)
                              .withRackName(RACK_NAME)
                              .withRackType(RACK_TYPE)
                              .withUnits(42)
                              .build();
                              
        assertThat(message.getAdministrativeState(),is(NEW));
        assertTrue(message.isAscending());
        assertThat(message.getDescription(),is("description"));
        assertThat(message.getFacilityId(),is(FACILITY_ID));
        assertThat(message.getFacilityName(),is(FACILITY_NAME));
        assertThat(message.getFacilityType(),is(FACILITY_TYPE));
        assertThat(message.getRackId(),is(RACK_ID));
        assertThat(message.getRackName(),is(RACK_NAME));
        assertThat(message.getRackType(),is(RACK_TYPE));
    }
    
    @Test
    public void copy_base_element_group_envelope() {
        RackMessage message = newRackMessage()
                              .withAdministrativeState(NEW)
                              .withAscending(true)
                              .withDescription("description")
                              .withFacilityId(FACILITY_ID)
                              .withFacilityName(FACILITY_NAME)
                              .withFacilityType(FACILITY_TYPE)
                              .withRackId(RACK_ID)
                              .withRackName(RACK_NAME)
                              .withRackType(RACK_TYPE)
                              .withUnits(42)
                              .build();
        
        RackMessage clone = newRackMessage().withRack(message).build();
        
        assertEquals(message,clone);
    }


}
