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
import static io.leitstand.commons.jsonb.IsoDateAdapter.isoDateFormat;
import static io.leitstand.commons.messages.MessageFactory.createMessage;
import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.inventory.event.ElementConfigRemovedEvent.newElementConfigRemovedEvent;
import static io.leitstand.inventory.event.ElementConfigRevisionRemovedEvent.newElementConfigRevisionRemovedEvent;
import static io.leitstand.inventory.model.ElementValueObjects.elementValueObject;
import static io.leitstand.inventory.model.Element_Config.removeConfigRevisions;
import static io.leitstand.inventory.model.Element_Config_Revision.findActiveConfig;
import static io.leitstand.inventory.model.Element_Config_Revision.findElementConfig;
import static io.leitstand.inventory.model.Element_Config_Revision.findLatestConfig;
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
import static io.leitstand.inventory.service.ReasonCode.IVT0333E_ELEMENT_CONFIG_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0334E_ELEMENT_ACTIVE_CONFIG_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT0335I_CREATED_CANDIDATE_CONFIG_FROM_SUPERESED_CONFIG;
import static io.leitstand.inventory.service.ReasonCode.IVT0337I_ELEMENT_CONFIG_REMOVED;
import static io.leitstand.inventory.service.ReasonCode.IVT0338E_ELEMENT_CONFIG_NOT_RESTORABLE;
import static io.leitstand.inventory.service.ReasonCode.IVT0339I_SUPERSEDED_CONFIG_MATCHES_EXISTING_CANDIDATE_CONFIG;
import static io.leitstand.inventory.service.StoreElementConfigResult.configCreated;
import static io.leitstand.inventory.service.StoreElementConfigResult.seeOther;
import static io.leitstand.security.auth.UserName.userName;
import static java.lang.String.format;
import static java.util.logging.Logger.getLogger;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import io.leitstand.commons.ConflictException;
import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
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

@Dependent
public class ElementConfigManager {

	private static final Logger LOG = getLogger(ElementConfigManager.class.getName());
	
	private Repository repository;
	private DatabaseService database;
	private Messages messages;
	private Event<ElementConfigEvent> event;
	private UserContext creator;
	
	@Inject
	protected ElementConfigManager(	@Inventory Repository repository, 
									@Inventory DatabaseService database,
									UserContext authenticated,
									Event<ElementConfigEvent> event,
									Messages messages){
		this.creator = authenticated;
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
																			 "FROM inventory.element_config_revision "+
																			 "WHERE element_id = ? "+
																			 "AND name ~ ? "+
																			 "GROUP BY element_id, name )"+
																			 "SELECT r.uuid, r.name, r.state, r.creator, r.comment, c.type, r.tsmodified "+
																			 "FROM history h "+
																			 "JOIN inventory.element_config_revision r "+
																			 "ON h.name = r.name "+
																			 "AND h.element_id = r.element_id "+
																			 "AND h.tsmodified = r.tsmodified "+
																			 "JOIN inventory.content c "+
																			 "ON r.content_hash = c.hash "+
																			 "ORDER BY r.name",	
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
		
		return elementValueObject(newElementConfigs(),element)
			   .withDateModified(element.getDateModified())
			   .withConfigs(configs)
			   .build();
		
	}
	
