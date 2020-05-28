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

import javax.persistence.Embeddable;

import io.leitstand.commons.model.ValueObject;

@Embeddable
public class VlanTag extends ValueObject{
	
	public static Builder newVlanTag() {
		return new Builder();
	}
	
	public static class Builder {
		
		private VlanTag tag = new VlanTag();
		
		public Builder withVlanId(VlanID vlanId) {
			assertNotInvalidated(getClass(), tag);
			tag.vlanId = vlanId;
			return this;
		}
		
		public Builder withVlanTpid(VlanTPID vlanTpid) {
			assertNotInvalidated(getClass(), tag);
			tag.vlanTpid = vlanTpid;
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

	private VlanTPID vlanTpid;
	private VlanID vlanId;
	
	public VlanID getVlanId() {
		return vlanId;
	}
	
	public VlanTPID getVlanTpid() {
		return vlanTpid;
	}
}
