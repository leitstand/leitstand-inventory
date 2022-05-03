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

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.commons.model.StringUtil.trim;
import static io.leitstand.inventory.event.ImageAddedEvent.newImageAddedEvent;
import static io.leitstand.inventory.event.ImageRemovedEvent.newImageRemovedEvent;
import static io.leitstand.inventory.event.ImageStateChangedEvent.newImageStateChangedEvent;
import static io.leitstand.inventory.event.ImageStoredEvent.newImageStoredEvent;
import static io.leitstand.inventory.jpa.ImageStateConverter.toImageState;
import static io.leitstand.inventory.model.Application.findAll;
import static io.leitstand.inventory.model.Checksum.newChecksum;
import static io.leitstand.inventory.model.DefaultPackageService.packageVersionInfo;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Image.countElementImageReferences;
import static io.leitstand.inventory.model.Image.countReleaseImageReferences;
import static io.leitstand.inventory.model.Image.findImageById;
import static io.leitstand.inventory.model.Image.findImageByName;
import static io.leitstand.inventory.model.Image.markAllSuperseded;
import static io.leitstand.inventory.model.Image.restoreCandidates;
import static io.leitstand.inventory.model.Image.searchImages;
import static io.leitstand.inventory.model.Platform.findByChipset;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.groupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.elementId;
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.PULL;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageDeploymentCount.newImageDeploymentCount;
import static io.leitstand.inventory.service.ImageDeploymentStatistics.newImageStatistics;
import static io.leitstand.inventory.service.ImageId.imageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageReference.newImageReference;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageState.SUPERSEDED;
import static io.leitstand.inventory.service.ImageStatisticsElementGroupElementImageState.newElementGroupElementImageState;
import static io.leitstand.inventory.service.ImageStatisticsElementGroupElementImages.newElementGroupElementImages;
import static io.leitstand.inventory.service.ImageStatisticsElementGroupImageCount.newElementGroupImageCount;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0200E_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0201I_IMAGE_STATE_UPDATED;
import static io.leitstand.inventory.service.ReasonCode.IVT0202I_IMAGE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0203I_IMAGE_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0204E_IMAGE_NOT_REMOVABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static io.leitstand.inventory.service.ReleaseId.releaseId;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static io.leitstand.inventory.service.ReleaseRef.newReleaseReference;
import static io.leitstand.inventory.service.RoleImage.newRoleImage;
import static io.leitstand.inventory.service.RoleImages.newRoleImages;
import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.validation.Valid;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.event.ImageEvent;
import io.leitstand.inventory.event.ImageEvent.ImageEventBuilder;
import io.leitstand.inventory.service.ApplicationName;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementImageState;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageDeploymentCount;
import io.leitstand.inventory.service.ImageDeploymentStatistics;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageQuery;
import io.leitstand.inventory.service.ImageReference;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageStatisticsElementGroupElementImageState;
import io.leitstand.inventory.service.ImageStatisticsElementGroupElementImages;
import io.leitstand.inventory.service.ImageStatisticsElementGroupImageCount;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformSettings;
import io.leitstand.inventory.service.ReasonCode;
import io.leitstand.inventory.service.ReleaseRef;
import io.leitstand.inventory.service.RoleImage;
import io.leitstand.inventory.service.RoleImages;
import io.leitstand.inventory.service.Version;

@Service
public class DefaultImageService implements ImageService {
	
	private static final Logger LOG = getLogger(DefaultImageService.class.getName());
	
	@Inject
	private Messages messages;

	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	@Inventory
	private DatabaseService db;
	
	@Inject
	private PackageVersionService packages;
	
	@Inject
	private ElementGroupProvider groups;
	
	@Inject
	private Event<ImageEvent> sink;
	
	public DefaultImageService(){
		// CDI
	}
	
	DefaultImageService(PackageVersionService packages,
	                    ElementGroupProvider groups,
						Repository repository,
						DatabaseService db,
						Messages messages,
						Event<ImageEvent> sink){
		this.packages = packages;
		this.groups = groups;
		this.repository = repository;
		this.db = db;
		this.messages =messages;
		this.sink = sink;
	}
	
	@Override
	public List<ImageReference> findImages(ImageQuery query) {
	
		ElementRole role = null;
		if(query.getElementRole() != null) {
			role = repository.execute(findRoleByName(query.getElementRole()));
		}
		
		return repository
			   .execute(searchImages(role,query))
			   .stream()
			   .map(image ->  referenceOf(image))
			   .collect(toList());
		
	}

