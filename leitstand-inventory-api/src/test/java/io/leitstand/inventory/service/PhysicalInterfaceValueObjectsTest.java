package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AdministrativeState.UP;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.FacilityId.randomFacilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.PhysicalInterface.newPhysicalInterface;
import static io.leitstand.inventory.service.PhysicalInterfaceData.newPhysicalInterfaceData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

public class PhysicalInterfaceValueObjectsTest {
    
    private static final MACAddress       MAC_ADDRESS   = macAddress("00:11:22:33:44:55");
    private static final String           CATEGORY      = "category";
    private static final ElementGroupId   GROUP_ID      = randomGroupId();
    private static final ElementGroupType GROUP_TYPE    = groupType("pod");
    private static final ElementGroupName GROUP_NAME    = groupName("group");
    private static final ElementId        ELEMENT_ID    = randomElementId();
    private static final ElementName      ELEMENT_NAME  = elementName("element");
    private static final ElementAlias     ELEMENT_ALIAS = elementAlias("alias");
    private static final ElementRoleName  ELEMENT_ROLE  = elementRoleName("role");
    private static final InterfaceName    IFP_NAME      = interfaceName("ifp-0/0/1");
    private static final Date             DATE_MODIFIED = new Date();
    private static final FacilityId       FACILITY_ID   = randomFacilityId();
    private static final FacilityName     FACILITY_NAME = facilityName("facility");
    private static final String           IFP_ALIAS     = "alias";

    @Test
    public void create_physical_interface_data() {
        PhysicalInterfaceData ifp = newPhysicalInterfaceData()
                                    .withAdministrativeState(UP)
                                    .withDateModified(DATE_MODIFIED)
                                    .withElementAlias(ELEMENT_ALIAS)
                                    .withElementId(ELEMENT_ID)
                                    .withElementName(ELEMENT_NAME)
                                    .withElementRole(ELEMENT_ROLE)
                                    .withFacilityId(FACILITY_ID)
                                    .withFacilityName(FACILITY_NAME)
                                    .withGroupId(GROUP_ID)
                                    .withGroupName(GROUP_NAME)
                                    .withGroupType(GROUP_TYPE)
                                    .withIfpAdministrativeState(UP)
                                    .withIfpAlias(IFP_ALIAS)
                                    .withIfpName(IFP_NAME)
                                    .withIfpOperationalState(DOWN)
                                    .build();
        
        assertThat(ifp.getAdministrativeState(), is(UP));
        assertThat(ifp.getDateModified(),is(DATE_MODIFIED));
        assertThat(ifp.getElementAlias(),is(ELEMENT_ALIAS));
        assertThat(ifp.getElementId(),is(ELEMENT_ID));
        assertThat(ifp.getElementName(),is(ELEMENT_NAME));
        assertThat(ifp.getElementRole(),is(ELEMENT_ROLE));
        assertThat(ifp.getFacilityId(),is(FACILITY_ID));
        assertThat(ifp.getFacilityName(),is(FACILITY_NAME));
        assertThat(ifp.getGroupId(),is(GROUP_ID));
        assertThat(ifp.getGroupName(),is(GROUP_NAME));
        assertThat(ifp.getGroupType(),is(GROUP_TYPE));
        assertThat(ifp.getIfpAdministrativeState(),is(UP));
        assertThat(ifp.getIfpAlias(),is(IFP_ALIAS));
        assertThat(ifp.getIfpName(),is(IFP_NAME));
        assertThat(ifp.getIfpOperationalState(),is(DOWN));
        
    }
    
    @Test
    public void create_physical_interface() {
        PhysicalInterface ifp = newPhysicalInterface()
                                    .withAdministrativeState(UP)
                                    .withCategory(CATEGORY)
                                    .withIfpAlias(IFP_ALIAS)
                                    .withIfpName(IFP_NAME)
                                    .withMacAddress(MAC_ADDRESS)
                                    .withOperationalState(DOWN)
                                    .build();
        
        assertThat(ifp.getAdministrativeState(), is(UP));
        assertThat(ifp.getCategory(),is(CATEGORY));
        assertThat(ifp.getIfpAlias(),is(IFP_ALIAS));
        assertThat(ifp.getIfpName(),is(IFP_NAME));
        assertThat(ifp.getMacAddress(),is(MAC_ADDRESS));
        assertThat(ifp.getOperationalState(),is(DOWN));
        
    }
    
}
