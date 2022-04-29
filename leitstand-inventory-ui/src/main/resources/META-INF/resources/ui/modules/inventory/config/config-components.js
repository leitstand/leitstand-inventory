
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
import {UIElement,Control,html} from '/ui/js/ui-components.js';

class Diff extends UIElement {
	
	processDiff(diff, lines, content){

		// Line number counter and utility function for incrementing the line counter and emitting the line numbers.
		let line = 1; 
		const writeBlockLineNumbers = function(blockSize, cssClass, prefix){
			if (!prefix){
				prefix='';
			}
			const pre = document.createElement("pre")
			pre.className=cssClass;
			let text =''; 
			let n =''; // Prepend all lines with new line (start with empty string) to 
					   // not append new line after the last block line number.
			for (let i=0; i < blockSize; i++){
				text+=(n+prefix+line+':');
				n='\n';
				line++;
			}
			pre.innerText = text;
			lines.appendChild(pre);
		}
		
		const writeBlockLines = function(blockContent, cssClass){
			const pre = document.createElement("pre");
			if (cssClass){
				pre.className=cssClass;
			}
			pre.innerText=blockContent;
			content.appendChild(pre);
		}
	
		// Iterate over all changes.
		for (let i=0; i < diff.length; i++){
			const block = diff[i]
			// Process block line numbers.
			if (block.added){
				// Write line numbers for all added blocks.
				writeBlockLineNumbers(block.count,'a','++'); // 'a' class indicates that this block has been added
				writeBlockLines(block.value,'a');
			} else if (block.removed){
				// Write "--" for all removed line numbers without incrementing the line counter.
				// The line counter refers to the target file.
				const pre = document.createElement("pre");
				pre.className='d'; // 'd' class indicates that this block has been deleted
				let text ='';
				let n =''; // Prepend all lines with new line (start with empty string) to 
						   // not append new line after the last block line number.
				for (let i=0; i < block.count; i++){
					text+=(n+'--:');
					n='\n';
				}
				pre.innerText = text;
				lines.appendChild(pre);
				
				writeBlockLines(block.value,'d');	
			} else {
				// 10 line blocks are always displayed and collapsing a block of 11 lines does not make sense
				// because the link to expand a block also consumes one line. Set block size limit to 12 lines
				// to collapse at least 2 lines.
				if (block.count < 12) {
					writeBlockLineNumbers(block.count); // No class indicates that this block has not been modified.
					writeBlockLines(block.value);
				} else {
					const blockLines = block.value.split('\n');
						
					// Write first 5 block lines
					writeBlockLineNumbers(5);
					writeBlockLines(blockLines.slice(0,5).reduce((a,b)=>a+'\n'+b));
		
					const expandedLines = document.createElement("pre");
					expandedLines.innerText = blockLines.slice(5,block.count-5).reduce((a,b)=>a+'\n'+b);

					const expandCollapsedLines = (evt) => {
						evt.stopPropagation();
						evt.preventDefault();
						content.replaceChild(expandedLines,control);
						let expandedLineNumbers = "";
						let n = '';
						for (let i=offset; i < offset+block.count-10; i++){
							expandedLineNumbers+=(n+i+':');
							n='\n';
						}
						collapsedLineNumbers.outerHTML = "<pre>"+expandedLineNumbers+"</pre>";
					};
					// Create placeholder for collapsed lines and control to display collapsed lines
					const offset = line;
					line += (block.count-10); // Increment line counter by hidden lines.
					const collapsedLineNumbers = document.createElement("a");
					collapsedLineNumbers.innerHTML = "+"+(block.count-10)+":";
					collapsedLineNumbers.addEventListener('click',expandCollapsedLines);
					lines.appendChild(collapsedLineNumbers);
		
					const control = document.createElement("span");
					const button = document.createElement("a");
					control.appendChild(button);
					button.href='#'
					button.addEventListener('click', expandCollapsedLines);
					button.innerText="Show "+(block.count-10)+" hidden lines";
			
					content.appendChild(control);
					
					// Write tailing five block lines.
					writeBlockLineNumbers(5);
					writeBlockLines(blockLines.slice(block.count - 5).reduce((a,b)=>a+'\n'+b));
				}
			}
		}
	}
	
	connectedCallback(){

		try{
			const source = this.viewModel.getProperty(this.getAttribute('source'));
			const target = this.viewModel.getProperty(this.getAttribute('target'));
			if(source && target){

				const read = function(c){
					if(c && c.content_type=='application/json'){
						return JSON.stringify(JSON.parse(c.config),null,' ');
					}
					return c.config;
				};
				
				source.content = read(source);
				target.content = read(target);

				this.innerHTML=html `<div class="compare">
								       <div>
								        <div class="a" style="font-weight:bold; font-size: 1em; padding: 5px;">Added to $${target.config_name} at <ui-date readonly dateTime>${target.date_modified}</ui-date></div> 
								        <div class="d"  style="font-weight: bold; font-size: 1em; padding: 5px;">Removed from $${source.config_name} from <ui-date dateTime readonly>${source.date_modified}</ui-date></div>
								       </div>
								       <div id="diff">
								       		<div class="lines"></div>
								       		<div class="content"></div>
								       </div>
								     </div>`;
				this.requires({'stylesheet':'/ui/modules/inventory/config/diff.css'})
					.then( () => {
						const diff = JsDiff.diffLines(source.content,target.content);
						// Lookup DOM containers for line number and configuration content.
						const lines = document.querySelector('#diff .lines')
						const content = document.querySelector('#diff .content')
						// Render diff between both configurations.
						this.processDiff(diff,lines,content);				
					})				     
	
			}
		} catch(e) {
			console.log(e);
			this.controller.error(""+e)
		}
	}
}

class Editor extends Control {
    connectedCallback(){
        const config = this.viewModel.getProperty(this.binding)||{};
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



