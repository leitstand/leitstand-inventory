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
		
		protected Builder() {
			super(new ElementConfig());
		}

		/**
		 * Sets the configuration ID, which is unique among all element configurations.
		 * @param configId the configuration ID
		 * @return a reference to this builder to continue with the object creation.
		 */
		public Builder withConfigId(ElementConfigId configId) {
			assertNotInvalidated(getClass(), object);
			object.configId = configId;
			return this;
		}
		
		
		/**
		 * Sets the configuration name.
		 * @param configName the configuration name
		 * @return a reference to this builder to continue with the object creation.
		 */		
		public Builder withConfigName(ElementConfigName configName) {
			assertNotInvalidated(getClass(), object);
			object.configName = configName;
			return this;
		}

		/**
		 * Sets the configuration content hash.
		 * @param contentHash the configuration content hash
		 * @return a reference to this builder to continue with the object creation.
		 */
		public Builder withContentHash(String contentHash) {
			assertNotInvalidated(getClass(), object);
			object.contentHash = contentHash;
			return this;		
		}

		/**
		 * Sets the configuration lifecycle state.
		 * @param configState the configuration lifecycle state.
		 * @return a reference to this builder to continue with the object creation.
		 */
		public Builder withConfigState(ConfigurationState configState) {
			assertNotInvalidated(getClass(), object);
			object.configState = configState;
			return this;		
		}
		
		/**
		 * Sets teh configuration content type.
		 * @param contentType the content type.
		 * @return a reference to this builder to continue with the object creation.
		 */
		public Builder withContentType(String contentType) {
			assertNotInvalidated(getClass(), object);
			object.contentType = contentType;
			return this;		
		}
		
		/**
		 * Sets the configuration modification date.
		 * @param dateModified the modification date
		 * @return a reference to this builder to continue with the object creation.
		 */
		public Builder withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), object);
			object.dateModified = new Date(dateModified.getTime());
			return this;
		}
		
		/**
		 * Sets the comment that describes the configuration change.
		 * @param comment the configuration comment
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withComment(String comment) {
			assertNotInvalidated(getClass(), object);
			object.comment = comment;
			return this;
		}
		
		/**
		 * Sets the creator of the configuration.
		 * @param creator the user name of the configuration creator
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withCreator(UserName creator) {
			assertNotInvalidated(getClass(), object);
			object.creator = creator;
			return this;
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
	
	private UserName creator;
	
	private Date dateModified;
	
	/**
	 * Returns the configuration ID. 
	 * The configuration ID is unique among all element configurations.
	 * @return the configuration ID.
	 */
	public ElementConfigId getConfigId() {
		return configId;
	}
	
	
	/**
	 * Returns the configuration lifecycle state.
	 * @return the configuration lifecycle state.
	 */
	public ConfigurationState getConfigState() {
		return configState;
	}
	
	/**
	 * Returns the configuration content hash.
	 * The content hash is a Base36-encoded SHA-256 hash of the configuration content
	 * and used as key to access the configuration content in the configuration store.
	 * @return the configuration content hash.
	 */
	public String getContentHash() {
		return contentHash;
	}
	
	/**
	 * Returns the configuration name.
	 * @return the configuration name
	 */
	public ElementConfigName getConfigName() {
		return configName;
	}
	
	/**
	 * Returns the configuration comment, which describes the changes that have been applied by this configuration.
	 * @return the configuration comment.
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Returns the timestamp when the configuration got applied to the element.
	 * @return the timestamp when the configuration got applied to the element.
	 */
	public Date getDateModified() {
		return new Date(dateModified.getTime());
	}

	/**
	 * Returns the configuration content type.
	 * @return the configuration content type.
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Returns the username of the user who created the configuration.
	 * @return the username of the user who created the configuration.
	 */
	public UserName getCreator() {
		return creator;
	}
	
}
