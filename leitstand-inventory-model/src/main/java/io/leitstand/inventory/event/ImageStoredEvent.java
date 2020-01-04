/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ImageStoredEvent extends ImageEvent {

	public static Builder newImageStoredEvent() {
		return new Builder();
	}
	
	public static class Builder extends ImageEventBuilder<ImageStoredEvent, Builder>{
		public Builder() {
			super(new ImageStoredEvent());
		}
	}
}
