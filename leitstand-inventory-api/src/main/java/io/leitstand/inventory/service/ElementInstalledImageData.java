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
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
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

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.commons.model.ValueObject;

/**
 * Provides all details of an installed image.
 */

public class ElementInstalledImageData extends ValueObject{

	/**
	 * Returns a builder to create a new immutable <code>ElementInstalledImageData</code> instance.
	 * @return a builder to create a new immutable <code>ElementInstalledImageData</code> instance.
	 */
	public static Builder newElementInstalledImageData(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementInstalledImageData</code> instance.
	 */
	public static class Builder{
		private ElementInstalledImageData image = new ElementInstalledImageData();

		/**
		 * Sets the image id.
		 * @param id - the image id
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(), image);
			image.imageId = id;
			return this;
		}
		
		/**
		 * Sets the packages constituting the image.
		 * @param packages - the packages
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPackages(PackageVersionInfo.Builder... packages){
			return withPackages(stream(packages)
							    .map(PackageVersionInfo.Builder::build)
							    .collect(toList()));
		}

		/**
		 * Sets the packages constituting the image.
		 * @param packages - the packages
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPackages(PackageVersionInfo... packages){
			return withPackages(asList(packages));
		}

		/**
		 * Sets the packages constituting the image.
		 * @param packages - the packages
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPackages(List<PackageVersionInfo> packages) {
			assertNotInvalidated(getClass(), image);
			image.packages = unmodifiableList(new LinkedList<>(packages));
			return this;
		}
	
		/**
		 * Sets the available updates for this image.
		 * @param updates - the available updates
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAvailableUpdates(ElementAvailableUpdate.Builder... updates){
			return withAvailableUpdates(stream(updates)
									    .map(ElementAvailableUpdate.Builder::build)
									    .collect(toList()));
		}
		
		/**
		 * Sets the available updates for this image.
		 * @param updates - the available updates
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAvailableUpdates(ElementAvailableUpdate... updates){
			return withAvailableUpdates(asList(updates));
		}

		/**
		 * Sets the available updates for this image.
		 * @param updates - the available updates
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withAvailableUpdates(List<ElementAvailableUpdate> updates) {
			assertNotInvalidated(getClass(), image);
			image.availableUpdates = unmodifiableList(new LinkedList<>(updates));
			return this;
		}

		/**
		 * Sets whether this image is currently active or not.
		 * @param state - the installation state of the image
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withElementImageState(ElementImageState state) {
			assertNotInvalidated(getClass(), image);
			image.elementImageState = state;
			return this;
		}
		
		/**
		 * Sets the organization that issued this image.
		 * @param org - the organization that has issued this image as reverse domain name (e.g. net.rtbrick).
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withOrganization(String org){
			assertNotInvalidated(getClass(), image);
			image.organization = org;
			return this;
		}
		
		/**
		 * Sets the image revision
		 * @param version - the image revision
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(), image);
			image.imageVersion = version;
			return this;
		}
		
		/**
		 * Sets the element type this image was built for.
		 * @param elementRole - the element type the image was built for
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withElementRole(ElementRoleName elementRole){
			assertNotInvalidated(getClass(), image);
			image.elementRole = elementRole;
			return this;
		}
		
		/**
		 * Sets the image type.
		 * @param type - image type
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageType(ImageType type){
			assertNotInvalidated(getClass(), image);
			image.imageType = type;
			return this;
		}

		/**
		 * Sets the image lifecylce state of the installed image.
		 * @param state - the image lifecycle state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageState(ImageState state) {
			assertNotInvalidated(getClass(), image);
			image.imageState = state;
			return this;
		}
		
		/**
		 * Sets the image name.
		 * @param name - image name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageName(ImageName name){
			assertNotInvalidated(getClass(), image);
			image.imageName = name;
			return this;
		}
		
		/**
		 * Sets the available checksums for this installed image.
		 * @param checksums the available checksums
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withChecksums(Map<String,String> checksums){
			assertNotInvalidated(getClass(), image);
			image.checksums = new TreeMap<>(checksums);
			return this;
		}

		/**
		 * Sets the installation date of the image.
		 * @param date - the installation date
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withInstallationDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.installationDate = new Date(date.getTime());
			}
			return this;
		}

		/**
		 * Sets the build date  of the image.
		 * @param date - the build date
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.buildDate = new Date(date.getTime());
			}
			return this;
		}

		/**
		 * Sets the extension of the image file.
		 * @param ext - the image file extension
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageExtension(String ext) {
			assertNotInvalidated(getClass(), image);
			image.imageExtension = ext;
			return this;
		}
		
		/**
		 * Creates an immutable <code>ElementInstalledImageData</code> instance and invalidates this constructor.
		 * Any further interaction with this builder raises an exception.
		 * @return the immutable <code>ElementInstalledImageData</code> instance.
		 */
		public ElementInstalledImageData build(){
			try{
				assertNotInvalidated(getClass(), image);
				return image;
			} finally {
				this.image = null;
			}
		}
	}
	
	
	@JsonbProperty("image_id")
	private ImageId imageId;
	
	@JsonbProperty
	private Version imageVersion;
	
	@JsonbTypeAdapter(IsoDateAdapter.class)
	@JsonbProperty("date_installed")
	private Date installationDate;

	@JsonbTypeAdapter(IsoDateAdapter.class)
	@JsonbProperty("build_date")
	private Date buildDate;
	
	private List<PackageVersionInfo> packages;
	
	private List<ElementAvailableUpdate> availableUpdates;
	
	private ElementRoleName elementRole;
	
	private ImageType imageType;
	
	private ImageName imageName;
	
	private ImageState imageState;
	
	private String organization;
	
	private String imageExtension;
	
	private ElementImageState elementImageState;
	
	private Map<String,String> checksums = emptyMap();
	
	/**
	 * Returns the installation date of the image.
	 * @return the installation date of the image.
	 */
	public Date getInstallationDate() {
		if(installationDate == null) {
			return null;
		}
		return new Date(installationDate.getTime());
	}
	
	/**
	 * Returns the build date of the image.
	 * @return the build date of the image.
	 */
	public Date getBuildDate() {
		if(buildDate == null) {
			return null;
		}
		return new Date(buildDate.getTime());
	}
	
	/**
	 * Returns the packages constituting the image.
	 * @return the packages constituting the image.
	 */
	public List<PackageVersionInfo> getPackages() {
		return packages;
	}
	
	/** 
	 * Returns a list of available software updates.
	 * Returns an empty list if not software updates are available.
	 * @return a list of available software updates.
	 */
	public List<ElementAvailableUpdate> getAvailableUpdates() {
		return availableUpdates;
	}
	
	/**
	 * Returns the image revision.
	 * @return the image revision.
	 */
	public Version getImageVersion() {
		return imageVersion;
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
	 * Returns the element role.
	 * @return the element role.
	 */
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	/**
	 * Returns the image id.
	 * @return the image id.
	 */
	public ImageId getImageId() {
		return imageId;
	}
	
	/**
	 * Returns the name of the organization that has issued the image.
	 * The name is typically specified in reverse domain order (e.g. net.rtbrick).
	 * @return the organization that has issued the image.
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Returns whether the image is currently active (<code>true</code>) or not (<code>false</code>).
	 * @return <code>true</code> if this image is currently active on the element.
	 */
	public boolean isActive() {
		return elementImageState == ACTIVE;
	}
	
	public ElementImageState getElementImageInstallationState() {
		return elementImageState;
	}
	
	/**
	 * Returns the file extension of the image file.
	 * @return the file extension of the image file.
	 */
	public String getExtension() {
		return imageExtension;
	}
	
	/**
	 * Returns the lifecycle state of the installed image.
	 * @return the image lifecycle state.
	 */
	public ImageState getImageState() {
		return imageState;
	}
	
	public Map<String, String> getChecksums() {
		return unmodifiableMap(checksums);
	}
	
}
