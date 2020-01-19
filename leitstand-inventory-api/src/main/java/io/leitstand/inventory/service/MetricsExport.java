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
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

public class MetricsExport extends ValueObject {
	
	public static Builder newMetricsExport() {
		return new Builder();
	}
	
	public static class Builder {
		
		private MetricsExport export = new MetricsExport();
		
		public Builder withDateCreated(Date date) {
			assertNotInvalidated(getClass(), export);
			export.dateCreated = new Date(date.getTime());
			return this;
		}
		
		public Builder withMetrics(MetricExport.Builder... metrics) {
			return withMetrics(stream(metrics)
							   .map(MetricExport.Builder::build)
							   .collect(toList()));
		}
		
		public Builder withMetrics(List<MetricExport> metrics) {
			assertNotInvalidated(getClass(), export);
			export.metrics = new LinkedList<>(metrics);
			return this;
		}

		public MetricsExport build() {
			try {
				assertNotInvalidated(getClass(), export);
				return export;
			} finally {
				this.export = null;
			}
		}
		
	}
	
	private Date dateCreated = new Date();
	private List<MetricExport> metrics = emptyList();
	
	
	public Date getDateCreated() {
		return new Date(dateCreated.getTime());
	}
	
	public List<MetricExport> getMetrics() {
		return unmodifiableList(metrics);
	}

}
