package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Release.findReleaseById;
import static io.leitstand.inventory.model.Release.findReleaseByName;
import static java.lang.String.format;
import static net.rtbrick.rbms.release.service.ReasonCode.REL0001E_RELEASE_NOT_FOUND;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import net.rtbrick.rbms.release.service.ReleaseId;
import net.rtbrick.rbms.release.service.ReleaseName;

@Dependent
public class ReleaseProvider {

    private static final Logger LOG = Logger.getLogger(ReleaseProvider.class.getName());
    
    @Inject
    @Inventory
    private Repository repository;
    
    public Release tryFetchRelease(ReleaseId releaseId) {
        return repository.execute(findReleaseById(releaseId));
    }
    
    public Release tryFetchRelease(ReleaseName releaseName) {
        return repository.execute(findReleaseByName(releaseName));
    }
    
    public Release fetchRelease(ReleaseId releaseId) {
        Release release = tryFetchRelease(releaseId);
        if(release == null) {
            LOG.fine(() -> format("%s; Release %s not found",
                     REL0001E_RELEASE_NOT_FOUND, releaseId));
            throw new EntityNotFoundException(REL0001E_RELEASE_NOT_FOUND, releaseId);
        }
        return release;
    }
    
    public Release fetchRelease(ReleaseName releaseName) {
        Release release = tryFetchRelease(releaseName);
        if(release == null) {
            LOG.fine(() -> format("%s; Release %s not found",
                     REL0001E_RELEASE_NOT_FOUND, releaseName));
            throw new EntityNotFoundException(REL0001E_RELEASE_NOT_FOUND, releaseName);
        }
        return release;    }
    
}
