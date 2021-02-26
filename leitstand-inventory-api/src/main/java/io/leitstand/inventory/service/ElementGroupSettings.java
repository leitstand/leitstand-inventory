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
import static java.util.Arrays.asList;
import static java.util.Collections.emptySortedSet;
import static java.util.Collections.unmodifiableSortedSet;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * General settings of an element group.
 */
public class ElementGroupSettings extends BaseElementGroupEnvelope{
	
    /**
     * Returns a group settings builder.
     * @return a group settings builder.
     */
	public static Builder newElementGroupSettings(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementGroupSettings</code> instance.
	 */
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupSettings, Builder>{
		
	    /**
	     * Creates a new builder.
	     */
		protected Builder() {
		    super(new ElementGroupSettings());
		}
		
		/**
		 * Sets the group description 
 		 * @param description the group description
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withDescription(String description){
			assertNotInvalidated(getClass(), object);
			object.description = description;
			return this;
		}
		
	    /**
         * Sets the facility ID 
         * @param facilityId the facility ID
         * @return a reference to this builder to continue object creation
         */
		public Builder withFacilityId(FacilityId facilityId){
			assertNotInvalidated(getClass(), object);
			object.facilityId = facilityId;
			return this;
		}
		
	    /**
         * Sets the facility name
         * @param facilityName the facility name
         * @return a reference to this builder to continue object creation
         */
		public Builder withFacilityName(FacilityName facilityName) {
			assertNotInvalidated(getClass(), object);
			object.facilityName = facilityName;
			return this;
		}
		
	    /**
         * Sets the facility type
         * @param facilityType the facility type
         * @return a reference to this builder to continue object creation
         */
	    public Builder withFacilityType(FacilityType facilityType) {
	        assertNotInvalidated(getClass(), object);
	        object.facilityType = facilityType;
	        return this;
	    }
		
	    /**
         * Sets the group tags
         * @param tags the group tags
         * @return a reference to this builder to continue object creation
         */
		public Builder withTags(String... tags){
			return withTags(new TreeSet<>(asList(tags)));
		}

	    /**
         * Sets the group tags
         * @param tags the group tags
         * @return a reference to this builder to continue object creation
         */
		public Builder withTags(Set<String> tags){
			assertNotInvalidated(getClass(), object);
			object.tags = new TreeSet<>(tags);
			return this;
		}
		
	}
	
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

	/**
	 * Returns the group description.
	 * @return the group description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the facility ID.
	 * @return the facility ID.
	 */
	public FacilityId getFacilityId() {
		return facilityId;
	}
	
	/**
	 * Returns the facility name.
	 * @return the facility name.
	 */
	public FacilityName getFacilityName() {
		return facilityName;
	}
	
	/**
	 * Returns the facility type.
	 * @return the facility type.
	 */
	public FacilityType getFacilityType() {
        return facilityType;
    }
	
	/**
	 * Returns group tags.
	 * @return the group tags.
	 */
	public SortedSet<String> getTags() {
		return unmodifiableSortedSet(tags);
	}

}
