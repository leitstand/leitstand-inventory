/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.ReasonCode.IVT0206E_IMAGE_NAME_ALREADY_IN_USE;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageQuery;
import io.leitstand.inventory.service.ImageReference;
import io.leitstand.inventory.service.ImageService;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.Version;

@RunWith(MockitoJUnitRunner.class)
public class ImageResourceTest {
	
    private static final ElementRoleName     ROLE          = elementRoleName("role");
    private static final PlatformChipsetName CHIPSET       = platformChipsetName("chipset");
    private static final ImageType           IMAGE_TYPE    = imageType("type");
	private static final ImageId             IMAGE_ID      = randomImageId();
	private static final ImageName           IMAGE_NAME    = imageName("image");
	private static final String              FILTER        = "filter";
	private static final Version             IMAGE_VERSION = version("1.0.0");
	private static final ImageInfo           IMAGE         = newImageInfo()
										                     .withImageId(IMAGE_ID)
										                     .withImageName(IMAGE_NAME)
										                     .build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private Messages messages;
	
	@Mock
	private ImageService service;
	
	@InjectMocks
	private ImageResource resource = new ImageResource();
	
	@Test
	public void get_image_by_id() {
	    when(service.getImage(IMAGE_ID)).thenReturn(IMAGE);
	    assertEquals(IMAGE, service.getImage(IMAGE_ID));
	}

	@Test
    public void get_image_by_name() {
        when(service.getImage(IMAGE_NAME)).thenReturn(IMAGE);
        assertEquals(IMAGE, service.getImage(IMAGE_NAME));
    }
	
	@Test
	public void cannot_change_image_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeImage(randomImageId(), IMAGE);
	}
	
	@Test
	public void find_images() {
	    
	    @SuppressWarnings("unchecked")
        List<ImageReference> images = mock(List.class);
	    ArgumentCaptor<ImageQuery> queryCaptor = ArgumentCaptor.forClass(ImageQuery.class);
	    when(service.findImages(queryCaptor.capture())).thenReturn(images);
	    
	    resource.findImages(FILTER, 
	                        ROLE, 
	                        CHIPSET, 
	                        IMAGE_TYPE, 
	                        RELEASE, 
	                        IMAGE_VERSION);
	    
	    ImageQuery query = queryCaptor.getValue();
	    assertThat(query.getElementRole(),is(ROLE));
        assertThat(query.getFilter(),is(FILTER));
        assertThat(query.getImageState(),is(RELEASE));
        assertThat(query.getImageType(),is(IMAGE_TYPE));
        assertThat(query.getImageVersion(),is(IMAGE_VERSION));
        assertThat(query.getPlatformChipset(),is(CHIPSET));
	    
	}
	
	@Test
	public void add_image() {
		when(service.storeImage(IMAGE)).thenReturn(true);
		
		Response response = resource.storeImage(IMAGE);
		
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void store_image() {
		Response response = resource.storeImage(IMAGE);
		
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void report_image_name_unique_key_constraint_violation() {
	    exception.expect(UniqueKeyConstraintViolationException.class);
	    exception.expect(reason(IVT0206E_IMAGE_NAME_ALREADY_IN_USE));
	    
		when(service.storeImage(IMAGE)).then(ROLLBACK);
		when(service.getImage(IMAGE_NAME)).thenReturn(IMAGE);
		
		resource.storeImage(IMAGE);
	}
	
	@Test
	public void return_empty_versions_list_if_image_type_is_null() {
	    assertTrue(resource.getImageVersions(null).isEmpty());
	    verifyZeroInteractions(service);
	}
	
    @Test
    public void query_image_versions_by_image_type() {
        resource.getImageVersions(IMAGE_TYPE);
        verify(service).getImageVersions(IMAGE_TYPE);
    }	
	    
	
	@Test
	public void remove_image_by_id() {
	    resource.removeImage(IMAGE_ID);
	    verify(service).removeImage(IMAGE_ID);
	}
	
	@Test
    public void remove_image_by_name() {
        resource.removeImage(IMAGE_NAME);
        verify(service).removeImage(IMAGE_NAME);
    }
}

