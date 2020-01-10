/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

import io.leitstand.commons.model.CompositeValue;
import io.leitstand.security.auth.UserName;

public class ElementConfigReference extends CompositeValue {

	public static Builder newElementConfigReference() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementConfigReference ref = new ElementConfigReference();
		
		public Builder withConfigName(ElementConfigName configName) {
			assertNotInvalidated(getClass(), ref);
			ref.configName = configName;
			return this;
		}
		
		public Builder withCreator(UserName creator) {
			assertNotInvalidated(getClass(),ref);
			ref.creator = creator;
			return this;
		}
		
		public Builder withComment(String comment) {
			assertNotInvalidated(getClass(), ref);
			ref.comment = comment;
			return this;
		}
		
		public Builder withConfigState(ConfigurationState configState) {
			assertNotInvalidated(getClass(), ref);
			ref.configState = configState;
			return this;
		}

		public Builder withConfigId(ElementConfigId configId) {
			assertNotInvalidated(getClass(), ref);
			ref.configId = configId;
			return this;
		}
		
		public Builder withDateModified(Date date) {
			assertNotInvalidated(getClass(), ref);
			ref.dateModified = new Date(date.getTime());
			return this;
		}
		
		public Builder withContentType(String contentType) {
			assertNotInvalidated(getClass(), ref);
			ref.contentType = contentType;
			return this;
		}
		
		public ElementConfigReference build() {
			try {
				assertNotInvalidated(getClass(), ref);
				return ref;
			} finally {
				this.ref = null;
			}
		}

	}
	
	private ElementConfigId configId;
	private ElementConfigName configName;
	private ConfigurationState configState;
	private UserName creator;
	private String comment;
	private Date dateModified;
	private String contentType;
	
	public ElementConfigId getConfigId() {
		return configId;
	}
	
	public ElementConfigName getConfigName() {
		return configName;
	}
	
	public ConfigurationState getConfigState() {
		return configState;
	}
	
	public String getComment() {
		return comment;
	}
	
	public UserName getCreator() {
		return creator;
	}
	
	public Date getDateModified() {
		if(dateModified == null) {
			return null;
		}
		return new Date(dateModified.getTime());
	}

	public String getContentType() {
		return contentType;
	}
}
