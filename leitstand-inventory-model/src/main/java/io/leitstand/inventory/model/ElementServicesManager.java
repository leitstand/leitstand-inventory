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

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.optional;
import static io.leitstand.inventory.jpa.OperationalStateConverter.toOperationalState;
import static io.leitstand.inventory.model.Element_Service.findElementService;
import static io.leitstand.inventory.model.Element_Service.findElementServices;
import static io.leitstand.inventory.model.Element_ServiceContext.findServiceContext;
import static io.leitstand.inventory.model.Service.findService;
import static io.leitstand.inventory.service.ElementId.elementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementServiceContext.newElementServiceContext;
import static io.leitstand.inventory.service.ElementServiceReference.newElementServiceReference;
import static io.leitstand.inventory.service.ElementServiceStack.newElementServiceStack;
import static io.leitstand.inventory.service.ElementServices.newElementServices;
import static io.leitstand.inventory.service.ReasonCode.IVT0320E_ELEMENT_SERVICE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0321I_ELEMENT_SERVICE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0322I_ELEMENT_SERVICE_REMOVED;
import static io.leitstand.inventory.service.ServiceData.newServiceData;
import static io.leitstand.inventory.service.ServiceInfo.newServiceInfo;
import static io.leitstand.inventory.service.ServiceName.serviceName;
import static java.lang.String.format;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.jpa.ServiceTypeConverter;
import io.leitstand.inventory.service.ElementServiceContext;
import io.leitstand.inventory.service.ElementServiceReference;
import io.leitstand.inventory.service.ElementServiceStack;
import io.leitstand.inventory.service.ElementServiceSubmission;
import io.leitstand.inventory.service.ElementServices;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ServiceInfo;
import io.leitstand.inventory.service.ServiceName;

@Dependent
public class ElementServicesManager {
	
	private static final Logger LOG = Logger.getLogger(ElementServicesManager.class.getName());

	
	private ElementProvider elements;
	private Repository repository;
	private DatabaseService datasource;
	private SubtransactionService transaction;
	private Messages messages;
	protected ElementServicesManager() {
		// CDI
	}
	
	
	@Inject
	protected ElementServicesManager(@Inventory Repository repository, 
									 @Inventory DatabaseService datasource,
	                                 @Inventory SubtransactionService transaction,
	                                 ElementProvider elements,
	                                 Messages messages){
		this.repository = repository;
		this.datasource = datasource;
		this.transaction = transaction;
		this.elements = elements;
		this.messages = messages;
	}
	
	public ElementServices getElementServices(Element element) {
		ElementGroup group = element.getGroup();
		List<ServiceInfo> services = new LinkedList<>();
		for(Element_Service service: repository.execute(findElementServices(element))){
			
			services.add(newServiceInfo()
						 .withServiceName(service.getServiceName())
						 .withDisplayName(service.getDisplayName())
						 .withOperationalState(service.getOperationalState())
						 .withDateModified(service.getDateModified())
						 .withServiceType(service.getServiceType())
						 .build());
			
		}
		
		services.sort((a,b) -> a.getServiceName().compareTo(b.getServiceName())); 
		
		return newElementServices()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withServices(services)
			   .build();
	}


	public ElementServiceStack getElementServiceStack(Element element, 
													  ServiceName name) {
		ElementGroup group = element.getGroup();
		Service service = repository.execute(Service.findService(name));
		
		if(service == null) {
			throw new EntityNotFoundException(IVT0320E_ELEMENT_SERVICE_NOT_FOUND, 
											  element.getElementName(), 
											  name);
		}
		
		
		String sql = "WITH RECURSIVE HIERARCHY (servicecontext_id, element_id, element_uuid, element_name, service_id, service_type,service_name, "+
		                                       "service_opstate, service_display_name, "+
				                               "parent_id, level)"+ 
					 "AS ( "+
					 "SELECT servicecontext_id, element_id, element_uuid, element_name, service_id, service_type, service_name,"+
		                    "service_opstate, service_display_name, "+
				            "parent_id, 1 "+
					 "FROM inventory.service_context "+
					 "WHERE element_uuid=? AND service_name=? "+
					 "UNION ALL "+
					 "SELECT c.servicecontext_id, c.element_id, c.element_uuid, c.element_name, c.service_id, c.service_type, c.service_name, "+
	                        "c.service_opstate, c.service_display_name,"+
			                "c.parent_id, level + 1 "+
					 "FROM   inventory.service_context c "+ 
					 "JOIN 	 HIERARCHY h "+
					 "ON 	 c.servicecontext_id = h.parent_id "+
					 ") "+
					 "SELECT element_uuid, element_name, service_type, service_name, service_display_name, service_opstate "+
					 "FROM   HIERARCHY ctx "+
					 "WHERE  level < 10 "+ // Circuit breaker, if someone has accidentally stored a circular service dependency
					 "ORDER BY level"; // Order the result by level to get a proper order of the service hierarchy.
		
		List<ServiceInfo> services = datasource.executeQuery(prepare(sql,
																	 element.getElementId().toString(),
																	 name.toString()),
						  									 rs ->  newServiceInfo()
						  											.withElementId(elementId(rs.getString(1)))
						  											.withElementName(elementName(rs.getString(2)))
						  											.withServiceType(ServiceTypeConverter.parse(rs.getString(3)))
						  											.withServiceName(serviceName(rs.getString(4)))
						  											.withDisplayName(rs.getString(5))
						  											.withOperationalState(toOperationalState(rs.getString(6)))
																	.build());
		return newElementServiceStack()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withServices(services)
			   .build();
	}

