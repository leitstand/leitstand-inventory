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

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.inventory.service.ElementGroupSettings;

public class ElementMovedEvent extends ElementEvent{

	public static Builder newElementMovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementMovedEvent, Builder>{
		Builder(){
			super(new ElementMovedEvent());
		}
		
		public Builder withFrom(ElementGroupSettings from) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			object.from = from;
			return this;
		}

		public Builder withTo(ElementGroupSettings to) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			object.to = to;
			return this;
		}
	}
	
	private ElementGroupSettings from;
	private ElementGroupSettings to;

	public ElementGroupSettings getFrom() {
		return from;
	}
	
	public ElementGroupSettings getTo() {
		return to;
	}
	
}
