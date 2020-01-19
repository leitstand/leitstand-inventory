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
import {UIElement,Control} from '/ui/js/ui-components.js';

class Diff extends UIElement {
	
	connectedCallback(){

		try{
		let source = this._viewModel.getProperty(this.getAttribute('source'));
		let target = this._viewModel.getProperty(this.getAttribute('target'));
		if(source && target){

			let read = function(c){
				if(c && c.content_type=='application/json'){
					return JSON.stringify(c.config,null,' ');
				}
				return c.config;
			};
			
			source.content = read(source);
			target.content = read(target);
			
			let diff = JsDiff.diffLines(source.content,
					target.content)
					.map(part => `<span style="background-color:${part.added ? 'Lime' : (part.removed ? '#ffd6cc' : 'None')}">${part.value||''}</span>`)
					.reduce((a,b)=>a+b);
			this.innerHTML=`<div id="comparator">
								<div class="row">
									<div class="cell column one-half" >
									<h3 style="color: black; background-color: #ffd6cc; padding-left: 1em;"><ui-date readonly>${source.date_modified}</ui-date></h3>
										<ui-code>${source.content}</ui-code>
									</div>
									<div class="cell column one-half">
										<h3 style="color: black; background-color: lime; padding-left: 1em;" ><ui-date readonly>${target.date_modified}</ui-date></h3>
										<ui-code>${target.content}</ui-code>
									</div>
								</div>
								<div class="row">
									<div class="single-column cell" style="clear:both;">
										<h3 style="color: black; background-color: #cedaed; padding-left: 1em; margin-top: 10px">Diff</h3>
										<code id="diff" class="hl"><pre>${diff}</pre></code>
									</div>
								</div>
							</div>
							<p class="text-center note">
								Click on a listing to enlarge.
							</p>`;
			hljs.highlightBlock(this.querySelector('#diff'));
			this.addEventListener('click',function(evt){
				let div = this.eventSource().up('div');
				if(div.css().contains('cell')){
					if(div.style.width=='100%'){
						this.elements('#comparator div.cell').forEach(function(element){
							element.css().remove('hidden');
						});
						div.style.width=null;
					} else {
						this.elements('#comparator div.cell').forEach(function(element){
							element.css().add('hidden');
						});
						div.css().remove('hidden');
						div.style.width='100%';
					}				
				}
				
			}.bind(this._page));

		}
		}catch(e){
			alert(JSON.stringify(e));
			console.log(e);
		}
	}
}

class Editor extends Control {
	connectedCallback(){
		let config = this._viewModel.getProperty(this._binding);
		let editorpanel = document.createElement('div');
		if(this.hasAttribute('style')){
			editorpanel.setAttribute('style',this.getAttribute('style'));
		}
		let options = {'mode':'code'};
	    let editor = new JSONEditor(editorpanel, options);
	    editor.set(config);
		this.appendChild(editorpanel);
		this.form.addEventListener('UIPreExecuteAction',function(){
			this._viewModel.setProperty(this._binding,editor.get());
		}.bind(this));
	}
	
}

customElements.define('config-diff',Diff);
customElements.define('config-editor',Editor);

let elementConfigsController = function(){
	let configs = new Element({scope:"configs"});
	return new Controller({
		resource: configs,
		buttons: {
			"filter" : function(){
				this.error("Not yet implemented!");
			}
		}
	});
};
	
let elementConfigController = function(){
	let config = new Element({scope:"configs/{{config}}"});
	let editor = null;
	return new Controller({
		resource: config,
		viewModel:function(config){
			config.removable = function(){
				return this.config_state == 'CANDIDATE' || this.config_state == 'SUPERSEDED';
			};
			
			config.restorable = function(){
				return this.config_state == 'SUPERSEDED';
			};
			
			config.active = function() {
				return this.config_state == 'ACTIVE' && this.content_type == 'application/json';;
			};
			
			config.editable = function() {
				return this.config_state == 'CANDIDATE' && this.content_type == 'application/json';
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
				config.remove(this.location().params());
			},
			"confirm-edit":function(){
				config.editConfig(this.location().params(),
								  this.input("comment").value());
			},
			"save-config":function(){
				config.saveConfig(this.location().params(),
								  {"config_name":this.getViewModel("config_name"),
								   "comment":this.getViewModel("comment"),
								   "content":JSON.stringify(this.getViewModel("config"))});
			}
		},
		onCreated: function(location){
			// Extract config-id from redirect location
			let params = this.location().params();
			params.config=location.substring(location.lastIndexOf('/')+1);
			this.navigate({"view":"/ui/views/inventory/config/element-config.html",
						   "?":params});
		},
		onRedirect: function(location){
			// Extract config-id from redirect location
			let params = this.location().params();
			params.config=location.substring(location.lastIndexOf('/')+1);
			alert("Got a redirect notification. Proceed at: "+location);
			this.navigate({"view":"/ui/views/inventory/config/element-config.html",
						   "?":params});
		},
		onSuccess: function(){
			this.navigate({"view":"/ui/views/inventory/config/element-config-history.html",
						   "?": {"group":this.location().param("group"),
							     "element":this.location().param("element"),
							     "config":this.getViewModel("config_name")}})
		}
	});
};

let elementConfigHistoryCompareController = function(){
	let source = new Element({scope:"configs/{{a}}"});
	return new Controller({
		resource: source,
		viewModel:async function(source){
			let compare = source;
			// Add a back reference to improve template readability.
			compare.source = source;
			// Load the target configuration to be compared with the selected configuration
			let target = new Element({scope:"configs/{{b}}"});
			compare.target = await target.load(this.location().params());
			return compare;
		}
	});
};
	
let elementConfigHistoryController = function(){
	let configs = new Element({scope:"configs/{{config}}"});
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
				let params = this.location().params();
				params.a = this.elememt("input[name='a']:checked").value();
				params.b = this.radio("input[name='b']:checked").value();
				this.navigate({"view":"/ui/views/inventory/config/element-config-history-compare.html",
							   "?":params});
			}
		}
	});
};

let configsMenu = {
	"master":elementConfigsController(),
	"details":{ "element-config-history.html" : elementConfigHistoryController(),
				"element-config.html" : elementConfigController(),
				"element-config-editor.html":elementConfigController(),
				"confirm-remove.html" : elementConfigController(),
				"confirm-restore.html" : elementConfigController(),
				"confirm-edit.html" : elementConfigController(),
				"element-config-history-compare.html":elementConfigHistoryCompareController()}
};
	
export const menu = new Menu({"element-configs.html" : configsMenu});
