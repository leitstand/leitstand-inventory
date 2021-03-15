package io.leitstand.inventory.ui;

import static io.leitstand.ui.model.Contribution.loadContribution;
import static io.leitstand.ui.model.ModuleDescriptor.readModuleDescriptor;
import static io.leitstand.ui.model.ReasonCode.UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR;
import static io.leitstand.ui.model.ReasonCode.UIM0002E_CANNOT_PROCESS_MODULE_EXTENSION;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.logging.Level.FINE;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import io.leitstand.ui.model.Contribution;
import io.leitstand.ui.model.ModuleDescriptor;
import io.leitstand.ui.model.ModuleDescriptorException;

/**
 * Inventory UI module loader.
 * <p>
 * Loads the inventory module descriptor and all default applications.
 */
@Dependent
public class InventoryModule  {
	
	/** Inventory module path template. */
	private static final String MODULE_PATH = "/META-INF/resources/ui/modules/inventory/%s";
	private static final String MODULE_NAME = "inventory";
	
	private static final Logger LOG = Logger.getLogger(InventoryModule.class.getName());

	/**
	 * Loads a default inventory application.
	 * @param path the application configuration
	 * @return the contribution defined by the application
	 */
	static Contribution contribution(String path) {
		String contrib = path.substring(0,path.lastIndexOf('/'));
		try {
			ClassLoader cl =  currentThread().getContextClassLoader();
			return loadContribution(cl.getResource(format(MODULE_PATH,path)))
				   .withBaseUri(contrib)
				   .build();
		} catch (Exception e) {
			LOG.severe(format("%s: Cannot process contribution %s for module %s. Reason: %s",
							  UIM0002E_CANNOT_PROCESS_MODULE_EXTENSION.getReasonCode(),
							  contrib,
							  MODULE_NAME,
							  e.getMessage()));
			LOG.log(FINE,e.getMessage(),e);
			throw new ModuleDescriptorException(e,
												UIM0002E_CANNOT_PROCESS_MODULE_EXTENSION,
												MODULE_NAME,
												contrib);
		}
	}
	
	/**
	 * Creates the <code>ModuleDescriptor</code> including all existing default applications.
	 * @return the inventory module descriptor.
	 */
	@Produces
	public ModuleDescriptor getInventoryModule(){
		
		try {
			URL moduleDescriptor =  currentThread().getContextClassLoader()
												   .getResource(format(MODULE_PATH,"module.yaml"));
			// Load default inventory applications
			Contribution podContrib 	  = contribution("pod/menu.yaml");
			Contribution elementContrib   = contribution("element/menu.yaml");
			Contribution configContrib 	  = contribution("config/menu.yaml");
			Contribution envContrib		  = contribution("env/menu.yaml");
			Contribution dnsContrib 	  = contribution("dns/menu.yaml");
			Contribution platformContrib  = contribution("platform/menu.yaml");
			Contribution roleContrib      = contribution("role/menu.yaml");
			Contribution facilityContrib  = contribution("facility/menu.yaml");
			Contribution racksContrib	  = contribution("rack/menu.yaml");
			
			return readModuleDescriptor(moduleDescriptor)
				   .withContributions(podContrib,
						   			  elementContrib,
						   			  configContrib,
						   			  envContrib,
						   			  dnsContrib,
						   			  platformContrib,
						   			  roleContrib,
						   			  facilityContrib,
						   			  racksContrib)
					.build();
		} catch (IOException e) {
			LOG.severe(format("%s: Cannot process %s module descriptor. Reason: %s",
					  UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR.getReasonCode(),
					  MODULE_NAME,
					  e.getMessage()));
			LOG.log(FINE,e.getMessage(),e);
			throw new ModuleDescriptorException(e,
												UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR,
												MODULE_NAME);
		}
	}
	
}
