/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementGroupIdAdapter;

@JsonbTypeAdapter(ElementGroupIdAdapter.class)
public class ElementGroupId extends Scalar<String> {

	public static final String PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
	
	private static final long serialVersionUID = 1L;

	/**
	 * Alias for {@link #valueOf(String)} to improve readability.
	 * <p>
	 * Creates an <code>ElementGroupId</code> from the specified string.
	 * @param id the group ID
	 * @return the <code>ElementGroupId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementGroupId groupId(String id) {
		return fromString(id,ElementGroupId::new);
	}
	
	/**
	 * Creates an <code>ElementGroupId</code> from the specified string.
	 * @param id the group ID
	 * @return the <code>ElementGroupId</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementGroupId valueOf(String id) {
		return fromString(id,ElementGroupId::new);
	}

	
	public static ElementGroupId randomGroupId() {
		return new ElementGroupId(UUID.randomUUID().toString());
	}
	
	@NotNull(message="{group_id.required}")
	@Pattern(message="{group_id.invalid}", regexp=PATTERN)
	private String value;
	
	public ElementGroupId(String value){
		this.value = value;
	}
	
	@Override
	public String getValue() {
		return value;
	}




}
