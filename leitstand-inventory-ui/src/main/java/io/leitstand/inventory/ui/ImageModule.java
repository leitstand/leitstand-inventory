package io.leitstand.inventory.ui;

import static io.leitstand.ui.model.ModuleDescriptor.readModuleDescriptor;
import static io.leitstand.ui.model.ReasonCode.UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.util.logging.Level.FINE;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import io.leitstand.ui.model.ModuleDescriptor;
import io.leitstand.ui.model.ModuleDescriptorException;

/**
 * Image UI module loader.
 * <p>
 * Loads the image module descriptor.
 */
@Dependent
public class ImageModule {

	private static final Logger LOG = Logger.getLogger(ImageModule.class.getName()); 
	
	/**
	 * Loads the image module descriptor.
	 * @return the image module descriptor.
	 */
	@Produces
	public ModuleDescriptor getImageModule() {
		try {
			return readModuleDescriptor(currentThread().getContextClassLoader()
													   .getResource("/META-INF/resources/ui/modules/image/module.yaml"))
				   .build();
		} catch (IOException e) {
			LOG.severe(format("%s: Cannot read image module. Reason: %s",
							  UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR.getReasonCode(),
							  e.getMessage()));
			LOG.log(FINE,e.getMessage(),e);
			throw new ModuleDescriptorException(e,
												UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR,
												"image");
		}
	}
	
}
