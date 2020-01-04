/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.json.SerializableJsonObject.serializable;

import javax.json.JsonObject;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import io.leitstand.commons.jpa.SerializableJsonObjectConverter;
import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.service.ServiceName;

@Entity
@Table(schema="inventory", name="element_service_context")
@NamedQuery(name="Element_ServiceContext.findServiceContext",
			query="SELECT s FROM Element_ServiceContext s WHERE s.element=:element AND s.service.service.name=:serviceName")
@NamedQuery(name="Element_ServiceContext.removeAll",
			query="DELETE FROM Element_ServiceContext s WHERE s.element=:element")
public class Element_ServiceContext extends VersionableEntity {
	private static final long serialVersionUID = 1L;
	
	public static Query<Element_ServiceContext> findServiceContext(Element element, ServiceName serviceName) {
		return em -> em.createNamedQuery("Element_ServiceContext.findServiceContext",Element_ServiceContext.class)
				   	   .setParameter("element",element)
				   	   .setParameter("serviceName", serviceName)
				   	   .getSingleResult();	
	}
	
	public static Update removeServiceContexts(Element element){
		return em -> em.createNamedQuery("Element_ServiceContext.removeAll",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	@OneToOne
	@JoinColumns({
		@JoinColumn(name="element_id", referencedColumnName="element_id", insertable=false, updatable=false),
		@JoinColumn(name="service_id", referencedColumnName="service_id")
	})
	private Element_Service service;
	
	@ManyToOne
	@JoinColumn(name="parent_servicecontext_id")
	private Element_ServiceContext parent;
	
	public Element_ServiceContext(){
		// JPA
	}

	public Element_ServiceContext(Element_Service service) {
		this.service = service;
		this.element = service.getElement();
	}
	
	private String type;
	
	@Convert(converter=SerializableJsonObjectConverter.class)
	private SerializableJsonObject context;

	public Element_Service getService() {
		return service;
	}

	public void setService(Element_Service service) {
		this.service = service;
	}

	public Element_ServiceContext getParent() {
		return parent;
	}

	public void setParent(Element_ServiceContext parent) {
		this.parent = parent;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setContext(JsonObject context) {
		this.context = serializable(context);
	}
	
	public JsonObject getContext() {
		return context;
	}



	
}
