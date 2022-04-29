package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementConfigId.randomConfigId;
import static io.leitstand.inventory.service.ElementConfigName.elementConfigName;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigService;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;

@RunWith(MockitoJUnitRunner.class)
public class DefaultElementConfigManagerTest {

    private static ElementId         ELEMENT_ID     = randomElementId();
    private static ElementName       ELEMENT_NAME   = elementName("element");
    private static ElementConfigId   CONFIG_ID      = randomConfigId();
    private static ElementConfigName CONFIG_NAME    = elementConfigName("config");
    private static Element           ELEMENT        = mock(Element.class);
    
    @Mock
    private ElementConfigManager manager;
    
    @Mock
    private ElementProvider providers;
    
    @InjectMocks
    private ElementConfigService service = new DefaultElementConfigService();
    
    
    @Test
    public void find_active_config_by_element_id() {
        when(providers.fetchElement(ELEMENT_ID)).thenReturn(ELEMENT);
        service.getActiveElementConfig(ELEMENT_ID, CONFIG_NAME);
        verify(manager).getActiveElementConfig(ELEMENT, CONFIG_NAME);
    }
    
    @Test
    public void find_active_config_by_element_name() {
        when(providers.fetchElement(ELEMENT_NAME)).thenReturn(ELEMENT);
        service.getActiveElementConfig(ELEMENT_NAME, CONFIG_NAME);
        verify(manager).getActiveElementConfig(ELEMENT, CONFIG_NAME);
    }
    
    @Test
    public void find_latest_config_by_element_id() {
        when(providers.fetchElement(ELEMENT_ID)).thenReturn(ELEMENT);
        service.getElementConfig(ELEMENT_ID, CONFIG_NAME);
        verify(manager).getElementConfig(ELEMENT, CONFIG_NAME);
    }
    
    @Test
    public void find_latest_config_by_element_name() {
        when(providers.fetchElement(ELEMENT_NAME)).thenReturn(ELEMENT);
        service.getElementConfig(ELEMENT_NAME, CONFIG_NAME);
        verify(manager).getElementConfig(ELEMENT, CONFIG_NAME);
    }
    
    @Test
    public void find_element_config_by_element_id() {
        when(providers.fetchElement(ELEMENT_ID)).thenReturn(ELEMENT);
        service.getElementConfig(ELEMENT_ID, CONFIG_ID);
        verify(manager).getElementConfig(ELEMENT, CONFIG_ID);
    }
    
    @Test
    public void find_element_config_by_element_name() {
        when(providers.fetchElement(ELEMENT_NAME)).thenReturn(ELEMENT);
        service.getElementConfig(ELEMENT_NAME, CONFIG_ID);
        verify(manager).getElementConfig(ELEMENT, CONFIG_ID);
    }
    
    @Test
    public void restore_element_config_by_element_id() {
        when(providers.fetchElement(ELEMENT_ID)).thenReturn(ELEMENT);
        service.restoreElementConfig(ELEMENT_ID, 
                                     CONFIG_ID,
                                     "comment");
        verify(manager).restoreElementConfig(ELEMENT, 
                                             CONFIG_ID, 
                                             "comment");
    }
    
    @Test
    public void restore_element_config_by_element_name() {
        when(providers.fetchElement(ELEMENT_NAME)).thenReturn(ELEMENT);
        service.restoreElementConfig(ELEMENT_NAME, 
                                     CONFIG_ID,
                                     "comment");
        verify(manager).restoreElementConfig(ELEMENT, 
                                             CONFIG_ID, 
                                             "comment");
    }

    
}
