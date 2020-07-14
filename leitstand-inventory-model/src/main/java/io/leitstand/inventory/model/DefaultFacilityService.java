package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.Facility.countGroups;
import static io.leitstand.inventory.model.Facility.countRacks;
import static io.leitstand.inventory.model.Facility.findFacilityByName;
import static io.leitstand.inventory.service.FacilitySettings.newFacilitySettings;
import static io.leitstand.inventory.service.ReasonCode.IVT0601I_FACILITY_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0602I_FACILITY_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0605E_FACILITY_NOT_REMOVABLE;
import static java.lang.String.format;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.inventory.service.FacilityId;
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityService;
import io.leitstand.inventory.service.FacilitySettings;

@Service
public class DefaultFacilityService implements FacilityService {
	
	private static final Logger LOG = Logger.getLogger(DefaultFacilityService.class.getName());

	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	private FacilityProvider facilities;
	
	@Inject
	private Messages messages;
	
	public DefaultFacilityService() {
		// CDI
	}
	
	DefaultFacilityService(FacilityProvider facilities, 
						   Repository repository, 
						   Messages messages){
		this.facilities = facilities;
		this.repository = repository;
		this.messages = messages;
	}
	
	@Override
	public List<FacilitySettings> findFacilities(String filter) {
		return repository
			   .execute(findFacilityByName(filter))
			   .stream()
			   .map(f -> settingsOf(f))
			   .collect(Collectors.toList());
	}

	@Override
	public FacilitySettings getFacility(FacilityId facilityId) {
		return settingsOf(facilities.fetchFacility(facilityId));
	}

	@Override
	public FacilitySettings getFacility(FacilityName facilityName) {
		return settingsOf(facilities.fetchFacility(facilityName));
	}

	@Override
	public boolean storeFacility(FacilitySettings settings) {
		boolean created = false;
		Facility facility = facilities.tryFetchFacility(settings.getFacilityId());
		if(facility == null) {
			facility = new Facility(settings.getFacilityId(),
								    settings.getFacilityType(),
								    settings.getFacilityName());
			repository.add(facility);
			created = true;
		}
		facility.setFacilityType(settings.getFacilityType());
		facility.setFacilityName(settings.getFacilityName());
		facility.setCategory(settings.getCategory());
		facility.setDescription(settings.getDescription());
		facility.setLocation(settings.getLocation());
		facility.setGeolocation(settings.getGeolocation());
		LOG.fine(() -> format("%s: Facility %s (%s) stored.",
					   IVT0601I_FACILITY_STORED.getReasonCode(),
					   settings.getFacilityName(),
					   settings.getFacilityId()));
		
		messages.add(createMessage(IVT0601I_FACILITY_STORED,
					 settings.getFacilityName()));
		return created;
	}

	@Override
	public void removeFacility(FacilityId facilityId) {
		Facility facility = facilities.tryFetchFacility(facilityId);
		removeFacility(facility);
		
	}
	
	@Override
	public void removeFacility(FacilityName facilityName) {
		Facility facility = facilities.tryFetchFacility(facilityName);
		removeFacility(facility);		
	}

	private void removeFacility(Facility facility) {
		if(facility != null) {
			long references = repository.execute(countGroups(facility))
							+ repository.execute(countRacks(facility));
			
			if(references > 0) {
				LOG.fine(() -> format("%s: Facility %s (%s) cannot be removed because of %d existing references!",
									 IVT0605E_FACILITY_NOT_REMOVABLE.getReasonCode(),
									 facility.getFacilityName(),
									 facility.getFacilityId(),
									 references));
				throw new ConflictException(IVT0605E_FACILITY_NOT_REMOVABLE, 
											facility.getFacilityName(),
											references);
			}
			
			repository.remove(facility);
			LOG.fine(() -> format("%s: Facility %s (%s) removed.", 
						   		  IVT0602I_FACILITY_REMOVED.getReasonCode(),
						   		  facility.getFacilityName(),
						   		  facility.getFacilityId()));
			
			messages.add(createMessage(IVT0602I_FACILITY_REMOVED,facility.getFacilityName()));
			
		}
	}

	private FacilitySettings settingsOf(Facility facility) {
		return newFacilitySettings()
			   .withFacilityId(facility.getFacilityId())
			   .withFacilityName(facility.getFacilityName())
			   .withFacilityType(facility.getFacilityType())
			   .withCategory(facility.getCategory())
			   .withLocation(facility.getLocation())
			   .withGeolocation(facility.getGeolocation())
			   .withDescription(facility.getDescription())
			   .build();
	}

}
