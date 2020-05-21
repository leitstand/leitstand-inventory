package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static java.util.Objects.hash;

public class Rack_ItemPK {

	private Long rack;
	private Integer position;
	
	public Rack_ItemPK() {
		// JPA
	}
	
	public Rack_ItemPK(Rack rack, int position) {
		this.rack = rack.getId();
		this.position = position;
	}

	public Long getRack() {
		return rack;
	}
	
	public Integer getPosition() {
		return position;
	}
	
	@Override
	public int hashCode() {
		return hash(rack,position);
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
		Rack_ItemPK other = (Rack_ItemPK) o;
		if(isDifferent(position, other.position)) {
			return false;
		}
		if(isDifferent(rack,other.rack)) {
			return false;
		}
		return true;
	}
	
}
