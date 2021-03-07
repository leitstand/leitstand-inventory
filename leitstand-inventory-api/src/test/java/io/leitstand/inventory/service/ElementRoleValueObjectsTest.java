package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.ElementRoleId.randomElementRoleId;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementRoleRef.newElementRoleRef;
import static io.leitstand.inventory.service.ElementRoleSettings.newElementRoleSettings;
import static io.leitstand.inventory.service.Plane.DATA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ElementRoleValueObjectsTest {

    private static final ElementRoleId ROLE_ID = randomElementRoleId();
    private static final ElementRoleName ROLE_NAME = elementRoleName("role");
    private static final String DISPLAY_NAME = "display name";
    private static final String DESCRIPTION = "description";
    
    @Test
    public void create_element_role_ref() {
        ElementRoleRef ref = newElementRoleRef()
                             .withElementRole(ROLE_NAME)
                             .withPlane(DATA)
                             .build();
        
        assertThat(ref.getElementRole(), is(ROLE_NAME));
        assertThat(ref.getPlane(),is(DATA));
    }
    
    @Test
    public void create_element_role_settings() {
        ElementRoleSettings role = newElementRoleSettings()
                                   .withDescription(DESCRIPTION)
                                   .withDisplayName(DISPLAY_NAME)
                                   .withManageable(true)
                                   .withPlane(DATA)
                                   .withRoleId(ROLE_ID)
                                   .withRoleName(ROLE_NAME)
                                   .build();
        assertThat(role.getDescription(),is(DESCRIPTION));
        assertThat(role.getDisplayName(),is(DISPLAY_NAME));
        assertThat(role.getPlane(),is(DATA));
        assertThat(role.getRoleId(),is(ROLE_ID));
        assertThat(role.getRoleName(),is(ROLE_NAME));
    }
}
