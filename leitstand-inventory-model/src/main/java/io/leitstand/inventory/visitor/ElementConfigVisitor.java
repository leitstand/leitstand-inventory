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
package io.leitstand.inventory.visitor;

import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigReference;

public interface ElementConfigVisitor {

	default String getConfigSelector() {
		return null;
	}
	boolean visitElementConfig(ElementConfigReference config);
	
	default void visitElementConfig(ElementConfig config) {
		
	}
	
}
