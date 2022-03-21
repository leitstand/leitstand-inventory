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

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.ResourceBundle.getBundle;

import java.util.ResourceBundle;

import io.leitstand.commons.Reason;

/**
 * Enumeration of all inventory reason codes.
 */
public enum ReasonCode implements Reason{

	/** The requested PoD does not exist.*/
	IVT0100E_GROUP_NOT_FOUND,
	
	/** A new element group has been stored.*/
	IVT0101I_GROUP_STORED,
	
	/** The specified element group has been removed from the inventory.*/
	IVT0102I_GROUP_REMOVED,
	
	/** A group with that name already exist.*/
	IVT0103E_GROUP_NAME_ALREADY_IN_USE,
	
	/** 
	 * The specified element group cannot be removed from the inventory. 
	 * Typically the element group contains some elements.
	 */
	IVT0103E_GROUP_NOT_REMOVABLE,
	
	/** The requested images does not exist. */
	IVT0200E_IMAGE_NOT_FOUND,
	
	/** 
	 * The lifecycle state of the image has been updated.
	 * @see ImageState
	 */
	IVT0201I_IMAGE_STATE_UPDATED, 
	
	/** The image has been updated.*/
	IVT0202I_IMAGE_STORED, 

	/** The image has been removed.*/
	IVT0203I_IMAGE_REMOVED, 
	
	/** The image cannot be removed.*/
	IVT0204E_IMAGE_NOT_REMOVABLE, 
	
	/** The image cannot be removed.*/
    IVT0205E_RELEASE_IMAGE_NOT_REMOVABLE, 
    
    /** The image name is already in use.*/
    IVT0206E_IMAGE_NAME_ALREADY_IN_USE,
    
	/** The release does not exist.*/
	IVT0210E_RELEASE_NOT_FOUND,
	
	/** The release has been stored.*/
	IVT0211I_RELEASE_STORED,
	
	/** The release name is already in use.*/
	IVT0212E_RELEASE_NAME_ALREADY_IN_USE,
	
	/** The release contains ambiguous image references.*/
	IVT0213E_AMBIGUOUS_IMAGE,

	IVT0214I_RELEASE_REMOVED,
	
	/** The requested element does not exist.*/
	IVT0300E_ELEMENT_NOT_FOUND,
	
	/** A new element was added to the inventory.*/
	IVT0301I_ELEMENT_STORED,
	
	/** An existing element has been deleted.*/
	IVT0302I_ELEMENT_REMOVED,
	
	/** The specified element cannot be removed from the inventory.*/
	IVT0303E_ELEMENT_NOT_REMOVABLE,
	
	/** The element has been activated.*/
	IVT0304I_ELEMENT_ACTIVATED,
	
	/** The element has been retired.*/
	IVT0305I_ELEMENT_RETIRED,
	
	/** The element has been cloned successfully.*/
	IVT0306I_ELEMENT_CLONED,
	
	/** An element with the given name already exists.*/
	IVT0307E_ELEMENT_NAME_ALREADY_IN_USE,
	
	/** The requested hardware module of an element does not exist.*/
	IVT0310E_ELEMENT_MODULE_NOT_FOUND,

	/** The  hardware module of an element has been stored.*/
	IVT0311I_ELEMENT_MODULE_STORED,
	
	/** The  hardware module has been removed.*/
	IVT0312I_ELEMENT_MODULE_REMOVED,
	
	/** A new element configuration revision was added to the inventory.*/
	IVT0330I_ELEMENT_CONFIG_REVISION_STORED,
	
	/** An existing element configuration revision has been removed from the inventory.*/
	IVT0331I_ELEMENT_CONFIG_REVISION_REMOVED,
	
	/** The requested element configuration revision does not exist!*/
	IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND,
	
	/** The requested element configuration does not exist.*/
	IVT0333E_ELEMENT_CONFIG_NOT_FOUND,

	/** No active configuration of the element configuration exists.*/
	IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND,
	
	/** Removed the config with all its revisions.*/
	IVT0337I_ELEMENT_CONFIG_REMOVED,
	
