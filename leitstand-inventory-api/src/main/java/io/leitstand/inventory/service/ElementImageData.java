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
 * Provides details for an image eligible for deployment or already installed on an element.
 */
public class ElementImageData extends ValueObject{

	/**
	 * Returns a builder to create an immutable <code>ElementImageData</code> value object.
	 * @return a builder to create an immutable <code>ElementIamgeData</code> value object.
	 */
	public static Builder newElementImageData(){
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable <code>ElementInstalledImageData</code> value object.
	 */
	public static class Builder{
		
	    private ElementImageData image = new ElementImageData();

		/**
		 * Sets the image id.
		 * @param id the image id.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(), image);
			image.imageId = id;
			return this;
		}
		
		/**
		 * Sets the packages shipped with the image.
		 * @param packages the packages.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackages(PackageVersionInfo.Builder... packages){
			return withPackages(stream(packages)
							    .map(PackageVersionInfo.Builder::build)
							    .collect(toList()));
		}

		/**
		 * Sets the packages shipped with the image.
		 * @param packages the packages.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackages(PackageVersionInfo... packages){
			return withPackages(asList(packages));
		}

		/**
		 * Sets the packages shipped with the image.
		 * @param packages the packages.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackages(List<PackageVersionInfo> packages) {
			assertNotInvalidated(getClass(), image);
			image.packages = unmodifiableList(new LinkedList<>(packages));
			return this;
		}
	
		/**
		 * Sets the available image upgrades.
		 * @param upgrades the available image upgrades.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withAvailableUpgrades(ElementAvailableUpgrade.Builder... updates){
			return withAvailableUpgrades(stream(updates)
									    .map(ElementAvailableUpgrade.Builder::build)
									    .collect(toList()));
		}
		
        /**
         * Sets the available image upgrades.
         * @param upgrades the available image upgrades.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withAvailableUpgrades(ElementAvailableUpgrade... updates){
			return withAvailableUpgrades(asList(updates));
		}

        /**
         * Sets the available image upgrades. 
         * @param upgrades the available image upgrades.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withAvailableUpgrades(List<ElementAvailableUpgrade> updates) {
			assertNotInvalidated(getClass(), image);
			image.availableUpgrades = unmodifiableList(new LinkedList<>(updates));
			return this;
		}

		/**
		 * Sets the status of this image for the selected element.
		 * @param state the element image state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withElementImageState(ElementImageState state) {
			assertNotInvalidated(getClass(), image);
			image.elementImageState = state;
			return this;
		}
		
		/**
		 * Sets the organization that published this image.
		 * @param org the organization name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org){
			assertNotInvalidated(getClass(), image);
			image.organization = org;
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
		 * Sets the image type.
		 * @param type image type.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageType(ImageType type){
			assertNotInvalidated(getClass(), image);
			image.imageType = type;
			return this;
		}

		/**
		 * Sets the image lifecycle state.
		 * @param state - the image lifecycle state.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageState(ImageState state) {
			assertNotInvalidated(getClass(), image);
			image.imageState = state;
			return this;
		}
		
		/**
		 * Sets the image name.
		 * @param name image name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageName(ImageName name){
			assertNotInvalidated(getClass(), image);
			image.imageName = name;
			return this;
		}
		
		/**
		 * Sets the image checksums.
		 * @param checksums the image checksums.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withChecksums(Map<String,String> checksums){
			assertNotInvalidated(getClass(), image);
			image.checksums = new TreeMap<>(checksums);
			return this;
		}

		/**
		 * Sets the installation date of the image.
		 * @param date the installation date.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withInstallationDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.installationDate = new Date(date.getTime());
			}
			return this;
		}

		/**
		 * Sets the build date of the image.
		 * @param date the build date.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(), image);
			if(date != null) {
				image.buildDate = new Date(date.getTime());
			}
			return this;
		}

		/**
		 * Sets the image file extension.
		 * @param ext the image file extension.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImageExtension(String ext) {
			assertNotInvalidated(getClass(), image);
			image.imageExtension = ext;
			return this;
		}
		
		/**
		 * Sets whether this image shall be installed via zero-touch provisioning (ZTP).
		 * @param ztp whether this image shall be installed via ZTP.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withZtp(boolean ztp) {
		    assertNotInvalidated(getClass() , image);
		    image.ztp = ztp;
		    return this;
		}
		
		/**
		 * Creates an immutable <code>ElementInstalledImageData</code> value object and invalidates this builder.
		 * Subsquence invocations of the <code>buils()</code> raise an exception.
		 * @return the immutable <code>ElementInstalledImageData</code> value object.
		 */
		public ElementImageData build(){
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
	
	private List<ElementAvailableUpgrade> availableUpgrades;
	
	private ImageType imageType;
	
	private ImageName imageName;
	
	private ImageState imageState;
	
	private String organization;
	
	private String imageExtension;
	
	private boolean ztp;
	
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
	 * Returns the packages shipped with the image.
	 * @return the packages shipped with the image.
	 */
	public List<PackageVersionInfo> getPackages() {
		return packages;
	}
	
	/** 
	 * Returns a list of available software updates.
	 * Returns an empty list if not software updates are available.
	 * @return a list of available software updates.
	 */
	public List<ElementAvailableUpgrade> getAvailableUpgrades() {
		return availableUpgrades;
	}
	
	/**
	 * Returns the image version.
	 * @return the image version.
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
	 * Returns the image id.
	 * @return the image id.
	 */
	public ImageId getImageId() {
		return imageId;
	}
	
	/**
	 * Returns the name of the organization that has published the image.
	 * @return the organization that has published the image.
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
	
	/**
	 * Returns the element image lifecycle state.
	 * @return the element image lifecycle state.
	 * @see ElementImageState
	 */
	public ElementImageState getElementImageState() {
		return elementImageState;
	}
	
	/**
	 * Returns the image file extension.
	 * @return the image file extension.
	 */
	public String getImageExtension() {
		return imageExtension;
	}
	
	/**
	 * Returns the image lifecycle state.
	 * @return the image lifecycle state.
	 */
	public ImageState getImageState() {
		return imageState;
	}
	
	/**
	 * Returns the image checksums.
	 * @return the image checksums.
	 */
	public Map<String, String> getChecksums() {
		return unmodifiableMap(checksums);
	}
	
	/**
	 * Returns whether this image is installed over ZTP.
	 * @return <code>true</code> if this image is installed over ZTP, <code>false</code> otherwise.
	 */
	public boolean isZtp() {
        return ztp;
    }
}
