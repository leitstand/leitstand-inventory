/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
