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
