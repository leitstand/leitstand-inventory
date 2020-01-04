/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public interface ElementEnvironmentService {

	ElementEnvironments getElementEnvironments(ElementId elementId);
	ElementEnvironments getElementEnvironments(ElementName elementName);
	
	ElementEnvironment getElementEnvironment(EnvironmentId id);
	ElementEnvironment getElementEnvironment(ElementId elementId, EnvironmentName name);
	ElementEnvironment getElementEnvironment(ElementName elementName, EnvironmentName name);
	boolean storeElementEnvironment(ElementId elementId, Environment env);
	boolean storeElementEnvironment(ElementName elementName, Environment env);
	void removeElementEnvironment(EnvironmentId id);
	void removeElementEnvironment(ElementId elementId, EnvironmentName name);
	void removeElementEnvironment(ElementName elementName, EnvironmentName name);
	
}
