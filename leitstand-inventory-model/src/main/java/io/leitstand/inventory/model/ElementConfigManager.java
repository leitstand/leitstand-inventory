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
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static io.leitstand.commons.jpa.SerializableJsonObjectConverter.parseJson;
import static io.leitstand.commons.jsonb.IsoDateAdapter.isoDateFormat;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.inventory.event.ElementConfigRemovedEvent.newElementConfigRemovedEvent;
import static io.leitstand.inventory.event.ElementConfigRevisionRemovedEvent.newElementConfigRevisionRemovedEvent;
import static io.leitstand.inventory.event.ElementConfigStoredEvent.newElementConfigStoredEvent;
import static io.leitstand.inventory.model.Element_Config.findActiveConfig;
import static io.leitstand.inventory.model.Element_Config.findElementConfig;
import static io.leitstand.inventory.model.Element_Config.findLatestConfig;
import static io.leitstand.inventory.model.Element_Config.removeConfigRevisions;
import static io.leitstand.inventory.service.ConfigurationState.ACTIVE;
import static io.leitstand.inventory.service.ConfigurationState.CANDIDATE;
import static io.leitstand.inventory.service.ConfigurationState.SUPERSEDED;
import static io.leitstand.inventory.service.ConfigurationState.configurationState;
import static io.leitstand.inventory.service.ElementConfig.newElementConfig;
import static io.leitstand.inventory.service.ElementConfigId.elementConfigId;
import static io.leitstand.inventory.service.ElementConfigName.elementConfigName;
import static io.leitstand.inventory.service.ElementConfigReference.newElementConfigReference;
import static io.leitstand.inventory.service.ElementConfigRevisions.newElementConfigRevisions;
import static io.leitstand.inventory.service.ElementConfigs.newElementConfigs;
import static io.leitstand.inventory.service.ReasonCode.IVT0330I_ELEMENT_CONFIG_REVISION_STORED;
import static io.leitstand.inventory.service.ReasonCode.IVT0331I_ELEMENT_CONFIG_REVISION_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0337I_ELEMENT_CONFIG_REMOVED;
import static io.leitstand.inventory.service.StoreElementConfigResult.configCreated;
import static io.leitstand.inventory.service.StoreElementConfigResult.configUpdated;
import static io.leitstand.security.auth.UserName.userName;
import static io.leitstand.security.crypto.SecureHashes.md5;
import static java.lang.String.format;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.MessageFactory;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.event.ElementConfigEvent;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfig;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementConfigReference;
import io.leitstand.inventory.service.ElementConfigRevisions;
import io.leitstand.inventory.service.ElementConfigs;
import io.leitstand.inventory.service.StoreElementConfigResult;
import io.leitstand.security.auth.UserContext;
import io.leitstand.security.auth.UserName;

@Dependent
public class ElementConfigManager {

	private static final Logger LOG = Logger.getLogger(ElementConfigManager.class.getName());
	
	private Repository repository;
	private DatabaseService database;
	private Messages messages;
	private Event<ElementConfigEvent> event;
	private UserName creator;
	
	@Inject
	protected ElementConfigManager(	@Inventory Repository repository, 
									@Inventory DatabaseService database,
									UserContext authenticated,
									Event<ElementConfigEvent> event,
									Messages messages){
		this.creator = authenticated.getUserName();
		this.repository = repository;
		this.database   = database;
		this.event = event;
		this.messages   = messages;
	}
	
