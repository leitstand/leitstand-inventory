/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementIdAdapter;

/**
 * A unique identifier for an element stored in the resource inventory.
 * <p>
 * The element ID is immutable for an element and hence forms a persistent unique key for an element.
 * The element ID is compatible to the UUIDv4 format.
 * </p>
 * @see ElementSettings
 * @see ElementConfig
 * @see ElementInstalledImages
 * @see ElementPhysicalInterfaces
 * @see ElementLogicalInterfaces
 * @see ElementModules
 * @see ElementLocation
 */
@JsonbTypeAdapter(ElementIdAdapter.class)
public class ElementId extends Scalar<String> {
	
	private static final long serialVersionUID = 1L;
	private static final String PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

	/**
	 * Returns a random <code>ElementId</code>.
	 * @return a random <code>ElementId</code>.
	 */
	public static ElementId randomElementId() {
		return new ElementId(UUID.randomUUID());
	}

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Creates an <code>ElementId</code> from the specified string.
	 * @param id the element ID
	 * @returns the <code>ElementId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementId elementId(String id) {
		return valueOf(id);
	}
	
	/**
	 * Creates an <code>ElementId</code> from the specified string.
	 * @param id the element ID
	 * @returns the <code>ElementId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementId valueOf(String id) {
		return ElementId.fromString(id, ElementId::new);
	}

	@NotNull(message="{element_id.required}")
	@Pattern(message="{element_id.invalid}", regexp=PATTERN)
	private String value;
	
	/**
	 * Create a <code>ElementId</code>.
	 * @param value - the element id
	 */
	public ElementId(UUID value) {
		this(value.toString());
	}
	
	/**
	 * Create a <code>ElementId</code>.
	 * @param value - the element id
	 */
	public ElementId(String value){
		this.value = value;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValue() {
		return value;
	}

}
