/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.Patterns.UUID_PATTERN;

import java.util.UUID;

import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.validation.constraints.Pattern;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.jsonb.DnsRecordSetIdAdapter;

@JsonbTypeAdapter(DnsRecordSetIdAdapter.class)
public class DnsRecordSetId extends Scalar<String>{

	private static final long serialVersionUID = 1L;

	public static DnsRecordSetId randomDnsRecordSetId() {
		return valueOf(UUID.randomUUID().toString());
	}
	
	public static DnsRecordSetId dnsRecordSetId(String id) {
		return valueOf(id);
	}
	
	public static DnsRecordSetId valueOf(String id) {
		return Scalar.fromString(id, DnsRecordSetId::new);
	}
	
	@Pattern(regexp=UUID_PATTERN, message="{dns_recordset_id.invalid}")
	private String value;
	
	public DnsRecordSetId(String id) {
		this.value = id;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
}
