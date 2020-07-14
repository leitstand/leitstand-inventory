package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.RackExport.newRackExport;
import static io.leitstand.inventory.service.RackExportItem.newRackExportItem;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.leitstand.inventory.service.RackExport;
import io.leitstand.inventory.service.RackExportItem;
import io.leitstand.inventory.service.RackExportService;
import io.leitstand.inventory.service.RackItemData;
import io.leitstand.inventory.service.RackItems;
import io.leitstand.inventory.service.RackService;
import io.leitstand.inventory.service.RackSettings;

@ApplicationScoped
public class DefaultRackExportService implements RackExportService {

	@Inject
	private RackService service;
	
	@Override
	public RackExport exportRacks(String filter) {
		List<RackExportItem> racks = new LinkedList<>();
		for(RackSettings rack : service.findRacks(filter)) {
			RackItems rackItems = service.getRackItems(rack.getRackName());
			racks.add(newRackExportItem()
					  .withRackSettings(rack)
					  .withRackItems(rackItems.getItems())
					  .build());
		}
		
		return newRackExport()
			   .withDateCreated(new Date())
			   .withRacks(racks)
			   .build();
	
	}

	@Override
	public void importRacks(RackExport racks) {
		for(RackExportItem rack : racks.getRacks()) {
			RackSettings settings = rack.getRackSettings();
			service.storeRack(settings);
			for(RackItemData item : rack.getRackItems()) {
				service.storeRackItem(settings.getRackId(), item);
			}
		}
		
	}

}
