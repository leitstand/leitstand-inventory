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
import static io.leitstand.inventory.service.ImageState.REVOKED;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.commons.model.ValueObject;

/**
 * The <code>ImageInfo</code> provides a comprehensive set of information of a stored image.
 * <p>
 * The information includes, but is not limited to 
 * <ul>
 * 	<li>the image ID</li>
 * 	<li>the image type (e.g. LXC, CONFIG)</li>
 *  <li>the image name (e.g. RBFS)
 * 	<li>the image state (e.g. candidate image)</li>
 * 	<li>the organization that has issued the image,</li>
 * 	<li>the build date, </li>
 *  <li>the image checksum, </li>
 * 	<li>the images shipped with the image, </li>
 * 	<li>the supported applications,</li>
 * 	<li>the image version and </li>
 *	<li> the element role the image is built for.</li>
 * </ul>
 * </p>
 * The {@link ImageId}, image name and version are mandatory.
 * @see ImageId
 * @see ImageName
 * @see ImageState
 * @see ImageType
 * @see ElementRoleName
 * @see Version
 */

public class ImageInfo extends ValueObject{
	
	public static boolean isRevokedImage(ImageInfo image) {
		return image.getImageState() == REVOKED;
	}
	
	public static boolean isUnrevokedImage(ImageInfo image) {
		return image.getImageState() != REVOKED;
	}

	
	/**
	 * Returns a new builder to create an immutable <code>ImageInfo</code> instance.
	 * @return a new <code>ImageInfo</code> builder.
	 */
	public static Builder newImageInfo(){
		return new Builder();
	}
	
	/**
	 * The builder for an immutable <code>ImageInfo</code> instance.
	 */
	public static class Builder{
		
		private ImageInfo image= new ImageInfo();
		
		
		/**
		 * Sets the organization that has issued the image.
		 * The organization is specified as reversed domain (e.g. net.rtbrick)
		 * @param org the organization
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withOrganization(String org) {
			assertNotInvalidated(getClass(), image);
			image.organization = org;
			return this;
		}
		
		/**
		 * Sets the mandatory {@link ImageId}.
		 * @param id the image id.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(), image);
			image.imageId = id;
			return this;
		}
		
		/**
		 * Sets the mandatory {@link ImageName}.
		 * @param imageName the image name.
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withImageName(ImageName imageName) {
			assertNotInvalidated(getClass(), image);
			image.imageName = imageName;
			return this;
		}
		
		/**
		 * Sets the mandatory {@link ImageType}.
		 * @param imageType the image type
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImageType(ImageType imageType){
			assertNotInvalidated(getClass(), image);
			image.imageType = imageType;
			return this;
		}
		
		/**
		 * Sets the mandatory {@link ImageState}.
		 * @param imageState the image state
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImageState(ImageState imageState) {
			assertNotInvalidated(getClass(), image);
			image.imageState = imageState;
			return this;
		}
		
		/**
		 * Sets the mandatory element type this image was built for.
		 * @param elementRole the element role
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withElementRole(ElementRoleName elementRole){
			assertNotInvalidated(getClass(), image);
			image.elementRole = elementRole;
			return this;
		}
		
		/**
		 * Sets the chipset for which this image is built.
		 * @param platformChipset the chipset name
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withPlatformChipset(PlatformChipsetName platformChipset) {
			assertNotInvalidated(getClass(), image);
			image.platformChipset = platformChipset;
			return this;
		}

		/**
		 * Sets the known platforms this image can be installed on.
		 * @param platforms the known platforms
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withPlatforms(PlatformSettings.Builder... platforms) {
			return withPlatforms(stream(platforms)
								 .map(PlatformSettings.Builder::build)
								 .collect(toList()));
		}
		
		/**
		 * Sets the known platforms this image can be installed on.
		 * @param platforms the known platforms
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withPlatforms(List<PlatformSettings> platforms) {
			assertNotInvalidated(getClass(), image);
			image.platforms = new LinkedList<>(platforms);
			return this;
		}
		
		/**
		 * Sets the optional element name this image was built for.
		 * An image can be created for a certain element. 
		 * If an element name is specified, the EMS does not allow to install 
		 * the image on a different image.
		 * @param elementName the element name
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withElementName(ElementName elementName){
			assertNotInvalidated(getClass(), image);
			image.elementName = elementName;
			return this;
		}
		
		/**
		 * Sets the optional file extension of the image file.
		 * @param ext the file extension.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withExtension(String ext){
			assertNotInvalidated(getClass(), image);
			image.extension = ext;
			return this;
		}
		
		/**
		 * Sets the mandatory version of the image.
		 * @param version the image version
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(), image);
			image.imageVersion = version;
			return this;
		}
		
		/**
		 * Sets the optional build date of the image.
		 * @param date the build date
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.buildDate =  new Date(date.getTime());
			}
			return this;
		}

		/**
		 * Sets the build id of the image build job.
		 * @param buildid the build ID
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withBuildId(String buildId){
			assertNotInvalidated(getClass(), image);
			image.buildId =  buildId;
			return this;
		}

		/**
		 * Sets an optional image description.
		 * @param description the description.
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withDescription(String description) {
			assertNotInvalidated(getClass(),image);
			image.description = description;
			return this;
		}
		
		/**
		 * Sets the images constituting the image.
		 * @param builders the image builders
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withPackages(PackageVersionInfo.Builder...builders ) {
			return withPackages(stream(builders)
							    .map(PackageVersionInfo.Builder::build)
							    .collect(toList()));
		}
		
		/**
		 * Sets the images constituting the image.
		 * @param packages the images
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withPackages(PackageVersionInfo... packages) {
			return withPackages(asList(packages));
		}
		
		/**
		 * Sets the images constituting the image.
		 * @param packages the images
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withPackages(List<PackageVersionInfo> packages){
			assertNotInvalidated(getClass(), image);
			image.packages = Collections.unmodifiableList(new LinkedList<>(packages));
			return this;
		}
		
		/**
		 * Sets the available checksums for this image.
		 * @param md5 MD5 checksum
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withChecksums(Map<String,String> checksums){
			assertNotInvalidated(getClass(), image);
			image.checksums = new TreeMap<>(checksums);
			return this;
		}

		/**
		 * Sets the applications supported by the image.
		 * @param applicationNames the application names
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withApplications(ApplicationName... applicationNames) {
			return withApplications(asList(applicationNames));
		}

		/**
		 * Sets the applications supported by the image.
		 * @param applicationNames the application names
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withApplications(List<ApplicationName> applications) {
			assertNotInvalidated(getClass(), image);
			image.applications = unmodifiableList(new LinkedList<>(applications));
			return this;
		}
		
		/**
		 * Sets the image category as defined by the organization that issues the image.
		 * @param category the image category
		 * @return a reference to this builder to continue with object creation.
		 */
		public Builder withCategory(String category) {
			assertNotInvalidated(getClass(), image);
			image.category = category;
			return this;
		}
		
