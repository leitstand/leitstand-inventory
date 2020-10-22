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

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class BandwidthValuesTest {
    
    
    @Parameters    
    public static List<Object[]> bandwidths(){
        
        return asList(new Object[][] {
            {"64.000 Kbps", 64f, Bandwidth.Unit.KBPS},
            {"100.000 Mbps", 100f, Bandwidth.Unit.MBPS},
            {"10.000 Gbps", 10f, Bandwidth.Unit.GBPS},
            {"1.000 Tbps", 1f, Bandwidth.Unit.TBPS},
            {"10Mbps", 10f, Bandwidth.Unit.MBPS},
            {"10Gbps", 10f, Bandwidth.Unit.GBPS},
            {"100Gbps", 100f, Bandwidth.Unit.GBPS},
        });
        
    }
    
    private String bandwidth;
    private float bwValue;
    private Bandwidth.Unit bwUnit;
    
    public BandwidthValuesTest(String bandwidth, float bwValue, Bandwidth.Unit bwUnit) {
        this.bandwidth = bandwidth;
        this.bwValue = bwValue;
        this.bwUnit = bwUnit;
    }
    
	
    @Test
    public void string_to_bandwidth_conversion() {
        Bandwidth bw = new Bandwidth(bandwidth);
        assertEquals(bwUnit,bw.getUnit());
        assertEquals(bwValue,bw.getValue(),0.0001);
    }
    
}
