package io.leitstand.inventory.service;

import static io.leitstand.inventory.service.AddressInterface.newAddressInterface;
import static io.leitstand.inventory.service.AdministrativeState.ACTIVE;
import static io.leitstand.inventory.service.AdministrativeState.UP;
import static io.leitstand.inventory.service.Bandwidth.bandwidth;
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
import static io.leitstand.inventory.service.ElementImageData.newElementImageData;
import static io.leitstand.inventory.service.ElementImageReference.newElementImageReference;
import static io.leitstand.inventory.service.ElementImages.newElementImages;
import static io.leitstand.inventory.service.ElementLinkData.newElementLinkData;
import static io.leitstand.inventory.service.ElementLinks.newElementLinks;
import static io.leitstand.inventory.service.ElementLogicalInterface.newLogicalInterface;
import static io.leitstand.inventory.service.ElementLogicalInterfaceData.newElementLogicalInterfaceData;
import static io.leitstand.inventory.service.ElementLogicalInterfaceSubmission.newElementLogicalInterfaceSubmission;
import static io.leitstand.inventory.service.ElementLogicalInterfaces.newLogicalInterfaces;
import static io.leitstand.inventory.service.ElementManagementInterface.newElementManagementInterface;
import static io.leitstand.inventory.service.ElementModule.newElementModule;
import static io.leitstand.inventory.service.ElementModules.newElementModules;
import static io.leitstand.inventory.service.ElementName.elementName;
import static io.leitstand.inventory.service.ElementPhysicalInterface.newPhysicalInterface;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceData.newPhysicalInterfaceData;
import static io.leitstand.inventory.service.ElementPhysicalInterfaceSubmission.newPhysicalInterfaceSubmission;
import static io.leitstand.inventory.service.ElementPhysicalInterfaces.newPhysicalInterfaces;
import static io.leitstand.inventory.service.ElementRoleName.elementRoleName;
import static io.leitstand.inventory.service.ElementSettings.newElementSettings;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;
import static io.leitstand.inventory.service.EnvironmentInfo.newEnvironmentInfo;
import static io.leitstand.inventory.service.EnvironmentName.environmentName;
import static io.leitstand.inventory.service.ImageId.randomImageId;
import static io.leitstand.inventory.service.ImageName.imageName;
import static io.leitstand.inventory.service.ImageState.CANDIDATE;
import static io.leitstand.inventory.service.ImageState.RELEASE;
import static io.leitstand.inventory.service.ImageType.imageType;
import static io.leitstand.inventory.service.InterfaceName.interfaceName;
import static io.leitstand.inventory.service.MACAddress.macAddress;
import static io.leitstand.inventory.service.ModuleData.newModuleData;
import static io.leitstand.inventory.service.ModuleName.moduleName;
import static io.leitstand.inventory.service.OperationalState.DOWN;
import static io.leitstand.inventory.service.Plane.DATA;
import static io.leitstand.inventory.service.PlatformId.randomPlatformId;
import static io.leitstand.inventory.service.PlatformName.platformName;
import static io.leitstand.inventory.service.RoutingInstanceName.routingInstance;
import static io.leitstand.inventory.service.Version.version;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.containsExactly;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static javax.json.Json.createObjectBuilder;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.json.JsonObject;

import org.junit.Test;

public class ElementValueObjectTest {
    
