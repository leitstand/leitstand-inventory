/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static java.util.UUID.randomUUID;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import io.leitstand.commons.jpa.BooleanConverter;
import io.leitstand.commons.model.Query;
import io.leitstand.commons.model.VersionableEntity;
import io.leitstand.inventory.jpa.ElementRoleNameConverter;
import io.leitstand.inventory.jpa.PlaneConverter;
import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.Plane;

@Entity
@Table(schema="inventory", name="elementrole")
@NamedQueries({
@NamedQuery(name="ElementRole.findByName", 
			query="SELECT r from ElementRole r WHERE r.name=:name"),
@NamedQuery(name="ElementRole.findById", 
			query="SELECT r from ElementRole r WHERE r.uuid=:uuid"),
@NamedQuery(name="ElementRole.findAll", 
			query="SELECT r from ElementRole r ORDER BY r.displayName, r.name"),
@NamedQuery(name="ElementRole.countElements",
			query="SELECT count(e) from Element e WHERE e.role=:role"),
@NamedQuery(name="ElementRole.findAllRolesInElementGroup", 
		    query="SELECT DISTINCT e.role from Element e WHERE e.group=:group")})
public class ElementRole  extends VersionableEntity{

	private static final long serialVersionUID = 1L;

	public static Query<List<ElementRole>> findRoles() {
		return em -> em.createNamedQuery("ElementRole.findAll",
										 ElementRole.class)
					   .getResultList();
	}

	public static Query<List<ElementRole>> findAllRolesInGroup(ElementGroup group) {
		return em -> em.createNamedQuery("ElementRole.findAllRolesInElementGroup",
										 ElementRole.class)
					   .setParameter("group",group)
					   .getResultList();
	}

	public static Query<ElementRole> findRoleById(ElementRoleId id) {
		return em -> em.createNamedQuery("ElementRole.findById",
										  ElementRole.class)
					   .setParameter("uuid",id.toString())
					   .getSingleResult();
	}
	
	
	public static Query<ElementRole> findRoleByName(ElementRoleName name) {
		return em -> em.createNamedQuery("ElementRole.findByName",
										  ElementRole.class)
					   .setParameter("name",name)
					   .getSingleResult();
	}
	
	public static Query<Long> countElements(ElementRole role) {
		return em -> em.createNamedQuery("ElementRole.countElements",
										 Long.class)
					   .setParameter("role",role)
					   .getSingleResult();
	}
	
	@Convert(converter=ElementRoleNameConverter.class)
	@Column(unique=true)
	private ElementRoleName name;
	
	private String displayName;
	
	private String description;
	@Convert(converter=PlaneConverter.class)
	private Plane plane;
	
	@Convert(converter=BooleanConverter.class)
	private boolean manageable;
	
	protected ElementRole() {
		// JPA
	}
	
	public ElementRole(ElementRoleId roleId) {
		super(roleId.toString());
	}

	public ElementRole(ElementRoleName roleName, Plane plane) {
		super(randomUUID().toString());
		this.name = roleName;
		this.plane = plane;
	}

	public ElementRoleName getRoleName() {
		return name;
	}
	
	public ElementRoleId getRoleId() {
		return new ElementRoleId(getUuid());
	}
	
	public String getDescription() {
		return description;
	}
	
	public Plane getPlane() {
		return plane;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}
	
	public void setManageable(boolean manageable) {
		this.manageable = manageable;
	}
	
	public boolean isManageable() {
		return manageable;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setRoleName(ElementRoleName roleName) {
		this.name = roleName;
	}



}