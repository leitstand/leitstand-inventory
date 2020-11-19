package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;

import java.util.List;

import io.leitstand.commons.model.ValueObject;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.Version;

public class ReleaseImage extends ValueObject {

    public static Builder newReleaseImage() {
        return new Builder();
    }
    
    public static class Builder {
        
        private ReleaseImage image = new ReleaseImage();
        
        public Builder withElementRoles(ElementRoleName... roleNames) {
            return withElementRoles(asList(roleNames));
        }

        public Builder withElementRoles(List<ElementRoleName> roleNames) {
            assertNotInvalidated(getClass(), image);
            image.elementRoles = roleNames;
            return this;
        }

        
        public Builder withPlatformChipset(PlatformChipsetName chipsetName) {
            assertNotInvalidated(getClass(), image);
            image.platformChipset = chipsetName;
            return this;
        }
        
        public Builder withImageId(ImageId imageId) {
            assertNotInvalidated(getClass(), image);
            image.imageId = imageId;
            return this;
        }

        public Builder withImageName(ImageName imageName) {
            assertNotInvalidated(getClass(), image);
            image.imageName = imageName;
            return this;
        }
        
        public Builder withImageVersion(Version imageVersion) {
            assertNotInvalidated(getClass(), image);
            image.imageVersion = imageVersion;
            return this;
        }
        
        public Builder withImageState(ImageState imageState) {
            assertNotInvalidated(getClass(), image);
            image.imageState = imageState;
            return this;
        }
        
        public Builder withImageType(ImageType imageType) {
            assertNotInvalidated(getClass(), image);
            image.imageType = imageType;
            return this;
        }
        
        public ReleaseImage build() {
            try {
                assertNotInvalidated(getClass(), image);
                return image;
            } finally {
                this.image = null;
            }
        }
        
        
    }
    
    
    private List<ElementRoleName> elementRoles;
    private PlatformChipsetName platformChipset;
    private ImageId imageId;
    private ImageName imageName;
    private Version imageVersion;
    private ImageType imageType;
    private ImageState imageState;
    
    public ImageId getImageId() {
        return imageId;
    }
    
    public ImageName getImageName() {
        return imageName;
    }
    
    public ImageState getImageState() {
        return imageState;
    }
    
    public ImageType getImageType() {
        return imageType;
    }
    
    public Version getImageVersion() {
        return imageVersion;
    }
    
    public PlatformChipsetName getPlatformChipset() {
        return platformChipset;
    }
    
    public List<ElementRoleName> getElementRoles() {
        return elementRoles;
    }
    
}
