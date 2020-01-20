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
import {Pod,Metadata,Platforms,Element,Rack} from '/ui/modules/inventory/inventory.js';
import {Events} from '/ui/modules/event/events.js';
import {Control} from '/ui/js/ui-components.js';

class PodLocation extends Control {
	
	connectedCallback(){
		let id = `${this.id}-container`;
		this.innerHTML = `<div id="${id}" style="display:block; clear: both; position:relative; width:750px; height:600px; margin:auto; background-color: #EFEFEF"></div>`;
		let geolocation = this.viewModel.getProperty(this.binding);
		if(geolocation){
	      let map = new ol.Map({
	          target: id,
	          layers: [
	            new ol.layer.Tile({
	              source: new ol.source.OSM()
	            }),
	            new ol.layer.Vector({
	                source: new ol.source.Vector({
	                  features: [new ol.Feature(new ol.geom.Point(ol.proj.fromLonLat([geolocation.longitude, 
	                		  														  geolocation.latitude])))]
	                }),
	                style: new ol.style.Style({
	                    image: new ol.style.Icon({
	                      scale: 0.2,
	                      src: '/images/pod.png'
	                    })
	                })			                
	             })
	          ],
	          view: new ol.View({
	            center: ol.proj.fromLonLat([geolocation.longitude, 
	            							geolocation.latitude]),
	            zoom: 16
	          })
	       });
	       map.addControl(new ol.control.ScaleLine());
		}
	}
	
}
customElements.define('pod-location',PodLocation);


let podElementsController = function(){
	let pod = new Pod({"scope":"elements"});
	return new Controller({
		resource:pod,
		viewModel:function(settings){
			settings.planeshort=function(){
				return this.plane == "CONTROL" ? 'C' : 'D';
			};
			settings.planelong=function(){
				return this.plane == "CONTROL" ? 'Control plane' : 'Data plane';
			};
			settings.display_state = function(){
				return this.state == "NORMAL" ? "ok" : "malfunction";
			};
			return settings;
		}
	});
};

let addElementController = function(){
	let pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		viewModel:async function(pod){
			let roles = new Metadata({"scope":"roles"});
			let platforms = new Platforms();
			pod.roles = await roles.load();
			pod.roles = pod.roles.map(role => ({"value":role.role_name,"label":role.display_name}));
			pod.platforms = await platforms.load();
			pod.platforms = pod.platforms.map(platform => ({"value":`[${platform.vendor_name}][${platform.model_name}]`,
															"label":`${platform.vendor_name} ${platform.model_name}`}));
			return pod;
		},
		buttons:{
			"add-element":function(){
				let element = new Element({"scope":"settings"});
				// Let page controller handle all element errors
				this.attach(element);
				// Create a new element
				let submission = this.getViewModel("element");
				submission.group_id = this.getViewModel("group_id");
				submission.group_type = this.getViewModel("group_type");
				submission.group_name = this.getViewModel("group_name");
				submission.operational_state = "DOWN";
				submission.administrative_state="NEW";

				let platform = this.input("platform").value();
				let segments = /\[(.*)\]\[(.*)\]/.exec(platform);
				let vendorName = segments[1];
				let modelName  = segments[2];
				submission.platform = {
						"model_name":modelName,
						"vendor_name":vendorName
				};
				
				//FIXME Select multivalue field

				
				
				
				element.createElement(this.location().params(),
									  submission);
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/pod/pod-elements.html",
						   "?":this.location().params()});
		}
	});
};


let podController = function(){
	let pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		buttons:{
			"save-group":function(){
				pod.saveSettings(this.location().params(),
				                 this.getViewModel());
			},
			"remove-pod":function(){
				pod.removePod(this.location().params());
			}
		},
		onRemoved:function(){
			this.navigate({"view":"/ui/views/inventory/pods.html"});	
		}
	});
};


let podLocationController = function(){
	let pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		buttons:{
			"save-location":function(){
				pod.saveSettings(this.location().params(),
				                 this.getViewModel());
			},
			"lookup":function(){

				let location = this.getViewModel("location");
				if(location){
					let coord = new Json("https://nominatim.openstreetmap.org?format=json&q="+location);
					coord.onLoaded = this.newEventHandler(function(matches){
						// Attempt to resove geolocation of given address...
						if(matches && matches.length > 0){
							this.updateViewModel({"geolocation":{"latitude":parseFloat(matches[0].lat),
																 "longitude":parseFloat(matches[0].lon)}});
						} else {
							this.updateViewModel({"geolocation":null});
						}
						// ... and update view.
						this.render();
					});
					coord.load();
					
				} else {
					// Remove geolocation from view model...
					this.updateViewModel({"location":null,
										  "geolocation":null});
					// ... and update view.
					this.render();
				}
			}
		}
	});
}

let podRacksController = function(){
	let racks = new Pod({"scope":"racks"});
	return new Controller({resource:racks,
					 buttons:{
					   "add-rack":function(){
						   this.navigate({"view":"new-rack.html",
							   			  "?": this.location().params()});
					   }	 
					 }});
}

let addRackController = function(){
	let pod = new Pod({"scope":"settings"});
	return new Controller({resource:pod,
					 buttons:{
		            	 "add-rack":function(){
		            		 let settings = {
		            			"rack_name":this.input("rack_name").value(),
		            			"units":this.input("units").value(),
		            			"location":this.input("location").value(),
		            			"description":this.input("description").value()
		            		 };
		            		 let rack = new Rack();
		            		 this.attach(rack);
		            		 let params = this.location().params();
		            		 params.rack = settings.rack_name;
		            		 rack.saveSettings(params,
		            				 		   settings);
		            	 }
		             },
		             onSuccess:function(){
		            	 this.navigate({"view":"pod-racks.html",
		            		 			"?":this.location().params()});
		             }});
}


let rackController = function(){
	let rack = new Rack();
	return new Controller({resource:rack,
					 buttons:{
		            	 "save-rack":function(){
		            		 let settings = {
		            			"rack_name":this.input("rack_name").value(),
		            			"units":this.input("units").value(),
		            			"location":this.input("location").value(),
		            			"description":this.input("description").value()
		            		 };
		            		 rack.saveSettings(this.location().params(),
		            				 		  settings);
		            	 },
		            	 "remove-rack":function(){
		            		 this.navigate({"view":"confirm-remove-rack.html",
		            			 			"?":this.location().params()});
		            	 },
		            	 "confirm-remove":function(){
		            		 rack.removeRack(this.location().params());
		            	 }
		             },
		             onSuccess:function(){
		            	 this.navigate({"view":"pod-racks.html",
		            		 			"?":this.location().params()});
		             }});
}


let podMenu = {
	"master":podController(),
	"details" : {
		"confirm-remove-pod.html" : podController()
	}
};

let podRacksMenu = {
	"master" : podRacksController(),	
	"details" : { "new-rack.html":addRackController(),
				  "rack.html":rackController(),
				  "confirm-remove-rack.html":rackController()}	
};

let podElementsMenu = {
	"master" : podElementsController(),
	"details": { "new-element.html" : addElementController() }
};

export const menu = new Menu({
	"pod.html" : podMenu,
	"pod-elements.html" : podElementsMenu,
	"pod-location.html" : podLocationController(),
	"pod-racks.html":podRacksMenu
});

