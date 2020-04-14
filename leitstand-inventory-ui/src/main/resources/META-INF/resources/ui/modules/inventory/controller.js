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
//import {Events} from '/ui/modules/event/events.js';

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
			// Fetch open error counts for all pods and add a link to events if errors exists.
			const events = new Events({"scope":"counts"});
			events.onLoaded = this.newEventHandler(function(stats){
				this.elements("tbody tr").forEach(function(group){
					const name = group.select("td:nth-child(1)").text();
					if(stats[name]){
						const count = stats[name]["count"];
						if(count){
							group.select(".hidden .counter").html(count);
							group.select(".hidden").css.remove("hidden");
						}
					}
				});
			});
			events.load(this.location.params);
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
		buttons:{
			"create-group":function(){
				const group = {"group_name":this.input("group_name").value(),
							   "description":this.input("description").value() };
				
				pods.createPod(this.location.params,
							   group);
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
