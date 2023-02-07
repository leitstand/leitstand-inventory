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

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Counts group elements grouped by their administrative and operational state. 
 */
public class ElementGroupStatistics extends BaseElementGroupEnvelope {

    /**
     * Creates a builder for an immutable <code>ElementGroupStatistics</code> value object.
     * @return a builder for an immutable <code>ElementGroupStatistics</code> value object.
     */
	public static Builder newElementGroupStatistics() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementGroupStatistics</code> value object.
	 * @author mast
	 *
	 */
	public static final class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupStatistics,Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>ElementGroupStatistics</code> value object.
	     */
		protected Builder() {
			super(new ElementGroupStatistics());
		}
		
		/**
		 * Sets the element count.
		 * @param opState the operational state.
		 * @param count the element count.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withCount(OperationalState opState, int count) {
			object.activeElements.put(opState.name(), count);
			return this;
		}
		
		/**
		 * Sets the element count.
		 * @param counts element counts by operational state.
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withCounts(Map<String,Integer> counts) {
			object.activeElements.putAll(counts);
			return this;
		}

		/**
		 * Tests whether this builder counts elements for the specified element group.
		 * @param groupId the group ID
		 * @return <code>true</code> if this builder counts elements for the specified group, <code>false</code> otherwise.
		 */
		public boolean hasGroupId(ElementGroupId groupId) {
			return Objects.equals(object.getGroupId(), groupId);
		}

		/**
		 * Sets the number of new elements in the element group.
		 * @param count the number of new elements.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withNewCount(int count) {
			object.newElements = count;
			return this;
		}
		
		/**
		 * Sets the number of retired elements in the element group.
		 * @param count the number of retired elements.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRetiredCount(int count) {
			object.retiredElements = count;
			return this;
		}
	}	
	
	/**
	 * Creates an <code>ElementGroupStatistics</code> value object.
	 */
	protected ElementGroupStatistics() {
		this.activeElements = new TreeMap<>();
	}
	
	private int newElements;
	private Map<String,Integer> activeElements;
	private int retiredElements;

	/**
	 * Returns the number of active elements in the element group grouped by their operational state.
	 * @return the number of active elements in the element group grouped by their operational state.
	 */
	public Map<String, Integer> getActiveElements() {
		return activeElements;
	}
	
	/**
	 * Returns the number of new elements in the element group.
	 * @return the number of new elements in the element group.
	 */
	public int getNewElements() {
		return newElements;
	}
	
	/**
	 * Returns the number of retired elements in the element group.
	 * @return the number of retired elements in the element group.s
	 */
	public int getRetiredElements() {
		return retiredElements;
	}

}
