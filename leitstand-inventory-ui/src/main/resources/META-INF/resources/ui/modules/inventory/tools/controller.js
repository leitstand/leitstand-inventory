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


import {Menu,Controller} from '/ui/js/ui.js';
import {UIElement, html} from '/ui/js/ui-components.js';
import {Element} from '/ui/modules/inventory/inventory.js';
import '../inventory-components.js';

class ToolList extends UIElement{
	constructor(){
		super();
	}
	
	renderDom(){
		const element = this.controller.getViewModel();
		const tools = this.view.module.computeMenuViewModel(element,m => m.menu=='element-tools')[0];
		if (tools.items.length > 0){
			this.innerHTML=`
			<ui-group>
			<ui-label>Tools</ui-label>
			<ui-note>Select a tool from the list below:</ui-note>
			<table class="list">
				<caption>List of available tools.</caption>
				<thead>
					<tr><th class="text">Tool</th><th class="text">Description</th></tr>
				</thead>
				<tbody>
					${tools.items.map(item => html `<tr>
					                                  <td class="text"><a href="${item.viewpath}" 
					                                                      title="$${item.title}" 
					                                                      ${item.target ? `target="${item.target}"` :''}>$${item.label}</a></td>
					                                  <td class="text">$${item.config.description}</td>
					                                </tr>`).reduce((a,b)=>a+b,'')}
				</tbody>
			</table>
			</ui-group>`;
		} else {
			this.innerHTML=html `<ui-blankslate>
								   <ui-title>No tools found</ui-title><ui-note>No tools registered for element $${element.element_name}</ui-note>
							     </ui-blankslate>`
		}
	}
	
}

customElements.define('element-tools',ToolList);

const toolsController = function(){
	const element = new Element({'scope':'settings'})
	return new Controller({
		resource:element
	});
};


export const menu = new Menu({'element-tools.html' : toolsController()});