	public ElementConfigs filterElementConfig(Element element,
											  String filter){
		
		if(isEmptyString(filter)) {
			filter = ".*";
		}
		
		List<ElementConfigReference> configs = database.executeQuery(prepare("WITH history AS ("+ 
																			 "SELECT element_id, name, max(tsmodified) AS tsmodified "+
																			 "FROM inventory.element_config "+
																			 "WHERE element_id = ? "+
																			 "AND name ~ ? "+
																			 "GROUP BY element_id, name )"+
																			 "SELECT c.uuid, c.name, c.state, c.creator, c.comment, c.contenttype, c.tsmodified "+
																			 "FROM history h "+
																			 "JOIN inventory.element_config c "+
																			 "ON h.name = c.name "+
																			 "AND h.element_id = c.element_id "+
																			 "AND h.tsmodified = c.tsmodified "+
																			 "ORDER BY c.name ",	
																			 element.getId(),
																			 filter),
									 								 rs -> newElementConfigReference()
									 								 	   .withConfigId(elementConfigId(rs.getString(1)))
									 								 	   .withConfigName(elementConfigName(rs.getString(2)))
									 								 	   .withConfigState(configurationState(rs.getString(3)))
									 								 	   .withCreator(userName(rs.getString(4)))
									 								 	   .withComment(rs.getString(5))
									 								 	   .withContentType(rs.getString(6))
									 								 	   .withDateModified(rs.getTimestamp(7))
									 								 	   .build());
		
		return newElementConfigs()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withConfigs(configs)
			   .build();
		
	}
	
	public ElementConfigRevisions getElementConfigRevisions(Element element,
														 	ElementConfigName configName){
		
		List<ElementConfigReference> revisions = database.executeQuery(prepare("SELECT uuid,state,creator,comment,tsmodified "+
																			   "FROM inventory.element_config "+
																			   "WHERE element_id=? "+
																			   "AND name=? "+
																			   "ORDER BY tsmodified DESC",
									  										   element.getId(),configName), 
							  										  rs -> newElementConfigReference()
							  										  	    .withConfigId(elementConfigId(rs.getString(1)))
							  										  	    .withConfigName(configName)
							  										  	    .withConfigState(configurationState(rs.getString(2)))
							  										  	    .withCreator(userName(rs.getString(3)))
							  										  		.withComment(rs.getString(4))
							  										  		.withDateModified(rs.getTimestamp(5))
							  										  		.build());
		
		if(revisions.isEmpty()) {
			throw new EntityNotFoundException(IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND, 
											  element.getElementName(), 
											  configName);
		}
		
		return newElementConfigRevisions()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withElementConfigName(configName)
			   .withElementConfigRevisions(revisions)
			   .build();
	}
	
	
	public ElementConfig getElementConfig(Element element, ElementConfigId configId) {
		Element_Config config = repository.execute(findElementConfig(configId));
		
		if(config == null) {
			LOG.fine(() -> format("%s: %s configuration for element %s not found.",
								  IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND,
								  configId,
								  element.getElementName()));
			
			throw new EntityNotFoundException(IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND, 
											  element.getElementName(), 
											  configId);
		}
		
		return config(element, 
					  config);
	
	}

	private ElementConfig config(Element element, Element_Config config) {
		return newElementConfig()
			   .withGroupId(element.getGroup().getGroupId())
			   .withGroupName(element.getGroup().getGroupName())
			   .withGroupType(element.getGroup().getGroupType())
			   .withElementId(element.getElementId())
			   .withElementName(element.getElementName())
			   .withElementAlias(element.getElementAlias())
			   .withElementRole(element.getElementRoleName())
			   .withConfigId(config.getConfigId())
			   .withConfigName(config.getName())
			   .withContentType(config.getContentType())
			   .withContentHash(config.getContentHash())
			   .withDateModified(config.getDateModified())
			   .withComment(config.getComment())
			   .withCreator(config.getCreator())
			   .withConfig(config.isJsonConfig() ? parseJson(config.getConfig()) : config.getConfig()) 
			   .withConfigState(config.getConfigState())
			   .build();
	}
	
	public ElementConfig getElementConfig(Element element, ElementConfigName configName) {
		Element_Config config = repository.execute(findActiveConfig(element,configName));
		
		if(config == null) {
			LOG.fine(() -> format("%s: No active %s configuration for element %s found.",
								  IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND,
								  configName,
								  element.getElementName()));
			
			throw new EntityNotFoundException(IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND, 
											  element.getElementName(), 
											  configName);
		}
		
		return config(element,
					  config);
	
	}
	

