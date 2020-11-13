package io.leitstand.inventory.rs;

import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsHeader;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.PhysicalInterfaceData;
import io.leitstand.inventory.service.PhysicalInterfaceService;

@RunWith(MockitoJUnitRunner.class)
public class PhysicalInterfacesResourceTest {

    @Mock
    private PhysicalInterfaceService service;
    
    @InjectMocks
    private PhysicalInterfacesResource resource = new PhysicalInterfacesResource();
    
    @Test
    public void return_empty_list_when_empty_filters_are_specified() {
        Response response = resource.findPhysicalInterfaces("",
                                                            "", 
                                                            0, 
                                                            100);
        
        assertThat(response.getStatus(),
                   is(NO_CONTENT.getStatusCode()));
        verifyZeroInteractions(service);
        
    }
    
    @Test
    public void return_empty_list_when_null_filters_are_is_specified() {
        Response response = resource.findPhysicalInterfaces(null,
                                                            null, 
                                                            0, 
                                                            100);
        
        assertThat(response.getStatus(),
                   is(NO_CONTENT.getStatusCode()));
        verifyZeroInteractions(service);
    }
    
    
    @Test
    public void return_empty_list_when_blank_filter_is_specified() {
        Response response = resource.findPhysicalInterfaces("  ",
                                                            " ", 
                                                            0, 
                                                            100);
        assertThat(response.getStatus(),
                   is(NO_CONTENT.getStatusCode()));
        verifyZeroInteractions(service);
    }
    
    
    @Test
    @SuppressWarnings("unchecked")
    public void search_matching_interfaces() {
        List<PhysicalInterfaceData> ifps = mock(List.class);
        when(ifps.size()).thenReturn(24);
        
        when(service.findPhysicalInterfaces("facility", "ifp", 0, 101)).thenReturn(ifps);
     
        Response response = resource.findPhysicalInterfaces("facility","ifp ", 0, 100);
        
        assertEquals(ifps,response.getEntity());
        assertThat(response,containsHeader("Leitstand-Offset",0));
        assertThat(response,containsHeader("Leitstand-Limit",100));
        assertThat(response,containsHeader("Leitstand-Size",24));
        assertThat(response,containsHeader("Leitstand-Eof",true));        
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void search_matching_interfaces_page() {
        List<PhysicalInterfaceData> ifps = mock(List.class);
        List<PhysicalInterfaceData> page = mock(List.class);
        when(page.size()).thenReturn(100);
        when(ifps.size()).thenReturn(101);
        when(ifps.subList(0, 100)).thenReturn(page);
        when(service.findPhysicalInterfaces("facility","ifp", 200, 101)).thenReturn(ifps);
        
        Response response = resource.findPhysicalInterfaces("facility","ifp ", 200, 100);

        assertEquals(page,response.getEntity());
        assertThat(response,containsHeader("Leitstand-Offset",200));
        assertThat(response,containsHeader("Leitstand-Limit",100));
        assertThat(response,containsHeader("Leitstand-Size",100));
        assertThat(response,containsHeader("Leitstand-Eof",false)); 
        
    }
    

}
