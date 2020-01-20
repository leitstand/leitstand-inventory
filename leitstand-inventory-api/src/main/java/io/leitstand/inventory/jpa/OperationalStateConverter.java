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
package io.leitstand.inventory.jpa;

import static io.leitstand.inventory.service.OperationalState.UNKNOWN;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.OperationalState;

@Converter
public class OperationalStateConverter implements AttributeConverter<OperationalState, String> {

	@Override
	public String convertToDatabaseColumn(OperationalState state) {
		return operationalStateDbString(state);
	}

	@Override
	public OperationalState convertToEntityAttribute(String s) {
		return toOperationalState(s);
	}

	public static String operationalStateDbString(OperationalState state) {
		if(state == null){
			return UNKNOWN.getValue();
		}
		return OperationalState.toString(state);
	}
	
	public static OperationalState toOperationalState(String s) {
		OperationalState state = OperationalState.valueOf(s);
		if(state != null) {
			return state;
		}
		return UNKNOWN;
	}

}
