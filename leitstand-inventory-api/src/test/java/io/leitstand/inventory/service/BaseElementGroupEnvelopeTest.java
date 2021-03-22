package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.BaseElementGroupEnvelopeTest.ElementGroupMessage.newElementGroupMessage;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class BaseElementGroupEnvelopeTest {

    private static final ElementGroupId     GROUP_ID        = randomGroupId();
    private static final ElementGroupType   GROUP_TYPE      = groupType("type");
    private static final ElementGroupName   GROUP_NAME      = groupName("group");
    
    
    
    static final class ElementGroupMessage extends BaseElementGroupEnvelope {
        
        static Builder newElementGroupMessage() {
            return new Builder();
        }
        
        private static final class Builder extends BaseElementGroupEnvelopeBuilder<ElementGroupMessage, Builder>{
            
            protected Builder() {
                super(new ElementGroupMessage());
            }
            
        }
        
    }

    @Test
    public void create_base_element_group_envelope() {
        ElementGroupMessage message = newElementGroupMessage()
                                      .withGroupId(GROUP_ID)
                                      .withGroupName(GROUP_NAME)
                                      .withGroupType(GROUP_TYPE)
                                      .build();
        
        assertThat(message.getGroupId(),is(GROUP_ID));
        assertThat(message.getGroupName(),is(GROUP_NAME));
        assertThat(message.getGroupType(),is(GROUP_TYPE));
    }
    
    @Test
    public void copy_base_element_group_envelope() {
        ElementGroupMessage message = newElementGroupMessage()
                                 .withGroupId(GROUP_ID)
                                 .withGroupName(GROUP_NAME)
                                 .withGroupType(GROUP_TYPE)
                                 .build();
        
        ElementGroupMessage clone = newElementGroupMessage().withGroup(message).build();
        
        assertEquals(message,clone);
    }


}
