/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.sql.DataSource;

import io.leitstand.commons.db.DatabaseService;

@Dependent
public class InventoryDatabaseServiceProducer {

	@Resource(lookup="java:/jdbc/rbms")
	private DataSource ds;
	
	@Produces
	@ApplicationScoped
	@Inventory
	public DatabaseService createInventoryDatabaseService() {
		return new DatabaseService(ds);
	}
	
}