	public StoreElementConfigResult storeElementConfig(Element element, 
													   ElementConfigName configName,
													   MediaType contentType,
													   ConfigurationState configState,
													   String configData,
													   String comment) {
		String contentHash = new BigInteger(md5().hash(configData)).abs().toString(16);
		
		Element_Config config = repository.execute(findLatestConfig(element, configName ));
		if(config != null ) {
			if(configState == ACTIVE) {
				// New active configuration detected.
				if(config.isActiveConfig()) {
					if(config.isSameContentHash(contentHash)) {
						// Do nothing when active config is reported again
						return configUpdated(config.getConfigId());
					}
					config.setConfigState(SUPERSEDED);
				} else {
					// New active configuration reported.
					// Existing active config, if any, must be set to superseded.
					Element_Config active = repository.execute(findActiveConfig(element, configName));
					if(active != null) {
						// No active config exists, if only a first candidate is available.
						active.setConfigState(SUPERSEDED);
					}
				}
			}

			if (config.isCandidateConfig()) {
				// Update existing candidate configuration. 
				// Config must be fetched first because it is being lazy loaded.
				// Otherwise changes made to the entity (e.g. comment update) get lost.
				config.setConfig(configData);
	
				// An operator uploads new CANDIDATE configs.
				// There is only one candidate config, i.e. an existing candidate will be updated.
				// A device sends a config whenever it has been changed to confirm configuration activation.
				if(comment != null || configState == CANDIDATE) {
					// A device sends no comment, i.e. comment is null, when confirming configuration activation.
					// This null check averts that comments are accidentally removed by devices.
					// Operators create candidate configs. Null-values for CANDIDATE configs are considered intentional.
					config.setComment(comment);
				}
				config.setConfigState(configState);
				config.setContentType(contentType);
				config.setContentHash(contentHash);
				LOG.fine(() -> format("%s: Updated %s configuration for element %s (%s)",
			 			  			  IVT0330I_ELEMENT_CONFIG_REVISION_STORED.getReasonCode(),
			 			  			  configName,
			 			  			  element.getElementName(), 
			 			  			  contentHash));
				messages.add(createMessage(IVT0330I_ELEMENT_CONFIG_REVISION_STORED,
						  				   element.getElementName(),
						  				   configName,
						  				   isoDateFormat(config.getDateModified())));
				
				event.fire(newElementConfigStoredEvent()
						   .withGroupId(element.getGroupId())
						   .withGroupName(element.getGroupName())
						   .withGroupType(element.getGroupType())
						   .withElementId(element.getElementId())
						   .withElementName(element.getElementName())
						   .withElementAlias(element.getElementAlias())
						   .withElementRole(element.getElementRoleName())
						   .withConfigId(config.getConfigId())
						   .withConfigName(configName)
						   .withConfigState(configState)
						   .withContentType(contentType.toString())
						   .withCreator(config.getCreator())
						   .withDateModified(config.getDateModified())
						   .build());
				
				return configUpdated(config.getConfigId());
			}
			
		}
		
		
		// Create a new configuration
		config = new Element_Config(element,
									configName,
									configState,
									contentType,
									contentHash,
									configData,
									creator);
		config.setComment(comment);
		repository.add(config);
		LOG.fine(() -> format("%s: Stored new %s configuration for element %s (%s)",
				 			  IVT0330I_ELEMENT_CONFIG_REVISION_STORED.getReasonCode(),
				 			  configName,
				 			  element.getElementName(),
				 			  contentHash));
		messages.add(createMessage(IVT0330I_ELEMENT_CONFIG_REVISION_STORED, 
								   element.getElementName(),
								   configName,
								   contentHash));
			
		event.fire(newElementConfigStoredEvent()
				   .withGroupId(element.getGroupId())
				   .withGroupName(element.getGroupName())
				   .withGroupType(element.getGroupType())
				   .withElementId(element.getElementId())
				   .withElementName(element.getElementName())
				   .withElementRole(element.getElementRoleName())
				   .withConfigId(config.getConfigId())
				   .withConfigName(configName)
				   .withConfigState(configState)
				   .withContentType(contentType.toString())
				   .withCreator(config.getCreator())
				   .withDateModified(config.getDateModified())
				   .build());

		return configCreated(config.getConfigId());
	}
	

