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
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;

public class ElementGroupRacks extends BaseElementGroupEnvelope {

	public static Builder newElementGroupRacks() {
		return new Builder();
	}
	
	public static class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupRacks, Builder>{
		 
		public Builder() {
			super(new ElementGroupRacks());
		}
		
		public Builder withRacks(RackSettings.Builder... racks) {
			return withRacks(stream(racks)
							 .map(RackSettings.Builder::build)
							 .collect(toList()));
		}
		
		public Builder withRacks(List<RackSettings> racks) {
			assertNotInvalidated(getClass(), object);
			object.racks = racks;
			return this;
		}
	}
	
	private List<RackSettings> racks;
	
	public List<RackSettings> getRacks() {
		return unmodifiableList(racks);
	}
}
