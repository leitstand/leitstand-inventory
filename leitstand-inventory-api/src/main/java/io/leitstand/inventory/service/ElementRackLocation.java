/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.ValueObject;

public class ElementRackLocation extends ValueObject{

	public static Builder newElementRackLocation() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementRackLocation instance = new ElementRackLocation();
		
		public Builder withRackName(RackName rackName) {
			assertNotInvalidated(getClass(), instance);
			instance.rackName = rackName;
			return this;
		}
		
		public Builder withUnit(int unit) {
			assertNotInvalidated(getClass(), instance);
			instance.unit = unit;
			return this;
		}
		
		public Builder withPosition(ElementRackLocationPosition position) {
			assertNotInvalidated(getClass(), instance);
			instance.position = position;
			return this;
		}
		
		public ElementRackLocation build() {
			try {
				assertNotInvalidated(getClass(), instance);
				return instance;
			} finally {
				this.instance = null;
			}
		}
		
	}
	
	public enum ElementRackLocationPosition {
		LEFT,
		RIGHT;
	}
	
	private RackName rackName;
	private int unit;
	private ElementRackLocationPosition position;
	
	public RackName getRackName() {
		return rackName;
	}
	
	public int getUnit() {
		return unit;
	}
	
	public ElementRackLocationPosition getPosition() {
		return position;
	}
	
}
