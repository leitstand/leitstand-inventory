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

/**
 * The qualified package name consists of the organization that publishes the package and the package name.
 */
public class QualifiedPackageName extends CompositeValue implements Comparable<QualifiedPackageName> {
    
    /**
     * Creates a builder for an immutable <code>QualifiedPackageName</code> value object.
     * @return a builder for an immutable <code>QualifiedPackageName</code> value object.
     */
	public static Builder newQualifiedPackageName(){
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>QualifiedPackageName</code> value object.
	 */
	public static class Builder {
		
		private QualifiedPackageName qn = new QualifiedPackageName();
		
		/**
		 * Sets the organization that publishes the package.
		 * @param org the organization name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org){
			qn.organization = org;
			return this;
		}
		
		/**
		 * Sets the package name.
		 * @param name the package name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withName(String name){
			qn.name = name;
			return this;
		}
		
		/**
		 * Creates an immutable <code>QualifiedPackageName</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>QualifiedPackageName</code> object.
		 */
		public QualifiedPackageName build(){
			try{
				return qn;
			} finally {
				this.qn = null;
			}
		}
	}
	
	private String organization;
	private String name;
	
	/**
	 * Returns the organization that published this image.
	 * @return the organization that published this image.
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Returns the package name.
	 * @return the package name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Compares two qualified package names by comparing the organization and package names.
	 * @return a negative integer, zero, or a positive integer as this qualified name is less than, equal to, or greater than the specified qualified name.
	 */
	@Override
	public int compareTo(QualifiedPackageName qn){
		int orgOrder = organization.compareTo(qn.getOrganization());
		if(orgOrder != 0){
			return orgOrder;
		}
		return name.compareTo(qn.getName());
	}
	
	/**
	 * Returns the string representation of the qualified name by concatenating organization and package names using a dot (.) as delimiter.
	 * @returns the string representation of the qualified name by concatenating organization and package names using a dot (.) as delimiter.
	 */
	@Override
	public String toString() {
	    return organization+"."+name;
	}
	
}
