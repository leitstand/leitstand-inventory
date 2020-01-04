/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

public class BaseElementGroupEnvelope extends ValueObject {

	public static class BaseElementGroupEnvelopeBuilder<T extends BaseElementGroupEnvelope,B extends BaseElementGroupEnvelopeBuilder<T,B>>{
		
		protected T object;
		
		protected BaseElementGroupEnvelopeBuilder(T object) {
			this.object = object;
		}
		
		/**
		 * Sets the element group id.
		 * @param id - the element group id
		 * @return a reference to this builder to continue with object creation
		 */
		public B withGroupId(ElementGroupId groupId) {
			assertNotInvalidated(getClass(), object);
			((BaseElementGroupEnvelope)object).groupId = groupId;
			return (B) this;
		}

		/**
		 * Sets the element group name.
		 * @param name - the element group name
		 * @return a reference to this builder to continue with object creation
		 */
		public B withGroupName(ElementGroupName groupName) {
			assertNotInvalidated(getClass(), object);
			((BaseElementGroupEnvelope)object).groupName = groupName;
			return (B) this;
		}
		
		
		/**
		 * Sets the element group type.
		 * @param name - the element group name
		 * @return a reference to this builder to continue with object creation
		 */
		public B withGroupType(ElementGroupType groupType) {
			assertNotInvalidated(getClass(), object);
			((BaseElementGroupEnvelope)object).groupType = groupType;
			return (B) this;
		}

		public T build() {
			try {
				assertNotInvalidated(getClass(), object);
				return object;
			} finally {
				this.object = null;
			}
		}
		
	}
	
	
	@Valid
	@NotNull(message="{group_id.required}")
	private ElementGroupId groupId = randomGroupId();
	
	@Valid
	@NotNull(message="{group_name.required}")
	private ElementGroupName groupName;
	
	@Valid
	@NotNull(message="{group_type.required}")
	private ElementGroupType groupType;
	
	public ElementGroupId getGroupId() {
		return groupId;
	}
	
	public ElementGroupName getGroupName() {
		return groupName;
	}
	
	public ElementGroupType getGroupType() {
		return groupType;
	}
}
