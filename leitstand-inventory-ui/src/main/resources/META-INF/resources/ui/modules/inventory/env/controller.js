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
import '../inventory-components.js';

class Editor extends Control {
	connectedCallback(){
		const config = this.viewModel.getProperty(this.binding);
		const editorpanel = document.createElement('div');
		if(this.hasAttribute('style')){
			editorpanel.setAttribute('style',this.getAttribute('style'));
		}
		const options = {'mode':'code'};
	    const editor = new JSONEditor(editorpanel, options);
	    if(config){
	    	editor.set(config);
	    }
		this.appendChild(editorpanel);
		this.form.addEventListener('UIPreExecuteAction',() => {
			this.viewModel.setProperty(this.binding,editor.get());
		});
	}
	
}

customElements.define('env-editor',Editor);

const elementEnvironmentsController = function(){
	const envs = new Element({scope:"environments"});
	return new Controller({
		resource: envs
	});
};
	
const elementEnvironmentController = function(){
	const env = new Element({scope:"environments/{{environment}}"});
	return new Controller({
		resource: env,
		postRender:function(){
            this.element("button[name='inline']").css.add("hidden");
		},
		buttons: {
			"save-env":function(){
			    const upload = this.input('inventory-upload').unwrap().content;
				env.saveEnvironment(this.location.params,
								    { "environment_id" : this.getViewModel("environment_id"),
									  "environment_name" : this.getViewModel("environment_name"),
									  "category" : this.getViewModel("category"),
									  "type" : this.getViewModel("type"),
									  "description" : this.getViewModel("description"),
									  "variables" : upload ? JSON.parse(upload) : this.getViewModel("variables") });
			},
			
			"confirm-remove":function(){
				env.remove(this.location.params);
			},
		    "upload":function(){
		        this.element("env-editor").css.add("hidden");
		        this.element("inventory-upload").css.remove("hidden");
		        this.element("button[name='upload']").css.add("hidden");
                this.element("button[name='inline']").css.remove("hidden");
		    },
		    "inline":function(){
                this.element("inventory-upload").css.add("hidden");
                this.element("env-editor").css.remove("hidden");
                this.element("button[name='inline']").css.add("hidden");
                this.element("button[name='upload']").css.remove("hidden");

		    }
		},
		onSuccess: function(){
			this.navigate({"view" : "element-envs.html",
						   "?" : {"group" : this.location.param("group"),
							      "element" : this.location.param("element")}})
		}
	});
};


const envsMenu = {
	"master" : elementEnvironmentsController(),
	"details" : { "element-env.html" : elementEnvironmentController(),
				  "new-element-env.html" : elementEnvironmentController(),
				  "confirm-remove.html" : elementEnvironmentController()}
};
	
export const menu = new Menu({"element-envs.html" : envsMenu});
