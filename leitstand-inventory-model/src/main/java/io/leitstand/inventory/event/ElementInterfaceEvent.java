/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.OperationalState;

public abstract class ElementInterfaceEvent extends ElementEvent{

	public static class ElementInterfaceEventBuilder<E extends ElementInterfaceEvent, B extends ElementInterfaceEventBuilder<E,B>> extends ElementEventBuilder<E,B>{
		ElementInterfaceEventBuilder(E event){
			super(event);
		}
		
		public B withOperationalState(OperationalState opState) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			((ElementInterfaceEvent)object).operationalState = opState;
			return (B) this;
		}
		
		public B withAdministrativeState(AdministrativeState admState) {
			BuilderUtil.assertNotInvalidated(getClass(), object);
			((ElementInterfaceEvent)object).administrativeState = admState;
			return (B) this;
		}
	}
	
	@JsonbProperty("op_state")
	private OperationalState operationalState;
	@JsonbProperty("adm_state")
	private AdministrativeState administrativeState;
	
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
}
