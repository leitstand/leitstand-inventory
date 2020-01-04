/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.VisualizationConfigId;

public class VisualizationConfigIdAdapter implements JsonbAdapter<VisualizationConfigId, String>{

	@Override
	public String adaptToJson(VisualizationConfigId configId) throws Exception {
		return VisualizationConfigId.toString(configId);
	}

	@Override
	public VisualizationConfigId adaptFromJson(String configId) throws Exception {
		return VisualizationConfigId.valueOf(configId);
	}

}
