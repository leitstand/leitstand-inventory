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

import static io.leitstand.inventory.service.ConfigurationState.ACTIVE;
import static io.leitstand.inventory.service.ConfigurationState.CANDIDATE;
import static io.leitstand.inventory.service.ElementConfigId.randomConfigId;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.ElementConfigIdConverter;
import io.leitstand.inventory.jpa.ElementConfigNameConverter;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.security.auth.UserName;
import io.leitstand.security.auth.jpa.UserNameConverter;

@Entity
@Table(schema="inventory", name="element_config")
@NamedQuery(name="Element_Config.removeConfigRevisions",
			query="DELETE FROM Element_Config c WHERE c.element=:element AND c.name=:configName AND c.state != io.leitstand.inventory.service.ConfigurationState.ACTIVE")	
@NamedQuery(name="Element_Config.removeAll",
			query="DELETE FROM Element_Config c WHERE c.element=:element")	
@NamedQuery(name="Element_Config.findLatestConfig",
			query="SELECT c FROM Element_Config c WHERE c.element=:element AND c.name=:configName ORDER BY c.tsmodified DESC")
@NamedQuery(name="Element_Config.findActiveConfig",
			query="SELECT c FROM Element_Config c WHERE c.element=:element AND c.name=:configName AND c.state=io.leitstand.inventory.service.ConfigurationState.ACTIVE")


public class Element_Config implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Query<Element_Config> findElementConfig(ElementConfigId configId){
		return em -> em.find(Element_Config.class, configId);
	}

	public static Query<Element_Config> findLatestConfig(Element element, 
														 ElementConfigName name){
		return em -> em.createNamedQuery("Element_Config.findLatestConfig",Element_Config.class)
					   .setParameter("element", element)
					   .setParameter("configName", name)
					   .setMaxResults(1)
					   .getSingleResult();
	}
	
	public static Query<Element_Config> findActiveConfig(Element element, 
				 										 ElementConfigName name){
		return em -> em.createNamedQuery("Element_Config.findActiveConfig",Element_Config.class)
					   .setParameter("element", element)
					   .setParameter("configName", name)
					   .getSingleResult();
	}
	
	public static Update removeAllConfigurations(Element element) {
		return em -> em.createNamedQuery("Element_Config.removeAll",int.class)
					   .setParameter("element",element)
					   .executeUpdate();
	}
	
	public static Update removeConfigRevisions(Element element, 
											   ElementConfigName configName) {
		return em -> em.createNamedQuery("Element_Config.removeConfigRevisions", int.class)
					   .setParameter("element", element)
					   .setParameter("configName",configName)
					   .executeUpdate();
	}

	@Id
	@Convert(converter=ElementConfigIdConverter.class)
	@Column(name="uuid")
	private ElementConfigId configId;
	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	@Convert(converter=ElementConfigNameConverter.class)
	private ElementConfigName name;
	
	@Temporal(TIMESTAMP)
	private Date tsmodified;

	@Enumerated(STRING)
	private ConfigurationState state;
	private String comment;
	@Convert(converter=UserNameConverter.class)
	private UserName creator;
	
	@ManyToOne
	@JoinColumn(name="content_hash")
	private Content content;
	
	protected Element_Config(){
		// JPA
	}
	
	protected Element_Config(Element element, 
							 ElementConfigId configId, 
							 ElementConfigName name, 
							 ConfigurationState configState, 
							 UserName creator,
							 Content content,
							 String comment) {
		this.element = element;
		this.configId = configId;
		this.name = name;
		this.state = configState;
		this.creator = creator;
		this.content = content;
		this.comment = comment;
		this.tsmodified = new Date();
		
	}
	
	protected Element_Config(Element_Config src, UserName creator, String comment) {
		this.element = src.element;
		this.configId = randomConfigId();
		this.name = src.name;
		this.tsmodified = new Date();
		this.state = CANDIDATE;
		this.comment = comment;
		this.creator = creator;
		this.content = src.content;
	}
	
	public ElementConfigName getName() {
		return name;
	}
	
	public Element getElement() {
		return element;
	}
	
	
	public String getContentType() {
		return content.getContentType();
	}
	
	public ConfigurationState getConfigState() {
		return state;
	}
	
	public String getContentHash() {
		return content.getContentHash();
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getComment() {
		return comment;
	}
	
	public Date getDateModified() {
		return new Date(tsmodified.getTime());
	}

	public boolean isJsonConfig() {
		return "application/json".equalsIgnoreCase(getContentType());
	}
	
	public boolean isCandidateConfig() {
		return getConfigState() == CANDIDATE;
	}
	
	public boolean isActiveConfig() {
		return getConfigState() == ACTIVE;
	}
	
	public ElementConfigId getConfigId() {
		return configId;
	}

	public UserName getCreator() {
		return creator;
	}
	

}
