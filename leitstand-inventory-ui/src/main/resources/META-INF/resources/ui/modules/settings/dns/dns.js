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
import {Resource} from '/ui/js/client.js';

export class DnsZones extends Resource {

	load(query){
		return this.json("/api/v1/dns/zones?filter={{filter}}",query)
				   .GET();
	}
	
	addZone(zone){
		return this.json("/api/v1/dns/zones")
				   .POST(zone);
	}
	
}

export class DnsZone extends Resource {
	
	constructor(config){
		super();
		this.config = config;
	}
	
	load(query){
		return this.json("/api/v1/dns/zones/{{zone}}/{{scope}}",this.config, query)
				   .GET();
	}
	
	storeSettings(params,zone){
		return this.json("/api/v1/dns/zones/{{zone}}/{{scope}}",this.config,params)
		   		   .PUT(zone);
	}
	
	removeZone(params){
		return this.json("/api/v1/dns/zones/{{zone}}?force={{force}}",this.config,params)
				   .DELETE();
	}
	
}
