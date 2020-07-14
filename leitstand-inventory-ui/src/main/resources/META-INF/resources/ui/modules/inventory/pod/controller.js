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
import {Control} from '/ui/js/ui-components.js';
import '../inventory-components.js';


class PodLocation extends Control {
	
	connectedCallback(){
		const id = `${this.id}-container`;
		this.innerHTML = `<div id="${id}" style="display:block; clear: both; position:relative; width:750px; height:600px; margin:auto; background-color: #EFEFEF"></div>`;
		const geolocation = this.viewModel.getProperty(this.binding);
		if(geolocation){
	      const map = new ol.Map({
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
	                      src: '/ui/modules/inventory/pod/pod.png'
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


const podElementsController = function(){
	const pod = new Pod({"scope":"elements"});
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

const addElementController = function(){
	const pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		buttons:{
			"add-element":function(){
				const element = new Element({"scope":"settings"});
				// Let page controller handle all element errors
				this.attach(element);
				// Create a new element
				const submission = this.getViewModel("element");
				submission.group_id = this.getViewModel("group_id");
				submission.group_type = this.getViewModel("group_type");
				submission.group_name = this.getViewModel("group_name");
				submission.operational_state = "DOWN";
				submission.administrative_state="NEW";
				const settings = this.getViewModel();
				const platform = this.input("element-platform").unwrap();
				settings.platform_id = platform.selected.value;
				settings.platform_name = platform.selected.label;
				element.createElement(this.location.params,
									  submission);
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/pod/pod-elements.html",
						   "?":this.location.params});
		}
	});
};


const podController = function(){
	const pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		buttons:{
			"save-group":function(){
				pod.saveSettings(this.location.params,
				                 this.getViewModel());
			},
			"remove-pod":function(){
				pod.removePod(this.location.params);
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/pods.html"});	
		}
	});
};


const podLocationController = function(){
	const pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		buttons:{
			"save-location":function(){
				pod.saveSettings(this.location.params,
				                 this.getViewModel());
			},
			"lookup":async function(){

				const location = this.getViewModel("location");
				if(location){
					const coord = new Json("https://nominatim.openstreetmap.org?format=json&q="+location);
					const matches = await coord.load();
					// Attempt to resolve geolocation of given address...
					if(matches && matches.length > 0){
						this.updateViewModel({"geolocation":{"latitude":parseFloat(matches[0].lat),
															 "longitude":parseFloat(matches[0].lon)}});
					} else {
						this.updateViewModel({"geolocation":null});
					}
					// ... and update view.
					this.renderView();
					
				} else {
					// Remove geolocation from view model...
					this.updateViewModel({"location":null,
										  "geolocation":null});
					// ... and update view.
					this.renderView();
				}
			}
		}
	});
}

const podRacksController = function(){
	const racks = new Pod({"scope":"racks"});
	return new Controller({resource:racks,
					 buttons:{
					   "add-rack":function(){
						   this.navigate({"view":"new-rack.html",
							   			  "?": this.location.params});
					   }	 
					 }});
}

const addRackController = function(){
	const pod = new Pod({"scope":"settings"});
	return new Controller({resource:pod,
					 buttons:{
		            	 "add-rack":function(){
		            		 const settings = {
		            			"rack_name":this.input("rack_name").value(),
		            			"units":this.input("units").value(),
		            			"location":this.input("location").value(),
		            			"description":this.input("description").value()
		            		 };
		            		 const rack = new Rack();
		            		 this.attach(rack);
		            		 const params = this.location.params;
		            		 params.rack = settings.rack_name;
		            		 rack.saveSettings(params,
		            				 		   settings);
		            	 }
		             },
		             onSuccess:function(){
		            	 this.navigate({"view":"pod-racks.html",
		            		 			"?":this.location.params});
		             }});
}


const rackController = function(){
	const rack = new Rack();
	return new Controller({resource:rack,
					 buttons:{
		            	 "save-rack":function(){
		            		 const settings = {
		            			"rack_name":this.input("rack_name").value(),
		            			"units":this.input("units").value(),
		            			"location":this.input("location").value(),
		            			"description":this.input("description").value()
		            		 };
		            		 rack.saveSettings(this.location.params,
		            				 		  settings);
		            	 },
		            	 "remove-rack":function(){
		            		 this.navigate({"view":"confirm-remove-rack.html",
		            			 			"?":this.location.params});
		            	 },
		            	 "confirm-remove":function(){
		            		 rack.removeRack(this.location.params);
		            	 }
		             },
		             onSuccess:function(){
		            	 this.navigate({"view":"pod-racks.html",
		            		 			"?":this.location.params});
		             }});
}


const podMenu = {
		"master":podController(),
		"details" : {
			"confirm-remove-pod.html" : podController()
		}
};

const podRacksMenu = {
		"master" : podRacksController(),	
		"details" : { "new-rack.html":addRackController(),
					  "rack.html":rackController(),
					  "confirm-remove-rack.html":rackController()}	
};

const podElementsMenu = {
		"master" : podElementsController(),
		"details": { "new-element.html" : addElementController() }
};

export const menu = new Menu({
	"pod.html" : podMenu,
	"pod-elements.html" : podElementsMenu,
	"pod-location.html" : podLocationController(),
	"pod-racks.html":podRacksMenu
});

