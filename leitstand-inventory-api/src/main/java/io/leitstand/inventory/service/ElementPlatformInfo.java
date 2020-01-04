/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import javax.json.bind.annotation.JsonbProperty;

import io.leitstand.commons.model.ValueObject;

public class ElementPlatformInfo extends ValueObject{

	public static Builder newPlatformInfo() {
		return new Builder();
	}
	
	public static class Builder {
		
		private ElementPlatformInfo vendor = new ElementPlatformInfo();
		
		public Builder withVendorName(String name) {
			vendor.vendorName = name;
			return this;
		}
		
		public Builder withModelName(String model) {
			vendor.modelName = model;
			return this;
		}
		
		public ElementPlatformInfo build() {
			try {
				if(vendor.modelName==null && vendor.vendorName==null) {
					return null;
				}
				return vendor;
			} finally {
				this.vendor = null;
			}
		}
		
	}

	@JsonbProperty("vendor_name")
	private String vendorName;
	@JsonbProperty("model_name")
	private String modelName;
	
	public String getVendorName() {
		return vendorName;
	}
	
	public String getModelName() {
		return modelName;
	}
	
}
