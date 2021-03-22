package io.leitstand.inventory.service;

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

/**
 * A filter for physical interfaces of an element.
 */
public class ElementPhysicalInterfaceFilter {


    /**
     * Creates a new physical interface filter.
     * @return a new physical interface filter.
     */
    public static ElementPhysicalInterfaceFilter ifpFilter() {
        return new ElementPhysicalInterfaceFilter();
    }

    private Pattern ifpNamePattern;
    private Pattern ifpAliasPattern;
    private AdministrativeState administrativeState;
    private OperationalState operationalState;
    
    /**
     * Sets the IFP name pattern.
     * @param pattern a regular expression the IFP name must match.
     * @return a reference to this filter to continue filter composition.
     */
    public ElementPhysicalInterfaceFilter ifpNamePattern(String pattern) {
        if(isNonEmptyString(pattern)) {
            this.ifpNamePattern = compile(pattern);
        }
        return this;
    }
    
    /**
     * Sets the IFP alias pattern.
     * @param pattern a regular expression the IFP alias must match.
     * @return a reference to this filter to continue filter composition.
     */    
    public ElementPhysicalInterfaceFilter ifpAliasPattern(String pattern) {
        if(isNonEmptyString(pattern)) {
            this.ifpAliasPattern = compile(pattern);
        }
        return this;
    }
    
    /**
     * Sets the administrative the IFP must have.
     * @param state the administrative state.
     * @return a reference to this filter to continue filter composition.
     */
    public ElementPhysicalInterfaceFilter administrativeState(String state) {
        return administrativeState(AdministrativeState.valueOf(state));
    }
    
    /**
     * Sets the administrative the IFP must have.
     * @param state the administrative state.
     * @return a reference to this filter to continue filter composition.
     */
    public ElementPhysicalInterfaceFilter administrativeState(AdministrativeState state) {
        this.administrativeState = state;
        return this;
    }
    
    /**
     * Sets the operational state the IFP must have.
     * @param state the operational state.
     * @return a reference to this filter to continue filter composition.
     */
    public ElementPhysicalInterfaceFilter operationalState(String state) {
        return operationalState(OperationalState.valueOf(state));
    }

    /**
     * Sets the operational state the IFP must have.
     * @param state the operational state.
     * @return a reference to this filter to continue filter composition.
     */
    public ElementPhysicalInterfaceFilter operationalState(OperationalState state) {
        this.operationalState = state;
        return this;
    }
    
    /**
     * Tests whether the interface name matches the specified IFP name pattern.
     * @param ifpName the interface name to match the pattern.
     * @return <code>true</code> if the interface name matches the specified IFP name pattern or if no pattern is set, <code>false</code> otherwise.
     */
    public boolean ifpNameMatches(InterfaceName ifpName) {
        if(ifpNamePattern == null) {
            return ifpAliasPattern == null;
        }
        if(ifpName == null) {
            return false;
        }
        return ifpNamePattern.matcher(ifpName.toString()).matches();
        
    }
    
    /**
     * Tests whether the administrative state matches the expected state.
     * @param state the current administrative state
     * @return <code>true</code> if the given administrative state matches the expected administrative state or if no administrative state is specified, <code>false</code> otherwise.
     */
    public boolean administrativeStateMatches(AdministrativeState state) {
        if(administrativeState == null) {
            return true;
        }
        return administrativeState.equals(state);
    }

    /**
     * Tests whether the operational state matches the expected state.
     * @param state the current operational state
     * @return <code>true</code> if the given operational state matches the expected operational state or if no operational state is specified, <code>false</code> otherwise.
     */
    public boolean operationalStateMatches(OperationalState state) {
        if(operationalState == null) {
            return true;
        }
        return operationalState.equals(state);
    }

    /**
     * Tests whether the IFP alias matches the expected alias.
     * @param ifpAlias the IFP alias.
     * @return <code>true</code> if the IFP alias matches the alias pattern or if no alias pattern is <code>specified</code>, <code>false</code> otherwise.
     */
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
