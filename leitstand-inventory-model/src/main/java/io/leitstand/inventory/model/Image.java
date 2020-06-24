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

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageState.REVOKED;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TypedQuery;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.ImageNameConverter;
import io.leitstand.inventory.jpa.ImageStateConverter;
import io.leitstand.inventory.jpa.ImageTypeConverter;
import io.leitstand.inventory.jpa.PlatformChipsetNameConverter;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageQuery;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.Version;
@Entity
@Table(schema="inventory", name="image")
@NamedQuery(name="Image.findByImageId", 
		    query="SELECT d FROM Image d WHERE d.uuid=:id")
@NamedQuery(name="Image.markElementImageSuperseded",
			query="UPDATE Image d "+ 
			      "SET d.imageState=io.leitstand.inventory.service.ImageState.SUPERSEDED "+
			      "WHERE d.chipset=:chipset "+
			      "AND :role MEMBER OF d.roles "+
			      "AND d.element=:element "+
			      "AND d.imageType=:type "+
			      "AND d.imageState<>io.leitstand.inventory.service.ImageState.REVOKED "+
			      "AND ("+
			      "(d.major=:major AND d.minor=:minor AND  d.patch=:patch AND d.prerelease IS NOT NULL AND d.prerelease < :prerelease) OR "+
			      "(d.major=:major AND d.minor=:minor AND  d.patch<:patch) OR "+
			      "(d.major=:major AND d.minor<:minor) OR "+
			      "(d.major<:major))")

@NamedQuery(name="Image.restoreElementImageCandidates",
		    query="UPDATE Image d "+ 
		    		  "SET d.imageState=io.leitstand.inventory.service.ImageState.CANDIDATE "+
		    		  "WHERE d.chipset=:chipset "+
		    		  "AND :role MEMBER OF d.roles "+
		    		  "AND d.element=:element "+
		    		  "AND d.imageType=:type "+
		    		  "AND d.imageState<>io.leitstand.inventory.service.ImageState.REVOKED "+
		    		  "AND ("+
		    		  "(d.major=:major AND d.minor=:minor AND d.patch=:patch AND d.prerelease IS NOT NULL and d.prerelease > :prerelease) OR "+
		    		  "(d.major=:major AND d.minor=:minor AND  d.patch>:patch) OR "+
		    		  "(d.major=:major AND d.minor>:minor) OR "+
				      "(d.major>:major))")
@NamedQuery(name="Image.markRoleImageSuperseded",
			query="UPDATE Image d "+ 
				  "SET d.imageState=io.leitstand.inventory.service.ImageState.SUPERSEDED "+
				  "WHERE d IN (SELECT d FROM Image d "+
				  "JOIN d.roles r "+
				  "WHERE d.chipset=:chipset "+
				  "AND r IN :role "+
				  "AND d.element IS NULL "+
				  "AND d.imageType=:type "+
				  "AND d.imageState<>io.leitstand.inventory.service.ImageState.REVOKED "+
				  "AND ("+
			      "(d.major=:major AND d.minor=:minor AND  d.patch=:patch AND d.prerelease IS NOT NULL AND d.prerelease is NOT NULL AND d.prerelease < :prerelease) OR "+
				  "(d.major=:major AND d.minor=:minor AND  d.patch<:patch) OR "+
				  "(d.major=:major AND d.minor<:minor) OR "+
				  "(d.major<:major)))")
@NamedQuery(name="Image.restoreRoleImageCandidates",
				query="UPDATE Image d "+ 
					  "SET d.imageState=io.leitstand.inventory.service.ImageState.CANDIDATE "+
					  "WHERE d IN (SELECT d FROM Image d "+
					  "JOIN d.roles r "+
					  "WHERE d.chipset=:chipset "+
					  "AND r IN :role "+
					  "AND d.element IS NULL "+
					  "AND d.imageType=:type "+
					  "AND d.imageState<>io.leitstand.inventory.service.ImageState.REVOKED "+
					  "AND ("+
		    		  "(d.major=:major AND d.minor=:minor AND d.patch=:patch AND d.prerelease is NOT NULL AND d.prerelease > :prerelease) OR "+
					  "(d.major=:major AND d.minor=:minor AND  d.patch>:patch) OR "+
					  "(d.major=:major AND d.minor>:minor) OR "+
					  "(d.major>:major)))")
