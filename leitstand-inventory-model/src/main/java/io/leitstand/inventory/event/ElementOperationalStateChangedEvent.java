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

import io.leitstand.inventory.service.OperationalState;

public class ElementOperationalStateChangedEvent extends ElementEvent {

	public static Builder newElementOperationalStateChangedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementEventBuilder<ElementOperationalStateChangedEvent, Builder> {
		protected Builder() {
			super(new ElementOperationalStateChangedEvent());
		}
		
		public Builder withPreviousState(OperationalState previousState) {
			assertNotInvalidated(getClass(), object);
			object.previousOpertionalState = previousState;
			return this;
		}
		
		public Builder withOperationalState(OperationalState operationalState) {
			assertNotInvalidated(getClass(), object);
			object.operationalState = operationalState;
			return this;
		}
	}
	
	private OperationalState operationalState;
	private OperationalState previousOpertionalState;

	public OperationalState getPreviousState() {
		return previousOpertionalState;
	}
	
	public OperationalState getOperationalState() {
		return operationalState;
	}
}
