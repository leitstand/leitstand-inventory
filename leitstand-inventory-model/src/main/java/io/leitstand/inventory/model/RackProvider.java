package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Rack.findRackById;
import static io.leitstand.inventory.model.Rack.findRackByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0800E_RACK_NOT_FOUND;
import static java.lang.String.format;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.RackId;
import io.leitstand.inventory.service.RackName;

@Dependent
public class RackProvider {

	
	private static final Logger LOG = Logger.getLogger(PlatformProvider.class.getName());

	private Repository repository;
	
	protected RackProvider() {
		// CDI
	}
	
	@Inject
	protected RackProvider(@Inventory Repository repository) {
		this.repository = repository;
	}
	
	public Rack tryFetchRack(RackId rackId) {
		return repository.execute(findRackById(rackId));
	}
	
	public Rack tryFetchRack(RackName rackName) {
		return repository.execute(findRackByName(rackName));
	}
	
	
	public Rack fetchRack(RackId rackId) {
		Rack rack = tryFetchRack(rackId);
		if(rack == null) {
			LOG.fine(() -> format("%s: Rack %s does not exist!", 
							IVT0800E_RACK_NOT_FOUND.getReasonCode(),
							rackId));
			throw new EntityNotFoundException(IVT0800E_RACK_NOT_FOUND,
											  rackId);
		}
		return rack;
	}
	
	public Rack fetchRack(RackName name) {
		Rack rack = tryFetchRack(name);
		if(rack == null) {
			LOG.fine(() -> format("%s: Rack %s does not exist!", 
							IVT0800E_RACK_NOT_FOUND.getReasonCode(),
							name));
			throw new EntityNotFoundException(IVT0800E_RACK_NOT_FOUND,
											  name);
		}
		return rack;
	}
	
}
