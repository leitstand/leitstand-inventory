package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.RackIdAdapter;

/**
 * Unique rack ID in UUIDv4 format.
 * <p>
 * The rack ID is immutable thereby forming a persistent unique key for each rack.
 * @see RackName
 */
@JsonbTypeAdapter(RackIdAdapter.class)
public class RackId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random rack ID.
	 * @return a random rack ID.
	 */
	public static RackId randomRackId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	/**
	 * Returns a rack ID from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param rackId the rack ID
	 * @return a rack ID or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static RackId rackId(String rackId) {
		return valueOf(rackId);
	}

	
	/**
     * Returns a rack ID from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param rackId the rack ID
     * @return a rack ID or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static RackId valueOf(String rackId) {
		return fromString(rackId, RackId::new);
	}
	
	private String value;

	/**
	 * Creates a rack ID
	 * @param rackId the rack ID
	 */
	public RackId(String rackId) {
		this.value = rackId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}
