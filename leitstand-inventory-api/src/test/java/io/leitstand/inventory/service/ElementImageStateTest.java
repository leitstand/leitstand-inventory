package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ElementImageState.ACTIVE;
import static io.leitstand.inventory.service.ElementImageState.CACHED;
import static io.leitstand.inventory.service.ElementImageState.PULL;
import static io.leitstand.inventory.service.ElementImageState.toElementImageState;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ElementImageStateTest {

    @Test
    public void null_element_image_state_defaults_to_pull() {
        assertThat(toElementImageState(null),is(PULL));
    }
    
    @Test
    public void empty_element_image_state_defaults_to_pull() {
        assertThat(toElementImageState(""),is(PULL));
    }
    
    @Test
    public void pull_element_image_state() {
        assertThat(toElementImageState("PULL"),is(PULL));
    }

    @Test
    public void cached_element_image_state() {
        assertThat(toElementImageState("CACHED"),is(CACHED));
    }
    
    @Test
    public void active_element_image_state() {
        assertThat(toElementImageState("ACTIVE"),is(ACTIVE));
    }
    
    @Test
    public void active_element_is_cached() {
        assertTrue(ACTIVE.isCached());
    }
    
    @Test
    public void cached_element_is_cached() {
        assertTrue(CACHED.isCached());
    }
    
    @Test
    public void pull_element_is_not_cached() {
        assertFalse(PULL.isCached());
    }
    
}
