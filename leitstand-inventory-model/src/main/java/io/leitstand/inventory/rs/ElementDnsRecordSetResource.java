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

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT_DNS;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static io.leitstand.inventory.service.ReasonCode.IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSet;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
import io.leitstand.inventory.service.ElementDnsRecordSet;
import io.leitstand.inventory.service.ElementDnsRecordSetService;
import io.leitstand.inventory.service.ElementDnsRecordSets;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.security.auth.Scopes;

@Resource
@Path("/elements")
@Scopes({IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class ElementDnsRecordSetResource {

	@Inject
	private ElementDnsRecordSetService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/dns")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
	public ElementDnsRecordSets getElementDnsRecordSets(@Valid @PathParam("element") ElementId elementId) {
		return service.getElementDnsRecordSets(elementId);
	}
	
	@GET
	@Path("/{element}/dns")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
	public ElementDnsRecordSets getElementDnsRecordSets(@Valid @PathParam("element") ElementName elementName) {
		return service.getElementDnsRecordSets(elementName);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/dns/{dns_name}/{dns_type}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
	public ElementDnsRecordSet getElementDnsRecordSet(@Valid @PathParam("element") ElementId elementId,
													  @Valid @PathParam("dns_name") DnsName dnsName,
													  @Valid @PathParam("dns_type") DnsRecordType dnsType) {
		return service.getElementDnsRecordSet(elementId, dnsName, dnsType);
	}
	
	@GET
	@Path("/{element}/dns/{dns_name}/{dns_type}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
	public ElementDnsRecordSet getElementDnsRecordSets(@Valid @PathParam("element") ElementName elementName,
															  @Valid @PathParam("dns_name") DnsName dnsName,
															  @Valid @PathParam("dns_type") DnsRecordType dnsType) {
		return service.getElementDnsRecordSet(elementName,dnsName,dnsType);
	}
	
	@GET
	@Path("/{element:"+UUID_PATTERN+"}/dns/{dns_id:"+UUID_PATTERN+"}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
	public ElementDnsRecordSet getElementDnsRecordSet(@Valid @PathParam("element") ElementId elementId,
													  @Valid @PathParam("dns_id") DnsRecordSetId dnsId) {
		ElementDnsRecordSet recordSet = service.getElementDnsRecordSet(dnsId);
		if(isDifferent(elementId,recordSet.getElementId())) {
			throw new ConflictException(IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT, 
										recordSet.getElementId(),
										recordSet.getElementName());
		}
		return recordSet;
	}

	
	@GET
	@Path("/{element}/dns/{dns_id:"+UUID_PATTERN+"}")
	@Scopes({IVT_READ, IVT, IVT_ELEMENT,IVT_ELEMENT_DNS})
	public ElementDnsRecordSet getElementDnsRecordSet(@Valid @PathParam("element") ElementName elementName,
													  @Valid @PathParam("dns_id") DnsRecordSetId dnsId) {
		ElementDnsRecordSet recordSet = service.getElementDnsRecordSet(dnsId);
		if(isDifferent(elementName,recordSet.getElementName())) {
			throw new ConflictException(IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT, 
										recordSet.getElementId(),
										recordSet.getElementName());
		}
		return recordSet;
	}
	
	@PUT
	@Path("/{element:"+UUID_PATTERN+"}/dns/{dns_id:"+UUID_PATTERN+"}")
	public Response storeElementDnsRecordSet(@Valid @PathParam("element") ElementId elementId,
											 @Valid @PathParam("dns_id") DnsRecordSetId dnsRecordSetId,
											 @Valid DnsRecordSet record) {
		if(isDifferent(dnsRecordSetId, record.getDnsRecordSetId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "dns_recordset_id", 
												   dnsRecordSetId, 
												   record.getDnsRecordSetId());
		}
		
		boolean created = service.storeElementDnsRecordSet(elementId, record);
		if(created) {
			return created(messages, dnsRecordSetId);
		}
		return success(messages);
	}
	
	@PUT
	@Path("/{element}/dns/{dns_id:"+UUID_PATTERN+"}")
	public Response storeElementDnsRecordSet(@Valid @PathParam("element") ElementName elementName,
											  @Valid @PathParam("dns_id") DnsRecordSetId dnsRecordSetId,
											  @Valid DnsRecordSet record) {
		if(isDifferent(dnsRecordSetId, record.getDnsRecordSetId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "dns_recordset_id", 
												   dnsRecordSetId, 
												   record.getDnsRecordSetId());
		}
		
		boolean created = service.storeElementDnsRecordSet(elementName, record);
		if(created) {
			return created(messages, dnsRecordSetId);
		}
		return success(messages);
	}
	
	@POST
	@Path("/{element:"+UUID_PATTERN+"}/dns")
	public Response storeElementDnsRecordSet(@Valid @PathParam("element") ElementId elementId,
											 @Valid DnsRecordSet record) {
		boolean created = service.storeElementDnsRecordSet(elementId, record);
		if(created) {
			return created(messages, record.getDnsRecordSetId());
		}
		return success(messages);
	}

	@POST
	@Path("/{element}/dns")
	public Response storeElementDnsRecordSet(@Valid @PathParam("element") ElementName elementName,
											 @Valid DnsRecordSet record) {
		boolean created = service.storeElementDnsRecordSet(elementName, record);
		if(created) {
			return created(messages, record.getDnsRecordSetId());
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/{element:"+UUID_PATTERN+"}/dns/{dns_id:"+UUID_PATTERN+"}")
	public Response removeElementDnsRecordSet(@Valid @PathParam("element") ElementId elementId,
											  @Valid @PathParam("dns_id") DnsRecordSetId dnsId) {
		service.removeElementDnsRecordSet(elementId, dnsId);
		return success(messages);
	}
	
	@DELETE
	@Path("/{element}/dns/{dns_name}")
	public Response removeElementDnsRecordSet(@Valid @PathParam("element") ElementName elementName,
											  @Valid @PathParam("dns_id") DnsRecordSetId dnsId) {
		service.removeElementDnsRecordSet(elementName, dnsId);
		return success(messages);
	}
	
}
