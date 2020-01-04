/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static java.util.Objects.hash;

public class ElementGroup_Rack_ElementPK {
	
	private Long element;
	private Long rack;
	
	public ElementGroup_Rack_ElementPK() {
		// JPA
	}
	
	public ElementGroup_Rack_ElementPK(Element element, ElementGroup_Rack rack) {
		this.element = element.getId();
		this.rack = rack.getId();
	}

	public Long getElement() {
		return element;
	}
	
	public Long getRack() {
		return rack;
	}
	
	@Override
	public int hashCode() {
		return hash(element,rack);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if(o == this) {
			return true;
		}
		if(o.getClass() != getClass()) {
			return false;
		}
		ElementGroup_Rack_ElementPK other = (ElementGroup_Rack_ElementPK) o;
		if(isDifferent(element, other.element)) {
			return false;
		}
		if(isDifferent(rack,other.rack)) {
			return false;
		}
		return true;
	}
	
	
}
