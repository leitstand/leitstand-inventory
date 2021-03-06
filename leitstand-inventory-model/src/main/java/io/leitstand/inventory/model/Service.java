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

import static io.leitstand.inventory.service.ServiceId.serviceId;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.ServiceNameConverter;
import io.leitstand.inventory.jpa.ServiceTypeConverter;
import io.leitstand.inventory.service.ServiceId;
import io.leitstand.inventory.service.ServiceName;
import io.leitstand.inventory.service.ServiceType;

@Entity
@Table(schema="inventory", name="service")
@NamedQueries({
	@NamedQuery(name="Service.findAll", query="SELECT s FROM Service s"),
	@NamedQuery(name="Service.findByName", query="SELECT s FROM Service s WHERE s.name=:name")
})
public class Service extends VersionableEntity{

	private static final long serialVersionUID = 1L;
	
	public static Query<List<Service>> findAllServices(){
		return em -> em.createNamedQuery("Service.findAll",Service.class)
					   .getResultList();
	}
	
	public static Query<Service> findService(ServiceName name) {
		return em -> em.createNamedQuery("Service.findByName",Service.class)
					   .setParameter("name",name)
					   .getSingleResult();
	}
	
	
	@Column(length=128, nullable=false, unique=true)
	@Convert(converter=ServiceNameConverter.class)
	private ServiceName name;
	
	@Column(length=256)
	private String displayName;
	
	@Column(length=1024)
	private String description;
	
	@Column(length=1)
	@Convert(converter=ServiceTypeConverter.class)
	private ServiceType type;
	
	protected Service(){
		// JPA
	}
	
	public Service(ServiceType type, ServiceName name){
		super(UUID.randomUUID().toString());
		this.type = type;
		this.name = name;
		this.displayName = name.toString();
	}
	
	public void setDisplayName(String displayName) {
		if(displayName == null || displayName.isEmpty()) {
			this.displayName = getServiceName().toString();
		} else {
			this.displayName = displayName;
		}
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ServiceId getServiceId() {
		return serviceId(getUuid());
	}
	
	public ServiceName getServiceName() {
		return name;
	}
	public String getDisplayName() {
		return displayName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ServiceType getServiceType(){
		return type;
	}

	
}
