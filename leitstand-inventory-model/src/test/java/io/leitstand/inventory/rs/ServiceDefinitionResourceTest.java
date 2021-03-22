package io.leitstand.inventory.rs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.ServiceDefinition;
import io.leitstand.inventory.service.ServiceDefinitionService;

@RunWith(MockitoJUnitRunner.class)
public class ServiceDefinitionResourceTest {

    @Mock
    private ServiceDefinitionService service;
    
    @InjectMocks
    private ServiceDefinitionResource resource = new ServiceDefinitionResource();
    
    @Test
    public void get_service_definitions() {
        @SuppressWarnings("unchecked")
        List<ServiceDefinition> services = mock(List.class);
        
        when(service.getServices()).thenReturn(services);
        
        assertEquals(services, resource.getServices());
    }
    
}
