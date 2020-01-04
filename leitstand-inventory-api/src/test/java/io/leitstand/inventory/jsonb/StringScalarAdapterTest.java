/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.jsonb;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import javax.json.bind.adapter.JsonbAdapter;
import javax.json.bind.annotation.JsonbTypeAdapter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.service.AlertRuleId;
import io.leitstand.inventory.service.AlertRuleName;
import io.leitstand.inventory.service.ApplicationName;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
import io.leitstand.inventory.service.DnsZoneId;
import io.leitstand.inventory.service.DnsZoneName;
import io.leitstand.inventory.service.ElementAlias;
import io.leitstand.inventory.service.ElementConfigId;
import io.leitstand.inventory.service.ElementConfigName;
import io.leitstand.inventory.service.ElementGroupId;
import io.leitstand.inventory.service.ElementGroupName;
import io.leitstand.inventory.service.ElementGroupType;
import io.leitstand.inventory.service.ElementId;
import io.leitstand.inventory.service.ElementName;
import io.leitstand.inventory.service.ElementRoleId;
import io.leitstand.inventory.service.ElementRoleName;
import io.leitstand.inventory.service.EnvironmentId;
import io.leitstand.inventory.service.EnvironmentName;
import io.leitstand.inventory.service.IPv4Prefix;
import io.leitstand.inventory.service.IPv6Prefix;
import io.leitstand.inventory.service.IPvxPrefix;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.ModuleName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.RoutingInstanceName;
import io.leitstand.inventory.service.ServiceName;
import io.leitstand.inventory.service.VisualizationConfigId;
import io.leitstand.inventory.service.VisualizationConfigName;

@RunWith(Parameterized.class)
public class StringScalarAdapterTest {

