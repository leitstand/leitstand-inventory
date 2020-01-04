/*
 *  (c) RtBrick, Inc - All rights reserved, 2015 - 2017
 */
import {Controller,Menu} from '/ui/js/ui.js';
import {Roles,Role} from '/ui/modules/inventory/inventory.js';

let rolesController = function() {
	let roles = new Roles();
		return new Controller({resource:roles,
						viewModel:function(roles){
							return {"roles":roles};
						}});
};
	
let roleController = function() {
	let role = new Role();
	return new Controller({resource:role,
					 viewModel:function(settings){//TODO Refactor when porting to UI components.
						 settings.planes=[{"plane":"DATA",
									    "display_name":"Data Plane",
									    "selected":(settings.plane == "DATA" ? "selected" : "")},
									   {"plane":"CONTROL",
										"display_name":"Control Plane",
										 "selected":(settings.plane == "CONTROL" ? "selected" : "")},
										{"plane":"MANAGEMENT",
										 "display_name":"Management Plane",
										 "selected":(settings.plane == "MANAGEMENT" ? "selected" : "")}];
						settings["check-manageable"]=settings.manageable ? "checked" : ""; 
						return settings;
					 },
					 buttons:{
						"save":function(){
							 let settings = this.updateViewModel({
								 "role_name":this.input("role_name").value(),
								 "display_name":this.input("display_name").value(),
								 "manageable":this.input("manageable").isChecked(),
								 "plane":this.input("plane").value(),
								 "description":this.input("description").value()
							 });
							 role.saveSettings(this.location().params(),
									 		   settings);
						},
						"remove-role":function(){
							role.remove(this.location().params());
						}
					},
					onSuccess:function(){
						this.navigate("roles.html");
					},
					onRemoved:function(){
						this.navigate("roles.html");
					}
				});
};

let addRoleController = function() {
	let roles = new Roles();
	return new Controller({resource:roles,
				 	 buttons:{
				 		 "save":function(){
				 			 let role = {
				 				"role_name":this.input("role_name").value(),
								"display_name":this.input("display_name").value(),
								"manageable":this.input("manageable").isChecked(),
								"plane":this.input("plane").value(),
								"description":this.input("description").value()
							};
							roles.addRole(this.location().params(),
										  role);
						}
					 },
					 onSuccess:function(){
						this.navigate("roles.html");
					}
	});
};

let roleMenu =  {
	"master" : rolesController(),
	"details" : {
		"new-role.html":addRoleController(),
		"confirm-remove.html":roleController(),
		"role.html":roleController()
	} 
}

export const menu = new Menu({"roles.html" : roleMenu});
	
