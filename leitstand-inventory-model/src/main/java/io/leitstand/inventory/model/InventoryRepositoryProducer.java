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

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.TransactionScoped;

import io.leitstand.commons.model.Repository;

/**
 * Produces transaction-scoped repositories for the inventory module.
 */
@Dependent
public class InventoryRepositoryProducer {

	@PersistenceUnit(unitName="inventory")
	private EntityManagerFactory emf;
	
	/**
	 * Obtains a transaction-scoped entity manager to create an inventory repository.
	 * @return a transaction-scoped inventory repository.
	 */
	@Produces
	@TransactionScoped
	@Inventory
	public Repository createInventoryRepository() {
		return new Repository(emf.createEntityManager());
	}
	
	/**
	 * Closes a repository and the underlying transaction-scoped entity manager.
	 * @param repository the repository to be closed
	 */
	public void closeRepository(@Disposes @Inventory Repository repository) {
		repository.close();
	}
	
}
