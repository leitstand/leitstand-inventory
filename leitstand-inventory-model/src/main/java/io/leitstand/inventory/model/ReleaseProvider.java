package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Release.findReleaseById;
import static io.leitstand.inventory.model.Release.findReleaseByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0210E_RELEASE_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;

@Dependent
public class ReleaseProvider {

    private static final Logger LOG = Logger.getLogger(ReleaseProvider.class.getName());
    
    @Inject
    @Inventory
    private Repository repository;
    
    protected ReleaseProvider() {
        // CDI
    }
    
    protected ReleaseProvider(Repository repository) {
        this.repository = repository;
    }
    
    public Release tryFetchRelease(ReleaseId releaseId) {
        return repository.execute(findReleaseById(releaseId));
    }
    
    public Release tryFetchRelease(ReleaseName releaseName) {
        return repository.execute(findReleaseByName(releaseName));
    }
    
    public Release fetchRelease(ReleaseId releaseId) {
        Release release = tryFetchRelease(releaseId);
        if(release == null) {
            LOG.fine(() -> format("%s: Release %s not found",
                                  IVT0210E_RELEASE_NOT_FOUND, 
                                  releaseId));
            throw new EntityNotFoundException(IVT0210E_RELEASE_NOT_FOUND, 
                                              releaseId);
        }
        return release;
    }
    
    public Release fetchRelease(ReleaseName releaseName) {
        Release release = tryFetchRelease(releaseName);
        if(release == null) {
            LOG.fine(() -> format("%s: Release %s not found",
                                  IVT0210E_RELEASE_NOT_FOUND, 
                                  releaseName));
            throw new EntityNotFoundException(IVT0210E_RELEASE_NOT_FOUND, 
                                              releaseName);
        }
        return release;    
    }
    
}
