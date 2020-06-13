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
import static io.leitstand.commons.model.ObjectUtil.optional;
import static io.leitstand.inventory.event.ImageAddedEvent.newImageAddedEvent;
import static io.leitstand.inventory.event.ImageRemovedEvent.newImageRemovedEvent;
import static io.leitstand.inventory.event.ImageStateChangedEvent.newImageStateChangedEvent;
import static io.leitstand.inventory.event.ImageStoredEvent.newImageStoredEvent;
import static io.leitstand.inventory.model.Application.findAll;
import static io.leitstand.inventory.model.Checksum.newChecksum;
import static io.leitstand.inventory.model.DefaultPackageService.packageVersionInfo;
import static io.leitstand.inventory.model.ElementRole.findRoleByName;
import static io.leitstand.inventory.model.Image.countImageReferences;
import static io.leitstand.inventory.model.Image.findByElementAndImageTypeAndVersion;
import static io.leitstand.inventory.model.Image.findImageById;
import static io.leitstand.inventory.model.Image.markAllSuperseded;
import static io.leitstand.inventory.model.Image.restoreCandidates;
import static io.leitstand.inventory.model.Image.searchImages;
import static io.leitstand.inventory.model.Platform.findByChipset;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageReference.newImageReference;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageState.SUPERSEDED;
import static io.leitstand.inventory.service.ImageStatistics.newImageStatistics;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.PlatformSettings.newPlatformSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0200E_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0201I_IMAGE_STATE_UPDATED;
import static io.leitstand.inventory.service.ReasonCode.IVT0202I_IMAGE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0203I_IMAGE_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0204E_IMAGE_NOT_REMOVABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0400E_ELEMENT_ROLE_NOT_FOUND;
import static io.leitstand.inventory.service.RoleImage.newRoleImage;
import static io.leitstand.inventory.service.RoleImages.newRoleImages;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.event.ImageEvent;
import io.leitstand.inventory.event.ImageEvent.ImageEventBuilder;
import io.leitstand.inventory.service.ApplicationName;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageReference;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.ImageState;
import io.leitstand.inventory.service.ImageStatistics;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PackageVersionInfo;
import io.leitstand.inventory.service.PlatformSettings;
import io.leitstand.inventory.service.RoleImage;
import io.leitstand.inventory.service.RoleImages;
import io.leitstand.inventory.service.Version;

@Service
public class DefaultImageService implements ImageService {
	
	private static final Logger LOG = Logger.getLogger(DefaultImageService.class.getName());
	
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
	private ElementProvider elements;
	
	@Inject
	private Event<ImageEvent> sink;
	
	public DefaultImageService(){
		// EJB
	}
	
	DefaultImageService(PackageVersionService packages,
						Repository repository,
						DatabaseService db,
						Messages messages,
						Event<ImageEvent> sink){
		this.packages = packages;
		this.repository = repository;
		this.db = db;
		this.messages =messages;
		this.sink = sink;
	}
	
