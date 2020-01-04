/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.DnsZoneElements;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.DnsZoneService;
import io.leitstand.inventory.service.DnsZoneSettings;

@RequestScoped
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/dns")
public class DnsZoneResource {

	@Inject
	private Messages messages;
	
	@Inject
	private DnsZoneService service;
	
	
	@GET
	@Path("/zones")
	public List<DnsZoneSettings> getDnsZones(@QueryParam("filter") @DefaultValue(".*") String filter){
		return service.getDnsZones(filter, 0, 1000);
	}
	
	@GET
	@Path("/zones/{zone:"+UUID_PATTERN+"}/settings")
	public DnsZoneSettings getDnsZoneSettings(@Valid @PathParam("zone") DnsZoneId zoneId){
		return service.getDnsZoneSettings(zoneId);
	}

	@GET
	@Path("/zones/{zone}/settings")
	public DnsZoneSettings getDnsZoneSettings(@Valid @PathParam("zone") DnsZoneName zoneName){
		return service.getDnsZoneSettings(zoneName);
	}
	
	@GET
	@Path("/zones/{zone:"+UUID_PATTERN+"}/elements")
	public DnsZoneElements getDnsZoneElements(@Valid @PathParam("zone") DnsZoneId zoneId){
		return service.getDnsZoneElements(zoneId);
	}

	@GET
	@Path("/zones/{zone}/elements")
	public DnsZoneElements getDnsZoneElements(@Valid @PathParam("zone") DnsZoneName zoneName){
		return service.getDnsZoneElements(zoneName);
	}
	
	@PUT
	@Path("/zones/{zone:"+UUID_PATTERN+"}/settings")
	public Response storeDnsZoneSettings(@Valid @PathParam("zone") DnsZoneId zoneId,
									     @Valid DnsZoneSettings settings){
		if(isDifferent(zoneId,settings.getDnsZoneId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "dns_zone_id", 
												   zoneId, 
												   settings.getDnsZoneId());
		}
		boolean created = service.storeDnsZoneSettings(settings);
		if(created) {
			return created(messages, zoneId);
		}
		return success(messages);
	}
	
	
	@POST
	@Path("/zones")
	public Response storeDnsZoneSettings(@Valid DnsZoneSettings settings){
		boolean created = service.storeDnsZoneSettings(settings);
		if(created) {
			return created(messages, settings.getDnsZoneId());
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/zones/{zone:"+UUID_PATTERN+"}")
	public Response removeDnsZone(@Valid @PathParam("zone") DnsZoneId zoneId,
								  @QueryParam("force") boolean force){
		if(force) {
			service.forceRemoveDnsZone(zoneId);
		} else {
			service.removeDnsZone(zoneId);
		}
		return success(messages);
	}
	
	@DELETE
	@Path("/zones/{zone}")
	public Response removeDnsZone(@Valid @PathParam("zone") DnsZoneName zoneName,
								  @QueryParam("force") boolean force){
		if(force) {
			service.forceRemoveDnsZone(zoneName);
		} else {
			service.removeDnsZone(zoneName);
		}
		return success(messages);
	}
	
}