	/** Configuration cannot be restored.*/
    IVT0338E_ELEMENT_CONFIG_NOT_RESTORABLE,
    
    /** Outdated element configuration removed*/
    IVT0339E_ELEMENT_OUTDATED_CONFIG_REMOVED,
    	
	/** The service does not exist on the specified element.*/
	IVT0320E_ELEMENT_SERVICE_NOT_FOUND,

	/** Service stored for the specified element.*/
	IVT0321I_ELEMENT_SERVICE_STORED,

	/** Service stored for the specified element.*/
	IVT0322I_ELEMENT_SERVICE_REMOVED,

	/** The image is not available on the specified element.*/
	IVT0340W_ELEMENT_IMAGE_NOT_FOUND,
	
	/** The element role does not exist.*/
	IVT0400E_ELEMENT_ROLE_NOT_FOUND,
	
	/** The element role has been stored.*/
	IVT0401I_ELEMENT_ROLE_STORED,

	/** The element role has been removed.*/
	IVT0402I_ELEMENT_ROLE_REMOVED,
	
	/** The element role cannot be removed because elements of that role exist*/
	IVT0403E_ELEMENT_ROLE_NOT_REMOVABLE,
	
	/** A role with the given name already exist.*/
	IVT0404E_ELEMENT_ROLE_NAME_ALREADY_IN_USE,
	
	/** The package does not exist.*/
	IVT0500E_PACKAGE_NOT_FOUND,
	
	/** The package revision does not exist.*/
	IVT0510E_PACKAGE_VERSION_NOT_FOUND, 
	
	/** The physical interface does not exist on this element.*/
	IVT0350E_ELEMENT_IFP_NOT_FOUND,
	
	/** Stored physical interface in the inventory.*/
	IVT0351I_ELEMENT_IFP_STORED,
	
	/** Removed physical interface from the inventory.*/
	IVT0352I_ELEMENT_IFP_REMOVED,
	
	/** The physical interface cannot be removed from the element.*/
	IVT0353E_ELEMENT_IFP_NOT_REMOVABLE, 
	
	/** Stored physical interface neighbor information.*/
	IVT0354I_ELEMENT_IFP_NEIGHBOR_STORED,

	/** Neighbor element is unknown to the inventory.*/
	IVT0355W_ELEMENT_IFP_NEIGHBOR_NOT_FOUND,
	
	/** Removed physical interface neighbor information.*/
	IVT0356I_ELEMENT_IFP_NEIGHBOR_REMOVED,
	
	/** The logical interface does not exist on this element.*/
	IVT0360E_ELEMENT_IFL_NOT_FOUND, 
	
	/** The logical interface has been stored in the inventory.*/
	IVT0361I_ELEMENT_IFL_STORED, 

	/** The logical interface has been removed from the inventory*/
	IVT0362I_ELEMENT_IFL_REMOVED, 
	
	/** The logical interface has been removed from the inventory*/
	IVT0370I_ELEMENT_IFC_STORED,

	/** The image is currently active on the element and the requested action therefore cannot be executed.*/
	IVT0341E_ELEMENT_IMAGE_ACTIVE, 

	/** The image has been removed from the element.*/
	IVT0342I_ELEMENT_IMAGE_REMOVED, 
	
	/** The package revision does already exist.*/
	IVT0511E_PACKAGE_VERSION_EXISTS, 
	
	/** The requested facility does not exist.*/
	IVT0600E_FACILITY_NOT_FOUND,
	
	/** The facility has been stored.*/
	IVT0601I_FACILITY_STORED,
	
	/** The facility has been removed.*/
	IVT0602I_FACILITY_REMOVED,
	
	/** The facility name is already used for another facility.*/
	IVT0603E_FACILITY_NAME_ALREADY_IN_USE,
	
	/** The facility name in the request does not match the current facility name.*/
	IVT0604W_FACILITY_NAME_MISMATCH,

	/** The facility cannot be removed.*/
	IVT0605E_FACILITY_NOT_REMOVABLE,

    /** The facility type in the request does not match the current facility type.*/
    IVT0606W_FACILITY_TYPE_MISMATCH,
	
