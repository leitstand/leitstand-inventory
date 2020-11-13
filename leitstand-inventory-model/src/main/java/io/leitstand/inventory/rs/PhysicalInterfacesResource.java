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
package io.leitstand.inventory.rs;

import static io.leitstand.commons.model.StringUtil.isEmptyString;
import static io.leitstand.commons.model.StringUtil.trim;
import static io.leitstand.commons.rs.Responses.eofHeader;
import static io.leitstand.commons.rs.Responses.limitHeader;
import static io.leitstand.commons.rs.Responses.noContent;
import static io.leitstand.commons.rs.Responses.offsetHeader;
import static io.leitstand.commons.rs.Responses.sizeHeader;
import static io.leitstand.commons.rs.Responses.success;
import static io.leitstand.inventory.rs.Scopes.IVT;
import static io.leitstand.inventory.rs.Scopes.IVT_ELEMENT;
import static io.leitstand.inventory.rs.Scopes.IVT_READ;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import io.leitstand.commons.rs.Resource;
import io.leitstand.inventory.service.PhysicalInterfaceData;
import io.leitstand.inventory.service.PhysicalInterfaceService;
import io.leitstand.security.auth.Scopes;

@Resource
@Scopes({IVT, IVT_READ, IVT_ELEMENT})
@Path("/physical_interfaces")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PhysicalInterfacesResource {
	
	
	@Inject
	private PhysicalInterfaceService ifps;
	
    @GET
	public Response findPhysicalInterfaces(@QueryParam("facility") String facilityFilter,
	                                       @QueryParam("ifp") String ifpFilter,
	                                       @QueryParam("offset") int offset,
	                                       @QueryParam("limit") @DefaultValue("100") int limit){
		String trimmedIfpFilter = trim(ifpFilter);
		String trimmedFacilityFilter = trim(facilityFilter);
	    if(isEmptyString(trimmedIfpFilter) && isEmptyString(trimmedFacilityFilter)) {
	        return noContent();
	    }
		
	    List<PhysicalInterfaceData> data = ifps.findPhysicalInterfaces(facilityFilter,
	                                                                   trimmedIfpFilter, 
		                                                               offset, 
		                                                               limit+1);
	    
	    boolean eof = data.size() < limit +1;
	    
	    if(!eof) {
	        // Remove lookahead
	        data = data.subList(0, limit);
	    }
	    
	    return success(data, 
	                   offsetHeader(offset),
	                   limitHeader(limit),
	                   sizeHeader(data.size()),
	                   eofHeader(eof));
	    
	}

}
