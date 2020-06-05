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
import {Metadata} from '/ui/modules/inventory/inventory.js';
import {Images,Image} from './image.js';

const imagesController = function(){
	const images = new Metadata({"scope":"images"});
	return new Controller({
		resource:images,
		viewModel:async function(settings){ //TODO Refactor with view migration
			const viewModel = settings;
			viewModel.filter = { "filter":this.location.param("filter"),
								 "image_type":this.location.param("image_type"),
								 "image_version":this.location.param("image_version"),
								 "image_state":this.location.param("image_state")};
			viewModel.image_types = [{"label":"All image types",
							   		  "value":""},
							   		 {"label":"ONL Images",
							   		  "value":"ONL"},
							   		 {"label":"LXC Images",
							   		  "value":"LXC"}];

	  		const images = new Images();
	  		viewModel.images = await images.load(viewModel.filter);
	  		viewModel.element_display_name = function(){
	  			return this["element_name"] ? this["element_name"] : "*";
	  		}
	  		return viewModel;
		},
		buttons:{
			"filter":function(){
				this.reload(this.getViewModel("filter"));
			}
		}
	})
};
	
const imageController = function(){
	const image = new Image();
		return new Controller({
			resource:image,
			viewModel: function(viewModel){
				viewModel.revoked = function(){
					return this.image_state == "REVOKED";
				};
				return viewModel;
			},
			buttons:{
				"apply-state":function(){
					let params = this.location.params;
					params["image_state"] = this.input("[name='image_state']").value();
					image.updateState(params);
				},
				"purge":function(){
					image.purgeCaches(this.location.params);
				},
				"save-settings":function(){
					image.saveSettings(this.location.params,this.getViewModel());
				}
			},
			onSuccess: function(){
				this.reload();
			}
		});
	};

const imageStatisticsController = function(){
	const imageStats = new Image({"scope":"statistics"});
	return new Controller({
			resource: imageStats,
			viewModel:function(stats){
				const pods = {};
				let totalActiveCount = 0;
				let totalCacheCount  = 0;
				let podCount = 0;
				if (stats.active_count){
					for(const podName in stats.active_count){
						let pod = pods[podName];
						if(!pod){
							pod = {"active_count":0,
								   "cached_count":0};
							pods[podName]= pod;
							podCount++;
						}
						pod.active_count = stats.active_count[podName];
						totalActiveCount += pod.active_count;
					}
				}
				if(stats.cached_count){
					for(const podName in stats.cached_count){
						let pod = pods[podName];
						if(!pod){
							pod = {"active_count":0,
								   "cached_count":0};
							pods[podName]= pod;
							podCount++;
						}
						pod.cached_count = stats.cached_count[podName];
						totalCacheCount += pod.cached_count;
					}
				}
				const images = [];
				for(const podName in pods){
					const pod = pods[podName];
					pod["group_name"] = podName;
					images.push(pod);
				}
				const viewModel = stats.image;
				viewModel["pod_count"]=podCount;
				viewModel["active_count"]=totalActiveCount;
				viewModel["cached_count"]=totalCacheCount;
				viewModel["total_count"]=(totalActiveCount+totalCacheCount);
				viewModel["images"]=images;
				return viewModel;
			}
	});
}
	
const imagesMenu = {
	"master" : imagesController(),
	"details"  : {
		"image.html" : imageController()
	}
}

export const menu = new Menu({"images.html" : imagesMenu,
							  "image-meta.html" : imageController(),
							  "image-pkgs.html" : imageController(),
							  "image-apps.html" : imageController(),
							  "image-state.html" : imageController(),
							  "image-stats.html" : imageStatisticsController()},
							  "/ui/views/image/images.html");
