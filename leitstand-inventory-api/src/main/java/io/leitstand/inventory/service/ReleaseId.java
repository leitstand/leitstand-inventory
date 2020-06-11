package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ReleaseIdAdapter;

@JsonbTypeAdapter(ReleaseIdAdapter.class)
public class ReleaseId extends Scalar<String> {

    private static final long serialVersionUID = 1L;

    public static ReleaseId randomReleaseId() {
        return valueOf(UUID.randomUUID().toString());
    }
    
    public static ReleaseId releaseId(String releaseId) {
        return valueOf(releaseId);
    }
    
    public static ReleaseId valueOf(String releaseId) {
        return fromString(releaseId,ReleaseId::new);
    }
    
    @NotNull(message="{release_id.required}")
    @Pattern(regexp=UUID_PATTERN, message="{release_id.invalid}")
    private String value;
    
    public ReleaseId(String releaseId) {
        this.value = releaseId;
    }
    
    @Override
    public String getValue() {
        return value;
    }
    
}
