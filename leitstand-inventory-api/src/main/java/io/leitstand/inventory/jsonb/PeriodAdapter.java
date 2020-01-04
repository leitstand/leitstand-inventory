/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.Period;

public class PeriodAdapter implements JsonbAdapter<Period,String> {

	@Override
	public Period adaptFromJson(String v) {
		return Period.valueOf(v);
	}

	@Override
	public String adaptToJson(Period v) {
		return Period.toString(v);
	}

}
