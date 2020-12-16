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
import {Pod,Metadata,Platforms,Element,Rack,Facilities} from '/ui/modules/inventory/inventory.js';
import {Control,html} from '/ui/js/ui-components.js';
import '../inventory-components.js';


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
						    				${facilities.map(facility => html `<tr>
							    									             <td class="text">
							    										           <label>
							    											         <input type="radio" 
							    											                name="facility_id" 
							    											                value="${facility.facility_id}" 
							    											                data-facility-name="$${facility.facility_name}"  
							    											                ${ facilityId == facility.facility_id && 'checked' }>
							    											         &nbsp;$${facility.facility_name}
							    										           </label>
							    									             </td>
							    									             <td class="text">$${facility.facility_type||'-'}</td> 
							    								                 <td class="text">$${facility.location||'-'}</td> 
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
customElements.define('pod-facility',FacilitySelector);


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
			"save-pod":function(){
				pod.saveSettings(this.location.params,
				                 this.getViewModel());
			},
			"change-facility":function(){
				pod.saveSettings(this.location.params,
								 this.getViewModel())
				   .then(()=>{
					   this.navigate({"view":"/ui/views/inventory/pod/pod-facility.html",
						   			  "?":this.location.params})
				   });
			},
			"remove-pod":function(){
				pod.removePod(this.location.params);
			}
		},
		onRemoved:function(){
		    this.navigate({'view':'/ui/views/inventory/pods.html'});
		}
	});
};

const podFacilityController = function(){
	const pod = new Pod({"scope":"settings"});
	return new Controller({
		resource:pod,
		buttons:{
			"filter":function(){
				const params = this.location.params;
				params.filter = this.input("filter").value();
				this.reload(params);
			},
			"save-pod":function(){
				pod.saveSettings(this.location.params,
				                 this.getViewModel());
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/pod/pod.html",
						   "?":this.location.params});	
		}
	});
};



const podMenu = {
		"master":podController(),
		"details" : {
			"confirm-remove-pod.html" : podController(),
			"pod-facility.html":podFacilityController()
		}
};

const podElementsMenu = {
		"master" : podElementsController(),
		"details": { "new-element.html" : addElementController() }
};

export const menu = new Menu({
	"pod.html" : podMenu,
	"pod-elements.html" : podElementsMenu
});

