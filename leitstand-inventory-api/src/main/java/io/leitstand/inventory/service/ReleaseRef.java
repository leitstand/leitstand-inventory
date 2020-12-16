package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;

import io.leitstand.commons.model.ValueObject;

public class ReleaseRef extends ValueObject {
    
    
    public static Builder newReleaseSettings() {
        return new Builder();
    }
    
    public static class Builder {
        
        private ReleaseRef release = new ReleaseRef();
        
        public Builder withReleaseId(ReleaseId releaseId) {
            assertNotInvalidated(getClass(), release);
            release.releaseId = releaseId;
            return this;
        }
        
        public Builder withReleaseName(ReleaseName releaseName) {
            assertNotInvalidated(getClass(), release);
            release.releaseName = releaseName;
            return this;
        }
        
        public Builder withReleaseState(ReleaseState releaseState) {
            assertNotInvalidated(getClass(), release);
            release.releaseState = releaseState;
            return this;
        }
        
        public ReleaseRef build() {
            try {
                assertNotInvalidated(getClass(), release);
                return release;
            } finally {
                this.release = null;
            }
        }
    }
    
    private ReleaseId releaseId = randomReleaseId();
    private ReleaseName releaseName;
    private ReleaseState releaseState;
    
    public ReleaseId getReleaseId() {
        return releaseId;
    }
    
    public ReleaseName getReleaseName() {
        return releaseName;
    }
    
    public ReleaseState getReleaseState() {
        return releaseState;
    }
    
}
