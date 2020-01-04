/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.MetricName;

public class MetricNameAdapter implements JsonbAdapter<MetricName,String> {

	@Override
	public MetricName adaptFromJson(String v) throws Exception {
		return MetricName.valueOf(v);
	}

	@Override
	public String adaptToJson(MetricName v) throws Exception {
		return MetricName.toString(v);
	}

}
