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

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.optional;
import static io.leitstand.inventory.model.DefaultPackageService.packageVersionInfo;
import static io.leitstand.inventory.model.Element_Image.findInstalledImage;
import static io.leitstand.inventory.model.Element_Image.findInstalledImages;
import static io.leitstand.inventory.model.Image.findImageById;
import static io.leitstand.inventory.model.Image.findUpdates;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.newElementAvailableUpgrade;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.MAJOR;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.MINOR;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.PATCH;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.PRERELEASE;
import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;
import static io.leitstand.inventory.service.ElementImageState.PULL;
import static io.leitstand.inventory.service.ElementInstalledImage.newElementInstalledImage;
import static io.leitstand.inventory.service.ElementInstalledImageData.newElementInstalledImageData;
import static io.leitstand.inventory.service.ElementInstalledImages.newElementInstalleImages;
import static io.leitstand.inventory.service.ReasonCode.IVT0200E_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0340W_ELEMENT_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0341E_ELEMENT_IMAGE_ACTIVE;
import static io.leitstand.inventory.service.ReasonCode.IVT0342I_ELEMENT_IMAGE_REMOVED;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementAvailableUpgrade;
import io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType;
import io.leitstand.inventory.service.ElementImageState;
import io.leitstand.inventory.service.ElementInstalledImage;
import io.leitstand.inventory.service.ElementInstalledImageData;
import io.leitstand.inventory.service.ElementInstalledImageReference;
import io.leitstand.inventory.service.ElementInstalledImages;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.PackageVersionInfo;

@Dependent
public class ElementImageManager {

	private static final Logger LOG = Logger.getLogger(ElementImageManager.class.getName());
	
	private Repository repository;
	private Messages messages;
	private SubtransactionService inventory;
	
	@Inject
	protected ElementImageManager(@Inventory Repository repository, 
							      @Inventory SubtransactionService inventory,
							      Messages messages){
		this.repository = repository;
		this.inventory  = inventory;
		this.messages 	= messages;
	}
	
	
	protected ElementImageManager() {
		// CDI
	}


	public ElementInstalledImages getElementInstalledImages(Element element) {
		ElementGroup group = element.getGroup();
		List<ElementInstalledImageData> installed = new LinkedList<>();
		
		for(Element_Image elementImage : repository.execute(findInstalledImages(element))){
			Image image = elementImage.getImage();
			
			List<PackageVersionInfo> packages = new LinkedList<>();
			for(Package_Version revision : image.getPackages()){
				packages.add(packageVersionInfo(revision));
			}
			
			List<ElementAvailableUpgrade> updates = new LinkedList<>();
			for(Image update : repository.execute(findUpdates(element.getPlatform(),
															  image.getImageType(), 
															  image.getImageName(),
															  element.getElementRole(), 
															  image.getImageVersion(), 
															  element))){
				UpgradeType type = updateType(image, update);
				
				updates.add(newElementAvailableUpgrade()
						    .withImageId(update.getImageId())
						    .withImageName(update.getImageName())
						    .withImageState(update.getImageState())
							.withImageVersion(update.getImageVersion())
							.withBuildDate(update.getBuildDate())
							.withUpdateType(type)
							.build());
			}
			installed.add(newElementInstalledImageData()
						  .withOrganization(image.getOrganization())
						  .withImageId(image.getImageId())
						  .withImageType(image.getImageType())
						  .withImageState(image.getImageState())
						  .withImageName(image.getImageName())
						  .withElementImageState(elementImage.getInstallationState())
						  .withZtp(elementImage.isZtp())
						  .withImageExtension(image.getImageExtension())
						  .withImageVersion(image.getImageVersion())
						  .withChecksums(image.getChecksums().stream().collect(Collectors.toMap(c -> c.getAlgorithm().name(), Checksum::getValue)))
						  .withInstallationDate(elementImage.getDeployDate())
						  .withBuildDate(image.getBuildDate())
						  .withPackages(packages)
						  .withAvailableUpdates(updates)
						  .build());
		}	
		
		 return newElementInstalleImages()
				.withGroupId(group.getGroupId())
				.withGroupName(group.getGroupName())
				.withGroupType(group.getGroupType())
				.withElementId(element.getElementId())
				.withElementName(element.getElementName())
				.withElementAlias(element.getElementAlias())
				.withInstalledImages(installed)
				.build(); 
		
	}

