/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

public class ElementGroupExport extends ValueObject {

	public static Builder newElementGroupExport() {
		return new Builder();
	}
	
	public static class Builder {
		private ElementGroupExport export = new ElementGroupExport();
		
		public Builder withGroup(ElementGroupSettings groupSettings) {
			assertNotInvalidated(getClass(), export);
			export.group = groupSettings;
			return this;
		}

		public Builder withElements(List<ElementSettings> elements) {
			assertNotInvalidated(getClass(), export);
			export.elements = new ArrayList<>(elements);
			return this;
		}
		
		public ElementGroupExport build() {
			try {
				assertNotInvalidated(getClass(), export);
				return export;
			} finally {
				this.export = null;
			}
		}

	}
	
	private ElementGroupSettings group;
	private List<ElementSettings> elements = emptyList();
	
	
	public ElementGroupSettings getGroup() {
		return group;
	}
	
	public List<ElementSettings> getElements(){
		return unmodifiableList(elements);
	}
	
}
