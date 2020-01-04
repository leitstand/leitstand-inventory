/*
 *  (c) RtBrick, Inc - All rights reserved, 2015 - 2017
 */

import {Resource} from '/ui/js/client.js';

// ES6 Migration Backlog
// TODO Replace all string computations with the ES6 template function
// TODO Update JSDoc

/**
 * Creates a new Pods collection.
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
		return this.json("/api/v1/physical_interfaces?filter={{&filter}}&limit={{&limit}}",
						 {"limit":100},
						 this._cfg,
						 params)
				   .GET();
	}
	
}

/**
 * Creates a new Pod resource.
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
		var query="";
		var del="?";
		if(params["hops"]){
			query+=(del+"hops={{&hops}}");
			del="&";
		}
		if(params["layout"]){
			query+=(del+"layout={{&layout}}");
			del="&";
		}
		if(params["type"]){
			if(params["type"].forEach){
				params["type"].forEach(function(type){
					query+=(del+"type="+type);
					del="&";
				});
			} else {
				query+=(del+"type="+params["type"]);
				del="&";
			}
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

export class Rack extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	load(params){
		return this.json("/api/v1/pods/{{&group}}/racks/{{&rack}}",
						 this._cfg,
						 params)
				   .GET();
	}

	saveSettings(params, settings){
		return this.json("/api/v1/pods/{{&group}}/racks/{{&rack}}",
						 this._cfg,
						 settings,
						 params)
				   .PUT(settings);
	}
	
	removeRack(params){
		return this.json("/api/v1/pods/{{&group}}/racks/{{&rack}}",
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
		return this.json("/api/v1/elements?filter={{&filter}}",
						 this._cfg,
						 params)
				   .GET();
	}
}

/**
 * Creates a new Element resource.
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
		
		if(this._cfg["scope"]=="modules"){
			return this.json("/api/v1/elements/{{&element}}/modules/{{&module_name}}",
							 this._cfg,
							 params)
					   .GET();
		}
		
		if(this._cfg["scope"]=="metrics"){
			if(!this._cfg["metric_scope"]){
				this._cfg["metric_scope"] = "ELEMENT";
			}
			
			return this.json("/api/v1/elements/{{&element}}/metrics?metric_scope={{metric_scope}}&fetch_mode=VISUALIZATION_CONFIG",
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
	editConfig(params,comment){
		return this.json(`/api/v1/elements/{{&element}}/${this._cfg['scope']}/_edit?comment={{comment}}&state={{state}}`,
						 this._cfg,
						 params,
						 {"comment":comment,
						  "state":"CANDIDATE"})
					.POST();
	}
	
	saveConfig(params,config){
		config.state="CANDIDATE";
		return this.json("/api/v1/elements/{{&element}}/configs/{{config_name}}?comment={{comment}}&state={{state}}",
						 this._cfg,
						 params,
						 config)
				   .POST(config.content);
	}
	
	saveDnsRecord(params,record){
		return this.json("/api/v1/elements/{{&element}}/dns",
						 this._cfg,
						 params)
				   .POST(record);
	}
}

/**
 * Creates a new Events resource.
 * @constructor
 * @param {string} [cfg.scope="/"] - specifies whether to load the events (<code>/</code>) or to count events (<code>counts</code>).
 * @param {string} [cfg.severity="WARNING"] The minimum severity of the loaded events (<code>INFORMATION &lt; WARNING &lt; ERROR</code>) 
 * @param {date}   [cfg.after] -  timestamp after fetched events have to be created
 * @param {date}   [cfg.before] - timestamp before fetched events have to be created
 * @param {string} [cfg.state] - state of the fetched events.
 * @param {string} [cfg.filter] - a event message filter
 * @param {number} [cfg.limit] The maximum number of fetched events
 * @classdesc
 * The Events resource provides access to events in the event store.
 */
export class Events extends Resource {
	
	constructor(cfg) {
		super();
		if(!cfg){
			cfg = {};
		}
	
		if(!cfg.scope){
			cfg.scope = "event";
		}
		
		this._cfg = cfg;
	}
	
