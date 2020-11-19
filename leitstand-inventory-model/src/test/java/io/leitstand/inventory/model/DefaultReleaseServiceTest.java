package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReleaseServiceTest {
    
    private static final ReleaseId RELEASE_ID = randomReleaseId();
    private static final ReleaseName RELEASE_NAME = releaseName("release");

    @Mock
    private ReleaseManager manager;
    
    @Mock
    private ReleaseProvider releases;
    
    @InjectMocks
    private DefaultReleaseService service = new DefaultReleaseService();
    
    @Test
    public void remove_release_by_id() {
        Release release = mock(Release.class);
        
        when(releases.tryFetchRelease(RELEASE_ID)).thenReturn(release);
        service.removeRelease(RELEASE_ID);
        verify(manager).removeRelease(release);
        
    }
    
    @Test
    public void remove_release_by_name() {
        Release release = mock(Release.class);
        
        when(releases.tryFetchRelease(RELEASE_NAME)).thenReturn(release);
        service.removeRelease(RELEASE_NAME);
        verify(manager).removeRelease(release);
        
    }
    
    @Test
    public void remove_unknown_release_by_id() {
        service.removeRelease(RELEASE_ID);
        verify(releases).tryFetchRelease(RELEASE_ID);
        verify(manager,never()).removeRelease(null);
        
    }
    
    @Test
    public void remove_unknown_release_by_name() {
        service.removeRelease(RELEASE_NAME);
        verify(releases).tryFetchRelease(RELEASE_NAME);
        verify(manager,never()).removeRelease(null);
    }
    
}
