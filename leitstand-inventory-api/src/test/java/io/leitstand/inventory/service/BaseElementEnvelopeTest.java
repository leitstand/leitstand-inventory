package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AdministrativeState.UP;
import static io.leitstand.inventory.service.BaseElementEnvelopeTest.ElementMessage.newElementMessage;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;

public class BaseElementEnvelopeTest {

    private static final ElementGroupId     GROUP_ID        = randomGroupId();
    private static final ElementGroupType   GROUP_TYPE      = groupType("type");
    private static final ElementGroupName   GROUP_NAME      = groupName("group");
    private static final ElementId          ELEMENT_ID      = randomElementId();
    private static final ElementName        ELEMENT_NAME    = elementName("element");
    private static final ElementAlias       ELEMENT_ALIAS   = elementAlias("alias");
    private static final ElementRoleName    ELEMENT_ROLE    = elementRoleName("role");
    private static final Date               DATE_MODIFIED   = new Date();
    
    
    
    static final class ElementMessage extends BaseElementEnvelope {
        
        static Builder newElementMessage() {
            return new Builder();
        }
        
        private static final class Builder extends BaseElementEnvelopeBuilder<ElementMessage, Builder>{
            
            protected Builder() {
                super(new ElementMessage());
            }
            
        }
        
    }

    @Test
    public void create_base_element_envelope() {
        ElementMessage message = newElementMessage()
                                 .withAdministrativeState(UP)
                                 .withDateModified(DATE_MODIFIED)
                                 .withElementAlias(ELEMENT_ALIAS)
                                 .withElementId(ELEMENT_ID)
                                 .withElementName(ELEMENT_NAME)
                                 .withElementRole(ELEMENT_ROLE)
                                 .withGroupId(GROUP_ID)
                                 .withGroupName(GROUP_NAME)
                                 .withGroupType(GROUP_TYPE)
                                 .withOperationalState(DOWN)
                                 .build();
        
        assertThat(message.getAdministrativeState(), is(UP));
        assertThat(message.getDateModified(),is(DATE_MODIFIED));
        assertThat(message.getElementAlias(),is(ELEMENT_ALIAS));
        assertThat(message.getElementId(),is(ELEMENT_ID));
        assertThat(message.getElementName(),is(ELEMENT_NAME));
        assertThat(message.getElementRole(),is(ELEMENT_ROLE));
        assertThat(message.getGroupId(),is(GROUP_ID));
        assertThat(message.getGroupName(),is(GROUP_NAME));
        assertThat(message.getGroupType(),is(GROUP_TYPE));
        assertThat(message.getOperationalState(),is(DOWN));
    }
    
    @Test
    public void copy_base_element_envelope() {
        ElementMessage message = newElementMessage()
                                 .withAdministrativeState(UP)
                                 .withDateModified(DATE_MODIFIED)
                                 .withElementAlias(ELEMENT_ALIAS)
                                 .withElementId(ELEMENT_ID)
                                 .withElementName(ELEMENT_NAME)
                                 .withElementRole(ELEMENT_ROLE)
                                 .withGroupId(GROUP_ID)
                                 .withGroupName(GROUP_NAME)
                                 .withGroupType(GROUP_TYPE)
                                 .withOperationalState(DOWN)
                                 .build();
        
        ElementMessage clone = newElementMessage().withElement(message).build();
        
        assertEquals(message,clone);
    }


}
