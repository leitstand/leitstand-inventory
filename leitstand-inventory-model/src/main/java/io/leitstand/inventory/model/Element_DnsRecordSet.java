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

import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static java.util.Collections.unmodifiableList;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.TABLE;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.inventory.jpa.DnsNameConverter;
import io.leitstand.inventory.jpa.DnsRecordSetIdConverter;
import io.leitstand.inventory.jpa.DnsRecordTypeConverter;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecord;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;

@Entity
@Table(schema="inventory", name="element_dns")
@NamedQuery(name="Element_DnsRecordSet.removeAll",
			query="DELETE FROM Element_DnsRecordSet s WHERE s.element=:element")
@NamedQuery(name="Element_DnsRecordSet.findDnsRecordSets",
			query="SELECT s FROM Element_DnsRecordSet s WHERE s.element=:element")
@NamedQuery(name="Element_DnsRecordSet.findDnsRecordSetByName",
			query="SELECT s FROM Element_DnsRecordSet s WHERE s.element=:element AND s.name=:name AND s.type=:type")
@NamedQuery(name="Element_DnsRecordSet.findDnsRecordSetById",
			query="SELECT s FROM Element_DnsRecordSet s WHERE s.uuid=:id")

public class Element_DnsRecordSet {

	public static Update removeDnsRecordSets(Element element) {
		return em -> em.createNamedQuery("Element_DnsRecordSet.removeAll",int.class)
					   .setParameter("element",element)
					   .executeUpdate();
	}
	
	public static Query<List<Element_DnsRecordSet>> findDnsRecordSets(Element element) {
		return em -> em.createNamedQuery("Element_DnsRecordSet.findDnsRecordSets",Element_DnsRecordSet.class)
					   .setParameter("element", element)
					   .getResultList();
	}
	
	public static Query<Element_DnsRecordSet> findDnsRecordSet(Element element, DnsName name, DnsRecordType type){
		return em -> em.createNamedQuery("Element_DnsRecordSet.findDnsRecordSetByName",Element_DnsRecordSet.class)
					   .setParameter("element",element)
					   .setParameter("name", name)
					   .setParameter("type", type)
					   .getSingleResult();
	}
	
	public static Query<Element_DnsRecordSet> findDnsRecordSet(DnsRecordSetId id){
		return em -> em.createNamedQuery("Element_DnsRecordSet.findDnsRecordSetById",Element_DnsRecordSet.class)
					   .setParameter("id",id)
					   .getSingleResult();
	}
	
	@Id
	@TableGenerator(name = "Entity.Sequence",
					schema="leitstand",
					table = "sequence", 
					initialValue = 1, 
					allocationSize = 100, 
					pkColumnName = "name", 
					pkColumnValue = "id", 
					valueColumnName = "count")
	@GeneratedValue(strategy=TABLE , generator="Entity.Sequence")	
	private Long id;
	
	@JoinColumn(name="element_id")
	private Element element;

	@Column(unique=true, length=36)
	@Convert(converter=DnsRecordSetIdConverter.class)
	private DnsRecordSetId uuid;
	
	@Convert(converter=DnsRecordTypeConverter.class)
	private DnsRecordType type;
	
	private int ttl;
	
	@ManyToOne(cascade=PERSIST)
	@JoinColumn(name="dnszone_id")
	private DnsZone dnsZone;
	
	@ElementCollection
	@CollectionTable(schema="inventory", 
					 name="element_dns_record", 
					 joinColumns=@JoinColumn(name="element_dns_id"))
	@AttributeOverride(name="dnsRecordValue", column=@Column(name="value"))
	private List<DnsRecord> records;
	
	@Convert(converter=DnsNameConverter.class)
	private DnsName name;

	private String description;
	
	@Temporal(TIMESTAMP)
	private Date tsModified;
	
	protected Element_DnsRecordSet() {
		// JPA
	}
	
	protected Element_DnsRecordSet(DnsZone zone, 
								   Element element, 
								   DnsName name) {
		this(zone,
			 element,
			 randomDnsRecordSetId(),
			 name);
	}
	
	protected Element_DnsRecordSet(DnsZone zone, 
								   Element element, 
								   DnsRecordSetId id, 
								   DnsName name ) {
		this.dnsZone = zone;
		this.element = element;
		this.uuid = id;
		this.name = name;
	}
	
	@PreUpdate
	protected void updateDateModified() {
		this.tsModified = new Date();
	}

	public void setDnsName(DnsName name) {
		this.name = name;
	}
	
	public DnsName getDnsName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public DnsRecordSetId getDnsRecordSetId() {
		return uuid;
	}
	
	public DnsRecordType getDnsRecordType() {
		return type;
	}
	
	public void setDnsRecordType(DnsRecordType type) {
		this.type = type;
	}
	
	public void setDnsRecords(List<DnsRecord> records) {
		this.records = records;
	}
	
	public List<DnsRecord> getDnsRecords() {
		return unmodifiableList(records);
	}
	
	public Date getDateModified() {
		return new Date(tsModified.getTime());
	}

	protected Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	
	public DnsZone getDnsZone() {
		return dnsZone;
	}
	
	public void setTimeToLiveInSeconds(int ttl) {
		this.ttl = ttl;
	}
	
	public int getTimeToLoveInSeconds() {
		return ttl;
	}
	
	public void setDnsZone(DnsZone dnsZone) {
		this.dnsZone = dnsZone;
	}

	public DnsZoneId getDnsZoneId() {
		return dnsZone.getDnsZoneId();
	}
	
	public DnsZoneName getDnsZoneName() {
		return dnsZone.getDnsZoneName();
	}
	
}
