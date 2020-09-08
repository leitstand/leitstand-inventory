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

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static java.lang.String.format;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import io.leitstand.commons.model.CompositeValue;
import io.leitstand.commons.model.StringUtil;
import io.leitstand.inventory.jsonb.BandwidthAdapter;

/**
 * The bandwidth of a certain data link.
 * <p>
 * The bandwidth is either represented in <code>Kbps</code>, <code>Mbps</code>, <code>Gbps</code> or <code>Tbps</code>.
 * The bandwidth unit provides method to convert a bandwidth from one unit into another unit.
 */
@Embeddable
@JsonbTypeAdapter(BandwidthAdapter.class)
public class Bandwidth extends CompositeValue implements Serializable{

	private static final Pattern BANDWITH_PATTERN = Pattern.compile("^(\\d+[\\.]\\d{3}) ([KMGT]bps)$");
	
	public static Bandwidth bandwidth(float value, String unit) {
	    if(StringUtil.isEmptyString(unit)) {
	        return null;
	    }
	    return new Bandwidth(value, Unit.valueOf(unit));
	}
	
	public static Bandwidth bandwidth(String bandwidth) {
		if(isEmptyString(bandwidth)) {
			return null;
		}
		return new Bandwidth(bandwidth);
	}
	
	/**
	 * Enumeration of available bandwidths units.
	 */
	public enum Unit {
		/**
		 * Kilobits per second.
		 */
		KBPS(1000),
		
		/**
		 * Megabits per second.
		 */
		MBPS(1000000),
		
		/**
		 * Gigabits per second.
		 */
		GBPS(1000000000),
		
		/**
		 * Terabits per second.
		 */
		TBPS(1000000000000L);
		
		private long scale;
		private String label;
		
		private Unit(long scale) {
			this.scale = scale;
			this.label = name().charAt(0)+"bps";
		}
		
		/**
		 * Converts the given bandwidth from this unit to bits per second.
		 * @param value - the bandwidth
		 * @return the given bandwidth converted to bits per second.
		 */
		public long toBps(double value) {
			return Math.round(value * scale);
		}

		/**
		 * Converts the given bandwidth from this unit to kilobits per second.
		 * @param value - the bandwidth
		 * @return the given bandwidth converted to kilobits per second.
		 */
		public double toKpbs(double value) {
			return value*scale/KBPS.scale;
		}

		/**
		 * Converts the given bandwidth from this unit to megabits per second.
		 * @param value - the bandwidth
		 * @return the given bandwidth converted to megabits per second.
		 */
		public double toMpbs(double value) {
			return value*scale/MBPS.scale;
		}
		
		/**
		 * Converts the given bandwidth from this unit to gigabits per second.
		 * @param value - the bandwidth
		 * @return the given bandwidth converted to kilobits per second.
		 */
		public double toGpbs(double value) {
			return value*scale/GBPS.scale;
		}
		
		
		/**
		 * Converts the given bandwidth from this unit to terabits per second.
		 * @param value - the bandwidth
		 * @return the given bandwidth converted to kilobits per second.
		 */
		public double toTpbs(double value) {
			return value*scale/TBPS.scale;
		}
		
		public String getLabel() {
			return label;
		}
	}
	
	private static final long serialVersionUID = 1L;

	private Float value;
	@Enumerated(STRING)
	private Unit unit;
	
	protected Bandwidth() {
		// JPA
	}
	
	/**
	 * Create a <code>Bandwitdh</code> value object.
	 * @param value the bandwidth value
	 * @param unit the bandwidth unit
	 */
	public Bandwidth(float value, Unit unit) {
		this.value = value;
		this.unit  = unit;
	}

	/**
	 * Creates a <code>Bandwidth</code> value object from a string.
	 * <p>
	 * The string uses a dot as thousands separator and a comma as decimal fraction delimiter.
	 * A blank separates the bandwidth value from the bandwidth unit (e.g. <code>100,000 Mbps</code>
	 * </p>
	 * @param bandwidth the bandwidth expressed as string
	 */
	public Bandwidth(String bandwidth){
		Matcher matcher = BANDWITH_PATTERN.matcher(bandwidth);
		matcher.matches();
		value = Float.parseFloat(matcher.group(1));
		unit  = Unit.valueOf(matcher.group(2).toUpperCase());
	}
	
	/**
	 * Returns the bandwidth value.
	 * @return the bandwidth value.
	 */
	public float getValue() {
		return value;
	}
	
	/**
	 * Returns the bandwidth unit.
	 * @return the bandwidth unit.
	 */
	public Unit getUnit() {
		return unit;
	}
	
	
	/**
	 * Returns a string representation of this bandwidth.
	 * <p>
	 * The string uses a dot as thousands separator and a comma as decimal fraction delimiter.
	 * A blank separates the bandwidth value from the bandwidth unit (e.g. <code>100,000 Mbps</code>
	 * </p>
	 * 
	 * @return a string representation of this bandwidth
	 */
	@Override
	public String toString() {
		return format(Locale.ENGLISH,"%.3f %s",value,unit.getLabel());
	}
}

