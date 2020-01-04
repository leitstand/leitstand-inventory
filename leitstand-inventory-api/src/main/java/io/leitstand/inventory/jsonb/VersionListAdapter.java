/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.json.bind.adapter.JsonbAdapter;

import io.leitstand.inventory.service.Version;

public class VersionListAdapter implements JsonbAdapter<List<Version>,List<String>> {
	
	@Override
	public List<Version> adaptFromJson(List<String> v) throws Exception {
		return v.stream()
				.map(Version::valueOf)
				.collect(toList());
	}

	@Override
	public List<String> adaptToJson(List<Version> versions) throws Exception {
		return versions.stream()
					   .map(v -> Version.toString(v))
					   .collect(toList());
	}

}
