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
 * An enumeration of existing service types.
 */
public enum ServiceType {
	/** An operating system hosted by an x86 server*/
	OS,
	/** A virtual machine.*/
	VM,
	/** A container that hosts a service instance.*/
	CONTAINER,
	/** A daemon, i.e. a background process*/
	DAEMON,
	/** A service, i.e. a service that provides a method of interaction for the user*/
	SERVICE
}
