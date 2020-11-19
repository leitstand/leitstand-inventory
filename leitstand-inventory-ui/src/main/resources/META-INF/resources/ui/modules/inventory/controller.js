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
import {Pod,Pods,PhysicalInterfaces,Elements} from './inventory.js'
import './inventory-components.js';

const podsController = function() {
	const pods = new Pods();
	return new Controller({
		resource:pods,
		viewModel:function(items){
			const filter = this.location.param("filter");
			return {"pods"	: items,
				    "filter" : filter};
		},
		postRender:function(){
			const pods = new Pods({'scope':'_statistics'});
			pods.load()
				.then(stats => {
					const groups = {};
					stats.forEach(stat => {
						let active = 0;
						for(const state in stat.active_elements){
							active += stat.active_elements[state];
						}
						const total = active + stat.new_elements + stat.retired_elements;
						groups[stat.group_id] = {active:active,total:total};
					});
					const elements = this.elements(".pod-elements").forEach(element => {
						const groupId = element.getAttribute("data-group");
						const groupElements = groups[groupId];
						if(groupElements && groupElements.total){
						    if(groupElements.total > 1){
						        element.html(`<a href="pod/pod-elements.html?group=${groupId}" title="Show element list">${groupElements.total} elements.</a>`);
						    } else {
		                        element.html(`<a href="pod/pod-elements.html?group=${groupId}" title="Show element list">1 element.</a>`);
						    }
						} else {
							element.html(`<a href="pod/pod-elements.html?group=${groupId}" title="Show element list">No elements.</a>`);
						}
					});
				});
		},
		buttons:{
			"filter":function(){
				this.reload({"filter":this.input("filter").value()});
			}
		}
	});
};


const ifpsController = function() {
	const ifps = new PhysicalInterfaces();
	return new Controller({
		resource:ifps,
		viewModel:function(items,response){
			const facility = this.location.param("facility");
			const ifp = this.location.param("ifp");
			const limit  = parseInt(response.headers.get("Leitstand-Limit"));
			const offset = parseInt(response.headers.get("Leitstand-Offset"));
			const eof = (response.headers.get("Leitstand-Eof") == "true");
			const size = parseInt(response.headers.get("Leitstand-Size"));
			const limitExceeded = (((offset + size) > limit) || !eof);
			
			return {"ifps"	: items,
					"facility" : facility,
					"ifp":ifp,
					"offset":offset,
					"eof":eof,
					"limit":limit,
					"exceeded":limitExceeded,
					"no_match":!!(facility||ifp)};
			
		},
		buttons:{
			"filter":function(){
				this.reload({"facility":this.input("facility").value(),
				             "ifp":this.input("ifp").value()});
			}
		}
	});
};

const addPodController = function(){
	const pods = new Pods();
	return new Controller({
		resource:pods,
		viewModel:function(){
			return {};
		},
		buttons:{
			"create-group":function(){
				pods.createPod(this.location.params,
							   this.getViewModel());
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/pods.html"})
		}
	});
};

const elementsController = function() {
	const elements = new Elements();
	return new Controller({
		resource:elements,
		viewModel:function(items){
			const by = this.location.param("by") || "name";
			return { "elements"	: items,
					 "filter" : this.location.params,
					 "render_description":function(){
						return by == "name" || by == "ntag";
					 },
					 "render_serial":function(){
						return by == "serial";
				 	 },
					 "render_assetid":function(){
						return by == "assetid";
					 },
					 "render_mgmt_ip":function(){
						return by == "ip";
					 },
					 "management_interface_list":function(){
						 const ifcs = [];
						 for(const ifc in this.mgmt_interfaces){
							 ifcs.push(this.mgmt_interfaces[ifc]);
						 }
						 return ifcs;
					 }};
		},
		buttons:{
			"filter":function(){
				this.reload({"filter":this.input("filter").value(),
							 "by":this.input("by").value()});
			}
		}
	});
};

const podsMenu = {
		"master":podsController(),
		"details":{
			"new-pod.html":addPodController()
		}
};

export const menu = new Menu({"pods.html" : podsMenu,
							  "elements.html":elementsController(),
							  "ifps.html":ifpsController()},
							  "/ui/views/inventory/pods.html");
