package io.leitstand.inventory.ui;

import static io.leitstand.ui.model.Contribution.loadContribution;
import static io.leitstand.ui.model.ModuleDescriptor.readModuleDescriptor;
import static io.leitstand.ui.model.ReasonCode.UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR;
import static io.leitstand.ui.model.ReasonCode.UIM0002E_CANNOT_PROCESS_MODULE_EXTENSION;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.logging.Level.FINE;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import io.leitstand.ui.model.Contribution;
import io.leitstand.ui.model.ModuleDescriptor;
import io.leitstand.ui.model.ModuleDescriptorException;

/**
 * Settings UI module loader.
 * <p>
 * Loads the settings module descriptor and all default applications.
 */
@Dependent
public class SettingsModule {

	private static final Logger LOG = getLogger(SettingsModule.class.getName()); 
	
	private static final String MODULE_NAME = "settings";
	private static final String MODULE_PATH = "/META-INF/resources/ui/modules/settings/%s";
	
	
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
	 * Loads the settings module descriptor.
	 * @return the settings module descriptor.
	 */
	@Produces
	public ModuleDescriptor getSettingsModule() {
		try {
			Contribution platformContrib  = contribution("platform/menu.yaml");
			Contribution roleContrib      = contribution("role/menu.yaml");
			
			return readModuleDescriptor(currentThread().getContextClassLoader()
													   .getResource(format(MODULE_PATH,"module.yaml")))
				   .withContributions(platformContrib,
						   			  roleContrib)
				   .build();
		} catch (IOException e) {
			LOG.severe(format("%s: Cannot load image module. Reason: %s",
							  UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR.getReasonCode(),
							  e.getMessage()));
			LOG.log(FINE,e.getMessage(),e);
			throw new ModuleDescriptorException(e,
												UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR,
												MODULE_NAME);
		}
	}
	
}
