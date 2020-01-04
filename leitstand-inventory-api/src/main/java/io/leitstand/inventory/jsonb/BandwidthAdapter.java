/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.Bandwidth;

public class BandwidthAdapter implements JsonbAdapter<Bandwidth,String> {
	
	@Override
	public String adaptToJson(Bandwidth v) throws Exception {
		if(v == null){
			return null;
		}
		return v.toString();		
	}

	@Override
	public Bandwidth adaptFromJson(String v) throws Exception {
		if(v == null || v.isEmpty()){
			return null;
		}
		return new Bandwidth(v);	
	}

}
