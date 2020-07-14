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

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import io.leitstand.commons.model.CompositeValue;
import io.leitstand.inventory.jsonb.VersionAdapter;

/**
 * An artifact <code>Version</code> consisting of a <em>major</em>, <em>minor</em> and
 * <em>patch</em> level.
 * <p>
 * The string representation of a version is formed by the major level followed by the minor level 
 * using a dot (.) as delimiter followed by the patch level using a hyphen (-) as delimiter (e.g. 18.04-1).
 * </p>
 */
@JsonbTypeAdapter(VersionAdapter.class)
@Embeddable
public class Version extends CompositeValue implements Comparable<Version>, Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String VERSION_PATTERN = "(\\d+)\\.(\\d+)\\.(\\d+)(?:\\-(\\p{Graph}+))?";
	private static final Pattern PATTERN = compile(VERSION_PATTERN);
	
	public static Version version(String version) {
		return valueOf(version);
	}
	
	/**
	 * Returns a <code>Version</code> if the specified string is not <code>null</code> or empty.
	 * @param version - the string representation of a version
	 * @return
	 */
	public static Version valueOf(String version){
		if(isEmptyString(version)) {
			return null;
		}
		return new Version(version);
	}
	
	public static String toString(Version version) {
		return version != null ? version.toString() : null;
	}
	
	@Min(0)
	private int majorLevel;
	@Min(0)
	private int minorLevel;
	@Min(0)
	private int patchLevel;
	
	private String preRelease;

	/**
	 * Create a <code>Version</code>
	 * @param version - the string representation of a version
	 */
	public Version(String version) {
		Matcher matcher = PATTERN.matcher(version);
		if(matcher.matches()) {
			this.majorLevel = parseInt(matcher.group(1));
			this.minorLevel = parseInt(matcher.group(2));
			this.patchLevel = parseInt(matcher.group(3));
			if(matcher.groupCount() == 4) {
				this.preRelease = matcher.group(4);
			}
		} else {
			throw new IllegalArgumentException("Invalid version string "+version);
		}

	}
	
	public Version(){
		// JAX-B
	}
	
	/**
	 * Create a <code>Version</code> instance.
	 * @param major the version's major level
	 * @param minor the version's minor level
	 * @param patch the version's patch level
	 */
	public Version(int major, int minor ,int patch){
		this(major,minor,patch,null);
	}
	
	
	/**
	 * Create a <code>Version</code> instance.
	 * @param major the version's major level
	 * @param minor the version's minor level
	 * @param patch the version's patch level
	 * @param preRelease the optional pre-release
	 */
	public Version(int major, int minor, int patch, String preRelease) {
		this.majorLevel = major;
		this.minorLevel = minor;
		this.patchLevel = patch;
		this.preRelease = preRelease;
	}

	/**
	 * Returns the version's major level.
	 * @return the version's major level.
	 */
	public int getMajorLevel() {
		return majorLevel;
	}

	/**
	 * Returns the version's minor level.
	 * @return the version's minor level.
	 */
	public int getMinorLevel() {
		return minorLevel;
	}
	
	/**
	 * Returns the version's patch level.
	 * @return the version's patch level.
	 */
	public int getPatchLevel() {
		return patchLevel;
	}
	
	/**
	 * Returns a string representation of this version in the form of <code>major.minor-patch</code>.
	 * @return a concatenation of major. minor and patch level using a dot (.) as delimiter.
	 */
	@Override
	public String toString(){
		if(isEmptyString(preRelease)) {
			return format("%d.%d.%d", 
						  majorLevel,
						  minorLevel,
						  patchLevel);
		}
		return format("%d.%d.%d-%s", 
				  	  majorLevel,
				  	  minorLevel,
				  	  patchLevel,
				  	  preRelease);

	}	
	
	public String getPreRelease() {
		return preRelease;
	}
	
	/**
	 * Compares the major, minor and patch level of this version against the given version in ascending order.
	 * {@inheritDoc}
	 * @param version - the version to be compared against this version
	 */
	@Override
	public int compareTo(Version version){
		int d = majorLevel - version.majorLevel;
		if(d != 0){
			return d;
		}
		d = minorLevel - version.minorLevel;
		if(d != 0){
			return d;
		}
		d = patchLevel - version.patchLevel;
		if(d != 0) {
			return d;
		}
		if(preRelease == null) {
			if(version.getPreRelease() == null) {
				return 0;
			}
			return 1;
		}
		if(version.getPreRelease() != null) {
			return preRelease.compareTo(version.getPreRelease());
		}
		return -1;
		
	}




}
