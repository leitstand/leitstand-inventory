/*
 * Copyright 2020 RtBrick Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License") you may not
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
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static java.lang.ClassLoader.getSystemResourceAsStream;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.After;

import io.leitstand.testing.it.JpaIT;

/**
 * Inventory transaction tests base class.
 */
public class InventoryIT extends JpaIT {

	/** {@inheritDoc} */
	@Override
	protected Properties getConnectionProperties() throws IOException {
		Properties properties = new Properties();
		properties.load(getSystemResourceAsStream("inventory-it.properties"));
		return properties;
	}
	
	/** {@inheritDoc} */
	@Override
	protected void initDatabase(DataSource ds) throws SQLException{
		try (Connection c = ds.getConnection()) {
			// Create empty schemas to enable JPA to create all tables. 
			c.createStatement().execute("CREATE SCHEMA inventory;");
			c.createStatement().execute("CREATE SCHEMA leitstand;");
		} 
	}
	
	/** {@inheritDoc} */
	@Override
	protected String getPersistenceUnitName() {
		// Inventory module resource name.
		return "inventory";
	}
	
	@After
	public void clearDatabase() {
	    transaction(()->{
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_config_revision"));
	    	getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_config"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_dns_record"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_dns"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_env"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_ifl_ifa"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_ifl_vlan"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_ifl"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_ifp"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_ifc"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_image"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_management_interface"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_module"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_service"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_service_context"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element_tag"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.elementgroup_tag"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.image_application"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.image_checksum"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.image_elementrole"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.image_package_version"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.package_version_checksum"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.package_version"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.package"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.rack_item"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.rack"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.release_image"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.application"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.release"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.image"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.service"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.dnszone"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.element"));
            getDatabase().executeUpdate(prepare("DELETE FROM inventory.platform_port"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.platform"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.elementrole"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.elementgroup"));
    	    getDatabase().executeUpdate(prepare("DELETE FROM inventory.facility"));
	    });
	}
	

}
