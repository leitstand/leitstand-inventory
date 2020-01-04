/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VersionTest {
	
	@Test
	public void create_version_without_prerelease(){
		Version rev = Version.valueOf("1.2.3");
		assertEquals(1,rev.getMajorLevel());
		assertEquals(2,rev.getMinorLevel());
		assertEquals(3,rev.getPatchLevel());
	}
	
	@Test
	public void create_version_with_prerelease(){
		Version rev = Version.valueOf("1.2.3-alpha");
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
		Version a = Version.valueOf("1.0.0");
		Version b = Version.valueOf("2.0.0");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	@Test
	public void minor_level_ordering(){
		Version a = Version.valueOf("1.1.0");
		Version b = Version.valueOf("1.2.0");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	@Test
	public void patch_level_ordering(){
		Version a = Version.valueOf("1.0.0");
		Version b = Version.valueOf("1.0.1");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	@Test
	public void prerelease_level_ordering(){
		Version a = Version.valueOf("1.0.0-alpha");
		Version b = Version.valueOf("1.0.0-beta");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}
	
	
	@Test
	public void release_prerelease_level_ordering(){
		Version a = Version.valueOf("1.0.0-alpha");
		Version b = Version.valueOf("1.0.0");
		assertEquals(-1,a.compareTo(b));
		assertEquals(1,b.compareTo(a));
	}

	
	@Test
	public void same_version_ordering(){
		Version a = Version.valueOf("1.0.0");
		Version b = Version.valueOf("1.0.0");
		assertEquals(0,a.compareTo(b));
		
		a = Version.valueOf("1.0.0-alpha");
		b = Version.valueOf("1.0.0-alpha");
		assertEquals(0,a.compareTo(b));
	}

	
}
