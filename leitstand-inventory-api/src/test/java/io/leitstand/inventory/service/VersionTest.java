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

import static io.leitstand.inventory.service.Version.version;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VersionTest {
    
    @Test(expected=IllegalArgumentException.class)
    public void create_non_semantic_version_without_prerelease(){
        Version rev = version("1.2.3.4");
        assertEquals(1,rev.getMajorLevel());
        assertEquals(2,rev.getMinorLevel());
        assertEquals(3,rev.getPatchLevel());
        assertEquals("4",rev.getPreRelease());
    }
	
	@Test
	public void create_semantic_version_without_prerelease(){
		Version rev = version("1.2.3");
		assertEquals(1,rev.getMajorLevel());
		assertEquals(2,rev.getMinorLevel());
		assertEquals(3,rev.getPatchLevel());
	}
	
	@Test
	public void create_semantic_version_with_prerelease(){
		Version rev = version("1.2.3-alpha");
		assertEquals(1,rev.getMajorLevel());
		assertEquals(2,rev.getMinorLevel());
		assertEquals(3,rev.getPatchLevel());
		assertEquals("alpha",rev.getPreRelease());
	}
	
	@Test
	public void create_string_representation(){
		assertEquals("1.2.3",new Version(1,2,3).toString());
		assertEquals("1.2.3-alpha",new Version(1,2,3,"alpha").toString());
	}
	
	@Test
	public void major_level_ordering(){
		Version a = version("1.0.0");
		Version b = version("2.0.0");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	@Test
	public void minor_level_ordering(){
		Version a = version("1.1.0");
		Version b = version("1.2.0");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	@Test
	public void patch_level_ordering(){
		Version a = version("1.0.0");
		Version b = version("1.0.1");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	@Test
	public void prerelease_level_ordering(){
		Version a = version("1.0.0-alpha");
		Version b = version("1.0.0-beta");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	
	@Test
	public void release_prerelease_level_ordering(){
		Version a = version("1.0.0-alpha");
		Version b = version("1.0.0");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}

	
	@Test
	public void same_version_ordering(){
		Version a = version("1.0.0");
		Version b = version("1.0.0");
		assertEquals(0,a.compareTo(b));
		
		a = version("1.0.0-alpha");
		b = version("1.0.0-alpha");
		assertEquals(0,a.compareTo(b));
	}

	
}
