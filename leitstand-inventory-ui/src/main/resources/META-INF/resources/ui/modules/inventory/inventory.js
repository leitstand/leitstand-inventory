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

/**
 * Creates a Pods collection.
 * @constructor
 * 
 * @classdesc
 * The Pods resource represents the collections of pods in the inventory and provides method to query for pods.
 * @augments Resource
 */
export class Pods extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	/**
	 * Selects all pods from the existing pods matching the given filter criterion 
	 * with a default limit of at most 100 pods.
	 * @param {string} params.filter The filter string to filter pods by name and/or tags
	 * @param {number} [params.limit=100] The maximum number of returned pods. 
	 */
	load(params) {
		if(this._cfg && this._cfg.scope){
			return this.json("/api/v1/pods/{{scope}}?filter={{&filter}}",
							 {"limit":100},
							 this._cfg,
							 params)
					   .GET();
		}
		
		return this.json("/api/v1/pods?filter={{&filter}}&limit={{&limit}}",
						 {"limit":100},
						 this._cfg,
						 params)
				   .GET();
	}
	
	/**
	 * Adds a new pod to the inventory.
	 * @param {string} params.group The group's unique, immutable UUID or the group's unique name 
	 * @param {string} settings.group_id The group's immutable UUID (read-only)
	 * @param {string} settings.group_name The group's unique name
	 * @param {string} settings.description An optional description of the PoD.
	 * @param {string} settings.location The location of the group
	 * @param {string[]} settings.tags The tags of the group
	 */
	createPod(params,settings){
		settings["group_type"]="pod";
		return this.json("/api/v1/pods/",
						 this._cfg,
						 settings,
						 params)
				   .POST(settings);
	}
	
}

export class Platforms extends Resource {
	
	load(params){
		return this.json("/api/v1/platforms")
		     	   .GET();
	}
	
	addPlatform(platform){
		return this.json("/api/v1/platforms")
		    	   .POST(platform);
	}
}

export class Platform extends Resource {	
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/platforms/{{platform}}",
				  		 this._cfg, 
				  		 params)
				   .GET();
	}
	
	saveSettings(params,settings){
		return this.json("/api/v1/platforms/{{platform}}",
				  	     this._cfg, 
				  	     params)
				   .PUT(settings);
	}
	
	remove(params){
		return this.json("/api/v1/platforms/{{platform}}",
				  		 this._cfg,
				  		 params)
				   .DELETE();
	}
}

export class Roles extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(){
		return this.json("/api/v1/roles")
		     	   .GET();
	}
	
	addRole(params,role){
		return this.json("/api/v1/roles",
						 this._cfg, 
						 params)
				   .POST(role);
	}
}

export class Role extends Resource {
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/roles/{{role}}",
						 this._cfg, 
						 params)
				   .GET();
	}
	
	saveSettings(params,settings){
		return this.json("/api/v1/roles/{{role}}",
						 this._cfg, 
						 params)
				   .PUT(settings);
	}
	
	remove(params){
		return this.json("/api/v1/roles/{{role}}",
				  		 this._cfg, 
				  		 params)
				   .DELETE();
	}
}

export class PhysicalInterfaces extends Resource {
	
	constructor(cfg) {
		super();
		this._cfg = cfg;
	}

	load(params) {
		return this.json("/api/v1/physical_interfaces?facility={{facility}}&ifp={{&ifp}}&limit={{&limit}}&offset={{&offset}}",
						 {"limit":100,"offset":"0"},
						 this._cfg,
						 params)
				   .GET();
	}
	
}

/**
 * Creates a Pod resource.
 * @constructor
 * @param {string} cfg.scope The sub resource to be loaded, such as 
 *        <code>settings</code>,<code>link-state</code> or <code>elements</code> of the group.
 * @param {string} [cfg.group] The group's immutable UUID or the group's unique name.
 * @classdesc
 * The Pod resource provides access to a group stored in the EMS inventory. Provides functions to retrieve informations of the PoD 
 * from the inventory and to update the settings of the PoD in the inventory.
 * @augments Resource
 */
