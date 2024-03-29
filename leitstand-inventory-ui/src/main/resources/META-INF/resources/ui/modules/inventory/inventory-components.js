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
import {Select,UIElement,Control,html} from '/ui/js/ui-components.js';
import {Metadata,Platforms,Facilities,Panel, Pod} from '/ui/modules/inventory/inventory.js';
import {Element} from '/ui/js/ui-dom.js';

class PlatformSelector extends Select {
	
	options(){
		const platforms = new Platforms();
		return platforms.load()
				 		.then(platforms => {
				 		            const options = [{"label":"-","value":""}]
				 					return options.concat(platforms.map(p => ({"value":p.platform_id, "label":p.platform_name})));
				 				});
	}
	
	   
    nooptions(){
        this.innerHTML=`<ui-blankslate>
                            <ui-title>No platforms found</ui-title>
                            <ui-note>Please <a href="/ui/views/settings/platform/platforms.html" title="Manage platforms">add a platform</a> before you continue adding a first element.</ui-note>
                        </ui-blankslate>`;
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
	
	nooptions(){
	    this.innerHTML=`<ui-blankslate>
	                        <ui-title>No roles found</ui-title>
	                        <ui-note>Please <a href="/ui/views/settings/role/roles.html" title="Manage roles">add a role</a> before you continue adding a first element.</ui-note>
	                    </ui-blankslate>`;
	}

}
customElements.define("element-role",ElementRoleSelector);

class TerminalPanel extends UIElement {
    
    constructor(){
        super();
    }
    
    renderDom(){
        this.addEventListener('click',evt => {
            if (evt.target.nodeName == 'BUTTON'){
                evt.preventDefault();
                evt.stopPropagation();
                const options = this.querySelector("ui-select");
                window.open(options.selected.value);                
            }
        });
        
        const module = document.querySelector("ui-module");
        
        module.addEventListener('UIRenderMenu',evt => {
            const params = evt.detail.viewModel
            const group = params["group_id"];
            if (group) {
                // Load all element settings to discover SSH endpoints.
                // Render a drop-down list and open terminal button for all elements with SSH endpoints.
                // Do not display a drop-down list if not SSH endpoints exist.
                const pod = new Pod({"scope":"elements"});
                pod.load({"group":group})
                   .then(pod => {
                       // Select all elements with configured SSH endpoint
                       const options = pod.elements
                                          .filter(element => element.mgmt_interfaces && element.mgmt_interfaces.SSH)
                                          .map(element => html `<ui-option default="${ (element.element_id == params['element_id']) ? 'true' : 'false'}" value="/ui/terminal/$${element.mgmt_interfaces.SSH.mgmt_hostname}?title=$${element.element_name}">$${element.element_name}</ui-option>`)
                                          .reduce((a,b)=>a+b,'')
                       if(options) {
                           document.querySelector('element-quick-links').innerHTML=`<ui-select name="element_selected"><ui-label></ui-label>${options}<ui-button>Open terminal</ui-button></ui-select>`
                       }
                   })
                  
            } else {
                document.querySelector('element-quick-links').innerHTML = ''
            }
        });
                    
   }
    
}

customElements.define('element-quick-links', TerminalPanel);

class InventoryPanel extends UIElement {
    renderDom(){
        const panel = new Panel();
        const params = this.location.params;
        params.panel = this.getAttribute('panel');
        panel.load(params)
             .then(elementPanels => {
                 const panels = elementPanels.panels
                 let panel = panels[0];
                 if(panels && panels.length){
                     let defaultPanel = null;
                     if(panels.length > 1){
                         const namedPanels = {};
                         panels.forEach(p => {
                             namedPanels[p.panel_name]=p;
                             if(p.default_panel){
                                 panel = p;
                             }
                         });                     
                         
                         this.innerHTML = `<ui-select label="Metric" small class="right" name="panel">${panels.map(panel => html `<ui-option value="$${panel.panel_name}" ${panel.default_panel ? "default" : ""} >$${panel.panel_title}</ui-option>`).reduce((a,b)=>a+b,'')}</ui-select>
                                           <div class="panel">
                                           </div>`;
                         this.addEventListener("change",(evt) => {
                             const selected = evt.target.options[evt.target.selectedIndex].value;
                             const panel = namedPanels[selected];
                             this.querySelector("div.panel").innerHTML = html `<iframe src="$${panel.uri}" width="${panel.width||'100%'}" height="$${panel.height||'400px'}" frameborder="0" style="border: 1px solid #E7E7E7"/></iframe>`;
                         });
                         this.querySelector("div.panel").innerHTML = html `<iframe src="${panel.uri}" width="$${panel.width||'100%'}" height="$${panel.height||'400px'}" frameborder="0" style="border: 1px solid #E7E7E7"/></iframe>`;
                     } else {
                         this.innerHTML = html `<iframe src="${panel.uri}" width="$${panel.width||'100%'}" height="$${panel.height||'400px'}" frameborder="0" style="border: 1px solid #E7E7E7"/></iframe>`;
                     }
                 }
             })
             .catch(e => console.log(e));
    }
}

customElements.define("element-panel",InventoryPanel);


class AdministrativeState extends UIElement {
    renderDom(){
        const state = this.viewModel.getProperty("administrative_state");
        this.innerHTML=html `<span class="right inventory administrative $${state}">$${state}</span>`;
    }
}
customElements.define("inventory-administrative-state",AdministrativeState);

class OperationalState extends UIElement {
    renderDom(){
        const adm = this.viewModel.getProperty("administrative_state");
        const ops = this.viewModel.getProperty("operational_state");
        if(ops && adm){
            this.innerHTML=html `<span class="right $${ops}">$${ops}</span>`;
        }
    }
}
customElements.define("inventory-operational-state",OperationalState);



class FileUpload extends Control {
    
