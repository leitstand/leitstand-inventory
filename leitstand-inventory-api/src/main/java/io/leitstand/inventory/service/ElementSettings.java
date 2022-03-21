/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * General element settings.
 */
public class ElementSettings extends BaseElementEnvelope{

	/**
	 * Returns a builder for an immutable <code>ElementSettings</code> value object.
	 * @return a builder for an immutable <code>ElementSettings</code> value object.
	 */
	public static Builder newElementSettings() {
		return new Builder();
	}

	/**
	 * A builder to create an immutable <code>ElementSettings</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementSettings, Builder>{
		
		public Builder() {
			super(new ElementSettings());
		}
				
	    /**
         * Sets the element serial number.
         * @param serialNumber the serial number
         * @return a reference to this builder to continue object creation
         */
		public Builder withSerialNumber(String serialNumber) {
			assertNotInvalidated(getClass(), object);
			object.serialNumber = serialNumber;
			return this;
		}
		
		/**
		 * Sets the asset ID
		 * @param assetId the asset identifier
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAssetId(String assetId) {
			assertNotInvalidated(getClass(), object);
			object.assetId = assetId;
			return this;
		}
		
		/**
		 * Sets the MAC address of the management interface.
		 * @param address the management interface MAC address
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withManagementInterfaceMacAddress(MACAddress address) {
			assertNotInvalidated(getClass(), object);
			object.managementInterfaceMacAddress = address;
			return this;
		}
		
		/**
		 * Sets the element description.
		 * @param description the element description
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		/**
		 * Sets the element plane.
		 * @param plane the plane
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPlane(Plane plane) {
			assertNotInvalidated(getClass(), object);
			object.plane = plane;
			return this;
		}
		
		/**
		 * Sets the element management interfaces
		 * @param mgmtInterfaces the element management interfaces
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withManagementInterfaces(ElementManagementInterface.Builder... mgmtInterfaces) {
			return withManagementInterfaces(stream(mgmtInterfaces)
										    .map(ElementManagementInterface.Builder::build)
										    .collect(toMap(ElementManagementInterface::getName, 
										    			   identity())));
		}

	    /**
         * Sets the element management interfaces
         * @param mgmtInterfaces the element management interfaces
         * @return a reference to this builder to continue object creation
         */
		public Builder withManagementInterfaces(Map<String,ElementManagementInterface> mgmtInterfaces) {
			assertNotInvalidated(getClass(), object);
			object.managementInterfaces = new TreeMap<>(mgmtInterfaces);
			return this;
		}
		
	    /**
         * Sets the element tags
         * @param tags the element tags
         * @return a reference to this builder to continue object creation
         */
		public Builder withTags(String... tags){
			return withTags(new TreeSet<>(Arrays.asList(tags)));
		}
		
	    /**
         * Sets the element tags
         * @param tags the element tags
         * @return a reference to this builder to continue object creation
         */	
		public Builder withTags(Set<String> tags){
			assertNotInvalidated(getClass(), object);
			object.tags = unmodifiableSet(new TreeSet<String>(tags));
			return this;
		}

	}
	
	@JsonbProperty("mgmt_interfaces")
	@Valid
	private Map<String,ElementManagementInterface> managementInterfaces = emptyMap();
	
	
	@JsonbProperty("mgmt_mac")
	@Valid
	private MACAddress managementInterfaceMacAddress;
	private String serialNumber;
	private String assetId;

	
	@JsonbProperty
	private Plane plane;

	@JsonbProperty
	@Size(max=1024, message="{description.too_long}")
	private String description;
	
	@JsonbProperty
	private Set<String> tags = emptySet();
	
	
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the element management interfaces.
	 * @return an immutable map of management interfaces.
	 */
	public Map<String,ElementManagementInterface> getManagementInterfaces() {
		return unmodifiableMap(managementInterfaces);
	}
	
	/**
	 * Returns the element plane.
	 * @return the element plane.
	 */
	public Plane getPlane() {
		return plane;
	}
	
	/**
	 * Returns the element tags.
	 * @return an immutable set of element tags.
	 */
	public Set<String> getTags() {
		return unmodifiableSet(tags);
	}
	
	/**
	 * Returns the element serial number.
	 * @return the element serial number.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	
	/**
	 * Returns the MAC address of the element management port.
	 * @return the MAC address of the element management port.
	 */
	public MACAddress getManagementInterfaceMacAddress() {
		return managementInterfaceMacAddress;
	}

	/**
	 * Returns the URI to access the management interface.
	 * @param name the management interface name
	 * @return the management interface URI or <code>null</code> when the management interface does not exist
	 */
	public URI getManagementInterfaceUri(String name) {
		ElementManagementInterface ifc = managementInterfaces.get(name);
		if(ifc != null) {
			return ifc.toURI();
		}
		return null;
	}

	/**
	 * Returns the management interface with the given name.
	 * @param name the management interface name
	 * @return the management interface or <code>null</code> if the management interface does not exist
	 */
	public ElementManagementInterface getManagementInterface(String name) {
		return managementInterfaces.get(name);
	}
	
	/**
	 * Returns the element asset ID
	 * @return the element asset ID
	 */
	public String getAssetId() {
		return assetId;
	}
	
	
}
