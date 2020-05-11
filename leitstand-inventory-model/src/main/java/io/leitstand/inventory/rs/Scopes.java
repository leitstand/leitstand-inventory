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
package io.leitstand.inventory.rs;

public final class Scopes {
	
	public static final String IVT = "ivt";
	public static final String IVT_READ = "ivt.read";
	public static final String IVT_ELEMENT = "ivt.element";
	public static final String IVT_ELEMENT_SETTINGS = "ivt.element.settings";
	public static final String IVT_ELEMENT_DNS = "ivt.element.dns";
	public static final String IVT_ELEMENT_CONFIG ="ivt.element.config";
	public static final String IVT_ELEMENT_MODULE = "ivt.element.module";
	public static final String IVT_GROUP = "ivt.group";
	public static final String IVT_GROUP_SETTINGS = "ivt.group.settings";
	public static final String IVT_IMAGE = "ivt.image";
	
	
	private Scopes() {
		// No instances allowed
	}
}
