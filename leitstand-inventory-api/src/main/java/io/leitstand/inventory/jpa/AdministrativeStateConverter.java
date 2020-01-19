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

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.inventory.service.AdministrativeState.UNKNOWN;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.AdministrativeState;

@Converter
public class AdministrativeStateConverter implements AttributeConverter<AdministrativeState, String> {

	@Override
	public String convertToDatabaseColumn(AdministrativeState state) {
		if(state == null) {
			return "UNKNOWN";
		}
		return administrativeStateDbString(state);
	}

	@Override
	public AdministrativeState convertToEntityAttribute(String s) {
		return toAdministrativeState(s);
	}

	public static String administrativeStateDbString(AdministrativeState state) {
		return AdministrativeState.toString(state);
	}
	
	public static AdministrativeState toAdministrativeState(String s) {
		if(isEmptyString(s)){
			return UNKNOWN;
		}
		return AdministrativeState.valueOf(s);
	}

}