	public ElementConfigRevisions getElementConfigRevisions(Element element,
														 	ElementConfigName configName){
		
		List<ElementConfigReference> revisions = database.executeQuery(prepare("SELECT uuid,state,creator,comment,tsmodified "+
																			   "FROM inventory.element_config_revision "+
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
		
		return elementValueObject(newElementConfigRevisions(),element)
			   .withElementConfigName(configName)
			   .withElementConfigRevisions(revisions)
			   .build();
	}
	
	
	public ElementConfig getElementConfig(Element element, ElementConfigId configId) {
		Element_Config_Revision config = repository.execute(findElementConfig(configId));
		
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

	private ElementConfig config(Element element, Element_Config_Revision config) {
		return elementValueObject(newElementConfig(), element)
			   .withConfigId(config.getConfigId())
			   .withConfigName(config.getName())
			   .withContentType(config.getContentType())
			   .withContentHash(config.getContentHash())
			   .withDateModified(config.getDateModified())
			   .withComment(config.getComment())
			   .withCreator(config.getCreator())
			   .withConfigState(config.getConfigState())
			   .build();
	}
	
	public ElementConfig getActiveElementConfig(Element element, ElementConfigName configName) {
		Element_Config_Revision config = repository.execute(findActiveConfig(element,configName));
		
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
	
	public ElementConfig getElementConfig(Element element, ElementConfigName configName) {
       Element_Config_Revision config = repository.execute(findLatestConfig(element, configName));
        
       if(config == null) {
            LOG.fine(() -> format("%s: No %s configuration for element %s found.",
                                  IVT0333E_ELEMENT_CONFIG_NOT_FOUND,
                                  configName,
                                  element.getElementName()));
            
            throw new EntityNotFoundException(IVT0333E_ELEMENT_CONFIG_NOT_FOUND, 
                                              element.getElementName(), 
                                              configName);
        }
        
        return config(element,
                      config);
	}
	

	public void removeElementConfig(Element element, 
									ElementConfigId configId) {
		Element_Config_Revision config = repository.execute(findElementConfig(configId));
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
					   .withAdministrativeState(element.getAdministrativeState())
					   .withOperationalState(element.getOperationalState())
					   .withDateModified(element.getDateModified())
					   .withConfigName(config.getName())
					   .withConfigDate(config.getDateModified())
					   .withContentType(config.getContentType())
					   .withCreator(config.getCreator())
					   .build());

		}
	}

	public void setElementConfigComment(Element element,
										ElementConfigId configId, 
										String comment) {
		Element_Config_Revision config = findConfig(element, configId);
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

	private Element_Config_Revision findConfig(Element element, ElementConfigId configId) {
		Element_Config_Revision config = repository.execute(findElementConfig(configId));
		if(config == null) {
			LOG.fine(() -> format("%s: Configuration %s for element %s not found",
								  IVT0332E_ELEMENT_CONFIG_REVISION_NOT_FOUND.getReasonCode(),
								  configId,
								  element.getElementName()));
			
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
					   .withAdministrativeState(element.getAdministrativeState())
					   .withOperationalState(element.getOperationalState())
					   .withDateModified(element.getDateModified())
					   .withConfigName(configName)
					   .build());
		}
		
		LOG.fine(() -> format("%s: Removed all %d %s revisions for element %s.",
							  IVT0337I_ELEMENT_CONFIG_REMOVED.getReasonCode(),
					    	  count,
					    	  configName,
					    	  element.getElementName()));	
		messages.add(createMessage(IVT0337I_ELEMENT_CONFIG_REMOVED, element.getElementName(),configName));
		
		return count;
	}

	public StoreElementConfigResult restoreElementConfig(Element element, 
													  	 ElementConfigId configId, 
													  	 String comment) {
		Element_Config_Revision config = findConfig(element, configId);
		if(config.getConfigState() != ConfigurationState.SUPERSEDED) {
		    LOG.fine(() -> format("%s: Cannot restore %s %s configuration. Only superseded configurations are restorable.",
		                          IVT0338E_ELEMENT_CONFIG_NOT_RESTORABLE.getReasonCode(),
		                          config.getConfigState(),
		                          config.getName()));
		    throw new ConflictException(IVT0338E_ELEMENT_CONFIG_NOT_RESTORABLE,config.getName(),config.getConfigState());
		}
		// Remove existing candidate configuration. Only one candidate configuration must exist at a time.
		Element_Config_Revision latest = repository.execute(findLatestConfig(element, config.getName()));
		if (latest != null && latest.isCandidateConfig()) {
			if (latest.getContentHash().equals(config.getContentHash())) {
				// Configuration matches the current candidate configuration.
				// No new candidate configuration needed!
			    LOG.fine(() -> format("%s: The candidate %s configuration and the superseded configuration are equal. No need to create a new candidate configuration.",
			    				      IVT0339I_SUPERSEDED_CONFIG_MATCHES_EXISTING_CANDIDATE_CONFIG.getReasonCode(),
			    				      config.getName()));
			    messages.add(createMessage(IVT0339I_SUPERSEDED_CONFIG_MATCHES_EXISTING_CANDIDATE_CONFIG, config.getName()));
				return seeOther(latest.getConfigId());
			}
			repository.remove(latest);
		}
		Element_Config_Revision candidate = new Element_Config_Revision(config,creator.getUserName(),comment);
		repository.add(candidate);
	    LOG.fine(() -> format("%s: Created new %s candidate configuration from the superseded configuration.",
	    					  IVT0335I_CREATED_CANDIDATE_CONFIG_FROM_SUPERESED_CONFIG.getReasonCode(),
	    					  config.getName()));
	    messages.add(createMessage(IVT0335I_CREATED_CANDIDATE_CONFIG_FROM_SUPERESED_CONFIG, config.getName()));
		return configCreated(candidate.getConfigId());
	}

}
