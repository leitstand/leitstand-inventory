package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

public class RackExport extends ValueObject{
	
	public static Builder newRackExport() {
		return new Builder();
	}
	
	public static class Builder {
		private RackExport export = new RackExport();

		public Builder withRacks(List<RackExportItem> racks) {
			assertNotInvalidated(getClass(), export);
			this.export.racks = new ArrayList<>(racks);
			return this;
		}
		
		public Builder withDateCreated(Date dateCreated) {
			assertNotInvalidated(getClass(), export);
			this.export.dateCreated = new Date(dateCreated.getTime());
			return this;
		}
		
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
	
	
	public List<RackExportItem> getRacks(){
		return unmodifiableList(racks);
	}
	
	public Date getDateCreated() {
		return new Date(dateCreated.getTime());
	}
}