	protected static ImageReference referenceOf(Image image) {
        return newImageReference()
        	   .withImageId(image.getImageId())
        	   .withBuildDate(image.getBuildDate())
        	   .withImageState(image.getImageState())
        	   .withImageType(image.getImageType())
        	   .withImageName(image.getImageName())
        	   .withPlatformChipset(image.getPlatformChipset())
        	   .withImageVersion(image.getImageVersion())
        	   .withElementRoles(image.getElementRoleNames())
        	   .withTags(image.getTags())
        	   .build();
    }

	@Override
	public boolean storeImage(ImageInfo submission) {
		List<ElementRole> elementRoles = new LinkedList<>();
		
		for(ElementRoleName roleName : submission.getElementRoles()) {
			ElementRole elementRole = repository.execute(findRoleByName(roleName));
			if(elementRole == null){
				throw new EntityNotFoundException(IVT0400E_ELEMENT_ROLE_NOT_FOUND, 
												  roleName);
			}
			elementRoles.add(elementRole);
		}
		
		List<Package_Version> versions = new LinkedList<>();
		for(PackageVersionInfo info : submission.getPackages()){
			Package_Version version = packages.getPackageVersion(info.getOrganization(), 
																info.getPackageName(), 
																info.getPackageVersion());
			if(version == null){
				version = packages.storePackageVersion(info);
			} 
			version.setBuildDate(info.getBuildDate());
			List<Checksum> checksums = info.getChecksums()
										   .entrySet()
										   .stream()
										   .map(c -> newChecksum()
												   	 .withAlgorithm(Checksum.Algorithm.valueOf(c.getKey()))
												   	 .withValue(c.getValue())
												   	 .build())
										   .collect(toList());
			version.setChecksums(checksums);
			versions.add(version);
		}
		
		Map<ApplicationName,Application> applications = repository.execute(findAll());
		List<Application> imageApplications = new LinkedList<>();
		for(ApplicationName name: submission.getApplications()) {
			Application app = applications.get(name);
			if(app != null) {
				imageApplications.add(app);
				continue;
			}
			app = new Application(name);
			repository.add(app);
			imageApplications.add(app);
		}
		
		Image image = repository.execute(findImageById(submission.getImageId()));
		boolean created = false;
		if(image == null){
			image = new Image(submission.getImageId());
			repository.add(image);
			created = true;
		} 
		image.setPlatformChipset(submission.getPlatformChipset());
		image.setCategory(submission.getCategory());
		image.setImageState(submission.getImageState());
		image.setImageType(submission.getImageType());
		image.setImageName(submission.getImageName());
		image.setElementRoles(elementRoles);
		image.setExtension(submission.getExtension());
		image.setImageVersion(submission.getImageVersion());
		image.setBuildDate(submission.getBuildDate());
		image.setBuildId(submission.getBuildId());
		image.setPackages(versions);
		image.setApplications(imageApplications);
		image.setChecksums(submission.getChecksums()
				   			   		 .entrySet()
				   			   		 .stream()
				   			   		 .map(c -> newChecksum()
				   			   				   .withAlgorithm(Checksum.Algorithm.valueOf(c.getKey()))
				   			   				   .withValue(c.getValue())
				   			   				   .build())
				   			   		 .collect(toList()));
		image.setOrganization(submission.getOrganization());
		image.setTags(submission.getTags());

		messages.add(createMessage(IVT0202I_IMAGE_STORED, 
				   				   image.getImageName()));		
		if (created) {
			fire(newImageAddedEvent(),
				 submission);
			return true;
		}
		fire(newImageStoredEvent(),
			 submission);
		return false;
	}


	private <E extends ImageEvent, B extends ImageEventBuilder<E,B>> void fire(B event, ImageInfo image) {
		sink.fire(event
				  .withImageId(image.getImageId())
				  .withOrganization(image.getOrganization())
				  .withImageType(image.getImageType())
				  .withElementRoles(image.getElementRoles())
				  .withImageName(image.getImageName())
				  .withImageVersion(image.getImageVersion())
				  .withImageExtension(image.getExtension())
				  .withImageState(image.getImageState())
				  .withChecksums(image.getChecksums())
				  .build());		
	}

	@Override
	public ImageInfo getImage(ImageId id) {
		Image image = repository.execute(findImageById(id));
		if(image == null){
			throw new EntityNotFoundException(IVT0200E_IMAGE_NOT_FOUND,id);
		}
		return imageInfo(image);
		
	}

