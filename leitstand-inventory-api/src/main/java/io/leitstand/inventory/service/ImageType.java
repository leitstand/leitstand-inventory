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

/** An enumeration of all available image types.*/
public enum ImageType {
	/** An operation system image.*/
	OS,
	/** Linux container image.*/
	LXC,
	/** Docker container image*/
	DOCKER,
	/** Configuration image.*/
	CONFIG;
	
	public static ImageType imageType(String type) {
		return valueOf(type);
	}
	
	/**
	 * Returns <code>true</code> if this image is a configuration image,
	 * i.e. an image that does contain configuration and no binaries.
	 * @return <code>true</code> if this image is a configuration image.
	 */
	public boolean isConfig(){
		return this == CONFIG;
	}
	
	
	/**
	 * Returns <code>true</code> if this image is a software image,
	 * i.e. an image that does contains binaries.
	 * @return <code>true</code> if this image is a software image.
	 */
	public boolean isBinary(){
		return this != CONFIG;
	}



}
