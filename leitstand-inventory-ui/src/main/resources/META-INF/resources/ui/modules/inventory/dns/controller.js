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

const elementDnsRecordSetsController = function(){
	const element = new Element({"scope":"dns"});
	return new Controller({resource:element});
};

const addElementDnsRecordSetController = function(){
	const element = new Element({"scope":"dns"});
	return new Controller({resource:element,
						   viewModel: function(element){
							   return {};
						   },
						   buttons:{
							   "save-settings":function(){
								   const dns = this.getViewModel("dns");
								   const zoneName = this.input("dns-zones").unwrap().selected.label;
								   dns.dns_zone_name = zoneName;
								   dns.dns_records=[
									   {"dns_value": dns.dns_value,
									    "disabled":dns.disabled}
								   ];
								   element.saveDnsRecord(this.location.params,dns);
							   }
						   },
						   onSuccess:function(){
							   this.navigate({"view":"element-dns-records.html",
								   			  "?":this.location.params})
						   }
	});
};

const elementDnsRecordSetController = function(){
	const element = new Element({"scope":"dns/{{record}}"});
	return new Controller({resource:element,
						   viewModel: async function(element){
							   const record = element.dns_recordset.dns_records[0];
							   element.dns_recordset.dns_value = record.dns_value;
							   element.dns_recordset.disabled = record.disabled;
							   delete element.dns_recordset.dns_records;
							   return element;
						   },
						   buttons:{
							   "save-settings":function(){
								   const dns = this.getViewModel("dns_recordset");
								   const zoneName = this.input("dns-zones").unwrap().selected.label;
								   dns.dns_zone_name = zoneName;
								   dns.dns_records=[
									   {"dns_value": dns.dns_value,
									    "disabled":dns.disabled}
								   ];
								   element.saveSettings(this.location.params,dns);
							   },
							   "confirm-remove":function(){
								   element.remove(this.location.params);
							   }
						   },
						   onSuccess:function(){
							   this.navigate({"view":"element-dns-records.html",
								   			  "?":this.location.params})
						   }
	});
};
	

const elementDnsView = {
		"master":elementDnsRecordSetsController(),
		"details":{
			"element-dns-record.html":elementDnsRecordSetController(),
			"confirm-remove-dns-record.html":elementDnsRecordSetController(),
			"add-element-dns-record.html":addElementDnsRecordSetController()
		}
		
};

export const menu = new Menu({
		"element-dns-records.html":elementDnsView
 	});