export class Pod extends Resource {
	
	constructor(cfg) {
		super();
		this._cfg = cfg;
	}
	
	/**
	 * Loads the PoD sub-resource from the inventory and invokes the appropriate event listener
	 * depending on the server reply.
	 * @param {string} params.group the group's immutable UUID or the group's unique name 
	 */
	load(params) {
		let query="";
		let del="?";
		if(params["hops"]){
			query+=(del+"hops={{&hops}}");
			del="&";
		}
		if(params["layout"]){
			query+=(del+"layout={{&layout}}");
			del="&";
		}
		if(params["role"]){
		    query+=(del+"role={{&role}}");
			del="&";
		}
		if(params["filter"]){
			query+=(del+"filter={{&filter}}");
			del="&";
		}
		if(this._cfg.scope == "topology"){
			query+=(del+"overview={{&overview}}");
			del="&";
		}
		
		return this.json(`/api/v1/pods/{{&group}}/{{&scope}}${query}`,
						 this._cfg,
						 params)
				   .GET();
	}

	//TODO Add params descriptor
	//TODO Add settings descriptor
	/**
	 * Updates the group settings in the inventory.
	 * @param {string} params.group The group's unique, immutable UUID or the group's unique name 
	 * @param {string} settings.group_id The group's immutable UUID (read-only)
	 * @param {string} settings.group_name The group's unique name
	 * @param {string} settings.description An optional description of the PoD.
	 * @param {string} settings.location The location of the group
	 * @param {string[]} settings.tags The tags of the group
	 */
	saveSettings(params,settings) {
		return this.json("/api/v1/pods/{{&group}}/settings",
						 this._cfg,
						 settings,
						 params)
				   .PUT(settings);
	}
	
	/**
	 * Removes an empty group from the inventory.
	 * @param {string} params.group The group's unique, immutable UUID or the group's unique name 
	 */
	removePod(params){
		return this.json("/api/v1/pods/{{&group}}",
						 this._cfg,
						 params)
				   .DELETE();
	}
}

export class Facilities extends Resource{
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/facilitys?filter={{&filter}}",this._cfg,params)
				   .GET();
	}
	
	add(params,rack){
		return this.json("/api/v1/facilitys",params)
				   .POST(rack);
	}
	
	
}

export class Facility extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/facilitys/{{&facility}}/{{&scope}}",
						 this._cfg,
						 params)
				   .GET();
	}

	saveSettings(params, settings){
		return this.json("/api/v1/facilitys/{{&facility}}/{{&scope}}",
						 this._cfg,
						 settings,
						 params)
				   .PUT(settings);
	}
	
	removeFacility(params){
		return this.json("/api/v1/facilitys/{{&facility}}",
		          		 this._cfg,
		          		 params)
		           .DELETE();	
	}
}


export class Racks extends Resource{
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		if(this._cfg && this._cfg.scope == '_findElement'){
			return this.json("/api/v1/racks/_findElement?element={{element}}",this._cfg,params)
					   .GET()
		}
		
		return this.json("/api/v1/racks?filter={{filter}}&facility={{facility}}",this._cfg,params)
				   .GET();
	}
	
	add(params,rack){
		return this.json("/api/v1/racks",params)
				   .POST(rack);
	}
	
	
}

export class Rack extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/racks/{{&rack}}/{{&scope}}",
						 this._cfg,
						 params)
				   .GET();
	}

	saveSettings(params, settings){
		return this.json("/api/v1/racks/{{&rack}}/{{&scope}}",
						 this._cfg,
						 settings,
						 params)
				   .PUT(settings);
	}
	
	addRackItem(params,item){
		return this.json("/api/v1/racks/{{&rack}}/items/{{position}}",params)
				   .PUT(item);
	}
	
	removeRack(params){
		return this.json("/api/v1/racks/{{&rack}}?force={{force}}",
		          		 this._cfg,
		          		 params)
		           .DELETE();	
	}
}

