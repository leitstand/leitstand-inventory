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