@NamedQuery(name="Image.findElementRoleImage",
		    query="SELECT d FROM Image d "+
				  "WHERE d.chipset=:chipset "+
		    	  "AND :role MEMBER OF d.roles "+
				  "AND d.imageType=:type "+
		    	  "AND d.major=:major "+
				  "AND d.minor=:minor "+
		    	  "AND d.patch=:patch "+
				  "AND d.prerelease=:prerelease "+
				  "AND d.element IS NULL")
@NamedQuery(name="Image.findDefaultImage",
			query="SELECT d FROM Image d "+
				  "WHERE d.chipset=:chipset "+
				  "AND :role MEMBER OF d.roles "+
				  "AND d.imageType=:type "+
				  "AND d.imageState=io.leitstand.inventory.service.ImageState.RELEASE "+
				  "AND d.element is null")
@NamedQuery(name="Image.findCandidateImages",
            query="SELECT d FROM Image d "+
                  "WHERE d.chipset=:chipset "+
                  "AND :role MEMBER OF d.roles "+
                  "AND d.imageType=:type "+
                  "AND d.imageState=io.leitstand.inventory.service.ImageState.CANDIDATE "+
                  "AND d.element is null")
@NamedQuery(name="Image.findDefaultImages",
			query="SELECT d FROM Image d "+
				  "WHERE d.chipset=:chipset "+
				  "AND :role MEMBER OF d.roles "+
				  "AND d.imageState=io.leitstand.inventory.service.ImageState.RELEASE "+
				  "AND d.element is null")
@NamedQuery(name="Image.findAvailableUpdates",
			    query="SELECT d FROM Image d "+
			    	  "WHERE d.chipset=:chipset "+
			    	  "AND d.imageState <> io.leitstand.inventory.service.ImageState.REVOKED " +
			    	  "AND :role MEMBER OF d.roles "+
			    	  "AND d.imageType=:type "+
			    	  "AND (d.element IS NULL OR d.element=:element) "+
			   		  "AND ((d.major > :major) "+
			   		  "OR ( d.major = :major AND d.minor > :minor) "+
			   		  "OR (d.major=:major AND d.minor=:minor AND d.patch > :patch) "+
			   		  "OR (d.major=:major AND d.minor=:minor AND d.patch = :patch AND d.prerelease IS NOT NULL AND d.prerelease > :prerelease)) "+
				  "ORDER BY d.major DESC, d.minor DESC, d.patch DESC")
@NamedQuery(name="Image.findByElementAndImageTypeAndVersion", 
			query="SELECT d FROM Image d "+
				  "WHERE :role MEMBER OF d.roles "+
				  "AND d.chipset=:chipset "+
				  "AND d.imageType=:type "+
				  "AND (d.element is NULL OR d.element=:element) "+
				  "AND d.major=:major "+
				  "AND d.minor=:minor "+
				  "AND d.patch=:patch "+
				  "AND d.prerelease=:prerelease")
@NamedQuery(name="Image.countReferences",
			query="SELECT count(ei) FROM Element_Image ei WHERE ei.image=:image")
public class Image extends VersionableEntity{
	
	private static final long serialVersionUID = 1L;
	private static final String RELEASE = "~RELEASE";
	
	static final String prerelease(Version version) {
		return isEmptyString(version.getPreRelease()) ? RELEASE : version.getPreRelease();
	}

	static final String prerelease(String version) {
		return RELEASE.equals(version) ? null : version;
	}
	
