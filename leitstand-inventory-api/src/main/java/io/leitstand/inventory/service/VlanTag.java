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

import io.leitstand.commons.model.ValueObject;

public class VlanTag extends ValueObject{
	
	public static Builder newVlanTag() {
		return new Builder();
	}
	
	public static class Builder {
		
		private VlanTag tag = new VlanTag();
		
		public Builder withTagType(Type type) {
			assertNotInvalidated(getClass(), tag);
			tag.type = type;
			return this;
		}
		
		public Builder withVlanId(VlanId vlanId) {
			assertNotInvalidated(getClass(), tag);
			tag.vlanId = vlanId;
			return this;
		}
		
		public Builder withVlanTpId(VlanTpId vlanTpId) {
			assertNotInvalidated(getClass(), tag);
			tag.vlanTpId = vlanTpId;
			return this;
		}
		
		public VlanTag build() {
			try {
				assertNotInvalidated(getClass(), tag);
				return tag;
			} finally {
				this.tag = null;
			}
		}
	}

	public static enum Type {
		CTAG,
		STAG;
	}
	
	
	private Type type;
	private VlanTpId vlanTpId;
	private VlanId vlanId;
	
	
	public Type getType() {
		return type;
	}
	
	public VlanId getVlanId() {
		return vlanId;
	}
	
	public VlanTpId getVlanTpId() {
		return vlanTpId;
	}
}
