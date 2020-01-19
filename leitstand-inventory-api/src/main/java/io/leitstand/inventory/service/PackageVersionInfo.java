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
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Date;
import java.util.Map;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.commons.model.ValueObject;


/**
 * The <code>PackageVersionInfo</code> contains the informations of a revision
 *  of a router software package.
 * <p>
 * Each package is unambiguously identified by the organization which has published
 * the package, the name of the package and the installed revision of the
 * package.
 * </p>
 * <p>
 * For each revision additional informations such as the build date, the build-id,
 * the SHA256 checksum and optionally an exiper date are availabe.
 * </p>
 */

public class PackageVersionInfo extends ValueObject {
	
	/**
	 * Returns a builder to create a new <code>PackageVersionInfo</code>.
	 * @return a builder to create a new <code>PackageVersionInfo</code>.
	 */
	public static Builder newPackageVersionInfo(){
		return new Builder();
	}

	/**
	 * A builder to create an immutable <code>PackageVersionInfo</code> instance.
	 */
	public static class Builder {
		
		private PackageVersionInfo rev = new PackageVersionInfo();
		
		/**
		 * Sets the name of the organization which has published the deployed package.The name must not have more than
		 * 128 characters and may only contain small letters, digits, dots (.), hyphens (-) and underscores (_).
		 * @param org - the name of the organization.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org){
			assertNotInvalidated(getClass(), rev);
			rev.organization = org;
			return this;
		}
		
		/**
		 * Sets the name of the deployed package. The name must not have more than
		 * 128 characters and may only contain small letters, digits, dots (.), hyphens (-) and underscores (_).
		 * @param organization - the name of the package.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackageName(String name){
			assertNotInvalidated(getClass(), rev);
			rev.packageName = name;
			return this;
		}

		
		public Builder withPackageExtension(String ext){
			assertNotInvalidated(getClass(), rev);
			rev.packageExtension = ext;
			return this;
		}
		
		/**
		 * Sets the revision of the package.
		 * @param rev - the revision of the deployed package.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackageVersion(Version version){
			assertNotInvalidated(getClass(), rev);
			rev.packageVersion = version;
			return this;
		}

		/**
		 * Sets the build date of the deployed package.
		 * @param rev - the build date of the deployed package.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withBuildDate(Date buildDate){
			assertNotInvalidated(getClass(), rev);
			rev.buildDate = buildDate;
			return this;
		}

		public Builder withBuildId(String buildId) {
			assertNotInvalidated(getClass(), rev);
			rev.buildId = buildId;
			return this;
		}
		
		/**
		 * Sets available package checksums
		 * @param checksums the available checksums
		 * @return a reference to this builder to continue object creation
		 */
		public Builder withChecksums(Map<String,String> checksums){
			assertNotInvalidated(getClass(), rev);
			rev.checksums = checksums;
			return this;
		}

		/**
		 * Returns an immutable <code>PackageVersionInfo</code> instance and
		 * invalidates this builder. Subsequent invocations of the <code>build</code> method
		 * or any other method of the builder will cause an <code>NullPointerException</code>.
		 * @return an immutable <code>PackageVersionInfo</code> instance.
		 */
		public PackageVersionInfo build(){
			try{
				assertNotInvalidated(getClass(), rev);
				return rev;
			} finally {
				rev = null;
			}
		}


	}
	
	@NotNull(message="organization.required}")
	@Pattern(regexp="[a-z0-9._-]{3,128}",message="{organization.invalid}")
	private String organization;
	
	@NotNull(message="{package_name.required}")
	@Pattern(regexp="[a-z0-9._-]{3,128}",message="{package_name.invalid}")
	private String packageName;
	
	
	@NotNull(message="{package_extension.required}")
	@Pattern(regexp="[a-z0-9._-]{2,16}",message="{package_extension.invalid}")
	private String packageExtension;
	
	@JsonbProperty
	@Valid
	@NotNull(message="{package_version.required}")
	private Version packageVersion;
	
	@JsonbProperty("build_date")
	@JsonbTypeAdapter(IsoDateAdapter.class)
	@NotNull(message="{build_date.required}")
	private Date buildDate;
	private String buildId;
	
	private Map<String,String> checksums = emptyMap();
	
	/**
	 * Returns the name of the organization which has published this package.
	 * @return the name of the organization which has published this package.
	 */
	public String getOrganization() {
		return organization;
	}
	
	/**
	 * Returns the name of this package.
	 * @return the name of this package.
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Returns the revision of this package.
	 * @return the revision of this package.
	 */
	public Version getPackageVersion() {
		return packageVersion;
	}
	
	/**
	 * Returns the build date of this package.
	 * @return the build date of this package.
	 */
	public Date getBuildDate() {
		return buildDate;
	}
	
	public String getPackageExtension() {
		return packageExtension;
	}
	
	/**
	 * Return the package SHA-1 checksum or <code>null</code> if no SHA-1 checksum has been set.
	 * @return the package SHA-1 checksum
	 */
	public Map<String,String> getChecksums() {
		return unmodifiableMap(checksums);
	}
	
	public String getBuildId() {
		return buildId;
	}
	
}