	public static Query<List<Image>> searchImages(ElementRole role, ImageQuery querySpec){
		
		return em -> {

		    ImageType type = querySpec.getImageType();
		    ImageState state = querySpec.getImageState();
		    Version version = querySpec.getImageVersion();
		    PlatformChipsetName chipset = querySpec.getPlatformChipset();
		    
			Map<String,Object> params = new HashMap<>();
			
			String jpql = "SELECT i FROM Image i "+
					      "WHERE CAST(i.imageName AS TEXT) REGEXP :name ";

			if(role != null) {
				jpql += "AND :role MEMBER OF i.roles ";
				params.put("role",role);
			}
			
			if(type != null) {
				jpql += "AND i.imageType = :type ";
				params.put("type",type);
			}
			
			if(state != null) {
				jpql += "AND i.imageState=:state ";
				params.put("state",state);
			}
			
			if(chipset != null) {
			    jpql += "AND i.chipset=:chipset ";
			    params.put("chipset", chipset);
			}

			if(version != null) {
				jpql += "AND i.major=:major AND i.minor=:minor AND i.patch=:patch ";
				params.put("major",version.getMajorLevel());
				params.put("minor",version.getMinorLevel());
				params.put("patch",version.getPatchLevel());
				if(isNonEmptyString(version.getPreRelease())) {
					jpql += "AND i.prerelease=:prerelease ";
					params.put("prerelease",version.getPreRelease());
				}
			}
			
				
			jpql += "ORDER BY i.imageName";
			
			TypedQuery<Image> query = em.createQuery(jpql,Image.class);
			query.setParameter("name",querySpec.getFilter());
			for(Map.Entry<String, Object> param : params.entrySet()) {
				query.setParameter(param.getKey(), param.getValue());
			}
			query.setMaxResults(querySpec.getLimit());
			
			return query.getResultList();
			
		};
		
	}
	
	public static Query<Image> findReleaseImage(ElementRole role,
	                                            PlatformChipsetName chipset,
	                                            ImageType imageType) {
	    return em -> em.createNamedQuery("Image.findDefaultImage",Image.class)
	                   .setParameter("role",role)
	                   .setParameter("chipset",chipset)
	                   .setParameter("type",imageType)
	                   .getSingleResult();
	}
	
	   public static Query<List<Image>> findCandidateImages(ElementRole role,
                                                            PlatformChipsetName chipset,
                                                            ImageType imageType) {
	       return em -> em.createNamedQuery("Image.findCandidateImages",Image.class)
	                      .setParameter("role",role)
	                      .setParameter("chipset",chipset)
	                      .setParameter("type",imageType)
	                      .getResultList();
	   }
 	
	public static Update markAllSuperseded(Image image) {
		
		if(image.getElement() != null) {
			return em -> em.createNamedQuery("Image.markElementImageSuperseded",Image.class)
						   .setParameter("chipset", image.getPlatformChipset())
						   .setParameter("role", image.getElementRoles())
						   .setParameter("element", image.getElement())
						   .setParameter("type", image.getImageType())
						   .setParameter("major",image.getImageVersion().getMajorLevel())
						   .setParameter("minor",image.getImageVersion().getMinorLevel())
						   .setParameter("patch",image.getImageVersion().getPatchLevel())
						   .setParameter("prerelease", prerelease(image.getImageVersion()))
						   .executeUpdate();
		}
		return em -> em.createNamedQuery("Image.markRoleImageSuperseded",Image.class)
				   	   .setParameter("chipset", image.getPlatformChipset())
				   	   .setParameter("role", image.getElementRoles())
				   	   .setParameter("type", image.getImageType())
				   	   .setParameter("major",image.getImageVersion().getMajorLevel())
				   	   .setParameter("minor",image.getImageVersion().getMinorLevel())
				   	   .setParameter("patch",image.getImageVersion().getPatchLevel())
					   .setParameter("prerelease", prerelease(image.getImageVersion()))
				   	   .executeUpdate();
			
	}

	public static Update restoreCandidates(Image image) {
		if(image.getElement() != null) {
			return em -> em.createNamedQuery("Image.restoreElementImageCandidates",Image.class)
						   .setParameter("chipset", image.getPlatformChipset())
						   .setParameter("role", image.getElementRoles())
						   .setParameter("element", image.getElement())
						   .setParameter("type", image.getImageType())
						   .setParameter("major",image.getImageVersion().getMajorLevel())
						   .setParameter("minor",image.getImageVersion().getMinorLevel())
						   .setParameter("patch",image.getImageVersion().getPatchLevel())
						   .setParameter("prerelease", prerelease(image.getImageVersion()))
						   .executeUpdate();
		}
		return em -> em.createNamedQuery("Image.restoreRoleImageCandidates",Image.class)
					   .setParameter("chipset", image.getPlatformChipset())
					   .setParameter("role", image.getElementRoles())
					   .setParameter("type", image.getImageType())
					   .setParameter("major",image.getImageVersion().getMajorLevel())
					   .setParameter("minor",image.getImageVersion().getMinorLevel())
					   .setParameter("patch",image.getImageVersion().getPatchLevel())
					   .setParameter("prerelease", prerelease(image.getImageVersion()))
					   .executeUpdate();
	}
	
