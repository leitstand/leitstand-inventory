package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;

import io.leitstand.commons.model.ValueObject;

/**
 * A reference to a release.
 */
public class ReleaseRef extends ValueObject {
    
    /**
     * Creates a builder for an immutable <code>ReleaseRef</code> value object.
     * @return a builder for an immutable <code>ReleaseRef</code> value object.
     */
    public static Builder newReleaseReference() {
        return new Builder();
    }
    
    /**
     * A builder for an immutable <code>ReleaseRef</code> value object.
     */
    public static class Builder {
        
        private ReleaseRef release = new ReleaseRef();
        
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
         * Creates an immutable <code>ReleaseRef</code> value object and invalidates this builder.
         * Subsequent invocations of the <code>build()</code> method raise an exception.
         * @return an immutable <code>ReleaseRef</code> value object.
         */
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
    
}
