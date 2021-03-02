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
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

/**
 * The software images available for an element role.
 */
public class RoleImages extends ValueObject{

    /**
     * Creates a builder for an immutable <code>RoleImages</code> value object.
     * @return a builder for an immutable <code>RoleImages</code> value object.
     */
	public static Builder newRoleImages() {
		return new Builder();
	}
	
	/**
     * A builder for an immutable <code>RoleImages</code> value object.
     */
	public static class Builder {
		
		private RoleImages instance = new RoleImages();
		
		/**
		 * Sets the element role name.
		 * @param elementRole the element role name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementRole(ElementRoleName elementRole) {
			assertNotInvalidated(getClass(), instance);
			instance.elementRole = elementRole;
			return this;
		}
		
		/**
		 * Sets the images available for the element role.
		 * @param images the available images.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImages(List<RoleImage> images) {
			assertNotInvalidated(getClass(), instance);
			instance.images = new LinkedList<>(images);
			return this;
		}
		
		/**
		 * Creates an immutable <code>RolesImages</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>RolesImages</code> value object.
		 */
		public RoleImages build() {
			try {
				assertNotInvalidated(getClass(), instance);
				return instance;
			} finally {
				this.instance = null;
			}
		}
	}
	
	private ElementRoleName elementRole;
	private List<RoleImage> images;
	
	/**
	 * Returns the element role name.
	 * @return the element role name.
	 */
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	/**
	 * Returns the images available for the element role.
	 * @return the available images.
	 */
	public List<RoleImage> getImages() {
		return unmodifiableList(images);
	}
	
}