	private UpgradeType updateType(Image image, Image update) {
		UpgradeType type = PRERELEASE;
		if(update.getImageVersion().getMajorLevel() > image.getImageVersion().getMajorLevel()){
			type = MAJOR;
		}
		if(update.getImageVersion().getMinorLevel() > image.getImageVersion().getMinorLevel()){
			type = MINOR;
		}
		if(update.getImageVersion().getPatchLevel() > image.getImageVersion().getPatchLevel()){
			type = PATCH;
		}
		return type;
	}

	public ElementInstalledImage getElementInstalledImage(Element element, ImageId imageId) {
		ElementGroup group = element.getGroup();
		
		Element_Image elementImage = repository.execute(findInstalledImage(element,imageId));
		Image image = elementImage.getImage();
			
		List<PackageVersionInfo> packages = new LinkedList<>();
		for(Package_Version revision : image.getPackages()){
			packages.add(packageVersionInfo(revision));
		}
		
		List<ElementAvailableUpgrade> updates = new LinkedList<>();
		for(Image update : repository.execute(findUpdates(element.getPlatform(),
														  image.getImageType(), 
														  image.getImageName(),
														  element.getElementRole(),
														  image.getImageVersion(),
														  element))){
			UpgradeType type = updateType(image, update);
			
			updates.add(newElementAvailableUpgrade()
					 	.withImageId(update.getImageId())
					 	.withImageName(update.getImageName())
					 	.withImageState(update.getImageState())
					 	.withImageVersion(update.getImageVersion())
					 	.withBuildDate(update.getBuildDate())
						.withUpdateType(type)
						.build());
		}
		return newElementInstalledImage()
			   .withGroupId(group.getGroupId())
			   .withGroupName(group.getGroupName())
			   .withGroupType(group.getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withImage(newElementInstalledImageData()
					   	  .withImageId(image.getImageId())
					   	  .withZtp(elementImage.isZtp())
					   	  .withOrganization(image.getOrganization())
				   		  .withImageType(image.getImageType())
				   		  .withImageState(image.getImageState())
				   		  .withImageName(image.getImageName())
				   		  .withImageExtension(image.getImageExtension())
						  .withElementImageState(elementImage.getInstallationState())
				   		  .withImageVersion(image.getImageVersion())
				   		  .withChecksums(image.getChecksums()
				   				  			  .stream()
				   				  			  .collect(toMap(c -> c.getAlgorithm().name(), 
				   				  					  		 Checksum::getValue)))
				   		  .withInstallationDate(elementImage.getDeployDate())
				   		  .withBuildDate(image.getBuildDate())
				   		  .withPackages(packages)
				   		  .withAvailableUpdates(updates))
			              
			   .build();
			
	}
	
	public void storeInstalledImages(Element element, List<ElementInstalledImageReference> refs) {
		Map<ImageId,Element_Image> images = new HashMap<>();
		for(Element_Image image : repository.execute(findInstalledImages(element))){
			images.put(image.getImageId(),image);
		}
		
		for(ElementInstalledImageReference installed : refs){
			Element_Image image = images.remove(installed.getImageId());
			if(image != null) {
				image.setImageInstallationState(imageInstallationState(installed));
				continue;
			}
			Image artefact = repository.execute(findImageById(installed.getImageId()));
			if(artefact == null){
				
				
				LOG.warning(() -> format("%s: %s %s in %s %s attempted to register image %s (%s) which is unknown to the inventory!",
										 IVT0340W_ELEMENT_IMAGE_NOT_FOUND.getReasonCode(),
										 element.getElementRoleName(),
										 element.getElementName(),
										 element.getGroupType(),
										 element.getGroupName(),
										 installed.getImageId(),
										 installed.getImageType(),
										 optional(installed, ElementInstalledImageReference::getImageName,"no name specified"),
										 installed.getImageVersion()));
				messages.add(createMessage(IVT0340W_ELEMENT_IMAGE_NOT_FOUND,
						  				   element.getElementName(),
						  				   format("%s-%s-%s",
						  						   installed.getImageType(), 
						  						   installed.getImageName(), 
						  						   installed.getImageVersion())));
				
				CreateImageStubRecordFlow flow = new CreateImageStubRecordFlow(element, installed);
				artefact = inventory.run(flow);
				if(artefact == null) {
					LOG.fine(() -> format("Attemt to create image stub record failed. Proceed ignoring the image registration attempt of element %s!",
										  element.getElementName()));
					continue; // With next entry.
				}
			}
			image = new Element_Image(element,artefact);
			image.setImageInstallationState(imageInstallationState(installed));
			repository.add(image);
		}
		
		for(Element_Image image : images.values()){
			repository.remove(image);
		}
		
	}

	private ElementImageState imageInstallationState(ElementInstalledImageReference installed) {
		return installed.isActive() ? ACTIVE : CACHED;
	}


	public void removeInstalledImage(Element element, ImageId imageId) {
		Element_Image image = repository.execute(findInstalledImage(element, imageId));
		if(image == null) {
			return;
		}
		if(image.isActive()) {
			LOG.fine(() -> format("%s: Active image %s (%s) cannot be removed from element %s (%s)",
								  IVT0341E_ELEMENT_IMAGE_ACTIVE.getReasonCode(),
								  image.getImageName(),
								  image.getImageId(),
								  element.getElementName(),
								  element.getElementId()));
			throw new ConflictException(IVT0341E_ELEMENT_IMAGE_ACTIVE, 
										element.getElementName(), 
										image.getImageName(), 
										image.getImageId());
		}
		
		messages.add(createMessage(IVT0342I_ELEMENT_IMAGE_REMOVED, 
								   element.getElementName(), 
								   image.getImageName(),
								   image.getImageId()));
		repository.remove(image);
		
	}


    public void setZtpImage(Element element, ImageId imageId) {
        Image image = repository.execute(findImageById(imageId));
        if(image == null) {
            throw new EntityNotFoundException(IVT0200E_IMAGE_NOT_FOUND,imageId);
        }
        boolean ztpSet = false;
        for(Element_Image elementImage : repository.execute(findInstalledImages(element))){
            if(elementImage.isZtp()) {
               if(elementImage.getImageId().equals(imageId)) {
                   return; // No ZTP changes needed. We're done.
               }
               
               if(elementImage.getInstallationState() == PULL) {
                   // Remove PULL image if it is not longer the ZTP image.
                   repository.remove(elementImage);
               } else {
                   // Keep all other images but don't declare them as ZTP image.
                   elementImage.setZtp(false);
               }
               continue; // process next image.
            }
            
            if(elementImage.getImageId().equals(imageId)) {
                elementImage.setZtp(true);
                ztpSet = true; // ZTP image found.
                // Still need to continue to process the remaining images to find old ZTP image that now needs to be set to false.
            }
        }
        if(!ztpSet) {
            // Create a PULL image for the ZTP image.
            Element_Image ztpImage = new Element_Image(element,image);
            ztpImage.setImageInstallationState(PULL); // Must be PULL because otherwise it would have been processed in the loop above.
            ztpImage.setZtp(true); // This image shall be pulled via ZTP when doing an upgrade.
            repository.add(ztpImage);
        }
    }

    public void resetZtpImage(Element element) {
        for(Element_Image image : repository.execute(findInstalledImages(element))){
            if(image.isZtp()) {
                if(image.getInstallationState() == PULL) {
                    // A pull image is not needed anymore if it is not a ZTP image.
                    repository.remove(image);
                } else {
                    image.setZtp(false);
                }
            }
        }
    }
    

}
