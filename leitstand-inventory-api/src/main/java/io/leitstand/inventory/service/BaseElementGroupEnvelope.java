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
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.ValueObject;

/**
 * The <code>BaseElementGroupEnvelope</code> contains the attributes shared with every group-related message.
 */
public class BaseElementGroupEnvelope extends ValueObject {

    /**
     * A base builder for a element group value object.
     *
     * @param <T> the element group value object type
     * @param <B> the element group value object builder
     */
	@SuppressWarnings("unchecked")
	public static class BaseElementGroupEnvelopeBuilder<T extends BaseElementGroupEnvelope,B extends BaseElementGroupEnvelopeBuilder<T,B>>{
		
		protected T object;
		
		/**
		 * Creates an element group builder
		 * @param object the value object under construction
		 */
		protected BaseElementGroupEnvelopeBuilder(T object) {
			this.object = object;
		}
		
		/**
		 * Sets the element group id.
		 * @param groupId the element group id
		 * @return a reference to this builder to continue with object creation
		 */
		public B withGroupId(ElementGroupId groupId) {
			assertNotInvalidated(getClass(), object);
			((BaseElementGroupEnvelope)object).groupId = groupId;
			return (B) this;
		}

		/**
		 * Sets the element group name.
		 * @param groupName the element group name
		 * @return a reference to this builder to continue with object creation
		 */
		public B withGroupName(ElementGroupName groupName) {
			assertNotInvalidated(getClass(), object);
			((BaseElementGroupEnvelope)object).groupName = groupName;
			return (B) this;
		}
		
		/**
		 * Sets the element group type.
		 * @param groupType the element group type
		 * @return a reference to this builder to continue with object creation
		 */
		public B withGroupType(ElementGroupType groupType) {
			assertNotInvalidated(getClass(), object);
			((BaseElementGroupEnvelope)object).groupType = groupType;
			return (B) this;
		}
		
		/**
		 * Copies all values of the given group envelope to this envelope.
		 * This method is a convenience method to simplify value object creation.
		 * @return a reference to this builder to continue object creation
		 */
		public B withGroup(BaseElementGroupEnvelope env) {
		    assertNotInvalidated(getClass(),object);
		    withGroupId(env.getGroupId());
		    withGroupName(env.getGroupName());
		    withGroupType(env.getGroupType());
		    return (B) this;
		}

		/**
		 * Creates the immutable value object instance and invalidates the builder. Subsequent calls of the <code>build()</code> method raise an exception.
		 * @return the immutable value object instance.
		 */
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
	
	/**
	 * Returns the element group ID
	 * @return the element group ID
	 */
	public ElementGroupId getGroupId() {
		return groupId;
	}
	
	/**
	 * Returns the element group name.
	 * @return the element group name.
	 */
	public ElementGroupName getGroupName() {
		return groupName;
	}
	
	/**
	 * Returns the element group type.
	 * @return the element group type.
	 */
	public ElementGroupType getGroupType() {
		return groupType;
	}
}
