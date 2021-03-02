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

/**
 * The settings of a software release.
 */
public class ReleaseSettings extends ValueObject {
    

    /**
     * Creates a builder for an immutable <code>ReleaseSettings</code> value object.
     * @return a builder for an immutable <code>ReleaseSettings</code> value object.
     */
    public static Builder newReleaseSettings() {
        return new Builder();
    }
    
    /**
     * A builder for an immutable <code>ReleaseSettings</code> value object.
     */
    public static class Builder {
        
        private ReleaseSettings release = new ReleaseSettings();
        
        /**
         * Sets the release ID.
         * @param releaseId the release ID.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withReleaseId(ReleaseId releaseId) {
            assertNotInvalidated(getClass(), release);
            release.releaseId = releaseId;
            return this;
        }

        /**
         * Sets the release name.
         * @param releaseName the release name.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withReleaseName(ReleaseName releaseName) {
            assertNotInvalidated(getClass(), release);
            release.releaseName = releaseName;
            return this;
        }
        
        /**
         * Sets the release state.
         * @param releaseState the release state.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withReleaseState(ReleaseState releaseState) {
            assertNotInvalidated(getClass(), release);
            release.releaseState = releaseState;
            return this;
        }
        
        /**
         * Sets the release description.
         * @param description the release description.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withDescription(String description) {
            assertNotInvalidated(getClass(), release);
            release.description = description;
            return this;
        }
        
        /**
         * Sets the release images.
         * @param releaseId the release images.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImages(ReleaseImage.Builder... images) {
            return withImages(stream(images)
                              .map(ReleaseImage.Builder::build)
                              .collect(toList()));
        }
        
        /**
         * Sets the release images.
         * @param releaseId the release images.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImages(List<ReleaseImage> images) {
            assertNotInvalidated(getClass(), release);
            release.images = new ArrayList<>(images);
            return this;
        }
        
        /**
         * Creates an immutable <code>ReleaseSettings</code> value object and invalidates this builder.
         * Subsequent invocations of the <code>build()</code> method raise an exception.
         * @return an immutable <code>ReleaseSettings</code> value object.
         */
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
    
    /**
     * Returns the release ID.
     * @return the release ID.
     */
    public ReleaseId getReleaseId() {
        return releaseId;
    }
    
    /**
     * Returns the release name.
     * @return the release name.
     */
    public ReleaseName getReleaseName() {
        return releaseName;
    }
    
    /**
     * Returns the release state.
     * @return the release state.
     */
    public ReleaseState getReleaseState() {
        return releaseState;
    }
    
    /**
     * Returns the release description.
     * @return the release description. 
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Returns the release images.
     * @return the release images.
     */
    public List<ReleaseImage> getImages() {
        return unmodifiableList(images);
    }
    
}
