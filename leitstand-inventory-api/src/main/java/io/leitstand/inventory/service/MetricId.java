/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
