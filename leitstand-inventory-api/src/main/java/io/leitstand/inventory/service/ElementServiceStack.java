/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

public class ElementServiceStack extends BaseElementEnvelope {

	public static Builder newElementServiceStack(){
		return new Builder();
	}
	
	public static class Builder extends BaseElementEnvelopeBuilder<ElementServiceStack, Builder> {
		
		public Builder() {
			super(new ElementServiceStack());
		}
		
		public Builder withServices(ServiceInfo... services){
			return withServices(asList(services));
		}

		public Builder withServices(ServiceInfo.Builder... services){
			return withServices(stream(services)
							    .map(ServiceInfo.Builder::build)
							    .collect(toList()));
		}

		
		public Builder withServices(List<ServiceInfo> services){
			assertNotInvalidated(getClass(), object);
			object.services = services;
			return this;
		}

	}
	
	
	@JsonbProperty("stack")
	private List<ServiceInfo> services = emptyList();
	
	public List<ServiceInfo> getServices() {
		return unmodifiableList(services);
	}
	
}