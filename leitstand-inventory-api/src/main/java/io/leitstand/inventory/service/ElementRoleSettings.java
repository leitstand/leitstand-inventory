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
import static io.leitstand.inventory.service.ElementRoleId.randomElementRoleId;
import static io.leitstand.inventory.service.Plane.DATA;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * The general element role settings.
 */
public class ElementRoleSettings extends ValueObject {

    /**
     * Creates a builder for an immutable <code>ElementRoleSettings</code> value object.
     * @return a builder for an immutable <code>ElementRoleSettings</code> value object.
     */
	public static Builder newElementRoleSettings(){
		return new Builder();
	}
	
	/**
     * A builder for an immutable <code>ElementRoleSettings</code> value object.
	 */
	public static class Builder {
		
		private ElementRoleSettings settings = new ElementRoleSettings();
		
		/**
		 * Sets the element role ID.
		 * @param roleId the role ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRoleId(ElementRoleId roleId) {
			assertNotInvalidated(getClass(),settings);
			settings.roleId = roleId;
			return this;
		}

	    /**
         * Sets the element role name.
         * @param roleName the role name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withRoleName(ElementRoleName roleName){
			assertNotInvalidated(getClass(), settings);
			settings.roleName = roleName;
			return this;
		}

	    /**
         * Sets the element role display name.
         * @param displayName the display name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withDisplayName(String displayName){
			assertNotInvalidated(getClass(), settings);
			settings.displayName = displayName;
			return this;
		}
		

		/**
		 * Sets the element role description.
		 * @param description the role description.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), settings);
			settings.description= description;
			return this;
		}
		

		/**
		 * Sets the plane elements of this role belongs to.
		 * @param plane the plane.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPlane(Plane plane){
			assertNotInvalidated(getClass(), settings);
			settings.plane = plane;
			return this;
		}
		
		/**
		 * Sets whether elements of this role are managed elements or just documented elements.
		 * @param manageable whether elements of this role are managed elements (<code>true</code>) or documented elements (<code>false</code>).
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withManageable(boolean manageable) {
			assertNotInvalidated(getClass(), settings);
			settings.manageable = manageable;
			return this;
		}

		/**
		 * Creates an immutable <code>ElementRoleSettings</code> value object and invalidates this builder.
		 * Subsequence invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ElementRoleSettings</code> value object.
		 */
		public ElementRoleSettings build(){
			try{
				assertNotInvalidated(getClass(), settings);
				return settings;
			} finally {
				this.settings = null;
			}
		}

	}
	
	@Valid
	@NotNull(message="{role_id.required}")
	private ElementRoleId roleId = randomElementRoleId();

	@Valid
	@NotNull(message="{role_name.required}")
	private ElementRoleName roleName;
	
	private String displayName;
	
	private String description;
	
	@Valid
	@NotNull(message="{plane.required}")
	private Plane plane = DATA;
	
	private boolean manageable;
	
	/**
	 * Returns the element role ID.
	 * @return the element role ID.
	 */
	public ElementRoleId getRoleId() {
		return roleId;
	}
	
	/**
	 * Returns the element role name.
	 * @return the element role name.
	 */
	public ElementRoleName getRoleName() {
		return roleName;
	}
	
	/**
	 * Returns the element role display name.
	 * @return the element role display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Returns the element role description.
 	 * @return the element role description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the plane elements of this role belongs to.
	 * @return the plane elements of this role belongs to.
	 */
	public Plane getPlane() {
		return plane;
	}
	
	/**
	 * Returns whether elements of this role are managed (<code>true</code>) or documented (<code>false</code>) elements.
	 * @return whether elements of this role are managed (<code>true</code>) or documented (<code>false</code>) elements.
	 */
	public boolean isManageable() {
		return manageable;
	}
	
}
