/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Id;

public class Element_ImagePK implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private Long element;
	@Id
	private Long image;
	
	public Element_ImagePK(){
		// JPA
	}
	
	public Element_ImagePK(Long element, Long image){
		this.element = element;
		this.image = image;
	}
	
	public Long getElement() {
		return element;
	}
	
	
	@Override
	public int hashCode(){
		return Objects.hash(element,image);
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
		Element_ImagePK pk = (Element_ImagePK) o;
		return Objects.equals(element, pk.element) 
				&& Objects.equals(image, pk.image);
	}
	
	
}
