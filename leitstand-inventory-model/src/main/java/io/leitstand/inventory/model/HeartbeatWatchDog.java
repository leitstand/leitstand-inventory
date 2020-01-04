/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.service.ReasonCode.IVT8000I_HEARTBEAT_WATCHDOG_STARTED;
import static io.leitstand.inventory.service.ReasonCode.IVT8001I_HEARTBEAT_WATCHDOG_STOPPED;
import static io.leitstand.inventory.service.ReasonCode.IVT8002E_HEARTBEAT_WATCHDOG_FAILED;
import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.leitstand.commons.ShutdownListener;
import io.leitstand.commons.StartupListener;
import io.leitstand.commons.db.DatabaseService;

@ApplicationScoped
public class HeartbeatWatchDog implements Runnable, StartupListener, ShutdownListener{

	private static final Logger LOG = Logger.getLogger(HeartbeatWatchDog.class.getName());
	
	@Resource
	private ManagedExecutorService pool;
	
	@Inject
	@Inventory
	private DatabaseService db;
	
	@Inject
	private HeartbeatWatchDogService service;
	
	private volatile boolean run;
	
	@Override
	public void onShutdown() {
		this.run = false;
		LOG.info(() -> format("%s: Heartbeat watchdog started!",
							  IVT8001I_HEARTBEAT_WATCHDOG_STOPPED.getReasonCode()));

	}

	@Override
	public void onStartup() {
		this.run = true;
		pool.execute(this);
	}

	@Override
	public void run() {
		LOG.info(() -> format("%s: Heartbeat watchdog started!",
							  IVT8000I_HEARTBEAT_WATCHDOG_STARTED.getReasonCode()));
		while(run) {
			try {
				service.markDetachedSwitches();
			} catch (Exception e) {
				LOG.warning(() -> format("%s: Heartbeat watchdog failed due to %s",
										 IVT8002E_HEARTBEAT_WATCHDOG_FAILED.getReasonCode(),
										 e.getMessage()));
			}
			try {
				sleep(SECONDS.toMillis(60));
			} catch (InterruptedException e) {
				currentThread().interrupt();
			}
		}
		
	}
	
}
