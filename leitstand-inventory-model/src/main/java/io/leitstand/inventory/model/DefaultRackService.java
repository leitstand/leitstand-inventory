package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.inventory.model.Rack.findRacksByFacility;
import static io.leitstand.inventory.model.Rack.findRacksByName;
import static io.leitstand.inventory.model.Rack_Item.findRackItem;
import static io.leitstand.inventory.service.RackItem.newRackItem;
import static io.leitstand.inventory.service.RackItemData.newRackItemData;
import static io.leitstand.inventory.service.RackItems.newRackItems;
import static io.leitstand.inventory.service.RackSettings.newRackSettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0604W_FACILITY_NAME_MISMATCH;
import static io.leitstand.inventory.service.ReasonCode.IVT0801I_RACK_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0802I_RACK_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0803E_RACK_NOT_REMOVABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0804I_RACK_ITEM_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0805I_RACK_ITEM_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0806E_RACK_ITEM_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0807E_RACK_ITEM_NAME_OR_ELEMENT_REQUIRED;
import static io.leitstand.inventory.service.ReasonCode.IVT0808E_ELEMENT_RACK_ITEM_NOT_FOUND;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackItem;
import  io.leitstand.inventory.service.RackItemData;
import io.leitstand.inventory.service.RackItems;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RackService;
import io.leitstand.inventory.service.RackSettings;

@Service
public class DefaultRackService implements RackService{
	
	private static final Logger LOG = Logger.getLogger(DefaultRackService.class.getName());

	static RackSettings settingsOf(Rack rack) {
		return newRackSettings()
			   .withFacilityId(rack.getFacilityId())
			   .withFacilityType(rack.getFacilityType())
			   .withFacilityName(rack.getFacilityName())
			   .withRackId(rack.getRackId())
			   .withRackName(rack.getRackName())
			   .withRackType(rack.getType())
			   .withAdministrativeState(rack.getAdministrativeState())
			   .withUnits(rack.getUnits())
			   .withAscending(rack.isAscending())
			   .withLocation(rack.getLocation())
			   .withAssetId(rack.getAssetId())
			   .withSerialNumber(rack.getSerialNumber())
			   .withDescription(rack.getDescription())
			   .build();
	}
	
	static RackItems rackItems(Rack rack) {
		return newRackItems()
			   .withFacilityId(rack.getFacilityId())
			   .withFacilityType(rack.getFacilityType())
			   .withFacilityName(rack.getFacilityName())
			   .withRackId(rack.getRackId())
			   .withRackName(rack.getRackName())
			   .withRackType(rack.getType())
			   .withAdministrativeState(rack.getAdministrativeState())
			   .withUnits(rack.getUnits())
			   .withAscending(rack.isAscending())
			   .withDescription(rack.getDescription())
			   .withItems(rack.getItems()
					   		  .stream()
					   		  .map(item -> rackItem(item))
					   		  .collect(toList()))
			   
			   .build();
		
		
	}
	
	static RackItemData rackItem(Rack_Item item ) {
		return newRackItemData()
			   .withGroupId(item.getGroupId())
			   .withGroupType(item.getGroupType())
			   .withGroupName(item.getGroupName())
			   .withElementId(item.getElementId())
			   .withElementName(item.getElementName())
			   .withElementAlias(item.getElementAlias())
			   .withElementRole(item.getElementRoleName())
			   .withAdministrativeState(item.getAdministrativeState())
			   .withPlatformId(item.getPlatformId())
			   .withPlatformName(item.getPlatformName())
			   .withHeight(item.getHeight())
			   .withRackItemName(item.getName())
			   .withPosition(item.getPosition())
			   .withFace(item.getFace())
			   .build();
	}
	
	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	private RackProvider racks;
	
	@Inject
	private FacilityProvider facilities;
	
	@Inject
	private ElementProvider elements;
	
	@Inject
	private Messages messages;
	
