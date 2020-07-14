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
		int count = db.executeUpdate(prepare("UPDATE inventory.element SET opstate='DETACHED' WHERE opstate='UP' AND tsmodified < ?", overdue));
		LOG.fine(() -> format("%d overdue elements declared detached!",count));
	}
	
}
