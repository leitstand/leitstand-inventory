package io.leitstand.inventory.rs;

import static io.leitstand.commons.UniqueKeyConstraintViolationException.key;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.Patterns.UUID_PATTERN;
import static io.leitstand.commons.model.RollbackExceptionUtil.givenRollbackException;
import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.commons.rs.Responses.created;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.service.ReasonCode.IVT0212E_RELEASE_NAME_ALREADY_IN_USE;

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
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseService;
import io.leitstand.inventory.service.ReleaseSettings;

@Resource
@Path("/releases")
public class ReleasesResource {

    @Inject
    private ReleaseService service;
    
    @Inject
    private Messages messages;
    
    
    @GET
    public List<ReleaseSettings> findReleases(@QueryParam("filter") String filter){
        return service.findReleases(filter);
    }
    
    @GET
    @Path("/{release:"+UUID_PATTERN+"}")
    public ReleaseSettings getRelease(@PathParam("release") ReleaseId release) {
        return service.getRelease(release);
    }
    
    @GET
    @Path("/{release}")
    public ReleaseSettings getRelease(@PathParam("release") ReleaseName release) {
        return service.getRelease(release);
    }
    
    @POST
    public Response storeRelease(@Valid ReleaseSettings settings) {
        return storeRelease(settings.getReleaseId(),
                            settings);
    }
    
    @PUT
    @Path("/{release:"+UUID_PATTERN+"}")
    public Response storeRelease(@Valid @PathParam("release") ReleaseId releaseId,
                                 @Valid ReleaseSettings release) {
        
        try {
            if(isDifferent(releaseId, 
                           release.getReleaseId())) {
                throw new UnprocessableEntityException(VAL0003E_IMMUTABLE_ATTRIBUTE , 
                                                       "release_id", 
                                                       releaseId,
                                                       release.getReleaseId());
            }
            boolean created = service.storeRelease(release);
            if(created) {
                return created(messages,
                               releaseId);
            }
            return success(messages);
        } catch(Exception e) {
            givenRollbackException(e)
            .whenEntityExists(() -> service.getRelease(release.getReleaseName()))
            .thenThrow(new UniqueKeyConstraintViolationException(IVT0212E_RELEASE_NAME_ALREADY_IN_USE, 
                                                                 key("release_name", release.getReleaseName())));
            throw e;
        }
    }
    
    @PUT
    @Path("/{release}")
    public Response storeRelease(@Valid @PathParam("release") ReleaseName releaseName,
                                 @Valid ReleaseSettings release) {
        
        try {
            boolean created = service.storeRelease(release);
            if(created) {
                return created(messages,
                               releaseName);
            }
            return success(messages);
        } catch(Exception e) {
            givenRollbackException(e)
            .whenEntityExists(() -> service.getRelease(release.getReleaseName()))
            .thenThrow(new UniqueKeyConstraintViolationException(IVT0212E_RELEASE_NAME_ALREADY_IN_USE, 
                                                                 key("release_name", release.getReleaseName())));
            throw e;
        }
    }
    
    @DELETE
    @Path("/{release:"+UUID_PATTERN+"}")
    public Response removeRelease(@PathParam("release") ReleaseId release) {
        service.removeRelease(release);
        return success(messages);
    }
    
    @DELETE
    @Path("/{release}")
    public Response removeRelease(@PathParam("release") ReleaseName release) {
        service.removeRelease(release);
        return success(messages);
    }
    
}
