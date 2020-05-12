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
import {units} from '/ui/js/widgets.js';
import {Metadata,Element,Pod,ElementPhysicalInterfaces,ElementPhysicalInterface,ElementLogicalInterfaces,ElementLogicalInterface,Platforms} from '/ui/modules/inventory/inventory.js';
import '../inventory-components.js';

//TODO: Implement Rack Component!
const elementRackController = function(){
	
	let Rack = function(rack){
		
		const units = [];
		const elements = {};
		
		for(let i = 0; i < rack.units; i++){
			units.push({
				"unit": ((100+i+1)+"").substring(1),
				"elements":[]
			});
		}			
		
		for(let i = 0; i < rack.elements.length; i++){
			let element = {
				"element":rack.elements[i],
				"selected":rack.elements[i].element_name == rack.element_name ? "selected" :  "",
				"height":rack.elements[i].height,
				"size":function(){
					if (this.element.half_rack){
						if(this.element.half_rack_pos == "LEFT"){
							return "half_rack left";
						}
						return "half_rack right";
					}
				} 		
			};
			units[rack.elements[i].unit-2+rack.elements[i].height].elements.push(element);
		}			
		
		this.units = function(){
			units.reverse();
			return units;
		}
	};
	
	const element = new Element({"scope":"rack"});
	return new Controller({
		resource:element,
		viewModel:function(settings){
			settings.rack = this.transient(new Rack(settings));
			return settings;
		},
		onNotFound : function(){
			this.navigate({"view":"new-element-location.html",
						   "?":this.location.params});
		},
		buttons:{
			"save-rack":function(){
				const location = { "rack_name":this.input("rack_name").value(),
						  		   "rack_position":this.input("rack_position").value(),
						  		   "address":this.input("address").value()};
				const settings = this.updateViewModel({"location":location});
				element.saveSettings(this.location.params,
				                     settings);
			}
		}
	});
};

const addElementLocationController = function(){
	const element = new Element({"scope":"settings"});
	return new Controller({
		resource:element,
		viewModel:async function(settings){
			const racks = new Pod({"scope":"racks"});
			settings.racks = await racks.load(this.location.params);
			return settings;
		},
		buttons:{
			"save-location":function(){
				const location = {"rack_name":this.input("rack_name").value(),
								  "unit":this.input("unit").value()};
				const rack = new Element({"scope":"rack"});
				this.attach(rack);
				rack.saveSettings(this.location.params,
								  location);
			}
		},
		onSuccess : function(){
			this.navigate({"view":"element-rack.html",
						   "?": this.location.params});
		}
	});
};

const elementMountPointController = function(){
	const element = new Element({"scope":"rack"});
	return new Controller({
		resource:element,
		viewModel: async function(settings){ 
			const racks = new Pod({"scope":"racks"})
						  .load(this.location.params);
			const element = (function() {
				for(let i=0; i < settings.elements.length; i++){
					if(settings.elements[i].element_id == settings.element_id){
						return settings.elements[i];
					}
				 }
			})();
				
			const half_rack_pos = [{"value":"LEFT",
									  "display_text":"Left side",
									  "selected":element.half_rack_pos == "LEFT" ? "selected" : ""},
									{"value":"RIGHT",
									  "display_text":"Right side",
									  "selected":element.half_rack_pos == "RIGHT" ? "selected" : ""}];
				
			this.updateViewModel({"racks":racks.racks,
								  "unit": element.unit,
								  "half_rack":element.half_rack,
								  "positions":half_rack_pos,
								  "selected":function(){
										  return this.rack_name == settings.rack_name	? "selected" : "";}});
		},
		buttons:{
			"save-location":function(){
				const location = {"rack_name":this.input("rack_name").value(),
								  "unit":this.input("unit").value(),
								  "position":this.input("position").value()};
				const rack = new Element({"scope":"rack"});
				this.attach(rack);
				rack.saveSettings(this.location.params,
								  location);
			},
			"remove-location":function(){
				const rack = new Element({"scope":"rack"});
				this.attach(rack);
				rack.remove(this.location.params);
			}
		},
		onSuccess:function(){
			this.navigate({"view":"element-rack.html",
						   "?":this.location.params});
		}
	});
};

const elementLocationController = function(){
	const element = new Element({"scope":"settings"});
	return new Controller({
		resource:element,
		buttons:{
			"save-location":function(){
				const location = { "rack_name":this.input("rack_name").value(),
						  		   "rack_position":this.input("rack_position").value(),
						  		   "address":this.input("address").value()};
				const settings = this.updateViewModel({"location":location});
				element.saveSettings(this.location.params,
				                     settings);
			}
		}
	});
};


const elementServiceController = function(){
	const element = new Element({"scope":"services"});
	return new Controller({
		resource:element,
		viewModel:function(model){
			//Expose top of stack to simplify UI template.
			model.service_name = this.transient(model.stack[0].service_name);
			model.display_name = this.transient(model.stack[0].display_name);
			model.operational_state = this.transient(model.stack[0].operational_state);
			return model;
		}
	});
};


