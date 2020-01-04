/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.VisualizationConfigName;

public class VisualizationConfigNameAdapter implements JsonbAdapter<VisualizationConfigName, String>{

	@Override
	public String adaptToJson(VisualizationConfigName configName) throws Exception {
		return VisualizationConfigName.toString(configName);
	}

	@Override
	public VisualizationConfigName adaptFromJson(String configName) throws Exception {
		return VisualizationConfigName.valueOf(configName);
	}

}
