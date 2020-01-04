/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementAvailableUpdate.UpdateType.MAJOR;
import static io.leitstand.inventory.service.ElementAvailableUpdate.UpdateType.MINOR;
import static io.leitstand.inventory.service.ElementAvailableUpdate.UpdateType.PATCH;

import java.util.Date;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.commons.model.ValueObject;

/**
 * A summary of an existing software update for a certain element.
 */
public class ElementAvailableUpdate extends ValueObject {

	/**
	 * An enumeration of update types.
	 */
	public enum UpdateType {
		/** A major software update.*/
		MAJOR,
		/** A minor software update.*/
		MINOR,
		/** A patch update*/
		PATCH,
		/** A pre-release update.*/
		PRERELEASE;
		
	}
	
	/**
	 * Returns a builder for an immutable <code>ElementAvailableUpdate</code> instance.
	 * @return a builder for an immutable <code>ElementAvailableUpdate</code> instance.
	 */
	public static Builder newElementAvailableUpdate(){
		return new Builder();
	}
	
	/**
	 * The builder for an immutable <code>ElementAvailableUpdate</code> instance.
	 */
	public static class Builder {
		
		private ElementAvailableUpdate update = new ElementAvailableUpdate();
		
		/** 
		 * Sets the image id of the available update.
		 * @param id - the image id
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImageId(ImageId id){
			assertNotInvalidated(getClass(),update);
			update.imageId = id;
			return this;
		}
		
		/** 
		 * Sets the image version of the available update.
		 * @param version - the image version
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withImageVersion(Version version){
			assertNotInvalidated(getClass(),update);
			update.imageVersion = version;
			return this;
		}
		
		
		/** 
		 * Sets the build date of the available update.
		 * @param date- the image build date
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withBuildDate(Date date){
			assertNotInvalidated(getClass(),update);
			if(date != null) {
				update.buildDate = new Date(date.getTime());
			}
			return this;
		}
		
		/** 
		 * Sets the type of the available update.
		 * @param type - the update type
		 * @return a reference to this builder to continue with object creation
		 */
		public Builder withUpdateType(UpdateType type){
			assertNotInvalidated(getClass(),update);
			update.updateType = type;
			return this;
		}
		
		public ElementAvailableUpdate build(){
			try{
				assertNotInvalidated(getClass(),update);
				return update;
			} finally {
				this.update = null;
			}
		}
		
	}
	
	private ImageId imageId;
	
	@JsonbTypeAdapter(IsoDateAdapter.class)
	private Date buildDate;
	
	private Version imageVersion;
	
	private UpdateType updateType;
	
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
	public Version getRevision(){
		return imageVersion;
	}
	
	/**
	 * Returns <code>true</code> when this update is a major update.
	 * @return <code>true</code> when this update is a major update.
	 * @see UpdateType#MAJOR
	 */
	public boolean isMajorUpdate(){
		return updateType == MAJOR;
	}
	
	/**
	 * Returns <code>true</code> when this update is a minor update.
	 * @return <code>true</code> when this update is a minor update.
	 * @see UpdateType#MINOR
	 */
	public boolean isMinorUpdate(){
		return updateType == MINOR;
	}
	
	/**
	 * Returns <code>true</code> when this update is a patch update.
	 * @return <code>true</code> when this update is a patch update.
	 * @see UpdateType#PATCH
	 */
	public boolean isPatch(){
		return updateType == PATCH;
	}
	
	/**
	 * Returns <code>true</code> when this update is a pre-release update.
	 * @return <code>true</cpde> when this update is a pre-release update.
	 * @see UpdateType#PRERELEASE
	 */
	
	/**
	 * Returns the update type.
	 * @return the update type.
	 * @see #isMajorUpdate()
	 * @see #isMinorUpdate()
	 * @see #isPatch()
	 */
	public UpdateType getUpdateType() {
		return updateType;
	}
	
}
