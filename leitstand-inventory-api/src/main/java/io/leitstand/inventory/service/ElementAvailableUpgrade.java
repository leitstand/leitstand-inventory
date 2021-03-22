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
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.MAJOR;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.MINOR;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.PATCH;

import java.util.Date;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.commons.model.ValueObject;

/**
 * Summary of an available element software upgrade.
 */
public class ElementAvailableUpgrade extends ValueObject {

	/**
	 * An enumeration of upgrade types.
	 */
	public enum UpgradeType {
		/** A major software upgrade.*/
		MAJOR,
		/** A minor software upgrade.*/
		MINOR,
		/** A patch upgrade*/
		PATCH,
		/** A pre-release upgrade.*/
		PRERELEASE;
		
	}
	
	/**
	 * Returns a builder for a <code>ElementAvailableUpgrade</code> instance.
	 * @return a builder for a <code>ElementAvailableUpgrade</code> instance.
	 */
	public static Builder newElementAvailableUpgrade(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ElementAvailableUpdate</code> instance.
	 */
	public static class Builder {
		
		private ElementAvailableUpgrade upgrade = new ElementAvailableUpgrade();
		
		/** 
		 * Sets the image ID.
		 * @param id the image ID
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(),upgrade);
			upgrade.imageId = id;
			return this;
		}

		/**
		 * Sets the image name.
		 * @param name the image name
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageName(ImageName name){
			assertNotInvalidated(getClass(),upgrade);
			upgrade.imageName = name;
			return this;
		}
		
		/**
		 * Sets the image state.
		 * @param state the image state
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageState(ImageState state){
			assertNotInvalidated(getClass(),upgrade);
			upgrade.imageState = state;
			return this;
		}
		
		/** 
		 * Sets the image version.
		 * @param version the image version
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(),upgrade);
			upgrade.imageVersion = version;
			return this;
		}
		
		/** 
		 * Sets the build date of the image
		 * @param date the image build date
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(),upgrade);
			if(date != null) {
				upgrade.buildDate = new Date(date.getTime());
			}
			return this;
		}
		
		/** 
		 * Sets the upgrade type
		 * @param type the upgrade type
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withUpgradeType(UpgradeType type){
			assertNotInvalidated(getClass(),upgrade);
			upgrade.upgradeType = type;
			return this;
		}
		
		/**
		 * Returns an immutable <code>ElementAvailableUpgrade</code> instance.
		 * @return an immutable <code>ElementAvailableUpgrade</code> instance.
		 */
		public ElementAvailableUpgrade build(){
			try{
				assertNotInvalidated(getClass(),upgrade);
				return upgrade;
			} finally {
				this.upgrade = null;
			}
		}
		
	}
	
	private ImageId imageId;
	
	private ImageName imageName;
	
	private ImageState imageState;
	
	@JsonbTypeAdapter(IsoDateAdapter.class)
	private Date buildDate;
	
	private Version imageVersion;
	
	private UpgradeType upgradeType;
	
	/**
	 * Returns the id of the available update.
	 * @return the image id of the available update.
	 */
	public ImageId getImageId() {
		return imageId;
	}
	
	/**
	 * Return the build date of the available update.
	 * @return the build date of available update.
	 */
	public Date getBuildDate(){
		if(buildDate == null) {
			return null;
		}
		return new Date(buildDate.getTime());
	}
	
	/**
	 * Returns the revision of the available update.
	 * @return the revision of the available update.
	 */
	public Version getImageVersion(){
		return imageVersion;
	}
	
	/**
	 * Returns <code>true</code> when this update is a major update.
	 * @return <code>true</code> when this update is a major update.
	 * @see UpgradeType#MAJOR
	 */
	public boolean isMajorUpdate(){
		return upgradeType == MAJOR;
	}
	
	/**
	 * Returns <code>true</code> when this update is a minor update.
	 * @return <code>true</code> when this update is a minor update.
	 * @see UpgradeType#MINOR
	 */
	public boolean isMinorUpdate(){
		return upgradeType == MINOR;
	}
	
	/**
	 * Returns <code>true</code> when this update is a patch update.
	 * @return <code>true</code> when this update is a patch update.
	 * @see UpgradeType#PATCH
	 */
	public boolean isPatch(){
		return upgradeType == PATCH;
	}
	
	/**
	 * Returns the upgrade type.
	 * @return the upgrade type.
	 * @see #isMajorUpdate()
	 * @see #isMinorUpdate()
	 * @see #isPatch()
	 */
	public UpgradeType getUpgradeType() {
		return upgradeType;
	}
	
	/**
	 * Returns the image name.
	 * @return the image name.
	 */
	public ImageName getImageName() {
		return imageName;
	}
	
	/**
	 * Returns the image state.
	 * @return the image state.
	 */
	public ImageState getImageState() {
		return imageState;
	}
}
