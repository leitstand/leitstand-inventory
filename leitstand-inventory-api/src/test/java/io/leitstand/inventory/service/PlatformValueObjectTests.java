package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.PlatformPortMapping.newPlatformPortMapping;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.TreeSet;

import org.junit.Test;

public class PlatformValueObjectTests {

    private static final PlatformId PLATFORM_ID = PlatformId.randomPlatformId();
    private static final PlatformName PLATFORM_NAME = PlatformName.platformName("platform");
    private static final PlatformChipsetName PLATFORM_CHIPSET = PlatformChipsetName.platformChipsetName("chipset");
    private static final String DESCRIPTION = "description";
    private static final String MODEL = "model";
    private static final String VENDOR = "vendor";
    
    @Test
    public void create_platform_settings() {
        
        PlatformSettings platform = newPlatformSettings()
                                    .withDescription(DESCRIPTION)
                                    .withModelName(MODEL)
                                    .withVendorName(VENDOR)
                                    .withPlatformId(PLATFORM_ID)
                                    .withPlatformName(PLATFORM_NAME)
                                    .withPlatformChipset(PLATFORM_CHIPSET)
                                    .withRackUnits(2)
                                    .build();
        
        assertThat(platform.getDescription(), is(DESCRIPTION));
        assertThat(platform.getModelName(),is(MODEL));
        assertThat(platform.getPlatformChipset(),is(PLATFORM_CHIPSET));
        assertThat(platform.getPlatformId(),is(PLATFORM_ID));
        assertThat(platform.getPlatformName(),is(PLATFORM_NAME));
        assertThat(platform.getRackUnits(),is(2));
        assertThat(platform.getVendorName(),is(VENDOR));
        
        
    }
    
    
    @Test
    public void create_platform_settings_with_port_mapping() {
        
        PlatformPortMapping portA = newPlatformPortMapping()
                                    .withChassisId("chassis-1")
                                    .withFace(PlatformPortMapping.Face.FRONT)
                                    .withPanelBlockId("panel-1")
                                    .withPortId("port-1")
                                    .withPortAlias("port-alias")
                                    .withIfpName(interfaceName("ifp-0/0/0"))
                                    .withDescription("Mapped port")
                                    .build();
       
        PlatformPortMapping portB = newPlatformPortMapping()
                                    .withChassisId("chassis-1")
                                    .withFace(PlatformPortMapping.Face.FRONT)
                                    .withPanelBlockId("panel-1")
                                    .withPortId("port-2")
                                    .build();
        
        PlatformSettings platform = newPlatformSettings()
                                    .withDescription(DESCRIPTION)
                                    .withModelName(MODEL)
                                    .withVendorName(VENDOR)
                                    .withPlatformId(PLATFORM_ID)
                                    .withPlatformName(PLATFORM_NAME)
                                    .withPlatformChipset(PLATFORM_CHIPSET)
                                    .withRackUnits(2)
                                    .withPortMappings(asList(portA,portB))
                                    .build();
        
        assertThat(platform.getDescription(), is(DESCRIPTION));
        assertThat(platform.getModelName(),is(MODEL));
        assertThat(platform.getPlatformChipset(),is(PLATFORM_CHIPSET));
        assertThat(platform.getPlatformId(),is(PLATFORM_ID));
        assertThat(platform.getPlatformName(),is(PLATFORM_NAME));
        assertThat(platform.getRackUnits(),is(2));
        assertThat(platform.getVendorName(),is(VENDOR));
        assertThat(new LinkedList<>(platform.getPortMappings()),
                   is(new LinkedList<>(asList(portA,portB))));
        
    }
}
