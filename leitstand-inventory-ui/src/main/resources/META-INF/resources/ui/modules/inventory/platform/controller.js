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
import {html} from '/ui/js/ui-components.js';
import {Platforms,Platform} from '/ui/modules/inventory/inventory.js';

const IFP_NAME  = /[a-z]+\-(?:\/?\d+){3,}/;
const BANDWIDTH = /\d{1,3}\.\d{3} [G|M]bps/;


const platformsController = function() {
	const platforms = new Platforms();
	return new Controller({
		resource: platforms,
		viewModel: function(platforms){
			return {"platforms":platforms};
		}});
};
	
const platformController = function() {
	const platform = new Platform();
	let portIndex = -1;
	return new Controller({
		resource: platform,
		viewModel:function(settings){
			settings.indexed_port_mappings=function(){
				const ports = [];
				if(settings.port_mappings){
					for(let i=0; i < settings.port_mappings.length; i++){
						ports.push({"index":i,"port":settings.port_mappings[i]});
					}
				}
				return ports;
			}
			return settings;
		},
		buttons: {
			"save":function(){
				platform.saveSettings(this.location.params,
									  this.getViewModel());
			},
			"remove-platform":function(){
				platform.remove(this.location.params);
			},
			"add-port":function(){
				portIndex=-1;
				this.updateViewModel({"new":{},"edit":true});
				this.renderView();
			},
			"edit-port":function(location,element){
				portIndex=parseInt(element.getAttribute("data-index"));
				const portMapping = this.getViewModel("port_mappings")[portIndex];
				if(portMapping.bandwidth){
					const bw = portMapping.bandwidth.split(" ");
					portMapping.bandwidth_value=bw[0];
					portMapping.bandwidth_unit=bw[1];
				}
				this.updateViewModel({"new":portMapping,"edit":true});
				this.renderView();
			},
			"save-port":function(){
				const newPortMapping = this.getViewModel("new");
				const mappings = this.getViewModel("port_mappings");
				const ifpName = newPortMapping.ifp_name;
				const bandwidth = newPortMapping.bandwidth_value ? newPortMapping.bandwidth_value+" "+newPortMapping.bandwidth_unit : null;
				delete newPortMapping.bandwidth_value;
				delete newPortMapping.bandwidth_unit;
				let valid = true;
				if (!newPortMapping.chassis_id){
					valid = false;
					this.message({
						severity:'ERROR',
						property:'new.chassis_id',
						message:'Enter a valid chassis ID.'
					});
				}
				
				if (!newPortMapping.panel_block_id){
					valid = false;
					this.message({
						severity:'ERROR',
						property:'new.chassis_id',
						message:'Enter a valid block ID.'
					});
				}
				
				if (!newPortMapping.port_id){
					valid = false;
					this.message({
						severity:'ERROR',
						property:'new.port_id',
						message:'Enter a valid port ID.'
					});
				}
				
				if (!ifpName) {
					valid = false;
					this.message({
						severity:'ERROR',
						property:'new.ifp_name',
						message:'Enter a physical interface name.'
					});
				}
				
				if (ifpName && !ifpName.match(IFP_NAME)){
					valid = false;
					this.message({
						severity:'ERROR',
						property:'new.ifp_name',
						message:'Invalid physical interface name.'
					});
				}
				if(bandwidth && !bandwidth.match(BANDWIDTH)) {
					valid = false;
					this.message({
						severity:'ERROR',
						property:'new.bandwidth_value',
						message:'Invalid bandwidth value.'
					});
				} else {
					newPortMapping.bandwidth=bandwidth;
				}
				
				if (valid){
					if (portIndex < 0){
						mappings.push(Object.assign({},newPortMapping));				
					} else {
						mappings[portIndex]=Object.assign({},newPortMapping);
					}
					this.updateViewModel({"port_mappings":mappings,"edit":false});
					this.renderView();					
				}
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

const addPlatformController = function() {
	const platforms = new Platforms();
	return new Controller({
		resource:platforms,
		viewModel:function(){
			return {};
		},
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
	
const platformMenu =  {
	"master" : platformsController(),
	"details" : {
		"new-platform.html":addPlatformController(),
		"confirm-remove.html":platformController(),
		"platform.html":platformController()
	} 
};
	
export const menu = new Menu({"platforms.html" : platformMenu});
