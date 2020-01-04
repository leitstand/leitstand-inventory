/*
 * (c) RtBrick, Inc All rights reserved, 2015 2019
 */
package io.leitstand.inventory.service;

import javax.ws.rs.core.MediaType;

/**
 * A stateless and transactional service to manage element configurations.
 */
public interface ElementConfigService {
	
	

	/**
	 * Returns the <em>ACTIVE</em> element configuration for the specified element.
	 * @param elementId the element ID
	 * @param configName the name of the configuration
	 * @return the configuration for the specified element
	 * @see ConfigurationState#ACTIVE
	 */
	ElementConfig getActiveElementConfig(ElementId elementId, 
										 ElementConfigName configName);

	/**
	 * Returns the <em>ACTIVE</em> element configuration for the specified element.
	 * @param elementName the element name
	 * @param configName the name of the configuration
	 * @return the configuration for the specified element
 	 * @see ConfigurationState#ACTIVE
	 */
	ElementConfig getActiveElementConfig(ElementName elementName, 
								   		 ElementConfigName configName);

	
	/**
	 * Returns the element configuration for the specified element.
	 * @param elementId the element ID
	 * @param configId the element configuration ID
	 */
	ElementConfig getElementConfig(ElementId elementId,
								   ElementConfigId configId);

	
	/**
	 * Returns the element configuration for the specified element.
	 * @param elementName the element name
	 * @param configId the element configuration ID
	 */
	ElementConfig getElementConfig(ElementName elementName,
								   ElementConfigId configId);

	
	/**
	 * Stores an element configuration for the specified element.
	 * @param element the element ID
	 * @param configName the name of the configuration
	 * @param contentType the configuration content type.
	 * @param configState the configuration state
	 * @param config the element configuration
	 * @param comment a brief description of the configuration change.
	 * @return storage result with a pointer to the config and a creation flag 
	 * 		   which is set to <code>true</code> when a new configuration was created and 
	 * 		   <code>false</code> if an existing configuration was updated 
	 * 		   (e.g. state changed from candidate to active)
	 */
	StoreElementConfigResult storeElementConfig(ElementId elementId, 
							   					ElementConfigName configName, 
							   					MediaType contentType,
							   					ConfigurationState configState,
							   					String config,
							   					String comment);

	
	/**
	 * Stores an element configuration for the specified element.
	 * @param elementName the element name
	 * @param configName the name of the configuration
	 * @param contentType the configuration contentType
	 * @param configState the configuration state
	 * @param config the element configuration
	 * @param comment a brief description of the configuration change.
	 * @return storage result with a pointer to the config and a creation flag 
	 * 		   which is set to <code>true</code> when a new configuration was created and 
	 * 		   <code>false</code> if an existing configuration was updated 
	 * 		   (e.g. state changed from candidate to active)
	 */
	StoreElementConfigResult storeElementConfig(ElementName elementName, 
							   					ElementConfigName configName,
							   					MediaType contentType,
							   					ConfigurationState configState,
							   					String config,
							   					String comment);
	
	/**
	 * Removes an element configuration.
	 * An <em>active</em> configuration cannot be removed.
	 * @param elementId the element ID
	 * @param configId the element configuration ID
	 * @see ConfigurationState
	 */
	void removeElementConfig(ElementId elementId,
							 ElementConfigId configId);
	
	
	/**
	 * Removes an element configuration.
	 * An <em>active</em> configuration cannot be removed.
	 * @param elementName the element name
	 * @param configId the element configuration ID
	 * @see ConfigurationState
	 */
	void removeElementConfig(ElementName elementName,
							 ElementConfigId configId);
	
	
	/**
	 * Removes an entire configuration series for the specified element,
	 * except active configurations, if any.
	 * @param elementId the element ID
	 * @param configName the name of the configuration series to be removed.
	 * @return the number of removed configurations.
	 */
	int removeElementConfig(ElementId elementId,
							ElementConfigName configName);
	
	/**
	 * Removes an entire configuration series for the specified element,
	 * except active configurations, if any.
	 * @param elementId the element ID
	 * @param configName the name of the configuration series to be removed.
	 * @return the number of removed configurations.
	 */
	int removeElementConfig(ElementName elementName,
							ElementConfigName configName);

	/**
	 * Returns all configurations matching the specified name for the specified element.
	 * @param elementId the element ID
	 * @param filter a regular expression name filter
	 * @return all matching configurations
	 */
	ElementConfigs findElementConfigs(ElementId elementId,
									  String filter);
	
	/**
	 * Returns all configurations matching the specified name for the specified element.
	 * @param elementName the element name
	 * @param filter a regular expression name filter
	 * @return all matching configurations
	 */
	ElementConfigs findElementConfigs(ElementName elementName,
									  String filter);

	
	/**
	 * Returns all revisions of a certain element configuration.
	 * @param elementId the element ID
	 * @param configName the configuration name
	 * @return all revision of the specified element configuration
	 */
	ElementConfigRevisions getElementConfigRevisions(ElementId elementId,
												     ElementConfigName configName);
	
	/**
	 * Returns all revisions of a certain element configuration.
	 * @param elementName the element name
	 * @param configName the configuration name
	 * @return all revision of the specified element configuration
	 */
	ElementConfigRevisions getElementConfigRevisions(ElementName elementName,
												     ElementConfigName configName);

	/**
     * Creates a new candidate configuration from a superseded or the currently active configuration.
	 * Overrides existing candidate configurations.
	 * @param elementName the element ID
	 * @param configId the id of the configuration to be restored
	 * @param comment an optional comment why the config is restored.
	 * @return a reference to the created configuration
	 */
	StoreElementConfigResult editElementConfig(ElementId elementId,
											   ElementConfigId configId,
											   String comment);

	/**
     * Creates a new candidate configuration from a superseded or the currently active configuration.
	 * Overrides existing candidate configurations.
	 * @param elementName the element name
	 * @param configId the id of the configuration to be restored
	 * @param comment an optional comment why the config is restored
	 * @return a reference to the created configuration
	 */
	StoreElementConfigResult editElementConfig(ElementName elementName,
											   ElementConfigId configId,
											   String comment);
	
	/**
	 * Updates the comment of an existing configuration.
	 * @param elementId the element ID
	 * @param configId the element configuration ID 
	 * @param comment the comment
	 */
	void setElementConfigComment( ElementId elementId,
								  ElementConfigId configId,
								  String comment);

	/**
	 * Updates the comment of an existing configuration.
	 * @param elementName the element name
	 * @param elementConfigId the element configuration ID 
	 * @param comment the comment
	 */
	void setElementConfigComment( ElementName elmentName,
								  ElementConfigId configId,
								  String comment);
	

}