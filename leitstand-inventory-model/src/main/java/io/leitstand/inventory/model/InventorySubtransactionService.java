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
