/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;

import java.util.Map;
import java.util.TreeMap;

import io.leitstand.commons.model.ValueObject;

public class ImageStatistics extends ValueObject {

	
	public static Builder newImageStatistics() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ImageStatistics stats = new ImageStatistics();
		
		public Builder withImage(ImageInfo.Builder image) {
			return withImage(image.build());
		}
		
		public Builder withImage(ImageInfo image) {
			assertNotInvalidated(getClass(), stats);
			stats.image = image;
			return this;
		}
		
		public Builder withActiveCount(Map<ElementGroupName,Integer> activeCount) {
			assertNotInvalidated(getClass(), stats);
			stats.activeCount = new TreeMap<>(activeCount);
			return this;
		}
		
		public Builder withCachedCount(Map<ElementGroupName,Integer> cachedCount) {
			assertNotInvalidated(getClass(), stats);
			stats.cachedCount = new TreeMap<>(cachedCount);
			return this;
		}
		
		public ImageStatistics build() {
			try {
				assertNotInvalidated(getClass(), stats);
				return stats;
			} finally {
				this.stats = null;
			}
		}
		
	}

	
	private ImageInfo image;
	private Map<ElementGroupName,Integer> activeCount = emptyMap();
	private Map<ElementGroupName,Integer> cachedCount = emptyMap();
	
	public ImageInfo getImage() {
		return image;
	}
	
	public Map<ElementGroupName,Integer> getActiveCount(){
		return unmodifiableMap(activeCount);
	}
	
	public Map<ElementGroupName,Integer> getCachedCount(){
		return unmodifiableMap(cachedCount);
	}
	
	
	
	
}
