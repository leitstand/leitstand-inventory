package io.leitstand.inventory.service;

import javax.validation.Valid;

import io.leitstand.commons.model.BuilderUtil;

public class RackItem extends BaseRackEnvelope {

	public static Builder newRackItem() {
		return new Builder();
	}
	
	public static class Builder extends BaseRackEnvelopeBuilder<RackItem, Builder>{
		
		protected Builder() {
			super(new RackItem());
		}
		
		public Builder withRackItem(RackItemData.Builder item) {
			return withRackItem(item.build());
		}
		
		public Builder withRackItem(RackItemData item) {
			BuilderUtil.assertNotInvalidated(getClass(), super.rack);
			this.rack.item = item;
			return this;
		}
		
	}
	
	@Valid
	private RackItemData item;
	
	public RackItemData getItem() {
		return item;
	}
}
