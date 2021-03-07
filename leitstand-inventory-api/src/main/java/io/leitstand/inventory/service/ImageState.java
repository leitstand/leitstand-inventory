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

/**
 * Enumeration of image lifecycle states.
 */
public enum ImageState {
	
	/** A newly registered image.*/
	NEW,
	
	/** A candidate for becoming the new release image.*/
	CANDIDATE,
	
	/** The current release image. The release image is deployed by default. */ 
	RELEASE,
	
	/** 
	 * A revoked image. Revoked images must not be used any longer.
	 */
	REVOKED,
	
	/**
	 * A superseded image is a release image replaced by a newer release image.
	 */
	SUPERSEDED
	
}