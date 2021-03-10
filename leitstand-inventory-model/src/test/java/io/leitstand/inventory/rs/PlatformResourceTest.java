package io.leitstand.inventory.rs;

import static io.leitstand.commons.rs.ReasonCode.VAL0003E_IMMUTABLE_ATTRIBUTE;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.ReasonCode.IVT0904E_PLAFORM_NAME_ALREADY_IN_USE;
import static io.leitstand.testing.ut.Answers.ROLLBACK;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.Family.SUCCESSFUL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.leitstand.commons.UniqueKeyConstraintViolationException;
import io.leitstand.commons.UnprocessableEntityException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.PlatformService;
import io.leitstand.inventory.service.PlatformSettings;

@RunWith(MockitoJUnitRunner.class)
public class PlatformResourceTest {
    
    private static final PlatformId PLATFORM_ID = randomPlatformId();
    private static final PlatformName PLATFORM_NAME = platformName("platform");
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Mock
    private Messages messages;
    
    @Mock
    private PlatformService service;
    
    @InjectMocks
    private PlatformResource resource = new PlatformResource();
    
    
    @Test
    public void get_platforms() {
        List<PlatformSettings> platforms = mock(List.class);
        when(service.getPlatforms("filter")).thenReturn(platforms);
        
        assertSame(platforms,resource.getPlatforms("filter"));
    }
    
    @Test
    public void get_platform_by_id() {
        PlatformSettings platform = mock(PlatformSettings.class);
        when(service.getPlatform(PLATFORM_ID)).thenReturn(platform);
        
        assertSame(platform,resource.getPlatform(PLATFORM_ID));
    }
    
    @Test
    public void get_platform_by_name() {
        PlatformSettings platform = mock(PlatformSettings.class);
        when(service.getPlatform(PLATFORM_NAME)).thenReturn(platform);
        
        assertSame(platform,resource.getPlatform(PLATFORM_NAME));
    }
    
    @Test
    public void add_new_platform_by_name() {
        PlatformSettings platform = mock(PlatformSettings.class);
        when(service.storePlatform(platform)).thenReturn(true);
        
        Response response = resource.storePlatform(PLATFORM_NAME,platform);
        assertThat(response.getStatus(),is(CREATED.getStatusCode()));
    }
    
    @Test
    public void add_new_platform_by_id() {
        PlatformSettings platform = mock(PlatformSettings.class);
        when(platform.getPlatformId()).thenReturn(PLATFORM_ID);
        when(service.storePlatform(platform)).thenReturn(true);
        
        Response response = resource.storePlatform(PLATFORM_ID,platform);
        assertThat(response.getStatus(),is(CREATED.getStatusCode()));
    }
    
    
    @Test
    public void store_platform_by_name() {
        PlatformSettings platform = mock(PlatformSettings.class);
        
        Response response = resource.storePlatform(PLATFORM_NAME,platform);
        assertThat(response.getStatus(),is(OK.getStatusCode()));
        verify(service).storePlatform(platform);
    }
    
    @Test
    public void store_platform_by_id() {
        PlatformSettings platform = mock(PlatformSettings.class);
        when(platform.getPlatformId()).thenReturn(PLATFORM_ID);
        
        Response response = resource.storePlatform(PLATFORM_ID,platform);
        assertThat(response.getStatus(),is(OK.getStatusCode()));
        verify(service).storePlatform(platform);

    }
    
    @Test
    public void cannot_modify_platform_id() {
        exception.expect(UnprocessableEntityException.class);
        exception.expect(reason(VAL0003E_IMMUTABLE_ATTRIBUTE));
        
        PlatformSettings settings = mock(PlatformSettings.class);
        resource.storePlatform(PLATFORM_ID, settings);
    }
    
    @Test
    public void report_unique_key_constraint_violation() {
        exception.expect(UniqueKeyConstraintViolationException.class);
        exception.expect(reason(IVT0904E_PLAFORM_NAME_ALREADY_IN_USE));
        
        PlatformSettings platform = mock(PlatformSettings.class);
        when(platform.getPlatformId()).thenReturn(PLATFORM_ID);
        when(platform.getPlatformName()).thenReturn(PLATFORM_NAME);
        
        when(service.storePlatform(platform)).then(ROLLBACK);
        when(service.getPlatform(PLATFORM_NAME)).thenReturn(platform);
        
        resource.storePlatform(PLATFORM_ID, platform);
    }
    
    
    @Test
    public void remove_platform_by_id() {
        Response response = resource.removePlatform(PLATFORM_ID);
        verify(service).removePlatform(PLATFORM_ID);
        assertThat(response.getStatusInfo().getFamily(),is(SUCCESSFUL));
        
    }
    
    @Test
    public void remove_platform_by_name() {
        Response response = resource.removePlatform(PLATFORM_ID);
        verify(service).removePlatform(PLATFORM_ID);
        assertThat(response.getStatusInfo().getFamily(),is(SUCCESSFUL));
        
    }
}
