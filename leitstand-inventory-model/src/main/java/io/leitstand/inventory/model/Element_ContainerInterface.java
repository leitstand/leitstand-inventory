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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.service.InterfaceName;

@Entity
@Table(schema="inventory", name="element_ifc")
@IdClass(Element_InterfacePK.class)
@NamedQuery(name="Element_ContainerInterface.removeAll",
			query="DELETE FROM Element_ContainerInterface ifc WHERE ifc.element=:element")
public class Element_ContainerInterface implements Serializable {

	public static Query<Element_ContainerInterface> findIfcByName(Element element, InterfaceName name) {
		return em -> em.find(Element_ContainerInterface.class, new Element_InterfacePK(element,name));
	}

	public static Update removeIfcs(Element element) {
		return em -> em.createNamedQuery("Element_ContainerInterface.removeAll",int.class)
					   .setParameter("element",element)
					   .executeUpdate();
	}
	
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	@Id
	@Column(name="name")
	private InterfaceName name;
	
	protected Element_ContainerInterface() {
		// No instances allowed.
	}
	
	public Element_ContainerInterface(Element element, InterfaceName name){
		this.element = element;
		this.name = name;
	}
	
	public Element getElement() {
		return element;
	}
	
	public InterfaceName getInterfaceName() {
		return name;
	}

}
