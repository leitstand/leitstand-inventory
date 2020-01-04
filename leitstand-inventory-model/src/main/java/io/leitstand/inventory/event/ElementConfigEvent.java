/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTypeAdapter;

import io.leitstand.commons.jsonb.IsoDateAdapter;
import io.leitstand.inventory.service.ConfigurationState;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.security.auth.UserId;

public abstract class ElementConfigEvent extends ElementEvent {

	static class ElementConfigEventBuilder<E extends ElementConfigEvent, B extends ElementConfigEventBuilder<E,B>> extends ElementEventBuilder<E, B>{
		
		ElementConfigEventBuilder(E event){
			super(event);
		}

		public B withConfigId(ElementConfigId configId) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).configId = configId;
			return (B) this;
		}
		
		public B withConfigName(ElementConfigName configName) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).configName = configName;
			return (B) this;
		}

		public B withConfigState(ConfigurationState configState) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).configState = configState;
			return (B) this;
		}
		
		public B withContentType(String contentType) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).contentType = contentType;
			return (B) this;
		}
		
		public B withCreator(UserId creator) {
			assertNotInvalidated(getClass(),object);
			((ElementConfigEvent)object).creator = creator;
			return (B) this;
		}
		
		public B withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), object);
			((ElementConfigEvent)object).dateModified = new Date(dateModified.getTime());
			return (B) this;
		}
		
	}
	
	private ElementConfigId configId;
	
	@JsonbProperty("datastore")
	private ElementConfigName configName;
	
	private ConfigurationState configState;
	private String contentType;
	@JsonbTypeAdapter(IsoDateAdapter.class)
	private Date dateModified;
	private UserId creator;
	
	
	public ElementConfigId getConfigId() {
		return configId;
	}
	
	public ElementConfigName getConfigName() {
		return configName;
	}
	
	public ConfigurationState getConfigState() {
		return configState;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public Date getDateModified() {
		if(dateModified == null) {
			return null;
		}
		return new Date(dateModified.getTime());
	}
	
	public UserId getCreator() {
		return creator;
	}
	
}
