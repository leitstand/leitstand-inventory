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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The <code>RouterSettings</code> object contains the settings of a router.
 */

public class ElementSettings extends BaseElementEnvelope{

	/**
	 * Returns a builder to create an immutable <code>RouterSettings</code> instance.
	 * @return a builder to create an immutable <code>RouterSettings</code> instance.
	 */
	public static Builder newElementSettings() {
		return new Builder();
	}

	/**
	 * A builder to create an immutable <code>RouterSettings</code> instance.@author mast
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementSettings, Builder>{
		
		public Builder() {
			super(new ElementSettings());
		}
		
		public Builder withPlatformId(PlatformId platformId) {
			assertNotInvalidated(getClass(),object);
			object.platformId = platformId;
			return this;
		}
		
		public Builder withPlatformName(PlatformName platformName) {
			assertNotInvalidated(getClass(),object);
			object.platformName = platformName;
			return this;
		}
		
		public Builder withSerialNumber(String serialNumber) {
			assertNotInvalidated(getClass(), object);
			object.serialNumber = serialNumber;
			return this;
		}
		
		public Builder withManagementInterfaceMacAddress(MACAddress address) {
			assertNotInvalidated(getClass(), object);
			object.managementInterfaceMacAddress = address;
			return this;
		}
		
		public Builder withAdministrativeState(AdministrativeState admState){
			assertNotInvalidated(getClass(), object);
			object.administrativeState = admState;
			return this;
		}

		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
		public Builder withPlane(Plane plane) {
			assertNotInvalidated(getClass(), object);
			object.plane = plane;
			return this;
		}
		
		public Builder withManagementInterfaces(ElementManagementInterface.Builder... mgmtInterfaces) {
			return withManagementInterfaces(stream(mgmtInterfaces)
										    .map(ElementManagementInterface.Builder::build)
										    .collect(toMap(ElementManagementInterface::getName, 
										    			   identity())));
		}

		
		public Builder withManagementInterfaces(Map<String,ElementManagementInterface> mgmtInterfaces) {
			assertNotInvalidated(getClass(), object);
			object.managementInterfaces = new TreeMap<>(mgmtInterfaces);
			return this;
		}

		
		public Builder withOperationalState(OperationalState operationalState) {
			assertNotInvalidated(getClass(), object);
			object.operationalState = operationalState;
			return this;
		}
		
		public Builder withTags(String... tags){
			return withTags(new TreeSet<>(Arrays.asList(tags)));
		}
		
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
	private MACAddress managementInterfaceMacAddress;
	private String serialNumber;

	
	@JsonbProperty
	private Plane plane;
	
	@JsonbProperty("administrative_state")
	@NotNull(message="{administrative_state.required}")
	private AdministrativeState administrativeState;
	
	@JsonbProperty("operational_state")
	@NotNull(message="{operational_state.required}")
	private OperationalState operationalState;
	
	@JsonbProperty
	@Size(max=1024, message="{description.too_long}")
	private String description;
	
	@JsonbProperty
	private Set<String> tags = emptySet();
	
	private PlatformId platformId;
	private PlatformName platformName;
	
	
	public String getDescription() {
		return description;
	}
	
	public Map<String,ElementManagementInterface> getManagementInterfaces() {
		return managementInterfaces;
	}
	
	public Plane getPlane() {
		return plane;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
	public OperationalState getOperationalState(){
		return operationalState;
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public PlatformId getPlatformId() {
		return platformId;
	}
	
	public PlatformName getPlatformName() {
		return platformName;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	public MACAddress getManagementInterfaceMacAddress() {
		return managementInterfaceMacAddress;
	}

	public URI getManagementInterfaceUri(String name) {
		ElementManagementInterface ifc = managementInterfaces.get(name);
		if(ifc != null) {
			return ifc.toURI();
		}
		return null;
	}

	public ElementManagementInterface getManagementInterface(String name) {
		return managementInterfaces.get(name);
	}
	
}
