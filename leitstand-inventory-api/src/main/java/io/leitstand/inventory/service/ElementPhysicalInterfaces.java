/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;

/**
 * A summary of all physical interfaces of a certain element.
 */

public class ElementPhysicalInterfaces extends BaseElementEnvelope{

	/**
	 * Returns a builder to create an immutable <code>ElementPhysicalInterfaces</code> instance.
	 * @return a builder to create an immutable <code>ElementPhysicalInterfaces</code> instance.
	 */
	public static Builder newPhysicalInterfaces(){
		return new Builder();
	}
	
	/**
	 * The builder of an immutable <code>ElementPhysicalInterfaces</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementPhysicalInterfaces, Builder>{
		
		
		public Builder() {
			super(new ElementPhysicalInterfaces());
		}
	
		/**
		 * Sets the physical interfaces of the element.
		 * @param interfaces - the physical interfaces
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterface(ElementPhysicalInterfaceData.Builder... interfaces){
			return withPhysicalInterfaces(stream(interfaces)
										 .map(ElementPhysicalInterfaceData.Builder::build)
										 .collect(toList()));
		}
		
		
		/**
		 * Sets the physical interfaces of the element.
		 * @param interfaces - the physical interfaces
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterface(ElementPhysicalInterfaceData... interfaces){
			assertNotInvalidated(getClass(), object);
			return withPhysicalInterfaces(asList(interfaces));
		}
		
		/**
		 * Sets the physical interfaces of the element.
		 * @param interfaces - the physical interfaces
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withPhysicalInterfaces(List<ElementPhysicalInterfaceData> physicalInterface){
			assertNotInvalidated(getClass(), object);
			object.physicalInterfaces = unmodifiableList(new ArrayList<>(physicalInterface));
			return this;
		}
		
	}
	
	
	
	@JsonbProperty("physical_interfaces")
	private List<ElementPhysicalInterfaceData> physicalInterfaces;
	
	/**
	 * Returns an immutable list of all
	 * @return
	 */
	public List<ElementPhysicalInterfaceData> getPhysicalInterfaces() {
		return physicalInterfaces;
	}
}
