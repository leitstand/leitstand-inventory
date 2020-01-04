/*
 *  (c) RtBrick, Inc - All rights reserved, 2015 - 2017
 */
import {Controller,Menu} from '/ui/js/ui.js';
import {Platforms,Platform} from '/ui/modules/inventory/inventory.js';

let platformsController = function() {
	let platforms = new Platforms();
	return new Controller({
		resource: platforms,
		viewModel: function(platforms){
			return {"platforms":platforms};
		}});
};
	
let platformController = function() {
	let platform = new Platform();
	return new Controller({
		resource: platform,
		buttons: {
			"save":function(){
				platform.saveSettings(this.location().params(),
									  this.getViewModel());
			},
			"remove-platform":function(){
				platform.remove(this.location().params());
			}
		},
		onSuccess:function(){
			this.navigate("platforms.html");
		},
		onRemoved:function(){
			this.navigate("platforms.html");
		}
	});
};	

let addPlatformController = function() {
	let platforms = new Platforms();
	return new Controller({
		resource:platforms,
		buttons:{
			"save":function(){
				platforms.addPlatform(this.getViewModel());
			}
		},
		onSuccess:function(){
			this.navigate("platforms.html");
		}
	});
};
	
let platformMenu =  {
	"master" : platformsController(),
	"details" : {
		"new-platform.html":addPlatformController(),
		"confirm-remove.html":platformController(),
		"platform.html":platformController()
	} 
};
	
export const menu = new Menu({"platforms.html" : platformMenu});