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

public class QualifiedPackageName extends CompositeValue implements Comparable<QualifiedPackageName> {

	public static Builder newQualifiedPackageName(){
		return new Builder();
	}
	
	public static class Builder {
		
		private QualifiedPackageName qn = new QualifiedPackageName();
		
		public Builder withOrganization(String org){
			qn.org = org;
			return this;
		}
		
		public Builder withName(String name){
			qn.name = name;
			return this;
		}
		
		public QualifiedPackageName build(){
			try{
				return qn;
			} finally {
				this.qn = null;
			}
		}
	}
	
	private String org;
	private String name;
	
	
	public String getOrg() {
		return org;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int compareTo(QualifiedPackageName qn){
		int orgOrder = org.compareTo(qn.getOrg());
		if(orgOrder != 0){
			return orgOrder;
		}
		return name.compareTo(qn.getName());
	}
	
}
