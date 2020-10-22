/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.Bandwidth.bandwidth;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BandwidthTest {


    @Test(expected=IllegalStateException.class)
    public void unknown_unit_raises_exception() {
        bandwidth("1.000 ubps");
        fail("Exception expected");
    }
        
    @Test
    public void empty_string_is_mapped_to_null() {
        assertNull(bandwidth(""));
    }
    
    @Test
    public void null_string_is_mapped_to_null() {
        assertNull(bandwidth(null));
    }
    
}
