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
import static io.leitstand.inventory.service.RackItemData.Face.FRONT;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.CompositeValue;

public class RackItemData extends CompositeValue{

	public enum Face{
		FRONT,
		REAR
	}
	
	public static Builder newRackItemData() {
		return new Builder();
	}

	public static class Builder {
		
		private RackItemData rack = new RackItemData();
		
		public Builder withGroupId(ElementGroupId groupId) {
			assertNotInvalidated(getClass(), rack);
			rack.groupId = groupId;
			return this;
		}
		
		public Builder withGroupType(ElementGroupType groupType) {
			assertNotInvalidated(getClass(), rack);
			rack.groupType = groupType;
			return this;
		}
		
		public Builder withGroupName(ElementGroupName groupName) {
			assertNotInvalidated(getClass(), rack);
			rack.groupName = groupName;
			return this;
		}
		
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

		public Builder withPlatformId(PlatformId platformId) {
			assertNotInvalidated(getClass(), rack);
			rack.platformId = platformId;
			return this;
		}
		
		public Builder withAdministrativeState(AdministrativeState state) {
			assertNotInvalidated(getClass(),rack);
			rack.administrativeState = state;
			return this;
		}
		
		public Builder withPlatformName(PlatformName platformName) {
			assertNotInvalidated(getClass(), rack);
			rack.platformName = platformName;
			return this;
		}
		

		public Builder withHeight(int height) {
			assertNotInvalidated(getClass(), rack);
			rack.height = height;
			return this;
		}
		
		public Builder withPosition(int position) {
			assertNotInvalidated(getClass(), rack);
			rack.position = position;
			return this;
		}
		
		public Builder withFace(Face face) {
			assertNotInvalidated(getClass(), rack);
			rack.face = face;
			return this;
		}
		
		public Builder withRackItemName(String name) {
			assertNotInvalidated(getClass(), rack);
			rack.rackItemName = name;
			return this;
		}
		
		public RackItemData build() {
			try {
				assertNotInvalidated(getClass(), rack);
				return rack;
			} finally {
				this.rack = null;
			}
		}


		
	}
	
	@Valid
	private ElementGroupId groupId;

	@Valid
	private ElementGroupType groupType;

	@Valid
	private ElementGroupName groupName;
	
	@Valid
	private ElementId elementId;

	@Valid
	private ElementName elementName;

	@Valid
	private ElementAlias elementAlias;
	
	@Valid
	private ElementRoleName elementRole;

	@Valid
	private AdministrativeState administrativeState;

	@Valid
	private PlatformId platformId;

	@Valid
	private PlatformName platformName;

	private String rackItemName;

	private int position;

	@NotNull(message="{face.required}")
	private Face face = FRONT;
	private int height;

	public ElementGroupId getGroupId() {
		return groupId;
	}
	
	public ElementGroupName getGroupName() {
		return groupName;
	}
	
	public ElementGroupType getGroupType() {
		return groupType;
	}
	
	public ElementId getElementId() {
		return elementId;
	}
	
	public ElementName getElementName() {
		return elementName;
	}
	
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	public PlatformId getPlatformId() {
		return platformId;
	}
	
	public PlatformName getPlatformName() {
		return platformName;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ElementAlias getElementAlias() {
		return elementAlias;
	}

	public String getRackItemName() {
		return rackItemName;
	}
	
	public Face getFace() {
		return face;
	}
}
