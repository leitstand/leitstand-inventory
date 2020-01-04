/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
