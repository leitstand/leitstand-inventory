package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceFilter.ifpFilter;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ElementPhysicalInterfaceFilterTest {

    
    @Test
    public void accept_any_administrative_state_if_empty_administrative_state_is_specified() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .administrativeState("");
        assertTrue(filter.administrativeStateMatches(ACTIVE));
        
    }
    
    @Test
    public void accept_any_administrative_state_if_specified_administrative_state_is_null() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .administrativeState((String)null);
        assertTrue(filter.administrativeStateMatches(ACTIVE));
        
    }
    
    @Test
    public void accept_any_administrative_states_if_no_administrative_state_is_specified() {
        ElementPhysicalInterfaceFilter filter = ifpFilter();
        assertTrue(filter.administrativeStateMatches(ACTIVE));
        
    }
    
    @Test
    public void reject_mismatching_administrative_state() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .administrativeState(ACTIVE);
        assertTrue(filter.administrativeStateMatches(ACTIVE));
        assertFalse(filter.administrativeStateMatches(RETIRED));
        
    }
    
    
    @Test
    public void accept_any_operational_state_if_empty_operational_state_is_specified() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .operationalState("");
        assertTrue(filter.operationalStateMatches(UP));
        
    }
    
    @Test
    public void accept_any_operational_state_if_specified_operational_state_is_null() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .operationalState((String)null);
        assertTrue(filter.operationalStateMatches(UP));
        
    }
    
    @Test
    public void accept_any_operational_states_if_no_operational_state_is_specified() {
        ElementPhysicalInterfaceFilter filter = ifpFilter();
        assertTrue(filter.operationalStateMatches(UP));
    }
    
    @Test
    public void reject_mismatching_operational_state() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .operationalState(UP);
        assertTrue(filter.operationalStateMatches(UP));
        assertFalse(filter.operationalStateMatches(DOWN));
    }
    
    @Test
    public void null_name_pattern_matches_all_interface_names() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpNamePattern(null);
        assertTrue(filter.ifpNameMatches(interfaceName("ifp-0/0/0")));
    }
    
    @Test
    public void empty_name_pattern_matches_all_interface_names() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpNamePattern("");
        assertTrue(filter.ifpNameMatches(interfaceName("ifp-0/0/0")));
    }
    
    @Test
    public void accept_matching_interface_names() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpNamePattern("ifp-0/0/\\d");
        assertTrue(filter.ifpNameMatches(interfaceName("ifp-0/0/0")));
        assertTrue(filter.ifpNameMatches(interfaceName("ifp-0/0/9")));
        assertFalse(filter.ifpNameMatches(interfaceName("ifp-0/0/10")));

    }
    
    @Test
    public void null_alias_pattern_matches_all_interface_names() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpAliasPattern(null);
        assertTrue(filter.ifpAliasMatches("Alias"));
    }
    
    @Test
    public void empty_alias_pattern_matches_all_interface_names() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpAliasPattern("");
        assertTrue(filter.ifpAliasMatches("Alias"));
    }
    
    @Test
    public void accept_matching_interface_aliases() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpAliasPattern("foo");
        assertTrue(filter.ifpAliasMatches("foo"));
        assertFalse(filter.ifpAliasMatches("bar"));

    }
    
    @Test
    public void reject_empty_alias_if_alias_pattern_is_specified() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpAliasPattern("foo");
        assertFalse(filter.ifpAliasMatches(""));

    }
    
    @Test
    public void reject_null_alias_if_alias_pattern_is_specified() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpAliasPattern("foo");
        assertFalse(filter.ifpAliasMatches(null));

    }
}
