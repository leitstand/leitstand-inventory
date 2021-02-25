package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

public class ImageStatisticsElementGroupImageCount extends BaseElementGroupEnvelope{

    public static Builder newElementGroupImageCount() {
        return new Builder();
    }
    
    public static class Builder extends BaseElementGroupEnvelopeBuilder<ImageStatisticsElementGroupImageCount,Builder>{
        
        protected Builder() {
            super(new ImageStatisticsElementGroupImageCount());
        }
        
        public Builder withPullCount(int pullCount) {
            assertNotInvalidated(getClass(), object);
            object.pullCount = pullCount;
            return this;
        }

        public Builder withActiveCount(int activeCount) {
            assertNotInvalidated(getClass(), object);
            object.activeCount = activeCount;
            return this;
        }

        public Builder withCachedCount(int cachedCount) {
            assertNotInvalidated(getClass(), object);
            object.cachedCount = cachedCount;
            return this;
        }
        
    }
    
    private int pullCount;
    private int activeCount;
    private int cachedCount;

    public int getPullCount() {
        return pullCount;
    }
    
    public int getActiveCount() {
        return activeCount;
    }
    
    public int getCachedCount() {
        return cachedCount;
    }
    
    public int getTotalCount() {
        return pullCount + activeCount + cachedCount;
    }
}
