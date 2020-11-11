/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySortedSet;
import static java.util.Collections.unmodifiableSortedSet;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.leitstand.commons.model.ValueObject;


public class ElementGroupSettings extends ValueObject{
	
	public static Builder newElementGroupSettings(){
		return new Builder();
	}
	
	public static class Builder{
		
		private ElementGroupSettings settings = new ElementGroupSettings();
		
		
		public Builder withGroupId(ElementGroupId id){
			assertNotInvalidated(getClass(), settings);
			settings.groupId = id;
			return this;
		}
		
		public Builder withGroupName(ElementGroupName name){
			assertNotInvalidated(getClass(), settings);
			settings.groupName = name;
			return this;
		}

		public Builder withGroupType(ElementGroupType type){
			assertNotInvalidated(getClass(), settings);
			settings.groupType = type;
			return this;
		}
		
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), settings);
			settings.description = description;
			return this;
		}
		
		public Builder withFacilityId(FacilityId facilityId){
			assertNotInvalidated(getClass(), settings);
			settings.facilityId = facilityId;
			return this;
		}
		
		public Builder withFacilityName(FacilityName facilityName) {
			assertNotInvalidated(getClass(), settings);
			settings.facilityName = facilityName;
			return this;
		}
		
	    public Builder withFacilityType(FacilityType facilityType) {
	        assertNotInvalidated(getClass(), settings);
	        settings.facilityType = facilityType;
	        return this;
	    }
		
		public Builder withTags(String... tags){
			return withTags(new TreeSet<>(asList(tags)));
		}
		
		public Builder withTags(Set<String> tags){
			assertNotInvalidated(getClass(), settings);
			settings.tags = new TreeSet<>(tags);
			return this;
		}
		
		public ElementGroupSettings build(){
			try{
				assertNotInvalidated(getClass(), settings);
				return settings;
			} finally {
				this.settings = null;
			}
		}
	}
	
	@Valid
	@NotNull(message="{group_id.required}")
	private ElementGroupId groupId = randomGroupId();

	@Valid
	@NotNull(message="{group_name.required}")
	private ElementGroupName groupName;

	@Valid
	@NotNull(message="{group_type.required}")
	private ElementGroupType groupType;
	
	@Size(max=1024, message="{description.invalid}")
	private String description;
	
	@Size(max=512, message="{location.invalid}")
	private String location;
	
	@Valid
	private FacilityId facilityId;
	
	@Valid
	private FacilityName facilityName;
	
	@Valid
	private FacilityType facilityType;
	
	private SortedSet<String> tags = emptySortedSet();

	public ElementGroupId getGroupId() {
		return groupId;
	}
	
	public ElementGroupName getGroupName() {
		return groupName;
	}
	
	public ElementGroupType getGroupType() {
		return groupType;
	}
	
	public String getDescription() {
		return description;
	}
	
	public FacilityId getFacilityId() {
		return facilityId;
	}
	
	public FacilityName getFacilityName() {
		return facilityName;
	}
	
	public FacilityType getFacilityType() {
        return facilityType;
    }
	
	public SortedSet<String> getTags() {
		return unmodifiableSortedSet(tags);
	}

}
