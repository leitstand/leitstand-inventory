/*
 *  (c) RtBrick, Inc - All rights reserved, 2015 - 2017
 */
import {Controller,Menu} from '/ui/js/ui.js';
import {Pod,Pods,PhysicalInterfaces,Elements} from './inventory.js'
import {Events} from '/ui/modules/event/events.js';

let podsController = function() {
	let pods = new Pods();
	return new Controller({
		resource:pods,
		viewModel:function(items){
			let filter = this.location().param("filter");
			return{"pods"	: items,
				   "filter" : filter};
		},
		postRender:function(){
			// Fetch open error counts for all pods and add a link to events if errors exists.
			let events = new Events({"scope":"counts"});
			events.onLoaded = this.newEventHandler(function(stats){
				this.elements("tbody tr").forEach(function(group){
					let name = group.select("td:nth-child(1)").text();
					if(stats[name]){
						let count = stats[name]["count"];
						if(count){
							group.select(".hidden .counter").html(count);
							group.select(".hidden").css().remove("hidden");
						}
					}
				});
			});
			events.load(this.location().params());
		},
		buttons:{
			"filter-pods":function(){
				this.reload({"filter":this.input("filter").value()});
			}
		}
	});
};


let ifpsController = function() {
	let ifps = new PhysicalInterfaces();
	return new Controller({
		resource:ifps,
		viewModel:function(items){
			let filter = this.location().param("filter");
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

let addPodController = function(){
	let pods = new Pods();
	return new Controller({
		resource:pods,
		buttons:{
			"create-group":function(){
				let group = {"group_name":this.input("group_name").value(),
							 "description":this.input("description").value() };
				
				pods.createPod(this.location().params(),
							   group);
			}
		},
		onSuccess:function(){
			this.navigate({"view":"/ui/views/inventory/pods.html"})
		}
	});
}


let elementsController = function() {
	let elements = new Elements();
	return new Controller({
		resource:elements,
		viewModel:function(items){
			let filter = this.location().param("filter");
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

let podsMenu = {
		"master":podsController(),
		"details":{
			"new-pod.html":addPodController()
		}
};

export const menu = new Menu({"pods.html" : podsMenu,
							  "elements.html":elementsController(),
							  "ifps.html":ifpsController()},
							  "/ui/views/inventory/pods.html");