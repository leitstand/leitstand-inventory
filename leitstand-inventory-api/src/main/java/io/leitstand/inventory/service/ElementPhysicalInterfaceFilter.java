package io.leitstand.inventory.service;

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public class ElementPhysicalInterfaceFilter {


    public static ElementPhysicalInterfaceFilter ifpFilter() {
        return new ElementPhysicalInterfaceFilter();
    }

    private Pattern ifpNamePattern;
    private Pattern ifpAliasPattern;
    private AdministrativeState administrativeState;
    private OperationalState operationalState;
    
    public ElementPhysicalInterfaceFilter ifpNamePattern(String pattern) {
        if(isNonEmptyString(pattern)) {
            this.ifpNamePattern = compile(pattern);
        }
        return this;
    }
    
    public ElementPhysicalInterfaceFilter ifpAliasPattern(String pattern) {
        if(isNonEmptyString(pattern)) {
            this.ifpAliasPattern = compile(pattern);
        }
        return this;
    }
    
    public ElementPhysicalInterfaceFilter administrativeState(String state) {
        return administrativeState(AdministrativeState.valueOf(state));
    }
    
    public ElementPhysicalInterfaceFilter administrativeState(AdministrativeState state) {
        this.administrativeState = state;
        return this;
    }
    
    public ElementPhysicalInterfaceFilter operationalState(String state) {
        return operationalState(OperationalState.valueOf(state));
    }
    
    public ElementPhysicalInterfaceFilter operationalState(OperationalState state) {
        this.operationalState = state;
        return this;
    }
    
    
    public boolean ifpNameMatches(InterfaceName ifpName) {
        if(ifpNamePattern == null) {
            return ifpAliasPattern == null;
        }
        if(ifpName == null) {
            return false;
        }
        return ifpNamePattern.matcher(ifpName.toString()).matches();
        
    }
    
    public boolean administrativeStateMatches(AdministrativeState state) {
        if(administrativeState == null) {
            return true;
        }
        return administrativeState.equals(state);
    }
    
    public boolean operationalStateMatches(OperationalState state) {
        if(operationalState == null) {
            return true;
        }
        return operationalState.equals(state);
    }

    public boolean ifpAliasMatches(String ifpAlias) {
        if(ifpAliasPattern == null) {
            return ifpNamePattern == null;
        }
        if(isEmptyString(ifpAlias)) {
            return false;
        }
        return ifpAliasPattern.matcher(ifpAlias).matches();
    }
    
}
