/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.commons.db.DatabaseService.prepare;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Date;
import java.util.logging.Logger;

import javax.inject.Inject;

import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.model.Service;

@Service
public class HeartbeatWatchDogService {

	private static final Logger LOG = Logger.getLogger(HeartbeatWatchDogService.class.getName());
	
	@Inject
	@Inventory
	private DatabaseService db;
	
	public void markDetachedSwitches() {
		Date overdue = new Date(currentTimeMillis() - SECONDS.toMillis(180));
		int count = db.executeUpdate(prepare("UPDATE inventory.element SET op_state='DETACHED' WHERE op_state='UP' AND tsmodified < ?", overdue));
		LOG.fine(() -> format("%d overdue elements declared detached!",count));
	}
	
}