	public ImageInfo imageInfo(Image image) {

		List<PlatformSettings> platforms = repository
										   .execute(findByChipset(image.getPlatformChipset()))
										   .stream()
										   .map(p -> newPlatformSettings()
												   	 .withPlatformId(p.getPlatformId())
													 .withPlatformName(p.getPlatformName())
													 .withModelName(p.getModel())
													 .withVendorName(p.getVendor())
													 .withDescription(p.getDescription())
													 .build())
											.collect(toList());

		
		List<PackageVersionInfo> pkgVersions = new LinkedList<>();
		for(Package_Version p : image.getPackages()){
			pkgVersions.add(packageVersionInfo(p));
		}
		
		List<ApplicationName> applications = image.getApplications()
												  .stream()
												  .map(Application::getName)
												  .collect(toList());
		
		
		return newImageInfo()
			   .withImageId(image.getImageId())
		   	   .withImageType(image.getImageType())
		   	   .withImageName(image.getImageName())
		   	   .withImageState(image.getImageState())
		   	   .withElementRoles(image.getElementRoleNames())
		   	   .withPlatformChipset(image.getPlatformChipset())
		   	   .withPlatforms(platforms)
		   	   .withExtension(image.getImageExtension())
		   	   .withImageVersion(image.getImageVersion())
		   	   .withBuildDate(image.getBuildDate())
		   	   .withBuildId(image.getBuildId())
		   	   .withPackages(pkgVersions)
		   	   .withApplications(applications)
		   	   .withOrganization(image.getOrganization())
		   	   .withCategory(image.getCategory())
		   	   .withTags(image.getTags())
		   	   .withChecksums(image
		   			   		  .getChecksums()
		   				 	  .stream()
		   				 	  .collect(toMap(c -> c.getAlgorithm().name(), 
		   				 				   	 Checksum::getValue)))
			   .build();
		
	}

	@Override
	public void removeImage(ImageId id) {
		Image image = repository.execute(findImageById(id));
		if(image == null){
			return;
		}
		long elementImageCount = repository.execute(countElementImageReferences(image));
		if(elementImageCount > 0) {
			LOG.fine(()->format("%s: Cannot remove image %s (%s) because it is referenced from %d elements.",
								IVT0204E_IMAGE_NOT_REMOVABLE.getReasonCode(),
								image.getImageName(),
								image.getImageId(),
								elementImageCount));
			throw new ConflictException(IVT0204E_IMAGE_NOT_REMOVABLE, 
										image.getImageId(), 
										image.getImageName());
		}
		
        long releaseImageCount = repository.execute(countReleaseImageReferences(image));
        if(releaseImageCount > 0) {
            LOG.fine(()->format("%s: Cannot remove image %s (%s) because it is referenced from %d elements.",
                                IVT0204E_IMAGE_NOT_REMOVABLE.getReasonCode(),
                                image.getImageName(),
                                image.getImageId(),
                                releaseImageCount));
            throw new ConflictException(IVT0204E_IMAGE_NOT_REMOVABLE, 
                                        image.getImageId(), 
                                        image.getImageName());
        }
		
		ImageInfo info = imageInfo(image);
		repository.remove(image);
		messages.add(createMessage(IVT0203I_IMAGE_REMOVED,
								   image.getImageName()));
		fire(newImageRemovedEvent(),
			 info);
	}

	@Override
	public void updateImageState(ImageId id, ImageState state) {
		Image image = repository.execute(findImageById(id));
		if(image.getImageState() == state) {
			return;
		}
		if(state == SUPERSEDED) {
			throw new IllegalArgumentException("Images must not be set to superseded manually.");
		}
		if(state == RELEASE) {
			repository.execute(markAllSuperseded(image));
			repository.execute(restoreCandidates(image));
		}
		ImageState prev = image.getImageState();
		image.setImageState(state);
		sink.fire(newImageStateChangedEvent()
				  .withImageId(image.getImageId())
				  .withOrganization(image.getOrganization())
				  .withImageType(image.getImageType())
				  .withElementRoles(image.getElementRoleNames())
				  .withImageName(image.getImageName())
				  .withImageVersion(image.getImageVersion())
				  .withImageExtension(image.getImageExtension())
				  .withImageState(image.getImageState())
				  .withPreviousState(prev)
				  .withChecksums(image
						  		 .getChecksums()
						  		 .stream()
						  		 .collect(toMap(c -> c.getAlgorithm().name(), 
						  				  Checksum::getValue)))
				  .build());			
		messages.add(createMessage(IVT0201I_IMAGE_STATE_UPDATED, 
								   image.getImageName(),
								   state));
		
	}

