/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
