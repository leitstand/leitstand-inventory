/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupSettings.newElementGroupSettings;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static java.lang.String.format;

import java.util.concurrent.atomic.AtomicInteger;

import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupSettings;

public class ElementGroupSettingsMother {
	
	private static AtomicInteger SEQUENCE = new AtomicInteger(0);
	
	public static ElementGroupSettings newTestElementGroupWithoutElementGroupId() {
		return newElementGroupSettings()
				   .withGroupName(unique_group_name())
				   .withGroupType(groupType("junit"))
				   .build();	
	}

	private static ElementGroupName unique_group_name() {
		return new ElementGroupName(format("UNIT-POD-%d",SEQUENCE.incrementAndGet()));
	}
	
	public static ElementGroupSettings newTestElementGroup(){
		return newElementGroupSettings()
			   .withGroupId(randomGroupId())
			   .withGroupName(unique_group_name())
			   .withGroupType(groupType("junit"))
			   .build();
	}
	
	public static ElementGroupSettings.Builder updateSettings(ElementGroupSettings group){
		return newElementGroupSettings()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withLocation(group.getLocation())
			   .withDescription(group.getDescription());
	}
	
}