	public DefaultRackService() {
		//CDI
	}
	
	DefaultRackService(Repository repository, 
					   RackProvider racks, 
					   ElementProvider elements,
					   Messages messages){
		this.repository = repository;
		this.racks = racks;
		this.elements = elements;
		this.messages = messages;
	}
	
	
	@Override
	public List<RackSettings> findRacks(String filter) {
		return repository
			   .execute(findRacksByName(filter))
			   .stream()
			   .map(rack -> settingsOf(rack))
			   .collect(toList());
	}

	@Override
	public RackSettings getRackSettings(RackId rackId) {
		return settingsOf(racks.fetchRack(rackId));
	}

	@Override
	public RackSettings getRackSettings(RackName rackName) {
		return settingsOf(racks.fetchRack(rackName));
	}

	@Override
	public RackItems getRackItems(RackId rackId) {
		return rackItems(racks.fetchRack(rackId));
	}

	@Override
	public RackItems getRackItems(RackName rackName) {
		return rackItems(racks.fetchRack(rackName));
	}

	@Override
	public boolean storeRack(RackSettings settings) {
		boolean created = false;
		Rack rack = racks.tryFetchRack(settings.getRackId());
		if(rack == null) {
			rack = new Rack(settings.getRackId(),settings.getRackName());
			repository.add(rack);
			created = true;
		}
		Facility facility = null;
		if(settings.getFacilityId() != null) {
			facility = facilities.fetchFacility(settings.getFacilityId());
			if(isDifferent(facility.getFacilityName(), settings.getFacilityName())) {
				FacilityName currentName = facility.getFacilityName();
				LOG.fine(()->format("%s: Current facility name %s does not match the expected facility name %s. Facility-ID: %s. Proceed with current name.",
								 	IVT0604W_FACILITY_NAME_MISMATCH.getReasonCode(),
								 	currentName,
								 	settings.getFacilityName(),
								 	settings.getFacilityId()));
				messages.add(createMessage(IVT0604W_FACILITY_NAME_MISMATCH,
										   facility.getFacilityName(),
										   settings.getFacilityName()));
			}
		}
		

		rack.setAscending(settings.isAscending());
		rack.setAssetId(settings.getAssetId());
		rack.setDescription(settings.getDescription());
		rack.setLocation(settings.getLocation());
		rack.setRackName(settings.getRackName());
		rack.setSerialNumber(settings.getSerialNumber());
		rack.setAdministrativeState(settings.getAdministrativeState());
		rack.setType(settings.getRackType());
		rack.setUnits(settings.getUnits());
		rack.setFacility(facility);
		
		LOG.fine(()->format("%s: Rack %s (%s) stored.", 
						    IVT0801I_RACK_STORED.getReasonCode(),
						    settings.getRackName(),
						    settings.getRackId()));
		
		messages.add(createMessage(IVT0801I_RACK_STORED,settings.getRackName()));
		
		return created;
	}

	@Override
	public void storeRackItem(RackId rackId, RackItemData item) {
		storeRackItem(racks.fetchRack(rackId),item);
	}
	
	@Override
	public void storeRackItem(RackName rackName, RackItemData item) {
		storeRackItem(racks.fetchRack(rackName),item);
		
	}
	
