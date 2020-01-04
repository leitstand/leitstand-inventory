/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;

/**
 * A collection of all element modules.
 */

public class ElementModules extends BaseElementEnvelope{

	/**
	 * Returns a builder to create an immutable <code>ElementModules</code> instance.
	 * @return a builder to create an immutable <code>ElementModules</code> instance.
	 */
	public static Builder newElementModules(){
		return new Builder();
	}
	
	/**
	 * The builder to create an immutable <code>ElementModules</code> instance.
	 */
	public static class Builder extends BaseElementEnvelopeBuilder<ElementModules, Builder>{
		
		public Builder() {
			super(new ElementModules());
		}
		
		/**
		 * Sets the element hardware modules.
		 * @param modules - the element hardware modules
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModules(ModuleData.Builder... modules) {
			return withModules(stream(modules)
							   .map(ModuleData.Builder::build)
							   .collect(toList()));
		}
		
		/**
		 * Sets the element hardware modules.
		 * @param modules - the element hardware modules
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModules(ModuleData... modules) {
			return withModules(asList(modules));
		}
		
		/**
		 * Sets the element hardware modules.
		 * @param modules - the element hardware modules
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withModules(List<ModuleData> modules) {
			assertNotInvalidated(getClass(), modules);
			object.modules = unmodifiableList(new LinkedList<>(modules));
			return this;
		}
		
	}
	
	private List<ModuleData> modules;
	
	/**
	 * Returns the list of element units.
	 * Returns an empty list if no unit information is available
	 * @return the element units.
	 */
	public List<ModuleData> getModules() {
		return modules;
	}
	
}