	public static Query<Image> findElementRoleImage(Platform chipset,
										     		ElementRole elementRole, 
										     		ImageType imageType,
										     		ImageName imageName,
										     		Version version) {
		return em -> em.createNamedQuery("Image.findElementRoleImage",Image.class)
					   .setParameter("chipset", chipset)
					   .setParameter("role", elementRole)
					   .setParameter("type", imageType)
					   .setParameter("major", version.getMajorLevel())
					   .setParameter("minor", version.getMinorLevel())
					   .setParameter("patch", version.getPatchLevel())
					   .setParameter("prerelease", prerelease(version))
					   .getSingleResult();
	}
	
	public static Query<List<Image>> findDefaultImages(ElementRole elementRole,
													   Platform chipset){
		return em -> em.createNamedQuery("Image.findDefaultImages",Image.class)
					   .setParameter("chipset",chipset)
					   .setParameter("role",elementRole)
					   .getResultList();

}
	
	
	public static Query<List<Image>> findUpdates(Platform platform,
												 ImageType imageType, 
												 ImageName imageName,
	                                             ElementRole role, 
	                                             Version version, 
	                                             Element element){
		return em -> em.createNamedQuery("Image.findAvailableUpdates",
										 Image.class)
					   .setParameter("chipset",platform.getChipset())
				       .setParameter("role", role)
				       .setParameter("major",version.getMajorLevel())
				       .setParameter("minor",version.getMinorLevel())
				       .setParameter("patch",version.getPatchLevel())
					   .setParameter("prerelease", prerelease(version))
				       .setParameter("type",imageType)
				       .setParameter("element", element)
				       .getResultList();
	}

	public static Query<Image> findImageById(ImageId id){
		return em -> em.createNamedQuery("Image.findByImageId",
										 Image.class)
					   .setParameter("id", id.toString())
					   .getSingleResult();
	}
	
	public static Query<Image> findByElementAndImageTypeAndVersion(Element element,
																   ImageType imageType, 
																   ImageName imageName,
	                                                               Version version) {
		
		return em -> em.createNamedQuery("Image.findByElementAndImageTypeAndVersion", 
										 Image.class)
					   .setParameter("platform",element.getPlatform())
					   .setParameter("role", element.getElementRole())
					   .setParameter("element", element)
					   .setParameter("type",imageType)
					   .setParameter("major",version.getMajorLevel())
					   .setParameter("minor", version.getMinorLevel())
					   .setParameter("patch", version.getPatchLevel())
					   .setParameter("prerelease", prerelease(version))
					   .getSingleResult();
	}
	
	public static Query<Long> countImageReferences(Image image){
		return em -> em.createNamedQuery("Image.countReferences",Long.class)
					   .setParameter("image",image)
					   .getSingleResult();
	}
	
	
	private String org;
	

	@Convert(converter=ImageTypeConverter.class)
	@Column(name="type")
	private ImageType imageType;
	
	@Convert(converter=ImageNameConverter.class)
	@Column(name="name")
	private ImageName imageName;
	
	@Convert(converter=ImageStateConverter.class)
	@Column(name="state")
	private ImageState imageState;
	
	private String category;
	
	@ManyToMany
	@JoinTable(
		schema="INVENTORY",	
		name="IMAGE_ELEMENTROLE",
		joinColumns=@JoinColumn(name="IMAGE_ID",referencedColumnName="ID"),
		inverseJoinColumns=@JoinColumn(name="ELEMENTROLE_ID",referencedColumnName="ID")
	)
	private List<ElementRole> roles;
	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	@Column(nullable=false)
	private int major;
	
	@Column(nullable=false)
	private int minor;
	
	@Column(nullable=false)
	private int patch;

	private String prerelease;
	
	private String ext;
	
	private String description;
	
	@ElementCollection
	@CollectionTable(schema="inventory",
					 name="image_checksum",
					 joinColumns=@JoinColumn(name="image_id"))
	private List<Checksum> checksums = emptyList();
	
	@Convert(converter=PlatformChipsetNameConverter.class)
	private PlatformChipsetName chipset;
	
