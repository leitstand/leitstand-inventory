/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;


/**
 * A summary of all logical interfaces of a certain element.
 */

public class ElementLogicalInterfaces extends BaseElementEnvelope{

	/**
	 * Returns a builder to create an immutable <code>ElementLogicalInterfaces</code> instance.
	 * @return a builder to create an immutable <code>ElementLogicalInterface</code> instance.
	 */
	public static Builder newLogicalInterfaces(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementLogicalInterfaces</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementLogicalInterfaces,Builder>{
		
		public Builder() {
			super(new ElementLogicalInterfaces());
		}
		
		/**
		 * Sets all logical interfaces available on the element.
		 * @param interfaces - the logical interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterface(ElementLogicalInterfaceData.Builder... interfaces){
			assertNotInvalidated(getClass(), object);
			return withLogicalInterface(stream(interfaces)
									   .map(ElementLogicalInterfaceData.Builder::build)
									   .collect(toList()));
		}
		
		/**
		 * Sets all logical interfaces available on the element.
		 * @param interfaces - the logical interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterface(ElementLogicalInterfaceData... interfaces){
			return withLogicalInterface(Arrays.asList(interfaces));
		}

		/**
		 * Sets all logical interfaces available on the element.
		 * @param interfaces - the logical interfaces
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withLogicalInterface(List<ElementLogicalInterfaceData> logicalInterface){
			assertNotInvalidated(getClass(), object);
			object.logicalInterfaces = Collections.unmodifiableList(new ArrayList<>(logicalInterface));
			return this;
		}
		
	}
	
	@JsonbProperty("logical_interfaces")
	private List<ElementLogicalInterfaceData> logicalInterfaces;
	
	/**
	 * Returns an immutable list of all logical interfaces defined on the element.
	 * @return the list of all logical interfaces
	 */
	public List<ElementLogicalInterfaceData> getLogicalInterfaces() {
		return logicalInterfaces;
	}
}