	public boolean storeElementService(Element element, ElementServiceSubmission submission) {
		boolean created = false;
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		Element_Service service = repository.execute(findElementService(element, 
																	    submission.getServiceName()));
		if(service == null){
			Service serviceDef = repository.execute(findService(submission.getServiceName()));
			if(serviceDef == null){
				serviceDef = transaction.run(action -> { Service newServiceDef = new Service(submission.getServiceType(),
																							 submission.getServiceName());
														 action.add(newServiceDef);}, 
											  resume -> resume.execute(findService(submission.getServiceName())));
			}
			service = new Element_Service(element,serviceDef);
			repository.add(service);
			created = true;
		}
		service.setOperationalState(submission.getOperationalState());
		service.setServiceContext(submission.getServiceContext());
		service.setServiceContextType(submission.getServiceContextType());
		
		if(submission.getParentService() != null){
			ElementServiceReference parentRef = submission.getParentService();
			Element parentElement = elements.tryFetchElement(parentRef.getElementName());
			if(parentElement != null){
				Element_ServiceContext parent = repository.execute(findServiceContext(element,
																					   parentRef.getServiceName()));
				service.setParentContext(parent);
			}
			
		}
		
		LOG.fine(() -> format("%s: Service %s does not exist on %s %s (%s).",
							  IVT0321I_ELEMENT_SERVICE_STORED.getReasonCode(),
							  submission.getServiceName(),
							  element.getElementRoleName(),
							  element.getElementName(),
							  element.getElementId()));
		
		messages.add(createMessage(IVT0321I_ELEMENT_SERVICE_STORED, 
								   element.getElementId(),
								   element.getElementName(),
								   submission.getServiceName()));
		
		return created;
	}

	
	public void updateElementServiceOperationalState(Element element,
													 ServiceName serviceName,
													 OperationalState state) {
		Element_Service service = repository.execute(findElementService(element,
																		serviceName));
		if(service == null){
			LOG.fine(() -> format("%s: Service %s does not exist on element %s (%s).",
								  IVT0320E_ELEMENT_SERVICE_NOT_FOUND.getReasonCode(),
								  serviceName,
								  element.getElementName(),
								  element.getElementId()));
			throw new EntityNotFoundException(IVT0320E_ELEMENT_SERVICE_NOT_FOUND,
											  element.getElementName(),
											  serviceName);
		}
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		service.setOperationalState(state);

	}

	public void storeElementServices(Element element, List<ElementServiceSubmission> services) {
		repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
		for(ElementServiceSubmission service : services){
			storeElementService(element, service);
		}
	}

	public void removeElementService(Element element, 
	                                 ServiceName serviceName) {
		Element_Service service = repository.execute(findElementService(element, 
																	    serviceName));
		if(service != null){
			repository.lock(element, OPTIMISTIC_FORCE_INCREMENT);
			repository.remove(service);
			LOG.fine(() -> format("%s: Service %s does not exist on %s %s (%s).",
								  IVT0322I_ELEMENT_SERVICE_REMOVED.getReasonCode(),
								  serviceName,
								  element.getElementRoleName(),
								  element.getElementName(),
								  element.getElementId()));

			messages.add(createMessage(IVT0322I_ELEMENT_SERVICE_REMOVED, 
									   element.getElementId(),
									   element.getElementName(),
									   serviceName));
		}
	}
	


	public ElementServiceContext getElementService(Element element, ServiceName serviceName) {
		Element_Service service = repository.execute(findElementService(element, serviceName));
		if(service == null) {
			LOG.fine(() -> format("%s: Service %s does not exist on element %s (%s).",
								  IVT0320E_ELEMENT_SERVICE_NOT_FOUND.getReasonCode(),
								  serviceName,
								  element.getElementName(),
								  element.getElementId()));
			throw new EntityNotFoundException(IVT0320E_ELEMENT_SERVICE_NOT_FOUND, 
										 	  element.getElementId(),
										 	  element.getElementName(),
										 	  serviceName);
		}
		
		ElementServiceReference parentRef = optional(service.getParent(), 
													 parent -> newElementServiceReference()
													 		   .withElementName(parent.getElement().getElementName())
													 		   .withServiceName(parent.getServiceName())
													 		   .build());
		
		return newElementServiceContext()
			   .withGroupId(element.getGroupId())
			   .withGroupType(element.getGroupType())
			   .withGroupName(element.getGroupName())
			   .withElementRole(element.getElementRoleName())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withService(newServiceData()
					   		.withServiceType(service.getServiceType())
					   		.withServiceName(service.getServiceName())
					   		.withDisplayName(service.getDisplayName())
					   		.withDescription(service.getDescription())
					   		.withOperationalState(service.getOperationalState())
					   		.withServiceContextType(service.getServiceContextType())
					   		.withServiceContext(service.getServiceContext())
					   		.withParent(parentRef))
			.build();

	}
}
