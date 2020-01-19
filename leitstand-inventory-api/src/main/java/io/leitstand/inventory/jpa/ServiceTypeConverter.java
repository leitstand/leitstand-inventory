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

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import io.leitstand.inventory.service.ServiceType;

@Converter
public class ServiceTypeConverter implements AttributeConverter<ServiceType, String> {

	@Override
	public String convertToDatabaseColumn(ServiceType type) {
		if(type == null) {
			return null;
		}
		return type.name().substring(0, 1);
	}

	@Override
	public ServiceType convertToEntityAttribute(String s) {
		return parse(s);
	}

	public static ServiceType parse(String s) {
		if(s == null || s.isEmpty()) {
			return null;
		}
		switch (s) {
			case "S":
				return ServiceType.SERVICE;
			case "V":
				return ServiceType.VM;
			case "O":
				return ServiceType.OS;
			case "C":
				return ServiceType.CONTAINER;
			case "D":
				return ServiceType.DAEMON;
			default:
				return null;
		}
	}

}
