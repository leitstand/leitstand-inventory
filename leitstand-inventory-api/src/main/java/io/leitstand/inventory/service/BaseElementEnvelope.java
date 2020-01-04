/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementId.randomElementId;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BaseElementEnvelope extends BaseElementGroupEnvelope {

	public static class BaseElementEnvelopeBuilder<T extends BaseElementEnvelope, B extends BaseElementEnvelope.BaseElementEnvelopeBuilder<T,B>> extends BaseElementGroupEnvelopeBuilder<T, B>{
		
		protected BaseElementEnvelopeBuilder(T object) {
			super(object);
		}
		
		/**
		 * Sets the element id.
		 * @param id - the element id
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementId(ElementId elementId) {
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementId = elementId;
			return (B) this;
		}

		/**
		 * Sets the element name.
		 * @param name - the element name
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementName = elementName;
			return (B) this;
		}

		/**
		 * Sets the element alias.
		 * @param alias - the element alias
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementAlias(ElementAlias elementAlias) {
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementAlias = elementAlias;
			return (B) this;
		}
		
		/**
		 * Sets the element role.
		 * @param role - the element role
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementRole(ElementRoleName role){
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementRole = role;
			return (B) this;
		}
		
	}
	
	
	@Valid
	@NotNull(message="{element_id.required}")
	private ElementId elementId = randomElementId();

	@Valid
	@NotNull(message="{element_name.required}")
	private ElementName elementName;

	private ElementAlias elementAlias;
	
	@Valid
	private ElementRoleName elementRole;

	
	public ElementId getElementId() {
		return elementId;
	}
	
	public ElementName getElementName() {
		return elementName;
	}
	
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	public ElementAlias getElementAlias() {
		return elementAlias;
	}
	
}
