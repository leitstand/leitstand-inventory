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
import {Element} from '/ui/modules/inventory/inventory.js';
import {UIElement,Control,html} from '/ui/js/ui-components.js';
import '../inventory-components.js';
class Diff extends UIElement {
	
	connectedCallback(){

		try{
		const source = this.viewModel.getProperty(this.getAttribute('source'));
		const target = this.viewModel.getProperty(this.getAttribute('target'));
		if(source && target){

			const read = function(c){
				if(c && c.content_type=='application/json'){
					return JSON.stringify(c.config,null,' ');
				}
				return c.config;
			};
			
			source.content = read(source);
			target.content = read(target);
			
			const diff = JsDiff.diffLines(target.content,
										  source.content)
					           .map(part => html `<span style="background-color:${part.added ? 'Lime' : (part.removed ? '#ffd6cc' : 'None')}">$${part.value}</span>`)
					           .reduce((a,b)=>a+b);
			this.innerHTML=`<div id="comparator">
							    <div>
							        <div style="background-color:lime; font-weight:bold; font-size: 1em; padding: 5px;">Added to $${target.config_name} at <ui-date readonly dateTime>${target.date_modified}</ui-date></div> 
							        <div style="background-color: #ffd6cc; font-weight: bold; font-size: 1em; padding: 5px;">Removed from $${source.config_name} from <ui-date dateTime readonly>${source.date_modified}</ui-date></div>
							    </div>
							    <code id="diff" class="hl" style="height: 45em; overflow: scroll"><pre>${diff}</pre></code>
							</div>`;
			hljs.highlightBlock(this.querySelector('#diff'));

		}
		}catch(e){
			console.log(e);
		}
	}
}

class Editor extends Control {
    connectedCallback(){
        const config = this.viewModel.getProperty(this.binding);
        this.innerHTML=`<textarea>${JSON.stringify(config,null,' ')}</textarea>`;
        const editor = CodeMirror.fromTextArea(this.querySelector("textarea"), {
            lineNumbers: true,
            styleActiveLine: true,
            matchBrackets: true,
            mode:{name:'javascript', json:true}
        });
        this.form.addEventListener('UIPreExecuteAction',() => {
            this.viewModel.setProperty(this.binding,JSON.parse(editor.getValue()));
        });
    }
}

customElements.define('config-diff',Diff);
customElements.define('config-editor',Editor);

const elementConfigsController = function(){
	const configs = new Element({scope:"configs"});
	return new Controller({
		resource: configs,
		viewModel:function(configs){
			configs.filter = this.location.param("filter");
			return configs;
		},
		buttons: {
			"filter" : function(){
				const params = this.location.params;
				const filter = this.input("filter").value();
				this.reload(Object.assign(params,{"filter":filter}));
			}
		}
	});
};
	
const elementConfigController = function(){
	const config = new Element({scope:"configs/{{config}}"});
	return new Controller({
		resource: config,
		viewModel:function(config){
			config.removable = function(){
				return this.config_state == 'CANDIDATE' || this.config_state == 'SUPERSEDED';
			};
			
			config.restorable = function(){
				return this.config_state == 'SUPERSEDED';
			};
			
			config.content = function(){ 
	  			if(this.content_type == "application/json"){
	  				return JSON.stringify(this.config,null,"  ");
	  			}
	  			return this.config;
			};
			return config;
		},
		buttons: {
			"confirm-remove":function(){
				config.remove(this.location.params);
			}
		},
		onCreated: function(location){
			// Extract config-id from redirect location
			const params = this.location.params;
			params.config=location.substring(location.lastIndexOf('/')+1);
			this.navigate({"view":"element-config.html",
						   "?":this.location.params});
		},
		onRedirect: function(location){
			// Extract config-id from redirect location
			const params = this.location.params;
			params.config=location.substring(location.lastIndexOf('/')+1);
			this.navigate({"view":"element-config.html",
						   "?":params});
		},
		onSuccess: function(){
			this.navigate({"view":"element-config-history.html",
						   "?": {"group":this.location.param("group"),
							     "element":this.location.param("element"),
							     "config":this.getViewModel("config_name")}})
		}
	});
};


const addElementConfigController = function(){
	const element = new Element({scope:'settings'});
	let content = '';
	let fileName = '';
	return new Controller({
		resource: element,
		postRender:function(){
		    const upload = this.element('inventory-upload').unwrap();
		    upload.onload((file)=>{
		        this.element('#save-config').enable();
                const configName = this.input('config_name');
                if(!configName.value()){
                   configName.value(file.name);
                }
                this.input("content_type").value(file.type);
                this.element('#save-config').enable();
		    });
		    upload.onerror((file)=>{
		        this.input('config_name').value('');
		        this.element('#save-config').disable();
		    });
		    upload.onreset((file)=>{
                this.element('#save-config').disable();
                const configName = this.input('config_name');
                if(configName.value() == file.name){
                    configName.value('');
                }
		    });
		},
		buttons: {
			"save-config":function(){
			    const params = this.location.params;
			    const config = {
			        config_name : this.input('config_name').value(),
			        comment: this.input('comment').value(),
			        content_type : this.input('content_type').value(),
			        content : this.input('inventory-upload').unwrap().content
			    };
				element.saveConfig(Object.assign(this.location.params,config.config_name),
								   config);
			}
		},
		onSuccess : function(){
			this.navigate({"view":"element-configs.html",
						   "?": this.location.params});
		}
	});
};

const elementConfigHistoryCompareController = function(){
	const source = new Element({scope:"configs/{{a}}"});
	return new Controller({
		resource: source,
		viewModel:async function(source){
			const compare = source;
			// Add a back reference to improve template readability.
			compare.source = source;
			// Load the target configuration to be compared with the selected configuration
			const target = new Element({scope:"configs/{{b}}"});
			compare.target = await target.load(this.location.params);
			return compare;
		}
	});
};
	
const elementConfigHistoryController = function(){
	const configs = new Element({scope:"configs/{{config}}"});
	return new Controller({
		resource: configs,
		viewModel:function(settings){
			let a = 0;
			settings["check-first"] = function(){
				if(a++==0){
					return "checked";
				}
			};
			settings["check-second"] = function() {
				if(a == 2){
					return "checked";
				}
			};
			return settings;
		},
		buttons:{
			compare: function(){
				const params = this.location.params;
				params.a = this.element("input[name='a']:checked").value();
				params.b = this.element("input[name='b']:checked").value();
				this.navigate({"view":"/ui/views/inventory/config/element-config-history-compare.html",
							   "?":params});
			}
		},
		onNotFound: function(location){
			this.navigate({"view":"element-configs.html",
						   "?":this.location.params});
		}
	});
};

const configsMenu = {
	"master":elementConfigsController(),
	"details":{ "element-config-history.html" : elementConfigHistoryController(),
				"element-config.html" : elementConfigController(),
				"confirm-remove.html" : elementConfigController(),
				"confirm-restore.html" : elementConfigController(),
				"add-config.html" : addElementConfigController(),
				"element-config-history-compare.html":elementConfigHistoryCompareController()}
};
	
export const menu = new Menu({"element-configs.html" : configsMenu});
