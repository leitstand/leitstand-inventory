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

@Embeddable
public class ElementManagementInterface extends ValueObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static Builder newElementManagementInterface() {
		return new Builder();
	}
	
	public static class Builder{
		
		private ElementManagementInterface instance = new ElementManagementInterface();
		
		public Builder withName(String name) {
			assertNotInvalidated(getClass(), instance);
			instance.name = name;
			return this;
		}
		
		public Builder withProtocol(String protocol) {
			assertNotInvalidated(getClass(), instance);
			instance.protocol = protocol;
			return this;
		}
		
		public Builder withHostname(String hostname) {
			assertNotInvalidated(getClass(), instance);
			instance.hostname = hostname;
			return this;
		}
		
		public Builder withPort(int portNumber) {
			assertNotInvalidated(getClass(),instance);
			instance.port = portNumber;
			return this;
		}
		
		public Builder withPath(String path) {
			assertNotInvalidated(getClass(),path);
			instance.path = path;
			return this;
		}
		
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
	
	
	public String getName() {
		return name;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public int getPort() {
		return port;
	}
	
	public String getHostname() {
		return hostname;
	}
	
	public String getPath() {
		return path;
	}
	
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
