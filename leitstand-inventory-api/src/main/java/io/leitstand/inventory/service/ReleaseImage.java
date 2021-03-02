package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import io.leitstand.commons.model.ValueObject;

/**
 * An image shipped with a release.
 * @see ReleaseSettings
 */
public class ReleaseImage extends ValueObject {

    /**
     * Returns a builder for an immutable <code>ReleaseImage</code> value object.
     * @return a builder for an immutable <code>ReleaseImage</code> value object.
     */
    public static Builder newReleaseImage() {
        return new Builder();
    }
    
    /**
     * A builder for an immutable <code>ReleaseImage</code> value object.
     */
    public static class Builder {
        
        private ReleaseImage image = new ReleaseImage();
        
        /**
         * Sets the element roles supported by the image.
         * @param roleNames the element roles.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementRoles(ElementRoleName... roleNames) {
            return withElementRoles(asList(roleNames));
        }

        /**
         * Sets the element roles supported by the image.
         * @param roleNames the element roles.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementRoles(List<ElementRoleName> roleNames) {
            assertNotInvalidated(getClass(), image);
            image.elementRoles = roleNames;
            return this;
        }

        /**
         * Sets the platform supported by the image.
         * @param chipsetName the platform chipset.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withPlatformChipset(PlatformChipsetName chipsetName) {
            assertNotInvalidated(getClass(), image);
            image.platformChipset = chipsetName;
            return this;
        }
        
        /**
         * Sets the image ID.
         * @param imageId the image ID.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImageId(ImageId imageId) {
            assertNotInvalidated(getClass(), image);
            image.imageId = imageId;
            return this;
        }
        
        /**
         * Sets the image name.
         * @param imageName the image name.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImageName(ImageName imageName) {
            assertNotInvalidated(getClass(), image);
            image.imageName = imageName;
            return this;
        }

        /**
         * Sets the image version.
         * @param imageVersion the image version.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImageVersion(Version imageVersion) {
            assertNotInvalidated(getClass(), image);
            image.imageVersion = imageVersion;
            return this;
        }
        
        /**
         * Sets the image lifecycle state.
         * @param imageName the image lifecycle state.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImageState(ImageState imageState) {
            assertNotInvalidated(getClass(), image);
            image.imageState = imageState;
            return this;
        }
        
        /**
         * Sets the image type.
         * @param imageType the image type.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withImageType(ImageType imageType) {
            assertNotInvalidated(getClass(), image);
            image.imageType = imageType;
            return this;
        }
        
        /**
         * Returns the immutable <code>ReleaseImage</code> value object and invalidates this builder.
         * Subsequent invocations of the <code>build()</code> method raise an exception.
         * @return a reference to this builder to continue object creation.
         */
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
    
    /**
     * Returns the image ID.
     * @return the image ID.
     */
    public ImageId getImageId() {
        return imageId;
    }
    
    /** 
     * Returns the image name.
     * @return the image name.
     */
    public ImageName getImageName() {
        return imageName;
    }
    
    /**
     * Returns the image lifecycle state.
     * @return the image lifecycle state.
     */
    public ImageState getImageState() {
        return imageState;
    }
    
    /**
     * Returns the image type.
     * @return the image type.
     */
    public ImageType getImageType() {
        return imageType;
    }
    
    /**
     * Returns the image version.
     * @return the image version.
     */
    public Version getImageVersion() {
        return imageVersion;
    }
    
    /**
     * Returns the platform chipset name.
     * @return the platform chipset name.
     */
    public PlatformChipsetName getPlatformChipset() {
        return platformChipset;
    }
    
    /**
     * Returns the supported element roles.
     * @return the supported element roles.
     */
    public List<ElementRoleName> getElementRoles() {
        return unmodifiableList(elementRoles);
    }
    
}
