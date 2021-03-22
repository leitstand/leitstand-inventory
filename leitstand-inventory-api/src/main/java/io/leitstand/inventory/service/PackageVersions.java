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

import static java.util.Collections.unmodifiableList;

import java.util.LinkedList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;


/**
 * All versions of a package.
 */
public class PackageVersions {

	/**
	 * Returns a new builder for an immutable <code>PackageVersions</code> value object.
	 * @return a new builder for an immutable <code>PackageVersions</code> value object.
	 */
	public static Builder newPackageVersions(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>PackageVersions</code> value object.
	 */
	public static class Builder{
		
		private PackageVersions data = new PackageVersions();
		
		/**
		 * Sets the name of the organization that has published the package.
		 * @param organization the name of the organization that has published the package.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String organization){
			data.organization = organization;
			return this;
		}
		
		/**
		 * Sets the package name.
		 * @param name the package name
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackageName(String name){
			data.packageName = name;
			return this;
		}
		
		/**
		 * Sets the existing package versions.
		 * @param versions the package versions.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withVersions(List<PackageVersionRef> versions){
			data.versions = new LinkedList<>(versions);
			return this;
		}
		
		/**
		 * Creates an immutable <code>PackageVersions</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
         * @return the immutable <code>PackageVersions</code> value object and invalidates this builder.
		 */
		public PackageVersions build(){
			try{
				return data;
			} finally {
				this.data = null;
			}
		}
		
	}
	
	@JsonbProperty
	private String organization;
	@JsonbProperty
	private String packageName;
	@JsonbProperty
	private List<PackageVersionRef> versions; 
	
	/**
	 * Returns the name of the organization which has published this package.
	 * @return the name of the organization which has published this package.
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
	 * Returns the package versions.
	 * @return the package versions.
 	 */
	public List<PackageVersionRef> getVersions() {
		return unmodifiableList(versions);
	}
}
