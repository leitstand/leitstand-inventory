package io.leitstand.inventory.ui;

import static io.leitstand.ui.model.ModuleDescriptor.readModuleDescriptor;
import static io.leitstand.ui.model.ReasonCode.UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.logging.Level.FINE;
import static java.util.logging.Logger.getLogger;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

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
	
	/**
	 * Loads the settings module descriptor.
	 * @return the settings module descriptor.
	 */
	@Produces
	public ModuleDescriptor getSettingsModule() {
		try {
			return readModuleDescriptor(currentThread().getContextClassLoader()
													   .getResource("/META-INF/resources/ui/modules/settings/module.yaml"))
				   .build();
		} catch (IOException e) {
			LOG.severe(format("%s: Cannot load image module. Reason: %s",
							  UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR.getReasonCode(),
							  e.getMessage()));
			LOG.log(FINE,e.getMessage(),e);
			throw new ModuleDescriptorException(e,
												UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR,
												"settings");
		}
	}
	
}
