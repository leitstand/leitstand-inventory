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

import static io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor.newPhysicalInterfaceNeighbor;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
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
import io.leitstand.inventory.jpa.MACAddressConverter;
import io.leitstand.inventory.jpa.OperationalStateConverter;
import io.leitstand.inventory.service.AdministrativeState;
import io.leitstand.inventory.service.Bandwidth;
import io.leitstand.inventory.service.ElementPhysicalInterfaceNeighbor;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.OperationalState;

@Entity
@Table(schema="inventory", name="element_ifp")
@IdClass(Element_InterfacePK.class)
@NamedQuery(name="Element_PhysicalInterface.findByElement", 
			query="SELECT p FROM Element_PhysicalInterface p WHERE p.element=:element")
@NamedQuery(name="Element_PhysicalInterface.removeAll", 
			query="DELETE FROM Element_PhysicalInterface p WHERE p.element=:element")
@NamedQuery(name="Element_PhysicalInterface.findByName", 
			query="SELECT p FROM Element_PhysicalInterface p WHERE p.element=:element AND p.name=:name")
@NamedQuery(name="Element_PhysicalInterface.removeNeighbors", 
			query="UPDATE Element_PhysicalInterface p SET p.neighborElement=NULL, p.neighborElementIfpName=NULL WHERE p.neighborElement=:element")
@NamedQuery(name="Element_PhysicalInterface.updateOperationalState",
			query="UPDATE Element_PhysicalInterface p SET p.opState=:state WHERE p.element=:element")
public class Element_PhysicalInterface implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static Query<List<Element_PhysicalInterface>> findIfps(Element element){
		return em -> em.createNamedQuery("Element_PhysicalInterface.findByElement",Element_PhysicalInterface.class)
					   .setParameter("element", element)
					   .getResultList();
	}
	
	public static Query<Element_PhysicalInterface> findIfpByName(Element element, InterfaceName name){
		return em -> em.createNamedQuery("Element_PhysicalInterface.findByName",Element_PhysicalInterface.class)
					   .setParameter("element", element)
					   .setParameter("name", name)
					   .getSingleResult();
	}
	
	public static Update removeIfps(Element element) {
		return em -> em.createNamedQuery("Element_PhysicalInterface.removeAll",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	public static Update removeNeighbors(Element element) {
		return em -> em.createNamedQuery("Element_PhysicalInterface.removeNeighbors",int.class)
					   .setParameter("element", element)
					   .executeUpdate();
	}
	
	public static Update updateIfpOperationalState(Element element, OperationalState state) {
		return em -> em.createNamedQuery("Element_PhysicalInterface.updateOperationalState")
					   .setParameter("element", element)
					   .setParameter("state",state)
					   .executeUpdate();
	}

	@Id
	@ManyToOne
	@JoinColumn(name="element_id")
	private Element element;
	@Id
	@Convert(converter=InterfaceNameConverter.class)
	@Column(name="name")
	private InterfaceName name;
	
	@Column(name="mtu_size")
	private int mtuSize;
	
	@AttributeOverrides({
		@AttributeOverride(name="value", column=@Column(name="bw_value")),
		@AttributeOverride(name="unit", column=@Column(name="bw_unit"))
	})
	private Bandwidth type;
	
	@Convert(converter=OperationalStateConverter.class)
	@Column(name="op_state")
	private OperationalState opState;
	
	@Convert(converter=AdministrativeStateConverter.class)
	@Column(name="adm_state")
	private AdministrativeState admState;
	
	@Convert(converter=MACAddressConverter.class)
	@Column(name="mac_address")
	private MACAddress mac;
	
	@Temporal(TIMESTAMP)
	private Date tsModified;
	
	@Temporal(TIMESTAMP)
	private Date tsCreated;
	
	@Column(name="alias")
	private String ifpAlias;
	
	@Column(name="ifp_class")
	private String ifpClass;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="element_id", referencedColumnName="element_id", updatable=false, insertable=false),
		@JoinColumn(name="element_ifc_name", referencedColumnName="name")
	})
	private Element_ContainerInterface ifc;
	
	
	@ManyToOne
	@JoinColumn(name="neighbor_element_id")
	private Element neighborElement;
	
	@Convert(converter=InterfaceNameConverter.class)
	@Column(name="neighbor_element_ifp_name")
	private InterfaceName neighborElementIfpName;
	
	
	
	protected Element_PhysicalInterface(){
		//JPA
	}
	
	public Element_PhysicalInterface(Element element, InterfaceName name, Bandwidth type, Element_ContainerInterface ifc){
		this.element = element;
		this.name  = name;
		this.type    = type;
		this.ifc = ifc;
		this.mtuSize = 1500;
		long now = System.currentTimeMillis();
		this.tsCreated = new Date(now);
		this.tsModified = new Date(now);
	}
	
	@PreUpdate
	protected void updateLastModified() {
		this.tsModified = new Date();
	}
	
	
	public Element getElement() {
		return element;
	}
	
	public InterfaceName getIfpName() {
		return name;
	}
	
	public Bandwidth getBandwidth() {
		return type;
	}
	
	public void setMacAddress(MACAddress mac) {
		this.mac = mac;
	}
	
	public MACAddress getMacAddress() {
		return mac;
	}
	
	public void setOperationalState(OperationalState state) {
		this.opState = state;
	}
	
	public OperationalState getOperationalState(){
		return opState;
	}
	
	public void setAdministrativeState(AdministrativeState admState) {
		this.admState = admState;
	}
	
	public AdministrativeState getAdministrativeState() {
		return admState;
	}
	

	public Set<Element_LogicalInterface> getLogicalInterfaces() {
		return ifc.getLogicalInterfaces();
	}

	public void setContainerInterface(Element_ContainerInterface containerInterface) {
		if(this.ifc != null) {
			this.ifc.removePhyiscalInterface(this);
		}
		this.ifc = containerInterface;
		this.ifc.addPhysicalInterface(this);
	}

	public Element_ContainerInterface getContainerInterface() {
		return ifc;
	}

	public void setMtuSize(int mtuSize) {
		this.mtuSize = mtuSize;
	}
	
	public int getMtuSize() {
		return mtuSize;
	}
	
	public boolean linkTo(Element element, InterfaceName ifpName) {
		if(Objects.equals(this.neighborElement, element) && Objects.equals(this.neighborElementIfpName, ifpName)) {
			return false;
		}
		this.neighborElement = element;
		this.neighborElementIfpName = ifpName;
		return true;
	}
	
	public Element getNeighborElement() {
		return neighborElement;
	}
	
	public InterfaceName getNeighborElementIfpName() {
		return neighborElementIfpName;
	}

	public void removeNeighbor() {
		this.neighborElement = null;
		this.neighborElementIfpName = null;
	}

	public ElementPhysicalInterfaceNeighbor getNeighbor() {
		if(neighborElement == null) {
			return null;
		}
		return newPhysicalInterfaceNeighbor().withElementId(neighborElement.getElementId())
							    .withElementName(neighborElement.getElementName())
							    .withInterfaceName(neighborElementIfpName)
							    .build();
	}
	
	public void setIfpAlias(String ifpAlias) {
		this.ifpAlias = ifpAlias;
	}
	
	public String getIfpAlias() {
		return ifpAlias;
	}
	
	public void setIfpClass(String ifpClass) {
		this.ifpClass = ifpClass;
	}
	
	public String getIfpClass() {
		return ifpClass;
	}

}
