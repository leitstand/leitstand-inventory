/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
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