const elementController = function(){
	const element = new Element({"scope":"settings"});
	return new Controller({
		resource:element,
		viewModel:async function(settings){
			
			// The element settings are the basis for this view model.
			const viewModel = settings;

			// Add available administrative and operational states.
			// Both transient again.
			viewModel.administrative_states = this.transient([{"value" : "NEW",
															   "label" : "New"},
															  {"value" : "ACTIVE",
															   "label" : "Active"},
															  {"value" : "RETIRED",
															   "label" : "Retired"}]);
			viewModel.operational_states = this.transient([{"value" : "DOWN", 
											 				"label" : "Down"},		
											 			   {"value" : "UP", 
											 			    "label" : "Up"}, 
											 			   {"value" : "DETACHED", 
											 			    "label" : "Detached"}, 
											 			   {"value" : "MAINTENANCE", 
											 			    "label" : "Maintenance"}]);
			
			// Translate map of management interfaces into an array to render the list of management interfaces.
			viewModel.mgmt_interface_list = function() {
				const mgmt_interface_list = [];
				for(const mgmt_name in settings.mgmt_interfaces){
					mgmt_interface_list.push(settings.mgmt_interfaces[mgmt_name]);
				}
				return mgmt_interface_list;
			}
			viewModel.mgmt_interface_list_length = function() {
				const mgmt_interface_list = [];
				for(const mgmt_name in settings.mgmt_interfaces){
					mgmt_interface_list.push(settings.mgmt_interfaces[mgmt_name]);
				}
				return mgmt_interface_list.length;
			}

			viewModel.inactive = function(){
				return settings.administrative_state != "ACTIVE";
			}
			return viewModel;
		},
		buttons:{
			"save-element":function(){
				const settings = this.getViewModel();
				settings.platform_name = this.input("element-platform").unwrap().selected.label;
				element.saveSettings(this.location.params,
				                     settings);
			},
			"remove-element":function(){
				const params = this.location.params;
				params["force"] = this.input("force").value();
				element.removeElement(params);
			},
			"add-mgmt":function(){
				this.navigate({"view" : "/ui/views/inventory/element/element-mgmt.html",
							   "?" : this.location.params});
			}
		},
		onRemoved:function(){
			this.navigate({"view":"/ui/views/inventory/pod/pod-elements.html",
						    "?":{"group":this.location.param("group")}});
		},
		onSuccess:function(){
			this.reload();
		}
	});
};

const elementMgmtController = function(){

	const element = new Element({"scope":"settings"});
	
	return new Controller({
		resource:element,
		viewModel:function(settings){

			// Augment settings with available management protocols.
			settings.mgmt_protocols = this.transient([{"label":"HTTP",
													   "value":"http"},
													  {"label":"HTTPS",
													   "value":"https"},
													  {"label":"gNMI",
													   "value":"gnmi"},
													  {"label":"SSH",
													   "value":"ssh"}]);
			const mgmt_name = this.location.param("mgmt_name");
			settings.mgmt_ifc = settings.mgmt_interfaces[mgmt_name];
			return settings;
		},
		buttons:{
			"save-mgmt":function(){
				// Read element settings
				const settings = this.getViewModel();
				// Read mgmt_name 
				const mgmt_name = this.location.param("mgmt_name");
				if(mgmt_name){
					//Remove existing mgmt interface and add it again.
					delete settings.mgmt_interfaces[mgmt_name]					
				}
				// Register management interface. Rename is handled implicitly due to previous remove operation.
				settings.mgmt_interfaces[settings.mgmt_ifc.mgmt_name] = settings.mgmt_ifc;
				// Finally delete temporary mgmt_ifc binding to be compliant with REST API.
				delete settings.mgmt_ifc
				element.saveSettings(this.location.params,
									 settings);
			},
			"remove-mgmt":function(){
				const settings = this.getViewModel();
				const mgmt_name = this.location.param("mgmt_name");
				if(mgmt_name){
					delete settings.mgmt_interfaces[mgmt_name];
					element.saveSettings(this.location.params,
										 settings);
				} else {
					this.navigate({"view":"/ui/views/inventory/element/element.html",
						   "?": {"group":this.location.param("group"),
							   	 "element":this.location.param("element")}});
				}
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/element/element.html",
						   "?": {"group":this.location.param("group"),
							   	 "element":this.location.param("element")}});
		}
	});
};

const elementImagesController = function(){
	const element = new Element({"scope":"images"});
	return new Controller({
		resource:element,
		viewModel:function(settings){
			settings.updateSummary = function(){
				const major = 0;
				const minor = 0;
				const patch = 0;
				if(this["available_updates"]){
					this["available_updates"].forEach(function(update){
						if(update["update_type"]=="MAJOR"){
							major++;
						}
						if(update["update_type"]=="MINOR"){
							minor++;
						}
						if(update["update_type"]=="PATCH"){
							patch++;
						}
					});					
				}

				return {"major":major,
						"minor":minor,
						"patch":patch, 
						"updates": (major+minor+patch) > 0 };
			};
			return settings;
		},
	});
};

