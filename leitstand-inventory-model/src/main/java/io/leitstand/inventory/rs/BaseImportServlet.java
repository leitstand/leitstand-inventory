/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.rs;

import static io.leitstand.commons.json.MapMarshaller.marshal;
import static java.util.logging.Level.FINE;
import static javax.json.bind.JsonbBuilder.create;
import static javax.json.bind.config.PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES;
import static javax.json.bind.config.PropertyOrderStrategy.LEXICOGRAPHICAL;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbConfig;
import javax.json.bind.JsonbException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.yaml.snakeyaml.Yaml;

import io.leitstand.commons.jsonb.FieldAccessVisibilityStrategy;

abstract class BaseImportServlet<Import> extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(BaseImportServlet.class.getName());
	
	private static final JsonbConfig DEFAULT = new JsonbConfig()
											   .withPropertyVisibilityStrategy(new FieldAccessVisibilityStrategy())
											   .withPropertyNamingStrategy(LOWER_CASE_WITH_UNDERSCORES)
											   .withPropertyOrderStrategy(LEXICOGRAPHICAL)
											   .withDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", null);

	@Override
	public void doPost(HttpServletRequest request, 
					   HttpServletResponse response) {
		
		try {
			Part part = request.getPart("export");
			Import data = read(part);
			doImport(data);
			response.sendRedirect(getRedirectTarget());			
		} catch (Exception e) {
			LOG.log(FINE,e.getMessage(),e);
			sendError(response, e.getMessage());
		}
		
	}
	
	protected Import read(Part part) throws IOException{
		if("application/json".equals(part.getContentType())) {
			try(Jsonb unmarshaller = create(DEFAULT)){
				return unmarshaller.fromJson(part.getInputStream(), 
											 getImportType());
			} catch (JsonbException e) {
				LOG.log(FINE, e.getMessage(), e);
				throw e;
			} catch (Exception e) {
				LOG.log(FINE, e.getMessage(), e);
				throw new JsonbException(e.getMessage(),e);
			}
		}
		Yaml yaml = new Yaml();
		Map<String,Object> raw = yaml.load(part.getInputStream());
		
		try(Jsonb unmarshaller = create(DEFAULT)){
			return unmarshaller.fromJson(marshal(raw).toJson().toString(),
										 getImportType());
		} catch (JsonbException e) {
			LOG.log(FINE, e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.log(FINE, e.getMessage(), e);
			throw new JsonbException(e.getMessage(),e);

		}
	}	
	
	protected String getPartName() {
		return "export";
	}
	
	protected void sendError(HttpServletResponse response, String message) {
		try {
			response.sendError(SC_INTERNAL_SERVER_ERROR,message);
		} catch (IOException e) {
			LOG.log(FINE,e.getMessage(),e);
		}
	}

	protected abstract String getRedirectTarget();
	protected abstract Class<Import> getImportType();
	protected abstract void doImport(Import data);
	
}
