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
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

/**
 * Image deployment statistics.
 * <p>
 * The image statistics provides information about which releases and element groups use the image.
 */
public class ImageDeploymentStatistics extends ValueObject {

    /**
     * Creates a builder for an immutable <code>ImageStatistics</code> value object.
     * @return a builder for an immutable <code>ImageStatistics</code> value object.
     */
	public static Builder newImageDeploymentStatistics() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ImageStatistics</code> value object.
	 */
	public static class Builder {
		
		private ImageDeploymentStatistics stats = new ImageDeploymentStatistics();
		
		public Builder withImage(ImageInfo.Builder image) {
			return withImage(image.build());
		}
		
		/**
		 * Sets the image information.
		 * @param image the image information.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImage(ImageInfo image) {
			assertNotInvalidated(getClass(), stats);
			stats.image = image;
			return this;
		}

	    /**
         * Sets the image count per element group.
         * @param imagesPerGroup the image count per element group.
         * @return a reference to this builder to continue object creation.
         */
		public Builder withElementGroupCounters(ImageStatisticsElementGroupImageCount.Builder... imagesPerGroup) {
		    return withElementGroupCounters(stream(imagesPerGroup)
		                                    .map(ImageStatisticsElementGroupImageCount.Builder::build)
		                                    .collect(toList()));
		}

	    /**
         * Sets the image count per element group.
         * @param imagesPerGroup the image count per element group.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withElementGroupCounters(Collection<ImageStatisticsElementGroupImageCount> imagesPerGroup) {
            assertNotInvalidated(getClass(), stats);
            stats.groups = unmodifiableList(new ArrayList<>(imagesPerGroup));
            return this;
        }
        
        /**
         * Sets the releases using the image.
         * @param releases the releases using the image.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withReleases(ReleaseRef.Builder... releases) {
            return withReleases(stream(releases)
                                .map(ReleaseRef.Builder::build)
                                .collect(toList()));
        }
        
        
        /**
         * Sets the releases using the image.
         * @param releases the releases using the image.
         * @return a reference to this builder to continue object creation.
         */
        public Builder withReleases(List<ReleaseRef> releases) {
            assertNotInvalidated(getClass(),stats);
            stats.releases = unmodifiableList(new ArrayList<>(releases));
            return this;
        }
		
		/**
		 * Creates an immutable <code>ImageStatistics</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ImageStatistics</code> value object.
		 */
		public ImageDeploymentStatistics build() {
			try {
				assertNotInvalidated(getClass(), stats);
				return stats;
			} finally {
				this.stats = null;
			}
		}
		
	}

	
	private ImageInfo image;
	private List<ImageStatisticsElementGroupImageCount> groups;
	private List<ReleaseRef> releases;
	
	/**
	 * Returns the image information.
	 * @return the image information.
	 */
	public ImageInfo getImage() {
		return image;
	}
	
	/**
	 * Returns the image utilization statistics per element group.
	 * @return the image utilization statistics per element group.
	 */
	public List<ImageStatisticsElementGroupImageCount> getGroups() {
        return unmodifiableList(groups);
    }
	
	/**
	 * Returns the releases using the image.
	 * @return the releases using the image.
	 */
	public List<ReleaseRef> getReleases() {
        return unmodifiableList(releases);
    }
	
	
	
	
}
