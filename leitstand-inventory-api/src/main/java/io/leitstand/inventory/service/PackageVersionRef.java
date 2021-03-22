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
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import io.leitstand.commons.model.CompositeValue;

/**
 * A package version reference.
 */
public class PackageVersionRef extends CompositeValue implements Comparable<PackageVersionRef>{

    /**
     * Creates a builder for an immutable <code>PackageVersionRef</code> value object.
     * @return a builder for an immutable <code>PackageVersionRef</code> value object.
     */
	public static Builder newPackageVersionRef(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>PackageVersionRef</code> value object.
	 */
	public static class Builder {
		
		private PackageVersionRef qualifier = new PackageVersionRef();
		
		/**
		 * Sets the organization that published the package.
		 * @param org the organization name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org){
		    assertNotInvalidated(getClass(), qualifier);
			qualifier.organization = org;
			return this;
		}

		/**
         * Sets the package name.
         * @param org the package name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPackageName(String packageName){
	        assertNotInvalidated(getClass(), qualifier);
			qualifier.packageName = packageName;
			return this;
		}
		
		/**
		 * Sets the package version.
		 * @param packageVersion the package version.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackageVersion(Version packageVersion){
	        assertNotInvalidated(getClass(), qualifier);
			qualifier.packageVersion = packageVersion;
			return this;
		}
		
		/**
		 * Creates an immutable <code>PackageVersionRef</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>PackageVersionRef</code> value object and invalidates this builder.
		 */
		public PackageVersionRef build(){
			try{
		        assertNotInvalidated(getClass(), qualifier);
				return qualifier;
			} finally {
				this.qualifier = null;
			}
		}
		
	}
	
	private String organization;
	private String packageName;
	private Version packageVersion;
	
	/**
	 * Returns the name of the organization that published the package.
	 * @return the organization name.
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Returns the package name.
	 * @return the package name.
	 */
	public String getPackageName() {
		return packageName;
	}
	
	/**
	 * Returns the package version.
	 * @return the package version.
	 */
	public Version getPackageVersion() {
		return packageVersion;
	}
	
	/**
	 * Compares this package version with the given package version by organization, package name and version.
	 * @returns a negative integer, 0, or a positive integer as this version is lower, equal or greater than the specified version.
	 */
	@Override
	public int compareTo(PackageVersionRef o) {
		int orgOrder = getOrganization().compareTo(o.getOrganization());
		if(orgOrder != 0){
			return orgOrder;
		}
		int nameOrder = getPackageName().compareTo(o.getPackageName());
		if(nameOrder != 0){
			return nameOrder;
		}
		return getPackageVersion().compareTo(o.getPackageVersion());
	}
	
	/**
	 * Concatenates organization, package name and package version to form a string representation of this package version reference.
	 * @return a string representation of this package version.
	 */
	@Override
	public String toString() {
		return getOrganization()+"."+getPackageName()+"-"+getPackageVersion();
	}
}

