/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;
import static io.leitstand.inventory.service.ElementInstalledImageReference.newElementInstalledImageReference;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageType.LXC;
import static io.leitstand.inventory.service.ReasonCode.IVT0341E_ELEMENT_IMAGE_ACTIVE;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.commons.tx.Flow;
import io.leitstand.commons.tx.SubtransactionService;
import io.leitstand.inventory.service.ElementInstalledImageReference;

@RunWith(MockitoJUnitRunner.class)
public class ElementImageManagerTest {
	
	private static final ElementInstalledImageReference ACTIVE_IMAGE_REF =  newElementInstalledImageReference()
																			.withImageType(LXC)
																			.withImageName(imageName("JUNIT"))
																			.withImageVersion(version("1.0.0"))
																			.withElementImageState(ACTIVE)
																			.build();

	private static final ElementInstalledImageReference CACHED_IMAGE_REF =  newElementInstalledImageReference()
			   																.withImageType(LXC)
			   																.withImageName(imageName("JUNIT"))
			   																.withImageVersion(version("1.0.0"))
			   																.withElementImageState(CACHED)
			   																.build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Mock
	private Repository repository;

	@Mock
	private Messages messages;

	@Mock
	private SubtransactionService service;
	
	@InjectMocks
	private ElementImageManager manager = new ElementImageManager();

	
	@Test
	public void attempt_to_remove_a_nonexistent_installed_image_fails_silently() {
		when(repository.execute(any(Query.class))).thenReturn(null);
		manager.removeInstalledImage(mock(Element.class), 
									 LXC, 
									 imageName("JUNIT"),
									 version("1.0.0"));
		verify(repository,never()).remove(any());
	}
	
	@Test
	public void attempt_to_remove_an_active_installed_image_raises_conflict_exception() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0341E_ELEMENT_IMAGE_ACTIVE));
		
		Element_Image image = mock(Element_Image.class);
		doReturn(Boolean.TRUE).when(image).isActive();
		when(repository.execute(any(Query.class))).thenReturn(image);

		manager.removeInstalledImage(mock(Element.class), 
									 LXC, 
									 imageName("JUNIT"),
									 version("1.0.0"));
	}
	
	@Test
	public void remove_inactive_installed_image() {
		Element_Image image = mock(Element_Image.class);
		when(repository.execute(any(Query.class))).thenReturn(image);
		
		manager.removeInstalledImage(mock(Element.class), 
									 LXC, 
									 imageName("JUNIT"),
									 version("1.0.0"));
		
		verify(repository).remove(image);
	}
	
	@Test
	public void mark_existing_cached_image_as_active() {
		Element_Image cached = mock(Element_Image.class);
		when(cached.getImageType()).thenReturn(LXC);
		when(cached.getImageName()).thenReturn(imageName("JUNIT"));
		when(cached.getImageVersion()).thenReturn(version("1.0.0"));
		when(cached.getInstallationState()).thenReturn(CACHED);
		when(repository.execute(any(Query.class))).thenReturn(asList(cached));

		manager.storeInstalledImages(mock(Element.class),
									 asList(ACTIVE_IMAGE_REF));
		

		verify(cached).setImageInstallationState(ACTIVE);
		verify(repository,never()).add(cached);
		
	}
	
	@Test
	public void add_new_installed_image_if_not_associated_with_element() {
		Element_Image newImage = mock(Element_Image.class);
		when(newImage.getImageType())
		.thenReturn(LXC);
		
		when(newImage.getImageVersion())
		.thenReturn(version("1.0.0"));
		
		Image image = mock(Image.class);
		when(image.getImageType()).thenReturn(LXC);
		when(image.getImageVersion()).thenReturn(version("1.0.0"));
		
		when(repository.execute(any(Query.class)))
		.thenReturn(emptyList())
		.thenReturn(image);
		
		ArgumentCaptor<Element_Image> imageCaptor = ArgumentCaptor.forClass(Element_Image.class);
		doNothing().when(repository).add(imageCaptor.capture());
		
		manager.storeInstalledImages(mock(Element.class),
										  asList(ACTIVE_IMAGE_REF));
		

		assertEquals(version("1.0.0"),imageCaptor.getValue().getImageVersion());
		assertEquals(LXC,imageCaptor.getValue().getImageType());
		assertTrue(imageCaptor.getValue().isActive());
	}
	
	@Test
	public void attempt_to_register_an_unknown_image_fails_silently() {
		Image image = mock(Image.class);
		when(service.run(any(Flow.class))).thenReturn(image);

		when(repository.execute(any(Query.class)))
		.thenReturn(emptyList())
		.thenReturn(null);

		
		manager.storeInstalledImages(mock(Element.class,withSettings().defaultAnswer(RETURNS_MOCKS)),
										  asList(ACTIVE_IMAGE_REF));
		
		verify(service).run(any(Flow.class));
		verify(repository).add(any(Element_Image.class));
	}
	
	@Test
	public void remove_existing_references() {
		Element_Image cached = mock(Element_Image.class);
		when(cached.getImageType()).thenReturn(LXC);
		when(cached.getImageName()).thenReturn(imageName("JUNIT"));
		when(cached.getImageVersion()).thenReturn(version("1.0.0"));
		when(cached.getInstallationState()).thenReturn(CACHED);
		when(repository.execute(any(Query.class))).thenReturn(asList(cached));

		manager.storeInstalledImages(mock(Element.class),
										  Collections.emptyList());
		

		verify(repository).remove(cached);
	}
	
	
	@Test
	public void removing_list_of_cached_images_fails_if_an_image_is_active() {
		exception.expect(ConflictException.class);
		exception.expect(reason(IVT0341E_ELEMENT_IMAGE_ACTIVE));
		
		Element_Image cached = mock(Element_Image.class);
		doReturn(LXC).when(cached).getImageType();
		doReturn(imageName("JUNIT")).when(cached).getImageName();
		doReturn(version("1.0.0")).when(cached).getImageVersion();
		doReturn(CACHED).when(cached).getInstallationState();
		doReturn(true).when(cached).isActive();
		when(repository.execute(any(Query.class))).thenReturn(asList(cached));

		manager.removeCachedImages(mock(Element.class),
								   asList(CACHED_IMAGE_REF));
		
		
	}

	@Test
	public void removing_list_of_images_ignores_when_image_was_already_removed() {
		when(repository.execute(any(Query.class))).thenReturn(emptyList());

		manager.removeCachedImages(mock(Element.class),
										asList(ACTIVE_IMAGE_REF,CACHED_IMAGE_REF));
		
		verify(repository,never()).remove(any(Element_Image.class));
		
	}
	
	@Test
	public void remove_inactive_cached_images() {
		
		Element_Image cached = mock(Element_Image.class);
		doReturn(LXC).when(cached).getImageType();
		doReturn(imageName("JUNIT")).when(cached).getImageName();
		doReturn(version("1.0.0")).when(cached).getImageVersion();
		doReturn(CACHED).when(cached).getInstallationState();
		doReturn(false).when(cached).isActive();
		when(repository.execute(any(Query.class))).thenReturn(asList(cached));

		manager.removeCachedImages(mock(Element.class),
								   asList(CACHED_IMAGE_REF));
		
	
		verify(repository).remove(cached);	
		
	}
	
	
}
