package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static javax.persistence.EnumType.STRING;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.validation.Valid;

import io.leitstand.commons.model.ValueObject;

/**
 * The PlatformPortMapping provides information how a physical port is mapped to a physical interface.
 */
@Embeddable
public class PlatformPortMapping extends ValueObject implements Comparable<PlatformPortMapping>{

    public enum Face {
        FRONT,
        BACK
    }
    
    /**
     * Returns a builder for an immutable <code>PlatformPortMapping</code> object.
     * @return a builder for an immutable <code>PlatformPortMapping</code> object.
     */
    public static Builder newPlatformPortMapping() {
        return new Builder();
    }
    
    
    /**
     * A builder for a <code>PlatformPortMapping</code> object.
     */
    public static class Builder {
        
        private PlatformPortMapping mapping = new PlatformPortMapping();
        
        /**
         * Sets the physical interface name.
         * @param ifpName the physical interface name
         * @return a reference to this builder to continue object creation
         */
        public Builder withIfpName(InterfaceName ifpName) {
            assertNotInvalidated(getClass(),mapping);
            mapping.ifpName = ifpName;
            return this;
        }

        /**
         * Sets the chassis identifier.
         * @param chassId the chassis identifier
         * @return a reference to this builder to continue object creation
         */
        public Builder withChassisId(String chassisId) {
            assertNotInvalidated(getClass(),mapping);
            mapping.chassisId = chassisId;
            return this;
        }
        
        /**
         * Sets the face where the port is installed at.
         * @param face the face identifier
         * @return a reference to this builder to continue object creation
         */
        public Builder withFace(Face face) {
            assertNotInvalidated(getClass(),mapping);
            mapping.face = face;
            return this;
        }
        
        /**
         * Sets the panel block identifier.
         * @param panelBlockId the panel identifier
         * @return a reference to this builder to continue object creation
         */
        public Builder withPanelBlockId(String panelBlockId) {
            assertNotInvalidated(getClass(),mapping);
            mapping.panelBlockId = panelBlockId;
            return this;
        }
        
        
        /**
         * Sets the port identifier
         * @param portId the port identifier
         * @return a reference to this builder to continue object creation
         */
        public Builder withPortId(String portId) {
            assertNotInvalidated(getClass(),mapping);
            mapping.portId = portId;
            return this;
        }
        
        /**
         * Sets the port alias.
         * @param portAlias the port alias
         * @return a reference to this builder to continue object creation
         */
        public Builder withPortAlias(String portAlias) {
            assertNotInvalidated(getClass(),mapping);
            mapping.portAlias = portAlias;
            return this;
        }
        
        /**
         * Sets the description
         * @param description the port description
         * @return a reference to this builder to continue object creation
         */
        public Builder withDescription(String description) {
            assertNotInvalidated(getClass(),mapping);
            mapping.description = description;
            return this;
        }
        
        /**
         * Sets the port bandwidth.
         * @param bandwidth the port bandwidth.
         * @return a reference to this buidler to continue object creation
         */
        public Builder withBandwidth(Bandwidth bandwidth) {
            assertNotInvalidated(getClass(),mapping);
            mapping.bandwidth = bandwidth;
            return this;
        }
        
        
        /**
         * Creates an immutable <code>PlatformPortMapping</code> object and
         * invalidates this builder. Subsequent invocations of this builder raise an exception.
         * @return the <code>PlatformPortMapping</code> object.
         */
        public PlatformPortMapping build() {
            try {
                assertNotInvalidated(getClass(),mapping);
                return mapping;
            } finally {
                this.mapping = null;
            }
        }
        
    }
    
    @Valid
    private InterfaceName ifpName;
    private String chassisId;
    @Enumerated(STRING)
    private Face face;
    private String panelBlockId;
    private String portId;
    private String portAlias;
    private String description;
    @AttributeOverrides({
        @AttributeOverride(name="value", column=@Column(name="bwvalue")),
        @AttributeOverride(name="unit", column=@Column(name="bwunit"))
    })
    @Valid
    private Bandwidth bandwidth;
       

    /**
     * Returns the physical interface name.
     * @return the physical interface name.
     */
    public InterfaceName getIfpName() {
        return ifpName;
    }
    
    /**
     * Returns the chassis identifier.
     * @return the chassis identified.
     */
    public String getChassisId() {
        return chassisId;
    }
    
    /**
     * Returns on which face (front or back) the port is installed at.
     * @return the face where the port is installed at.
     */
    public Face getFace() {
        return face;
    }
    
    /**
     * Returns the panel block where the port is installed at.
     * @return the panel block where the port is installed at.
     */
    public String getPanelBlockId() {
        return panelBlockId;
    }
    
    /**
     * Returns the port identifier.
     * @return the port identifier.
     */
    public String getPortId() {
        return portId;
    }
    
    /**
     * Returns the port alias or <code>null</code> if no alias is assigned.
     * The alias allows supporting a naming scheme used by network management systems.
     * @return an alias assigned to this port
     */
    public String getPortAlias() {
        return portAlias;
    }
    
    /**
     * Returns the port description or <code>null</code> if no alias is assigned.
     * @return the port description
     */
    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(PlatformPortMapping o) {
        int d = compare(chassisId, o.chassisId);
        if (d != 0) {
            return d;
        }
        d = compare(face, o.face);
        if (d != 0){
            return d;
        }
        d = compare(panelBlockId, o.panelBlockId);
        if (d != 0) {
            return d;
        }
        d = compare(portId, o.portId);
        if (d != 0){
            return d;
        }
        d = compare(portAlias, o.portAlias);
        if (d != 0){
            return d;
        }
        d = compare(ifpName, o.ifpName);
        if (d!=0){
            return d;
        }
        d = compare(bandwidth, o.bandwidth);
        if (d!=0){
            return d;
        }
        return compare(description, o.description);
    
    }
    
    static <T> int compare(Comparable<T> a, T b) {
        if (a != null && b != null) {
            return a.compareTo(b);
        }
        if (b != null) {
            return 1;
        }
        return -1;
        
    }
    
}
