/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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