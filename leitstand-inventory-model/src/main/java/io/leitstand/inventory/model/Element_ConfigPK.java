package io.leitstand.inventory.model;

import static java.util.Objects.hash;

import java.util.Objects;

import javax.persistence.Convert;

import io.leitstand.inventory.jpa.ElementConfigNameConverter;
import io.leitstand.inventory.service.ElementConfigName;

public class Element_ConfigPK {

	private Long element;
	@Convert(converter = ElementConfigNameConverter.class)
	private ElementConfigName name;
	
	
	protected Element_ConfigPK() {
		// JPA
	}
	
	public Element_ConfigPK(Element element, ElementConfigName name) {
		this.element = element.getId();
		this.name = name;
	}
	
	
	public Long getElement() {
		return element;
	}
	
	public ElementConfigName getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return hash(element,name);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		Element_ConfigPK pk = (Element_ConfigPK) o;
		if (pk.element != element) {
			return false;
		}
		return Objects.equals(pk.name, name);
		
		
	}
	
}
