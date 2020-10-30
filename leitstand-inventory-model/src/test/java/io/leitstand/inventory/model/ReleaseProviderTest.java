package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ReasonCode.IVT0210E_RELEASE_NOT_FOUND;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Repository;

@RunWith(MockitoJUnitRunner.class)
public class ReleaseProviderTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Mock
    private Repository repository;
    
    @InjectMocks
    private ReleaseProvider releases = new ReleaseProvider();

    
    @Test
    public void try_fetch_release_by_id_returns_null_for_unknown_release_id() {
       assertNull(releases.tryFetchRelease(randomReleaseId()));
    }
    
    @Test
    public void try_fetch_release_by_name_returns_null_for_unknown_release_name() {
        assertNull(releases.tryFetchRelease(releaseName("unknown")));
    }
    
    @Test
    public void fetch_release_by_id_throws_EntityNotFoundException_for_unknown_release_id() {
        exception.expect(EntityNotFoundException.class);
        exception.expect(reason(IVT0210E_RELEASE_NOT_FOUND));
        
        releases.fetchRelease(randomReleaseId());
    }
    
    @Test
    public void fetch_release_by_id_throws_EntityNotFoundException_for_unknown_release_name() {
        exception.expect(EntityNotFoundException.class);
        exception.expect(reason(IVT0210E_RELEASE_NOT_FOUND));
        
        releases.fetchRelease(releaseName("unknown"));
    }
    
    @Test
    public void try_fetch_release_by_id() {
        Release release = mock(Release.class);
        when(repository.execute(any(Query.class))).thenReturn(release);
        
        assertSame(release,releases.tryFetchRelease(randomReleaseId()));
    }
    
    @Test
    public void try_fetch_release_by_name() {
        Release release = mock(Release.class);
        when(repository.execute(any(Query.class))).thenReturn(release);
        
        assertSame(release,releases.tryFetchRelease(releaseName("release")));
    }
    
    @Test
    public void fetch_release_by_id() {
        Release release = mock(Release.class);
        when(repository.execute(any(Query.class))).thenReturn(release);
        
        assertSame(release,releases.fetchRelease(randomReleaseId()));
    }
    
    @Test
    public void fetch_release_by_name() {
        Release release = mock(Release.class);
        when(repository.execute(any(Query.class))).thenReturn(release);
        
        assertSame(release,releases.fetchRelease(releaseName("release")));
    }

    
    
}
