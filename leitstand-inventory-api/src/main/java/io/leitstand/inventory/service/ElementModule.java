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

/**
 * A element hardware module.
 */
public class ElementModule extends BaseElementEnvelope {

	/**
	 * Creates a builder for an immutable <code>ElementModule</code> value object.
	 * @return a builder for an immutable <code>ElementModule</code> value object.
	 */
	public static Builder newElementModule() {
		return new Builder();
	}

	/**
     * A builder for an immutable <code>ElementModule</code> value object.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementModule, Builder> {

		public Builder() {
			super(new ElementModule());
		}
		
		/**
		 * Sets the module data.
		 * @param builder the module data builder.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withModule(ModuleData.Builder builder){
			return withModule(builder.build());
		}
		
		/**
		 * Sets the module data.
		 * @param module the module data.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withModule(ModuleData module){
			assertNotInvalidated(getClass(), object);
			object.module = module;
			return this;
		}

	}

	private ModuleData module;

	/**
	 * Returns the module data.
	 * @return the module data.
	 */
	public ModuleData getModule(){
		return module;
	}

}
