package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.DnsName.dnsName;
import static io.leitstand.inventory.service.DnsRecord.newDnsRecord;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.DnsRecordSetId.randomDnsRecordSetId;
import static io.leitstand.inventory.service.DnsRecordType.dnsRecordType;
import static io.leitstand.inventory.service.DnsZoneElement.newDnsZoneElement;
import static io.leitstand.inventory.service.DnsZoneElements.newDnsZoneElements;
import static io.leitstand.inventory.service.DnsZoneId.randomDnsZoneId;
import static io.leitstand.inventory.service.DnsZoneName.dnsZoneName;
import static io.leitstand.inventory.service.DnsZoneSettings.newDnsZoneSettings;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DnsValueObjectTest {

    private static final DnsZoneId DNS_ZONE_ID = randomDnsZoneId();
    private static final DnsRecordSetId DNS_RECORD_SET_ID = randomDnsRecordSetId();
    private static final DnsZoneName DNS_ZONE_NAME = dnsZoneName(".leitstand.io");
    private static final DnsName DNS_NAME = dnsName("test.leitstand.io");
    private static final String DESCRIPTION = "description";
    private static final DnsRecordType DNS_RECORD_TYPE = dnsRecordType("A");
    
    @Test
    public void create_dns_record() {
        
        DnsRecord dnsRecord = newDnsRecord()
                              .withDisabled(true)
                              .withSetPtr(true)
                              .withRecordValue("10.0.0.1")
                              .build();
        
        assertThat(dnsRecord.getDnsRecordValue(), is("10.0.0.1"));
        assertTrue(dnsRecord.isDisabled());
        assertTrue(dnsRecord.isSetPtr());
    }
    
    
    @Test
    public void create_dns_record_set() {
        DnsRecord dnsRecord = newDnsRecord()
                              .withDisabled(true)
                              .withSetPtr(true)
                              .withRecordValue("10.0.0.1")
                              .build();
        
        DnsRecordSet dnsRecordSet = newDnsRecordSet()
                                    .withDescription(DESCRIPTION)
                                    .withDnsName(DNS_NAME)
                                    .withDnsRecords(asList(dnsRecord))
                                    .withDnsRecordSetId(DNS_RECORD_SET_ID)
                                    .withDnsRecordTimeToLive(500)
                                    .withDnsRecordType(DNS_RECORD_TYPE)
                                    .withDnsZoneId(DNS_ZONE_ID)
                                    .withDnsZoneName(DNS_ZONE_NAME)
                                    .build();
        
        assertThat(dnsRecordSet.getDescription(),is(DESCRIPTION));
        assertThat(dnsRecordSet.getDnsName(),is(DNS_NAME));
        assertThat(dnsRecordSet.getDnsRecordSetId(),is(DNS_RECORD_SET_ID));
        assertThat(dnsRecordSet.getDnsTtl(),is(500));
        assertThat(dnsRecordSet.getDnsType(),is(DNS_RECORD_TYPE));
        assertThat(dnsRecordSet.getDnsZoneId(),is(DNS_ZONE_ID));
        assertThat(dnsRecordSet.getDnsZoneName(),is(DNS_ZONE_NAME));
        assertThat(dnsRecordSet.getDnsRecords(),containsExactly(dnsRecord));
    }
    
    @Test
    public void create_dns_zone_element() {
        DnsRecord dnsRecord = newDnsRecord()
                              .withDisabled(true)
                              .withSetPtr(true)
                              .withRecordValue("10.0.0.1")
                              .build();
        
        DnsRecordSet dnsRecordSet = newDnsRecordSet()
                                    .withDescription(DESCRIPTION)
                                    .withDnsName(DNS_NAME)
                                    .withDnsRecords(asList(dnsRecord))
                                    .withDnsRecordSetId(DNS_RECORD_SET_ID)
                                    .withDnsRecordTimeToLive(500)
                                    .withDnsRecordType(DNS_RECORD_TYPE)
                                    .withDnsZoneId(DNS_ZONE_ID)
                                    .withDnsZoneName(DNS_ZONE_NAME)
                                    .build();
        
        DnsZoneElement element = newDnsZoneElement()
                                 .withDnsRecordSets(asList(dnsRecordSet))
                                 .build();
        
        assertThat(element.getDnsRecordSets(),containsExactly(dnsRecordSet));
    }
    
    
    @Test
    public void create_dns_zone_elements() {
        DnsRecord dnsRecord = newDnsRecord()
                              .withDisabled(true)
                              .withSetPtr(true)
                              .withRecordValue("10.0.0.1")
                              .build();
        
        DnsRecordSet dnsRecordSet = newDnsRecordSet()
                                    .withDescription(DESCRIPTION)
                                    .withDnsName(DNS_NAME)
                                    .withDnsRecords(asList(dnsRecord))
                                    .withDnsRecordSetId(DNS_RECORD_SET_ID)
                                    .withDnsRecordTimeToLive(500)
                                    .withDnsRecordType(DNS_RECORD_TYPE)
                                    .withDnsZoneId(DNS_ZONE_ID)
                                    .withDnsZoneName(DNS_ZONE_NAME)
                                    .build();
        
        DnsZoneElement element = newDnsZoneElement()
                                 .withDnsRecordSets(asList(dnsRecordSet))
                                 .build();
        
        DnsZoneElements elements = newDnsZoneElements()
                                   .withDnsZoneId(DNS_ZONE_ID)
                                   .withDnsZoneName(DNS_ZONE_NAME)
                                   .withDnsZoneConfigType("custom-config")
                                   .withDnsZoneConfig(createObjectBuilder().build()) 
                                   .withDnsEntries(asList(element))
                                   .build();
        
        assertThat(elements.getDnsZoneId() ,is(DNS_ZONE_ID));
        assertThat(elements.getDnsZoneName(),is(DNS_ZONE_NAME));
        assertThat(elements.getDnsZoneConfigType(),is("custom-config"));
        assertThat(elements.getDnsZoneConfig(),is(createObjectBuilder().build()));
        assertThat(elements.getDnsEntries(),containsExactly(element));
    }
    
    @Test
    public void create_dns_zone_settings() {
        DnsZoneSettings settings = newDnsZoneSettings()
                                   .withDescription(DESCRIPTION)
                                   .withDnsZoneConfig(createObjectBuilder().build())
                                   .withDnsZoneConfigType("custom-config")
                                   .withDnsZoneId(DNS_ZONE_ID)
                                   .withDnsZoneName(DNS_ZONE_NAME)
                                   .build();
        
        assertThat(settings.getDescription(),is(DESCRIPTION));
        assertThat(settings.getDnsZoneConfig(),is(createObjectBuilder().build()));
        assertThat(settings.getDnsZoneConfigType(),is("custom-config"));
        assertThat(settings.getDnsZoneId(),is(DNS_ZONE_ID));
        assertThat(settings.getDnsZoneName(),is(DNS_ZONE_NAME));
    }
}
