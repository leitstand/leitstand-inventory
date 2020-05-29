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

import static io.leitstand.inventory.service.VlanTag.newVlanTag;
import static java.lang.Integer.compare;
import static java.lang.System.currentTimeMillis;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.AdministrativeStateConverter;
import io.leitstand.inventory.jpa.InterfaceNameConverter;
import io.leitstand.inventory.jpa.OperationalStateConverter;
import io.leitstand.inventory.jpa.RoutingInstanceNameConverter;
import io.leitstand.inventory.service.AddressInterface;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.OperationalState;
import io.leitstand.inventory.service.RoutingInstanceName;
import io.leitstand.inventory.service.VlanTag;

@Entity
@Table(schema="inventory", name="element_ifl")
@IdClass(Element_InterfacePK.class)
@NamedQuery(name="Element_LogicalInterface.findByElement", 
			query="SELECT i FROM Element_LogicalInterface i WHERE i.element=:element")
@NamedQuery(name="Element_LogicalInterface.removeAll", 
			query="DELETE FROM Element_LogicalInterface i WHERE i.element=:element")
@NamedQuery(name="Element_LogicalInterface.findLogicalInterfaces",
			query="SELECT i FROM Element_LogicalInterface i WHERE i.element=:element AND (CAST(i.routingInstance AS TEXT) REGEXP :filter OR CAST(i.name AS TEXT) REGEXP :filter OR CAST(i.alias AS TEXT) REGEXP :filter)" )
@NamedQuery(name="Element_LogicalInterface.findLogicalInterfacesByPrefix",
			query="SELECT i FROM Element_LogicalInterface i JOIN i.addresses a WHERE i.element=:element AND CAST(a.address AS TEXT)=:filter" )
@NamedQuery(name="Element_LogicalInterface.findLogicalInterfacesByVlan",
			query="SELECT i FROM Element_LogicalInterface i JOIN i.vlans v WHERE i.element=:element AND CAST(v.vlanId AS INTEGER)=:vlan" )


