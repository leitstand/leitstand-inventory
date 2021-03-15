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
import {Select,Control,html} from '/ui/js/ui-components.js';
import {units} from '/ui/js/widgets.js';
import {Metadata,Element,Pod,Pods,ElementPhysicalInterfaces,ElementPhysicalInterface,ElementLogicalInterfaces,ElementLogicalInterface,Platforms,TimeSeries} from '/ui/modules/inventory/inventory.js';
import '../inventory-components.js';

class AdministrativeStateSelector extends Select {
	constructor(){
		super();
	}
	
	options(){
		return Promise.resolve([{"value" : "NEW", "label" : "New"},
				  				{"value" : "ACTIVE", "label" : "Active"},
				  				{"value" : "RETIRED", "label" : "Retired"}]);
	}
}
customElements.define("element-administrative-state",AdministrativeStateSelector);

class OperationalStateSelector extends Select {
	constructor(){
		super();
	}
	
	options(){
		return Promise.resolve([{"value" : "DOWN", "label" : "Down"},		
								{"value" : "UP",  "label" : "Up"}, 
								{"value" : "DETACHED", "label" : "Detached"}, 
								{"value" : "MAINTENANCE", "label" : "Maintenance"}]);
	}
}
customElements.define("element-operational-state",OperationalStateSelector);

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
		postRender:function(){
			// Intercept all clicks on rendered links in order to save changes before leaving to "management interfaces" or "move to pod view";
			const form = this.element("ui-form");
			form.addEventListener("click",(evt) => {
				if(evt.target.nodeName === 'A'){
					const platform = this.input("element-platform").unwrap();
                    if(platform){
                        evt.preventDefault();
                        evt.stopPropagation();
                        const settings = this.getViewModel();
                        settings.platform_name = platform.selected.label;
                        element.saveSettings(this.location.params,settings)
                               .then(() => { this.navigate(evt.target.href)});                      
                    }
				}
			});
		},
		buttons:{
			"save-element":function(){
				const settings = this.getViewModel();
				settings.platform_name = this.input("element-platform").unwrap().selected.label;
				element.saveSettings(this.location.params,
				                     settings);
			},
			"confirm-remove-element":function(){
			    this.navigate({'view':'confirm-remove-element.html',
			                   '?':this.location.params});
			},
			"remove-element":function(){
			    const params = this.location.params;
				params.force = this.input("force").value();
				element.removeElement(params);
			},
			"add-mgmt":function(){
				// Store view model to avoid loosing unsaved changes!
				const settings = this.getViewModel();
				settings.platform_name = this.input("element-platform").unwrap().selected.label;
				element.saveSettings(this.location.params,settings)
					   .then(() => { this.navigate({"view" : "/ui/views/inventory/element/element-mgmt.html",
							   						"?" : this.location.params})});
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
		viewModel:function(element){
			element.upgrade_type=function(){
				if(this.upgrade_type == "MAJOR"){
					return "Major upgrade";
				}
				if(this.upgrade_type == "MINOR"){
					return "Minor upgrade";
				}
				if(this.upgrade_type == "PATCH"){
					return "Patch";
				}
				return "Pre-Release";
			}
			return element;
		}
	});
};

const elementServicesController = function(){
	const element = new Element({"scope":"services"});
	return new Controller({
		resource:element
	});
};

class PodSelector extends Control{
	renderDom(){
		const groupId = this.viewModel.getProperty("group_id");
		const pods = new Pods({'filter':this.location.param('filter')});
		pods.load()
		    .then(pods => {
		    			    	
		    	this.innerHTML=`<table class="list">
		    			          <thead>
		    				        <tr>
								      <th class="text">Pod</th>
								      <th class="text">Description</th>
		    				        </tr>
		    			          </thead>
			    		          <tbody>
		    				        ${pods.map(pod => html `<tr>
		    									              <td class="text">
		    										            <label>
		    											          <input type="radio" 
		    											                 name="group_id" 
		    											                 value="${pod.group_id}" 
		    											                 data-group-name="$${pod.group_name}" ${ groupId == pod.group_id && 'checked' }>
		    											                 &nbsp;$${pod.group_name}
		    										            </label>
		    									              </td>
		    									              <td class="text">$${pod.description}</td> 
		    								                </tr>`)
		    					          .reduce((a,b)=>a+b,'')}
		    			          </tbody>
		    		            </table>`;
		    });
		    
		this.addEventListener('change',(evt)=>{
			this.viewModel.setProperty("group_id",evt.target.value);
			this.viewModel.setProperty("group_name",evt.target.getAttribute("data-group-name"));
		});
	}
}

