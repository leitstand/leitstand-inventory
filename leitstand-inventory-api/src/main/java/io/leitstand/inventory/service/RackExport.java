package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

/**
 * A rack inventory export.
 */
public class RackExport extends ValueObject{
	
    /**
     * Creates a builder for an immutable <code>RackExport</code> value object.
     * @return a builder for an immutable <code>RackExport</code> value object.
     */
	public static Builder newRackExport() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>RackExport</code> value object.
	 */
	public static class Builder {
		private RackExport export = new RackExport();

		/**
		 * Sets the exported rack settings.
		 * @param racks the exported racks.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withRacks(List<RackExportItem> racks) {
			assertNotInvalidated(getClass(), export);
			this.export.racks = new ArrayList<>(racks);
			return this;
		}
		
		/**
		 * Sets the rack export creation date.
		 * @param dateCreated the creation date.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDateCreated(Date dateCreated) {
			assertNotInvalidated(getClass(), export);
			this.export.dateCreated = new Date(dateCreated.getTime());
			return this;
		}
		
		/**
		 * Creates an immutable <code>RackExport</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>RackExport</code> value object.
		 */
		public RackExport build() {
			try {
				assertNotInvalidated(getClass(),export);
				return export;
			} finally {
				this.export = null;
			}
		}

	}

	private List<RackExportItem> racks = emptyList();
	private Date dateCreated = new Date();
	
	/**
	 * Returns the exported racks.
	 * @return the exported racks-
	 */
	public List<RackExportItem> getRacks(){
		return unmodifiableList(racks);
	}
	
	/**
	 * Returns the export creation date.
	 * @return the export creation date.
	 */
	public Date getDateCreated() {
		return new Date(dateCreated.getTime());
	}
}
