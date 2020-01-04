/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

import io.leitstand.inventory.service.ImageState;

public class ImageStateChangedEvent extends ImageEvent {

	public static Builder newImageStateChangedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ImageEventBuilder<ImageStateChangedEvent, Builder>{
		public Builder() {
			super(new ImageStateChangedEvent());
		}
		
		public Builder withPreviousState(ImageState state) {
			instance.previousImageState = state;
			return this;
		}
	}
	
	private ImageState previousImageState;
	
	public ImageState getPreviousImageState() {
		return previousImageState;
	}
}
