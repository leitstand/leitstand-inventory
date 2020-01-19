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