customElements.define('element-pod',PodSelector);


const elementPodController = function(){
	const element = new Element({"scope":"settings"});
	return new Controller({
		resource:element,
		buttons: {
			"filter":function(){
				const params = this.location.params;
				params.filter = this.input("filter").value();
				this.reload(params);
			},
			"move-element":function(){
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
        viewModel:function(ifps){
            ifps.administrative_state=this.location.param("administrative_state");
            ifps.operational_state=this.location.param("operational_state");            
            ifps.filter=this.location.param("ifp_name");
            return ifps;
        },
        buttons:{
            "filter":function(){
                const filter = {
                    "administrative_state":this.input("administrative_state").value(),
                    "operational_state":this.input("operational_state").value(),
                    "ifp_name":this.input("filter").value(),
                    "ifp_alias":this.input("filter").value()
                };
                this.reload(Object.assign(this.location.params,filter));
            }

        },
        selections:{
            "operational_state":function(opState){
                const filter = {
                        "operational_state":opState,
                };
                this.reload(Object.assign(this.location.params,filter));
            },
            "administrative_state":function(admState){
                const filter = {
                        "administrative_state":admState,
                };
                this.reload(Object.assign(this.location.params,filter));
            }
        },
        postRender:function(){
            const ifps = this.getViewModel();
            const metrics = new TimeSeries({"metric_name":"ifp_byte_counter"});
            metrics.load(ifps)
                   .then(response => {
                            const ifps = {};
                            
                            const scale = function(value){
                                let scale = '';
                                let factor = 1000;
                                if(value > factor){
                                    value = value / factor;
                                    scale = 'k'
                                    if(value > factor){
                                        value = value / factor;
                                        scale = 'M';
                                        if(value > factor){
                                            value = value / factor;
                                            scale = 'G';
                                            if(value > factor){
                                                value = value / factor;
                                                scale = 'T';
                                                if(value > factor){
                                                    value = value / factor;
                                                    scale = 'P';
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                return {'value': scale ? value.toFixed(3) : value,
                                        'unit':scale+'bps'};
                            }
                            
                            if(response && response.metric && response.metric.metric_values){
                                response.metric.metric_values.forEach(function(metric){
                                    let ifp = ifps[metric.labels.ifp_name];
                                    if(!ifp){
                                        ifp = {};
                                        ifps[metric.labels.ifp_name] = ifp;
                                    }
                                    ifp[metric.labels.direction]=parseFloat(metric.value);
                                });
                                for( const ifp in ifps ){
                                    const rate = document.getElementById(ifp);
                                    if(rate){
                                        const inRate = scale(parseInt(ifps[ifp]["in"] ? ifps[ifp]["in"] : 0));
                                        const outRate = scale(parseInt(ifps[ifp]["out"] ? ifps[ifp]["out"] : 0));
                                        rate.innerHTML=`${inRate.value} ${inRate.unit} / ${outRate.value} ${outRate.unit}`;
                                    }
                                }
                            }
                   })
                   .catch(e => console.log(e))                 
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
		resource:ifls,
		viewModel:function(ifls){
			ifls.filter = this.location.param("filter");
			ifls.hex = function(){
				return function(tpid){
					return "0x"+this[tpid].toString(16).toUpperCase();
				}
			};
			return ifls;
		},
		buttons:{
			"filter":function(){
				const params = this.location.params;
				params.filter = this.input("filter").value();
				this.reload(params);
			}
		}
	});
};

const elementIflController = function(){
	const ifl = new ElementLogicalInterface();
	return new Controller({
		resource:ifl,
		viewModel:function(ifl){
			ifl.hex = function(){
				return function(tpid){
					return "0x"+this[tpid].toString(16).toUpperCase();
				}
			}
			return ifl;
		}
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
const elementIfpsMenu = {
	"master"  : elementIfpsController(),
	"details" : { "element-ifp.html" : elementIfpController() }
};

const elementIflsMenu = {
	"master"  : elementIflsController(),
	"details" : { "element-ifl.html" : elementIflController() }
};

const elementServicesMenu = {
    "master": elementServicesController(),
    "details": {"element-service.html" : elementServiceController()}
}

export const menu = new Menu({
		"element.html" : elementMenu,
		"element-ifls.html": elementIflsMenu,
		"element-ifps.html": elementIfpsMenu,
		"element-modules.html" :modulesMenu,
		"element-images.html" : elementImagesController(),
		"element-services.html":elementServicesMenu
 	});
