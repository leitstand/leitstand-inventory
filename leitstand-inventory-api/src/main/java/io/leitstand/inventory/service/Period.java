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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.leitstand.commons.model.CompositeValue;
import io.leitstand.inventory.jsonb.PeriodAdapter;

@JsonbTypeAdapter(PeriodAdapter.class)
public class Period extends CompositeValue{

	private static final Pattern PATTERN = Pattern.compile("([1-9]\\d*)([yMwdhms])");
	
	public enum Unit {
		YEARS("y"),
		MONTHS("M"),
		WEEKS("w"),
		DAYS("d"),
		HOURS("h"),
		MINUTES("m"),
		SECONDS("s");
		
		private String memnonic;
		
		private Unit(String memnonic) {
			this.memnonic = memnonic;
		}
		
		public String getMemnonic() {
			return memnonic;
		}
		
		static Unit fromMemnonic(String memnonic) {
			switch(memnonic) {
				case "y" : return YEARS;
				case "M" : return MONTHS;
				case "w" : return WEEKS;
				case "d" : return DAYS;
				case "h" : return HOURS;
				case "m" : return MINUTES;
				case "s" : return SECONDS;
				default  : throw new IllegalArgumentException("Unknown unit "+memnonic);
			}
		}
	}
	
	public static Period period(String s) {
		return valueOf(s);
	}
	
	public static Period valueOf(String s) {
		if(isEmptyString(s)) {
			return null;
		}
		return new Period(s);
	}
	
	public static String toString(Period p) {
		if(p == null) {
			return null;
		}
		return p.toString();
	}
	
	@NotNull( message="{metric_retention.required}")
	@Min(value=1, message="{metric_retention.invalid}")
	private int duration;
	
	@NotNull( message="{metric_retention.required}")
	private Unit unit;
	
	public Period(String period) {
		Matcher matcher = PATTERN.matcher(period);
		matcher.matches();
		this.duration = Integer.parseInt(matcher.group(1));
		this.unit 	  = Unit.fromMemnonic(matcher.group(2));
	}
	
	public Period(int duration, Unit unit) {
		this.duration = duration;
		this.unit = unit;
	}
	
	public Unit getUnit() {
		return unit;
	}
	
	public int getDuration() {
		return duration;
	}
	
	@Override
	public String toString() {
		return duration+unit.getMemnonic();
	}
	
}
