package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ElementGroupElements.newElementGroupElements;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupStatistics.newElementGroupStatistics;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.FacilityType.facilityType;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ElementGroupValueObjectTest {
    
    private static final ElementGroupId     GROUP_ID         = randomGroupId();
    private static final ElementGroupName   GROUP_NAME       = groupName("group");
    private static final ElementGroupType   GROUP_TYPE       = groupType("pod");
    private static final ElementId          ELEMENT_ID       = randomElementId();
    private static final ElementName        ELEMENT_NAME     = elementName("element");
    private static final FacilityId         FACILITY_ID      = randomFacilityId();  
    private static final FacilityType       FACILITY_TYPE    = facilityType("facility_type");
    private static final FacilityName       FACILITY_NAME    = facilityName("facility");
    private static final String             DESCRIPTION 	 = "description";
    
    
    @Test
    public void element_group_settings() {
        ElementGroupSettings group = ElementGroupSettings.newElementGroupSettings()
                                     .withGroupId(GROUP_ID)
                                     .withGroupType(GROUP_TYPE)
                                     .withGroupName(GROUP_NAME)
                                     .withFacilityId(FACILITY_ID)
                                     .withFacilityType(FACILITY_TYPE)
                                     .withFacilityName(FACILITY_NAME)
                                     .withDescription(DESCRIPTION)
                                     .withTags("tag1","tag2")
                                     .build();
        assertThat(group.getGroupId(),is(GROUP_ID));
        assertThat(group.getGroupType(),is(GROUP_TYPE));
        assertThat(group.getGroupName(),is(GROUP_NAME));
        assertThat(group.getFacilityId(),is(FACILITY_ID));
        assertThat(group.getFacilityType(),is(FACILITY_TYPE));
        assertThat(group.getFacilityName(),is(FACILITY_NAME));
        assertThat(group.getDescription(),is(DESCRIPTION));
        assertThat(group.getTags(),containsExactly("tag1","tag2"));
    }
    
    @Test
    public void element_group_elements() {
        ElementSettings element = newElementSettings()
                                  .withElementId(ELEMENT_ID)
                                  .withElementName(ELEMENT_NAME)
                                  .build();
        
        ElementGroupElements group = newElementGroupElements()
                                     .withElements(asList(element))
                                     .withDescription(DESCRIPTION)
                                     .build();
        assertThat(group.getElements(),containsExactly(element));
        assertThat(group.getDescription(),is(DESCRIPTION));
    }
    
    @Test
    public void element_group_statistics() {
        ElementGroupStatistics stats = newElementGroupStatistics()
                                       .withNewCount(3)
                                       .withCount(UP,2)
                                       .withCount(DOWN, 1)
                                       .withRetiredCount(4)
                                       .build();
        
        assertThat(stats.getNewElements(),is(3));
        assertThat(stats.getRetiredElements(),is(4));
        assertThat(stats.getActiveElements().get(UP.name()),is(2));
        assertThat(stats.getActiveElements().get(DOWN.name()),is(1));
        
    }
}
