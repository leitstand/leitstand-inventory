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
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.ElementId.randomElementId;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * The <code>BaseElementEnvelope</code> contains the attributes shared with every element-related message.
 */
public class BaseElementEnvelope extends BaseElementGroupEnvelope {

    /**
     * Base class for all element-related value object builders.
     * @param <T> the value object type
     * @param <B> the value object builder
     */
	@SuppressWarnings("unchecked")
	public static class BaseElementEnvelopeBuilder<T extends BaseElementEnvelope, B extends BaseElementEnvelope.BaseElementEnvelopeBuilder<T,B>> extends BaseElementGroupEnvelopeBuilder<T, B>{
		
	    /**
	     * Creates a new base builder
	     * @param object the object under construction
	     */
		protected BaseElementEnvelopeBuilder(T object) {
			super(object);
		}
		
		/**
		 * Sets the element id.
		 * @param id the element id
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementId(ElementId elementId) {
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementId = elementId;
			return (B) this;
		}

		/**
		 * Sets the element name.
		 * @param name the element name
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementName(ElementName elementName) {
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementName = elementName;
			return (B) this;
		}

		/**
		 * Sets the element alias.
		 * @param alias the element alias
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementAlias(ElementAlias elementAlias) {
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementAlias = elementAlias;
			return (B) this;
		}
		
		/**
		 * Sets the element role.
		 * @param role the element role
		 * @return a reference to this builder to continue with object creation
		 */
		public B withElementRole(ElementRoleName role){
			assertNotInvalidated(getClass(), object);
			((BaseElementEnvelope)object).elementRole = role;
			return (B) this;
		}
		
		/**
		 * Sets the administrative state of this element.
		 * @param administrativeState the administrative state
		 * @return a reference to this builder to continue with object creation
		 */
		public B withAdministrativeState(AdministrativeState administrativeState) {
		    assertNotInvalidated(getClass(),object);
		    ((BaseElementEnvelope)object).administrativeState = administrativeState;
		    return (B) this;
		}
		
	    /**
         * Sets the operational state of this element.
         * @param operationalState the operational state
         * @return a reference to this builder to continue with object creation
         */
        public B withOperationalState(OperationalState operationalState) {
            assertNotInvalidated(getClass(),object);
            ((BaseElementEnvelope)object).operationalState = operationalState;
            return (B) this;
        }
        
        /**
         * Sets the date when this element has been last modified.
         * @param administrativeState the administrative state
         * @return a reference to this builder to continue with object creation
         */
        public B withDateModified(Date dateModified) {
            assertNotInvalidated(getClass(),object);
            if(dateModified != null) {
                ((BaseElementEnvelope)object).dateModified = new Date(dateModified.getTime());
            }
            return (B) this;
        }
        
        /**
         * Copies all setting of the given element envelope to this envelope.
         * This is a convenience method 
         * @param envelope
         * @return
         */
        public B withElement(BaseElementEnvelope envelope) {
            assertNotInvalidated(getClass(), object);
            super.withGroup(envelope);
            withAdministrativeState(envelope.getAdministrativeState());
            withDateModified(envelope.getDateModified());
            withElementAlias(envelope.getElementAlias());
            withElementId(envelope.getElementId());
            withElementName(envelope.getElementName());
            withElementRole(envelope.getElementRole());
            withOperationalState(envelope.getOperationalState());
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
    @NotNull(message="{element_role.required}")
	private ElementRoleName elementRole;
	
	@Valid
	@NotNull(message="{administrative_state.required}")
	private AdministrativeState administrativeState;

	@Valid
    @NotNull(message="{operational_state.required}")
	private OperationalState operationalState;

	private Date dateModified; 

	/**
	 * Returns the element ID
	 * @return the element ID
	 */
	public ElementId getElementId() {
		return elementId;
	}
	
	/**
	 * Returns the element name.
	 * @return the element name.
	 */
	public ElementName getElementName() {
		return elementName;
	}
	
	/**
	 * Returns the element role.
	 * @return the element role.
	 */
	public ElementRoleName getElementRole() {
		return elementRole;
	}
	
	/**
	 * Returns the element alias.
	 * @return the element alias.
	 */
	public ElementAlias getElementAlias() {
		return elementAlias;
	}
	
	/**
	 * Returns the element administrative state.
	 * @return the element administrative state.
	 */
	public AdministrativeState getAdministrativeState() {
        return administrativeState;
    }
	
	/**
	 * Returns the element operational state.
	 * @return the element operational state.
	 */
	public OperationalState getOperationalState() {
        return operationalState;
    }
	
	/**
	 * Returns the last modification date.
	 * @return the last modification date.
	 */
	public Date getDateModified() {
        return dateModified;
    }
	
}
