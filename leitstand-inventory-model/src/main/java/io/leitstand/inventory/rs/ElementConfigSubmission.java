/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

import io.leitstand.commons.model.CompositeValue;

public class ElementConfigSubmission extends CompositeValue {

	public static Builder newElementConfigSubmission() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementConfigSubmission submission = new ElementConfigSubmission();
		
		public Builder withComment(String comment) {
			assertNotInvalidated(getClass(), submission);
			submission.comment = comment;
			return this;
		}

		public Builder withContentType(String contentType) {
			assertNotInvalidated(getClass(), submission);
			submission.contentType = contentType;
			return this;
		}		
		
		public Builder withConfig(JsonValue config) {
			assertNotInvalidated(getClass(), submission);
			submission.config = config;
			return this;
		}		
		
		public ElementConfigSubmission build() {
			try {
				assertNotInvalidated(getClass(), submission);
				return submission;
			} finally {
				this.submission = null;
			}
		}
		
		
	}
	
	private String contentType;
	private String comment;
	private JsonValue config;
	
	public String getContentType() {
		return contentType;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String getConfig() {
		if(contentType.equals("application/json")) {
			return config.toString();
		}
		if(config.getValueType() == ValueType.STRING) {
			return ((JsonString)config).getString();
		}
		return config.toString();
	}
	
}
