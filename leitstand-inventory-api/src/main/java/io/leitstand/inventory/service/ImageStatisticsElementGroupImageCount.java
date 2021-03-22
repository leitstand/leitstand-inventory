package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

/**
 * Counts image usage in an element group, grouped by element image lifecycle state.
 */
public class ImageStatisticsElementGroupImageCount extends BaseElementGroupEnvelope{

    /**
     * Creates a builder for an immutable <code>ElementGroupImageCount</code> value object.
     * @return a builder for an immutable <code>ElementGroupImageCount</code> value object.
     */
    public static Builder newElementGroupImageCount() {
        return new Builder();
    }
    
    /**
     * A builder for an immutable <code>ElementGroupImageCount</code> value object.
     */
    public static class Builder extends BaseElementGroupEnvelopeBuilder<ImageStatisticsElementGroupImageCount,Builder>{
        
        /**
         * Creates a builder for an immutable <code>ElementGroupImageCount</code> value object.
         */
        protected Builder() {
            super(new ImageStatisticsElementGroupImageCount());
        }
        
        /**
         * Sets the pull count, i.e. the number of elements pulling the image.
         * @param pullCount the pull count.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withPullCount(int pullCount) {
            assertNotInvalidated(getClass(), object);
            object.pullCount = pullCount;
            return this;
        }

        /**
         * Sets the active count, i.e. the number of elements running the image.
         * @param activeCount the active count.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withActiveCount(int activeCount) {
            assertNotInvalidated(getClass(), object);
            object.activeCount = activeCount;
            return this;
        }

        /**
         * Sets the cached count, i.e. the number of elements having a local copy of this image.
         * @param cachedCount the cache count.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withCachedCount(int cachedCount) {
            assertNotInvalidated(getClass(), object);
            object.cachedCount = cachedCount;
            return this;
        }
        
    }
    
    private int pullCount;
    private int activeCount;
    private int cachedCount;

    /**
     * Returns the pull count, i.e. the number of elements pulling the image.
     * @return the pull count.
     */
    public int getPullCount() {
        return pullCount;
    }
    
    /**
     * Returns the active count, i.e. the number of elements running the image.
     * @return the active count.
     */
    public int getActiveCount() {
        return activeCount;
    }
    
    /**
     * Returns the cached count, i.e. the number of elements having a local copy of this image.
     * @return the cached count.
     */
    public int getCachedCount() {
        return cachedCount;
    }
    
    /**
     * Returns the total number of elements using this image.
     * @return the total number of elements using this image.
     */
    public int getTotalCount() {
        return pullCount + activeCount + cachedCount;
    }
}
