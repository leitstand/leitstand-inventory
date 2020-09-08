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
import javax.ws.rs.core.MediaType;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigRevisions;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementConfigs;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.StoreElementConfigResult;

@Service
public class DefaultElementConfigService implements ElementConfigService {

	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementConfigManager manager;
	
	public DefaultElementConfigService() {
		// CDI
	}
	
	DefaultElementConfigService(ElementProvider elements, ElementConfigManager manager){
		this.elements = elements;
		this.manager = manager;
	}

	@Override
	public ElementConfigs findElementConfigs(ElementId elementId, 
											 String filter) {
		Element element = elements.fetchElement(elementId);
		return manager.filterElementConfig(element, 
										   filter);
	}

	@Override
	public ElementConfigs findElementConfigs(ElementName elementName, 
											 String filter) {
		Element element = elements.fetchElement(elementName);
		return manager.filterElementConfig(element, 
										   filter);
	}
	
	
	@Override
	public ElementConfig getActiveElementConfig(ElementId id, 
										  ElementConfigName configName) {
		Element element = elements.fetchElement(id);
		return manager.getElementConfig(element,
						   			    configName);
	}

	@Override
	public ElementConfig getActiveElementConfig(ElementName elementName, 
										  ElementConfigName configName) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementConfig(element,
										configName);
	}
	


	@Override
	public ElementConfigRevisions getElementConfigRevisions(ElementId elementId, 
															ElementConfigName configName) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementConfigRevisions(element, configName);
	}

	@Override
	public ElementConfigRevisions getElementConfigRevisions(ElementName elementName, 
															ElementConfigName configName) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementConfigRevisions(element, configName);
	}

	@Override
	public int removeElementConfig(ElementId elementId, 
									ElementConfigName configName) {
		Element element = elements.fetchElement(elementId);
		return manager.removeElementConfigRevisions(element,
									  				configName);
	}

	@Override
	public void removeElementConfig(ElementId elementId, 
									ElementConfigId configId) {
		Element element = elements.fetchElement(elementId);
		manager.removeElementConfig(element,
									configId);
	}
	
	@Override
	public void removeElementConfig(ElementName elementName, 
									ElementConfigId configId) {
		Element element = elements.fetchElement(elementName);
		manager.removeElementConfig(element,
								    configId);
	}

	@Override
	public int removeElementConfig(ElementName elementName,
								   ElementConfigName configName) {
		Element element = elements.fetchElement(elementName);
		return manager.removeElementConfigRevisions(element,
									  				configName);
	}

	@Override
	public StoreElementConfigResult storeElementConfig(ElementId elementId, 
													   ElementConfigName configName,
													   MediaType contentType,
													   ConfigurationState configState,
													   String config,
													   String comment) {
		Element element = elements.fetchElement(elementId);
		return manager.storeElementConfig(element,
										  configName,
										  contentType,
										  configState,
										  config,
										  comment);
	}
	
	@Override
	public StoreElementConfigResult storeElementConfig(ElementName elementName, 
									  				   ElementConfigName configName,
									  				   MediaType contentType,
									  				   ConfigurationState configState,
									  				   String config,
									  				   String comment) {
		Element element = elements.fetchElement(elementName);
		return manager.storeElementConfig(element,
										  configName,
										  contentType,
										  configState,
										  config,
										  comment);
	}
	
	@Override
	public void setElementConfigComment(ElementId elementId,
										ElementConfigId configId, 
										String comment) { 
		Element element = elements.fetchElement(elementId);
		manager.setElementConfigComment(element,
										configId,
										comment);
	}
	
	@Override
	public void setElementConfigComment(ElementName elementName,
										ElementConfigId configId, 
										String comment) { 
		Element element = elements.fetchElement(elementName);
		manager.setElementConfigComment(element,
										configId,
										comment);
	}

	@Override
	public ElementConfig getElementConfig(ElementId elementId,
										  ElementConfigId configId) {
		Element element = elements.fetchElement(elementId);
		return manager.getElementConfig(element,
										configId);
	}
	
	@Override
	public ElementConfig getElementConfig(ElementName elementName,
										  ElementConfigId configId) {
		Element element = elements.fetchElement(elementName);
		return manager.getElementConfig(element,
										configId);
	}

	@Override
	public StoreElementConfigResult restoreElementConfig(ElementId elementId, 
													     ElementConfigId configId,
													     String comment) {
		Element element = elements.fetchElement(elementId);
		return manager.restoreElementConfig(element,
										    configId,
										    comment);
	}

	@Override
	public StoreElementConfigResult restoreElementConfig(ElementName elementName, 
													     ElementConfigId configId,
													     String comment) {
		Element element = elements.fetchElement(elementName);
		return manager.restoreElementConfig(element,
										    configId,
										    comment);
	}

    @Override
    public ElementConfig getElementConfig(ElementId elementId, ElementConfigName configName) {
        Element element = elements.fetchElement(elementId);
        return manager.getElementConfig(element, configName);
    }

    @Override
    public ElementConfig getElementConfig(ElementName elementName, ElementConfigName configName) {
        Element element = elements.fetchElement(elementName);
        return manager.getElementConfig(element, configName);
    }
    
    @Override
    public void purgeOutdatedElementConfigs(ElementId elementId, ElementConfigName configName) {
        Element element = elements.fetchElement(elementId);
        manager.purgeOutdatedConfigurations(element, configName);
    }
    
    @Override
    public void purgeOutdatedElementConfigs(ElementName elementName, ElementConfigName configName) {
        Element element = elements.fetchElement(elementName);
        manager.purgeOutdatedConfigurations(element, configName);
    }
	
}
