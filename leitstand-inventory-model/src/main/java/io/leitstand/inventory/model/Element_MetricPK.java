/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;

import java.io.Serializable;
import java.util.Objects;

public class Element_MetricPK implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long element;
	private Long metric;
	
	public Element_MetricPK() {
		// JPA
	}
	
	public Element_MetricPK(Element element, Metric metric) {
		this.element = element.getId();
		this.metric = metric.getId();
	}

	@Override
	public boolean equals(Object o) {
		if(o==this) {
			return true;
		}
		if(o==null) {
			return false;
		}
		if(o.getClass() != getClass()) {
			return false;
		}
		Element_MetricPK pk = (Element_MetricPK) o;
		if(isDifferent(element, pk.element)) {
			return false;
		}
		if(isDifferent(metric, pk.metric)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(element,metric);
	}
}
