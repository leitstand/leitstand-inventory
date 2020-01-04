/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;

import java.util.Date;

public class ElementConfigRevisionRemovedEvent extends ElementConfigEvent {

	public static Builder newElementConfigRevisionRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ElementConfigEventBuilder<ElementConfigRevisionRemovedEvent, Builder>{
		
		Builder(){
			super(new ElementConfigRevisionRemovedEvent());
		}
		
		public Builder withDateModified(Date dateModified) {
			assertNotInvalidated(getClass(), object);
			object.dateModified = new Date(dateModified.getTime());
			return this;
		}
	}
	
	private Date dateModified;
	
	public Date getDateModified() {
		if(dateModified == null) {
			return null;
		}
		return new Date(dateModified.getTime());
	}

	
		
}
