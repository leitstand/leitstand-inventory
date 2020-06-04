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
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.inventory.service.ElementImageState;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.Version;

@Entity
@Table(schema="inventory", name="element_image")
@IdClass(Element_ImagePK.class)
@NamedQueries({
@NamedQuery(name="Element_Image.findInstalledImages", 
			query="SELECT ei FROM Element_Image ei "+
				  "WHERE ei.element=:element"),
@NamedQuery(name="Element_Image.findInstalledImageById", 
			query="SELECT ei FROM Element_Image ei "+
				  "WHERE ei.element=:element "+
				  "AND ei.image.uuid=:uuid"),
@NamedQuery(name="Element_Image.findInstalledImageByName", 
			query="SELECT ei FROM Element_Image ei "+
				  "WHERE ei.element=:element "+
				  "AND ei.image.imageType=:type "+
				  "AND ei.image.imageName=:name "+
				  "AND ei.image.major=:major "+
				  "AND ei.image.minor=:minor "+
				  "AND ei.image.patch=:patch "),
@NamedQuery(name="Element_Image.findCachedImages",
			query= "SELECT ei.element FROM Element_Image ei "+
			       "WHERE ei.imageState=io.leitstand.inventory.service.ElementImageState.CACHED AND ei.image.uuid=:imageId")

})
public class Element_Image {
	
	public static Query<List<Element>> findImageCaches(ImageId imageId) {
		return em -> em.createNamedQuery("Element_Image.findCachedImages",Element.class)
					   .setParameter("imageId",imageId.toString())
					   .getResultList();
	}

	
	public static Query<List<Element_Image>> findInstalledImages(Element element){
		return em -> em.createNamedQuery("Element_Image.findInstalledImages",
										 Element_Image.class)
					   .setParameter("element", element)
					   .getResultList();
	}

	public static Query<Element_Image> findInstalledImage(Element element, 
	                                                      ImageId imageId){
		return em -> em.createNamedQuery("Element_Image.findInstalledImageById",
										 Element_Image.class)
					   .setParameter("element", element)
					   .setParameter("uuid",imageId.toString())
					   .getSingleResult();
	}
	
	
	@OneToOne
	@JoinColumn(name="IMAGE_ID", nullable=true)
	@Id
	private Image image;
	
	@ManyToOne
	@JoinColumn(name="ELEMENT_ID", nullable=false)
	@Id
	private Element element;

	@Temporal(TIMESTAMP)
	private Date tsmodified;
	
	@Column(name="state")
	@Enumerated(STRING)
	private ElementImageState imageState;
	
	protected Element_Image(){
		// JPA
	}
	
	Element_Image(Element element, Image image){
		this.element = element;
		this.image = image;
		this.imageState = ACTIVE;
		this.tsmodified = new Date();
	}
	
	public Element getElement() {
		return element;
	}
	
	public Image getImage() {
		return image;
	}
	
	public Date getDeployDate() {
		if(tsmodified == null) {
			return null;
		}
		return new Date(tsmodified.getTime());
	}

	public Long getId() {
		return image.getId();
	}

	public Date getDateModified() {
		return image.getDateModified();
	}

	public Date getDateCreated() {
		return image.getDateCreated();
	}

	public Version getImageVersion() {
		return image.getImageVersion();
	}

	public ImageId getImageId() {
		return image.getImageId();
	}

	public Date getBuildDate() {
		return image.getBuildDate();
	}

	public List<Package_Version> getPackages() {
		return image.getPackages();
	}

	public ImageType getImageType() {
		return image.getImageType();
	}

	public ElementRole getElementRole() {
		return image.getElementRole();
	}

	public String getExtension() {
		return image.getImageExtension();
	}

	public String getOrganization() {
		return image.getOrganization();
	}

	public ElementImageState getInstallationState() {
		return imageState;
	}

	public void setImageInstallationState(ElementImageState imageState) {
		this.imageState = imageState;
	}

	public ImageName getImageName() {
		return image.getImageName();
	}

	public boolean isActive() {
		return imageState == ACTIVE;
	}

	
}
