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
		
		public Builder withElementGroupCounters(ImageStatisticsElementGroupImageCount.Builder... groups) {
		    return withElementGroupCounters(stream(groups)
		                                    .map(ImageStatisticsElementGroupImageCount.Builder::build)
		                                    .collect(toList()));
		}
		
        public Builder withElementGroupCounters(Collection<ImageStatisticsElementGroupImageCount> groups) {
            assertNotInvalidated(getClass(), stats);
            stats.groups = unmodifiableList(new ArrayList<>(groups));
            return this;
        }
        
        public Builder withReleases(ReleaseRef.Builder... releases) {
            return withReleases(stream(releases)
                                .map(ReleaseRef.Builder::build)
                                .collect(toList()));
        }
        
        public Builder withReleases(List<ReleaseRef> releases) {
            assertNotInvalidated(getClass(),stats);
            stats.releases = unmodifiableList(new ArrayList<>(releases));
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
	private List<ImageStatisticsElementGroupImageCount> groups;
	private List<ReleaseRef> releases;
	
	public ImageInfo getImage() {
		return image;
	}
	
	public List<ImageStatisticsElementGroupImageCount> getGroups() {
        return groups;
    }
	
	public List<ReleaseRef> getReleases() {
        return releases;
    }
	
	
	
	
}
