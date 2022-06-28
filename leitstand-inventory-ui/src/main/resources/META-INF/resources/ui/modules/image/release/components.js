import {Control, html} from '/ui/js/ui-components.js';
import {Images} from '../image.js';

class ReleaseImageSelector extends Control {

    renderDom(){

		this.innerHTML=`<ui-input name="filter">
							<ui-label>Search</ui-label>
							<ui-note>Search an image by its name or version.</ui-note>
							<ui-button name="search">Search</ui-button>
						</ui-input>
						<div class="result">
						</div>`;
		
		const imagesById = {};
		
		this.addEventListener('click',(evt)=>{
			if(evt.target.tagName != 'BUTTON'){
				return;
			}
			evt.stopPropagation();
			evt.preventDefault();
			if(evt.target.name == 'add-image'){
				const imageId = evt.target.value;
				document.querySelector("release-image-list").addImage(imagesById[imageId]);
			}
			
			const images = new Images();
			images.load({'filter':this.querySelector("input[name='filter']").value,'image_type':'onl-installer','limit':20})
				  .then(images => {
						const list = this.querySelector('div.result');
						if(images.length > 0){
							list.innerHTML=`<table class="list">
										   	 <thead>
											   <tr>
												 <th class="text">Image</th>
												 <th class="text">Tags</th>
												 <th class="text">State</th>
												 <th class="text">Version</th>
												 <th class="text">Platform</th>
												 <th class="text">Roles</th>
											   	 <th class="check">Action</th>
											   </tr>
											 </thead>
											 <tbody>
											 </tbody>
										   </table>`;
							
							const tbody = list.querySelector('tbody');
							images.forEach(image => imagesById[image.image_id]=image);
							tbody.innerHTML = images.map(image => 
														   html `<tr class="middle">
																   <td class="text middle">$${image.image_name}</td>
																   <td class="text"><ui-tags readonly tags="${image.tags}"></ui-tags></td>
																   <td class="text"><span class="$${image.image_state}">$${image.image_state}</span></td>
																   <td class="text">$${image.image_version}</td>
																   <td class="text">$${image.platform_chipset||'-'}</td>
																   <td class="text">$${image.element_roles.join(', ')}</td>
																   <td class="relative"><ui-button style="position:absolute" name="add-image" small value="${image.image_id}">Add</ui-button></td>
															     </tr>`)
													  .reduce((a,b)=>a+b,'');
						} else {
							list.innerHTML="<ui-blankslate><ui-title>No images found!</ui-title><ui-note>No matching images found.</ui-note></ui-blankslate>";
						}
	
					});

		});
	}

}

customElements.define('release-image-selector',ReleaseImageSelector);

class ReleaseImageList extends Control {
	
	constructor(){
		super()
		this._imagesById = {};
	}
	
	addImage(image) {
		if(!this._imagesById[image.image_id]){
			this._imagesById[image.image_id] = image;
			let images = this.controller.getViewModel('images');
			if (!images){
				images = [];
				this.controller.updateViewModel({'images':images})
			}
			images.push(image);
		}
		this.renderDom();
	 	this.controller.info(html `Added image $${image.image_name} to this release.`);
	}
	
	renderDom(){

		const images = this.controller.getViewModel('images');
		
		this.addEventListener('click',(evt)=>{
			if(evt.target.tagName != 'BUTTON'){
				return;
			}
			evt.stopPropagation();
			evt.preventDefault();
			if(evt.target.name == 'remove-image'){
				const imageId = evt.target.value;
				let images = this.controller.getViewModel('images');
				images = images.filter(image => image.image_id != imageId);
				this.controller.updateViewModel({'images':images});
				this.renderDom();
			}
		});
		
		
		if (images && images.length > 0){
		
			if (this.readonly){
				this.innerHTML = images.map(image =>
					html `<ui-details>
				            <ui-property>
				               <ui-label>Image ID</ui-label>
				               <ui-value>$${image.image_id}</ui-value>             
				            </ui-property>
				            <ui-property>
				               <ui-label>Image Name</ui-label>
				               <ui-value>$${image.image_name}</ui-value>             
				            </ui-property>
				            <ui-property>
				               <ui-label>Image State</ui-label>
				               <ui-value><span class="$${image.image_state}">$${image.image_state}</span></ui-value>             
				            </ui-property>
				            <ui-property>
				               <ui-label>Chipset</ui-label>
				               <ui-value>$${image.platform_chipset}</ui-value>             
				            </ui-property>
				            <ui-property>
				                <ui-label>Element Roles</ui-label>
				                <ui-value>
				                    <ul>
				                    	${image.element_roles.map(role => html `<li>$${role}</li>`)}
				                    </ul>
				                </ui-value>
				            </ui-property>
				        </ui-details>`
				).reduce((a,b)=>a+b,'');
			} else {
				this.innerHTML =`<table class="list">
									<thead>
							   			<tr>
								 			<th class="text">Image</th>
										 	<th class="text">Tags</th>
											<th class="text">State</th>
											<th class="text">Version</th>
											<th class="text">Platform</th>
											<th class="text">Roles</th>
										   	<th class="check">Action</th>
										</tr>
							 		</thead>
									<tbody>
									</tbody>
								 </table>`;
				const tbody = this.querySelector('tbody');
				tbody.innerHTML = images.map(image => 
												   html `<tr class="middle">
														   <td class="text middle">$${image.image_name}</td>
														   <td class="text"><ui-tags readonly tags="${image.tags}"></ui-tags></td>
														   <td class="text"><span class="$${image.image_state}">$${image.image_state}</span></td>
														   <td class="text">$${image.image_version}</td>
														   <td class="text">$${image.platform_chipset||'-'}</td>
														   <td class="text">$${image.element_roles.join(', ')}</td>
														   <td class="relative"><ui-button style="position:absolute" name="remove-image" small value="${image.image_id}">Remove</ui-button></td>
													     </tr>`)
													  .reduce((a,b)=>a+b,'');
			}		
		} else {
			this.innerHTML=`<ui-blankslate>
	            <ui-title>No images selected!</ui-title>
	            <ui-note>This release contains no images.</ui-note>
	        </ui-blankslate>`;
		}
		
	}
	
}

customElements.define('release-image-list',ReleaseImageList);