package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseState.CANDIDATE;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

public class ReleaseSettings extends ValueObject {
    
    
    public static Builder newReleaseSettings() {
        return new Builder();
    }
    
    public static class Builder {
        
        private ReleaseSettings release = new ReleaseSettings();
        
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
        
        public Builder withDescription(String description) {
            assertNotInvalidated(getClass(), release);
            release.description = description;
            return this;
        }
        
        public Builder withImages(ReleaseImage.Builder... images) {
            return withImages(stream(images)
                              .map(ReleaseImage.Builder::build)
                              .collect(toList()));
        }
        
        public Builder withImages(List<ReleaseImage> images) {
            assertNotInvalidated(getClass(), release);
            release.images = new ArrayList<>(images);
            return this;
        }
        
        public ReleaseSettings build() {
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
    private ReleaseState releaseState = CANDIDATE;
    private String description;
    private List<ReleaseImage> images = emptyList();
    
    public ReleaseId getReleaseId() {
        return releaseId;
    }
    
    public ReleaseName getReleaseName() {
        return releaseName;
    }
    
    public ReleaseState getReleaseState() {
        return releaseState;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<ReleaseImage> getImages() {
        return unmodifiableList(images);
    }
    
}
