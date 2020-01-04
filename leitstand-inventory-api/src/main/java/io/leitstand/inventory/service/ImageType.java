/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
