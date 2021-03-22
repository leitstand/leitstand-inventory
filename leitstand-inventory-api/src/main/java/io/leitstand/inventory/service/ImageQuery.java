package io.leitstand.inventory.service;

/**
 * A query for images.
 * <code>
 * The <code>ImageQuery</code> allows filtering images by a combination of 
 * <ul>
 *  <li>image type,</li>
 *  <li>image name,</li>
 *  <li>image version,</li>
 *  <li>image lifecycle state,</li>
 *  <li>platform chipset and </li>
 *  <li>element role</li>.
 * </ul>
 * 
 * element role, image type,  image lifecycle state, image name, image version, platform chipset
 */
public class ImageQuery {

    /**
     * Creates a new image query.
     * @return the image query.
     */
    public static ImageQuery newQuery() {
        return new ImageQuery();
    }
    
    private ElementRoleName elementRole;
    private ImageState imageState;
    private ImageType imageType;
    private Version imageVersion;
    private PlatformChipsetName platformChipset;
    private String filter;
    private int limit = 100;
    
    
    /**
     * Sets the element role name.
     * @param elementRole the element role name.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery roleName(ElementRoleName elementRole) {
        this.elementRole = elementRole;
        return this;
    }

    /**
     * Sets the image lifecycle state.
     * @param imageState the image lifecycle state.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery imageState(ImageState imageState) {
        this.imageState = imageState;
        return this;
    }

    /**
     * Sets the image type.
     * @param imageType the image type.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery imageType(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }

    /**
     * Sets the image version.
     * @param imageVersion the image version.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery imageVersion(Version imageVersion) {
        this.imageVersion = imageVersion;
        return this;
    }
    
    /**
     * Sets the platform chipset.
     * @param platformChipset the platform chipset.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery platformChipset(PlatformChipsetName platformChipset) {
        this.platformChipset = platformChipset;
        return this;
    }
       
    /**
     * Sets a regular expression to filter for image names.
     * @param filter the image name pattern.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery filter(String filter) {
        this.filter = filter;
        return this;
    }
    
    /**
     * Sets the maximum number of returned matching images.
     * @param limit the maximum number of returned matching images.
     * @return a reference to this query to continue query composition.
     */
    public ImageQuery limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    /**
     * Returns the maximum number of returned matching images.
     * @return the maximum number of returned matching images.
     */
    public int getLimit() {
        return limit;
    }
    
    /**
     * Returns the regular expression to filter image names or
     * <code>null</code> if no image name filter is applied.
     * @return the regular expression to filter image names or
     * <code>null</code> if no image name filter is applied.
     */
    public String getFilter() {
        return filter;
    }
    
    /**
     * Returns the image lifecycle state filter or 
     * <code>null</code> if no lifecycle state filter is applied.
     * @return the image lifecycle state filter or 
     * <code>null</code> if no lifecycle state filter is applied.
     */
    public ImageState getImageState() {
        return imageState;
    }
    
    /**
     * Returns the image type filter or 
     * <code>null</code> if no image type filter is applied.
     * @return the image type filter or 
     * <code>null</code> if no image type filter is applied.
     */
    public ImageType getImageType() {
        return imageType;
    }
    
    /**
     * Returns the image platform chipset filter or 
     * <code>null</code> if no platform chipset filter is applied.
     * @return the image platform chipset filter or 
     * <code>null</code> if no platform chipset filter is applied.
     */
    public PlatformChipsetName getPlatformChipset() {
        return platformChipset;
    }
    
    /**
     * Returns the element role filter or 
     * <code>null</code> if no element role filter is applied.
     * @return the image element role filter or 
     * <code>null</code> if no element role filter is applied.
     */
    public ElementRoleName getElementRole() {
        return elementRole;
    }
    
    /**
     * Returns the image version filter or 
     * <code>null</code> if no image version is applied.
     * @return the image version filter or 
     * <code>null</code> if no image version filter is applied.
     */
    public Version getImageVersion() {
        return imageVersion;
    }
}