    private static final String              ALIAS             = "alias";
    private static final String              CATEGORY          = "category";
    private static final InterfaceName       IFP_NAME          = interfaceName("ifp-0/0/1");
    private static final InterfaceName       IFL_NAME          = interfaceName("ifl-0/0/1/1/1");
    private static final InterfaceName       IFC_NAME          = interfaceName("ifc-0/0/1/1");
    private static final String              VENDOR_TYPE       = "vendor-type";
    private static final String              SOFTWARE_REV      = "software-rev";
    private static final String              SERIALNUMBER      = "serial";
    private static final ModuleName          MODULE_NAME       = moduleName("module");
    private static final ModuleName          PARENT_MODULE     = moduleName("parent_module");
    private static final String              MODULE_CLASS      = "module-class";
    private static final String              MODEL_NAME        = "model-name";
    private static final String              MANUFACTURER_NAME = "manufacturer";
    private static final String              LOCATION          = "location";
    private static final String              HARDWARE_REV      = "hardware-rev";
    private static final String              FIRMWARE_REV      = "firmware-rev";
    private static final ElementGroupId      GROUP_ID          = randomGroupId();
    private static final ElementGroupName    GROUP_NAME        = groupName("group");
    private static final ElementGroupType    GROUP_TYPE        = groupType("pod");
    private static final ElementId           ELEMENT_ID        = randomElementId();
    private static final ElementName         ELEMENT_NAME      = elementName("element");
    private static final ElementAlias        ELEMENT_ALIAS     = elementAlias(ALIAS);
    private static final ElementRoleName     ELEMENT_ROLE      = elementRoleName("role");
    private static final String              DESCRIPTION       = "description";
    private static final Date                DATE_MODIFIED     = new Date();
    private static final Date                BUILD_DATE        = new Date();
    private static final PlatformId          PLATFORM_ID       = randomPlatformId();
    private static final PlatformName        PLATFORM_NAME     = platformName("platform");
    private static final MACAddress          MAC_ADDRESS       = macAddress("11:22:33:44:55:66");
    private static final ImageId             IMAGE_ID          = randomImageId();
    private static final ImageName           IMAGE_NAME        = imageName("image");
    private static final ImageType           IMAGE_TYPE        = imageType("image-type");
    private static final EnvironmentId       ENVIRONMENT_ID    = randomEnvironmentId();
    private static final EnvironmentName     ENVIRONMENT_NAME  = environmentName("environment");
    private static final String              ASSET_ID          = "asset-id";
    private static final Date                DATE_MANUFACTURED = new Date();
    private static final RoutingInstanceName INSTANCE_NAME     = routingInstance("instance");

    
    
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
                                  .withOperationalState(DOWN)
                                  .withPlane(DATA)
                                  .withPlatformId(PLATFORM_ID)
                                  .withPlatformName(PLATFORM_NAME)
                                  .withSerialNumber(SERIALNUMBER)
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
        assertThat(element.getOperationalState(),is(DOWN));
        assertThat(element.getPlane(),is(DATA));
        assertThat(element.getPlatformId(),is(PLATFORM_ID));
        assertThat(element.getPlatformName(),is(PLATFORM_NAME));
        assertThat(element.getSerialNumber(),is(SERIALNUMBER));
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
                                      .withSerialNumber(SERIALNUMBER)
                                      .build();
        