		/**
		 * Returns an immutable <code>ImageInfo</code> and invalidates this builder.
		 * Any further invocation of this builder raises an <code>IllegalStateException</code>.
		 * @return the <code>ImageInfo</code> instance.
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

	@NotNull(message="{organization.required}")
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
	
	private ElementRoleName elementRole;
	
	private ElementName elementName;
	
	@NotNull(message="{extension.required}")
	private String extension;
	
	@JsonbProperty
	@Valid
	@NotNull(message="{image_version.required}")
	private Version imageVersion;
	
	@JsonbTypeAdapter(IsoDateAdapter.class)
	private Date buildDate;
	
	private String buildId;

	@Valid
	private List<PackageVersionInfo> packages;
	
	@Valid
	private List<ApplicationName> applications;
	
	@Valid
	@NotNull(message="{platform_chipset.required}")
	private PlatformChipsetName platformChipset;
	
	private List<PlatformSettings> platforms;

	private String description;
	
	private Map<String,String> checksums = emptyMap();
	
	/**
	 * Returns the image id.
	 * @return the image id.
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
	 * Returns the type of element this image was built for.
	 * @return the type of element this image was built for
	 */
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	/**
	 * Returns the name of the element this image was built for or
	 * <code>null</code> if this image was built for all elements of a certain element type.
	 * @return the name of the element this image was built for or <code>null</code> if this image was not built for a certain element
	 * @see #getElementRole()
	 */
	public ElementName getElementName(){
		return elementName;
	}
	
	/**
	 * Returns the image build date or <code>null</code> if the build date is unknown.
	 * @return the build date of the image
	 */
	public Date getBuildDate() {
		if(buildDate == null) {
			return null;
		}
		return new Date(buildDate.getTime());
	}
	
	
	/**
	 * Returns the images constituting this image or an empty list if no image information is available.
	 * @return the images constituting this image or an empty list if no image information is available.
	 */
	public List<PackageVersionInfo> getPackages() {
		if(packages == null){
			return emptyList();
		}
		return unmodifiableList(packages);
	}
	
	/**
	 * Returns the image file extension or <code>null</code> if the file extension was not specified.
	 * @return the image file extension
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
	 * Returns the organization that has issued this image.
	 * The organization is specified in reverse domain order (e.g. net.rtbrick).
	 * @return the organization that has issued this image.
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Returns the list of applications that are supported by this image.
	 * @return the list of applications supported by this image.
	 */
	public List<ApplicationName> getApplications() {
		if(applications == null) {
			return emptyList();
		}
		return unmodifiableList(applications);
	}

	public PlatformChipsetName getPlatformChipset() {
		return platformChipset;
	}
		
	/**
	 * Returns the image state.
	 * @return the image state.
	 */
	public ImageState getImageState() {
		return imageState;
	}

	public String getBuildId() {
		return buildId;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getDescription() {
		return description;
	}
	
	/**
	 * Return the image MD5 checksum or <code>null</code> if no MD5 checksum has been set.
	 * @return the image MD5 checksum
	 */
	public Map<String,String> getChecksums() {
		return unmodifiableMap(checksums);
	}
	
	public List<PlatformSettings> getPlatforms() {
		return unmodifiableList(platforms);
	}
	
	
}
