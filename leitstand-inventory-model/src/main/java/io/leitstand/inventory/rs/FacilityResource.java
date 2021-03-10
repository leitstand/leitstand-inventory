package io.leitstand.inventory.rs;

import static io.leitstand.commons.UniqueKeyConstraintViolationException.key;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.model.RollbackExceptionUtil.givenRollbackException;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.service.ReasonCode.IVT0603E_FACILITY_NAME_ALREADY_IN_USE;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityService;
import io.leitstand.inventory.service.FacilitySettings;

@Resource
@Path("/facilitys")
public class FacilityResource {

	@Inject
	private FacilityService service;
	
	@Inject
	private Messages messages;
	
	@GET
	public List<FacilitySettings> getFacilities(@QueryParam("filter") String name){
		return service.findFacilities(name);
	}
	
	@GET
	@Path("/{facility:"+UUID_PATTERN+"}")
	public FacilitySettings getFacility(@Valid @PathParam("facility") FacilityId facility) {
		return service.getFacility(facility);
	}
	
	@GET
	@Path("/{facility}")
	public FacilitySettings getFacility(@Valid @PathParam("facility") FacilityName facility) {
		return service.getFacility(facility);
	}
	
	@POST
	public Response storeFacility(@Valid FacilitySettings settings) {
        try {
            boolean created = service.storeFacility(settings);
            if(created) {
                return created(messages,settings.getFacilityId());
            }
            return success(messages);
        } catch (Exception e) {
            givenRollbackException(e)
            .whenEntityExists(() -> service.getFacility(settings.getFacilityName()))
            .thenThrow(new UniqueKeyConstraintViolationException(IVT0603E_FACILITY_NAME_ALREADY_IN_USE, 
                                                                 key("facility_name", 
                                                                     settings.getFacilityName())));
            
            throw e;
        }	
    }
	
	@PUT
	@Path("/{facility:"+UUID_PATTERN+"}")
	public Response storeFacility(@PathParam("facility") FacilityId facilityId,
								  @Valid FacilitySettings settings) {
		if(isDifferent(facilityId, settings.getFacilityId())) {
			throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE, 
												   "facility_id", 
												   facilityId, 
												   settings.getFacilityId());
		} 
		
		return storeFacility(settings);

	}
	
	@PUT
	@Path("/{facility}")
	public Response storeFacility(@Valid @PathParam("facility") FacilityName facilityName, 
								  @Valid FacilitySettings settings) {
	    return storeFacility(settings);
	}
	
	@DELETE
	@Path("/{facility:"+UUID_PATTERN+"}")
	public Response removeFacility(@PathParam("facility") FacilityId facilityId) {
		service.removeFacility(facilityId);
		return success(messages);
	}	
	
	@DELETE
	@Path("/{facility}")
	public Response removeFacility(@Valid @PathParam("facility") FacilityName facilityName) {
		service.removeFacility(facilityName);
		return success(messages);
	}
	
	
}
