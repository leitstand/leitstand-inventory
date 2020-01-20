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
