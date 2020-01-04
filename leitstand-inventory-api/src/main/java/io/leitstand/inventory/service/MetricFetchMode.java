/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

public enum MetricFetchMode {
	METADATA,
	VISUALIZATION_CONFIG,
	ALERT_CONFIG,
	STORAGE_CONFIG,
	ALL;
	
	
	public boolean fetchVisualizationConfig() {
		return this == VISUALIZATION_CONFIG || this == ALL;
	}
	
	public boolean fetchAlertConfig() {
		return this == ALERT_CONFIG || this == ALL;
	}
	
	public boolean fetchStorageConfig() {
		return this == STORAGE_CONFIG || this == ALL;
	}
}
