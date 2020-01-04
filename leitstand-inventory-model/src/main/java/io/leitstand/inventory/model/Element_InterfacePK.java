/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;

import java.io.Serializable;
import java.util.Objects;

import io.leitstand.inventory.service.InterfaceName;

public class Element_InterfacePK implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long element;
	private InterfaceName name;

	public Element_InterfacePK() {
		// JPA constructor
	}
	
	public Element_InterfacePK(Element element, InterfaceName name){
		this(element.getId(),name);
	}
	
	public Element_InterfacePK(Long element, InterfaceName name){
		this.element = element;
		this.name = name;
	}
	
	public Long getElement() {
		return element;
	}
	
	public InterfaceName getName() {
		return name;
	}
	
	@Override
	public int hashCode(){
		return Objects.hash(element,name);
	}
	
	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}
		if(o == null){
			return false;
		}
		if(o.getClass() != getClass()){
			return false;
		}
		Element_InterfacePK pk = (Element_InterfacePK) o;
		if(isDifferent(element,pk.element)){
			return false;
		}
		if(isDifferent(name, pk.name)){
			return false;
		}
		return true;
	}

	

}

