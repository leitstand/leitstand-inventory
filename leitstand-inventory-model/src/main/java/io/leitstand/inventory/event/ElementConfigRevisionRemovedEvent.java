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
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

public class ElementConfigRevisionRemovedEvent extends ElementConfigEvent {

	public static Builder newElementConfigRevisionRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementConfigEventBuilder<ElementConfigRevisionRemovedEvent, Builder>{
		
		Builder(){
			super(new ElementConfigRevisionRemovedEvent());
		}
		
		public Builder withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), object);
			object.dateModified = new Date(dateModified.getTime());
			return this;
		}
	}
	
	private Date dateModified;
	
	public Date getDateModified() {
		if(dateModified == null) {
			return null;
		}
		return new Date(dateModified.getTime());
	}

	
		
}
