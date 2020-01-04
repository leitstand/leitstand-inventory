/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;


/**
 * The <code>PackageInfo</code> consists of the package name, the organization that has issued the package 
 * and a list of existing package versions.
 * <p>
 * Every package is unambiguously identified by the name of the organization, that has published the package,
 * and the package name.
 * </p>
 */

public class PackageInfo {

	/**
	 * Returns a new builder to create an immutable <code>PackageInfo</code> instance.
	 * @return a new builder to create an immutable <code>PackageInfo</code> instance.
	 */
	public static Builder newPackageInfo(){
		return new Builder();
	}
	
	/**
	 * A builder to create an immutable <code>PackageInfo</code> instance.
	 */
	public static class Builder{
		
		private PackageInfo data = new PackageInfo();
		
		/**
		 * Sets the name of the organization which has published the package.
		 * @param org - the name of the organization which has published the package.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org){
			data.org = org;
			return this;
		}
		
		/**
		 * Sets the name of the package.
		 * @param name - the package's name
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withName(String name){
			data.name = name;
			return this;
		}
		
		/**
		 * Sets a list of available versions for this package.
		 * @param versions - the list of existing versions.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withVersions(List<PackageVersionId> versions){
			data.versions = new LinkedList<>(versions);
			return this;
		}
		
		/**
		 * Returns an immutable <code>PackageInfo</code> instance and
		 * invalidates this builder. Subsequent invocations of the <code>build</code> method
		 * or any other method of the builder will cause an <code>NullPointerException</code>.
		 * @return an immutable <code>PackageInfo</code> instance.
		 */
		public PackageInfo build(){
			try{
				return data;
			} finally {
				this.data = null;
			}
		}
		
	}
	
	@JsonbProperty
	private String org;
	@JsonbProperty
	private String name;
	@JsonbProperty
	private List<PackageVersionId> versions; 
	
	/**
	 * Returns the name of the organization which has published this package.
	 * @return the name of the organization which has published this package.
	 */
	public String getOrganization() {
		return org;
	}
	
	/**
	 * Returns the name of this package.
	 * @return the name ot this package.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns an immutable list of versions of this package.
	 * @return an immutable list of versions of this package.
 	 */
	public List<PackageVersionId> getVersions() {
		return unmodifiableList(versions);
	}
}
