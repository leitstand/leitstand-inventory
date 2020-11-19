package io.leitstand.inventory.model;

import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.inventory.model.Image.findImageById;
import static io.leitstand.inventory.model.Release.findReleaseByName;
import static io.leitstand.inventory.service.ReasonCode.IVT0200E_IMAGE_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0211I_RELEASE_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0213E_AMBIGUOUS_IMAGE;
import static io.leitstand.inventory.service.ReasonCode.IVT0214I_RELEASE_REMOVED;
import static io.leitstand.inventory.service.ReleaseImage.newReleaseImage;
import static io.leitstand.inventory.service.ReleaseSettings.newReleaseSettings;
import static java.lang.String.format;
import static java.util.Objects.hash;
import static java.util.logging.Logger.getLogger;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseImage;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseSettings;

@Dependent
public class ReleaseManager  {

    private static final Logger LOG = getLogger(ReleaseManager.class.getName());
    
    private static class ImageKey {
        
        private ElementRoleName roleName;
        private PlatformChipsetName chipsetName;
        
        public ImageKey(ElementRoleName roleName,
                        PlatformChipsetName chipsetName) {
            this.roleName = roleName;
            this.chipsetName = chipsetName;
        }
        
        @Override
        public int hashCode() {
            return hash(roleName,chipsetName);
        }
        
        @Override
        public boolean equals(Object o) {
            if(o == null) {
                return false;
            }
            if(getClass() != o.getClass()) {
                return false;
            }
            ImageKey k = (ImageKey) o;
            return Objects.equals(roleName,k.roleName) 
                   && Objects.equals(chipsetName,k.chipsetName);
        }
        
        @Override
        public String toString() {
            return chipsetName+"-"+roleName;
        }
    }
    
    @Inject
    @Inventory
    private Repository repository;
    
    @Inject
    private ReleaseProvider releases;
    
    @Inject
    private Messages messages;
    
    public ReleaseManager() {
        // CDI
    }
    
    protected ReleaseManager(Repository repository, 
                             ReleaseProvider releases,
                             Messages messages) {
        this.repository = repository;
        this.releases = releases;
        this.messages = messages;
    }
    
    static ReleaseSettings settingsOf(Release release) {
        return newReleaseSettings()
               .withReleaseId(release.getReleaseId())
               .withReleaseName(release.getReleaseName())
               .withReleaseState(release.getState())
               .withDescription(release.getDescription())
               .withImages(release
                           .getImages()
                           .stream()
                           .map(image -> newReleaseImage()
                                         .withImageId(image.getImageId())
                                         .withImageName(image.getImageName())
                                         .withImageState(image.getImageState())
                                         .withImageType(image.getImageType())
                                         .withImageVersion(image.getImageVersion())
                                         .withPlatformChipset(image.getPlatformChipset())
                                         .withElementRoles(image.getElementRoleNames())
                                         .build())
                           .collect(toList()))
               .build();
                              
                       
    }
    
    public List<ReleaseSettings> findReleases(String filter) {
        return repository
               .execute(findReleaseByName(filter))
               .stream()
               .map(r -> settingsOf(r))
               .collect(toList());
    }

    public ReleaseSettings getRelease(ReleaseId releaseId) {
        return settingsOf(releases.fetchRelease(releaseId));
    }

    public ReleaseSettings getRelease(ReleaseName releaseName) {
        return settingsOf(releases.fetchRelease(releaseName));
    }

    public boolean storeRelease(ReleaseSettings settings) {
        boolean created = false;
        Release release = releases.tryFetchRelease(settings.getReleaseId());
        if(release == null) {
            release = new Release(settings.getReleaseId(),
                                  settings.getReleaseName());
            repository.add(release);
            repository.flush();
            created = true;
        }
        
        release.setReleaseName(settings.getReleaseName());
        release.setDescription(settings.getDescription());
        List<Image> images = new LinkedList<>();
        Set<ImageKey> unique = new HashSet<>();
        for(ReleaseImage image : settings.getImages()) {
            Image releaseImage = repository.execute(findImageById(image.getImageId()));
            if (releaseImage == null) {
                LOG.fine(() -> format("%s: Failed to register release %s. Image %s does not exist.",
                                      IVT0200E_IMAGE_NOT_FOUND.getReasonCode(),
                                      settings.getReleaseName(),
                                      image.getImageId()));
                
                throw new EntityNotFoundException(IVT0200E_IMAGE_NOT_FOUND,image.getImageId());
            }
            
            PlatformChipsetName chipset = releaseImage.getPlatformChipset();
            for(ElementRoleName roleName : releaseImage.getElementRoleNames()) {
                if(unique.add(new ImageKey(roleName,
                                           releaseImage.getPlatformChipset()))) {
                    images.add(releaseImage);
                } else {
                    LOG.fine(()->format("%s: Release %s (%s) contains ambiguous images for role %s and chipset %s.", 
                                        IVT0213E_AMBIGUOUS_IMAGE,
                                        settings.getReleaseName(),
                                        settings.getReleaseId(),
                                        roleName,
                                        chipset ));
                   
                   throw new ConflictException(IVT0213E_AMBIGUOUS_IMAGE, 
                                               roleName,
                                               releaseImage.getPlatformChipset());
                }                
            }
        }
        release.setImages(images);
        LOG.fine(() -> format("%s: Release %s (%s) stored.",
                              IVT0211I_RELEASE_STORED.getReasonCode(),
                              settings.getReleaseName(),
                              settings.getReleaseId()));
        messages.add(createMessage(IVT0211I_RELEASE_STORED, 
                                   release.getReleaseName()));
        
        return created;
    }


    public void removeRelease(Release release) {
        if(release != null) {
            repository.remove(release);
            LOG.fine(() -> format("%s: Rlease %s (%s) removed.",
                                  IVT0214I_RELEASE_REMOVED.getReasonCode(),
                                  release.getReleaseName(),
                                  release.getReleaseId()));
            
            messages.add(createMessage(IVT0214I_RELEASE_REMOVED, release.getReleaseName()));
        }
    }


}