	@Override
	public List<ImageReference> findImages(String filter, 
										   ElementRoleName roleName, 
										   ImageType type, 
										   ImageState state, 
										   Version version, 
										   int limit) {
	
		ElementRole role = null;
		if(roleName != null) {
			role = repository.execute(findRoleByName(roleName));
		}
		
		return repository
			   .execute(searchImages(filter, role, type, state, version, limit))
			   .stream()
			   .map(image ->  newImageReference()
					   		  .withImageId(image.getImageId())
					   		  .withBuildDate(image.getBuildDate())
					   		  .withImageState(image.getImageState())
					   		  .withImageType(image.getImageType())
					   		  .withImageName(image.getImageName())
					   		  .withPlatformChipset(image.getPlatformChipset())
					   		  .withImageVersion(image.getImageVersion())
					   		  .withElementName(image.getElementName())
					   		  .withElementRoles(image.getElementRoleNames())
					   		  .build())
			   .collect(Collectors.toList());
		
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
		
		Element element = null;
		ElementName elementName = submission.getElementName();
		if(elementName != null){
			element = elements.fetchElement(elementName);
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
		image.setElement(element);

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
		   	   .withElementName(optional(image.getElement(), Element::getElementName))
		   	   .withExtension(image.getImageExtension())
		   	   .withImageVersion(image.getImageVersion())
		   	   .withBuildDate(image.getBuildDate())
		   	   .withBuildId(image.getBuildId())
		   	   .withPackages(pkgVersions)
		   	   .withApplications(applications)
		   	   .withOrganization(image.getOrganization())
		   	   .withCategory(image.getCategory())
		   	   .withChecksums(image
		   			   		  .getChecksums()
		   				 	  .stream()
		   				 	  .collect(toMap(c -> c.getAlgorithm().name(), 
		   				 				   	 Checksum::getValue)))
			   .build();
		
	}

	@Override
	public ImageInfo removeImage(ImageId id) {
		Image image = repository.execute(findImageById(id));
		if(image == null){
			return null;
		}
		long count = repository.execute(countImageReferences(image));
		if(count > 0) {
			LOG.fine(()->format("%s: Cannot remove image %s (%s) because it is referenced from %d elements.",
								IVT0204E_IMAGE_NOT_REMOVABLE.getReasonCode(),
								image.getImageName(),
								image.getImageId(),
								count));
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
		return info;
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
	public ImageInfo getImage(ImageType imageType, 
							  ImageName imageName, 
							  Version version, 
							  ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		Image image = repository.execute(findByElementAndImageTypeAndVersion(element, 
																			  imageType,
																			  imageName,
																			  version));
		return imageInfo(image);
	}

	@Override
	public ImageInfo getImage(ImageType imageType, 
							  ImageName imageName, 
							  Version version, 
							  ElementName name) {
		Element element = elements.fetchElement(name);
		Image image = repository.execute(findByElementAndImageTypeAndVersion(element, 
																			  imageType, 
																			  imageName,
																			  version));
		return imageInfo(image);
	}

	@Override
	public RoleImages findRoleImages(ElementRoleName role) {
		
		List<RoleImage> images = db.executeQuery(prepare("SELECT DISTINCT i.name, i.type "+
														 "FROM inventory.image i "+
														 "JOIN inventory.elementrole r "+
														 "ON i.elementrole_id = r.id "+
														 "WHERE r.name=? "+
														 "ORDER BY i.name, i.type", role.toString()), 
												 rs -> newRoleImage()
												 	   .withImageType(ImageType.valueOf(rs.getString(2)))
												 	   .withImageName(ImageName.valueOf(rs.getString(1)))
												 	   .build());
		
		return newRoleImages()
			   .withElementRole(role)
			   .withImages(images)
			   .build();
	}

	@Override
	public ImageStatistics getImageStatistics(ImageId imageId) {
		
		ImageInfo image = getImage(imageId);
		
		
		
		Map<ElementGroupName,Integer> activeCount = new HashMap<>();
		db.processQuery(prepare("SELECT eg.name, count(*) "+
							    "FROM inventory.image i "+
							    "JOIN inventory.element_image ei "+
							    "ON ei.image_id = i.id "+
							    "JOIN inventory.element e "+
							    "ON e.id = ei.element_id "+
							    "JOIN inventory.elementgroup eg "+
							    "ON eg.id = e.elementgroup_id "+
							    "WHERE i.uuid=? "+
							    "AND ei.state='ACTIVE' "+
							    "GROUP BY eg.name", 
							    image.getImageId()),
						rs -> activeCount.put(ElementGroupName.valueOf(rs.getString(1)),
									 	 	  rs.getInt(2)));
		
		Map<ElementGroupName,Integer> cachedCount = new HashMap<>();
		db.processQuery(prepare("SELECT eg.name, count(*) "+
							    "FROM inventory.image i "+
							    "JOIN inventory.element_image ei "+
							    "ON ei.image_id = i.id "+
							    "JOIN inventory.element e "+
							    "ON e.id = ei.element_id "+
							    "JOIN inventory.elementgroup eg "+
							    "ON eg.id = e.elementgroup_id "+
							    "WHERE i.uuid=? "+
							    "AND ei.state='CACHED' "+
							    "GROUP BY eg.name", 
							    image.getImageId()),
						rs -> cachedCount.put(ElementGroupName.valueOf(rs.getString(1)),
									 	 	  rs.getInt(2)));

		return newImageStatistics()
			   .withImage(image)
			   .withActiveCount(activeCount)
			   .withCachedCount(cachedCount)
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

}
