package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.ReasonCode.IVT0211I_RELEASE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0214I_RELEASE_REMOVED;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseImage.newReleaseImage;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static io.leitstand.inventory.service.ReleaseSettings.newReleaseSettings;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.hasSizeOf;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import io.leitstand.commons.messages.Message;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseService;
import io.leitstand.inventory.service.ReleaseSettings;
import io.leitstand.inventory.service.ReleaseState;
import io.leitstand.inventory.service.Version;

public class ReleaseServiceIT extends InventoryIT{

    
    private static final ReleaseId RELEASE_ID = randomReleaseId();
    private static final ReleaseName RELEASE_NAME = releaseName("release");
    private static final ImageId IMAGE_ID = randomImageId();
    private static final ImageName IMAGE_NAME = imageName("image");
    private static final ImageType IMAGE_TYPE = imageType("ONL");
    private static final Version IMAGE_VERSION = new Version(1,0,0);
    private static final ElementRoleName ELEMENT_ROLE = elementRoleName("role");
    private static final PlatformChipsetName PLATFORM_CHIPSET = platformChipsetName("chipset");
    
    private ReleaseService service;
    private ArgumentCaptor<Message> messageCaptor;
    private Repository repository;
    
    @Before
    public void initTestEnvironment() {
        this.repository = new Repository(getEntityManager());
        ReleaseProvider releases = new ReleaseProvider(repository);
        Messages messages = mock(Messages.class);
        messageCaptor = ArgumentCaptor.forClass(Message.class);
        doNothing().when(messages).add(messageCaptor.capture());
        
        service = new DefaultReleaseService(releases,
                                            new ReleaseManager(repository,
                                                               releases,  
                                                               messages));
        
        transaction(() -> {
            ElementRole role = new ElementRole(ELEMENT_ROLE, DATA);
            repository.add(role);
            
            Image image = new Image(IMAGE_ID, 
                                    IMAGE_TYPE, 
                                    IMAGE_NAME,
                                    asList(role),
                                    PLATFORM_CHIPSET,
                                    IMAGE_VERSION); 
            repository.add(image);
         });
    }
    
    @Test
    public void store_empty_release() {
        transaction(() -> {
            
            ReleaseSettings release = newReleaseSettings()
                                      .withReleaseId(RELEASE_ID)
                                      .withReleaseName(RELEASE_NAME)
                                      .withReleaseState(ReleaseState.CANDIDATE)
                                      .withDescription("description")
                                      .build();
            
            boolean created = service.storeRelease(release);
            assertTrue(created);
            assertEquals(IVT0211I_RELEASE_STORED.getReasonCode(),
                         messageCaptor.getValue().getReason());

        });
        
        transaction(() -> {
           ReleaseSettings release = service.getRelease(RELEASE_ID);
           assertEquals(RELEASE_ID,release.getReleaseId());
           assertEquals(RELEASE_NAME,release.getReleaseName());
           assertEquals(ReleaseState.CANDIDATE,release.getReleaseState());
           assertEquals("description",release.getDescription());
        });
        
    }

    @Test
    public void remove_empty_release() {
        transaction(() -> {
            
            ReleaseSettings release = newReleaseSettings()
                                      .withReleaseId(RELEASE_ID)
                                      .withReleaseName(RELEASE_NAME)
                                      .withReleaseState(ReleaseState.CANDIDATE)
                                      .withDescription("description")
                                      .build();
            
            boolean created = service.storeRelease(release);
            assertTrue(created);
        });
        
        transaction(() -> {
           service.removeRelease(RELEASE_ID);
           assertEquals(IVT0214I_RELEASE_REMOVED.getReasonCode(),
                        messageCaptor.getValue().getReason());
        });
    }
    
    @Test
    public void store_release() {


        
        transaction(() -> {
            
            ReleaseSettings release = newReleaseSettings()
                                      .withReleaseId(RELEASE_ID)
                                      .withReleaseName(RELEASE_NAME)
                                      .withReleaseState(ReleaseState.CANDIDATE)
                                      .withDescription("description")
                                      .withImages(newReleaseImage()
                                                  .withImageId(IMAGE_ID))
                                      .build();
            
            service.storeRelease(release);
            assertEquals(IVT0211I_RELEASE_STORED.getReasonCode(),
                         messageCaptor.getValue().getReason());

        });
   
        transaction(() -> {
            ReleaseSettings release = service.getRelease(RELEASE_ID);
            assertEquals(RELEASE_ID,release.getReleaseId());
            assertEquals(RELEASE_NAME,release.getReleaseName());
            assertEquals(ReleaseState.CANDIDATE,release.getReleaseState());
            assertEquals("description",release.getDescription());
            assertThat(release.getImages(),hasSizeOf(1));
            assertEquals(IMAGE_ID,release.getImages().get(0).getImageId());
            assertEquals(IMAGE_NAME,release.getImages().get(0).getImageName());
            assertEquals(IMAGE_TYPE,release.getImages().get(0).getImageType());
            assertEquals(IMAGE_VERSION,release.getImages().get(0).getImageVersion());
            assertEquals(PLATFORM_CHIPSET,release.getImages().get(0).getPlatformChipset());
            assertTrue(release.getImages().get(0).getElementRoles().contains(ELEMENT_ROLE));
        });        
        
    }
    
    

    @Test
    public void remove_release() {


        
        transaction(() -> {
            
            ReleaseSettings release = newReleaseSettings()
                                      .withReleaseId(RELEASE_ID)
                                      .withReleaseName(RELEASE_NAME)
                                      .withReleaseState(ReleaseState.CANDIDATE)
                                      .withDescription("description")
                                      .withImages(newReleaseImage()
                                                  .withImageId(IMAGE_ID))
                                      .build();
            
            service.storeRelease(release);

        });
   
        transaction(() -> {
            service.removeRelease(RELEASE_ID);
            assertEquals(IVT0214I_RELEASE_REMOVED.getReasonCode(),
                         messageCaptor.getValue().getReason());
        });        
        
        transaction(() -> {
           assertNotNull(repository.execute(Image.findImageById(IMAGE_ID))); 
        });
    }
    
    
    
}
