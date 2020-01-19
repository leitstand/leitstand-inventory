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
import {Json} from '/ui/js/client.js';
import {Controller,Menu} from '/ui/js/ui.js';
import {Select} from '/ui/js/ui-components.js';
import {Element} from '/ui/modules/inventory/inventory.js';
import {DnsZones,DnsZone} from '/ui/modules/inventory/dns/dns.js';

const DNS_TYPES = [{"label":"IPv4 address (A)","value":"A"},
				   {"label":"IPv6 address (AAAA)","value":"AAAA"},
				   {"label":"Alias (CNAME)","value":"CNAME"}];


let zonesController = function(){
	let zones = new DnsZones();
	return new Controller({resource:zones,
						   viewModel:function(zones){
							   return {"zones":zones,
								   	   "filter":this.location().param("filter")};
						   },
						   buttons:{
							   "add-zone":function(){
								   zones.addZone(this.getViewModel("zone"));
							   }
						   },
						   onSuccess:function(){
							   this.navigate("zones.html");
						   }
							});
}

let zoneSettingsController = function(){
	let zone = new DnsZone({"scope":"settings"});
	return new Controller({resource:zone,
						   buttons:{
							   "save-settings":function(){
								   zone.storeSettings(this.location().params(),
										   			  this.getViewModel());	
							   },
							   "confirm-remove":function(){
								   let params = this.location().params();
								   params.force = this.input("force").isChecked();
								   zone.removeZone(params);
							   }
						   },
						   onSuccess:function(){
							   this.navigate("zones.html");
						   }});
}

let zoneElementsController = function(){
	let zone = new DnsZone({"scope":"elements"});
	return new Controller({resource:zone});
}

let elementDnsRecordSetsController = function(){
	let element = new Element({"scope":"dns"});
	return new Controller({resource:element});
};

let addElementDnsRecordSetController = function(){
	let element = new Element({"scope":"dns"});
	return new Controller({resource:element,
						   viewModel: async function(element){
							   element.dns_types = DNS_TYPES;
							   // Lookup all zones to prepare a select list.
							   let zones = new DnsZones();
							   element.zones = await zones.load(this.location().params());
							   element.zones = element.zones.map(zone => ({"label":zone.dns_zone_name,"value":zone.dns_zone_name}));
							   return element;
						   },
						   buttons:{
							   "save-settings":function(){
								   let dns = this.getViewModel("dns");
								   dns.dns_records=[
									   {"dns_value": dns.dns_value,
									    "disabled":dns.disabled}
								   ];
								   element.saveDnsRecord(this.location().params(),dns);
							   }
						   },
						   onSuccess:function(){
							   this.navigate({"view":"element-dns-records.html",
								   			  "?":this.location().params()})
						   }
	});
};

let elementDnsRecordSetController = function(){
	let element = new Element({"scope":"dns/{{record}}"});
	return new Controller({resource:element,
						   viewModel: async function(element){
							   element.dns_types = DNS_TYPES;
							   // Lookup all zones to prepare a select list.
							   let zones = new DnsZones();
							   element.zones = await zones.load(this.location().params());
							   element.zones = element.zones.map(zone => ({"label":zone.dns_zone_name,"value":zone.dns_zone_name}));
							   let record = element.dns_recordset.dns_records[0];
							   element.dns_recordset.dns_value = record.dns_value;
							   element.dns_recordset.disabled = record.disabled;
							   delete element.dns_recordset.dns_records;
							   return element;
						   },
						   buttons:{
							   "save-settings":function(){
								   let dns = this.getViewModel("dns_recordset");
								   dns.dns_records=[
									   {"dns_value": dns.dns_value,
									    "disabled":dns.disabled}
								   ];
								   element.saveSettings(this.location().params(),dns);
							   },
							   "confirm-remove":function(){
								   element.remove(this.location().params());
							   }
						   },
						   onSuccess:function(){
							   this.navigate({"view":"element-dns-records.html",
								   			  "?":this.location().params()})
						   }
	});
};
	
let zonesView = {
		"master":zonesController(),
		"details":{
			"add-zone.html":zonesController()
		}
};

let zoneSettingsView = {
		"master":zoneSettingsController(),
		"details":{
			"confirm-remove-zone.html":zoneSettingsController()
		}
};

let elementDnsView = {
		"master":elementDnsRecordSetsController(),
		"details":{
			"element-dns-record.html":elementDnsRecordSetController(),
			"confirm-remove-dns-record.html":elementDnsRecordSetController(),
			"add-element-dns-record.html":addElementDnsRecordSetController()
		}
		
};

export const menu = new Menu({
		"zones.html" : zonesView,
		"zone-settings.html":zoneSettingsView,
		"zone-elements.html":zoneElementsController(),
		"element-dns-records.html":elementDnsView
 	});
