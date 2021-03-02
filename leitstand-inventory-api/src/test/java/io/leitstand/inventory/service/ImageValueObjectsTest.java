package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.PlatformChipsetName.platformChipsetName;
import static io.leitstand.inventory.service.RoleImage.newRoleImage;
import static io.leitstand.inventory.service.RoleImages.newRoleImages;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;

public class ImageValueObjectsTest {

    private static final ImageId             IMAGE_ID           = randomImageId();
    private static final ImageName           IMAGE_NAME         = imageName("image");
    private static final Version             IMAGE_VERSION      = new Version(1,0,0);
    private static final ImageType           IMAGE_TYPE         = imageType("type");
    private static final PlatformChipsetName PLATFORM_CHIPSET   = platformChipsetName("chipset");
    private static final ElementRoleName     ELEMENT_ROLE       = elementRoleName("role");
    
    
    @Test
    public void create_role_image() {
        RoleImage image = newRoleImage()
                          .withImageId(IMAGE_ID)
                          .withImageName(IMAGE_NAME)
                          .withImageType(IMAGE_TYPE)
                          .withImageVersion(IMAGE_VERSION)
                          .withPlatformChipset(PLATFORM_CHIPSET)
                          .build();
        
        assertThat(image.getImageId(),is(IMAGE_ID));
        assertThat(image.getImageName(),is(IMAGE_NAME));
        assertThat(image.getImageType(),is(IMAGE_TYPE));
        assertThat(image.getImageVersion(),is(IMAGE_VERSION));
        assertThat(image.getPlatformChipset(),is(PLATFORM_CHIPSET));
    }
    
    @Test
    public void create_role_images() {
        RoleImage image = Mockito.mock(RoleImage.class);
        RoleImages images = newRoleImages()
                            .withElementRole(ELEMENT_ROLE)
                            .withImages(asList(image))
                            .build();
        
        assertThat(images.getElementRole(),is(ELEMENT_ROLE));
        assertThat(images.getImages(),containsExactly(image));
    }
    
}
