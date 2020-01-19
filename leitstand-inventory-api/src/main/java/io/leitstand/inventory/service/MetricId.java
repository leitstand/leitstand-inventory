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

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.MetricIdAdapter;

@JsonbTypeAdapter(MetricIdAdapter.class)
public class MetricId extends Scalar<String> {

	private static final long serialVersionUID = 1L;

	public static MetricId randomMetricId() {
		return valueOf(UUID.randomUUID().toString());
	}

	
	public static MetricId valueOf(String metricId){
		return fromString(metricId,MetricId::new);
	}
	
	private String value;
	
	public MetricId(String metricId) {
		this.value = metricId;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}
