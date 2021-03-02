package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.FacilityTypeAdapter;

/**
 * The network facility type, like central office, street cabinet or data centers.
 */
@JsonbTypeAdapter(FacilityTypeAdapter.class)
public class FacilityType extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a facility type from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param type the facility type
	 * @return the facility type or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static FacilityType facilityType(String type) {
		return valueOf(type);
	}
	
	/**
     * Creates a facility type from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * <p>
     * @param type the facility type
     * @return the facility type or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static FacilityType valueOf(String name) {
		return fromString(name,FacilityType::new);
	}

	private String value;

	/**
	 * Creates a facility type.
	 * @param type the facility type
	 */
	public FacilityType(String type) {
		this.value = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}
