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

import io.leitstand.commons.model.ValueObject;

/**
 * Provides information of a certain element role. 
 */
public class ElementRoleInfo extends ValueObject {

	/**
	 * Returns a builder to create an immutable <code>ElementRoleInfo</code> instance. 
	 * @return a builder to create an immutable <code>ElementRoleInfo</code> instance.
	 */
	public static Builder newElementRoleInfo() {
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementRoleInfo</code> instance.
	 */
	public static class Builder {
		
		private ElementRoleInfo info = new ElementRoleInfo();
		
		/**
		 * Sets the element role name. 
		 * @param role - the element role.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withElementRole(ElementRoleName role) {
			info.elementRole = role;
			return this;
		}

		/**
		 * Sets the plane elements of the specified roles are located at.
		 * @param plane - the plane
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPlane(Plane plane) {
			info.plane = plane;
			return this;
		}
		
		/**
		 * Returns an immutable <code>ElementRoleInfo</code> instance and invalidates this builder.
		 * Any further interaction with this builder raises an exception.
		 * @return an immutable <code>ElementRoleInfo</code> instance.
		 */
		public ElementRoleInfo build() {
			try {
				return info;
			} finally {
				this.info = null;
			}
		}
		
	}
	
	private Plane plane;
	
	private ElementRoleName elementRole;

	/**
	 * Returns the plane where the element role is located at.
	 * @return the plane where the element role is located at.
	 */
	public Plane getPlane() {
		return plane;
	}
	
	/**
	 * Returns the element role name.
	 * @return the element role name.
	 */
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
}