	/** The requested rack does not exist.*/
	IVT0800E_RACK_NOT_FOUND,
	
	/** The rack has been stored.*/
	IVT0801I_RACK_STORED,
	
	/** The rack has been removed.*/
	IVT0802I_RACK_REMOVED,
	
	/** The rack cannot be removed.*/
	IVT0803E_RACK_NOT_REMOVABLE,
	
	/** The rack item has been stored.*/
	IVT0804I_RACK_ITEM_STORED,
	
	/** The rack item has been removed.*/
	IVT0805I_RACK_ITEM_REMOVED,
	
	/** The rack item has been removed.*/
	IVT0806E_RACK_ITEM_NOT_FOUND,
	
	/** Cannot store rack item with either referenced element or descriptive name.*/
	IVT0807E_RACK_ITEM_NAME_OR_ELEMENT_REQUIRED,

	/** No rack item for element found.*/
	IVT0808E_ELEMENT_RACK_ITEM_NOT_FOUND,
	
	/** Rack name already in use.*/
	IVT0809E_RACK_NAME_ALREADY_IN_USE,
	
	/** The platform does not exist.*/
	IVT0900E_PLATFORM_NOT_FOUND,
	
	/** The platform has been stored.*/
	IVT0901I_PLATFORM_STORED,
	
	/** The platform has been removed.*/
	IVT0902I_PLATFORM_REMOVED, 
	
	/** Platform cannot be removed because of existing elements on that platform*/
	IVT0903E_PLATFORM_NOT_REMOVABLE,
	
	/** The specified platform name conflicts with the name of another platform.*/
	IVT0904E_PLAFORM_NAME_ALREADY_IN_USE,
	
	/** The requested DNS zone does not exist.*/
	IVT0950E_DNS_ZONE_NOT_FOUND,
	
	/** The requested DNS zone has been stored.*/
	IVT0951I_DNS_ZONE_STORED,
	
	/** The requested DNS zone has been removed.*/
	IVT0952I_DNS_ZONE_REMOVED,

	/** The requested DNS zone cannot be removed.*/
	IVT0953E_DNS_ZONE_NOT_REMOVABLE,
	
	/** An attempt to import data into the inventory failed.*/
	IVT1000E_IMPORT_ERROR,
	
	/** The requested environment does not exist.*/
	IVT0390E_ELEMENT_ENVIRONMENT_NOT_FOUND,
	
	/** The requested environment is not bound to the specified element.*/
	IVT0393E_ELEMENT_ENVIRONMENT_OWNED_BY_OTHER_ELEMENT,
	
	/** The environment has been stored.*/
	IVT0391I_ELEMENT_ENVIRONMENT_STORED,
	
	/** The environment has been removed.*/
	IVT0392I_ELEMENT_ENVIRONMENT_REMOVED,

	/** An environment with the specified name already exists.*/
    IVT0393E_ELEMENT_ENVIRONMENT_EXISTS,
    
    /** The element environment violates the environment schema constraints.*/ 
    IVT0394E_ELEMENT_ENVIRONMENT_INVALID,
	
	/** The DNS record does not exist.*/
	IVT3001E_ELEMENT_DNS_RECORD_NOT_FOUND,
	
	/** The DNS record has been stored.*/
	IVT3002I_ELEMENT_DNS_RECORD_STORED,
	
	/** The DNS record has been removed.*/
	IVT3003I_ELEMENT_DNS_RECORD_REMOVED,

	/** The DNS record is owned by another element.*/
	IVT3003I_ELEMENT_DNS_RECORD_OWNED_BY_OTHER_ELEMENT,

	/** The DNS record cannot be assigned to this DNS zone.*/
	IVT3004E_ELEMENT_DNS_RECORD_ZONE_MISMATCH;
	
	private static final ResourceBundle MESSAGES = getBundle("InventoryMessages");
	
	/**
	 * {@inheritDoc}
	 */
	public String getMessage(Object... args){
		try{
			String pattern = MESSAGES.getString(name());
			return format(pattern, args);
		} catch(Exception e){
			return name() + asList(args);
		}
	}
	
}
