package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import io.leitstand.commons.model.BuilderUtil;
import io.leitstand.commons.model.ValueObject;

/**
 * An exported rack shipped with a rack export.
 * @see RackExport
 */
public class RackExportItem extends ValueObject {
	
    /**
     * Creates a builder for an immutable <code>RackExportItem</code> value object.
     * @return a builder for an immutable <code>RackExportItem</code> value object.
     */
	public static Builder newRackExportItem() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>RackExportItem</code> value object.
	 */
	public static class Builder {
		
		private RackExportItem item = new RackExportItem();

		/**
		 * Sets the general settings of the exported rack.
		 * @param settings the settings of the exported rack.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRackSettings(RackSettings settings) {
			BuilderUtil.assertNotInvalidated(getClass(), item);
			this.item.rackSettings = settings;
			return this;
		}
		
		/**
		 * Sets the rack items.
		 * @param items the rack items of the exported rack.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRackItems(List<RackItemData> items) {
			assertNotInvalidated(getClass(), item);
			this.item.rackItems = items;
			return this;
		}
		
		/**
		 * Creates an immutable <code>RackExportItem</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable  <code>RackExportItem</code> value object.
		 */
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
	
	/**
	 * Returns the general settings of the exported rack.
	 * @return the general settings of the exported rack.
	 */
	public RackSettings getRackSettings() {
		return rackSettings;
	}
	
	/**
	 * Returns the rack items of the exported rack.
	 * @return the rack items of the exported rack.
	 */
	public List<RackItemData> getRackItems() {
		return unmodifiableList(rackItems);
	}
	
}
