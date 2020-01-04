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