	@Parameters
	public static Collection<Object[]> adapters(){
		String uuid = randomUUID().toString();
		Object[][] adapters = new Object[][]{
			{new AlertRuleIdAdapter(),		 		"unit-app", 			new AlertRuleId("unit-app")},
			{new AlertRuleNameAdapter(),			"unit-app", 			new AlertRuleName("unit-app")},
			{new ApplicationNameAdapter(),			"unit-app", 			new ApplicationName("unit-app")},
			{new DnsNameAdapter(),					"unit-dnsname",			new DnsName("unit-dnsname")},
			{new DnsRecordSetIdAdapter(),			uuid,				    new DnsRecordSetId(uuid)},
			{new DnsRecordTypeAdapter(),			"unit-dnstype",			new DnsRecordType("unit-dnstype")},
			{new DnsZoneIdAdapter(),				uuid,					new DnsZoneId(uuid)},
			{new DnsZoneNameAdapter(),				"unit-zone",			new DnsZoneName("unit-zone")},
			{new DnsRecordSetIdAdapter(),			uuid,					new DnsRecordSetId(uuid)},
			{new DnsRecordTypeAdapter(),			"unit-dnstype",			new DnsRecordType("unit-dnstype")},
			{new ElementIdAdapter(),	 		 	uuid,					new ElementId(uuid)},
			{new ElementAliasAdapter(),		 		"unit-alias",			new ElementAlias("unit-alias")},
			{new ElementConfigIdAdapter(),	 		uuid,					new ElementConfigId(uuid)},
			{new ElementConfigNameAdapter(),		"unit-config",			new ElementConfigName("unit-config")},
			{new ElementGroupIdAdapter(),	 		uuid,					new ElementGroupId(uuid)},
			{new ElementGroupNameAdapter(),	 		"unit-group",			new ElementGroupName("unit-group")},
			{new ElementGroupTypeAdapter(),	 		"unit-type",			new ElementGroupType("unit-type")},
			{new ElementNameAdapter(),		 		"unit-element",			new ElementName("unit-element")},
			{new ElementRoleIdAdapter(),	 	 	uuid,					new ElementRoleId(uuid)},
			{new ElementRoleNameAdapter(),	 		"unit-role",			new ElementRoleName("unit-role")},
			{new EnvironmentIdAdapter(),	 	 	uuid,					new EnvironmentId(uuid)},
			{new EnvironmentNameAdapter(),	 		"unit-env",				new EnvironmentName("unit-env")},
			{new ImageIdAdapter(),  			 	uuid,		 			new ImageId(uuid)},
			{new ImageNameAdapter(),  		 		"unit-image",			new ImageName("unit-image")},
			{new InterfaceNameAdapter(),		 	"unit-0/0/0",			new InterfaceName("unit-0/0/0")},
			{new IPv4PrefixAdapter(),			 	"10.0.0/8",				new IPv4Prefix("10.0.0/8")},
			{new IPv6PrefixAdapter(),			 	"10.0.0/8",				new IPv6Prefix("10.0.0/8")},
			{new IPvxPrefixAdapter(),				"10.0.0/8",				new IPvxPrefix("10.0.0/8")},
			{new MACAddressAdapter(),				"00:11:22:33:44:55", 	new MACAddress("00:11:22:33:44:55")},
			{new MetricIdAdapter(),					uuid,					new MetricId(uuid)},
			{new MetricNameAdapter(),			 	"unit-metric",			new MetricName("unit-metric")},
			{new ModuleNameAdapter(),			 	"unit-module",			new ModuleName("unit-module")},
			{new ElementGroupIdAdapter(),	 	 	uuid,					new ElementGroupId(uuid)},
			{new ElementGroupNameAdapter(),	 		"unit-group",			new ElementGroupName("unit-group")},
			{new ElementGroupTypeAdapter(),	 		"unit-type",			new ElementGroupType("unit-type")},
			{new ServiceNameAdapter(),		 		"unit-service",			new ServiceName("unit-service")},
			{new PlatformIdAdapter(),			 	uuid,					new PlatformId(uuid)},
			{new RackNameAdapter(),			 		"unit-rack",			new RackName("unit-rack")},
			{new RoutingInstanceNameAdapter(), 		"unit-vfr",				new RoutingInstanceName("unit-vfr")},
			{new VisualizationConfigIdAdapter(),	uuid,					new VisualizationConfigId(uuid)},
			{new VisualizationConfigNameAdapter(),	"unit-visualization",	new VisualizationConfigName("unit-visualization")}

		};
		return asList(adapters);
	}
	
	
	private JsonbAdapter<Scalar<String>,String> adapter;
	private Scalar<String> scalar;
	private String value;
	
	public StringScalarAdapterTest(JsonbAdapter<Scalar<String>,String> adapter,
								   String value,
								   Scalar<String> scalar) {
		this.adapter = adapter;
		this.value = value;
		this.scalar = scalar;
		
	}
	
	@Test
	public void empty_string_is_mapped_to_null() throws Exception {
		assertNull(adapter.getClass().getSimpleName(), 
				   adapter.adaptFromJson(""));
	}
	
	@Test
	public void null_string_is_mapped_to_null() throws Exception {
		assertNull(adapter.getClass().getSimpleName(),
				   adapter.adaptFromJson(null));
	}
	
	@Test
	public void adapt_from_json() throws Exception{
		assertEquals(adapter.getClass().getSimpleName(),
					 scalar,adapter.adaptFromJson(value));
	}
	
	@Test
	public void adapt_to_json() throws Exception {
		assertEquals(adapter.getClass().getSimpleName(),
					 value,adapter.adaptToJson(scalar));
	}
	
	@Test
	public void null_scalar_is_mapped_to_null() throws Exception{
		assertNull(adapter.adaptToJson(null));
	}
	
	@Test
	public void jsonb_adapter_annotation_present() {
		assertTrue(scalar.getClass().isAnnotationPresent(JsonbTypeAdapter.class));
		assertSame(adapter.getClass(),scalar.getClass().getAnnotation(JsonbTypeAdapter.class).value());
	}
}
