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
	 * <p>
	 * Alias method for {@link #valueOf(String)} to improve readability.
	 * @param name the facility name
	 * @return the facility name
	 */
	public static FacilityName facilityName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Creates a facility name from the specified string.
	 * @param name the facility name
	 * @return the facility name
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