	public void removeElementConfig(Element element, 
									ElementConfigId configId) {
		Element_Config config = repository.execute(findElementConfig(configId));
		if(config != null) {
			LOG.fine(() -> format("%s: Removed %s configuration %s for element %s (Date modified: %s)",
								  IVT0331I_ELEMENT_CONFIG_REVISION_REMOVED.getReasonCode(),
								  config.getName(),
								  configId,
								  element.getElementName(), 
								  isoDateFormat(config.getDateModified())));
				     
			messages.add(createMessage(IVT0331I_ELEMENT_CONFIG_REVISION_REMOVED, 
	  				  				   element.getElementName(),
	  				  				   config.getName(),
	  				  				   configId));
			repository.remove(config);
			event.fire(newElementConfigRevisionRemovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withConfigName(config.getName())
					   .withContentType(config.getContentType())
					   .withCreator(config.getCreator())
					   .withDateModified(config.getDateModified())
					   .build());

		}
	}

	public void setElementConfigComment(Element element,
										ElementConfigId configId, 
										String comment) {
		Element_Config config = findConfig(element, configId);
		LOG.fine(() -> format("%s: Updated comment %s configuration (%s) for element %s (Date modified: %s)",
		 					  IVT0330I_ELEMENT_CONFIG_REVISION_STORED.getReasonCode(),
		 					  config.getName(),
		 					  configId,
		 					  element.getElementName(), 
		 					  isoDateFormat(config.getDateModified())));
		messages.add(createMessage(IVT0330I_ELEMENT_CONFIG_REVISION_STORED, 
								   element.getElementName(),
								   configId));
		config.setComment(comment);
	}

	private Element_Config findConfig(Element element, ElementConfigId configId) {
		Element_Config config = repository.execute(findElementConfig(configId));
		if(config == null) {
			LOG.fine(() -> format("%s: %s configuration for element %s not found(Date modified: %s)",
								  IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND,
								  configId,
								  element.getElementName(),
								  isoDateFormat(config.getDateModified())));
			
			throw new EntityNotFoundException(IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND, 
											  element.getElementName(), 
											  configId);
		}
		return config;
	}

	public int removeElementConfigRevisions(Element element, 
									  		 ElementConfigName configName) {
		int count = repository.execute(removeConfigRevisions(element,configName));
		if(count > 0) {
			event.fire(newElementConfigRemovedEvent()
					   .withGroupId(element.getGroupId())
					   .withGroupName(element.getGroupName())
					   .withGroupType(element.getGroupType())
					   .withElementId(element.getElementId())
					   .withElementName(element.getElementName())
					   .withElementAlias(element.getElementAlias())
					   .withElementRole(element.getElementRoleName())
					   .withConfigName(configName)
					   .build());
		}
		
		LOG.fine(() -> format("%s: Removed all %d %s revisions for element %s.",
							  IVT0337I_ELEMENT_CONFIG_REMOVED.getReasonCode(),
					    	  count,
					    	  configName,
					    	  element.getElementName()));	
		messages.add(MessageFactory.createMessage(IVT0337I_ELEMENT_CONFIG_REMOVED, element.getElementName(),configName));
		
		return count;
	}

	public StoreElementConfigResult editElementConfig(Element element, 
													  ElementConfigId configId, 
													  String comment) {
		Element_Config config = findConfig(element, configId);
		return storeElementConfig(element, 
								  config.getName(), 
								  MediaType.valueOf(config.getContentType()), 
								  CANDIDATE, 
								  config.getConfig(), 
								  comment);
	}

}
