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
import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

 /**
  * Collection of all rack items of a rack.
  */
public class RackItems extends BaseRackEnvelope {

    /**
     * Creates a builder for an immutable <code>RackItems</code> value object.
     * @return a builder for an immutable <code>RackItems</code> value object.
     */
    public static Builder newRackItems() {
		return new Builder();
	}
	
    /**
     * A builder for an immutable <code>RackItems</code> value object.
     */
	public static class Builder extends BaseRackEnvelopeBuilder<RackItems, Builder>{
		 
	    /**
	     * Creates a builder for an immutable <code>RackItems</code>.
	     */
	    public Builder() {
			super(new RackItems());
		}
		
	    /**
	     * Sets the rack items.
	     * @param items the rack items.
	     * @return a reference to this builder to continue object creation.
	     */
		public Builder withRackItems(List<RackItemData> items) {
			assertNotInvalidated(getClass(), rack);
			rack.items = new LinkedList<>(items);
			return this;
		}
	}
	
	private List<RackItemData> items;
	
	/**
	 * Returns the rack items.
	 * @return the rack items.
	 */
	public List<RackItemData> getItems() {
		return unmodifiableList(items);
	}
}
