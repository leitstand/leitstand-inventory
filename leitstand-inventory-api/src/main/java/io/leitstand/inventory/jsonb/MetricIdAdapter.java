/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.MetricId;

public class MetricIdAdapter implements JsonbAdapter<MetricId,String> {

	@Override
	public MetricId adaptFromJson(String v) throws Exception {
		return MetricId.valueOf(v);
	}

	@Override
	public String adaptToJson(MetricId v) throws Exception {
		return MetricId.toString(v);
	}

}
