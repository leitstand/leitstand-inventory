package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.optional;

import io.leitstand.inventory.service.BaseElementEnvelope;
import io.leitstand.inventory.service.BaseElementEnvelope.BaseElementEnvelopeBuilder;

/**
 * Utility class to simplify creation of element value objects.
 */
final class ElementValueObjects {

    public static <T extends BaseElementEnvelope, B extends BaseElementEnvelopeBuilder<T,B>> B elementValueObject(B builder, Element element){
        return builder.withAdministrativeState(element.getAdministrativeState())
                      .withDateModified(element.getDateModified())
                      .withElementAlias(element.getElementAlias())
                      .withElementId(element.getElementId())
                      .withElementName(element.getElementName())
                      .withElementRole(element.getElementRoleName())
                      .withGroupId(element.getGroupId())
                      .withGroupName(element.getGroupName())
                      .withGroupType(element.getGroupType())
                      .withOperationalState(element.getOperationalState())
                      .withPlatformId(optional(element.getPlatform(), Platform::getPlatformId))
                      .withPlatformName(optional(element.getPlatform(), Platform::getPlatformName))
                      .withPlatformChipsetName(optional(element.getPlatform(),Platform::getChipset));
    }
    
    
    
}
