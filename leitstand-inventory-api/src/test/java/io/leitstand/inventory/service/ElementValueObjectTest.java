package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.DnsName.dnsName;
import static io.leitstand.inventory.service.DnsRecord.newDnsRecord;
import static io.leitstand.inventory.service.DnsRecordSet.newDnsRecordSet;
import static io.leitstand.inventory.service.DnsRecordType.dnsRecordType;
import static io.leitstand.inventory.service.ElementAlias.elementAlias;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.newElementAvailableUpgrade;
import static io.leitstand.inventory.service.ElementAvailableUpgrade.UpgradeType.MAJOR;
import static io.leitstand.inventory.service.ElementCloneRequest.newCloneElementRequest;
import static io.leitstand.inventory.service.ElementDnsRecordSet.newElementDnsRecordSet;
import static io.leitstand.inventory.service.ElementDnsRecordSets.newElementDnsRecordSets;
import static io.leitstand.inventory.service.ElementEnvironment.newElementEnvironment;
import static io.leitstand.inventory.service.ElementEnvironments.newElementEnvironments;
import static io.leitstand.inventory.service.ElementGroupId.randomGroupId;
import static io.leitstand.inventory.service.ElementGroupName.groupName;
import static io.leitstand.inventory.service.ElementGroupType.groupType;
import static io.leitstand.inventory.service.ElementId.randomElementId;
import static io.leitstand.inventory.service.ElementImage.newElementImage;
import static io.leitstand.inventory.service.ElementManagementInterface.newElementManagementInterface;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static io.leitstand.inventory.service.EnvironmentInfo.newEnvironmentInfo;
import static io.leitstand.inventory.service.EnvironmentName.environmentName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.OperationalState.UP;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.net.URI;
import java.util.Date;

import javax.json.JsonObject;

import org.junit.Test;
import org.mockito.Mockito;

public class ElementValueObjectTest {
    
    private static final ElementGroupId     GROUP_ID         = randomGroupId();
    private static final ElementGroupName   GROUP_NAME       = groupName("group");
    private static final ElementGroupType   GROUP_TYPE       = groupType("pod");
    private static final ElementId          ELEMENT_ID       = randomElementId();
    private static final ElementName        ELEMENT_NAME     = elementName("element");
    private static final ElementAlias       ELEMENT_ALIAS    = elementAlias("alias");
    private static final ElementRoleName    ELEMENT_ROLE     = elementRoleName("role");
    private static final String             DESCRIPTION      = "description";
    private static final Date               DATE_MODIFIED    = new Date();
    private static final Date               BUILD_DATE       = new Date();
    private static final PlatformId         PLATFORM_ID      = randomPlatformId();
    private static final PlatformName       PLATFORM_NAME    = platformName("platform");
    private static final MACAddress         MAC_ADDRESS      = macAddress("11:22:33:44:55:66");
    private static final ImageId            IMAGE_ID         = randomImageId();
    private static final ImageName          IMAGE_NAME       = imageName("image");
    private static final EnvironmentId      ENVIRONMENT_ID   = randomEnvironmentId();
    private static final EnvironmentName    ENVIRONMENT_NAME = environmentName("environment");

    
    
    @Test
    public void element_settings() {
        ElementManagementInterface mgmtInterface = newElementManagementInterface()
                                                   .withName("SSH")
                                                   .withProtocol("ssh")
                                                   .withHostname("localhost")
                                                   .withPath("/")
                                                   .withPort(22)
                                                   .build();
        
        ElementSettings element = newElementSettings()
                                  .withAdministrativeState(ACTIVE)
                                  .withAssetId("asset")
                                  .withDateModified(DATE_MODIFIED)
                                  .withDescription(DESCRIPTION)
                                  .withElementAlias(ELEMENT_ALIAS)
                                  .withElementId(ELEMENT_ID)
                                  .withElementName(ELEMENT_NAME)
                                  .withElementRole(ELEMENT_ROLE)
                                  .withGroupId(GROUP_ID)
                                  .withGroupName(GROUP_NAME)
                                  .withGroupType(GROUP_TYPE)
                                  .withManagementInterfaceMacAddress(MAC_ADDRESS)
                                  .withManagementInterfaces(asList(mgmtInterface)
                                                            .stream()
                                                            .collect(toMap(ElementManagementInterface::getName, 
                                                                           identity())))
                                  .withOperationalState(UP)
                                  .withPlane(DATA)
                                  .withPlatformId(PLATFORM_ID)
                                  .withPlatformName(PLATFORM_NAME)
                                  .withSerialNumber("serial")
                                  .withTags("tag1","tag2")
                                  .build();
                                   
        assertThat(element.getAdministrativeState(),is(ACTIVE));
        assertThat(element.getAssetId(),is("asset"));
        assertThat(element.getDateModified(),is(DATE_MODIFIED));
        assertThat(element.getDescription(),is(DESCRIPTION));
        assertThat(element.getElementAlias(),is(ELEMENT_ALIAS));
        assertThat(element.getElementId(),is(ELEMENT_ID));
        assertThat(element.getElementName(),is(ELEMENT_NAME));
        assertThat(element.getElementRole(),is(ELEMENT_ROLE));
        assertThat(element.getGroupId(),is(GROUP_ID));
        assertThat(element.getGroupName(),is(GROUP_NAME));
        assertThat(element.getGroupType(),is(GROUP_TYPE));
        assertThat(element.getManagementInterfaceMacAddress(),is(MAC_ADDRESS));
        assertThat(element.getManagementInterface("SSH"),is(mgmtInterface));
        assertThat(element.getManagementInterfaceUri("SSH"),is(URI.create("ssh://localhost:22/")));
        assertNull(element.getManagementInterface("CTRLD"));
        assertNull(element.getManagementInterfaceUri("CTRLD"));
        assertThat(element.getOperationalState(),is(UP));
        assertThat(element.getPlane(),is(DATA));
        assertThat(element.getPlatformId(),is(PLATFORM_ID));
        assertThat(element.getPlatformName(),is(PLATFORM_NAME));
        assertThat(element.getSerialNumber(),is("serial"));
        assertThat(element.getTags(),containsExactly("tag1","tag2"));
        
    }
    
