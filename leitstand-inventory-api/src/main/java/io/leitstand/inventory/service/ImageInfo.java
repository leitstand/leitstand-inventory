/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.commons.model.ValueObject;

/**
 * Image metadata information.
 * <p>
 * The image information includes
 * <ul>
 * 	<li>the image ID,</li>
 * 	<li>the image type (e.g. LXC, CONFIG),</li>
 *  <li>the image name (e.g. RBFS),</li>
 * 	<li>the image lifecycle state (e.g. candidate image),</li>
 * 	<li>the image version,</li>
 * 	<li>the build date,</li>
 *  <li>the image checksums,</li>
 * 	<li>the organization that has published the image,</li>
 *	<li> the element roles the image is built for,</li>
 * 	<li>a list of packages shipped with the image and</li>
 * 	<li>a list of supported applications</li>
 * </ul>
 * </p>
 * @see ImageId
 * @see ImageName
 * @see ImageState
 * @see ImageType
 * @see ElementRoleName
 * @see Version
 */
public class ImageInfo extends ValueObject{
	
    /**
     * Creates a builder for an immutable <code>ImageInfo</code> value object.
     * @return a builder for an immutable <code>ImageInfo</code> value object.
     */
	public static Builder newImageInfo(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ImageInfo</code> value object.
	 */
	public static class Builder{
		
		private ImageInfo image= new ImageInfo();
		
		
		/**
		 * Sets the organization that has published the image.
		 * @param org the organization.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org) {
			assertNotInvalidated(getClass(), image);
			image.organization = org;
			return this;
		}
		
		/**
		 * Sets the image ID.
		 * @param imageId the image ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageId(ImageId imageId){
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
		 * Sets the image type.
		 * @param imageType the image type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageType(ImageType imageType){
			assertNotInvalidated(getClass(), image);
			image.imageType = imageType;
			return this;
		}
		
		/**
		 * Sets the image lifecycle state.
		 * @param imageState the image state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageState(ImageState imageState) {
			assertNotInvalidated(getClass(), image);
			image.imageState = imageState;
			return this;
		}
		
		/**
		 * Sets the element roles this image is applicable for.
		 * @param elementRoles the element roles.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementRoles(ElementRoleName... elementRoles){
			return withElementRoles(asList(elementRoles));
		}
		
        /**
         * Sets the element roles this image is applicable for.
         * @param elementRoles the element roles.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withElementRoles(List<ElementRoleName> elementRoles){
			assertNotInvalidated(getClass(), image);
			image.elementRoles = elementRoles;
			return this;
		}
		
		/**
		 * Sets the platform chipset this image is built for.
		 * @param platformChipset the chipset name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPlatformChipset(PlatformChipsetName platformChipset) {
			assertNotInvalidated(getClass(), image);
			image.platformChipset = platformChipset;
			return this;
		}

		/**
		 * Sets the platforms this image can be installed on.
		 * @param platforms the platforms.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPlatforms(PlatformSettings.Builder... platforms) {
			return withPlatforms(stream(platforms)
								 .map(PlatformSettings.Builder::build)
								 .collect(toList()));
		}
		
        /**
         * Sets the platforms this image can be installed on.
         * @param platforms the platforms.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPlatforms(List<PlatformSettings> platforms) {
			assertNotInvalidated(getClass(), image);
			image.platforms = new LinkedList<>(platforms);
			return this;
		}
		
		/**
		 * Sets the optional element name this image is bind to.
		 * A bound image can only be installed on the specified element.
		 * @param elementName the element name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementName(ElementName elementName){
			assertNotInvalidated(getClass(), image);
			image.elementName = elementName;
			return this;
		}
		
		/**
		 * Sets the image file extension.
		 * @param ext the file extension.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withExtension(String ext){
			assertNotInvalidated(getClass(), image);
			image.extension = ext;
			return this;
		}
		
		/**
		 * Sets the image version.
		 * @param version the image version.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(), image);
			image.imageVersion = version;
			return this;
		}
		
		/**
		 * Sets the image build date.
		 * @param date the image build date.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.buildDate =  new Date(date.getTime());
			}
			return this;
		}

		/**
		 * Sets the build ID.
		 * @param buildId the build ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withBuildId(String buildId){
			assertNotInvalidated(getClass(), image);
			image.buildId =  buildId;
			return this;
		}

		/**
		 * Sets the image description.
		 * @param description the description.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(),image);
			image.description = description;
			return this;
		}
		
		/**
		 * Sets the packages shipped with this image.
		 * @param builders the packages shipped with this image.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackages(PackageVersionInfo.Builder...builders ) {
			return withPackages(stream(builders)
							    .map(PackageVersionInfo.Builder::build)
							    .collect(toList()));
		}
		
        /**
         * Sets the packages shipped with this image.
         * @param builders the packages shipped with this image.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPackages(PackageVersionInfo... packages) {
			return withPackages(asList(packages));
		}
		
        /**
         * Sets the packages shipped with this image.
         * @param builders the packages shipped with this image.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPackages(List<PackageVersionInfo> packages){
			assertNotInvalidated(getClass(), image);
			image.packages = unmodifiableList(new LinkedList<>(packages));
			return this;
		}
		
		/**
		 * Sets the image checksums. 
		 * The map key contains the algorithm name and the value contains the checksum.
		 * @param checksums the image checksums.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withChecksums(Map<String,String> checksums){
			assertNotInvalidated(getClass(), image);
			image.checksums = new TreeMap<>(checksums);
			return this;
		}

		/**
		 * Sets the supported applications.
		 * @param applicationNames the application names.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withApplications(ApplicationName... applicationNames) {
			return withApplications(asList(applicationNames));
		}

		/**
		 * Sets the supported applications.
		 * @param applicationNames the application namess
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withApplications(List<ApplicationName> applications) {
			assertNotInvalidated(getClass(), image);
			image.applications = unmodifiableList(new LinkedList<>(applications));
			return this;
		}
		
		/**
		 * Sets the image category.
		 * @param category the image category.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), image);
			image.category = category;
			return this;
		}
		
		/**
		 * Returns an immutable <code>ImageInfo</code> value object and invalidates this builder.
		 * Subsequence invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ImageInfo</code> value object.
		 */
		public ImageInfo build(){
			try{
				assertNotInvalidated(getClass(), image);
				return this.image;
			} finally {
				this.image = null;
			}
		}

	}

	@Valid
	@NotNull(message="{image_id.required}")
	private ImageId imageId = randomImageId();

	private String organization;
	
	private String category;

	@Valid
	@NotNull(message="{image_name.required}")
	private ImageName imageName;

	@Valid
	@NotNull(message="{image_type.required}")
	private ImageType imageType;
	
	@Valid
	@NotNull(message="{image_state.required}")
	private ImageState imageState;
	
	private List<ElementRoleName> elementRoles;
	
	private ElementName elementName;
	
	private String extension;
	
	@JsonbProperty
	@Valid
	@NotNull(message="{image_version.required}")
	private Version imageVersion;
	
	@JsonbTypeAdapter(IsoDateAdapter.class)
	private Date buildDate;
	
	private String buildId;

	@Valid
	private List<PackageVersionInfo> packages = emptyList();
	
	@Valid
	private List<ApplicationName> applications = emptyList();
	
	@Valid
	@NotNull(message="{platform_chipset.required}")
	private PlatformChipsetName platformChipset;
	
	private List<PlatformSettings> platforms = emptyList();

	private String description;
	
	private Map<String,String> checksums = emptyMap();
	
	/**
	 * Returns the image ID.
	 * @return the image ID.
	 */
	public ImageId getImageId() {
		return imageId;
	}
	
	/**
	 * Returns the image version.
	 * @return the image version
	 */
	public Version getImageVersion() {
		return imageVersion;
	}
	
	/**
	 * Returns the element roles this image can be installed on.
	 * @return the element roles this image can be installed on.
	 */
	public List<ElementRoleName> getElementRoles() {
	    if(elementRoles == null) {
	        return emptyList();
	    }
		return unmodifiableList(elementRoles);
	}
	
	/**
	 * Returns the element this image is bound to or <code>null</code> if this image is not bound to an element.
	 * Bound images can only be installed on the specified element.
	 * @return the element name of the element this image is bound to or <code>null</code> if the image is not bound to an alement.
	 */
	public ElementName getElementName(){
		return elementName;
	}
	
	/**
	 * Returns the image build date or <code>null</code> if the build date is unknown.
	 * @return the build date of the image or <code>null</code> if the build date is unknown.
	 */
	public Date getBuildDate() {
		if(buildDate == null) {
			return null;
		}
		return new Date(buildDate.getTime());
	}
	
	/**
	 * Returns the packages shipped with this image.
	 * @return the packages shipped with this image.
	 */
	public List<PackageVersionInfo> getPackages() {
		return unmodifiableList(packages);
	}
	
	/**
	 * Returns the image file extension or <code>null</code> if the file extension is unknown.
	 * @return the image file extension or <code>null</code> if the file extension is unknown.
	 */
	public String getExtension() {
		return extension;
	}
	
	/**
	 * Returns the image type.
	 * @return the image type.
	 */
	public ImageType getImageType() {
		return imageType;
	}
	
	/**
	 * Returns the image name.
	 * @return the image name.
	 */
	public ImageName getImageName() {
		return imageName;
	}

	/**
	 * Returns the organization that has published this image.
	 * @return the organization that has published this image.
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Returns the list of applications supported by this image.
	 * @return the list of applications supported by this image.
	 */
	public List<ApplicationName> getApplications() {
		return unmodifiableList(applications);
	}

	/**
	 * Returns the chipset supported by this image.
	 * @return the supported chipset.
	 */
	public PlatformChipsetName getPlatformChipset() {
		return platformChipset;
	}
		
	/**
	 * Returns the image lifecycle state.
	 * @return the image lifecycle state.
	 */
	public ImageState getImageState() {
		return imageState;
	}

	/**
	 * Returns the build ID.
	 * @return the build ID.
	 */
	public String getBuildId() {
		return buildId;
	}
	
	/**
	 * Returns the image category.
     * @return the image category.
	 */
	public String getCategory() {
		return category;
	}
	
	/**
	 * Returns the image description.
	 * @return the image description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the image checksums.
	 * @return the image checksums.
	 */
	public Map<String,String> getChecksums() {
		return unmodifiableMap(checksums);
	}
	
	/**
	 * Returns the platforms this image can be installed on.
	 * @return the platforms this image can be installed on.
	 */
	public List<PlatformSettings> getPlatforms() {
		return unmodifiableList(platforms);
	}
	
	
}
