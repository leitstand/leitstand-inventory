/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ImageAddedEvent extends ImageEvent {

	public static Builder newImageAddedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ImageEventBuilder<ImageAddedEvent, Builder>{
		public Builder() {
			super(new ImageAddedEvent());
		}
	}
}
