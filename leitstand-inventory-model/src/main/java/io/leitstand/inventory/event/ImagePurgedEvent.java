/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.event;

public class ImagePurgedEvent extends ImageEvent{

	public static Builder newImagePurgedEvent() {
		return new Builder();
	}
	
	public static class Builder extends ImageEventBuilder<ImagePurgedEvent, Builder>{
		public Builder() {
			super(new ImagePurgedEvent());
		}
	}
	
}
