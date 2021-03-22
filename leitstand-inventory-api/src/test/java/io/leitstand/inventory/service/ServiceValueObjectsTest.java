package io.leitstand.inventory.service;


import static io.leitstand.inventory.service.AdministrativeState.UP;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementServiceReference.newElementServiceReference;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.ServiceDefinition.newServiceDefinition;
import static io.leitstand.inventory.service.ServiceId.randomServiceId;
import static io.leitstand.inventory.service.ServiceInfo.newServiceInfo;
import static io.leitstand.inventory.service.ServiceName.serviceName;
import static io.leitstand.inventory.service.ServiceSettings.newServiceSettings;
import static io.leitstand.inventory.service.ServiceType.SERVICE;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import javax.json.JsonObject;

import org.junit.Test;

public class ServiceValueObjectsTest {

    private static final ServiceId      SERVICE_ID      = randomServiceId();
    private static final ServiceName    SERVICE_NAME    = serviceName("service");
    private static final String         DESCRIPTION     = "description";
    private static final String         DISPLAY_NAME    = "display-name";
    private static final ElementId      ELEMENT_ID      = randomElementId();
    private static final ElementName    ELEMENT_NAME    = elementName("element");
    
    @Test
    public void create_service_definition() {
        
        ServiceDefinition service = newServiceDefinition()
                                    .withServiceId(SERVICE_ID)
                                    .withServiceName(SERVICE_NAME)
                                    .withServiceType(SERVICE)
                                    .withDisplayName(DISPLAY_NAME)
                                    .withDescription(DESCRIPTION)
                                    .build();
        
        assertThat(service.getServiceId(), is(SERVICE_ID));
        assertThat(service.getServiceName(),is(SERVICE_NAME));
        assertThat(service.getServiceType(),is(SERVICE));
        assertThat(service.getDisplayName(),is(DISPLAY_NAME));
        assertThat(service.getDescription(),is(DESCRIPTION));
        
    }
    
    @Test
    public void create_service_settings() {
        JsonObject context = createObjectBuilder().build();
        String contextType = "dummy";
        
        ServiceSettings service = newServiceSettings()
                              .withServiceContext(context)
                              .withServiceContextType(contextType)
                              .build();
        assertThat(service.getServiceContext(),is(context));
        assertThat(service.getServiceContextType(),is(contextType));
    }
    
    @Test
    public void create_service_reference() {
        ElementServiceReference ref = newElementServiceReference()
                                      .withElementName(ELEMENT_NAME)
                                      .withServiceName(SERVICE_NAME)
                                      .build();
        assertThat(ref.getElementName(),is(ELEMENT_NAME));
        assertThat(ref.getServiceName(),is(SERVICE_NAME));
    }
    
    @Test
    public void create_service_info() {

        ElementServiceReference parent = mock(ElementServiceReference.class);
        
        ServiceInfo service = newServiceInfo()
                              .withAdministrativeState(UP)
                              .withDescription(DESCRIPTION)
                              .withDisplayName(DISPLAY_NAME)
                              .withElementId(ELEMENT_ID)
                              .withElementName(ELEMENT_NAME)
                              .withOperationalState(DOWN)
                              .withParent(parent)
                              .withServiceName(SERVICE_NAME)
                              .withServiceType(SERVICE)
                              .build();

        assertThat(service.getAdministrativeState(),is(UP));
        assertThat(service.getDescription(),is(DESCRIPTION));
        assertThat(service.getDisplayName(),is(DISPLAY_NAME));
        assertThat(service.getElementId(),is(ELEMENT_ID));
        assertThat(service.getElementName(),is(ELEMENT_NAME));
        assertThat(service.getOperationalState(),is(DOWN));
        assertThat(service.getParent(),is(parent));
        assertThat(service.getServiceName(),is(SERVICE_NAME));
        assertThat(service.getServiceType(),is(SERVICE));
        assertThat(service.getDisplayName(),is(DISPLAY_NAME));
        assertThat(service.getDescription(),is(DESCRIPTION));
        
    }
    
}
