package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseImage.newReleaseImage;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static io.leitstand.inventory.service.ReleaseRef.newReleaseReference;
import static io.leitstand.inventory.service.ReleaseSettings.newReleaseSettings;
import static io.leitstand.inventory.service.ReleaseState.RELEASE;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ReleaseValueObjectTest {
    
    private static final ReleaseId           RELEASE_ID    = randomReleaseId();
    private static final ReleaseName         RELEASE_NAME  = releaseName("release");
    private static final String              DESCRIPTION   = "description";
    private static final ImageId             IMAGE_ID      = randomImageId();
    private static final ImageName           IMAGE_NAME    = imageName("image");
    private static final ImageType           IMAGE_TYPE    = imageType("image-type");
    private static final Version             IMAGE_VERSION = version("1.0.0");
    private static final ElementRoleName     ELEMENT_ROLE  = elementRoleName("role");
    private static final PlatformChipsetName CHIPSET       = PlatformChipsetName.platformChipsetName("chipset");
    
    @Test
    public void create_release_reference() {
        ReleaseRef ref = newReleaseReference()
                         .withReleaseId(RELEASE_ID)
                         .withReleaseName(RELEASE_NAME)
                         .withReleaseState(RELEASE)
                         .build();
        
        assertThat(ref.getReleaseId(), is(RELEASE_ID));
        assertThat(ref.getReleaseName(), is(RELEASE_NAME));
        assertThat(ref.getReleaseState(), is(RELEASE));
    }
    
    @Test
    public void create_release_settings() {
        ReleaseImage image = mock(ReleaseImage.class);
        
        ReleaseSettings release = newReleaseSettings()
                                  .withReleaseId(RELEASE_ID)
                                  .withReleaseName(RELEASE_NAME)
                                  .withReleaseState(RELEASE)
                                  .withImages(asList(image))
                                  .withDescription(DESCRIPTION)
                                  .build();
        
        assertThat(release.getReleaseId(), is(RELEASE_ID));
        assertThat(release.getReleaseName(), is(RELEASE_NAME));
        assertThat(release.getReleaseState(), is(RELEASE));
        assertThat(release.getDescription(),is(DESCRIPTION));
        assertThat(release.getImages(),containsExactly(image));
    }
    
    
    @Test
    public void create_release_image() {
        ReleaseImage image = newReleaseImage()
                             .withElementRoles(ELEMENT_ROLE)
                             .withImageId(IMAGE_ID)
                             .withImageName(IMAGE_NAME)
                             .withImageState(CANDIDATE)
                             .withImageType(IMAGE_TYPE)
                             .withImageVersion(IMAGE_VERSION)
                             .withPlatformChipset(CHIPSET)
                             .build();
       
        assertThat(image.getElementRoles(),containsExactly(ELEMENT_ROLE));
        assertThat(image.getImageId(),is(IMAGE_ID));
        assertThat(image.getImageName(),is(IMAGE_NAME));
        assertThat(image.getImageState(),is(CANDIDATE));
        assertThat(image.getImageType(),is(IMAGE_TYPE));
        assertThat(image.getImageVersion(),is(IMAGE_VERSION));
        assertThat(image.getPlatformChipset(),is(CHIPSET));
    }
    
}
