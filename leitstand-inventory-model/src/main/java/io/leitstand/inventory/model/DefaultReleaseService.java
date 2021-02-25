package io.leitstand.inventory.model;

import static java.util.Objects.hash;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseService;
import io.leitstand.inventory.service.ReleaseSettings;

@Service
public class DefaultReleaseService implements ReleaseService {

    private static final Logger LOG = Logger.getLogger(DefaultReleaseService.class.getName());
    
    private static class ImageKey {
        
        private ElementRoleName roleName;
        private PlatformChipsetName chipsetName;
        
        ImageKey(ElementRoleName roleName,
                 PlatformChipsetName chipsetName) {
            this.roleName = roleName;
            this.chipsetName = chipsetName;
        }
        
        @Override
        public int hashCode() {
            return hash(roleName,chipsetName);
        }
        
        @Override
        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }
            if(getClass() != o.getClass()) {
                return false;
            }
            ImageKey k = (ImageKey) o;
            return Objects.equals(roleName,k.roleName) 
                   && Objects.equals(chipsetName,k.chipsetName);
        }
        
        @Override
        public String toString() {
            return chipsetName+"-"+roleName;
        }
    }
    
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
