/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.InterfaceName;

public abstract class ElementPhysicalInterfaceEvent extends ElementInterfaceEvent {
	
	public static class ElementPhysicalInterfaceEventBuilder<E extends ElementPhysicalInterfaceEvent, B extends ElementPhysicalInterfaceEventBuilder<E,B>> extends ElementInterfaceEventBuilder<E,B>{
	
		ElementPhysicalInterfaceEventBuilder(E event){
			super(event);
		}
		
		public B withInterfaceName(InterfaceName ifpName) {
			BuilderUtil.assertNotInvalidated(getClass(),object);
			((ElementPhysicalInterfaceEvent)object).interfaceName = ifpName;
			return (B) this;
		}
		
		public B withNeighbor(ElementPhysicalInterfaceNeighbor neighbor) {
			BuilderUtil.assertNotInvalidated(getClass(),object);
			((ElementPhysicalInterfaceEvent)object).neighbor = neighbor;
			return (B) this;
		}
	}
	
	
	@JsonbProperty("ifp_name")
	private InterfaceName interfaceName;

	private ElementPhysicalInterfaceNeighbor neighbor;
	
	public InterfaceName getInterfaceName() {
		return interfaceName;
	}
	
	public ElementPhysicalInterfaceNeighbor getNeighbor() {
		return neighbor;
	}
	
	
}
