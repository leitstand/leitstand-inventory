package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilityType.facilityType;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.RackId.randomRackId;
import static io.leitstand.inventory.service.RackItem.newRackItem;
import static io.leitstand.inventory.service.RackItemData.newRackItemData;
import static io.leitstand.inventory.service.RackItemData.Face.FRONT;
import static io.leitstand.inventory.service.RackItems.newRackItems;
import static io.leitstand.inventory.service.RackName.rackName;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class RackValueObjectTests {

    private static final ElementId        ELEMENT_ID    = randomElementId();
    private static final ElementName      ELEMENT_NAME  = elementName("element");
    private static final ElementAlias     ELEMENT_ALIAS = elementAlias("alias");
    private static final ElementRoleName  ELEMENT_ROLE  = elementRoleName("role");
    private static final ElementGroupId   GROUP_ID      = randomGroupId();
    private static final ElementGroupName GROUP_NAME    = groupName("group");
    private static final ElementGroupType GROUP_TYPE    = groupType("type");
    private static final PlatformId       PLATFORM_ID   = randomPlatformId();
    private static final PlatformName     PLATFORM_NAME = platformName("platform");
    private static final RackId           RACK_ID       = randomRackId();
    private static final RackName         RACK_NAME     = rackName("rack");
    private static final String           RACK_TYPE     = "rack-type";
    private static final String           DESCRIPTION   = "description";
    private static final String           ASSET_ID      = "asset";
    private static final String           SERIAL_NUMBER = "serial";
    private static final String           LOCATION      = "location";
    private static final FacilityId       FACILITY_ID   = randomFacilityId();
    private static final FacilityName     FACILITY_NAME = facilityName("facility");
    private static final FacilityType     FACILITY_TYPE = facilityType("type");
    
    @Test
    public void create_rack_item() {
        RackItemData itemData = mock(RackItemData.class);
        RackItem item = newRackItem()
                        .withRackItem(itemData)
                        .build();
        assertThat(item.getItem(),is(itemData));
    }
    
    @Test
    public void create_rack_items() {
        RackItemData itemData = mock(RackItemData.class);
        RackItems item = newRackItems()
                         .withRackItems(asList(itemData))
                         .build();
        assertThat(item.getItems(),containsExactly(itemData));
    }
    
    @Test
    public void create_rack_item_data() {
        RackItemData item = newRackItemData()
                            .withAdministrativeState(ACTIVE)
                            .withElementAlias(ELEMENT_ALIAS)
                            .withElementId(ELEMENT_ID)
                            .withElementName(ELEMENT_NAME)
                            .withElementRole(ELEMENT_ROLE)
                            .withFace(FRONT)
                            .withGroupId(GROUP_ID)
                            .withGroupName(GROUP_NAME)
                            .withGroupType(GROUP_TYPE)
                            .withHeight(1)
                            .withPlatformId(PLATFORM_ID)
                            .withPlatformName(PLATFORM_NAME)
                            .withPosition(10)
                            .withRackItemName("rack-item-name")
                            .build();
        
        assertThat(item.getAdministrativeState(),is(ACTIVE));
        assertThat(item.getElementAlias(),is(ELEMENT_ALIAS));
        assertThat(item.getElementId(),is(ELEMENT_ID));
        assertThat(item.getElementName(),is(ELEMENT_NAME));
        assertThat(item.getElementRole(),is(ELEMENT_ROLE));
        assertThat(item.getFace(),is(FRONT));
        assertThat(item.getGroupId(),is(GROUP_ID));
        assertThat(item.getGroupName(),is(GROUP_NAME));
        assertThat(item.getGroupType(),is(GROUP_TYPE));
        assertThat(item.getHeight(),is(1));
        assertThat(item.getPlatformId(),is(PLATFORM_ID));
        assertThat(item.getPlatformName(),is(PLATFORM_NAME));
        assertThat(item.getPosition(),is(10));
        assertThat(item.getRackItemName(),is("rack-item-name"));
    }
    
    @Test
    public void create_rack_settings() {
        RackSettings settings = newRackSettings()
                                .withAdministrativeState(ACTIVE)
                                .withAscending(true)
                                .withAssetId(ASSET_ID)
                                .withDescription(DESCRIPTION)
                                .withFacilityId(FACILITY_ID)
                                .withFacilityName(FACILITY_NAME)
                                .withFacilityType(FACILITY_TYPE)
                                .withLocation(LOCATION)
                                .withRackId(RACK_ID)
                                .withRackName(RACK_NAME)
                                .withRackType(RACK_TYPE)
                                .withSerialNumber(SERIAL_NUMBER)
                                .build();
        
        assertThat(settings.getAdministrativeState(), is(ACTIVE));
        assertThat(settings.isAscending(),is(true));
        assertThat(settings.getAssetId(),is(ASSET_ID));
        assertThat(settings.getDescription(),is(DESCRIPTION));
        assertThat(settings.getFacilityId(),is(FACILITY_ID));
        assertThat(settings.getFacilityName(),is(FACILITY_NAME));
        assertThat(settings.getFacilityType(),is(FACILITY_TYPE));
        assertThat(settings.getLocation(),is(LOCATION));
        assertThat(settings.getRackId(),is(RACK_ID));
        assertThat(settings.getRackName(),is(RACK_NAME));
        assertThat(settings.getRackType(),is(RACK_TYPE));
        assertThat(settings.getSerialNumber(),is(SERIAL_NUMBER));
        
    }
    
}
