package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.PlatformChipsetNameAdapter;

/**
 * The platform chipset name.
 */
@JsonbTypeAdapter(PlatformChipsetNameAdapter.class)
public class PlatformChipsetName extends Scalar<String>{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a platform chipset name from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for the {@link #valueOf(String)} method to improve readability to avoid static import conflicts.
	 * @param name the chipset name
	 * @return the platform chipset name or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static PlatformChipsetName platformChipsetName(String name) {
		return valueOf(name);
	}

	/**
     * Creates a platform chipset name from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param name the chipset name
     * @return the platform chipset name or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static PlatformChipsetName valueOf(String name) {
		return fromString(name,PlatformChipsetName::new);
	}
	
	@NotNull(message="{chipset_name.required}")
	private String value;

	/**
	 * Creates a <code>PlatformChipsetName</code>.
	 * @param name the platform chipset name.
	 */
	public PlatformChipsetName(String name) {
		this.value = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
}
