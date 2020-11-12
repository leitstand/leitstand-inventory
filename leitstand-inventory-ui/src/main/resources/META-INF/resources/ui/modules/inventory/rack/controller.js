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
import {Select,Control,UIElement} from '/ui/js/ui-components.js';
import {Racks,Rack,RackItem,Elements,Facilities,Element} from '../inventory.js';
import '../inventory-components.js';

class RackPosition extends Select {
	constructor(){
		super();
	}
	
	options(){
		const units = this.viewModel.getProperty('units');
		const position = this.location.param('position');
		const options = [];
		for(let i=1; i < units+1; i++){
			options.push({'value':i,
						  'label':i+'U', 
						  'default':position == i ? true : false});
		}
		return Promise.resolve(options);
	}
}
customElements.define('rack-item-position',RackPosition);


class RackView extends UIElement {
	
	constructor(){
		super();
	}
	
	renderDom(){
		// Implement rendering.

		const rackId = this.viewModel.getProperty('rack_id');
		const units = this.viewModel.getProperty('units');
		const items = {};
		this.viewModel.getProperty('items').forEach((item) => {
			items[item.position] = item;
		});
		const ascending = this.viewModel.getProperty('ascending');
		this.requires({'stylesheet':'/ui/modules/inventory/rack/rack.css'})
		     .then(()=>{
		    	 const renderRackItem = function(position){
		    		 const item = items[position];
		    		 const lblUnit = ascending ? units-position+1 : position;
		    		 if(item){
		    			 if(item.element_id){
		    				 return `
		    				 <div>
		    				 	<div>
		    			 	  		<div class="unit">
		    			 	  			${lblUnit}
		    			 	  		</div>
		    			 	  		<div class="item">
		    			 	  			<div class="ru${item.height}">
		    				 				 <a href="rack-item.html?rack=${rackId}&unit=${item.position}" title="Show rack item details"> ${item.element_name} ${item.element_alias ? `(${item.element_alias})` :'' }</a>
		    			 	  				 <span class="admin ${item.administrative_state}">${item.administrative_state}</span>
		    			 	  				 
		    			 	  			</div>
		    			 	  		</div>
		    			 	  		<div class="unit">
		    			 	  			${lblUnit}
		    			 	  		</div>
		    				 	</div>		
	    			 		 </div>`
		    			 }
		    			 return `<div>
	    			 	  <div class="unit">
	    			 	  	${lblUnit}
	    			 	  </div>
	    			 	  <div class="item">
	    			 	  	<div class="ru${item.height}">
	    			 	      <a href="rack-item.html?rack=${rackId}&unit=${item.position}" title="Show rack item details">${item.rack_item_name}</a>
	    			 	  	</div>
	    			 	  </div>
	    			 	  <div class="unit">
	    			 	  	${lblUnit}
	    			 	  </div>
	    			    </div>`
		    		}
		    		 
		    		return `<div>
		    			 	  <div class="unit">
		    			 	  	${lblUnit}
		    			 	  </div>
		    			 	  <div class="item free"><ui-button small href="add-rack-item.html?rack=${rackId}&position=${position}">Add item</ui-button></div>
		    			 	  <div class="unit">
		    			 	  	${lblUnit}
		    			 	  </div>
		    			    </div>`;
		
		    	};
		    	
		    	let itemsHtml = '';
		    	for(let position = units; position > 0; position--){
		    		itemsHtml += renderRackItem(position);
		    	}
		    	 
				this.innerHTML=`
					<div class="rack">
						<div>
							<div class="unit">Unit</div>
							<div class="frame">${this.viewModel.getProperty('rack_name')}</div>
							<div class="unit">Unit</div>
						</div>
						${itemsHtml}
						<div>
							<div class="unit">Unit</div>
							<div class="frame">${this.viewModel.getProperty('rack_name')}</div>
							<div class="unit">Unit</div>
						</div>
					</div>`;
		     });
	}
}

customElements.define('rack-items',RackView);

