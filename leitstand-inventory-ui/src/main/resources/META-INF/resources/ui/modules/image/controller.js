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
import {Select} from '/ui/js/ui-components.js';
import '/ui/modules/inventory/inventory-components.js';


class ImageType extends Select {
	
	options(){
		const images = new Images({'scope':'_types'});
		return images
			   .load()
			   .then(types => ([{'value':'','label':'All types'}].concat(types.map(type => ({'value':type})))));
		       
	}
}
customElements.define('image-type',ImageType);

class ImageVersion extends Select {
	
	options(){
		const images = new Images({'scope':'_versions'});
		return images
			   .load()
			   .then(types => ([{'value':'','label':'All versions'}].concat(types.map(type => ({'value':type})))));
		       
	}
}
customElements.define('image-version',ImageVersion);


const imagesController = function(){
	const images = new Images();
	return new Controller({
		resource:images,
		viewModel: function(images){ //TODO Refactor with view migration
			const viewModel = {};
			viewModel.filter = { "filter":this.location.param("filter"),
								 "image_type":this.location.param("image_type"),
								 "image_version":this.location.param("image_version"),
								 "image_state":this.location.param("image_state")};
	  		viewModel.images = images;
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
			    const image = stats.image;
			    
				let totalActiveCount = 0;
				let totalCachedCount  = 0;
				let totalPullCount   = 0;
				let totalTotalCount  = 0;
				
				if(stats.groups){
				    stats.groups.forEach(group => {
				        totalActiveCount += group.active_count;
				        totalCachedCount += group.cached_count;
				        totalPullCount   += group.pull_count;
				        totalTotalCount  += group.total_count;
				    });
				}
				image.releases = stats.releases;
				image.groups = stats.groups;
				image.total_active_count = totalActiveCount;
                image.total_cached_count = totalCachedCount;
                image.total_pull_count = totalPullCount;
                image.total_total_count = totalTotalCount;
                return image;
			},
			buttons:{
			    "remove-image":function(){
			        imageStats.removeImage(this.location.params);
			    }
			},
			onRemoved:function(){
			    this.navigate({"view":"images.html"});
			}
	});
};

const groupImageStatisticsController = function(){
    const imageStats = new Image({"scope":"statistics/{{&group}}"});
    return new Controller({
        resource: imageStats,
        viewModel: function(group){
            const stats = group.image;
            stats.elements = group.elements;
            stats.group_id = group.group_id;
            stats.group_type = group.group_type;
            stats.group_name = group.group_name;
            return stats;
        }
    });
};

const imagesMenu = {
	"master" : imagesController(),
	"details"  : {
		"image.html" : imageController()
	}
};

const imageStatsMenu = {
    "master" : imageStatisticsController(),
    "details": {
        "confirm-remove-image.html":imageStatisticsController(),
        "group-image-stats.html":groupImageStatisticsController()
    }
};

export const menu = new Menu({"images.html" : imagesMenu,
							  "image-meta.html" : imageController(),
							  "image-pkgs.html" : imageController(),
							  "image-apps.html" : imageController(),
							  "image-state.html" : imageController(),
							  "image-stats.html" : imageStatsMenu},
							  "/ui/views/image/images.html");
