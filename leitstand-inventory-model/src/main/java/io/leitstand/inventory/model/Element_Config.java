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

import static java.util.Collections.unmodifiableList;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.ElementConfigNameConverter;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.security.auth.UserName;

@Entity
@IdClass(Element_ConfigPK.class)
@Table(schema="inventory", name="element_config")
@NamedQuery(name="Element_Config.findByName",
			query="SELECT c FROM Element_Config c WHERE c.element=:element AND c.name=:name")	
@NamedQuery(name="Element_Config.findAll",
			query="SELECT c FROM Element_Config c WHERE c.element=:element")
@NamedQuery(name="Element_Config.removeConfigRevisions",
			query="DELETE FROM Element_Config_Revision c WHERE c.elementConfig=:config AND c.state=io.leitstand.inventory.service.ConfigurationState.SUPERSEDED")
public class Element_Config implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Query<Element_Config> findElementConfigByName(Element element, ElementConfigName name){
		return em -> em.createNamedQuery("Element_Config.findByName",Element_Config.class)
					   .setParameter("element",element)
					   .setParameter("name",name)
					   .getSingleResult();
				
	}
	
	public static Update removeAllConfigurations(Element element) {
		return em -> {
			// JPQL does not support cascade DELETE (see JPA spec).
			int configs = 0;
			for(Element_Config config : em.createNamedQuery("Element_Config.findAll", Element_Config.class)
										  .setParameter("element", element)
										  .getResultList()) {
				
				configs += em.createNamedQuery("Element_Config.removeConfigRevisions",int.class)
							 .setParameter("config",config)
							 .executeUpdate();
				
				em.remove(config);
				
			}
			return configs;
		};
	}
	
	public static Update removeConfigRevisions(Element element, 
											   ElementConfigName configName) {
		
		
		return em -> {
				
			Element_Config key = em.getReference(Element_Config.class, 
												 new Element_ConfigPK(element,
														 			  configName));
			return em.createNamedQuery("Element_Config.removeConfigRevisions", int.class)
					 .setParameter("config", key)
					 .executeUpdate();
		};
	}

	@Id
	private Element element;
	
	@Id
	@Convert(converter=ElementConfigNameConverter.class)
	private ElementConfigName name;
	@Temporal(TIMESTAMP)
	private Date tsmodified;
	
	@OneToMany(cascade = ALL, orphanRemoval = true, mappedBy="elementConfig")
	private List<Element_Config_Revision> revisions;

	protected Element_Config(){
		// JPA
	}
	
	protected Element_Config(Element element, 
							 ElementConfigName name) {
		this.element = element;
		this.name = name;
		this.tsmodified = new Date();
		this.revisions = new LinkedList<>();
		
	}
	
	public ElementConfigName getName() {
		return name;
	}
	
	public Element getElement() {
		return element;
	}
	
	public Date getDateModified() {
		return new Date(tsmodified.getTime());
	}
	
	public List<Element_Config_Revision> getRevisions() {
		return unmodifiableList(revisions);
	}

	public void addRevision(ElementConfigId configId, 
							Content metadata, 	
							ConfigurationState configState, 
							UserName userName,
							String comment) {
		Element_Config_Revision rev = new Element_Config_Revision(this, configId,metadata, configState, userName, comment);
		this.revisions.add(rev);
		
	}

}
