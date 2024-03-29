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

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static java.util.Collections.unmodifiableSortedSet;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.PlatformChipsetNameConverter;
import io.leitstand.inventory.jpa.PlatformNameConverter;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.PlatformPortMapping;

@Entity
@Table(schema="inventory",
	   name="platform",
	   uniqueConstraints=@UniqueConstraint(columnNames= {"vendor","model"}))
@NamedQueries({
	@NamedQuery(name="Platform.findByPlatformId", 
				query="SELECT p FROM Platform p WHERE p.uuid=:uuid"),
	@NamedQuery(name="Platform.findByPlatformName", 
				query="SELECT p FROM Platform p WHERE p.name=:name"),
	@NamedQuery(name="Platform.findAll", 
				query="SELECT p FROM Platform p ORDER BY p.name"),
	@NamedQuery(name="Platform.findByChipset", 
				query="SELECT p FROM Platform p WHERE p.chipset=:chipset ORDER BY p.name"),
	@NamedQuery(name="Platform.findMatches", 
				query="SELECT p FROM Platform p WHERE CAST(p.name as TEXT) REGEXP :filter OR p.vendor REGEXP :filter OR p.model REGEXP :filter ORDER BY p.name" ),	
	@NamedQuery(name="Platform.findByElementGroupAndElementRole", 
				query="SELECT p FROM Element e JOIN e.platform p WHERE e.group=:group AND e.role=:role "),
	@NamedQuery(name="Platform.countElements",
				query="SELECT count(e) FROM Element e WHERE e.platform=:platform")
	
})
public class Platform extends VersionableEntity{

	private static final long serialVersionUID = 1L;
	
	public static Query<Long> countElements(Platform platform){
		return em -> em.createNamedQuery("Platform.countElements",Long.class)
					.setParameter("platform", platform)
					.getSingleResult();
	}

	public static Query<List<Platform>> findAll(String filter) {
		if(isEmptyString(filter) || ".*".equals(filter)) {
			return em -> em.createNamedQuery("Platform.findAll",Platform.class)
					   	   .getResultList();		
		}
		return em -> em.createNamedQuery("Platform.findMatches",Platform.class)
					   .setParameter("filter",filter)
					   .getResultList();
		
	}
	
	public static Query<Platform> findPlatformByName(PlatformName name){
		return em -> em.createNamedQuery("Platform.findByPlatformName",Platform.class)
					   .setParameter("name", name)
					   .getSingleResult();
	}
	
	public static Query<List<Platform>> findPlatformsByElementGroupAndElementRole(ElementGroup group, ElementRole role){
		return em -> em.createNamedQuery("Platform.findByElementGroupAndElementRole",Platform.class)
					   .setParameter("group", group)
					   .setParameter("role", role)
					   .getResultList();
	}
	
	public static Query<Platform> findPlatformById(PlatformId platformId){
		return em -> em.createNamedQuery("Platform.findByPlatformId", Platform.class)
					   .setParameter("uuid", platformId.toString())
					   .getSingleResult();
	}
	
	public static Query<List<Platform>> findByChipset(PlatformChipsetName platformChipset) {
		return em -> em.createNamedQuery("Platform.findByChipset",Platform.class)
					   .setParameter("chipset", platformChipset)
					   .getResultList();
	}
	
	@Convert(converter = PlatformNameConverter.class)
	@Column(unique=true)
	private PlatformName name;

	@Convert(converter = PlatformChipsetNameConverter.class)
	private PlatformChipsetName chipset;
	private String vendor;
	private String model;
	private String description;	
	private int rackUnits;
	
	
    @ElementCollection
    @CollectionTable(schema="inventory", 
                     name="platform_port", 
                     joinColumns=@JoinColumn(name="platform_id", referencedColumnName="id"))
    private TreeSet<PlatformPortMapping> ports;

	protected Platform() {
		// JPA
	}
	
	public Platform(PlatformId platformId, 
					PlatformName platformName,
					PlatformChipsetName platformChipset) {
		super(platformId.toString());
		this.name = platformName;
		this.chipset = platformChipset;
		this.rackUnits = 1;
		this.ports = new TreeSet<>();
	}
	
	public PlatformName getPlatformName() {
		return name;
	}
	
	public String getVendor() {
		return vendor;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	void setPlatformName(PlatformName platformName) {
		this.name = platformName;
	}
	
	void setVendor(String vendor) {
		this.vendor = vendor;
	}
	
	void setRackUnits(int rackUnits) {
		this.rackUnits = rackUnits;
	}
	
	void setPorts(Collection<PlatformPortMapping> ports) {
	    this.ports = new TreeSet<>(ports);
	}
	
	public int getHeight() {
		return this.rackUnits;
	}
	
	public PlatformId getPlatformId() {
		return PlatformId.valueOf(getUuid());
	}
	
	public void setChipset(PlatformChipsetName chipset) {
		this.chipset = chipset;
	}

	public PlatformChipsetName getChipset() {
		return chipset;
	}

    public SortedSet<PlatformPortMapping> getPorts() {
        return unmodifiableSortedSet(ports);
    }
	
}
