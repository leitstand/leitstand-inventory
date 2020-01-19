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

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementEnvironment;
import io.leitstand.inventory.service.ElementEnvironmentService;
import io.leitstand.inventory.service.ElementEnvironments;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.Environment;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;

@Service
public class DefaultElementEnvironmentService implements ElementEnvironmentService {

	private ElementProvider elements;
	private ElementEnvironmentManager manager;
	
	protected DefaultElementEnvironmentService() {
		// CDO
	}
	
	@Inject
	protected DefaultElementEnvironmentService(ElementProvider elements, ElementEnvironmentManager manager) {
		this.elements = elements;
		this.manager = manager;
	}
	
	
	
	@Override
	public ElementEnvironment getElementEnvironment(EnvironmentId id) {
		return manager.getElementEnvironment(id);
	}

	@Override
	public ElementEnvironment getElementEnvironment(ElementId elementId, EnvironmentName name) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementEnvironment(element,name);
	}

	@Override
	public ElementEnvironment getElementEnvironment(ElementName elementName, EnvironmentName name) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementEnvironment(element,name);
	}

	@Override
	public boolean storeElementEnvironment(ElementId elementId, Environment env) {
		Element element = elements.fetchElement(elementId);
		return manager.storeElementEnvironment(element,env);
	}

	@Override
	public boolean storeElementEnvironment(ElementName elementName, Environment env) {
		Element element = elements.fetchElement(elementName);
		return manager.storeElementEnvironment(element,env);
	}

	@Override
	public void removeElementEnvironment(EnvironmentId id) {
		manager.removeElementEnvironment(id);
	}

	@Override
	public void removeElementEnvironment(ElementId elementId, EnvironmentName name) {
		Element element = elements.fetchElement(elementId);
		manager.removeElementEnvironment(element,name);
		
	}

	@Override
	public void removeElementEnvironment(ElementName elementName, EnvironmentName name) {
		Element element = elements.fetchElement(elementName);
		manager.removeElementEnvironment(element,name);
	}

	@Override
	public ElementEnvironments getElementEnvironments(ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementEnvironments(element);
	}

	@Override
	public ElementEnvironments getElementEnvironments(ElementName elementName) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementEnvironments(element);
	}

}
