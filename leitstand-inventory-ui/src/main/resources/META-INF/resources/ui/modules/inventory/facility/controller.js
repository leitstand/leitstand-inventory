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
import {Control,UIElement} from '/ui/js/ui-components.js';
import {Json} from '/ui/js/client.js';
import {Facilities, Facility, Racks, Pod} from '../inventory.js';

class FacilityLocation extends Control {
	
	constructor(){
		super();
		this.addEventListener('click',(evt) => {
			if(evt.target.tagName!='BUTTON'){
				return;
			}
			evt.stopPropagation();
			evt.preventDefault();
			const location = this.viewModel.getProperty("location");
			if(location){
				const coord = new Json('https://nominatim.openstreetmap.org?format=json&q='+location);
				coord.load()
					 .then(matches => {
						 // Attempt to resolve geolocation of given address...
						 if(matches.length > 0){
							 this.viewModel.setProperty('geolocation',{'latitude':parseFloat(matches[0].lat),
								 				  					   'longitude':parseFloat(matches[0].lon)});
						 } else {
							 this.viewModel.removeProperty('geolocation');
						 }
						 this.controller.renderView();
						 
					 });
				
			} else {
				// Remove geolocation from view model...
				this.viewModel.removeProperty('location');
				this.viewModel.removeProperty('geolocation');
				this.controller.renderView();
			}
		});
	}
	
	connectedCallback(){
		const id = `${this.id}-container`;
		if(this.readonly){
			this.innerHTML = `<div id="${id}" style="display:block; clear: both; position:relative; width:750px; height:600px; margin:auto; background-color: #EFEFEF"></div>`;			
		} else {
			this.innerHTML = `<ui-input name="location">
								<ui-label>Location</ui-label>
								<ui-note>The location of the facility</ui-note>
								<ui-button name="lookup">Lookup</ui-button>
							  </ui-input>
							  <div id="${id}" style="display:block; clear: both; position:relative; width:750px; height:600px; margin:auto; background-color: #EFEFEF"></div>
							  <div class="row">
								<div class="one-half column">
									<ui-input name="longitude" bind="geolocation.longitude" readonly>
										<ui-label>Longitude</ui-label>
									</ui-input>
								</div>
								<div class="one-half column" >
								<ui-input name="latitude" bind="geolocation.latitude" readonly>
									<ui-label>Latitude</ui-label>
								</ui-input>
								</div>
							  </div>`;
		}
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
		                      src: '/ui/modules/inventory/facility/facility.png'
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
customElements.define('facility-location',FacilityLocation);

class FacilityType extends UIElement {
		
	renderDom(){
		const type = this.getAttribute('value');
		if(type == 'DC'){
			this.innerHTML = 'Data Center';
		} else if(type == 'CO') {
			this.innerHTML = 'Central Office';
		} else if (type == 'SC') {
			this.innerHTML = 'Street Cabinet'
		}
	}
}
customElements.define('facility-type',FacilityType);


const facilitiesController = function(){
	const facilities = new Facilities();
	return new Controller({
		resource:facilities,
		viewModel:function(facilities){
			return {'facilities':facilities,
					'filter':this.location.param('filter')};
		},
		buttons:{
			'filter':function(){
				this.reload({'filter':this.input('filter').value()});
			}
		}
	});
};

const addFacilityController = function(){
	const facilities = new Facilities();
	return new Controller({
		resource:facilities,
		viewModel:function(){
			return {};
		},
		buttons:{
        	 'add-facility':function(){
        		 const facility = this.getViewModel();
        		 facilities.add(this.location.params,facility);
        	 }

		},
		onSuccess:function(){
			this.navigate("facilities.html");
		}
	});
};


const facilitySettingsController = function(){
	const facility = new Facility();
	return new Controller({
		resource:facility,
		buttons:{
			'save-facility':function(){
				facility.saveSettings(this.location.params,
									  this.getViewModel());
			},
		    'remove-facility':function(){
		        facility.removeFacility(this.location.params);
		    },
		    'lookup':async function(){

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
		},
		onSuccess:function(){
			this.navigate('facilities.html');
		}
	});
};

const facilityRacksController = function(){
	const facility = new Facility();
	return new Controller({
			resource:facility,
			viewModel:async function(settings){
				const racks = new Racks();
				settings.racks = await racks.load(this.location.params);
				settings.filter = this.location.param('filter');
				return settings;
			},
			buttons:{
				'filter':function(){
					this.reload({'filter':this.input('filter').value(),
								 'facility':this.location.param('facility')});
				}
			}
		});	
};	

const podLocationController = function(){
	const pod = new Pod({'scope':'settings'});
	return new Controller({
		resource:pod,
		viewModel: async function(pod){
			if(pod.facility_id){
				const facility = new Facility();
				pod.facility = await facility.load({'facility':pod.facility_id});
				return pod;
			}
			return pod;
		}
	});
}

const facilitiesMenu = {
	'master' : facilitiesController(),
	'details': {
		'new-facility.html':addFacilityController()
	}
}

const facilityMenu = {
	'master' : facilitySettingsController(),
	'details': { 'confirm-remove.html' : facilitySettingsController()}
};


export const menu = new Menu({
	'facilities.html' : facilitiesMenu,
	'facility.html' : facilityMenu,
	'facility-racks.html' : facilityRacksController(),
	'pod-facility.html':podLocationController()
});


