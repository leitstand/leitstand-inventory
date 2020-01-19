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

import io.leitstand.commons.model.CompositeValue;

public class PackageVersionId extends CompositeValue implements Comparable<PackageVersionId>{

	public static Builder newPackageVersionId(){
		return new Builder();
	}
	
	public static class Builder {
		
		private PackageVersionId qualifier = new PackageVersionId();
		
		public Builder withOrganization(String org){
			qualifier.org = org;
			return this;
		}
		
		public Builder withPackageName(String name){
			qualifier.name = name;
			return this;
		}
		
		public Builder withPackageVersion(Version revision){
			qualifier.revision = revision;
			return this;
		}
		
		public PackageVersionId build(){
			try{
				return qualifier;
			} finally {
				this.qualifier = null;
			}
		}
		
	}
	
	private String org;
	private String name;
	private Version revision;
	
	public String getOrg() {
		return org;
	}
	
	public String getName() {
		return name;
	}
	
	public Version getRevision() {
		return revision;
	}
	
	@Override
	public int compareTo(PackageVersionId o) {
		int orgOrder = getOrg().compareTo(o.getOrg());
		if(orgOrder != 0){
			return orgOrder;
		}
		int nameOrder = getName().compareTo(o.getName());
		if(nameOrder != 0){
			return nameOrder;
		}
		return getRevision().compareTo(o.getRevision());
	}
	
	@Override
	public String toString() {
		return getOrg()+"."+getName()+"-"+getRevision();
	}
}
