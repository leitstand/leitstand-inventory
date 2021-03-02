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
 * An enumeration of image lifecycle states.
 */
public enum ImageState {
	
	/** A new image that has been registered and waits for being promoted to CANDIDATE to become eligible for deployment.*/
	NEW,
	
	/** A candidate to become the new default release image.*/
	CANDIDATE,
	
	/** The image deployed by default.*/ 
	RELEASE,
	
	/** 
	 * An image that must not be deployed anymore. 
	 * Typically, a candidate gets revoked if severe errors have been detected.
	 */
	REVOKED,
	
	/**
	 * A superseded image has been replaced by a new release. 
	 * A superseded image was a default image before and has never been revoked.
	 */
	SUPERSEDED
	
}