class FacilitySelector extends Control{
	renderDom(){
		const facilityId = this.viewModel.getProperty("facility_id");
		const facilities = new Facilities({'filter':this.location.param('filter')});
		facilities.load()
		    	  .then(facilities => {
		    			    	
		    		  this.innerHTML=`<table class="list">
		    			  				<thead>
						    				<tr>
												<th class="text">Name</th>
												<th class="text">Type</th>
												<th class="text">Location</th>
						    				</tr>
						    			</thead>
							    		<tbody>
						    				${facilities.map(facility => `<tr>
							    									   <td class="text">
							    										 <label>
							    											<input type="radio" name="facility_id" value="${facility.facility_id}" data-facility-name="${facility.facility_name}"  ${ facilityId == facility.facility_id && 'checked' }>
							    											&nbsp;${facility.facility_name}
							    										</label>
							    									   </td>
							    									   <td class="text">${facility.facility_type||''}</td> 
							    								       <td class="text">${facility.location||''}</td> 
							    								     </tr>`)
		    			  								.reduce((a,b)=>a+b,'')}
						    			</tbody>
						    		</table>`;
		    
		    });
		    
		    
		this.addEventListener('change',(evt)=>{
			this.viewModel.setProperty("facility_id",evt.target.value);
			this.viewModel.setProperty("facility_name",evt.target.getAttribute("data-facility-name"));
		});
	}
}
customElements.define('rack-facility',FacilitySelector);


class RackItemElement extends UIElement{
	
	constructor(){
		super();
	}
	
	renderDom(){
		this.innerHTML=`<ui-input name="filter">
							<ui-label>Search</ui-label>
							<ui-note>Search an element by name or alias.</ui-note>
							<ui-button name="search">Search</ui-button>
						</ui-input>
						<div class="result">
							<ui-blankslate>
								<ui-title>No elements found!</ui-title>
								<ui-note>No matching elements found.</ui-note>
							</ui-blankslate>	
						</div>`
		
		
		this.addEventListener('click',(evt)=>{
			if(evt.target.tagName != 'BUTTON'){
				return;
			}
			evt.stopPropagation();
			evt.preventDefault();
			const elements = new Elements();
			elements.load({'filter':this.querySelector("input[name='filter']").value,'limit':20})
					.then(elements => {
						const list = this.querySelector('div.result');
						if(elements.length > 0){
							list.innerHTML=`<table class="list">
										   	 <thead>
											   <tr>
												 <th class="text">Element</th>
												 <th class="text">Alias</th>
												 <th class="text">Role</th>
												 <th class="text">Platform</th>
												 <th class="text">Serial Number</th>
											   </tr>
											 </thead>
											 <tbody>
											 </tbody>
										   </table>`;
							
							const tbody = list.querySelector('tbody');
							tbody.innerHTML = elements.map(element => 
														   `<tr>
																<td class="text"><label><input type="radio" name="element" value="${element.element_id}" data-name="${element.element_name}">&nbsp; ${element.element_name}</label></td>
																<td class="text">${element.element_alias||'-'}</td>
																<td class="text">${element.element_role}</td>
																<td class="text">${element.platform_name||'-'}</td>
																<td class="text">${element.serial_number||'-'}</td>
															</tr>`)
													  .reduce((a,b)=>a+b,'');
						} else {
							list.innerHTML="<ui-blankslate><ui-title>No elements found!</ui-title><ui-note>No matching elements found.</ui-note></ui-blankslate>";
						}
						
	
					});

		});
		
	}
}
customElements.define("rack-item-element",RackItemElement);



const racksController = function(){
	const racks = new Racks();
	return new Controller({
		resource:racks,
		viewModel:function(racks){
			return {'racks':racks,
					'filter':this.location.param('filter')};
		},
		buttons:{
			'filter':function(){
				this.reload({'filter':this.input('filter').value()});
			}
		}
	});
};

const addRackController = function(){
	const racks = new Racks({'scope':'settings'});
	return new Controller({
		resource:racks,
		viewModel:function(){
			return {};
		},
		buttons:{
        	 'add-rack':function(){
        		 const rack = this.getViewModel();
        		 racks.add(this.location.params,rack);
        	 }
		},
		onSuccess:function(){
			this.navigate('racks.html');
		}
	});
};


const rackSettingsController = function(){
	const rack = new Rack({'scope':'settings'});
	return new Controller({
		resource:rack,
		buttons:{
			'save-rack':function(){
				rack.saveSettings(this.location.params,
	            				  this.getViewModel());
			},
			'change-facility':function(){
				const params = this.location.params;
				rack.saveSettings(params,
      				  			  this.getViewModel())
      				.then(()=>{
      					this.navigate({'view':'rack-facility.html',
      								   '?':params});
      				});
			},
		    'remove-rack':function(){
		    	const params = this.location.params;
		    	params.force = this.input('force').value();
		        rack.removeRack(params);
		    }
		},
		onSuccess:function(){
			this.navigate('racks.html');
		}
	});
};

