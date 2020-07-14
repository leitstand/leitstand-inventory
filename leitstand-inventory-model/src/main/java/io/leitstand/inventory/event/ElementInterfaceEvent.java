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
	
	@JsonbProperty("opstate")
	private OperationalState operationalState;
	@JsonbProperty("admstate")
	private AdministrativeState administrativeState;
	
	public OperationalState getOperationalState() {
		return operationalState;
	}
	
	public AdministrativeState getAdministrativeState() {
		return administrativeState;
	}
	
}
