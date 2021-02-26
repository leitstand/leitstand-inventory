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
import static io.leitstand.commons.model.Patterns.DNS_PATTERN;
import static io.leitstand.commons.model.Patterns.PATH_PATTERN;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static java.net.URI.create;

import java.io.Serializable;
import java.net.URI;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.ValueObject;

/**
 * An element management interface.
 * <p>
 * A management interface allows managing a element remotely over the network.
 * Examples for management interfaces are SSH access or a REST API.
 */
@Embeddable
public class ElementManagementInterface extends ValueObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a builder for an element management interface.
	 * @return a element management interface builder.
	 */
	public static Builder newElementManagementInterface() {
		return new Builder();
	}
	
	/**
     * A builder to create an immutable <code>ElementManagementInterface</code> instance.
	 */
	public static class Builder{
		
		private ElementManagementInterface instance = new ElementManagementInterface();
		
		/**
		 * Sets the management interface name
		 * @param name the management interface name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withName(String name) {
			assertNotInvalidated(getClass(), instance);
			instance.name = name;
			return this;
		}

	    /**
         * Sets the management interface network protocol
         * @param protocol the management interface network protocol
         * @return a reference to this builder to continue object creation
         */
		public Builder withProtocol(String protocol) {
			assertNotInvalidated(getClass(), instance);
			instance.protocol = protocol;
			return this;
		}
		
	    /**
         * Sets the management interface hostname or IP address
         * @param hostname the management interface hostname or IP address
         * @return a reference to this builder to continue object creation
         */
		public Builder withHostname(String hostname) {
			assertNotInvalidated(getClass(), instance);
			instance.hostname = hostname;
			return this;
		}
		
	    /**
         * Sets the management interface port
         * @param name the management interface port
         * @return a reference to this builder to continue object creation
         */
		public Builder withPort(int portNumber) {
			assertNotInvalidated(getClass(),instance);
			instance.port = portNumber;
			return this;
		}
		
	    /**
         * Sets the management interface base path
         * @param name the management interface base path
         * @return a reference to this builder to continue object creation
         */
		public Builder withPath(String path) {
			assertNotInvalidated(getClass(),path);
			instance.path = path;
			return this;
		}
		
		/**
		 * Returns an immutable element management interface.
		 * @return the element management interface.
		 */
		public ElementManagementInterface build() {
			try {
				assertNotInvalidated(getClass(), instance);
				return instance;
			} finally {
				this.instance = null;
			}
		}
		
	}
	@JsonbProperty("mgmt_name")
	@NotNull(message="{mgmt_name.required}")
	@Pattern(regexp="\\w{1,32}", message="{mgmt_name.invalid}")
	private String name;
	
	@JsonbProperty("mgmt_protocol")
	@NotNull(message="{mgmt_protocol.required}")
	@Pattern(regexp="\\w{1,32}", message="{mgmt_protocol.invalid}")
	private String protocol;
	
	
	@JsonbProperty("mgmt_hostname")
	@NotNull(message="{mgmt_hostname.required}")
	@Pattern(regexp=DNS_PATTERN, message="{mgmt_hostname.invalid}")
	private String hostname;
	
	@JsonbProperty("mgmt_port")
	@Min(value=1, message="{mgmt_port.range}")
	@Max(value=65536, message="{mgmt_port.range}")
	private int port;
	
	@JsonbProperty("mgmt_path")
	@Pattern(regexp=PATH_PATTERN, message="{mgmt_path.invalid}")
	private String path;
	
	
	/**
	 * Returns the element management interface name.
	 * @return the element management interface name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the management interface protocol.
	 * @return the management interface protocol.
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/**
	 * Returns the management interface port.
	 * @return the management interface port.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Returns the management interface hostname or IP address.
	 * @return the management interface hostname or IP address.
	 */
	public String getHostname() {
		return hostname;
	}
	
	/**
	 * Returns the management interface base path.
	 * @return the management interface base path.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Returns the management interface endpoint URI
	 * @return the management interface URI
	 */
	@JsonbTransient
	public URI toURI() {
		String origin = getProtocol()+"://"+getHostname()+":"+getPort();
		if(isNonEmptyString(path)) {
			if(path.startsWith("/")) {
				return create(origin+path);
			}
			return create(origin+"/"+path);
		}
		return create(origin);
	}
	
}
