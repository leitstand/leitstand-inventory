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
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.visitor.ElementTransformation;
import io.leitstand.inventory.visitor.ElementTransformationService;

@Service
public class DefaultElementTransformationService implements ElementTransformationService {

	@Inject
	private ElementProvider elements;
	
	@Inject
	private ElementTransformationManager manager;
	
	@Override
	public <T> T apply(ElementId elementId, 
					   ElementTransformation<T> transformation) {
		Element element = elements.fetchElement(elementId);
		return manager.apply(element,
							 transformation);
	}

	@Override
	public <T> T apply(ElementName elementName, 
					   ElementTransformation<T> transformation) {
		Element element = elements.fetchElement(elementName);
		return manager.apply(element,
					 		 transformation);
	}

}