        assertThat(request.getElementAlias(),is(ELEMENT_ALIAS));
        assertThat(request.getElementId(),is(ELEMENT_ID));
        assertThat(request.getElementName(),is(ELEMENT_NAME));
        assertThat(request.getMgmtMacAddress(),is(MAC_ADDRESS));
        assertThat(request.getSerialNumber(),is(SERIALNUMBER));
    }
    
    @Test
    public void element_environment() {
        JsonObject vars = createObjectBuilder().build();
        
        ElementEnvironment env = newElementEnvironment()
                                 .withCategory(CATEGORY)
                                 .withEnvironmentId(ENVIRONMENT_ID)
                                 .withEnvironmentName(ENVIRONMENT_NAME)
                                 .withType("type")
                                 .withVariables(vars)
                                 .build();
        
        assertThat(env.getEnvironmentId(),is(ENVIRONMENT_ID));
        assertThat(env.getEnvironmentName(),is(ENVIRONMENT_NAME));
        assertThat(env.getType(),is("type"));
        assertThat(env.getVariables(),is(vars));
        assertThat(env.getCategory(),is(CATEGORY));
    }
    
    @Test
    public void element_environments() {
        EnvironmentInfo env = newEnvironmentInfo()
                              .withCategory(CATEGORY)
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
                              .withCategory(CATEGORY)
                              .withEnvironmentId(ENVIRONMENT_ID)
                              .withEnvironmentName(ENVIRONMENT_NAME)
                              .withDescription(DESCRIPTION)
                              .withType("type")
                              .build();    
        
        assertThat(env.getEnvironmentId(),is(ENVIRONMENT_ID));
        assertThat(env.getEnvironmentName(),is(ENVIRONMENT_NAME));
        assertThat(env.getDescription(),is(DESCRIPTION));
        assertThat(env.getType(),is("type"));
        assertThat(env.getCategory(),is(CATEGORY));
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
        ElementImageData image = mock(ElementImageData.class);
        
        ElementImage elementImage = newElementImage()
                                    .withImage(image)
                                    .build();
        
        assertThat(elementImage.getImage(),is(image));
    }
    
    @Test
    public void element_image_data() {
        
        PackageVersionInfo packageInfo = mock(PackageVersionInfo.class);
        
        ElementAvailableUpgrade upgrade = mock(ElementAvailableUpgrade.class);
        
        Map<String,String> checksums = new HashMap<>();
        checksums.put("MD5","f6622021aae19fd282df4945a3a47aa8");
        
        ElementImageData image = newElementImageData()
                                 .withAvailableUpgrades(upgrade)
                                 .withBuildDate(BUILD_DATE)
                                 .withChecksums(checksums)
                                 .withElementImageState(ElementImageState.ACTIVE)
                                 .withImageExtension("gz")
                                 .withImageId(IMAGE_ID)
                                 .withImageName(IMAGE_NAME)
                                 .withImageState(RELEASE)
                                 .withImageType(IMAGE_TYPE)
                                 .withImageVersion(version("1.1.0"))
                                 .withInstallationDate(DATE_MODIFIED)
                                 .withOrganization("leitstand.io")
                                 .withPackages(packageInfo)
                                 .withZtp(true)
                                 .build();
        
        assertThat(image.getAvailableUpgrades(),containsExactly(upgrade));
        assertThat(image.getBuildDate(),is(BUILD_DATE));
        assertThat(image.getChecksums(),is(checksums));
        assertThat(image.getElementImageState(),is(ElementImageState.ACTIVE));
        assertThat(image.getImageExtension(),is("gz"));
        assertThat(image.getImageId(),is(IMAGE_ID));
        assertThat(image.getImageName(),is(IMAGE_NAME));
        assertThat(image.getImageType(),is(IMAGE_TYPE));
        assertThat(image.getImageVersion(),is(version("1.1.0")));
        assertThat(image.getInstallationDate(),is(DATE_MODIFIED));
        assertThat(image.getOrganization(),is("leitstand.io"));
        assertThat(image.getPackages(),containsExactly(packageInfo));
        assertTrue(image.isZtp());
        assertTrue(image.isActive());
        
    }
    
    @Test
    public void element_image_reference() {
        
        ElementImageReference image = newElementImageReference()
                                      .withElementImageState(ElementImageState.ACTIVE)
                                      .withImageId(IMAGE_ID)
                                      .withImageName(IMAGE_NAME)
                                      .withImageType(IMAGE_TYPE)
                                      .withImageVersion(version("1.1.0"))
                                      .build();
        
        assertThat(image.getElementImageState(),is(ElementImageState.ACTIVE));
        assertThat(image.getImageId(),is(IMAGE_ID));
        assertThat(image.getImageName(),is(IMAGE_NAME));
        assertThat(image.getImageType(),is(IMAGE_TYPE));
        assertThat(image.getImageVersion(),is(version("1.1.0")));
        assertTrue(image.isActive());
        
    }
    
    @Test
    public void element_images() {
        
        ElementImageData image = mock(ElementImageData.class);
        ElementImages images = newElementImages()
                               .withElementImages(image)
                               .build();
        
        assertThat(images.getImages(),containsExactly(image));
    }
    
    @Test
    public void element_link_data() {
        
        ElementLinkData link = newElementLinkData()
                               .withLocalAdministrativeState(AdministrativeState.UP)
                               .withLocalBandwidth(bandwidth("100.000 Gbps"))
                               .withLocalIfpName(IFP_NAME)
                               .withLocalMac(MAC_ADDRESS)
                               .withLocalOperationalState(OperationalState.UP)
                               .withRemoteAdministrativeState(AdministrativeState.DOWN)
                               .withRemoteBandwidth(bandwidth("10.000 Gbps"))
                               .withRemoteElementGroupId(GROUP_ID)
                               .withRemoteElementGroupType(GROUP_TYPE)
                               .withRemoteElementGroupName(GROUP_NAME)
                               .withRemoteElementId(ELEMENT_ID)
                               .withRemoteElementName(ELEMENT_NAME)
                               .withRemoteMac(macAddress("01:23:45:67:89:AB"))
                               .withRemoteOperationalState(OperationalState.DOWN)
                               .withRemoteIfpName(interfaceName("ifp-0/0/2"))
                               .build();
                               
        assertThat(link.getLocalAdministrativeState(),is(AdministrativeState.UP));
        assertThat(link.getLocalBandwidth(),is(bandwidth(100f, "Gbps")));
        assertThat(link.getLocalIfpName(),is(IFP_NAME));
        assertThat(link.getLocalMac(),is(MAC_ADDRESS));
        assertThat(link.getLocalOperationalState(),is(OperationalState.UP));
        assertThat(link.getNeighborAdministrativeState(),is(AdministrativeState.DOWN));
        assertThat(link.getNeighborBandwidth(),is(bandwidth(10f,"Gbps")));
        assertThat(link.getNeighborElementGroupId(),is(GROUP_ID));
        assertThat(link.getNeighborElementGroupType(),is(GROUP_TYPE));
        assertThat(link.getNeighborElementGroupName(),is(GROUP_NAME));
        assertThat(link.getNeighborElementId(),is(ELEMENT_ID));
        assertThat(link.getNeighborElementName(),is(ELEMENT_NAME));
        assertThat(link.getNeighborMac(),is(macAddress("01:23:45:67:89:AB")));
        assertThat(link.getNeighborOperationalState(),is(OperationalState.DOWN));
        assertThat(link.getNeighborIfpName(),is(interfaceName("ifp-0/0/2")));
        
    }
    
    @Test
    public void element_links_data() {
        ElementLinkData link = mock(ElementLinkData.class);
        ElementLinks links = newElementLinks()
                             .withLinks(link)
                             .build();
        assertThat(links.getLinks(),containsExactly(link));
        
    }
    
    @Test
    public void create_element_modules(){
        ModuleData moduleData = mock(ModuleData.class);
        ElementModules modules = newElementModules()
                                 .withModules(asList(moduleData))
                                 .build();
        
        assertThat(modules.getModules(),containsExactly(moduleData));
    }
    
    @Test
    public void create_element_module() {
        ModuleData moduleData = mock(ModuleData.class);
        ElementModule module = newElementModule()
                               .withModule(moduleData)
                               .build();
        
        assertThat(module.getModule(),is(moduleData));
        
    }
    
    @Test
    public void create_module_data() {
        ModuleData module = newModuleData()
                            .withAdministrativeState(ACTIVE)
                            .withAssetId(ASSET_ID)
                            .withDescription(DESCRIPTION)
                            .withFieldReplaceableUnit(true)
                            .withFirmwareRevision(FIRMWARE_REV)
                            .withHardwareRevision(HARDWARE_REV)
                            .withLocation(LOCATION)
                            .withManufacturerName(MANUFACTURER_NAME)
                            .withManufacturingDate(DATE_MANUFACTURED)
                            .withModelName(MODEL_NAME)
                            .withModuleClass(MODULE_CLASS)
                            .withModuleName(MODULE_NAME)
                            .withParentModule(PARENT_MODULE)
                            .withSerialNumber(SERIALNUMBER)
                            .withSoftwareRevision(SOFTWARE_REV)
                            .withVendorType(VENDOR_TYPE)
                            .build();
        
        assertThat(module.getAdministrativeState(),is(ACTIVE));
        assertThat(module.getAssetId(),is(ASSET_ID));
        assertThat(module.getDateManufactured(),is(DATE_MANUFACTURED));
        assertThat(module.getDescription(),is(DESCRIPTION));
        assertThat(module.getFirmwareRevision(),is(FIRMWARE_REV));
        assertThat(module.getHardwareRevision(),is(HARDWARE_REV));
        assertThat(module.getLocation(),is(LOCATION));
        assertThat(module.getManufacturerName(),is(MANUFACTURER_NAME));
        assertThat(module.getModelName(),is(MODEL_NAME));
        assertThat(module.getModuleClass(),is(MODULE_CLASS));
        assertThat(module.getModuleName(),is(MODULE_NAME));
        assertThat(module.getParentModule(),is(PARENT_MODULE));
        assertThat(module.getSerialNumber(),is(SERIALNUMBER));
        assertThat(module.getSoftwareRevision(),is(SOFTWARE_REV));
        assertThat(module.getVendorType(),is(VENDOR_TYPE));
        
    }
    
    @Test
    public void create_element_ifp_neighbor() {
        ElementPhysicalInterfaceNeighbor neighbor = ElementPhysicalInterfaceNeighbor.newPhysicalInterfaceNeighbor()
                                                    .withElementId(ELEMENT_ID)
                                                    .withElementName(ELEMENT_NAME)
                                                    .withInterfaceName(IFP_NAME)
                                                    .build();
        assertThat(neighbor.getElementId(),is(ELEMENT_ID));
        assertThat(neighbor.getElementName(),is(ELEMENT_NAME));
        assertThat(neighbor.getInterfaceName(),is(IFP_NAME));
    }
    
    @Test
    public void create_element_physical_interfaces() {
        ElementPhysicalInterfaceData ifpData = mock(ElementPhysicalInterfaceData.class);
        ElementPhysicalInterfaces ifps = newPhysicalInterfaces()
                                         .withPhysicalInterface(ifpData)
                                         .build();
        
        assertThat(ifps.getPhysicalInterfaces(),containsExactly(ifpData));
    }
    
    @Test
    public void create_element_physical_interface() {
        ElementPhysicalInterfaceData ifpData = mock(ElementPhysicalInterfaceData.class);
        ElementPhysicalInterface ifp = newPhysicalInterface()
                                       .withPhysicalInterface(ifpData)
                                       .build();
        
        assertThat(ifp.getPhysicalInterface(),is(ifpData));
    }
    
    @Test
    public void create_physical_interface_data() {
        ElementPhysicalInterfaceNeighbor neighbor = mock(ElementPhysicalInterfaceNeighbor.class);
        Bandwidth bandwidth = mock(Bandwidth.class);
        ElementPhysicalInterfaceData ifp = newPhysicalInterfaceData()
                                           .withAdministrativeState(UP)
                                           .withBandwidth(bandwidth)
                                           .withCategory(CATEGORY)
                                           .withIfpAlias(ALIAS)
                                           .withIfpName(IFP_NAME)
                                           .withMacAddress(MAC_ADDRESS)
                                           .withNeighbor(neighbor)
                                           .withOperationalState(DOWN)
                                           .build();
        
        assertThat(ifp.getAdministrativeState(),is(UP));
        assertThat(ifp.getBandwidth(),is(bandwidth));
        assertThat(ifp.getCategory(),is(CATEGORY));
        assertThat(ifp.getIfpAlias(),is(ALIAS));
        assertThat(ifp.getIfpName(),is(IFP_NAME));
        assertThat(ifp.getMacAddress(),is(MAC_ADDRESS));
        assertThat(ifp.getNeighbor(),is(neighbor));
        assertThat(ifp.getOperationalState(),is(DOWN));
    }
    
    @Test
    public void create_address_interface() {
        IPvxPrefix prefix = mock(IPvxPrefix.class);
        AddressInterface ifa = newAddressInterface()
                               .withAddress(prefix)
                               .build();
        
        assertThat(ifa.getAddress(),is(prefix));
    }
    
    @Test
    public void create_element_logical_interfaces() {
        ElementLogicalInterfaceData iflData = mock(ElementLogicalInterfaceData.class);
        ElementLogicalInterfaces ifls = newLogicalInterfaces()
                                        .withLogicalInterfaces(asList(iflData))
                                        .build();
        assertThat(ifls.getLogicalInterfaces(),containsExactly(iflData));
    }
    
    @Test
    public void create_element_logical_interface() {
        ElementLogicalInterfaceData iflData = mock(ElementLogicalInterfaceData.class);
        ElementLogicalInterface ifl = newLogicalInterface()
                                      .withLogicalInterface(iflData)
                                      .build();
        assertThat(ifl.getLogicalInterface(),is(iflData));        
    }
    
    @Test
    public void create_vlan_tag() {
        VlanID vlanId = new VlanID(1234);
        VlanTPID vlanTpid = new VlanTPID(8100);
        VlanTag tag = VlanTag.newVlanTag()
                             .withVlanId(vlanId)
                             .withVlanTpid(vlanTpid)
                             .build();
        assertThat(tag.getVlanId(),is(vlanId));
        assertThat(tag.getVlanTpid(),is(vlanTpid));
    }
    
    @Test
    public void create_logical_interface_data() {
        VlanTag vlan = mock(VlanTag.class);
        AddressInterface ifa = mock(AddressInterface.class);
        PhysicalInterface ifp = mock(PhysicalInterface.class);
        ElementLogicalInterfaceData ifl = newElementLogicalInterfaceData()
                                          .withAddressInterfaces(ifa)
                                          .withAdministrativeState(UP)
                                          .withInterfaceAlias(ALIAS)
                                          .withInterfaceName(IFL_NAME)
                                          .withOperationalState(DOWN)
                                          .withPhysicalInterfaces(ifp)
                                          .withRoutingInstance(INSTANCE_NAME)
                                          .withVlans(asList(vlan))
                                          .build();
        
        assertThat(ifl.getAddresses(),containsExactly(ifa));
        assertThat(ifl.getAdministrativeState(),is(UP));
        assertThat(ifl.getIflAlias(),is(ALIAS));
        assertThat(ifl.getIflName(),is(IFL_NAME));
        assertThat(ifl.getOperationalState(),is(DOWN));
        assertThat(ifl.getPhysicalInterfaces(),containsExactly(ifp));
        assertThat(ifl.getRoutingInstance(),is(INSTANCE_NAME));
        assertThat(ifl.getVlans(),containsExactly(vlan));
    }
    
    @Test
    public void create_physical_interface_submission() {
        ElementPhysicalInterfaceNeighbor neighbor = mock(ElementPhysicalInterfaceNeighbor.class);
        Bandwidth bandwidth = mock(Bandwidth.class);
        ElementPhysicalInterfaceSubmission submission = newPhysicalInterfaceSubmission()
                                                        .withAdministrativeState(UP)
                                                        .withBandwidth(bandwidth)
                                                        .withCategory(CATEGORY)
                                                        .withIfcName(IFC_NAME)
                                                        .withIfpAlias(ALIAS)
                                                        .withIfpName(IFP_NAME)
                                                        .withMacAddress(MAC_ADDRESS)
                                                        .withNeighbor(neighbor)
                                                        .withOperationalState(DOWN)
                                                        .build();
        
        assertThat(submission.getAdministrativeState(),is(UP));
        assertThat(submission.getBandwidth(),is(bandwidth));
        assertThat(submission.getCategory(),is(CATEGORY));
        assertThat(submission.getIfcName(),is(IFC_NAME));
        assertThat(submission.getIfpAlias(),is(ALIAS));
        assertThat(submission.getIfpName(),is(IFP_NAME));
        assertThat(submission.getMacAddress(),is(MAC_ADDRESS));
        assertThat(submission.getNeighbor(),is(neighbor));
        assertThat(submission.getOperationalState(),is(DOWN));
    }
    
    @Test
    public void create_logical_interface_submission() {
        VlanTag vlan = mock(VlanTag.class);
        AddressInterface ifa = mock(AddressInterface.class);
        ElementLogicalInterfaceSubmission ifl = newElementLogicalInterfaceSubmission()
                                                .withAddressInterfaces(asList(ifa))
                                                .withAdministrativeState(UP)
                                                .withIfcName(IFC_NAME)
                                                .withIflName(IFL_NAME)
                                                .withInterfaceAlias(ALIAS)
                                                .withOperationalState(DOWN)
                                                .withRoutingInstanceName(INSTANCE_NAME)
                                                .withVlans(asList(vlan))
                                                .build();
        
        assertThat(ifl.getAddresses(),containsExactly(ifa));
        assertThat(ifl.getAdministrativeState(),is(UP));
        assertThat(ifl.getIfcName(),is(IFC_NAME));
        assertThat(ifl.getIflName(),is(IFL_NAME));
        assertThat(ifl.getInterfaceAlias(),is(ALIAS));
        assertThat(ifl.getOperationalState(),is(DOWN));
        assertThat(ifl.getRoutingInstance(),is(INSTANCE_NAME));
        assertThat(ifl.getVlans(),containsExactly(vlan));
                                                
    }
    
}
