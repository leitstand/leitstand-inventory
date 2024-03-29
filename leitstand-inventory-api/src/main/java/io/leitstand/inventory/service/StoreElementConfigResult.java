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

public class StoreElementConfigResult {

	public static final StoreElementConfigResult configCreated(ElementConfigId configId) {
		return new StoreElementConfigResult(configId,true);
	}

	public static final StoreElementConfigResult seeOther(ElementConfigId configId) {
		return new StoreElementConfigResult(configId,false);
	}

	
	private StoreElementConfigResult(ElementConfigId configId, boolean created) {
		this.configId = configId;
		this.created = created;
	}
	
	private ElementConfigId configId;
	private boolean created;
	
	
	public ElementConfigId getConfigId() {
		return configId;
	}	
	public boolean isCreated() {
		return created;
	}
	
}
