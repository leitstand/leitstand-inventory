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
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

/**
 * Export of all groups and their respective {@link ElementGroupSettings} along with the corresponding elements with their respective {@link ElementSettings}
 */
public class ElementGroupsExport extends ValueObject {

	public static Builder newInventoryExport() {
		return new Builder();
	}
	
	public static class Builder {
		private ElementGroupsExport export = new ElementGroupsExport();
		
		public Builder withDateCreated(Date date) {
			assertNotInvalidated(getClass(), export);
			export.dateCreated = new Date(date.getTime());
			return this;
		}
		
		public Builder withElementRoles(List<ElementRoleSettings> types) {
			assertNotInvalidated(getClass(), export);
			export.roles = new LinkedList<>(types);
			return this;
		}
		
		public Builder withPlatforms(List<PlatformSettings> platforms) {
			assertNotInvalidated(getClass(), export);
			export.platforms = new LinkedList<>(platforms);
			return this;
		}
		
		public Builder withGroups(ElementGroupExport.Builder... groups) {
			return withGroups(stream(groups)
							.map(ElementGroupExport.Builder::build)
							.collect(toList()));
		}
		
		public Builder withGroups(List<ElementGroupExport> groups) {
			assertNotInvalidated(getClass(), export);
			export.groups = new LinkedList<>(groups);
			return this;
		}
		
		public Builder withRacks(List<ElementRack> racks) {
			assertNotInvalidated(getClass(), export);
			export.racks = new LinkedList<>(racks);
			return this;
		}
		
		public ElementGroupsExport build() {
			try {
				assertNotInvalidated(getClass(), export);
				return export;
			} finally {
				this.export = null;
			}
		}
	}
	
	private Date dateCreated;
	private List<ElementRoleSettings> roles = emptyList();
	private List<PlatformSettings> platforms = emptyList();
	private List<ElementGroupExport> groups = emptyList();
	private List<ElementRack> racks = emptyList();

	
	public List<ElementRack> getRacks() {
		return unmodifiableList(racks);
	}
	
	public List<ElementGroupExport> getGroups() {
		return unmodifiableList(groups);
	}
	
	public Date getDateCreated() {
		return dateCreated;
	}
	
	public List<ElementRoleSettings> getRoles() {
		return unmodifiableList(roles);
	}
	
	public List<PlatformSettings> getPlatforms() {
		return unmodifiableList(platforms);
	}
}
