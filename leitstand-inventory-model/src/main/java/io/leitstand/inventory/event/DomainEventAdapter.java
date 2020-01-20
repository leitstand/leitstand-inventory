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
