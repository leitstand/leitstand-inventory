package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.ValueObject;

public class ImageStatisticsElementGroupElementImageState extends ValueObject{

    public static Builder newElementGroupElementImageState() {
        return new Builder();
    }
    
    public static class Builder {
        private ImageStatisticsElementGroupElementImageState element = new ImageStatisticsElementGroupElementImageState();
        
        public Builder withElementId(ElementId elementId) {
            assertNotInvalidated(getClass(), element);
            element.elementId = elementId;
            return this;
        }

        public Builder withElementName(ElementName elementName) {
            assertNotInvalidated(getClass(), element);
            element.elementName = elementName;
            return this;
        }
        
        public Builder withElementAlias(ElementAlias elementAlias) {
            assertNotInvalidated(getClass(), element);
            element.elementAlias = elementAlias;
            return this;
        }
        
        public Builder withElementRole(ElementRoleName elementRole) {
            assertNotInvalidated(getClass(), element);
            element.elementRole = elementRole;
            return this;
        }
        
        public Builder withElementImageState(ElementImageState elementImageState) {
            assertNotInvalidated(getClass(), element);
            element.elementImageState = elementImageState;
            return this;
        }
        
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
    
    public ElementImageState getElementImageState() {
        return elementImageState;
    }
    
}
