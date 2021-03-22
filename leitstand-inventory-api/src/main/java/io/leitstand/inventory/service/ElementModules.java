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
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;

/**
 * A collection of element hardware modules.
 */
public class ElementModules extends BaseElementEnvelope{

	/**
	 * Returns a builder to create an immutable <code>ElementModules</code> instance.
	 * @return a builder to create an immutable <code>ElementModules</code> instance.
	 */
	public static Builder newElementModules(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementModules</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementModules, Builder>{
		
		public Builder() {
			super(new ElementModules());
		}
		
		/**
		 * Sets the element hardware modules.
		 * @param modules - the element hardware modules
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModules(ModuleData.Builder... modules) {
			return withModules(stream(modules)
							   .map(ModuleData.Builder::build)
							   .collect(toList()));
		}
		
		/**
		 * Sets the element hardware modules.
		 * @param modules - the element hardware modules
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModules(ModuleData... modules) {
			return withModules(asList(modules));
		}
		
		/**
		 * Sets the element hardware modules.
		 * @param modules - the element hardware modules
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModules(List<ModuleData> modules) {
			assertNotInvalidated(getClass(), modules);
			object.modules = unmodifiableList(new LinkedList<>(modules));
			return this;
		}
		
	}
	
	private List<ModuleData> modules;
	
	/**
	 * Returns the list of element units.
	 * Returns an empty list if no unit information is available
	 * @return the element units.
	 */
	public List<ModuleData> getModules() {
		return modules;
	}
	
}
