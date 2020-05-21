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
import {Select,UIElement} from '/ui/js/ui-components.js';
import {Metadata,Platforms,Facilities,Panel} from '/ui/modules/inventory/inventory.js';

class PlatformSelector extends Select {
	
	options(){
		const platforms = new Platforms();
		platforms.onNotFound=function(){
		    console.log("No panel found!");
		}
		return platforms.load()
				 		.then(platforms => {
				 					return platforms.map(platform => { 
				 							return {"value":platform.platform_id,"label":platform.platform_name}
				 					});
				 				});
	}
	
}
customElements.define("element-platform",PlatformSelector);

class ElementRoleSelector extends Select {

	options(){
		const roles = new Metadata({'scope':'roles'});
		return roles.load()
				 	.then(roles => {
				 			return roles.map(role => { 
				 							return {"value":role.role_name,"label":role.display_name}
				 					});
				 			});
	}

}
customElements.define("element-role",ElementRoleSelector);

class InventoryPanel extends UIElement {
    renderDom(){
        const panel = new Panel();
        const params = this.location.params;
        params.panel = this.getAttribute('panel');
        panel.load(params)
             .then(elementPanels => {
                 const panels = elementPanels.panels
                 if(panels && panels.length){
                     this.innerHTML = panels.map(panel => `<iframe src="${panel.uri}" width="${panel.width||'100%'}" height="${panel.height||'400px'}" frameborder="0" style="border: 1px solid #E7E7E7"/></iframe>`).reduce((a,b)=>a+b,'');
                 }
             })
             .catch(e => console.log(e));
    }
}

customElements.define("inventory-panel",InventoryPanel);