	void storeRackItem(Rack rack, RackItemData rackItem) {
		if(rackItem.getRackItemName()== null && rackItem.getElementId() == null && rackItem.getElementName() == null) {
			throw new UnprocessableEntityException(IVT0807E_RACK_ITEM_NAME_OR_ELEMENT_REQUIRED);
		}
		
		Rack_Item item = repository.find(Rack_Item.class,new Rack_ItemPK(rack, rackItem.getPosition()));
		if(item == null) {
			item = new Rack_Item(rack, 
								 rackItem.getPosition(),
								 rackItem.getFace());
		}
		Element element = null;
		if(rackItem.getElementId() != null) {
			element = elements.fetchElement(rackItem.getElementId());
		} else if (rackItem.getElementName() != null) {
			element = elements.fetchElement(rackItem.getElementName());
		}
		if(element != null) {
			Rack_Item old = repository.execute(Rack_Item.findRackItem(element));
			if(old != null && old.getPosition() != rackItem.getPosition()) {
				rack.removeElement(old);
			}
		}
		item.setElement(element);
		
		item.setName(rackItem.getRackItemName());
		item.setHeight(rackItem.getHeight());
		item.setFace(rackItem.getFace());
		
		LOG.fine(()->format("%s: Rack item %d of rack %s (%s) stored.",
							IVT0804I_RACK_ITEM_STORED.getReasonCode(),
							rackItem.getPosition(), 
							rack.getRackName(),
							rack.getRackId()));
		
		messages.add(createMessage(IVT0804I_RACK_ITEM_STORED, 
					 rack.getRackName(), 
					 rackItem.getPosition()));
		
		rack.addElement(item);
	
	}

	@Override
	public void removeRackItem(RackId rackId, int unit) {
		Rack rack = racks.fetchRack(rackId);
		Rack_Item item = repository.find(Rack_Item.class, new Rack_ItemPK(rack, unit));
		if(item != null) {
			repository.remove(item);
			LOG.fine(()->format("%s: Rack item %d of rack %s (%s) removed.",
								IVT0805I_RACK_ITEM_REMOVED.getReasonCode(),
								unit, 
								rack.getRackName(),
								rack.getRackId()));

			messages.add(createMessage(IVT0805I_RACK_ITEM_REMOVED, 
									   rack.getRackName(), 
									   unit));			
		}
		
	}
	
	@Override
	public void removeRackItem(RackName rackName, int unit) {
		Rack rack = racks.fetchRack(rackName);
		Rack_Item item = repository.find(Rack_Item.class, new Rack_ItemPK(rack, unit));
		if(item != null) {
			repository.remove(item);
			LOG.fine(()->format("%s: Rack item %d of rack %s (%s) removed.",
								IVT0805I_RACK_ITEM_REMOVED.getReasonCode(),
								unit, 
								rack.getRackName(),
								rack.getRackId()));

			messages.add(createMessage(IVT0805I_RACK_ITEM_REMOVED, 
									   rack.getRackName(), 
									   unit));			
		}		
	}

	@Override
	public void removeRack(RackId rackId) {
		Rack rack = racks.tryFetchRack(rackId);
		if(rack != null) {
			removeRack(rack);
		}
	}

	@Override
	public void removeRack(RackName rackName) {
		Rack rack = racks.tryFetchRack(rackName);
		if(rack != null) {
			removeRack(rack);
		}
	}
	
	void removeRack(Rack rack) {
		if(!rack.getItems().isEmpty()) {
			LOG.fine(() -> format("%s: Rack %s (%s) has %d items and cannot be removed. Use force remove to remove a rack with rack items.",
								  IVT0803E_RACK_NOT_REMOVABLE.getReasonCode(),
								  rack.getRackName(),
								  rack.getRackId(),
								  rack.getItems().size()));
			
			throw new ConflictException(IVT0803E_RACK_NOT_REMOVABLE, rack.getRackName());
		}
		LOG.fine(()->format("%s: Rack %s (%s) removed.",
							IVT0802I_RACK_REMOVED.getReasonCode(),
							rack.getRackName(),
							rack.getRackId()));
		messages.add(createMessage(IVT0802I_RACK_REMOVED, 
								   rack.getRackName()));	
		repository.remove(rack);
	}

