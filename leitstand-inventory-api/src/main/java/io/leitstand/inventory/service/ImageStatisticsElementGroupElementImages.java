package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

/**
 * The image lifecycle state of an image for all elements in an element group.
 */
public class ImageStatisticsElementGroupElementImages extends BaseElementGroupEnvelope {

    /**
     * Creates a builder for an immutable <code>ImageStatisticsElementGroupElementImages</code> value object.
     * @return a builder for an immutable <code>ImageStatisticsElementGroupElementImages</code> value object.
     */
    public static Builder newElementGroupElementImages() {
        return new Builder();
    }
    
    /**
     * A builder for an immutable <code>ImageStatisticsElementGroupElementImages</code> value object.
     */
    public static class Builder extends BaseElementGroupEnvelopeBuilder<ImageStatisticsElementGroupElementImages, Builder>{
        
        /**
         * Creates a builder for an immutable <code>ImageStatisticsElementGroupElementImages</code> value object.
         */
        protected Builder() {
            super(new ImageStatisticsElementGroupElementImages());
        }
        
        /**
         * Sets the image metadata.
         * @param image the image metadata.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImage(ImageInfo image) {
            assertNotInvalidated(getClass(),object);
            object.image = image;
            return this;
        }

        /**
         * Sets the image lifecycle state for all elements in the element group.
         * @param elements the image lifecycle state for all elements in the element group. 
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElements(ImageStatisticsElementGroupElementImageState.Builder... elements) {
            return withElements(stream(elements)
                                .map(ImageStatisticsElementGroupElementImageState.Builder::build)
                                .collect(toList()));
        }

        /**
         * Sets the image lifecycle state for all elements in the element group.
         * @param elements the image lifecycle state for all elements in the element group. 
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElements(List<ImageStatisticsElementGroupElementImageState> elements) {
            assertNotInvalidated(getClass(), object);
            object.elements = unmodifiableList(new ArrayList<>(elements));
            return this;
        }
        
    }
    
    
    private ImageInfo image;
    private List<ImageStatisticsElementGroupElementImageState> elements;
    
    
    /**
     * Returns the image lifecycle state for all elements in the element group.
     * @return the image lifecycle state for all elements in the element group.
     */
    public List<ImageStatisticsElementGroupElementImageState> getElements() {
        return unmodifiableList(elements);
    }
    
    /**
     * Returns the image metadata.
     * @return the image metadata.
     */
    public ImageInfo getImage() {
        return image;
    }
    
    
}
