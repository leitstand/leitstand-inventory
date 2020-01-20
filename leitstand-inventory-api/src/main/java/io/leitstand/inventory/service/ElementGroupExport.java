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
