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
 * Image UI module loader.
 * <p>
 * Loads the image module descriptor.
 */
@Dependent
public class ImageModule {

	private static final Logger LOG = getLogger(ImageModule.class.getName()); 
	
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
			LOG.severe(format("%s: Cannot load image module. Reason: %s",
							  UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR.getReasonCode(),
							  e.getMessage()));
			LOG.log(FINE,e.getMessage(),e);
			throw new ModuleDescriptorException(e,
												UIM0001E_CANNOT_PROCESS_MODULE_DESCRIPTOR,
												"image");
		}
	}
	
    @Produces
    public Contribution getReleaseManagementViews() {
        try {
            return loadContribution(currentThread()
                                    .getContextClassLoader()
                                    .getResource("/META-INF/resources/ui/modules/image/release/menu.yaml"))
                   .withBaseUri("release")
                   .build();
        } catch (IOException e) {
            LOG.warning(() -> format("%s: Cannot load release management views: %s", 
                                     UIM0002E_CANNOT_PROCESS_MODULE_EXTENSION.getReasonCode(), 
                                     e.getMessage()));
            throw new ModuleDescriptorException(e,
                                                UIM0002E_CANNOT_PROCESS_MODULE_EXTENSION,
                                                "topology");
        }
    }
	
}
