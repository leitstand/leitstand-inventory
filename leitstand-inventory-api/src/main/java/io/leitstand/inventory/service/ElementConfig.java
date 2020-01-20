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

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.security.auth.UserName;

/**
 * A configuration of an element.
 * <p>
 * The resource inventory can store multiple configurations for every element.
 * Every configuration must have a unique name to seperate each configuration from each other.
 * </p>
 * @see ElementConfigurationName
 */
public class ElementConfig extends BaseElementEnvelope{

	/**
	 * Returns a new builder to create an immutable element configuration.
	 * @return a new builder to create an immutable element configuration.
	 */
	public static Builder newElementConfig() {
		return new Builder();
	}
	/**
	 * A builder to create an immutable <code>ElementConfig</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementConfig, Builder>{
		
		public Builder() {
			super(new ElementConfig());
		}

		public Builder withConfigId(ElementConfigId configId) {
			assertNotInvalidated(getClass(), object);
			object.configId = configId;
			return this;
		}
		
		
		public Builder withConfigName(ElementConfigName configName) {
			assertNotInvalidated(getClass(), object);
			object.configName = configName;
			return this;
		}

		public Builder withContentHash(String contentHash) {
			assertNotInvalidated(getClass(), object);
			object.contentHash = contentHash;
			return this;		
		}

		public Builder withConfigState(ConfigurationState configState) {
			assertNotInvalidated(getClass(), object);
			object.configState = configState;
			return this;		
		}
		
		public Builder withContentType(String contentType) {
			assertNotInvalidated(getClass(), object);
			object.contentType = contentType;
			return this;		
		}
		
		/**
		 * Sets the configuration data.
		 * @param config - the configuration data
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withConfig(Object config){
			assertNotInvalidated(getClass(), object);
			object.config = config;
			return this;
		}
		
		public Builder withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), object);
			object.dateModified = new Date(dateModified.getTime());
			return this;
		}
		
		public Builder withComment(String comment) {
			assertNotInvalidated(getClass(), object);
			object.comment = comment;
			return this;
		}
		
		public Builder withCreator(UserName creator) {
			assertNotInvalidated(getClass(), object);
			object.creator = creator;
			return this;
		}
		
		/**
		 * Returns an immutable element configuration.
		 * @return an immutable element configuration.
		 */
		@Override
		public ElementConfig build(){
			try{
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				this.object = null;
			}
		}



	}
	
	private ElementConfigId configId;
	@Valid
	@NotNull(message="{config_name.required}")
	private ElementConfigName configName;
	private ConfigurationState configState;
	
	@NotNull(message="{content_type.required}")
	private String contentType;
	private String contentHash;
	private String comment;
	
	@NotNull(message="{config.required}")
	private Object config;
	
	private UserName creator;
	
	private Date dateModified;
	
	
	public ElementConfigId getConfigId() {
		return configId;
	}
	
	
	public ConfigurationState getConfigState() {
		return configState;
	}
	
	public String getContentHash() {
		return contentHash;
	}
	
	/**
	 * Returns the symbolic configuration name.
	 * @return the configuration name
	 */
	public ElementConfigName getConfigName() {
		return configName;
	}
	
	/**
	 * Returns the element configuration for the specified element in the specified group.
	 * @return the element configuration.
	 */
	public Object getConfig() {
		return config;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Date getDateModified() {
		return new Date(dateModified.getTime());
	}

	public String getContentType() {
		return contentType;
	}
	
	public UserName getCreator() {
		return creator;
	}
	
}