public class Element_LogicalInterface implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Query<List<Element_LogicalInterface>> findIfls(Element element, 
														         String filter, 
														         int limit) {
		return em -> em.createNamedQuery("Element_LogicalInterface.findLogicalInterfaces", Element_LogicalInterface.class)
				   	   .setParameter("element",element)
				   	   .setParameter("filter", filter)
				   	   .setMaxResults(limit)
				   	   .getResultList();	
	}
	
	public static Query<List<Element_LogicalInterface>> findIflsByPrefix(Element element, 
																	     String filter, 
																	     int limit) {
		return em -> em.createNamedQuery("Element_LogicalInterface.findLogicalInterfacesByPrefix", Element_LogicalInterface.class)
					   .setParameter("element",element)
					   .setParameter("filter", filter)
					   .setMaxResults(limit)
					   .getResultList();	
	}
	
	public static Query<List<Element_LogicalInterface>> findIflsByVlanId(Element element, 
																		 int vlan, 
																		 int limit) {
		return em -> em.createNamedQuery("Element_LogicalInterface.findLogicalInterfacesByVlan", Element_LogicalInterface.class)
					   .setParameter("element",element)
					   .setParameter("vlan", vlan)
					   .setMaxResults(limit)
					   .getResultList();	
}
	
	
	public static Query<Element_LogicalInterface> findIflByName(Element element, InterfaceName name) {
		return em -> em.find(Element_LogicalInterface.class, new Element_InterfacePK(element,name));
	}
	
	public static Query<List<Element_LogicalInterface>> findIflsOfElement(Element element) {
		return em -> em.createNamedQuery("Element_LogicalInterface.findByElement", Element_LogicalInterface.class)
					   .setParameter("element",element)
					   .getResultList();
	}
	
	public static Update removeIfls(Element element) {
		return em -> em.createNamedQuery("Element_LogicalInterface.removeAll",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	@Id
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	
	@Id
	@Column(name="name")
	@Convert(converter=InterfaceNameConverter.class)
	private InterfaceName name;
	
	private String alias;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="element_id", referencedColumnName="element_id", updatable=false, insertable=false),
		@JoinColumn(name="element_ifc_name", referencedColumnName="name")
	})
	private Element_ContainerInterface ifc;
	
	@Column(name="opstate")
	@Convert(converter=OperationalStateConverter.class)
	private OperationalState opState;
	
	
	@Column(name="admstate")
	@Convert(converter=AdministrativeStateConverter.class)
	private AdministrativeState admState;
	
	@Column(name="instance")
	@Convert(converter=RoutingInstanceNameConverter.class)
	private RoutingInstanceName routingInstance;
	
	@ElementCollection
	@CollectionTable(schema="inventory",
					 name="element_ifl_ifa", 
					 joinColumns={
						@JoinColumn(name="element_id", referencedColumnName="element_id"),
						@JoinColumn(name="element_ifl_name", referencedColumnName="name")
					 })
	@AttributeOverrides({
		@AttributeOverride(name="addressType", column=@Column(name="type")),
		@AttributeOverride(name="address", column=@Column(name="address")),
	})
	private List<AddressInterface> addresses;
	
	@ElementCollection
	@CollectionTable(schema="inventory",
					 name="element_ifl_vlan",
					 joinColumns= {
						@JoinColumn(name="element_id", referencedColumnName="element_id"),
						@JoinColumn(name="element_ifl_name", referencedColumnName="name")
					 })
	@AttributeOverride(name="vlanTpid", column=@Column(name="tpid"))
	@AttributeOverride(name="vlanId", column=@Column(name="vid"))
	private List<Element_LogicalInterface_Vlan> vlans;
	
	@Temporal(TIMESTAMP)
	private Date tsCreated;
	@Temporal(TIMESTAMP)
	private Date tsModified;
	
	protected Element_LogicalInterface(){
		// JPA constructor
	}
	
	public Element_LogicalInterface(Element element, 
								   Element_ContainerInterface ifc, 
								   InterfaceName name){
		this.element = element;
		this.ifc = ifc;
		this.name = name;
		this.addresses = new LinkedList<>();
		this.vlans = new LinkedList<>();
		long now = currentTimeMillis();
		this.tsCreated = new Date(now);
		this.tsModified = new Date(now);
	}
	
	@PreUpdate
	protected void updateLastModified(){
		this.tsModified = new Date();
	}
	
	public void setAddressInterfaces(List<AddressInterface> ifas){
		addresses.clear();
		addresses.addAll(ifas);
	}
	
	public InterfaceName getInterfaceName(){
		return name;
	}
	
	public String getInterfaceAlias() {
		return alias;
	}
	
	public void setInterfaceAlias(String alias) {
		this.alias = alias;
	}
	
	public Element_ContainerInterface getContainerInterface(){
		return ifc;
	}

	public List<AddressInterface> getAddressInterfaces() {
		return unmodifiableList(addresses);
	}

	public void setContainerInterface(Element_ContainerInterface ifc) {
		this.ifc = ifc;
	}
	
	public OperationalState getOperationalState() {
		return opState;
	}
	
	public void setOperationalState(OperationalState opState) {
		this.opState = opState;
	}
	
	public AdministrativeState getAdministrativeState() {
		return admState;
	}
	
	public void setAdministrativeState(AdministrativeState admState) {
		this.admState = admState;
	}

	public Element getElement() {
		return element;
	}
	
	public void setRoutingInstance(RoutingInstanceName routingInstance) {
		this.routingInstance = routingInstance;
	}
	
	public RoutingInstanceName getRoutingInstance() {
		return routingInstance;
	}

	public void setVlans(List<VlanTag> tags) {
		this.vlans.clear();
		this.vlans = new LinkedList<>();
		for(int i=0; i  < tags.size(); i++) {
			VlanTag vlan = tags.get(i);
			this.vlans.add(new Element_LogicalInterface_Vlan(i,vlan.getVlanTpid(),vlan.getVlanId()));
		}
	}
	
	public List<VlanTag> getVlans() {
		if(this.vlans.isEmpty()) {
			return emptyList();
		}
		return unmodifiableList(vlans
							    .stream()
							    .sorted((a,b) -> compare(a.getVlan(),b.getVlan()))
							    .map(vlan -> newVlanTag()
							    			 .withVlanTpid(vlan.getVlanTpid())
							    			 .withVlanId(vlan.getVlanId())
							    			 .build())
							    .collect(toList()));
	}

}
