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

public class PackageVersionRef extends CompositeValue implements Comparable<PackageVersionRef>{

	public static Builder newPackageVersionRef(){
		return new Builder();
	}
	
	public static class Builder {
		
		private PackageVersionRef qualifier = new PackageVersionRef();
		
		public Builder withOrganization(String org){
		    assertNotInvalidated(getClass(), qualifier);
			qualifier.organization = org;
			return this;
		}
		
		public Builder withPackageName(String packageName){
	        assertNotInvalidated(getClass(), qualifier);
			qualifier.packageName = packageName;
			return this;
		}
		
		public Builder withPackageVersion(Version oackageVersion){
	        assertNotInvalidated(getClass(), qualifier);
			qualifier.packageVersion = oackageVersion;
			return this;
		}
		
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
	
	public String getOrganization() {
		return organization;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public Version getPackageVersion() {
		return packageVersion;
	}
	
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
	
	@Override
	public String toString() {
		return getOrganization()+"."+getPackageName()+"-"+getPackageVersion();
	}
}
