/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ImageName;

public class ImageNameAdapter implements JsonbAdapter<ImageName,String> {

	@Override
	public String adaptToJson(ImageName obj) throws Exception {
		return ImageName.toString(obj);
	}

	@Override
	public ImageName adaptFromJson(String obj) throws Exception {
		return ImageName.valueOf(obj);
	}

}