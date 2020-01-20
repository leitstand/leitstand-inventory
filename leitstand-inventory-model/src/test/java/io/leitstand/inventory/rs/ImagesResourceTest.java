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
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageInfo.newImageInfo;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageInfo;
import io.leitstand.inventory.service.ImageService;

@RunWith(MockitoJUnitRunner.class)
public class ImagesResourceTest {
	
	private static final ImageId IMAGE_ID = randomImageId();
	private static final ImageInfo IMAGE = newImageInfo()
										   .withImageId(IMAGE_ID)
										   .build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Mock
	private Messages messages;
	
	@Mock
	private ImageService service;
	
	@InjectMocks
	private ImagesResource resource = new ImagesResource();
	
	@Test
	public void throws_UnprocessableEntityException_when_attempting_to_change_the_image_id() {
		exception.expect(UnprocessableEntityException.class);
		exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
		
		resource.storeImage(randomImageId(), IMAGE);
	}
	
	@Test
	public void send_created_resonse_when_new_image_has_been_added() {
		when(service.storeImage(IMAGE)).thenReturn(true);
		
		Response response = resource.storeImage(IMAGE);
		
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void send_success_response_when_existing_image_was_updated() {
		when(service.storeImage(IMAGE)).thenReturn(false);
		
		Response response = resource.storeImage(IMAGE);
		
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void send_created_resonse_when_new_image_identified_by_id_has_been_added() {
		when(service.storeImage(IMAGE)).thenReturn(true);
		
		Response response = resource.storeImage(IMAGE_ID,IMAGE);
		
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void send_success_response_when_existing_image_identified_by_id_was_updated() {
		when(service.storeImage(IMAGE)).thenReturn(false);
		
		Response response = resource.storeImage(IMAGE_ID,IMAGE);
		
		assertEquals(200, response.getStatus());
	}
}

