/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
