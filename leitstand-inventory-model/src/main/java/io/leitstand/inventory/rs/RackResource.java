package io.leitstand.inventory.rs;

import static io.leitstand.commons.UniqueKeyConstraintViolationException.key;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.model.RollbackExceptionUtil.givenRollbackException;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_RACK;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static io.leitstand.inventory.service.ElementId.elementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.FacilityId.facilityId;
import static io.leitstand.inventory.service.FacilityName.facilityName;
import static io.leitstand.inventory.service.ReasonCode.IVT0307E_ELEMENT_NAME_ALREADY_IN_USE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
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

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.StringUtil;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackItem;
import io.leitstand.inventory.service.RackItemData;
import io.leitstand.inventory.service.RackItems;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackService;
import io.leitstand.inventory.service.RackSettings;
import io.leitstand.security.auth.Scopes;

@Resource
@Path("/racks")
@Scopes({IVT,IVT_RACK})
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class RackResource {

	@Inject
	private RackService service;
	
	@Inject
	private Messages messages;
	
	@GET
	@Path("_findElement")
	@Scopes({IVT, IVT_RACK, IVT_READ})
	public RackItem findElementRackItem(@QueryParam("element") String element){
		if(element.matches(UUID_PATTERN)) {
			return service.findElementRackItem(elementId(element));
		}
		return service.findElementRackItem(elementName(element));
	}

	
	@GET
	@Scopes({IVT, IVT_RACK, IVT_READ})
	public List<RackSettings> findRacks(@QueryParam("facility") String facility,
										@QueryParam("filter") @DefaultValue("") String filter){
		
		if(isNonEmptyString(facility)) {
			if(facility.matches(UUID_PATTERN)) {
				return service.findRacks(facilityId(facility),filter);
			}
			return service.findRacks(facilityName(facility),filter);
		}
		return service.findRacks(filter);
	}
	
	@GET
	@Scopes({IVT,IVT_RACK, IVT_READ})
	@Path("/{rack:"+UUID_PATTERN+"}/settings")
	public RackSettings getRackSettings(@Valid @PathParam("rack") RackId rackId) {
		return service.getRackSettings(rackId);
	}
	
	@GET
	@Scopes({IVT,IVT_RACK, IVT_READ})
	@Path("/{rack}/settings")
	public RackSettings getRackSettings(@Valid @PathParam("rack") RackName rackName) {
		return service.getRackSettings(rackName);
	}
	
	
	@GET
	@Scopes({IVT,IVT_RACK, IVT_READ})
	@Path("/{rack:"+UUID_PATTERN+"}/items")
	public RackItems getRackItems(@Valid @PathParam("rack") RackId rackId) {
		return service.getRackItems(rackId);
	}
	
	@GET
	@Scopes({IVT,IVT_RACK, IVT_READ})
	@Path("/{rack}/items")
	public RackItems getRackItems(@Valid @PathParam("rack") RackName rackName) {
		return service.getRackItems(rackName);
	}
	
	@GET
	@Scopes({IVT,IVT_RACK, IVT_READ})
	@Path("/{rack:"+UUID_PATTERN+"}/items/{unit}")
	public RackItem getRackItem(@Valid @PathParam("rack") RackId rackId,
								@PathParam("unit") int unit) {
		return service.getRackItem(rackId,unit);
	}
	
	@GET
	@Scopes({IVT,IVT_RACK, IVT_READ})
	@Path("/{rack}/items/{unit}")
	public RackItem getRackItem(@Valid @PathParam("rack") RackName rackName,
								@PathParam("unit") int unit) {
		return service.getRackItem(rackName,unit);
	}

	@PUT
	@Path("/{rack:"+UUID_PATTERN+"}/items/{unit}")
	public Response storeRackItem(@Valid @PathParam("rack") RackId rackId,
								  @PathParam("unit") int unit,
								  @Valid RackItemData item) {
		if(isDifferent(unit, item.getPosition())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE,
												   "unit",
												   unit,
												   item.getPosition());
		}
		
		service.storeRackItem(rackId,item);
		return success(messages);
	}
	
	@PUT
	@Path("/{rack}/items/{unit}")
	public Response storeRackItem(@Valid @PathParam("rack") RackName rackName,
							  	  @PathParam("unit") int unit,
							  	  @Valid RackItemData item) {
		if(isDifferent(unit, item.getPosition())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE,
												   "unit",
												   unit,
												   item.getPosition());
		}
		service.storeRackItem(rackName,item);
		return success(messages);
	}
	
	@DELETE
	@Path("/{rack}/items/{unit}")
	public Response removeRackItem(@Valid @PathParam("rack") RackName rackName,
							       @PathParam("unit") int unit) {
		service.removeRackItem(rackName,unit);
		return success(messages);
	}
	
	@DELETE
	@Path("/{rack:"+UUID_PATTERN+"}/items/{unit}")
	public Response removeRackItem(@Valid @PathParam("rack") RackId rackId,
							       @PathParam("unit") int unit) {
		service.removeRackItem(rackId,unit);
		return success(messages);
	}
	
	@POST
	public Response storeRack(@Valid RackSettings settings) {
		return storeRack(settings.getRackId(),settings);
	}
	
	@PUT
	@Path("/{rack:"+UUID_PATTERN+"}/settings")
	public Response storeRack(@Valid @PathParam("rack") RackId rackId, 
							  @Valid RackSettings settings) {
		if (isDifferent(rackId, settings.getRackId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "rack_id", 
												   rackId, 
												   settings.getRackId());
		}
		
		try {
			if(service.storeRack(settings)){
				return created(messages,"/elements/%s/settings",settings.getRackId());
			}
			return success(messages);
		} catch (PersistenceException e) {
			givenRollbackException(e)
			.whenEntityExists(() -> service.getRackSettings(settings.getRackName()))
			.thenThrow( new UniqueKeyConstraintViolationException(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE,
																  key("element_name", settings.getRackName())));
			throw e;
		}
	}
	
	@PUT
	@Path("/{rack}/settings")
	public Response storeRack(@Valid @PathParam("rack") RackName rackName,
							  @Valid RackSettings settings) {
		try {
			if(service.storeRack(settings)){
				return created(messages,"/elements/%s/settings",settings.getRackId());
			}
			return success(messages);
		} catch (PersistenceException e) {
			givenRollbackException(e)
			.whenEntityExists(() -> service.getRackSettings(settings.getRackName()))
			.thenThrow( new UniqueKeyConstraintViolationException(IVT0307E_ELEMENT_NAME_ALREADY_IN_USE,
																  key("element_name", settings.getRackName())));
			throw e;
		}
	}
	
	@DELETE
	@Path("/{rack:"+UUID_PATTERN+"}")
	public Response removeRack(@Valid @PathParam("rack") RackId rackId, 
							   @QueryParam("force") boolean force) {
		if(force) {
			service.forceRemoveRack(rackId);
			return success(messages);
		} 
		service.removeRack(rackId);
		return success(messages);
	}
	
	@DELETE
	@Path("/{rack}")
	public Response removeRack(@Valid @PathParam("rack") RackName rackName, 
							   @QueryParam("force") boolean force) {
		if(force) {
			service.forceRemoveRack(rackName);
			return success(messages);
		} 
		service.removeRack(rackName);
		return success(messages);
	}

}
