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