const rackFacilityController = function(){
	const rack = new Rack({'scope':'settings'});
	return new Controller({
		resource:rack,
		buttons:{
			'filter':function(){
				const params = this.location.params;
				params.filter = this.input("filter").value();
				this.reload(params);
			},
			'save-rack':function(){
				rack.saveSettings(this.location.params,
	            				  this.getViewModel());
			}
		},
		onSuccess:function(){
		    this.navigate({'view':'rack-settings.html',
		    		       '?' : this.location.params});
		}
	});
};

const rackItemsController = function(){
	const rack = new Rack({'scope':'items'});
	return new Controller({
		resource:rack
	});
};

const addRackItemController = function(){
	const rack = new Rack({'scope':'settings'});
	return new Controller({
		resource:rack,
		viewModel:function(rack){
			rack.position = this.location.param('position');
			rack.type = 'element';
			rack.element = function(){
				return rack.type == 'element';
			};
			rack.other = function(){
				return rack.type != 'element';
			}
			return rack;
		},
		buttons:{
			'add-item':function(){
				const type = this.element("input[name='type']:checked").value();
				if(type == 'element'){
					const element = this.element("input[name='element']:checked");
					const item = {
						'position':this.input('position').value(),
						'face': this.input('face').value(),
						'element_id': element && element.value() || null,
						'element_name': element && element.getAttribute('data-name')
					};
					rack.addRackItem(this.location.params,item);
				} else {
					const item = {
						'position':this.input('position').value(),
						'rack_item_name': this.input('rack_item_name').value(),
						'height': this.input('height').value(),
						'face': this.input('face').value(),
					};
					rack.addRackItem(this.location.params,item);
				}

			}
		},
		selections:{
			'type':function(type){
				this.viewModel.setProperty('type',type);
				this.render();
			}
		},
		onSuccess:function(){
			this.navigate({'view':'rack-items.html',
						   '?':this.location.params});
		}
	});
};

const rackItemController = function(){
	const item = new RackItem();
	return new Controller({
		resource:item,
		viewModel: function(item){
			item.element = function(){
				return !!item.element_id;
			};
			item.other = function(){
				return !item.element_id;
			}
			return item;
		},
		buttons:{
			'remove-rack-item':function(){
				item.removeRackItem(this.location.params);
			},
			'save-rack-item':function(){
				const type = this.input("type").value();
				if(type == 'element'){
					const element = this.input("input[name='element']:checked");
					const settings = {
						'position':this.input('position').value(),
						'face': this.input('face').value(),
						'element_id': element && element.value() || null,
						'element_name': element && element.getAttribute('data-name')
					};
					item.saveSettings(this.location.params,item);
				} else {
					const settings = {
						'position':this.input('position').value(),
						'rack_item_name': this.input('rack_item_name').value(),
						'height': this.input('height').value(),
						'face': this.input('face').value(),
					};
					item.saveSettings(this.location.params,item);
				}
			
			}
		},
		onSuccess:function(){
			this.navigate({'view':'rack-items.html',
						   '?':this.location.params});
		}
	});
};

const elementRackItemController = function(){
	const element = new Element({'scope':'settings'})
	return new Controller({
		resource:element,
		viewModel: async function(settings){
			const racks = new Racks({'scope':'_findElement'});
			try {
				const item = await racks.load(this.location.params);
				settings.rack = item;
				return settings;
			} catch (e) {
				// Item not found
				return settings;
			}
		},
	});
}

const racksMenu = {
	'master' : racksController(),
	'details': {
		'new-rack.html':addRackController()
	}
}

const rackMenu = {
	'master' : rackSettingsController(),
	'details': { 'confirm-remove-rack.html' : rackSettingsController(),
				 'rack-facility.html' : rackFacilityController()}
};

const itemsMenu = {
	'master' : rackItemsController(),
	'details': { 'rack-item.html' : rackItemController(),
				 'add-rack-item.html' : addRackItemController(),
				 'edit-rack-item.html' : rackItemController(),
			     'confirm-remove-rack-item.html' : rackItemController()}
};

export const menu = new Menu({
	'racks.html' : racksMenu,
	'rack-settings.html' : rackMenu,
	'rack-items.html' : itemsMenu,
	'element-rack-item.html' : elementRackItemController()
});


