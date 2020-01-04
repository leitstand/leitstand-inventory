/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementNameAdapter;

/**
 * The <code>ElementName</code> is a unique name of an element.
 * <p>
 * An element can consist of up to 64 characters. 
 * The element name is unique among all existing elements in the network.
 * While the {@link ElementId} is immutable, the element name can be updated
 * provided that the new element name is still unique.
 * </P>
 */
@JsonbTypeAdapter(ElementNameAdapter.class)
public class ElementName extends Scalar<String> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Alias of the {@link #valueOf(String)} method to improve readability.
	 * <p>
	 * Creates an <code>ElementName</code> from the specified string.
	 * @param name the element name
	 * @return the <code>ElementName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementName elementName(String name) {
		return valueOf(name);
	}
	
	/**
	 * Alias of the {@link #valueOf(Scalar)} method to improve readability.
	 * <p>
	 * Creates an <code>ElementName</code> from the specified scalar.
	 * @param name the element name
	 * @return the <code>ElementName</code> or <code>null</code> if the specified scalar is <code>null</code>.
	 */
	public static ElementName elementName(Scalar<String> name) {
		return valueOf(Scalar.toString(name));
	}
	
	/**
	 * Creates an <code>ElementName</code> from the specified scalar.
	 * @param name the element name
	 * @return the <code>ElementName</code> or <code>null</code> if the specified scalar is <code>null</code>.
	 */
	public static ElementName valueOf(Scalar<String> name) {
		return valueOf(Scalar.toString(name));
	}
	
	/**
	 * Creates an <code>ElementName</code> from the specified string.
	 * @param name the element name
	 * @return the <code>ElementName</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementName valueOf(String name) {
		return fromString(name,ElementName::new);
	}

	@NotNull(message="{element_name.required}")
	@Pattern(message="{element_name.invalid}", regexp="\\p{Print}{1,64}")
	private String value;
	
	/**
	 * Create a <code>ElementName</code> instance.
	 * @param value - the element name
	 */
	public ElementName(String value){
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
