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
 * Package version metadata.
 */
public class PackageVersionInfo extends ValueObject {
	
	/**
	 * Returns a builder for an immutable <code>PackageVersionInfo</code> value object.
	 * @return a builder for an immutable <code>PackageVersionInfo</code> value object. 
	 */
	public static Builder newPackageVersionInfo(){
		return new Builder();
	}

	/**
	 * A builder for an immutable <code>PackageVersionInfo</code> value object.
	 */
	public static class Builder {
		
		private PackageVersionInfo rev = new PackageVersionInfo();
		
		/**
		 * Sets the organization that has published this package.
		 * @param org the organization name.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withOrganization(String org){
			assertNotInvalidated(getClass(), rev);
			rev.organization = org;
			return this;
		}
		
        /**
         * Sets the package name.
         * @param name the package name.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPackageName(String name){
			assertNotInvalidated(getClass(), rev);
			rev.packageName = name;
			return this;
		}

        /**
         * Sets the package extension.
         * @param ext the package extension.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withPackageExtension(String ext){
			assertNotInvalidated(getClass(), rev);
			rev.packageExtension = ext;
			return this;
		}
		
		/**
		 * Sets the package version
		 * @param version the package version
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withPackageVersion(Version version){
			assertNotInvalidated(getClass(), rev);
			rev.packageVersion = version;
			return this;
		}

		/**
		 * Sets package build date.
		 * @param buildDate the build date.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withBuildDate(Date buildDate){
			assertNotInvalidated(getClass(), rev);
			rev.buildDate = buildDate;
			return this;
		}

		/**
		 * Sets the build ID.
		 * @param buildId the build ID.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withBuildId(String buildId) {
			assertNotInvalidated(getClass(), rev);
			rev.buildId = buildId;
			return this;
		}
		
		/**
		 * Sets package checksums.
		 * @param checksums the package checksums.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withChecksums(Map<String,String> checksums){
			assertNotInvalidated(getClass(), rev);
			rev.checksums = checksums;
			return this;
		}

		/**
		 * Creates an immutable <code>PackageVersionInfo</code> value object and
		 * invalidates this builder. Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>PackageVersionInfo</code> value object.
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
	
	/**
	 * Returns the package extension.
	 * @return the package extension.
	 */
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
	
	/**
	 * Returns the build ID.
	 * @return the build ID.
	 */
	public String getBuildId() {
		return buildId;
	}
	
}
