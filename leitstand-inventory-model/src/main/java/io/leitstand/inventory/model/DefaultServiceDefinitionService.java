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

import static io.leitstand.inventory.model.Service.findAllServices;
import static io.leitstand.inventory.service.ServiceDefinition.newServiceDefinition;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ServiceDefinition;
import io.leitstand.inventory.service.ServiceDefinitionService;

@io.leitstand.commons.model.Service
public class DefaultServiceDefinitionService implements ServiceDefinitionService {

	@Inject
	@Inventory
	private Repository repository;
	
	@Override
	public List<ServiceDefinition> getServices() {
		return repository.execute(findAllServices())
						 .stream()
						 .map(service -> newServiceDefinition()
								 		 .withServiceName(service.getServiceName())
								 		 .withDisplayName(service.getDisplayName())
								 		 .withDescription(service.getDescription())
								 		 .withServiceType(service.getServiceType())
								 		 .build())
						 .collect(toList());
	}

}
