/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.ElementAliasAdapter;

/**
 * The <code>ElementAlias</code> is a unique alias of an element.
 * <p>
 * An element alias length is limited to 64 characters. 
 * The element alias is unique among all existing elements in the network.
 * While the {@link ElementId} is immutable, the element alias can be updated
 * provided that the new element alias is still unique.
 * </P>
 */
@JsonbTypeAdapter(ElementAliasAdapter.class)
public class ElementAlias extends Scalar<String> {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates an <code>ElementAlias</code> from the specified scalar.
	 * Returns <code>null</code> if the specfied alias scalar is <code>null</code>.
	 * <p> 
 	 * This is method is an alias for {@link #valueOf(String)} to improve readability by avoiding static import conflicts.
	 * <p>
	 * @param alias the element alias
	 * @return the <code>ElementAlias</code> or <code>null</code> if the specified scalar is <code>null</code>.
	 */
	public static ElementAlias elementAlias(Scalar<String> alias) {
		return valueOf(Scalar.toString(alias));
	}
	
	
	/**
	 * Creates an <code>ElementAlias</code> from the specified string. 
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for {@link #valueOf(String)} to improve readability by avoiding static import conflicts.
	 * <p>
	 * @param alias the element alias
	 * @return the <code>ElementAlias</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementAlias elementAlias(String alias) {
		return valueOf(alias);
	}
	
	/**
	 * Creates an <code>ElementAlias</code> from the specified scalar. 
	 * @param alias the element alias
	 * @return the <code>ElementAlias</code> or <code>null</code> if the specified scalar is <code>null</code>.
	 */
	public static ElementAlias valueOf(Scalar<String> alias) {
		return valueOf(Scalar.toString(alias));
	}
	
	/**
	 * Creates an <code>ElementAlias</code> from the specified string. 
	 * @param alias the element alias
	 * @return the <code>ElementAlias</code> or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static ElementAlias valueOf(String alias) {
		return fromString(alias,ElementAlias::new);
	}

	@NotNull(message="{element_alias.required}")
	@Pattern(message="{element_alias.invalid}", regexp="\\p{Print}{1,64}")
	private String value;
	
	/**
	 * Create a <code>ElementAlias</code> instance.
	 * @param value the element alias
	 */
	public ElementAlias(String value){
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
