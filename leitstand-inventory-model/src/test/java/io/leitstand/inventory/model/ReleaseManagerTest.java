package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.ReasonCode.IVT0200E_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0213E_AMBIGUOUS_IMAGE;
import static io.leitstand.inventory.service.ReasonCode.IVT0214I_RELEASE_REMOVED;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseImage.newReleaseImage;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static io.leitstand.inventory.service.ReleaseSettings.newReleaseSettings;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseSettings;

@RunWith(MockitoJUnitRunner.class)
public class ReleaseManagerTest {
    
    private static final ReleaseId RELEASE_ID = randomReleaseId();
    private static final ReleaseName RELEASE_NAME = releaseName("release");
    private static final ImageId IMAGE_ID = randomImageId();

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Mock
    private Repository repository;
    
    @Mock
    private ReleaseProvider releases;
    
    @Mock
    private Messages messages;
    
    @InjectMocks
    private ReleaseManager manager = new ReleaseManager();
    private ArgumentCaptor<Message> messageCaptor;
   
    @Before
    public void initTestEnvironment() {
        messageCaptor = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messages).add(messageCaptor.capture());
    }
    
    @Test
    public void throws_EntityNotFoundException_for_unknown_images() {
        exception.expect(EntityNotFoundException.class);
        exception.expect(reason(IVT0200E_IMAGE_NOT_FOUND));
        
        ReleaseSettings release = newReleaseSettings()
                                  .withReleaseId(RELEASE_ID)
                                  .withReleaseName(RELEASE_NAME)
                                  .withImages(newReleaseImage()
                                              .withImageId(IMAGE_ID))
                                  .build();
        
        manager.storeRelease(release);
    }
    
    
    @Test
    public void throws_ConflictException_when_two_different_images_for_the_same_chipset_and_platform_are_selected() {
        exception.expect(ConflictException.class);
        exception.expect(reason(IVT0213E_AMBIGUOUS_IMAGE));
        
        Image image = mock(Image.class);
        when(image.getElementRoleNames()).thenReturn(asList(elementRoleName("role")));
        when(image.getPlatformChipset()).thenReturn(platformChipsetName("chipset"));
        when(repository.execute(any(Query.class))).thenReturn(image)
                                                  .thenReturn(image);
        
        ReleaseSettings release = newReleaseSettings()
                                  .withReleaseId(RELEASE_ID)
                                  .withReleaseName(RELEASE_NAME)
                                  .withImages(newReleaseImage()
                                              .withImageId(IMAGE_ID),
                                              newReleaseImage()
                                              .withImageId(IMAGE_ID))
                                  .build();
        
        manager.storeRelease(release);
        
    }
    
    @Test
    public void remove_release() {
        Release release = mock(Release.class);
        manager.removeRelease(release);
        verify(repository).remove(release);
        assertEquals(IVT0214I_RELEASE_REMOVED.getReasonCode(),
                     messageCaptor.getValue().getReason());
        
    }
    
    @Test
    public void do_nothing_when_supposed_to_remove_a_null_release() {
        manager.removeRelease(null);
        verify(repository,never()).remove(null);
    }
    
}
