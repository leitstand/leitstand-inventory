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
						let activeElements = '';
						let totalElements = '';
						if(groupElements && groupElements.total){
							totalElements = `<a href="pod/pod-elements.html?group=${groupId}" class="btn btn-sm btn-default" title="Show element list">${groupElements.total} in total</a>`
							if(groupElements && groupElements.active){
								activeElements = `<a href="topology/link-state.html?group=${groupId}" class="btn btn-sm btn-primary" title="Show link-state graph">${groupElements.active} active</a>`
							}
						} else {
							totalElements = `<a href="pod/pod-elements.html?group=${groupId}">No elements.</a>`
						}
						element.html(activeElements + " "+totalElements);
					});
				});
		},
		buttons:{
			"filter-pods":function(){
				this.reload({"filter":this.input("filter").value()});
			}
		}
	});
};


const ifpsController = function() {
	const ifps = new PhysicalInterfaces();
	return new Controller({
		resource:ifps,
		viewModel:function(items){
			const filter = this.location.param("filter");
			return {"ifps"	: items,
					"filter" : filter};
			
		},
		buttons:{
			"filter":function(){
				this.reload({"filter":this.input("filter").value()});
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
}


const elementsController = function() {
	const elements = new Elements();
	return new Controller({
		resource:elements,
		viewModel:function(items){
			const filter = this.location.param("filter");
			return {"elements"	: items,
					"filter" : filter};
		},
		buttons:{
			"filter":function(){
				this.reload({"filter":this.input("filter").value()});
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
