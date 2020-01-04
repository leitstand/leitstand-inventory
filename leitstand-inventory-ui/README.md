# Leitstand Inventory User Interface

The Leistand Inventory User Interface contributes inventory management views to the Leitstand User Interface.

## Views
The following views exists:

| View | Description |
|:-----|:------------|
| Element Role List | Lists all existing element roles. Provides means to add new roles and view role details. |
| Element Role Settings | Manage role settings, including the role name and role description. Provides means to remove a role if no elements for that role exists. |
| Pod List | Lists all pods. The list can be filtered by the pod name and pod tags (regular expression). Provides means to add a new pod. |
| Pod Settings | Manage pod settings. Allows to remove empty pods. |
| Pod Elements | Lists all elements of a pod. Allows to add a new element. |
| Pod Location | Manage the location of a pod. |
| Pod Racks | Manage the racks that exist per pod|
| Element List | Lists all elements. The list can be filtered by the element name (regular expression). |
| Element Settings | Manage general element settings, including the element name, element alias, element role, hardware platform, description, tags, serial number, MAC address, management interfaces (e.g. REST API endpoint), operational and administrative states. Allows to remove inactive elements. |
| Element Service List | Lists the services that run on an element to manage an element and the transport network. Neither subscriber nor enterprise services are listed in this view. |
| Element Service Details | Displays service detail information. |
| Element Physical Interfaces | Lists all discovered physical interfaces including their name, bandwidth, administrative state, link state and discovered neighbor (LLDP). The view is augmented with real-time interface utilization if a TSDB connector is installed.|
| Element Physical Interface Details | Displays physical interface details including the logical interfaces defined on the physical interface.|
| Element Logical Interface Details | Display logical interface details. |
| Element Configuration List | Lists all known element configurations. |
| Element Configuration Revision List | List the revision history of a element configuration. |
| Element Configuration Details | Displays the configuration including configuration metadata. |
| Element Configuration Diff | Allows to compare two configuration revision with each other. |
| Element Hardware Modules | Lists all known hardware modules. |
| Element Hardware Module Details | Displays hardware module details. |
| Element Image List | Lists all images that are installed on the element, including whether the image is currently active or not and whether an upgrade is available. |
| Element DNS Records | Lists all DNS records defined for the element. Allows to add new DNS records.|
| Element DNS Record Settings | Allows to manage the DNS record. Provides means to remove the DNS record. |
| Image List | Lists all known images. The list can be filtered by image name, element role, image revision and image state. |
| Image Details | Lists all details of an image including the image name, image revision, image hashes and package revisions shipped with the image.|
| Image Utilization | Displays a summary statistic on how many elements, grouped by pod, an image is installed. |
| DNS Zones | Lists all known DNS zones. |
| DNS Zone Settings | Manage DNS zone settings.|

