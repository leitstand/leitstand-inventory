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
 * A element role reference.
 * <p>
 * The <code>ElementRoleRef</code> consists of the the role name and the plane the role belongs to.
 * @see Plane
 */
public class ElementRoleRef extends ValueObject {

	/**
	 * Creates a builder for an immutable <code>ElementRoleRef</code> value object. 
	 * @return a builder for an immutable <code>ElementRoleRef</code> value object.
	 */
	public static Builder newElementRoleRef() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementRoleRef</code> value object.
	 */
	public static class Builder {
		
		private ElementRoleRef info = new ElementRoleRef();
		
		/**
		 * Sets the element role name. 
		 * @param roleName the element role name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementRole(ElementRoleName roleName) {
			info.elementRole = roleName;
			return this;
		}

		/**
		 * Sets the plane elements of the specified roles belongs to.
		 * @param plane the plane.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPlane(Plane plane) {
			info.plane = plane;
			return this;
		}
		
		/**
		 * Returns an immutable <code>ElementRoleRef</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raises an exception.
		 * @return the immutable <code>ElementRoleRef</code> value object.
		 */
		public ElementRoleRef build() {
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
	 * Returns the plane elements of this role belong to.
     * @return the plane elements of this role belong to.
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