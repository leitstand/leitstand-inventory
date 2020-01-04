/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.ImageId;

public class ImageIdAdapter implements JsonbAdapter<ImageId,String> {

	@Override
	public String adaptToJson(ImageId obj) throws Exception {
		return ImageId.toString(obj);
	}

	@Override
	public ImageId adaptFromJson(String obj) throws Exception {
		return ImageId.valueOf(obj);
	}

}