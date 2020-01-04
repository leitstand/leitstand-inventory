/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;


public class ImageReference {

	public static Builder newImageReference(){
		return new Builder();
	}
	
	public static class Builder{
		
		private ImageReference image = new ImageReference();
		
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(), image);
			image.imageId = id;
			return this;
		}
		
		public Builder withElementRole(ElementRoleName role){
			assertNotInvalidated(getClass(), image);
			image.elementRole = role;
			return this;
		}
		
		public Builder withElementName(ElementName name){
			assertNotInvalidated(getClass(), image);
			image.elementName = name;
			return this;
		}
		
		public Builder withPlatform(ElementPlatformInfo platform) {
			assertNotInvalidated(getClass(), image);
			image.platform = platform;
			return this;
		}
		
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), image);
			image.imageName = imageName;
			return this;
		}
		
		public Builder withImageType(ImageType type){
			assertNotInvalidated(getClass(), image);
			image.imageType = type;
			return this;
		}
		
		public Builder withImageState(ImageState state) {
			assertNotInvalidated(getClass(), image);
			image.imageState = state;
			return this;
		}
		
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(), image);
			image.imageVersion = version;
			return this;
		}
		
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.buildDate =  new Date(date.getTime());
			}
			return this;
		}
		
		public ImageReference build(){
			try{
				assertNotInvalidated(getClass(), image);
				return this.image;
			} finally {
				this.image = null;
			}
		}
	}

	private ImageId imageId;
	
	private ElementRoleName elementRole;
	
	private ElementName elementName;

	private ElementPlatformInfo platform;
	
	private ImageName imageName;
	
	private ImageType imageType;
	
	private ImageState imageState;
	
	private Version imageVersion;
	
	private Date buildDate;
	
	public ImageId getImageId() {
		return imageId;
	}
	
	public Version getRevision() {
		return imageVersion;
	}
	
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	public ElementName getElementName(){
		return elementName;
	}
	
	public Date getBuildDate() {
		return buildDate;
	}

	public ElementPlatformInfo getPlatform() {
		return platform;
	}
	
	public ImageName getImageName() {
		return imageName;
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
	public ImageState getImageState() {
		return imageState;
	}
	
	
}
