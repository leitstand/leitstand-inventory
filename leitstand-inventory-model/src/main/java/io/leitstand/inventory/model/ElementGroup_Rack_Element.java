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
package io.leitstand.inventory.model;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRackLocation.ElementRackLocationPosition;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.RackName;

@Entity
@Table(schema="inventory", name="elementgroup_rack_element")
@NamedQuery(name="ElementGroup_Rack_Element.findRackElement",
			query="SELECT re FROM ElementGroup_Rack_Element re WHERE re.element=:element")			 
@NamedQuery(name="ElementGroup_Rack_Element.findRackByElement",
			query="SELECT re.rack FROM ElementGroup_Rack_Element re JOIN FETCH re.rack WHERE re.element=:element")			 
public class ElementGroup_Rack_Element implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Query<ElementGroup_Rack_Element> findRackElement(Element element) {
		return em -> em.createNamedQuery("ElementGroup_Rack_Element.findRackElement",
										 ElementGroup_Rack_Element.class)
					   .setParameter("element",element)
					   .getSingleResult();
	}
	
	public static Query<ElementGroup_Rack> findRackByElement(Element element) {
		return em -> em.createNamedQuery("ElementGroup_Rack_Element.findRackByElement",
				 						 ElementGroup_Rack.class)
					   .setParameter("element",element)
					   .getSingleResult();	
	}
	
	@Id
	@JoinColumn(name="element_id")
	private Element element;
	
	@Id
	@JoinColumn(name="rack_id")
	private ElementGroup_Rack rack;
	private int unit;
	@Column(name="hrpos")
	@Enumerated(STRING)
	private ElementRackLocationPosition hrpos;
	
	protected ElementGroup_Rack_Element() {
		//JPA
	}
	
	public ElementGroup_Rack_Element(ElementGroup_Rack rack,
						Element element) {
		this.rack = rack;
		this.element = element;
		this.rack.addElement(this);
	}
	
	public void setUnit(int unit) {
		this.unit = unit;
	}
	
	public int getUnit() {
		return unit;
	}
	
	public int getHeight() {
		Platform platform = element.getPlatform();
		if(platform == null) {
			return 1;
		}
		return platform.getHeight();
	}
	
	public boolean isHalfRack() {
		Platform platform = element.getPlatform();
		if(platform == null) {
			return false;
		}
		return platform.isHalfRackSize();
	}
	
	public ElementRackLocationPosition getHalfRackPosition() {
		return hrpos;
	}
	
	public void setHalfRackPosition(ElementRackLocationPosition position) {
		this.hrpos = position;
	}
	
	public ElementRoleName getElementRoleName() {
		return element.getElementRoleName();
	}
	
	public ElementId getElementId() {
		return element.getElementId();
	}
	
	public ElementName getElementName() {
		return element.getElementName();
	}
	
	public ElementGroup_Rack getRack() {
		return rack;
	}

	public RackName getRackName() {
		return getRack().getRackName();
	}

	public ElementAlias getElementAlias() {
		return element.getElementAlias();
	}
	
	public Platform getPlatform() {
		return element.getPlatform();
	}
	
}
