package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.RETIRED;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceFilter.ifpFilter;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.OperationalState.UP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import io.leitstand.inventory.service.ElementPhysicalInterfaceFilter;

public class ElementPhysicalInterfaceMatcherTest {

    
    @Test
    public void reject_ifp_when_administrative_state_does_not_match() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .administrativeState(ACTIVE);
        ElementPhysicalInterfaceMatcher matcher = new ElementPhysicalInterfaceMatcher(filter);
        
        Element_PhysicalInterface ifp = mock(Element_PhysicalInterface.class);
        when(ifp.getAdministrativeState()).thenReturn(RETIRED);
        
        assertFalse(matcher.accept(ifp));
    }
    
    @Test
    public void reject_ifp_when_operational_state_does_not_match() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .operationalState(UP);
        
        ElementPhysicalInterfaceMatcher matcher = new ElementPhysicalInterfaceMatcher(filter);

        Element_PhysicalInterface ifp = mock(Element_PhysicalInterface.class);
        when(ifp.getOperationalState()).thenReturn(DOWN);

        assertFalse(matcher.accept(ifp));        
    }
    
    @Test
    public void reject_ifp_when_name_does_not_match() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpNamePattern("foo");

        ElementPhysicalInterfaceMatcher matcher = new ElementPhysicalInterfaceMatcher(filter);

        Element_PhysicalInterface ifp = mock(Element_PhysicalInterface.class);
        when(ifp.getIfpName()).thenReturn(interfaceName("bar"));
        
        assertFalse(matcher.accept(ifp));        

    }

    @Test
    public void reject_ifp_when_alias_does_not_match() {
        ElementPhysicalInterfaceFilter filter = ifpFilter()
                                                .ifpAliasPattern("foo");

        ElementPhysicalInterfaceMatcher matcher = new ElementPhysicalInterfaceMatcher(filter);

        Element_PhysicalInterface ifp = mock(Element_PhysicalInterface.class);
        when(ifp.getIfpAlias()).thenReturn("bar");

        assertFalse(matcher.accept(ifp));  
    }

    @Test
    public void accept_ifp_when_all_conditions_are_satisfied() {

        ElementPhysicalInterfaceFilter filter = ifpFilter();

        ElementPhysicalInterfaceMatcher matcher = new ElementPhysicalInterfaceMatcher(filter);

        Element_PhysicalInterface ifp = mock(Element_PhysicalInterface.class);
        
        assertTrue(matcher.accept(ifp));
        
        verify(ifp).getAdministrativeState();
        verify(ifp).getOperationalState();
        verify(ifp).getIfpName();
        verify(ifp,never()).getIfpAlias();
        
    }

    
}
