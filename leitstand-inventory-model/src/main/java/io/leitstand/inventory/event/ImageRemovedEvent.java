/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ImageRemovedEvent extends ImageEvent {

	public static Builder newImageRemovedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ImageEventBuilder<ImageRemovedEvent, Builder>{
		public Builder() {
			super(new ImageRemovedEvent());
		}
	}
	
}
