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
import static java.lang.Float.parseFloat;
import static java.lang.Math.round;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;
import static java.util.regex.Pattern.compile;
import static javax.persistence.EnumType.STRING;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import io.leitstand.commons.model.CompositeValue;
import io.leitstand.inventory.jsonb.BandwidthAdapter;

/**
 * The bandwidth of an interface.
 * <p>
 * The bandwidth is either represented in <code>Kbps</code>, <code>Mbps</code>, <code>Gbps</code> or <code>Tbps</code>.
 * The bandwidth unit provides method to convert a bandwidth from one unit into another unit.
 */
@Embeddable
@JsonbTypeAdapter(BandwidthAdapter.class)
public class Bandwidth extends CompositeValue implements Serializable{

	private static final Pattern BANDWITH_PATTERN = compile("^(\\d+(?:\\.\\d{3})?)\\s?([KMGT]bps)$");
	
	/**
	 * Creates a bandwidth value object.
	 * @param value the bandwidth value
	 * @param unit the bandwidth unit
	 * @return the bandwidth value
	 */
	public static Bandwidth bandwidth(float value, String unit) {
	    if(isEmptyString(unit)) {
	        return null;
	    }
	    return new Bandwidth(value, Unit.valueOf(unit));
	}
	
	/**
	 * Creates a bandwidth object from the specified string. 
	 * <p>
     * The string representation must consist of the bandwidth value with a fraction of three digits followed by the unit.
     * A blank between bandwidth and unit is accepted. 
     * Example strings are shown below:
     * <ul>
     *  <li>1.000 Gbps for a one gigabit interface</li>
     *  <li>10.000Gbps for a ten gigabit interface</li>
     *  <li>100.000 Mbps for a 100 megabit interface</li>
     * </ul>
	 * <p>
	 * Returns <code>null</code> if the given string is <code>null</code> or empty.
	 * @param bandwidth the bandwidth string
	 * @return the bandwidth object or <code>null</code> if the given string is <code>null</code> or empty.
	 */
	public static Bandwidth bandwidth(String bandwidth) {
		if(isEmptyString(bandwidth)) {
			return null;
		}
		return new Bandwidth(bandwidth);
	}
	
	/**
	 * Enumeration of supported bandwidths units.
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
		 * Converts the bandwidth to bits per second.
		 * @param value the bandwidth
		 * @return the bandwidth in bits per second.
		 */
		public long toBps(double value) {
			return round(value * scale);
		}

		/**
		 * Converts the bandwidth to kilobits per second.
		 * @param value the bandwidth
		 * @return the bandwidth in kilobits per second.
		 */
		public double toKpbs(double value) {
			return value*scale/KBPS.scale;
		}

        /**
         * Converts the bandwidth to megabits per second.
         * @param value the bandwidth
         * @return the bandwidth in megabits per second.
         */
		public double toMpbs(double value) {
			return value*scale/MBPS.scale;
		}
		
        /**
         * Converts the bandwidth to gigabits per second.
         * @param value the bandwidth
         * @return the bandwidth in gigabits per second.
         */
		public double toGpbs(double value) {
			return value*scale/GBPS.scale;
		}
		
        /**
         * Converts the bandwidth to terrabits per second.
         * @param value the bandwidth
         * @return the bandwidth in terrabits per second.
         */
		public double toTpbs(double value) {
			return value*scale/TBPS.scale;
		}
		
		/**
		 * Returns the label of the bandwidth unit.
		 * @return the label of the bandiwdth unit.
		 */
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
	 * Create a <code>Bandwidth</code> value object.
	 * @param value the bandwidth value
	 * @param unit the bandwidth unit
	 */
	public Bandwidth(float value, Unit unit) {
		this.value = value;
		this.unit  = unit;
	}

   /**
    * Creates a new bandwidth from the given string.
    * <p>
    * The string representation must consist of the bandwidth value with a fraction of three digits followed by the unit.
    * A blank between bandwidth and unit is accepted. 
    * Example strings are shown below:
    * <ul>
    *  <li>1.000 Gbps for a one gigabit interface</li>
    *  <li>10.000Gbps for a ten gigabit interface</li>
    *  <li>100.000 Mbps for a 100 megabit interface</li>
    * </ul>
    * @param bandwidth the bandwith string representation
    */
	public Bandwidth(String bandwidth){
		Matcher matcher = BANDWITH_PATTERN.matcher(bandwidth);
		matcher.matches();
		value = parseFloat(matcher.group(1));
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
	 * The string consist of the bandwidth unit as float with a fraction of three digits followed by the unit (e.g. 100.000 Mbps for a 100 megabit interface).
	 * </p>
	 * @return the bandwidth string representation
	 */
	@Override
	public String toString() {
		return format(ENGLISH,"%.3f %s",value,unit.getLabel());
	}
}

