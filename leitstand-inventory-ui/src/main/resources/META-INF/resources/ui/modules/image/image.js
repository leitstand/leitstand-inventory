/*
 *  (c) RtBrick, Inc - All rights reserved, 2015 - 2017
 */

import {Resource,merge} from '/ui/js/client.js'

//TODO JSDoc

	
/**
 * Creates a new Images resource
 * @constructor
 * @param {string} [cfg.element_role] The type of element to search images for
 * @param {string} [cfg.image_type] The type of image to search images for, such as ONL-IMAGE, LXC-IMAGE or CFG-IMAGE
 * @param {string} [cfg.filter] - a filter to search for images for certain elements.
 * @classdesc
 * The Images resource provides access to the collections of software images registered in the inventory.
 * @augments Resource
 */
export class Images extends Resource {
	
	constructor(cfg) {
		super();
		this._cfg = cfg;
	}
	
	/**
	 * Loads all images from the inventory matching the given criteria.
	 * @param {string} [cfg.element_role] The type of element to search images for
	 * @param {string} [cfg.image_type] The type of image to search images for, such as ONL-IMAGE, LXC-IMAGE or CFG-IMAGE
	 * @param {string} [cfg.filter] - a filter to search for images for certain elements.
	 */
	load(params) {
		let args = merge(this._cfg,params);
		let path = "/api/v1/images";
		if(args.scope){
			path+="/{{scope}}";
		} else {
			path+="?filter={{&filter}}"
			if(args["image_version"]){
				path+="&image_version={{&image_version}}"
			}
			if(args["image_type"]){
				path+="&image_type={{&image_type}}";
			}
			if(args["image_state"]){
				path+="&image_state={{&image_state}}";
			}
		}
		return this.json(path,args)
				   .GET();
	}
}


/**
 * Creates a new Image resource.
 * @constructor
 * @param {string} [cfg.image] The image's UUID
 * @param {string} [cfg.scope] Optional statistics scope to fetch image utilization statistics.
 * @classdesc
 * The Image resource provides access to an image inventoried in the EMS.
 * @augments Resource
 */
export class Image extends Resource {
	
	constructor(cfg) {
		super();
		this._cfg = cfg;
	}
	
	/**
	 * Loads the image from the repository .
	 * @param {string} [params.image] The image's UUID
	 */
	load(params) {
		return this.json("/api/v1/images/{{&image}}/{{&scope}}",
						 this._cfg,
						 params)
				   .GET();
	}
	
	/**
	 * Sets the state of this image and updates the states of other images accordingly.
	 * @param {string} [params.image] The image's UUID
	 * @param {string} [params.image_state] The new state of this image
	 */
	updateState(params){
		return this.json("/api/v1/images/{{&image}}/image_state",
						 this._cfg,
						 params)
				   .PUT(params["image_state"]);
	}
	
	/**
	 * Purges a revoked image from all caches in the network.
	 */
	purgeCaches(params){
		return this.json("/api/v1/images/{{&image}}/_purge",
						 this._cfg,
						 params)
				   .POST();
	}
}