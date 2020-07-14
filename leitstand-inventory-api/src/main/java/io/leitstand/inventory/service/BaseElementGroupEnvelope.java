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

public class BaseElementGroupEnvelope extends ValueObject {

	@SuppressWarnings("unchecked")
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
