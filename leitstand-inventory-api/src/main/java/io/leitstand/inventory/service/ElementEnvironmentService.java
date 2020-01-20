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

public interface ElementEnvironmentService {

	ElementEnvironments getElementEnvironments(ElementId elementId);
	ElementEnvironments getElementEnvironments(ElementName elementName);
	
	ElementEnvironment getElementEnvironment(EnvironmentId id);
	ElementEnvironment getElementEnvironment(ElementId elementId, EnvironmentName name);
	ElementEnvironment getElementEnvironment(ElementName elementName, EnvironmentName name);
	boolean storeElementEnvironment(ElementId elementId, Environment env);
	boolean storeElementEnvironment(ElementName elementName, Environment env);
	void removeElementEnvironment(EnvironmentId id);
	void removeElementEnvironment(ElementId elementId, EnvironmentName name);
	void removeElementEnvironment(ElementName elementName, EnvironmentName name);
	
}
