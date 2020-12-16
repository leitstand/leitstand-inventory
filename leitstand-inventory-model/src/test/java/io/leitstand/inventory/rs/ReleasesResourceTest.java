package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.ReasonCode.IVT0212E_RELEASE_NAME_ALREADY_IN_USE;
import static io.leitstand.inventory.service.ReleaseId.randomReleaseId;
import static io.leitstand.inventory.service.ReleaseName.releaseName;
import static io.leitstand.inventory.service.ReleaseSettings.newReleaseSettings;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.transaction.RollbackException;
import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.inventory.service.ReleaseId;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ReleaseService;
import io.leitstand.inventory.service.ReleaseSettings;

@RunWith(MockitoJUnitRunner.class)
public class ReleasesResourceTest {
    
    private static final ReleaseId RELEASE_ID = randomReleaseId();
    private static final ReleaseName RELEASE_NAME = releaseName("release");

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Mock
    private ReleaseService service;
    
    @InjectMocks
    private ReleasesResource resource = new ReleasesResource();
    
    
    @Test
    public void find_releases() {
        resource.findReleases("filter");
        verify(service).findReleases("filter");
    }
    
    @Test
    public void get_release_by_id() {
        ReleaseSettings settings = mock(ReleaseSettings.class);
        when(service.getRelease(RELEASE_ID)).thenReturn(settings);
        assertSame(settings,resource.getRelease(RELEASE_ID));
    }
    
    @Test
    public void get_release_by_name() {
        ReleaseSettings settings = mock(ReleaseSettings.class);
        when(service.getRelease(RELEASE_NAME)).thenReturn(settings);
        assertSame(settings,resource.getRelease(RELEASE_NAME));
    }
    
    
    @Test
    public void remove_release_by_id() {
        resource.removeRelease(RELEASE_ID);
        verify(service).removeRelease(RELEASE_ID);
    }
    
    @Test
    public void remove_release_by_name() {
        resource.removeRelease(RELEASE_NAME);
        verify(service).removeRelease(RELEASE_NAME);
    }
    
    @Test
    public void store_release_settings() {
        ReleaseSettings settings = mock(ReleaseSettings.class);
        resource.storeRelease(settings);
        verify(service).storeRelease(settings);
        
    }
    
    @Test
    public void cannot_use_different_release_ids() {
        exception.expect(UnprocessableEntityException.class);
        exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));

        resource.storeRelease(randomReleaseId(), newReleaseSettings().build());
    }
    
    @Test
    public void report_duplicate_key() {
        exception.expect(UniqueKeyConstraintViolationException.class);
        exception.expect(reason(IVT0212E_RELEASE_NAME_ALREADY_IN_USE));
        
        ReleaseSettings settings = newReleaseSettings()
                                   .withReleaseName(RELEASE_NAME)
                                   .build();
        
        when(service.storeRelease(settings)).thenAnswer( new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new RollbackException();
            }
            
        });
        when(service.getRelease(RELEASE_NAME)).thenReturn(settings);
       
        resource.storeRelease(settings);
    }
    
    @Test
    public void create_new_release() {
        ReleaseSettings settings = newReleaseSettings().build();
        when(service.storeRelease(settings)).thenReturn(true);
        Response response = resource.storeRelease(settings);
        assertThat(response.getStatus(),is(201));
        
    }
    
}
