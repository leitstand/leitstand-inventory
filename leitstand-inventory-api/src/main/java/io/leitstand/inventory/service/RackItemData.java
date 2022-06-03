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
import static io.leitstand.inventory.service.RackItemId.randomRackItemId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.CompositeValue;

/**
 * The rack item provides information which element is installed in the rack at the specified rack unit including platform, element role and element group informations.
 * @see RackItem
 * @see RackItems
 */
public class RackItemData extends CompositeValue{

    /**
     * Enumeration of rack faces.
     */
	public enum Face{
	    /** The front rack face.*/
		FRONT,
		/** The rear rack face.*/
		REAR
	}
	
	/**
	 * Creates a builder for an immutable <code>RackItemData</code> value object.
	 * @return a builder for an immutable <code>RackItemData</code> value object.
	 */
	public static Builder newRackItemData() {
		return new Builder();
	}

	/**
	 * A builder for an immutable <code>RackItemData</code> value object.
	 */
	public static class Builder {
		
		private RackItemData rack = new RackItemData();
		
		/**
		 * Sets the ID of the element group the installed element is member of.
		 * @param groupId the element group ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withGroupId(ElementGroupId groupId) {
			assertNotInvalidated(getClass(), rack);
			rack.groupId = groupId;
			return this;
		}
		
		/**
		 * Sets the type of element group the installed element is member of.
		 * @param groupType the element group type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withGroupType(ElementGroupType groupType) {
			assertNotInvalidated(getClass(), rack);
			rack.groupType = groupType;
			return this;
		}
		
		/**
		 * Sets the name of the element group the installed element is member of.
		 * @param groupName the element group name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withGroupName(ElementGroupName groupName) {
			assertNotInvalidated(getClass(), rack);
			rack.groupName = groupName;
			return this;
		}
		
		/**
		 * Sets the ID of the installed element.
		 * @param elementId the element ID.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withElementId(ElementId elementId) {
			assertNotInvalidated(getClass(), rack);
			rack.elementId = elementId;
			return this;
		}

		/**
         * Sets the name of the installed element.
         * @param elementName the element name.
         * @return a reference to this builder to continue object creation
         */
		public Builder withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), rack);
			rack.elementName = elementName;
			return this;
		}
		
        /**
         * Sets the alias of the installed element.
         * @param elementAlias the element alias.
         * @return a reference to this builder to continue object creation
         */
		public Builder withElementAlias(ElementAlias elementAlias) {
			assertNotInvalidated(getClass(), rack);
			rack.elementAlias = elementAlias;
			return this;
		}

        /**
         * Sets the role of the installed element.
         * @param elementRole the element role.
         * @return a reference to this builder to continue object creation
         */
		public Builder withElementRole(ElementRoleName elementRole) {
			assertNotInvalidated(getClass(), rack);
			rack.elementRole = elementRole;
			return this;
		}

        /**
         * Sets the platform ID of the installed element.
         * @param platformId the element platform ID.
         * @return a reference to this builder to continue object creation
         */
		public Builder withPlatformId(PlatformId platformId) {
			assertNotInvalidated(getClass(), rack);
			rack.platformId = platformId;
			return this;
		}

        /**
         * Sets the platform name of the installed element.
         * @param platformName the element platform name.
         * @return a reference to this builder to continue object creation
         */
		public Builder withPlatformName(PlatformName platformName) {
		    assertNotInvalidated(getClass(), rack);
		    rack.platformName = platformName;
		    return this;
		}

		/**
		 * Sets the administrative state of the installed element.
		 * @param state the administrative state of the installed element.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAdministrativeState(AdministrativeState state) {
			assertNotInvalidated(getClass(),rack);
			rack.administrativeState = state;
			return this;
		}

		/**
		 * Sets the height of the installed element in rack units.
		 * @param height the height in rack units.
		 * @return the height of the installed element in rack units.
		 */
		public Builder withHeight(int height) {
			assertNotInvalidated(getClass(), rack);
			rack.height = height;
			return this;
		}
		
		/**
		 * Sets the position of the rack item by the lowest rack unit.
		 * @param position the rack unit
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPosition(int position) {
			assertNotInvalidated(getClass(), rack);
			rack.position = position;
			return this;
		}
		
		/**
		 * Sets the face to access the installed element.
		 * @param face the face to access the installed element.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withFace(Face face) {
			assertNotInvalidated(getClass(), rack);
			rack.face = face;
			return this;
		}

		/**
		 * Sets the rack item ID.
		 * @param id the rack item ID.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withRackItemId(RackItemId id) {
			assertNotInvalidated(getClass(), rack);
			rack.rackItemId = id;
			return this;
		}

		
		/**
		 * Sets the rack item name.
		 * @param name the rack item name.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withRackItemName(String name) {
			assertNotInvalidated(getClass(), rack);
			rack.rackItemName = name;
			return this;
		}
		
		
		/**
		 * Creates an immutable <code>RackItemData</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>RackItemData</code> value object.
		 */
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
	private RackItemId rackItemId = randomRackItemId();
	
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

	
	/**
	 * Returns the administrative state of the installed element.
	 * @return the administrative state of the installed element.
	 */
	public AdministrativeState getAdministrativeState() {
        return administrativeState;
    }
	
	/**
	 * Returns the element group ID of the element group the installed element is member of or
	 * <code>null</code> if no element is assigned to the rack item.
	 * @return the element group ID of the element group the installed element is member of.
	 */
	public ElementGroupId getGroupId() {
		return groupId;
	}
	
	/**
	 * Returns the element group name of the element group the installed element is member of or
	 * <code>null</code> if no element is assigned to the rack item.
	 * @return the element group name of the element group the installed element is member or.
	 */
	public ElementGroupName getGroupName() {
		return groupName;
	}
	

	/**
     * Returns the element group type of the element group the installed element is member of or
     * <code>null</code> if no element is assigned to the rack item.
     * @return the element group type of the element group the installed element is member or.
     */
	public ElementGroupType getGroupType() {
		return groupType;
	}
	
    /**
     * Returns the element ID of the installed element or
     * <code>null</code> if no element is assigned to the rack item.
     * @return the element ID of the installed element.
     */
	public ElementId getElementId() {
		return elementId;
	}

    /**
     * Returns the element name of the installed element or
     * <code>null</code> if no element is assigned to the rack item.
     * @return the element name of the installed element.
     */
	public ElementName getElementName() {
		return elementName;
	}
	
    /**
     * Returns the element role of the installed element or
     * <code>null</code> if no element is assigned to the rack item.
     * @return the element role of the installed element.
     */
	public ElementRoleName getElementRole() {
		return elementRole;
	}

	/**
	 * Returns the element alias of the installed element or
	 * <code>null</code> if no element is assigned to the rack item.
	 * @return the element alias of the installed element.
	 */
	public ElementAlias getElementAlias() {
	    return elementAlias;
	}

	/**
     * Returns the platform ID of the installed element or
     * <code>null</code> if no element is assigned to the rack item.
     * @return the platform ID of the installed element.
     */
	public PlatformId getPlatformId() {
		return platformId;
	}
	
    /**
     * Returns the platform name of the installed element or
     * <code>null</code> if no element is assigned to the rack item.
     * @return the platform name of the installed element.
     */
	public PlatformName getPlatformName() {
		return platformName;
	}
	
	/**
	 * Returns the position of the rack item by the rack unit.
	 * @return the position of the rack item by the rack unit.
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * Returns the rack item height in rack units.
	 * @return the rack item height in rack units.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the rack item name. 
	 * The rack item names allows describing an installed device that is not stored as element in the inventory.
	 * @return the rack item name.
	 */
	public String getRackItemName() {
		return rackItemName;
	}
	
	/**
	 * Returns the rack face to access the installed element.
	 * @return the rack face to access the installed element.
	 */
	public Face getFace() {
		return face;
	}
	
	/**
	 * Returns the rack item ID.
	 * @return the rack item ID.
	 */
	public RackItemId getRackItemId() {
		return rackItemId;
	}
}
