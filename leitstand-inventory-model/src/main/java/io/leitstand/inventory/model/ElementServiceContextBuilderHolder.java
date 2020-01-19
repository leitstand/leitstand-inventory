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
package io.leitstand.inventory.model;

import io.leitstand.inventory.service.ElementServiceContext;

class ElementServiceContextBuilderHolder {

	Long id;
	Long parent;
	ElementServiceContext.Builder builder;
	
	public ElementServiceContextBuilderHolder(Long id, Long parent, ElementServiceContext.Builder builder) {
		this.id 	 = id;
		this.parent  = parent;
		this.builder = builder;
	}
	
	public Long getId() {
		return id;
	}
	
	public Long getParent() {
		return parent;
	}
	
	public ElementServiceContext.Builder getBuilder() {
		return builder;
	}
}