    @Test
    public void element_available_upgrade() {
        ElementAvailableUpgrade upgrade = newElementAvailableUpgrade()
                                          .withBuildDate(BUILD_DATE)
                                          .withImageId(IMAGE_ID)
                                          .withImageName(IMAGE_NAME)
                                          .withImageState(CANDIDATE)
                                          .withImageVersion(version("2.0.0"))
                                          .withUpgradeType(MAJOR)
                                          .build();
        assertThat(upgrade.getBuildDate(),is(BUILD_DATE));
        assertThat(upgrade.getImageId(),is(IMAGE_ID));
        assertThat(upgrade.getImageName(),is(IMAGE_NAME));
        assertThat(upgrade.getImageState(),is(CANDIDATE));
        assertThat(upgrade.getImageVersion(),is(version("2.0.0")));
        assertThat(upgrade.getUpgradeType(),is(MAJOR));
    }
    
    @Test
    public void element_clone_request() {
        ElementCloneRequest request = newCloneElementRequest()
                                      .withElementAlias(ELEMENT_ALIAS)
                                      .withElementId(ELEMENT_ID)
                                      .withElementName(ELEMENT_NAME)
                                      .withMgmtMacAddress(MAC_ADDRESS)
                                      .withSerialNumber("serial")
                                      .build();
        
        assertThat(request.getElementAlias(),is(ELEMENT_ALIAS));
        assertThat(request.getElementId(),is(ELEMENT_ID));
        assertThat(request.getElementName(),is(ELEMENT_NAME));
        assertThat(request.getMgmtMacAddress(),is(MAC_ADDRESS));
        assertThat(request.getSerialNumber(),is("serial"));
    }
    
    @Test
    public void element_environment() {
        JsonObject vars = createObjectBuilder().build();
        
        ElementEnvironment env = newElementEnvironment()
                                 .withCategory("category")
                                 .withEnvironmentId(ENVIRONMENT_ID)
                                 .withEnvironmentName(ENVIRONMENT_NAME)
                                 .withType("type")
                                 .withVariables(vars)
                                 .build();
        
        assertThat(env.getEnvironmentId(),is(ENVIRONMENT_ID));
        assertThat(env.getEnvironmentName(),is(ENVIRONMENT_NAME));
        assertThat(env.getType(),is("type"));
        assertThat(env.getVariables(),is(vars));
        assertThat(env.getCategory(),is("category"));
    }
    
    @Test
    public void element_environments() {
        EnvironmentInfo env = newEnvironmentInfo()
                              .withCategory("category")
                              .withEnvironmentId(ENVIRONMENT_ID)
                              .withEnvironmentName(ENVIRONMENT_NAME)
                              .withDescription(DESCRIPTION)
                              .withType("type")
                              .build();    
        
        ElementEnvironments envs = newElementEnvironments()
                                   .withEnvironments(asList(env))
                                   .build();
        
        assertThat(envs.getEnvironments(),
                   containsExactly(env));
    }
    
    
    @Test
    public void element_environment_info() {
        EnvironmentInfo env = newEnvironmentInfo()
                              .withCategory("category")
                              .withEnvironmentId(ENVIRONMENT_ID)
                              .withEnvironmentName(ENVIRONMENT_NAME)
                              .withDescription(DESCRIPTION)
                              .withType("type")
                              .build();    
        
        assertThat(env.getEnvironmentId(),is(ENVIRONMENT_ID));
        assertThat(env.getEnvironmentName(),is(ENVIRONMENT_NAME));
        assertThat(env.getDescription(),is(DESCRIPTION));
        assertThat(env.getType(),is("type"));
        assertThat(env.getCategory(),is("category"));
    }
    
    @Test
    public void element_dns_record_set() {
        DnsRecordSet records = newDnsRecordSet()
                               .withDnsName(dnsName("test.leitstand.io"))
                               .withDnsRecordType(dnsRecordType("CNAME"))
                               .withDnsRecords(newDnsRecord()
                                               .withDisabled(true)
                                               .withSetPtr(true)
                                               .withRecordValue("alias.leitstand.io"))
                               .build();
       
        ElementDnsRecordSet dnsRecord = newElementDnsRecordSet()
                                        .withDnsRecordSet(records)
                                        .build();
        
        assertThat(dnsRecord.getDnsRecordSet(),is(records));
    }
    
    @Test
    public void element_dns_record_sets() {
        DnsRecordSet records = newDnsRecordSet()
                               .withDnsName(dnsName("test.leitstand.io"))
                               .withDnsRecordType(dnsRecordType("CNAME"))
                               .withDnsRecords(newDnsRecord()
                                               .withDisabled(true)
                                               .withSetPtr(true)
                                               .withRecordValue("alias.leitstand.io"))
                               .build();
       
        ElementDnsRecordSets dnsRecords = newElementDnsRecordSets()
                                          .withDnsRecordSets(asList(records))
                                          .build();
        
        assertThat(dnsRecords.getDnsRecordSets(),containsExactly(records));
    }
    
    @Test
    public void element_image() {
        ElementImageData image = Mockito.mock(ElementImageData.class);
        
        ElementImage elementImage = newElementImage()
                                    .withImage(image)
                                    .build();
        
        assertThat(elementImage.getImage(),is(image));
    }
    
}
