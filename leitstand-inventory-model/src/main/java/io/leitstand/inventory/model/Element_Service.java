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

import static io.leitstand.inventory.service.OperationalState.OPERATIONAL;
import static io.leitstand.inventory.service.OperationalState.UP;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.json.JsonObject;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.AdministrativeStateConverter;
import io.leitstand.inventory.jpa.OperationalStateConverter;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.ServiceName;
import io.leitstand.inventory.service.ServiceType;

@Entity
@Table(schema="inventory", name="element_service")
@NamedQuery(name="Element_Service.updateOperationalState",
			query="UPDATE Element_Service s SET s.opState=:state WHERE s.element=:element")
@NamedQuery(name="Element_Service.findServices",
			query="SELECT s FROM Element_Service s WHERE s.element=:element")
@NamedQuery(name="Element_Service.removeAll",
			query="DELETE FROM Element_Service s WHERE s.element=:element")
@NamedQuery(name="Element_Service.findService", 
			query="SELECT s FROM Element_Service s "+
				  "WHERE s.element=:element "+
				  "AND s.service.name=:name")
@IdClass(Element_ServicePK.class)
public class Element_Service implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static Update updateServiceOperationalState(Element element, OperationalState state) {
		return em -> em.createNamedQuery("Element_Service.updateOperationalState")
					   .setParameter("element", element)
					   .setParameter("state",state)
					   .executeUpdate();
	}

	public static Query<Element_Service> findElementService(Element element,
	                                                        ServiceName name) {
		return em -> em.createNamedQuery("Element_Service.findService", 
										 Element_Service.class)
					   .setParameter("element", element)
					   .setParameter("name",name)
					   .getSingleResult();
	}


	public static Query<List<Element_Service>> findElementServices(Element element) {
		return em -> em.createNamedQuery("Element_Service.findServices", 
										 Element_Service.class)
					   .setParameter("element", element)
					   .getResultList();
	}
	
	public static Update removeServices(Element element) {
		return em -> em.createNamedQuery("Element_Service.removeAll",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	
	@Id
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	@Id
	@ManyToOne
	@JoinColumn(name="service_id")
	private Service service;
	
	@OneToOne(mappedBy="service", fetch=LAZY, cascade=ALL)
	private Element_ServiceContext context;
	
	@Column(name="opstate")
	@Convert(converter=OperationalStateConverter.class)
	private OperationalState opState;

	@Column(name="admstate")
	@Convert(converter=AdministrativeStateConverter.class)
	private AdministrativeState admState;

	
	@Temporal(TIMESTAMP)
	private Date tsmodified;

	protected Element_Service(){
		// JPA
	}

	public Element_Service(Element element, Service service){
		this.element = element;
		this.service = service;
		this.tsmodified = new Date();
		this.context = new Element_ServiceContext(this);
	}

	public String getDescription() {
		return service.getDescription();
	}
	
	public String getDisplayName() {
		return service.getDisplayName();
	}
	
	public Date getDateModified() {
		return new Date(tsmodified.getTime());
	}
	
	public ServiceName getServiceName() {
		return service.getServiceName();
	}
	
	public OperationalState getOperationalState() {
		return opState;
	}
	
	public ServiceType getServiceType() {
		return service.getServiceType();
	}
	
	public void setOperationalState(OperationalState state) {
		this.opState = state;
	}

	public boolean isService(ServiceName name) {
		return Objects.equals(service.getServiceName(),name);
	}

	public boolean isActive() {
		return OPERATIONAL.is(getOperationalState()) || UP.is(getOperationalState());
	}

	public Element getElement() {
		return element;
	}

	public boolean isOperatingSystem() {
		return getServiceType() == ServiceType.OS;
	}
	
	public boolean isContainer() {
		return getServiceType() == ServiceType.CONTAINER;
	}


	public String getServiceContextType() {
		return context.getType();
	}
	
	public void setServiceContextType(String type) {
		context.setType(type);
	}
	
	public JsonObject getServiceContext() {
		return context.getContext();
	}
	
	public void setServiceContext(JsonObject context) {
		this.context.setContext(context);
	}
	
	public Element_Service getParent() {
		Element_ServiceContext parent = context.getParent();
		if(parent != null) {
			return parent.getService();
		}
		return null;
	}

	public void setParentContext(Element_ServiceContext parent) {
		context.setParent(parent);
	}

	public Long getServiceId() {
		return service.getId();
	}
	
	Element_ServiceContext getContext() {
		return context;
	}

    public void setAdministrativeState(AdministrativeState state) {
        this.admState = state;
    }
    
    public AdministrativeState getAdministrativeState() {
        return admState;
    }
	
}
