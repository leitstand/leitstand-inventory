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
 * A version of a software image or a package shipped with an image.
 * <p>
 * A version consists of
 * <ul>
 * <li>a major version number</li>
 * <li>a minor version number</li>
 * <li>a patch version number and</li>
 * <li>an optional pre-release version string.</li>
 * </ul>
 * Major, minor and patch version numbers are separated by dot ('.') and the pre-release is separated by a hyphen ('-').
 */
@JsonbTypeAdapter(VersionAdapter.class)
@Embeddable
public class Version extends CompositeValue implements Comparable<Version>, Serializable{
	
	private static final long serialVersionUID = 1L;
	private static final String VERSION_PATTERN = "(\\d+)\\.(\\d+)\\.(\\d+)(?:\\-(\\p{Graph}+))?";
	private static final Pattern PATTERN = compile(VERSION_PATTERN);
	
	/**
	 * Creates a version from the specified string.
	 * Returns <code>null</code> if the specified string is <code>null</code> or empty.
	 * <p>
	 * This method is an alias for the {@link #valueOf(String)} method to improve readability by avoiding static import conflicts.
	 * @param version the version string.
	 * @return the version or <code>null</code> if the specified string is <code>null</code> or empty.
	 */
	public static Version version(String version) {
		return valueOf(version);
	}
	
    /**
     * Creates a version from the specified string.
     * Returns <code>null</code> if the specified string is <code>null</code> or empty.
     * @param version the version string.
     * @return the version or <code>null</code> if the specified string is <code>null</code> or empty.
     */
	public static Version valueOf(String version){
		if(isEmptyString(version)) {
			return null;
		}
		return new Version(version);
	}

	/**
	 * Creates a string representation of the specified version.
	 * Returns <code>null</code> if the specified version is <code>null</code>.
	 * @param version the version
	 * @return a string representation of the specified version or <code>null</code> if the specified version is null.
	 */
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
	 * Creates a <code>Version</code> from the specified string.
	 * @param version the version string
	 * @throws IllegalArgumentException if the specified string does not match the version format in the class description.
	 * @throws NullPointerException if the specified string is <code>null</code>.
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
	
	/**
	 * Tool constructor used by the runtime environment.
	 */
	public Version(){
		// JAX-B
	}
	
	/**
	 * Creates a <code>Version</code> instance.
	 * @param major the major version number
	 * @param minor the minor version number
	 * @param patch the patch version number
	 */
	public Version(int major, int minor ,int patch){
		this(major,minor,patch,null);
	}
	
	
    /**
     * Creates a <code>Version</code> instance.
     * @param major the major version number
     * @param minor the minor version number
     * @param patch the patch version number
     * @param preRelease the pre-release string
     */
	public Version(int major, int minor, int patch, String preRelease) {
		this.majorLevel = major;
		this.minorLevel = minor;
		this.patchLevel = patch;
		this.preRelease = preRelease;
	}

	/**
	 * Returns the major version number.
	 * @returns the major version number.
	 */
	public int getMajorLevel() {
		return majorLevel;
	}

	/**
	 * Returns the minor version number.
	 * @returns the minor version number.
	 */
	public int getMinorLevel() {
		return minorLevel;
	}
	
	/**
	 * Returns the patch level.
	 * @returns the patch level.
	 */
	public int getPatchLevel() {
		return patchLevel;
	}
	
	/**
	 * Returns the version string for this version.
	 * Major, minor and patch level are separated by a dot ('.'). The pre-release string is separated by a hyphen ('-').
	 * Example version strings are
	 * <ul>
	 *     <li><code>1.0.0</code> for a major version</li>
	 *     <li><code>1.1.0</code> for a minor version</li>
	 *     <li><code>1.0.1</code> for a patch</li>
	 *     <li><code>2.1.0-RC0</code> for a first release candidate.</li>
	 * </ul>
	 * @return the version string.
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
	
	/**
	 * Returns the pre-release string or <code>null</code> if no pre-release is specified.
	 * @return the pre-release string or <code>null</code> if no pre-release is specified.
	 */
	public String getPreRelease() {
		return preRelease;
	}
	
	/**
	 * Compares two versions by comparing the major, minor, patch and pre-release settings.
	 * @returns -1, 0 or 1 depending on whether this version is lower, equal or higher than the specified version.
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