	@Override
	public RoleImages findRoleImages(ElementRoleName role) {
		
		List<RoleImage> images = db.executeQuery(prepare("SELECT DISTINCT i.uuid, i.name, i.type, i.major, i.minor, i.patch, i.prerelease, i.chipset "+
														 "FROM inventory.image i "+
														 "JOIN inventory.image_elementrole ir "+
														 "ON i.id = ir.image_id "+
														 "JOIN inventory.elementrole r "+
														 "ON ir.elementrole_id = r.id "+
														 "WHERE r.name=? "+
														 "ORDER BY i.name, i.type", role.toString()), 
												 rs -> newRoleImage()
												       .withImageId(imageId(rs.getString(1)))
												       .withImageName(imageName(rs.getString(2)))
												 	   .withImageType(imageType(rs.getString(3)))
												 	   .withImageVersion(new Version(rs.getInt(4),
												 	                                 rs.getInt(5),
												 	                                 rs.getInt(6),
												 	                                 rs.getString(7)))
												 	   .withPlatformChipset(platformChipsetName(rs.getString(8)))
												 	   .build());
		
		return newRoleImages()
			   .withElementRole(role)
			   .withImages(images)
			   .build();
	}
	
	@Override
	public List<ImageDeploymentCount> getDeploymentStatistics(String filter){
		
		if (isEmptyString(trim(filter))) {
			filter=".*";
		}
		
		return db.executeQuery(prepare(
							   "SELECT i.uuid, i.name, i.state, count(*) "+
							   "FROM inventory.image i "+
							   "JOIN inventory.element_image e "+
							   "ON i.id = e.image_id "+
							   "WHERE i.name ~ ? "+
							   "GROUP BY i.uuid, i.name, i.state "+
							   "ORDER BY i.name",
							   filter),
							rs -> newImageDeploymentCount()
								  .withImageId(imageId(rs.getString(1)))
								  .withImageName(imageName(rs.getString(2)))
								  .withImageState(toImageState(rs.getString(3)))
								  .withElements(rs.getInt(4))
								  .build());
		
		
	}
	

	@Override
	public ImageDeploymentStatistics getDeploymentStatistics(ImageId imageId) {
		
		ImageInfo image = getImage(imageId);
		Map<String,ImageStatisticsElementGroupImageCount.Builder> stats = new TreeMap<>();
		db.processQuery(prepare(
		                "SELECT g.uuid, g.type, g.name, ei.state, count(*) "+
						"FROM inventory.image i "+
						"JOIN inventory.element_image ei "+
						"ON ei.image_id = i.id "+
						"JOIN inventory.element e "+
						"ON e.id = ei.element_id "+
						"JOIN inventory.elementgroup g "+
						"ON g.id = e.elementgroup_id "+
						"WHERE i.uuid=? "+
						"GROUP BY g.uuid, g.type, g.name, ei.state ", 
						image.getImageId()),
						rs -> {
						    String groupName = rs.getString(3);
						    ImageStatisticsElementGroupImageCount.Builder group = stats.get(groupName);
						    if(group == null) {
						        group = newElementGroupImageCount()
						                .withGroupId(groupId(rs.getString(1)))
						                .withGroupType(groupType(rs.getString(2)))
						                .withGroupName(groupName(groupName));
						        stats.put(groupName, 
						                  group);
						    }
						    ElementImageState state = ElementImageState.valueOf(rs.getString(4));
						    
						    if(state == ACTIVE) {
						        group.withActiveCount(rs.getInt(5));
						    } else if (state == PULL) {
						        group.withPullCount(rs.getInt(5));
						    } else {
						        group.withCachedCount(rs.getInt(5));
						    }
						    
						});
		
		List<ImageStatisticsElementGroupImageCount> groupStats = stats
                                                                 .values()
                                                                 .stream()
                                                                 .map(ImageStatisticsElementGroupImageCount.Builder::build)
                                                                 .collect(toList());
		
		List<ReleaseRef> releases = db.executeQuery(prepare(
		                                            "SELECT r.uuid,r.name "+
		                                            "FROM inventory.release r "+
		                                            "JOIN inventory.release_image ri "+
		                                            "ON r.id = ri.release_id "+
		                                            "JOIN inventory.image i "+
		                                            "ON ri.image_id = i.id "+
		                                            "WHERE i.uuid=?",
		                                            imageId),
		                                            rs -> newReleaseReference()
		                                                  .withReleaseId(releaseId(rs.getString(1)))
		                                                  .withReleaseName(releaseName(rs.getString(2)))
		                                                  .build());
		                                                          
		
		return newImageStatistics()
		       .withImage(image)
		       .withElementGroupCounters(groupStats)
		       .withReleases(releases)
		       .build();

	}
	
