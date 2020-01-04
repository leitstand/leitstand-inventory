/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class DnsZoneRemovedEvent extends DnsZoneEvent{

	public static Builder newDnsZoneRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends DnsZoneEventBuilder<DnsZoneRemovedEvent,Builder> {
		protected Builder() {
			super(new DnsZoneRemovedEvent());
		}
	}
	
	
}
