package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.commons.model.ValueObject;

public class RackExportItem extends ValueObject {
	
	public static Builder newRackExportItem() {
		return new Builder();
	}
	
	public static class Builder {
		
		private RackExportItem item = new RackExportItem();
		
		public Builder withRackSettings(RackSettings settings) {
			BuilderUtil.assertNotInvalidated(getClass(), item);
			this.item.rackSettings = settings;
			return this;
		}
		
		public Builder withRackItems(List<RackItemData> items) {
			assertNotInvalidated(getClass(), item);
			this.item.rackItems = items;
			return this;
		}
		
		public RackExportItem build() {
			try {
				assertNotInvalidated(getClass(), item);
				return item;
			} finally {
				this.item = null;
			}
		}
		
	}

	private RackSettings rackSettings;
	private List<RackItemData> rackItems = emptyList();
	
	
	public RackSettings getRackSettings() {
		return rackSettings;
	}
	
	
	public List<RackItemData> getRackItems() {
		return unmodifiableList(rackItems);
	}
	
}
