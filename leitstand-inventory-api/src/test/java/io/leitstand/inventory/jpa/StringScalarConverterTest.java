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
package io.leitstand.inventory.jpa;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.UUID;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.leitstand.commons.model.Scalar;
import io.leitstand.inventory.service.ApplicationName;
import io.leitstand.inventory.service.DnsName;
import io.leitstand.inventory.service.DnsRecordSetId;
import io.leitstand.inventory.service.DnsRecordType;
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
import io.leitstand.inventory.service.FacilityName;
import io.leitstand.inventory.service.FacilityType;
import io.leitstand.inventory.service.IPv4Prefix;
import io.leitstand.inventory.service.IPv6Prefix;
import io.leitstand.inventory.service.IPvxPrefix;
import io.leitstand.inventory.service.ImageId;
import io.leitstand.inventory.service.ImageName;
import io.leitstand.inventory.service.ImageType;
import io.leitstand.inventory.service.InterfaceName;
import io.leitstand.inventory.service.MACAddress;
import io.leitstand.inventory.service.ModuleName;
import io.leitstand.inventory.service.PlatformChipsetName;
import io.leitstand.inventory.service.PlatformId;
import io.leitstand.inventory.service.PlatformName;
import io.leitstand.inventory.service.RackName;
import io.leitstand.inventory.service.ReleaseName;
import io.leitstand.inventory.service.ServiceName;

@RunWith(Parameterized.class)
public class StringScalarConverterTest {

	@Parameters
	public static Collection<Object[]> converters(){
		String uuid = UUID.randomUUID().toString();
		Object[][] converters = new Object[][]{
			{new ApplicationNameConverter(),			"unit-app", 			new ApplicationName("unit-app")},
			{new DnsNameConverter(),					"unit-dnsname",			new DnsName("unit-dnsname")},
			{new DnsRecordSetIdConverter(),			    uuid,				    new DnsRecordSetId(uuid)},
			{new DnsRecordTypeConverter(),				"unit-dnstype",			new DnsRecordType("unit-dnstype")},
			{new DnsZoneNameConverter(),				"unit-zone",			new DnsZoneName("unit-zone")},
			{new ElementIdConverter(),	 		 		uuid,					new ElementId(uuid)},
			{new ElementAliasConverter(),		 		"unit-alias",			new ElementAlias("unit-alias")},
			{new ElementConfigIdConverter(),	 		uuid,					new ElementConfigId(uuid)},
			{new ElementConfigNameConverter(),			"unit-config",			new ElementConfigName("unit-config")},
			{new ElementGroupIdConverter(),	 			uuid,					new ElementGroupId(uuid)},
			{new ElementGroupNameConverter(),	 		"unit-group",			new ElementGroupName("unit-group")},
			{new ElementGroupTypeConverter(),	 		"unit-type",			new ElementGroupType("unit-type")},
			{new ElementNameConverter(),		 		"unit-element",			new ElementName("unit-element")},
			{new ElementRoleIdConverter(),	 	 		uuid,					new ElementRoleId(uuid)},
			{new ElementRoleNameConverter(),	 		"unit-role",			new ElementRoleName("unit-role")},
			{new EnvironmentIdConverter(),	 	 		uuid,					new EnvironmentId(uuid)},
			{new EnvironmentNameConverter(),	 		"unit-env",				new EnvironmentName("unit-env")},
			{new FacilityNameConverter(),				"unit-facility",		new FacilityName("unit-facility")},
			{new FacilityTypeConverter(),				"unit-facility-type",	new FacilityType("unit-facility-type")},
			{new ImageIdConverter(),  			 		uuid,		 			new ImageId(uuid)},
			{new ImageNameConverter(),  		 		"unit-image",			new ImageName("unit-image")},
			{new ImageTypeConverter(),  		 		"unit-type",			new ImageType("unit-type")},
			{new InterfaceNameConverter(),		 		"unit-0/0/0",			new InterfaceName("unit-0/0/0")},
			{new IPv4PrefixConverter(),			 		"10.0.0/8",				new IPv4Prefix("10.0.0/8")},
			{new IPv6PrefixConverter(),			 		"10.0.0/8",				new IPv6Prefix("10.0.0/8")},
			{new IPvxPrefixConverter(),					"10.0.0/8",				new IPvxPrefix("10.0.0/8")},
			{new MACAddressConverter(),					"00:11:22:33:44:55", 	new MACAddress("00:11:22:33:44:55")},
			{new ModuleNameConverter(),			 		"unit-module",			new ModuleName("unit-module")},
			{new ElementGroupIdConverter(),	 	 		uuid,					new ElementGroupId(uuid)},
			{new ElementGroupNameConverter(),	 		"unit-group",			new ElementGroupName("unit-group")},
			{new ElementGroupTypeConverter(),	 		"unit-type",			new ElementGroupType("unit-type")},
			{new ServiceNameConverter(),		 		"unit-service",			new ServiceName("unit-service")},
			{new PlatformIdConverter(),			 		uuid,					new PlatformId(uuid)},
			{new PlatformNameConverter(),			 	"unit-platform",		new PlatformName("unit-platform")},
			{new PlatformChipsetNameConverter(),		"unit-chipset",			new PlatformChipsetName("unit-chipset")},
			{new RackNameConverter(),			 		"unit-rack",			new RackName("unit-rack")},
			{new ReleaseNameConverter(),                "unit-release",         new ReleaseName("unit-release")},
		};
		return asList(converters);
	}
	
	private AttributeConverter<Scalar<String>,String> converter;
	private Scalar<String> scalar;
	private String value;
	
	public StringScalarConverterTest(AttributeConverter<Scalar<String>,String> converter,
								     String value,
								     Scalar<String> scalar) {
		this.converter = converter;
		this.value = value;
		this.scalar = scalar;
		
	}
	
	@Test
	public void converter_annotation_present() {
		assertTrue(converter.getClass().isAnnotationPresent(Converter.class));
	}
	
	@Test
	public void convert_empty_string_column_value_to_null() throws Exception {
		assertNull(converter.convertToEntityAttribute(""));
	}
	
	@Test
	public void null_column_value_is_translated_to_null_entity_attribute_value() throws Exception {
		assertNull(converter.convertToEntityAttribute(null));
	}
	
	@Test
	public void convert_string_to_entity_attribute() throws Exception{
		assertEquals(scalar,converter.convertToEntityAttribute(value));
	}
	
	@Test
	public void convert_entity_attribute_to_column() throws Exception {
		assertEquals(value,converter.convertToDatabaseColumn(scalar));
	}
	
	@Test
	public void null_entity_attribute_is_translated_to_null_column_value() throws Exception{
		assertNull(converter.convertToDatabaseColumn(null));
	}
}
