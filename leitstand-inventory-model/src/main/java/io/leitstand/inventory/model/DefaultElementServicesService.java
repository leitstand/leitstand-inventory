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

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementServiceContext;
import io.leitstand.inventory.service.ElementServiceStack;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServices;
import io.leitstand.inventory.service.ElementServicesService;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ServiceName;

@Service
public class DefaultElementServicesService implements ElementServicesService {

	private ElementProvider elements;
	
	private ElementServicesManager manager;
	
	protected DefaultElementServicesService() {
		// CDI
	}
	
	@Inject
	protected DefaultElementServicesService(ElementProvider elements, 
											ElementServicesManager manager) {
		this.elements = elements;
		this.manager = manager;
	}
	
	@Override
	public ElementServices getElementServices(ElementId id) {
		Element element = elements.fetchElement(id);
		return manager.getElementServices(element);
	}

	@Override
	public ElementServices getElementServices(ElementName name) {
		Element element = elements.fetchElement(name);
		return manager.getElementServices(element);
	}

	@Override
	public ElementServiceStack getElementServiceStack(ElementId id, 
													  ServiceName service) {
		Element element = elements.fetchElement(id);
		return manager.getElementServiceStack(element,
											  service);
	}

	@Override
	public ElementServiceStack getElementServiceStack(ElementName name, 
													  ServiceName service) {
		Element element = elements.fetchElement(name);
		return manager.getElementServiceStack(element,
											  service);
	}

	@Override
	public void storeElementServices(ElementId id, List<ElementServiceSubmission> services) {
		Element element = elements.fetchElement(id);
		manager.storeElementServices(element,services);
	}
	

	@Override
	public void storeElementServices(ElementName name, List<ElementServiceSubmission> services) {
		Element element = elements.fetchElement(name);
		manager.storeElementServices(element,services);
	}

	@Override
	public boolean storeElementService(ElementId id, ElementServiceSubmission service) {
		Element element = elements.fetchElement(id);
		return manager.storeElementService(element,service);
	}
	
	@Override
	public boolean storeElementService(ElementName name, ElementServiceSubmission service) {
		Element element = elements.fetchElement(name);
		return manager.storeElementService(element,service);
	}


	@Override
	public void updateElementServiceOperationalState(	ElementId id,
														ServiceName serviceName,
														OperationalState state) {
		Element element = elements.fetchElement(id);
		manager.updateElementServiceOperationalState(element,
													 serviceName,
													 state);
	}

	@Override
	public void updateElementServiceOperationalState(	ElementName name,
														ServiceName serviceName,
														OperationalState state) {
		Element element = elements.fetchElement(name);
		manager.updateElementServiceOperationalState(element,
													serviceName,
													state);
	}

	@Override
	public void removeElementService(ElementId id, 
									 ServiceName serviceName) {
		Element element = elements.fetchElement(id);
		manager.removeElementService(element,serviceName);
	}

	@Override
	public void removeElementService(ElementName name, 
									 ServiceName serviceName) {
		Element element = elements.fetchElement(name);
		manager.removeElementService(element,serviceName);
	}

	@Override
	public ElementServiceContext getElementService(ElementId elementId, ServiceName service) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementService(element,service);
	}

	@Override
	public ElementServiceContext getElementService(ElementName elementName, ServiceName service) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementService(element,service);
	}
	
}