	/**
	 * Loads events from the event store.
	 * @param {string} [params.severity="INFORMATION"] The minimum severity of the loaded events (<code>INFORMATION &lt; WARNING &lt; ERROR</code>) 
     * @param {date}   [cfg.after] -  timestamp after fetched events have to be created
     * @param {date}   [cfg.before] - timestamp before fetched events have to be created
     * @param {string} [cfg.state] - state of the fetched events.
	 * @param {string} [params.filter] - a event message filter
	 * @param {number} [params.limit] The maximum number of fetched events
	 */
	load(params) {
		if(this._cfg["scope"] == "counts"){
			return this.json("/api/v1/alerts/counts")
					   .GET();
		}
		return this.json("/api/v1/{{&scope}}?severity={{&severity}}&group={{&group}}&element={{&element}}&before={{&before}}&after={{&after}}&filter={{&filter}}&limit={{&limit}}&state={{&state}}&relative={{&relative}}",
						 this._cfg,
						 params)
				   .GET();
	}
	
	closeAllOpenEvents(params){
		if(params["element"]){
			return this.json("/api/v1/{{&scope}}/_close_all_element?element={{&element}}",
						     this._cfg,
						     params)
					   .POST();
		}
		return this.json("/api/v1/{{&scope}}/_close_all_group?group={{&group}}",
						 this._cfg,
						 params)
				   .POST();
	}
	
}

/**
 * Creates a new Event resource
 * @constructor
 * @param {string} [cfg.event_id] The event's UUID
 * @classdesc
 * The Event resource provides access to an event stored in the event store and provides function to update the event's state.
 * @augments Resource
 */
export class Event extends Resource {
	
	constructor(cfg) {
		super();
		if(!cfg){
			cfg = {};
		}	
		if(!cfg.scope){
			cfg.scope = "event";
		}
		this._cfg = cfg;
	}
	
	/**
	 * Loads an event from the event store.
	 * @param {string} [params.event_id] The event's UUID
	 */
	load(params) {
		return this.json("/api/v1/{{&scope}}/{{&index}}/{{&event_id}}",
						 this._cfg,
						 params)
				   .GET();
	}
	
	/**
	 * Updates the current state of the event. For example, an error can be closed to mark the error cause as solved but to keep the error
	 * for documentation purposes instead of removing it from the event store.
	 * @param {string} [params.event_id] The event's UUID
	 * @param {string} state The event's new state.
	 */
	updateState(params, state){
		return this.json("/api/v1/{{&scope}}/{{&index}}/{{&event_id}}/state",
		          		 this._cfg,
		          		 params)
		           .PUT(state);
	}
}


//TODO Documentation
export class ElementPhysicalInterfaces extends Resource {
	constructor(cfg){
		super();
		this._cfg = cfg ? cfg : {};
	}
	
	load(params){
		if(this._cfg.scope == "metrics"){
			return this.json("/api/v1/elements/{{&element}}/metrics?metric_scope=IFP&fetch_mode=VISUALIZATION_CONFIG",
							 this._cfg,
							 params)
					   .GET()
		}
		
		return this.json("/api/v1/elements/{{&element}}/physical_interfaces",
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
		if(this._cfg.scope == "metrics"){
			return this.json("/api/v1/elements/{{&element}}/metrics?metric_scope=IFP&fetch_mode=VISUALIZATION_CONFIG",
					  		 this._cfg,
					  		 params)
					   .GET()
		}
		
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
		return this.json("/api/v1/elements/{{&element}}/logical_interfaces",
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

//TODO Documentation
//TODO Move into other file
export class CommandProxy extends Resource {
	
	constructor(cfg){
		super();
		this._cfg = cfg;
	}
	
	submit(cmd){
		return this.plainText("/api/v1/elements/{{&element}}/services/{{&service}}/_exec",
							  this._cfg)
				   .contentType("text/plain")
				   .POST(cmd);
	}
}

//TODO Move into other file
export class Connector extends Resource {
	constructor(cfg){
		super();
		if(!cfg){
			cfg = {};
		}
		if(cfg.scope){
			cfg.scope = "/"+cfg.scope;
		}
		
		this._cfg = cfg;
	}
	
	load(params){ //FIXME Install local revese proxy /api/v1/connectors/{{&scope}}
		return this.json(`/api/v1/connectors/timeseries/{{&element}}${this._cfg.scope}`,
				  		 this._cfg,
				  		 params)
				   .GET();
	}
}
	