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

import static io.leitstand.commons.model.ObjectUtil.optional;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.RackItemData;
import io.leitstand.inventory.service.RackName;

@Entity
@Table(schema="inventory", name="rack_item")
@NamedQuery(name="Rack_Item.findRackItem",
			query="SELECT r FROM Rack_Item r WHERE r.element=:element")			 
@IdClass(Rack_ItemPK.class)
public class Rack_Item implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Query<Rack_Item> findRackItem(Element element) {
		return em -> em.createNamedQuery("Rack_Item.findRackItem",
										 Rack_Item.class)
					   .setParameter("element",element)
					   .getSingleResult();
	}
	
	@Id
	@JoinColumn(name="rack_id")
	private Rack rack;

	@Id
	private int position;
	
	@Enumerated(STRING)
	private RackItemData.Face face;
	private String name;
	private int height;
	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	protected Rack_Item() {
		//JPA
	}
	
	public Rack_Item(Rack rack,
					 int position,
					 RackItemData.Face face) {
		this.rack = rack;
		this.position = position;
		this.face = face;
		this.rack.addElement(this);
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getHeight() {
		if(element != null) {
			Platform platform = element.getPlatform();
			if(platform == null) {
				return 1;
			}
			return platform.getHeight();
		}
		return height;
	}
	
	public ElementRoleName getElementRoleName() {
		return optional(element, Element::getElementRoleName);
	}
	
	public ElementId getElementId() {
		return optional(element, Element::getElementId);
	}
	
	public ElementName getElementName() {
		return optional(element, Element::getElementName);
	}
	
	public Rack getRack() {
		return rack;
	}

	public RackName getRackName() {
		return getRack().getRackName();
	}

	public ElementAlias getElementAlias() {
		return optional(element,Element::getElementAlias);
	}
	
	public PlatformId getPlatformId() {
		return optional(optional(element,Element::getPlatform),
						Platform::getPlatformId);
	}
	
	public PlatformName getPlatformName() {
		return optional(optional(element,Element::getPlatform),
						Platform::getPlatformName);
	}
	
	public ElementGroupId getGroupId() {
		return optional(element,Element::getGroupId);
	}
	
	public ElementGroupType getGroupType() {
		return optional(element,Element::getGroupType);
	}
	
	public ElementGroupName getGroupName() {
		return optional(element,Element::getGroupName);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setHeight(int units) {
		this.height = units;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public void setFace(RackItemData.Face face) {
		this.face = face;
	}
	
	public RackItemData.Face getFace() {
		return face;
	}

	public AdministrativeState getAdministrativeState() {
		return optional(element,Element::getAdministrativeState);
	}
	
}
