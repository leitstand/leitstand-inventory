package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ReleaseIdAdapter;

/**
 * A unique release ID in UUIDv4 format.
 */
@JsonbTypeAdapter(ReleaseIdAdapter.class)
public class ReleaseId extends Scalar<String> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a random release ID.
     * @return a random release ID.
     */
    public static ReleaseId randomReleaseId() {
        return valueOf(UUID.randomUUID().toString());
    }
    
    /**
     * Creates a release ID from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * <p>
     * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
     * @param releaseId the release ID
     * @return <code>null</code> if the specified string is <code>null</code> or empty.
     */
    public static ReleaseId releaseId(String releaseId) {
        return valueOf(releaseId);
    }
 
    /**
     * Creates a release ID from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param releaseId the release ID
     * @return <code>null</code> if the specified string is <code>null</code> or empty.
     */
    public static ReleaseId valueOf(String releaseId) {
        return fromString(releaseId,ReleaseId::new);
    }
    
    @NotNull(message="{release_id.required}")
    @Pattern(regexp=UUID_PATTERN, message="{release_id.invalid}")
    private String value;
    
    /**
     * Creates a release ID.
     * @param releaseId the release ID
     */
    public ReleaseId(String releaseId) {
        this.value = releaseId;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }
    
}