	@Override
	public List<ImageType> getImageTypes() {
		return db.executeQuery(prepare("SELECT DISTINCT type FROM inventory.image ORDER BY type ASC"),
									   rs -> imageType(rs.getString(1)));
	}
	
	@Override
	public List<Version> getImageVersions(ImageType type) {
		return db.executeQuery(prepare("SELECT DISTINCT major, minor, patch, prerelease FROM inventory.image WHERE type=? ORDER BY major, minor, patch ASC",type.toString()),
									   rs -> new Version(rs.getInt(1),
											   			 rs.getInt(2),
											   			 rs.getInt(3),
											   			 rs.getString(4)));
	}

    @Override
    public ImageReference getReleaseImage(ElementRoleName roleName, 
                                          PlatformChipsetName chipset,
                                          ImageType imageType) {
        ElementRole role = repository.execute(ElementRole.findRoleByName(roleName));
        if(role == null) {
            LOG.fine(() -> format("%s: Element role %s not found.", ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND.getReasonCode(),roleName));
            throw new EntityNotFoundException(ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND,roleName);
        }
        Image image = repository.execute(Image.findReleaseImage(role, chipset,imageType));
        if(image == null) {
            LOG.fine(() -> format("%s: No default image for role %s with chipset %s found.",
                                  ReasonCode.IVT0205E_RELEASE_IMAGE_NOT_REMOVABLE.getReasonCode(),
                                  roleName,
                                  chipset));
            throw new EntityNotFoundException( ReasonCode.IVT0205E_RELEASE_IMAGE_NOT_REMOVABLE,roleName,chipset);
        }
        
        return referenceOf(image);
    }

    protected ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId, 
                                                                                      ElementGroup group) {
        ImageInfo image = getImage(imageId);
        
        List<ImageStatisticsElementGroupElementImageState> elements = db.executeQuery(
                                                                         prepare(
                                                                         "SELECT e.uuid, e.name, e.alias, r.name, ei.state "+
                                                                         "FROM inventory.element e "+
                                                                         "JOIN inventory.element_image ei "+
                                                                         "ON e.id = ei.element_id "+
                                                                         "JOIN inventory.image i "+
                                                                         "ON ei.image_id = i.id "+
                                                                         "JOIN inventory.elementrole r "+
                                                                         "ON e.elementrole_id = r.id "+
                                                                         "WHERE e.elementgroup_id=? "+
                                                                         "AND i.uuid=?",
                                                                         group.getId(),
                                                                         imageId),
                                                                         rs -> newElementGroupElementImageState()
                                                                               .withElementId(elementId(rs.getString(1)))
                                                                               .withElementName(elementName(rs.getString(2)))
                                                                               .withElementAlias(elementAlias(rs.getString(3)))
                                                                               .withElementRole(elementRoleName(rs.getString(4)))
                                                                               .withElementImageState(ElementImageState.valueOf(rs.getString(5)))
                                                                               .build());
                
                
                
        
        return newElementGroupElementImages()
               .withGroupId(group.getGroupId())
               .withGroupName(group.getGroupName())
               .withGroupType(group.getGroupType())
               .withImage(image)
               .withElements(elements)
               .build();
        
    }

    @Override
    public ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId,
                                                                                   ElementGroupId groupId) {
        ElementGroup group = groups.fetchElementGroup(groupId);
        return getElementGroupImageStatistics(imageId, group);
    }

    @Override
    public ImageStatisticsElementGroupElementImages getElementGroupImageStatistics(ImageId imageId,
                                                                                   ElementGroupType groupType, 
                                                                                   ElementGroupName groupName) {
        ElementGroup group = groups.fetchElementGroup(groupType,groupName);
        return getElementGroupImageStatistics(imageId, group);

    }

    @Override
    public ImageInfo getImage(ImageName imageName) {
        Image image = repository.execute(findImageByName(imageName));
        if(image == null) {
            throw new EntityNotFoundException(IVT0200E_IMAGE_NOT_FOUND,imageName);
        }
        return imageInfo(image);
    }

    @Override
    public void removeImage(@Valid ImageName imageName) {
    	Image image = repository.execute(findImageByName(imageName));
    	if (image != null) {
    		repository.remove(image);
    	}
    }

}
