/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.json.SerializableJsonObject.serializable;

import java.util.List;

import javax.json.JsonObject;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.jpa.SerializableJsonObjectConverter;
import io.leitstand.commons.json.SerializableJsonObject;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.Update;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.DnsZoneNameConverter;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;

@Entity
@Table(schema="inventory", name="dnszone")
@NamedQuery(name="DnsZone.findDnsZones",
			query="SELECT z FROM DnsZone z WHERE CAST (z.name as text) REGEXP :filter")
@NamedQuery(name="DnsZone.findByName",
			query="SELECT z FROM DnsZone z WHERE z.name=:name")
@NamedQuery(name="DnsZone.findById",
	   		query="SELECT z FROM DnsZone z WHERE z.uuid=:id")
@NamedQuery(name="DnsZone.findAllElements",
			query="SELECT r FROM Element_DnsRecordSet r WHERE r.dnsZone=:zone ")
@NamedQuery(name="DnsZone.removeAllRecords",
			query="DELETE FROM Element_DnsRecordSet r WHERE r.dnsZone=:zone")
@NamedQuery(name="DnsZone.countRecords",
			query="SELECT count(r) FROM Element_DnsRecordSet r WHERE r.dnsZone=:zone")
public class DnsZone extends VersionableEntity {
	
	private static final long serialVersionUID = 1L;
	
	public static Query<List<DnsZone>> findDnsZones(String filter, int offset, int limit) {
		return em -> em.createNamedQuery("DnsZone.findDnsZones",DnsZone.class)
					   .setParameter("filter", filter)
					   .setFirstResult(offset)
					   .setMaxResults(limit)
					   .getResultList();
	}
	
	public static Update countDnsZoneRecords(DnsZone zone) {
		return em -> em.createNamedQuery("DnsZone.countRecords",Long.class)
					   .setParameter("zone", zone)
					   .getSingleResult()
					   .intValue();
	}

	public static Query<DnsZone> findDnsZoneByName(DnsZoneName zoneName){
		return em -> em.createNamedQuery("DnsZone.findByName",DnsZone.class)
					   .setParameter("name",zoneName)
					   .getSingleResult();
	}
	
	public static Query<DnsZone> findDnsZoneById(DnsZoneId zoneId){
		return em -> em.createNamedQuery("DnsZone.findById",DnsZone.class)
					   .setParameter("id",zoneId.toString())
					   .getSingleResult();
	}
	
	public static Query<List<Element_DnsRecordSet>> findDnsZoneElements(DnsZone zone){
		return em -> em.createNamedQuery("DnsZone.findAllElements",Element_DnsRecordSet.class)
					   .setParameter("zone",zone)
					   .getResultList();
	}
	
	public static Update removeDnsZoneRecords(DnsZone zone) {
		return em -> em.createNamedQuery("DnsZone.removeAllRecords",int.class)
					   .setParameter("zone",zone)
					   .executeUpdate();
	}
	
	@Convert(converter=DnsZoneNameConverter.class)
	private DnsZoneName name;
	private String description;
	private String type;
	@Convert(converter=SerializableJsonObjectConverter.class)
	private SerializableJsonObject config;
	
	protected DnsZone() {
		// JPA
	}
	
	protected DnsZone(DnsZoneId zoneId, DnsZoneName zoneName) {
		super(zoneId.toString());
		this.name = zoneName;
	}
	
	public DnsZoneId getDnsZoneId() {
		return DnsZoneId.valueOf(getUuid());
	}
	
	public DnsZoneName getDnsZoneName() {
		return name;
	}
	
	public DnsZoneName setDnsZoneName(DnsZoneName name) {
		DnsZoneName oldName = this.name;
		this.name = name;
		return oldName;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setDnsZoneConfigType(String type) {
		this.type = type;
	}
	
	public String getDnsZoneConfigType() {
		return type;
	}
	
	public JsonObject getDnsZoneConfig() {
		return config;
	}
	
	public void setDnsZoneConfig(JsonObject config) {
		this.config = serializable(config);
	}

}
