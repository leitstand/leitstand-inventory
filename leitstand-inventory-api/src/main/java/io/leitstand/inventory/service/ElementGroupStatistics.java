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

public class ElementGroupStatistics extends BaseElementGroupEnvelope {

	public static Builder newElementGroupStatistics() {
		return new Builder();
	}
	
	public static final class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupStatistics,Builder>{
		
		protected Builder() {
			super(new ElementGroupStatistics());
		}
		
		public Builder withCount(OperationalState opState, int count) {
			object.activeElements.put(opState, count);
			return this;
		}
		
		public Builder withCounts(Map<OperationalState,Integer> counts) {
			object.activeElements.putAll(counts);
			return this;
		}

		public boolean hasGroupId(ElementGroupId groupId) {
			return Objects.equals(object.getGroupId(), groupId);
		}

		public Builder withNewCount(int count) {
			object.newElements = count;
			return this;
		}
		public Builder withRetiredCount(int count) {
			object.retiredElements = count;
			return this;
		}
	}	
	
	
	protected ElementGroupStatistics() {
		this.activeElements = new TreeMap<>();
	}
	
	private int newElements;
	private Map<OperationalState,Integer> activeElements;
	private int retiredElements;

	public Map<OperationalState, Integer> getActiveElements() {
		return activeElements;
	}
	
	public int getNewElements() {
		return newElements;
	}
	public int getRetiredElements() {
		return retiredElements;
	}

}
