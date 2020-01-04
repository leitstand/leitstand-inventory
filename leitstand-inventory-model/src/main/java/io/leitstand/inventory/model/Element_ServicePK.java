/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;

import java.io.Serializable;
import java.util.Objects;

public class Element_ServicePK implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long element;
	private Long service;
	
	public Element_ServicePK() {
		// JPA
	}
	
	public Element_ServicePK(Element element, Element_Service service) {
		this.element = element.getId();
		this.service = service.getServiceId();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(o == this){
			return true;
		}
		
		if(o.getClass() != getClass()){
			return false;
		}
		
		Element_ServicePK other = (Element_ServicePK) o;
		
		if(isDifferent(element, other.element)) {
			return false;
		}
		
		if(isDifferent(service, other.service)) {
			return false;
		}
		
		return true;
		
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(element,service);
	}
	
}
