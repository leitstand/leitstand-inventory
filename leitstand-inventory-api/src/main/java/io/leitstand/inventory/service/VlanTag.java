/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
