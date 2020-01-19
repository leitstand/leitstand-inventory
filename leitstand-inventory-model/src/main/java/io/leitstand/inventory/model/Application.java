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

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Map;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.AbstractEntity;
import io.leitstand.commons.model.Query;
import io.leitstand.inventory.jpa.ApplicationNameConverter;
import io.leitstand.inventory.service.ApplicationName;

@Entity
@Table(schema="inventory", name="application")
@NamedQueries({
	@NamedQuery(name="Application.findAll", query="SELECT a FROM Application a"),
	@NamedQuery(name="Application.findByName", query="SELECT a FROM Application a WHERE a.name=:name")
})
public class Application extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public static Query<Map<ApplicationName,Application>> findAll() {
		return em -> em.createNamedQuery("Application.findAll",Application.class)
					   .getResultList()
					   .stream()
					   .collect(toMap(Application::getName, 
							   		  identity()));
	}
	
	public static Query<Application> findByApplicationName(ApplicationName name){
		return em -> em.createNamedQuery("Application.findByName",Application.class)
					   .setParameter("name",name)
					   .getSingleResult();
	}
	
	@Convert(converter=ApplicationNameConverter.class)
	private ApplicationName name;
	private String description;
	

	protected Application() {
		//JPA constructor

	}
	
	public Application(ApplicationName name) {
		this.name = name;
	}
	
	public ApplicationName getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

}
