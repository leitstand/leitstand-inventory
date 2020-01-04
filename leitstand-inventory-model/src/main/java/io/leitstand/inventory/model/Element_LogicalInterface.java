/*
 * (c) RtBrick, Inc. - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.VlanTag.newVlanTag;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
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
public class Element_LogicalInterface implements Serializable {

	private static final long serialVersionUID = 1L;

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
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="element_id", referencedColumnName="element_id", updatable=false, insertable=false),
		@JoinColumn(name="element_ifc_name", referencedColumnName="name")
	})
	private Element_ContainerInterface ifc;
	
	@Column(name="op_state")
	@Convert(converter=OperationalStateConverter.class)
	private OperationalState opState;
	
	
	@Column(name="adm_state")
	@Convert(converter=AdministrativeStateConverter.class)
	private AdministrativeState admState;
	
	@Column(name="routing_instance")
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
		long now = System.currentTimeMillis();
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
	
	public Element_ContainerInterface getContainerInterface(){
		return ifc;
	}

	public List<AddressInterface> getAddressInterfaces() {
		return unmodifiableList(addresses);
	}

	public void setContainerInterface(Element_ContainerInterface ifc) {
		this.ifc.removeLogicalInterface(this);
		this.ifc = ifc;
		this.ifc.addLogicalInterface(this);
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

	public void setVlans(List<VlanTag> vlans) {
		this.vlans.clear();
		for(int i=0; i < vlans.size(); i++) {
			this.vlans.add(new Element_LogicalInterface_Vlan(vlans.get(i).getVlanId(), i));
		}
	}
	
	public List<VlanTag> getVlans() {
		if(this.vlans.isEmpty()) {
			return emptyList();
		}
		VlanTag.Type type = VlanTag.Type.CTAG;
		if(this.vlans.size() == 1) {
			// Tag type is omitted for single-tagged VLANs.
			type = null;
		}
		List<VlanTag> tags = new LinkedList<>();
		for(Element_LogicalInterface_Vlan vlan : this.vlans) {
			tags.add(newVlanTag()
					 .withTagType(type)
					 .withVlanId(vlan.getVlanId())
					 .build());
			// All remaining tags are S-Tags.
			type = VlanTag.Type.STAG;
		}
		return unmodifiableList(tags);
	}


	
}