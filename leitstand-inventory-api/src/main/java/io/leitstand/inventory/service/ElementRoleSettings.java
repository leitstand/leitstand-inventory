/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementRoleId.randomElementRoleId;
import static io.leitstand.inventory.service.Plane.DATA;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

public class ElementRoleSettings extends ValueObject {

	public static Builder newElementRoleSettings(){
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementRoleSettings settings = new ElementRoleSettings();
		

		public Builder withRoleId(ElementRoleId roleId) {
			assertNotInvalidated(getClass(),settings);
			settings.roleId = roleId;
			return this;
		}
		
		public Builder withRoleName(ElementRoleName name){
			assertNotInvalidated(getClass(), settings);
			settings.roleName = name;
			return this;
		}

		public Builder withDisplayName(String displayName){
			assertNotInvalidated(getClass(), settings);
			settings.displayName = displayName;
			return this;
		}
		

		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), settings);
			settings.description= description;
			return this;
		}
		

		public Builder withPlane(Plane plane){
			assertNotInvalidated(getClass(), settings);
			settings.plane = plane;
			return this;
		}
		
		public Builder withManageable(boolean manageable) {
			assertNotInvalidated(getClass(), settings);
			settings.manageable = manageable;
			return this;
		}

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
	
	public ElementRoleId getRoleId() {
		return roleId;
	}
	
	public ElementRoleName getRoleName() {
		return roleName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Plane getPlane() {
		return plane;
	}
	
	public boolean isManageable() {
		return manageable;
	}
	
}
