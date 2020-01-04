/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BandwidthTest {

	
	@Test
	public void readKbps() {
		Bandwidth bw = new Bandwidth("64.000 Kbps");
		assertEquals(Bandwidth.Unit.KBPS,bw.getUnit());
		assertEquals(64f,bw.getValue(),0.0001);
		
	}
	
	@Test
	public void readMbps() {
		Bandwidth bw = new Bandwidth("100.000 Mbps");
		assertEquals(Bandwidth.Unit.MBPS,bw.getUnit());
		assertEquals(100f,bw.getValue(),0.0001);
	}
	
	@Test
	public void readGbps() {
		Bandwidth bw = new Bandwidth("10.000 Gbps");
		assertEquals(Bandwidth.Unit.GBPS,bw.getUnit());
		assertEquals(10f,bw.getValue(),0.0001);	
	}
	
	@Test
	public void readTbps() {
		Bandwidth bw = new Bandwidth("1.000 Tbps");
		assertEquals(Bandwidth.Unit.TBPS,bw.getUnit());
		assertEquals(1f,bw.getValue(),0.0001);	
	}

	@Test(expected=IllegalStateException.class)
	public void readUnkownUnit() {
		new Bandwidth("1.000 ubps");
		fail("Exception expected");
	}
	
}
