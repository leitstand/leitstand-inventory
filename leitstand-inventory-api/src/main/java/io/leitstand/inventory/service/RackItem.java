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

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.CompositeValue;
import io.leitstand.inventory.service.ElementRackLocation.ElementRackLocationPosition;

public class RackItem extends CompositeValue{

	public static Builder newRackItem() {
		return new Builder();
	}

	public static class Builder {
		
		private RackItem rack = new RackItem();
		
		public Builder withElementId(ElementId elementId) {
			assertNotInvalidated(getClass(), rack);
			rack.elementId = elementId;
			return this;
		}

		public Builder withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), rack);
			rack.elementName = elementName;
			return this;
		}
		

		public Builder withElementAlias(ElementAlias elementAlias) {
			assertNotInvalidated(getClass(), rack);
			rack.elementAlias = elementAlias;
			return this;
		}

		public Builder withElementRole(ElementRoleName elementRole) {
			assertNotInvalidated(getClass(), rack);
			rack.elementRole = elementRole;
			return this;
		}

		public Builder withPlatform(ElementPlatformInfo platform) {
			assertNotInvalidated(getClass(), rack);
			rack.platform = platform;
			return this;
		}

		public Builder withHeight(int height) {
			assertNotInvalidated(getClass(), rack);
			rack.height = height;
			return this;
		}
		
		public Builder withHalfRack(boolean halfRack) {
			assertNotInvalidated(getClass(), rack);
			rack.halfRack = halfRack;
			return this;
		}

		public Builder withHalfRackPosition(ElementRackLocationPosition halfRackPosition) {
			assertNotInvalidated(getClass(), rack);
			rack.halfRackPosition = halfRackPosition;
			return this;
		}
		
		public Builder withUnit(int unit) {
			assertNotInvalidated(getClass(), rack);
			rack.unit = unit;
			return this;
		}
		
		public RackItem build() {
			try {
				assertNotInvalidated(getClass(), rack);
				return rack;
			} finally {
				this.rack = null;
			}
		}


		
	}
	
	private ElementId elementId;
	private ElementName elementName;
	private ElementAlias elementAlias;
	private ElementRoleName elementRole;
	private ElementPlatformInfo platform;
	private int unit;
	private int height;
	private boolean halfRack;
	@JsonbProperty("half_rack_pos")
	private ElementRackLocationPosition halfRackPosition;

	
	public ElementId getElementId() {
		return elementId;
	}
	
	public ElementName getElementName() {
		return elementName;
	}
	
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	public ElementPlatformInfo getPlatform() {
		return platform;
	}
	
	public int getUnit() {
		return unit;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean isHalfRack() {
		return halfRack;
	}
	
	public ElementRackLocationPosition getHalfRackPosition() {
		return halfRackPosition;
	}
	
	public ElementAlias getElementAlias() {
		return elementAlias;
	}
	
}