export class RackItem extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/racks/{{&rack}}/items/{{&unit}}",
						 this._cfg,
						 params)
				   .GET();
	}

	saveSettings(params, settings){
		return this.json("/api/v1/racks/{{&rack}}/items/{{&unit}}",
						 this._cfg,
						 settings,
						 params)
				   .PUT(settings);
	}
	
	removeRackItem(params){
		return this.json("/api/v1/racks/{{&rack}}/items/{{&unit}}",
		          		 this._cfg,
		          		 params)
		           .DELETE();	
	}
}

export class Elements extends Resource {
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/elements?filter={{&filter}}&by={{by}}",
						 this._cfg,
						 params)
				   .GET();
	}
}

/**
 * Creates a Element resource.
 * @constructor
 * @param {string} cfg.scope The sub-resource of the element to be accessed, such as 
 *        <code>settings</code>, <code>links</code>, <code>ports</code>, <code>modules</code>, 
 *        <code>config</code> or installed <code>images</code>.
 * @param {string} [cfg.group] The group's immutable UUID or the group's unique name
 * @param {string} [cfg.element] The element's immutable UUID or the element's name
 * @classdesc
 * The Element resource provides access to an element stored in the inventory. Provides functions to fetch the inventoried element
 * and to update the element's settings.
 * @augments Resource
 */
export class Element extends Resource {

	constructor(cfg) {
		super();
		this._cfg = cfg;
	}
	
	/**
	 * Loads the element sub-resource from the inventory and invokes the appropriate event listener
	 * depending on the server reply.
	 * @param {string} [params.group] The group's immutable UUID or the group's unique name 
	 * @param {string} [params.element] The element's immutable UUID or the element's unique name
	 * @param {string} [params.image_id] The immutable UUID of the installed image, if asking for a certain image 
	 * @param {string} [params.service_name] The service name, if asking for a certain service
	 */
	load(params) {
		if(this._cfg["scope"]=="images"){
			return this.json("/api/v1/elements/{{&element}}/{{&scope}}/{{&image_id}}",
							 this._cfg,
							 params)
					   .GET();
		}
		
		if(this._cfg["scope"]=="services"){
			return this.json("/api/v1/elements/{{&element}}/{{&scope}}/{{&service_name}}",
							 this._cfg,
							 params)
					   .GET();
		}
		
		if(this._cfg["scope"]=="configs"){
			return this.json("/api/v1/elements/{{&element}}/{{&scope}}?filter={{filter}}",
							 this._cfg,
							 params)
					   .GET();
		}

		if(this._cfg["scope"]=="modules"){
			return this.json("/api/v1/elements/{{&element}}/modules/{{&module_name}}",
							 this._cfg,
							 params)
					   .GET();
		}
		
		// Don't use {{&scope}} in order to process all variables that are part of the specified scope.
		return this.json(`/api/v1/elements/{{&element}}/${this._cfg['scope']}`,
						 this._cfg,
						 params)
				   .GET();

	}

	/**
	 * Updates the element settings in the inventory and invokes the appropriate event listener depending on the server reply.
	 * @param {string} [params.group] The group's immutable UUID or the group's unique name 
	 * @param {string} [params.element] The element's immutable UUID or the element's unique name
	 * @param {Object} settings The element settings as defined in EMS REST API
	 */
	saveSettings(params,settings) {
		return this.json(`/api/v1/elements/{{&element}}/${this._cfg['scope']}`,
		          		this._cfg,
		          		settings,
		          		params)
		           .PUT(settings);
	}
	
	/**
	 * Creates an element in the inventory and invokes the appropriate event listener depending on the server reply.
	 * @param {string} [params.group] The group's immutable UUID or the group's unique name 
	 * @param {string} [params.element] The element's immutable UUID or the element's unique name
	 * @param {Object} settings The element settings as defined in EMS REST API
	 */
	createElement(params,settings) {
		return this.json("/api/v1/elements",
						 this._cfg,
						 settings,
						 params)
				   .POST(settings);
	}
	
