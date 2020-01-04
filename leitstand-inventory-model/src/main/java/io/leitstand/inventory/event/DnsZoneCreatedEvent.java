/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class DnsZoneCreatedEvent extends DnsZoneEvent{

	public static Builder newDnsZoneCreatedEvent() {
		return new Builder();
	}
	
	public static class Builder extends DnsZoneEventBuilder<DnsZoneCreatedEvent,Builder> {
		protected Builder() {
			super(new DnsZoneCreatedEvent());
		}
	}
	
	
}