	@ManyToMany
	@JoinTable(
			schema="INVENTORY",
			name="IMAGE_APPLICATION",
			joinColumns=@JoinColumn(name="IMAGE_ID", referencedColumnName="ID"),
			inverseJoinColumns=@JoinColumn(name="APPLICATION_ID",referencedColumnName="ID")
			)
	private List<Application> applications;
	

	@Temporal(TIMESTAMP)
	private Date tsbuild;
	
	private String buildId;
	
	@ManyToMany
	@JoinTable(
			schema="INVENTORY",
			name="IMAGE_PACKAGE_VERSION",
			joinColumns=@JoinColumn(name="IMAGE_ID",referencedColumnName="ID"),
			inverseJoinColumns=@JoinColumn(name="PACKAGE_VERSION_ID",referencedColumnName="ID")
			)
	private List<Package_Version> packages;
	
	protected Image(){
		// JPA
	}
	
	public Image(ImageId id){
		super(id.toString());
	}
	
	protected Image(ImageId imageId, 
					ImageType imageType,
					ImageName imageName,
					List<ElementRole> roles, 
					PlatformChipsetName chipset,
					Version version) {
		super(imageId.toString());
		this.imageType = imageType;
		this.imageName = imageName;
		this.imageState = CANDIDATE;
		this.roles = roles;
		this.chipset = chipset;
		this.major = version.getMajorLevel();
		this.minor = version.getMinorLevel();
		this.patch = version.getPatchLevel();
		this.prerelease = prerelease(version);
		this.packages = emptyList();
		this.applications = emptyList();
	}


	public Version getImageVersion(){
		return new Version(major,
						   minor,
						   patch,
						   prerelease(prerelease));
	}
	
	public ImageId getImageId() {
		return new ImageId(getUuid());
	}

	public List<Checksum> getChecksums() {
		return unmodifiableList(checksums);
	}
	
	public Date getBuildDate(){
		if(tsbuild == null) {
			return null;
		}
		return new Date(tsbuild.getTime());
	}
	
	public List<Package_Version> getPackages(){
		return unmodifiableList(packages);
	}
	
	public ImageType getImageType() {
		return imageType;
	}
	
	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
	}
	
	public List<ElementRole> getElementRoles() {
		return unmodifiableList(roles);
	}

	public void setElementRoles(List<ElementRole> roles) {
		this.roles = roles;
	}
	
	public String getImageExtension() {
		return ext;
	}
	
	public void setExtension(String ext) {
		this.ext = ext;
	}
	
	public void setImageVersion(Version version){
		this.major = version.getMajorLevel();
		this.minor = version.getMinorLevel();
		this.patch = version.getPatchLevel();
		this.prerelease = prerelease(version);
	}
	
	public void setBuildDate(Date buildDate){
		if(buildDate != null) {
			this.tsbuild = new Date(buildDate.getTime());
		} else {
			this.tsbuild = null;
		}
	}
	
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	
	public void setPackages(List<Package_Version> packages){
		this.packages = packages;
	}

	public void setChecksums(List<Checksum> checksums) {
		this.checksums = new ArrayList<>(checksums);
	}

	public void setOrganization(String org) {
		this.org = org;
	}

	public String getOrganization() {
		return org;
	}
	
	public void setElement(Element element) {
		this.element = element;
	}
	
	public Element getElement() {
		return element;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public String getBuildId() {
		return buildId;
	}

	public PlatformChipsetName getPlatformChipset() {
		return chipset;
	}

	public ImageState getImageState() {
		return imageState;
	}
	
	public void setImageState(ImageState imageState) {
		this.imageState = imageState;
	}

	public boolean isRevoked() {
		return getImageState() == REVOKED;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public void setPlatformChipset(PlatformChipsetName chipset) {
		this.chipset = chipset;
	}

	public void setImageName(ImageName imageName) {
		this.imageName = imageName;
	}
	
	public ImageName getImageName() {
		return imageName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<ElementRoleName> getElementRoleNames(){
		return getElementRoles()
			   .stream()
			   .map(ElementRole::getRoleName)
			   .collect(toList());
	}

	public ElementName getElementName() {
		if(element != null) {
			return element.getElementName();
		}
		return null;
	}

}
