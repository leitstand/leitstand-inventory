package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.ValueObject;

/**
 * The element image lifecycle state of an element.
 * @see ImageStatisticsElementGroupElementImages
 */
public class ImageStatisticsElementGroupElementImageState extends ValueObject{

    /**
     * Creates a builder for an immutable <code>ImageStatisticsElementGroupElementImageState</code> value object.
     * @return a builder for an immutable <code>ImageStatisticsElementGroupElementImageState</code> value object.
     */
    public static Builder newElementGroupElementImageState() {
        return new Builder();
    }
    
    /**
     * A builder for an immutable <code>ImageStatisticsElementGroupElementImageState</code> value object.
     */
    public static class Builder {
        private ImageStatisticsElementGroupElementImageState element = new ImageStatisticsElementGroupElementImageState();
        
        /**
         * Sets the element ID.
         * @param elementId the element ID.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementId(ElementId elementId) {
            assertNotInvalidated(getClass(), element);
            element.elementId = elementId;
            return this;
        }

        /** 
         * Sets the element name.
         * @param elementName the element name.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementName(ElementName elementName) {
            assertNotInvalidated(getClass(), element);
            element.elementName = elementName;
            return this;
        }
        
        /**
         * Sets the element alias. 
         * @param elementAlias the element alias.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementAlias(ElementAlias elementAlias) {
            assertNotInvalidated(getClass(), element);
            element.elementAlias = elementAlias;
            return this;
        }
        
        /**
         * Sets element role.
         * @param elementRole the element role.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementRole(ElementRoleName elementRole) {
            assertNotInvalidated(getClass(), element);
            element.elementRole = elementRole;
            return this;
        }
        
        /**
         * Sets the element image lifecycle state.
         * @param elementImageState the element image lifecycle state.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementImageState(ElementImageState elementImageState) {
            assertNotInvalidated(getClass(), element);
            element.elementImageState = elementImageState;
            return this;
        }
        
        /**
         * Creates an immutable <code>ImageStatisticsElementGroupElementImageState</code> value object and invalidates this builder.
         * Subsequent invocations of the <code>build()</code> method raise an exception.
         * @return the immutable <code>ImageStatisticsElementGroupElementImageState</code> value object and invalidates this builder.
         */
        public ImageStatisticsElementGroupElementImageState build() {
            try {
                assertNotInvalidated(getClass(), element);
                return this.element;
            } finally {
                this.element = null;
            }
        }
        
    }
    
    
    private ElementId elementId;
    private ElementName elementName;
    private ElementAlias elementAlias;
    private ElementRoleName elementRole;
    private ElementImageState elementImageState;
    
    
    /**
     * Returns the element ID.
     * @return the element ID.
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
     * Returns the element image lifecycle state.
     * @return the element image lifecycle state.
     */
    public ElementImageState getElementImageState() {
        return elementImageState;
    }
    
}