    constructor(){
        super();
        
        const readFile = (file) => {
            const reader = new FileReader(file);
            reader.onload = (evt) => {
                this._content = evt.target.result || '';
                this.innerHTML=html `<div>
                                       <div id="preview.content" style="max-height: 400px; width: 100%; overflow: scroll">
                                         <ui-code>$${this._content}</ui-code>
                                       </div>
                                       <ui-actions>
                                         <ui-button small name="dismiss">Dismiss</ui-button>
                                       </ui-actions>
                                     </div>`;
                if(this._onload){
                    this._onload(file,this._content);
                }
            };
            reader.onerror = (evt) => {
                this.input('config_name').value('');
                this.innerHTML= html `<div>
                                        <div id="preview.content" style="height: 400px; width: 100%; overflow: scroll">
                                          <ui-code>Cannot read file $${file.name} (${evt.target.error.code})</ui-code>
                                        </div>
                                        <ui-actions>
                                          <ui-button small name="dismiss">Dismiss</ui-button>
                                        </ui-actions>
                                      </div>`;                
                if(this._onerror){
                    this._onerror(file);
                }
            };
            reader.readAsText(file);
        };
        
        this.addEventListener('dragenter',(evt)=>{
          evt.preventDefault();
          evt.stopPropagation();
          this.querySelector("div").style.backgroundColor='#efefef';
        });
        
        this.addEventListener('dragover',(evt)=>{
            evt.preventDefault();
            evt.stopPropagation();
            this.querySelector('div').style.backgroundColor='#efefef';
        });
        
        this.addEventListener('dragleave',(evt)=>{
            evt.preventDefault();
            evt.stopPropagation();
            this.querySelector('div').style.backgroundColor='white';
        });
          
        this.addEventListener('drop',(evt)=>{
            evt.preventDefault();
            evt.stopPropagation();
            this.querySelector('div').style.backgroundColor='white';
            const dt = evt.dataTransfer
            const files = dt.files
            if(files.length > 1){
                this.querySelector("div.error").classList.remove("hidden");
                return;
            }
            this._file = files[0];
            readFile(this._file);
        });
        
        this.addEventListener('change',(e)=>{
           this._file = this.querySelector('input').files[0];
           readFile(this._file);
        });
        
        this.addEventListener('click', (e) => {
           if(e.target.name=='dismiss'){
              e.preventDefault();
              e.stopPropagation();
              this.renderDom();
              if(this._onreset){
                  this._onreset();
              }
           } 
        });
        
        this.addEventListener('UIPreExecuteAction',(e) => {
            this.viewModel.setProperty(this.binding,this._content);
        });
        
    }
    
    renderDom(){
        this.innerHTML=`<div style="border: 2px dashed #ccc; width: 100%; height: 300px; text-align:center;">
                          <div style="margin-top:125px">
                           <ui-note>Drop your configuration file onto the dashed region or select the configuration file in the file dialog.</ui-note>
                           <input type="file" style="display:none" id="fileElem">
                           <label class="btn btn-outline" style="display:inline-block; margin: auto;" for="fileElem">Select configuration file</label>
                           <div class="hidden error" style="color: #b33630; font-weight:bold; margin-top: 15px">Please drop a single file only.</div>
                          </div>
                        </div>`;
    }
    
    get file(){
        return this._file;
    }
    
    get content(){
        return this._content;
    }
    
    onload(onload){
        this._onload = onload;
    }
    
    onerror(onerror){
        this._onerror = onerror;
    }
    
    onreset(onreset){
        this._onreset = onreset;
    }
    
}
customElements.define('inventory-upload',FileUpload);