const elementImageController = function(){
	const element = new Element({"scope":"images"});
	return new Controller({
		resource:element,
		viewModel:function(settings){
			settings.updatesAvailable = function(){
				return this["available_updates"] && this["available_updates"].length > 0;
			};
			
			settings.displayType = function(){
				if(this["type"] == "MAJOR") return "Major ";
				if(this["type"] == "MINOR") return "Minor ";
				if(this["type"] == "PATCH") return "Patch ";
				return "Pre-Release ";
			};
			
			settings.state = function(){
				return this["active"] ? "STARTED" : "CACHED";
			}
			return settings;
		},
	});
};

const elementServicesController = function(){
	const element = new Element({"scope":"services"});
	return new Controller({
		resource:element
	});
};

const elementPodController = function(){
	const element = new Element({"scope":"settings"});
	return new Controller({
		resource:element,
		viewModel: function(settings){
			// Load all existing pods
			const podsLoader = new Pods({"filter":this.location.param("filter")});
			this.attach(podsLoader);
			const pods = podsLoader.load();
			pods.forEach(pod => pod.group_id == settings.group_id ? pod.checked = "checked" : pod.checked ="");
			settings.pods = pods;
			// Add filter statement
			settings.filter = filter;
			return settings;
		},
		buttons: {
			"filter-pods":function(){
				const params = this.location.params;
				params.filter = this.input("filter").value();
				this.reload(params);
			},
			"move-element":function(){
				this.updateViewModel({"group_id":this.input("group_id").value()});
				element.saveSettings(this.location.params,this.getViewModel());
			}
		},
		onSuccess:function(){
			this.navigate({"view":"element.html",
						   "?":this.location.params});
		}
	});
	
};

const elementIfpsController = function(){
	const ifps = new ElementPhysicalInterfaces();
	return new Controller({
		resource:ifps,
		postRender:function(){
			const ifps = this.getViewModel();
			const metrics = new Connector({"scope":"ifp"});
			metrics.onLoaded = function(response){
				const ifps = {};
				if(response && response.metrics && response.metrics.ifp_data_rate){
					response.metrics.ifp_data_rate.forEach(function(ifp){
						const metric = ifps[ifp.labels.ifp_name];
						if(!metric){
							metric = {};
							ifps[ifp.labels.ifp_name] = metric;
						}
						metric[ifp.labels.direction]=parseFloat(ifp.value);
					});
					for( const ifp in ifps ){
						document.getElementById(ifp).innerHTML=(Units.format(ifps[ifp]["in"],"Gbps")+" / "+Units.format(ifps[ifp]["out"],"Gbps"));
					}
				}
			};
			metrics.onNotFound = function(){alert("Cannot access interface metrics!")};
			metrics.load(this.location.params);
		}
	});
};

const elementIfpController = function(){
	const ifp = new ElementPhysicalInterface();
	return new Controller({
		resource:ifp
	});
};

const elementIflsController = function(){
	const ifls = new ElementLogicalInterfaces();
	return new Controller({
		resource:ifls
	});
};

const elementIflController = function(){
	const ifl = new ElementLogicalInterface();
	return new Controller({
		resource:ifl
	});
};

const elementModulesController = function(){
	const modules = new Element({"scope":"modules"});
	return new Controller({resource:modules});
};

const elementModuleController = function(){
	const module = new Element({"scope":"modules/{{module}}"});
	return new Controller({resource:module,
					 buttons:{
						 "save":function(){
							 const model = this.updateViewModel({"module.asset_id":this.input("asset_id").value()});
							 module.saveSettings(this.location.params,
									 			 model.module);
						 }
					 },
					 onSuccess:function(){
						 this.navigate({"view":"element-modules.html",
							 		    "?":{"group":this.location.param("group"),
							 		         "element":this.location.param("element")}});
						 }
					 });
}



const elementImagesMenu = {
	"master" : elementImagesController(),
	"details" : {"element-image.html" : elementImageController() }
};


const modulesMenu = {
	"master" :  elementModulesController(),
	"details" : {"element-module.html" : elementModuleController()}
};

const elementMenu = {
	"master" : elementController(),
	"details" : {"element-mgmt.html" : elementMgmtController(),
				 "element-pod.html" : elementPodController(),
				 "confirm-remove-element.html" : elementController()}
};

const elementRackMenu = {
	"master" : elementRackController(),
	"details" : {"new-element-location.html" : addElementLocationController(),
				 "element-location.html" : elementMountPointController(),
				 "confirm-remove-location.html": elementMountPointController()}
}
	
export const menu = new Menu({
		"element.html" : elementMenu,
		"element-ifls.html": elementIflsController(),
		"element-ifps.html":elementIfpsController(),
		"element-ifl.html": elementIflController(),
		"element-ifp.html":elementIfpController(),
		"element-location.html" : elementLocationController(),
		"element-rack.html":elementRackMenu,
		"element-modules.html" :modulesMenu,
		"element-images.html" : elementImagesMenu,
		"element-services.html":elementServicesController(),
		"element-service.html":elementServiceController()
 	});
