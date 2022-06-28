package io.leitstand.inventory.model;

import java.util.List;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseService;
import io.leitstand.inventory.service.ReleaseSettings;

@Service
public class DefaultReleaseService implements ReleaseService {

    @Inject
    private ReleaseManager manager;
    
    @Inject
    private ReleaseProvider releases;
    
    public DefaultReleaseService() {
        // CDI
    }
    
    protected DefaultReleaseService(ReleaseProvider releases,
                                    ReleaseManager manager) {
        this.manager = manager;
        this.releases = releases;
    }
    
    @Override
    public List<ReleaseSettings> findReleases(String filter) {
        return manager.findReleases(filter);
    }

    @Override
    public ReleaseSettings getRelease(ReleaseId releaseId) {
        return manager.getRelease(releaseId);
    }

    @Override
    public ReleaseSettings getRelease(ReleaseName releaseName) {
        return manager.getRelease(releaseName);
    }

    @Override
    public boolean storeRelease(ReleaseSettings settings) {
        return manager.storeRelease(settings);
    }

    @Override
    public void removeRelease(ReleaseId releaseId) {
        Release release = releases.tryFetchRelease(releaseId);
        if(release != null) {
            manager.removeRelease(release);
        }
    }
    @Override
    public void removeRelease(ReleaseName releaseName) {
        Release release = releases.tryFetchRelease(releaseName);
        if(release != null) {
            manager.removeRelease(release);
        }        
    }

}