	void forceRemoveRack(Rack rack) {
		LOG.fine(()->format("%s: Rack %s (%s) removed.",
							IVT0802I_RACK_REMOVED.getReasonCode(),
							rack.getRackName(),
							rack.getRackId()));
		messages.add(createMessage(IVT0802I_RACK_REMOVED, 
								   rack.getRackName()));	
		repository.remove(rack);
	}

	
	@Override
	public void forceRemoveRack(RackId rackId) {
		Rack rack = racks.tryFetchRack(rackId);
		if(rack != null) {
			forceRemoveRack(rack);
		}
	}

	@Override
	public void forceRemoveRack(RackName rackName) {
		Rack rack = racks.tryFetchRack(rackName);
		if(rack != null) {
			forceRemoveRack(rack);
		}		
	}

	@Override
	public RackItem getRackItem(RackId rackId, int unit) {
		Rack rack = racks.fetchRack(rackId);
		return getRackItem(rack,unit);
	}

	@Override
	public RackItem getRackItem(RackName rackName, int unit) {
		Rack rack = racks.fetchRack(rackName);
		return getRackItem(rack,unit);
	}

	private RackItem getRackItem(Rack rack, int unit) {
		Rack_Item item = repository.find(Rack_Item.class, new Rack_ItemPK(rack, unit));
		if(item == null) {
			throw new EntityNotFoundException(IVT0806E_RACK_ITEM_NOT_FOUND, rack.getRackName(),unit);
		}
		return newRackItem()
			   .withFacilityId(rack.getFacilityId())
			   .withFacilityType(rack.getFacilityType())
			   .withFacilityName(rack.getFacilityName())
		       .withRackId(rack.getRackId())
		       .withRackName(rack.getRackName())
		       .withDescription(rack.getDescription())
		       .withAdministrativeState(rack.getAdministrativeState())
		       .withAscending(rack.isAscending())
		       .withRackType(rack.getType())
		       .withUnits(rack.getUnits())
		       .withRackItem(rackItem(item))
		       .build();
	}

	@Override
	public List<RackSettings> findRacks(FacilityId facilityId, String filter) {
		Facility facility = facilities.fetchFacility(facilityId);
		return repository
			   .execute(findRacksByFacility(facility,filter))
			   .stream()
			   .map(rack -> settingsOf(rack))
			   .collect(toList());
	
	}

	@Override
	public List<RackSettings> findRacks(FacilityName facilityName, String filter) {
		Facility facility = facilities.fetchFacility(facilityName);
		return repository
			   .execute(findRacksByFacility(facility,filter))
			   .stream()
			   .map(rack -> settingsOf(rack))
			   .collect(toList());
	
	}
	
	@Override
	public RackItem findElementRackItem(ElementId elementId) {
		Element element = elements.fetchElement(elementId);
		return findElementRackItem(element);
	}
	
	@Override
	public RackItem findElementRackItem(ElementName elementName) {
		Element element = elements.fetchElement(elementName);
		return findElementRackItem(element);
	}

	private RackItem findElementRackItem(Element element) {
		Rack_Item item  = repository.execute(findRackItem(element));
		if(item == null) {
			LOG.fine(()->format("%s: No rack item for element %s (%s) found.",
								IVT0808E_ELEMENT_RACK_ITEM_NOT_FOUND.getReasonCode(),
								element.getElementName(),
								element.getElementId()));
			throw new EntityNotFoundException(IVT0808E_ELEMENT_RACK_ITEM_NOT_FOUND,
											  element.getElementName(),
											  element.getElementId());
		}

		Rack rack = item.getRack();
		return RackItem.newRackItem()
				   .withFacilityId(rack.getFacilityId())
				   .withFacilityType(rack.getFacilityType())
				   .withFacilityName(rack.getFacilityName())
				   .withRackId(rack.getRackId())
				   .withRackName(rack.getRackName())
				   .withRackType(rack.getType())
				   .withAdministrativeState(rack.getAdministrativeState())
				   .withUnits(rack.getUnits())
				   .withAscending(rack.isAscending())
				   .withDescription(rack.getDescription())
				   .withRackItem(rackItem(item))
				   .build();
	}

	


}
