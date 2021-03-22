package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.FacilityNameAdapter;

/**
 * Unique facility name.
 */
@JsonbTypeAdapter(FacilityNameAdapter.class)
public class FacilityName extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/** 
	 * Creates a facility name from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias method for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param name the facility name
	 * @return the facility name or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static FacilityName facilityName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a facility name from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * @param name the facility name
	 * @return the facility name or <code>null</code> if the specified name is <code>null</code> or empty.
	 */
	public static FacilityName valueOf(String name) {
		return fromString(name,FacilityName::new);
	}

	private String value;
	
	/**
	 * Creates a new facility name.
	 * @param name the facility name
	 */
	public FacilityName(String name) {
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
