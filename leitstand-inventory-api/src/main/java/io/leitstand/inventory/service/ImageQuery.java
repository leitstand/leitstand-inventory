package io.leitstand.inventory.service;

public class ImageQuery {

    public static ImageQuery newQuery() {
        return new ImageQuery();
    }
    
    private ElementRoleName elementRole;
    private ImageState imageState;
    private ImageType imageType;
    private Version imageVersion;
    private PlatformChipsetName platformChipset;
    private String filter;
    private int limit;
    
    
    public ImageQuery roleName(ElementRoleName elementRole) {
        this.elementRole = elementRole;
        return this;
    }
 
    public ImageQuery imageState(ImageState imageState) {
        this.imageState = imageState;
        return this;
    }
    
    public ImageQuery imageType(ImageType imageType) {
        this.imageType = imageType;
        return this;
    }
    
    public ImageQuery imageVersion(Version imageVersion) {
        this.imageVersion = imageVersion;
        return this;
    }
    
    public ImageQuery platformChipset(PlatformChipsetName chipsetName) {
        this.platformChipset = chipsetName;
        return this;
    }
       
    public ImageQuery filter(String filter) {
        this.filter = filter;
        return this;
    }
    
    public ImageQuery limit(int limit) {
        this.limit = limit;
        return this;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public String getFilter() {
        return filter;
    }
    
    public ImageState getImageState() {
        return imageState;
    }
    
    public ImageType getImageType() {
        return imageType;
    }
    
    public PlatformChipsetName getPlatformChipset() {
        return platformChipset;
    }
    
    public ElementRoleName getElementRole() {
        return elementRole;
    }
    
    public Version getImageVersion() {
        return imageVersion;
    }
}
