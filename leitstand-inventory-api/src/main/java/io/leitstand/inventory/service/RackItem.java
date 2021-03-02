package io.leitstand.inventory.service;

import javax.validation.Valid;

import io.leitstand.commons.model.BuilderUtil;

/**
 * A rack item of a rack.
 */
public class RackItem extends BaseRackEnvelope {

    /**
     * Creates a builder for an immutable <code>RackItem</code> value object.
     * @return a builder for an immutable <code>RackItem</code> value object.
     */
	public static Builder newRackItem() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>RackItem</code> value object.
	 */
	public static class Builder extends BaseRackEnvelopeBuilder<RackItem, Builder>{
		
	    /**
	     * Creates a builder for an immutable <code>RackItem</code> value object.
	     */
		protected Builder() {
			super(new RackItem());
		}
		
		/**
		 * Sets the rack item settings.
		 * @param item the rack item settings.
		 * @return a builder to this reference to continue object creation.
		 */
		public Builder withRackItem(RackItemData.Builder item) {
			return withRackItem(item.build());
		}
		

        /**
         * Sets the rack item settings.
         * @param item the rack item settings.
         * @return a builder to this reference to continue object creation.
         */
		public Builder withRackItem(RackItemData item) {
			BuilderUtil.assertNotInvalidated(getClass(), rack);
			this.rack.item = item;
			return this;
		}
		
	}
	
	@Valid
	private RackItemData item;
	
	/**
	 * Returns the rack item settings.
	 * @return the rack item settings.
	 */
	public RackItemData getItem() {
		return item;
	}
}
