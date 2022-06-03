package io.leitstand.inventory.service;

import static java.util.UUID.randomUUID;

import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.RackItemIdAdapter;

/**
 * Unique identifier for a rack item in UUIDv4 format.
 */
@JsonbTypeAdapter(RackItemIdAdapter.class)
public class RackItemId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a random rack item ID.
	 * @return a random rack item ID.
	 */
	public static RackItemId randomRackItemId() {
		return valueOf(randomUUID().toString());
	}
	
	/**
	 * Creates a <code>RackItemId</code> from the given string.
	 * This method is an alias of the {@link #valueOf(String)} method to avoid static import conflicts.
	 * @param uuid the rack item ID
	 * @return the <code>RackItemId</code> or <code>null</code> if the given string is <code>null</code> or empty.
	 */
	public static RackItemId rackItemId(String uuid) {
		return valueOf(uuid);
	}
	
	/**
	 * Creates a <code>RackItemId</code> from the given string.
	 * @param uuid the rack item ID
	 * @return the <code>RackItemId</code> or <code>null</code> if the given string is <code>null</code> or empty.
	 */
	public static RackItemId valueOf(String uuid) {
		return fromString(uuid,RackItemId::new);
	}
	
	private String value;
	
	/**
	 * Creates a <code>RackItemId</code>.
	 * @param uuid the rack item ID
	 */
	public RackItemId(String uuid) {
		this.value = uuid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}
	
}