	removeElement(params,settings){
		return this.json("/api/v1/elements/{{&element}}?force={{force}}",
						 this._cfg,
						 params)
				   .DELETE();
	}
	
	remove(params,settings){
		return this.json(`/api/v1/elements/{{&element}}/${this._cfg['scope']}`,
						 this._cfg,
						 settings,
						 params)
				   .DELETE();
	}

	saveConfig(params,config){
		config.state="CANDIDATE";
		if(config.content_type == 'application/json' && typeof config.content  == 'string'){
		    config.content = JSON.parse(config.content);
		}
		return this.json("/api/v1/elements/{{&element}}/configs/{{config_name}}?comment={{comment}}&state={{state}}",
						 this._cfg,
						 params,
						 config)
				   .contentType(config.content_type)
				   .POST(config.content);
	}
	
	saveEnvironment(params,env){
		return this.json("/api/v1/elements/{{&element}}/environments",
				 		 this._cfg,
				 		 params)
				   .POST(env);
		
	}
	
	saveDnsRecord(params,record){
		return this.json("/api/v1/elements/{{&element}}/dns",
						 this._cfg,
						 params)
				   .POST(record);
	}
	
	cloneElement(params,cloneRequest){
	    return this.json("/api/v1/elements/{{&element}}/_clone",
	                     this._cfg,
	                     params)
	               .POST(cloneRequest);
	}
	
}

//TODO Documentation
export class ElementPhysicalInterfaces extends Resource {
	constructor(cfg){
		super();
		this._cfg = cfg ? cfg : {};
	}
	
	load(params){
		return this.json("/api/v1/elements/{{&element}}/physical_interfaces?ifp_name={{&ifp_name}}&ifp_alias={{&ifp_alias}}&operational_state={{operational_state}}&administrative_state={{administrative_state}}",
				  		 this._cfg,
				  		 params)
				   .GET()
	}
}


//TODO Documentation
export class ElementPhysicalInterface extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg ? cfg : {};
	}
	
	load(params){
		return this.json("/api/v1/elements/{{&element}}/physical_interfaces/{{&ifp_name}}/{{&scope}}",
				  		 this._cfg,
				  		 params)
				   .GET()
	}
}

// TODO Documentation
export class ElementLogicalInterfaces extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/elements/{{&element}}/logical_interfaces?filter={{&filter}}",
				  		 this._cfg,
				  		 params)
				   .GET()
	}
}

//TODO Documentation
export class ElementLogicalInterface extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/elements/{{&element}}/logical_interfaces/{{&ifl_name}}",
				  		 this._cfg,
				  		 params)
				   .GET()
	}
}

export class Metadata extends Resource {
	
	constructor (cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/{{&scope}}",
				 		 this._cfg,
				 		 params)
				   .GET();
	}
}

export class TimeSeries extends Resource {
    
    constructor(cfg){
        super()
        this._cfg = cfg;
    }
    
    load(settings){
        const params = Object.assign(this._cfg,settings);
        if(params.metric_name){
            return this.json("/api/v1/timeseries/{{element_role}}/{{element_name}}/{{metric_name}}",params)
                       .GET();
        }
        return this.json("/api/v1/timeseries/{{element_role}}/{{element_name}}",params)
                   .GET();
    }
    
}

export class Panel extends Resource {
    
    constructor(cfg){
        super()
        this._cfg = cfg;
    }
    
    load(params){
        
        if(params.ifp_name){
            return this.json("/api/v1/elements/{{element}}/panels/{{panel}}?ifp_name={{&ifp_name}}",params)
                       .GET();
        }

        if(params.ifl_name){
            return this.json("/api/v1/elements/{{element}}/panels/{{panel}}?ifl_name={{&ifl_name}}",params)
                       .GET();
        }

        if(params.service_name){
            return this.json("/api/v1/elements/{{element}}/panels/{{panel}}?service_name={{service_name}}",params)
                       .GET();
        }

        return this.json("/api/v1/elements/{{element}}/panels/{{panel}}",params)
                   .GET();
    }
    
}

	
