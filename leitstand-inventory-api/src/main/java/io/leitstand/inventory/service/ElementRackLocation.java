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

import io.leitstand.commons.model.ValueObject;

public class ElementRackLocation extends ValueObject{

	public static Builder newElementRackLocation() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementRackLocation instance = new ElementRackLocation();
		
		public Builder withRackName(RackName rackName) {
			assertNotInvalidated(getClass(), instance);
			instance.rackName = rackName;
			return this;
		}
		
		public Builder withUnit(int unit) {
			assertNotInvalidated(getClass(), instance);
			instance.unit = unit;
			return this;
		}
		
		public Builder withPosition(ElementRackLocationPosition position) {
			assertNotInvalidated(getClass(), instance);
			instance.position = position;
			return this;
		}
		
		public ElementRackLocation build() {
			try {
				assertNotInvalidated(getClass(), instance);
				return instance;
			} finally {
				this.instance = null;
			}
		}
		
	}
	
	public enum ElementRackLocationPosition {
		LEFT,
		RIGHT;
	}
	
	private RackName rackName;
	private int unit;
	private ElementRackLocationPosition position;
	
	public RackName getRackName() {
		return rackName;
	}
	
	public int getUnit() {
		return unit;
	}
	
	public ElementRackLocationPosition getPosition() {
		return position;
	}
	
}
