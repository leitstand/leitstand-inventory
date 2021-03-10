package io.leitstand.inventory.rs;

import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementImageReference;
import io.leitstand.inventory.service.ElementImageService;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ImageId;

@RunWith(MockitoJUnitRunner.class)
public class ElementImageResourceTest {
    
    private static final ElementId   ELEMENT_ID   = randomElementId();
    private static final ElementName ELEMENT_NAME = elementName("element");
    private static final ImageId     IMAGE_ID     = randomImageId();

    @Mock
    private ElementImageService service;
   
    @Mock
    private Messages messages;
    
    @InjectMocks
    private ElementImageResource resource = new ElementImageResource();
    
    
    @Test
    public void get_element_image_by_element_id() {
        resource.getElementImage(ELEMENT_ID, IMAGE_ID);
        verify(service).getElementImage(ELEMENT_ID, IMAGE_ID);
    }
    
    @Test
    public void get_element_image_by_element_name() {
        resource.getElementImage(ELEMENT_NAME,IMAGE_ID);
        verify(service).getElementImage(ELEMENT_NAME, IMAGE_ID);
    }
    
    @Test
    public void get_element_images_by_element_id() {
        resource.getElementImages(ELEMENT_ID);
        verify(service).getElementImages(ELEMENT_ID);
    }
    
    @Test
    public void get_element_images_by_element_name() {
        resource.getElementImages(ELEMENT_NAME);
        verify(service).getElementImages(ELEMENT_NAME);
    }
    
    @Test
    public void remove_element_image_by_element_id() {
        resource.removeElementImage(ELEMENT_ID, IMAGE_ID);
        verify(service).removeElementImage(ELEMENT_ID, IMAGE_ID);
    }
    
    @Test
    public void remove_element_image_by_element_name() {
        resource.removeElementImage(ELEMENT_NAME, IMAGE_ID);
        verify(service).removeElementImage(ELEMENT_NAME, IMAGE_ID);
    }
    
    @Test
    public void store_element_images_by_element_id() {
        @SuppressWarnings("unchecked")
        List<ElementImageReference> images = mock(List.class);
        resource.storeElementImages(ELEMENT_ID, images);
        verify(service).storeElementImages(ELEMENT_ID, images);
    }

    @Test
    public void store_element_images_by_element_name() {
        @SuppressWarnings("unchecked")
        List<ElementImageReference> images = mock(List.class);
        resource.storeElementImages(ELEMENT_NAME, images);
        verify(service).storeElementImages(ELEMENT_NAME, images);
    }

    
}
