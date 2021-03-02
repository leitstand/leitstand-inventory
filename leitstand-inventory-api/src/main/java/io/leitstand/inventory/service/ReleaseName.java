package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ReleaseNameAdapter;

/**
 * A unique release name.
 */
@JsonbTypeAdapter(ReleaseNameAdapter.class)
public class ReleaseName extends Scalar<String> {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a release name from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * <p>
     * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
     * @param releaseName the release name
     * @return the release name or <code>null</code> if the specified string is <code>null</code> or empty.
     */
    public static ReleaseName releaseName(String releaseName) {
        return valueOf(releaseName);
    }

    
    /**
     * Creates a release name from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param releaseName the release name
     * @return the release name or <code>null</code> if the specified string is <code>null</code> or empty.
     */
    public static ReleaseName valueOf(String releaseName) {
        return fromString(releaseName,ReleaseName::new);
    }
    
    @NotNull(message="{release_id.required}")
    @Pattern(regexp=UUID_PATTERN, message="{release_id.invalid}")
    private String value;
    
    /**
     * Creates a <code>ReleaseName</code>.
     * @param releaseName the release name.
     */
    public ReleaseName(String releaseName) {
        this.value = releaseName;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return value;
    }
}