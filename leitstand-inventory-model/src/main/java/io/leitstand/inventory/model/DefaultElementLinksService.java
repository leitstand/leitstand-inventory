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
import io.leitstand.inventory.service.ElementLinkService;
import io.leitstand.inventory.service.ElementLinks;
import io.leitstand.inventory.service.ElementName;

@Service
public class DefaultElementLinksService implements ElementLinkService {

	@Inject
	private ElementProvider elements;
	@Inject
	private ElementLinksManager manager;

	@Override
	public ElementLinks getElementLinks(ElementId id) {
		Element element = elements.fetchElement(id);
		return manager.getElementLinks(element);
	}

	@Override
	public ElementLinks getElementLinks(ElementName name) {
		Element element = elements.fetchElement(name);
		return manager.getElementLinks(element);
	}

}
