package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.PlatformSettings;

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
}
