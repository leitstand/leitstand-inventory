/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import static io.leitstand.event.queue.service.DomainEvent.newDomainEvent;
import static io.leitstand.event.queue.service.TopicName.topicName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import io.leitstand.event.queue.service.DomainEvent;


@ApplicationScoped
public class DomainEventAdapter {

	@Inject
	private Event<DomainEvent<?>> bus;
	
	public void publishElementEvent(@Observes ElementEvent event) {
		bus.fire(newDomainEvent()
				 .withTopicName(topicName("element"))
				 .withPayload(event)
				 .build());
	}
	
	public void publishElementEvent(@Observes DnsZoneEvent event) {
		bus.fire(newDomainEvent()
				 .withTopicName(topicName("element"))
				 .withPayload(event)
				 .build());
	}
	
	public void publishImageEvent(@Observes ImageEvent event) {
		bus.fire(newDomainEvent()
				 .withTopicName(topicName("image"))
				 .withPayload(event)
				 .build());
		
	}
	
}
