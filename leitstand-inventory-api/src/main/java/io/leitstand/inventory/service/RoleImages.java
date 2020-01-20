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

import io.leitstand.commons.model.CompositeValue;

public class RoleImages extends CompositeValue{

	public static Builder newRoleImages() {
		return new Builder();
	}
	
	public static class Builder {
		
		private RoleImages instance = new RoleImages();
		
		public Builder withElementRole(ElementRoleName elementRole) {
			assertNotInvalidated(getClass(), instance);
			instance.elementRole = elementRole;
			return this;
		}
		
		public Builder withImages(List<RoleImage> images) {
			assertNotInvalidated(getClass(), instance);
			instance.images = new LinkedList<>(images);
			return this;
		}
		
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
	
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	public List<RoleImage> getImages() {
		return unmodifiableList(images);
	}
	
}
