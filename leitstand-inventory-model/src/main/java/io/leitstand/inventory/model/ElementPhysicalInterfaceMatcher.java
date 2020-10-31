package io.leitstand.inventory.model;

import io.leitstand.inventory.service.ElementPhysicalInterfaceFilter;

class ElementPhysicalInterfaceMatcher {

    private ElementPhysicalInterfaceFilter filter;
    
    public ElementPhysicalInterfaceMatcher(ElementPhysicalInterfaceFilter filter) {
        this.filter = filter;
    }
    
    boolean accept(Element_PhysicalInterface ifp) {
        return filter.administrativeStateMatches(ifp.getAdministrativeState())
               && filter.operationalStateMatches(ifp.getOperationalState())
               && (filter.ifpNameMatches(ifp.getIfpName())
                   || filter.ifpAliasMatches(ifp.getIfpAlias()));
    }
    
}
