package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

public class ImageStatisticsElementGroupElementImages extends BaseElementGroupEnvelope {

    public static Builder newElementGroupElementImages() {
        return new Builder();
    }
    
    public static class Builder extends BaseElementGroupEnvelopeBuilder<ImageStatisticsElementGroupElementImages, Builder>{
        
        protected Builder() {
            super(new ImageStatisticsElementGroupElementImages());
        }
        
        public Builder withImage(ImageInfo image) {
            assertNotInvalidated(getClass(),object);
            object.image = image;
            return this;
        }

        public Builder withElement(ImageStatisticsElementGroupElementImageState.Builder... elements) {
            return withElements(stream(elements)
                                .map(ImageStatisticsElementGroupElementImageState.Builder::build)
                                .collect(toList()));
        }
        
        public Builder withElements(List<ImageStatisticsElementGroupElementImageState> elements) {
            assertNotInvalidated(getClass(), object);
            object.elements = unmodifiableList(new ArrayList<>(elements));
            return this;
        }
        
    }
    
    
    private ImageInfo image;
    private List<ImageStatisticsElementGroupElementImageState> elements;
    
    
    public List<ImageStatisticsElementGroupElementImageState> getElements() {
        return elements;
    }
    
    public ImageInfo getImage() {
        return image;
    }
    
    
}
