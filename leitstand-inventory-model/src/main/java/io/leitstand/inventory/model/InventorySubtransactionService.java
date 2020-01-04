/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import javax.inject.Inject;
import javax.inject.Provider;

import io.leitstand.commons.model.Repository;
import io.leitstand.commons.model.Service;
import io.leitstand.commons.tx.SubtransactionService;

@Inventory
@Service
public class InventorySubtransactionService extends SubtransactionService {

	@Inject
	@Inventory
	private Repository repository;
	
	@Inject
	@Inventory
	private Provider<SubtransactionService> provider;
	
	protected InventorySubtransactionService() {
		//CDI
	}
	
	public InventorySubtransactionService(Repository repository,
										  Provider<SubtransactionService> provider) {
		this.repository = repository;
		this.provider = provider;
	}
	
	@Override
	protected Repository getRepository() {
		return repository;
	}
	
	@Override
	protected Provider<SubtransactionService> getServiceProvider() {
		return provider;
	}

}
