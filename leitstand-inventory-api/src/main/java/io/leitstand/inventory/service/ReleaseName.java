package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ReleaseNameAdapter;

@JsonbTypeAdapter(ReleaseNameAdapter.class)
public class ReleaseName extends Scalar<String> {

    private static final long serialVersionUID = 1L;

    public static ReleaseName releaseName(String releaseName) {
        return valueOf(releaseName);
    }
    
    public static ReleaseName valueOf(String releaseName) {
        return fromString(releaseName,ReleaseName::new);
    }
    
    @NotNull(message="{release_id.required}")
    @Pattern(regexp=UUID_PATTERN, message="{release_id.invalid}")
    private String value;
    
    public ReleaseName(String releaseName) {
        this.value = releaseName;
    }
    
    @Override
    public String getValue() {
        return value;
    }
}