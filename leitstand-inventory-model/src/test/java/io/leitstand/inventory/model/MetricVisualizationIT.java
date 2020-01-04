/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Metric.findMetricById;
import static io.leitstand.inventory.model.Metric_Visualization.findMetricVisualization;
import static io.leitstand.inventory.service.MetricId.randomMetricId;
import static io.leitstand.inventory.service.ReasonCode.IVT0600E_METRIC_NOT_FOUND;
import static io.leitstand.inventory.service.ReasonCode.IVT2100E_METRIC_VISUALIZATION_NOT_FOUND;
import static io.leitstand.inventory.service.VisualizationConfig.newVisualizationConfig;
import static io.leitstand.inventory.service.VisualizationConfigId.randomVisualizationId;
import static io.leitstand.testing.ut.LeitstandCoreMatchers.reason;
import static javax.json.Json.createObjectBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import javax.enterprise.event.Event;
import javax.json.JsonObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.MetricId;
import io.leitstand.inventory.service.MetricName;
import io.leitstand.inventory.service.MetricVisualization;
import io.leitstand.inventory.service.MetricVisualizationService;
import io.leitstand.inventory.service.VisualizationConfig;
import io.leitstand.inventory.service.VisualizationConfigName;

public class MetricVisualizationIT extends InventoryIT{

	private static final VisualizationConfigName UNKNOWN_VISUALIZATION = VisualizationConfigName.valueOf("unknown");
	private static final VisualizationConfigName NEW_VISUALIZATION = VisualizationConfigName.valueOf("new");
	private static final VisualizationConfigName UPDATED_VISUALIZATION = VisualizationConfigName.valueOf("updated");
	private static final VisualizationConfigName RENAME_VISUALIZATION = VisualizationConfigName.valueOf("rename");
	private static final VisualizationConfigName RENAMED_VISUALIZATION = VisualizationConfigName.valueOf("renamed");
	private static final MetricId METRIC_ID = randomMetricId();
	private static final MetricName METRIC_NAME = MetricName.valueOf("MetricVisualizationIT");
	private static final VisualizationConfigName VISUALIZATION = VisualizationConfigName.valueOf("default");
	private static final JsonObject VISUALIZATION_A = createObjectBuilder().add("query", "a").build();
	private static final JsonObject VISUALIZATION_B = createObjectBuilder().add("query", "b").build();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	private MetricVisualizationService service;
	private Repository repository;
	
	@Before
	public void initTestEnvironment() {
		this.repository = new Repository(getEntityManager());
		
		transaction(()->{
			Metric metric = repository.addIfAbsent(findMetricById(METRIC_ID), 
												   () -> new Metric(METRIC_ID,METRIC_NAME));
			
			
			repository.addIfAbsent(findMetricVisualization(metric, VISUALIZATION), 
								   () -> new Metric_Visualization(metric,
										   						  randomVisualizationId(), 
										   						  VISUALIZATION));
			
		});
		//TODO Add argument captors
		service = new DefaultMetricVisualizationService(new MetricVisualizationManager(repository, 
																		  getDatabase(), 
																		  mock(ElementRoleProvider.class),
																		  mock(Messages.class)), 
														new MetricProvider(repository) );
		
		
	}
	
	@After
	public void cleanDatabase() {
		transaction(()->{
			Metric metric = repository.execute(findMetricById(METRIC_ID));
			repository.execute(Metric_Visualization.removeMetricVisualizations(metric));
			
			
		});
	}
	
	@Test 
	public void read_metric_visualization_from_metric_identified_by_id() {
		 MetricVisualization viszualization = service.getMetricVisualization(METRIC_ID,VISUALIZATION);
		 assertNotNull(viszualization);
		 assertEquals(METRIC_ID,viszualization.getMetricId());
		 assertEquals(METRIC_NAME,viszualization.getMetricName());
		 assertEquals(VISUALIZATION,viszualization.getVisualization().getVisualizationName());
	}
	
	@Test 
	public void read_metric_visualization_from_metric_identified_by_name() {
		 MetricVisualization viszualization = service.getMetricVisualization(METRIC_NAME,VISUALIZATION);
		 assertNotNull(viszualization);
		 assertEquals(METRIC_ID,viszualization.getMetricId());
		 assertEquals(METRIC_NAME,viszualization.getMetricName());
		 assertEquals(VISUALIZATION,viszualization.getVisualization().getVisualizationName());
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_access_unknown_visualization_from_metric_identified_by_id() {

		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND));
		
		service.getMetricVisualization(METRIC_ID, UNKNOWN_VISUALIZATION);
	}

	@Test
	public void throws_EntityNotFoundException_when_attempting_to_access_unknown_visualization_from_metric_identified_by_name() {

		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND));
		
		service.getMetricVisualization(METRIC_NAME, UNKNOWN_VISUALIZATION);
	}
	
	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_unknown_metric_id() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricVisualization(randomMetricId(), VISUALIZATION);
	}

	@Test
	public void throws_EntityNotFoundException_when_attempting_to_read_unknown_metric_name() {
		exception.expect(EntityNotFoundException.class);
		exception.expect(reason(IVT0600E_METRIC_NOT_FOUND));
		
		service.getMetricVisualization(MetricName.valueOf("unknown"), VISUALIZATION);
	}

	
	@Test
	public void add_metric_visualization() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.withVisualizationName(NEW_VISUALIZATION)
											.withVisualizationType("unittest")
											.withVisualizationConfig(VISUALIZATION_A)
											.build();
		
		transaction(()->{
			assertTrue(service.storeMetricVisualization(METRIC_ID, visualization));
		});
		
		transaction(()->{
			MetricVisualization created = service.getMetricVisualization(METRIC_ID, NEW_VISUALIZATION);
			assertEquals(METRIC_ID,created.getMetricId());
			assertEquals(METRIC_NAME,created.getMetricName());
			assertNotNull(created.getVisualization());
			assertEquals(NEW_VISUALIZATION,created.getVisualization().getVisualizationName());
			assertEquals("unittest",created.getVisualization().getVisualizationType());
			assertNull(created.getVisualization().getCategory());
			assertNull(created.getVisualization().getDescription());
			assertNotNull(created.getVisualization().getVisualizationConfig());
			assertEquals("a",created.getVisualization().getVisualizationConfig().getString("query"));
		});
		
	}

	@Test
	public void update_metric_visualization() {

		VisualizationConfig visualization = newVisualizationConfig()
				.withVisualizationId(randomVisualizationId())
				.withVisualizationName(UPDATED_VISUALIZATION)
				.withDescription("description")
				.withVisualizationType("unittest")
				.withVisualizationConfig(VISUALIZATION_A)
				.build();

		// First create the visualization, then update the visualization
		transaction(()->{
			// Verify that a new visualization has been added, which will be updated in the next transaction.
			assertTrue(service.storeMetricVisualization(METRIC_NAME, visualization));
		});
		
		transaction(()->{
			VisualizationConfig update = newVisualizationConfig()
										 .withVisualizationId(visualization.getVisualizationId())
										 .withVisualizationName(UPDATED_VISUALIZATION)
										 .withDescription("new description")
										 .withCategory("new category")
										 .withVisualizationType("new type")
										 .withVisualizationConfig(VISUALIZATION_B)
										 .build();
			assertFalse(service.storeMetricVisualization(METRIC_NAME, update));
		});
			
		transaction(()->{
			MetricVisualization updated = service.getMetricVisualization(METRIC_ID, UPDATED_VISUALIZATION);
			assertEquals(METRIC_ID,updated.getMetricId());
			assertEquals(METRIC_NAME,updated.getMetricName());
			assertNotNull(updated.getVisualization());
			assertEquals(UPDATED_VISUALIZATION,updated.getVisualization().getVisualizationName());
			assertEquals("new type",updated.getVisualization().getVisualizationType());
			assertEquals("new description",updated.getVisualization().getDescription());
			assertEquals("new category",updated.getVisualization().getCategory());
			assertNotNull(updated.getVisualization().getVisualizationConfig());
			assertEquals("b",updated.getVisualization().getVisualizationConfig().getString("query"));
		});
		
	}
	
	@Test
	public void rename_metric_visualization() {

		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.withVisualizationName(RENAME_VISUALIZATION)
											.withDescription("description")
											.withVisualizationType("unittest")
											.withVisualizationConfig(VISUALIZATION_A)
											.build();

		// First create the visualization, then update the visualization
		transaction(()->{
			// Verify that a new visualization has been added which will be renamed in the next transaction.
			assertTrue(service.storeMetricVisualization(METRIC_NAME, visualization));
		});
		
		transaction(()->{
			VisualizationConfig renamed = newVisualizationConfig()
										  .withVisualizationId(visualization.getVisualizationId())
										  .withVisualizationName(RENAMED_VISUALIZATION)
										  .withDescription("description")
										  .withVisualizationType("unittest")
										  .withVisualizationConfig(VISUALIZATION_A)
										  .build();
			assertFalse(service.storeMetricVisualization(METRIC_NAME, renamed));
		});
			
		transaction(()->{
			MetricVisualization renamed = service.getMetricVisualization(METRIC_ID, RENAMED_VISUALIZATION);
			assertEquals(METRIC_ID,renamed.getMetricId());
			assertEquals(METRIC_NAME,renamed.getMetricName());
			assertNotNull(renamed.getVisualization());
			assertEquals(RENAMED_VISUALIZATION,renamed.getVisualization().getVisualizationName());
			assertEquals("unittest",renamed.getVisualization().getVisualizationType());
			assertEquals("description",renamed.getVisualization().getDescription());
			assertNotNull(renamed.getVisualization().getVisualizationConfig());
			assertEquals("a",renamed.getVisualization().getVisualizationConfig().getString("query"));
		});
		
	}

	@Test
	public void throws_EntityNotFoundException_when_visualization_id_is_unknown() {
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND));
			
			service.getMetricVisualization(randomVisualizationId());
		});
	}
	
	@Test
	public void remove_metric_visualization() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.withVisualizationName(NEW_VISUALIZATION)
											.withVisualizationType("unittest")
											.withVisualizationConfig(VISUALIZATION_A)
											.build();

		transaction(()->{
			assertTrue(service.storeMetricVisualization(METRIC_ID, visualization));
		});
		
		transaction(()->{
			MetricVisualization created = service.getMetricVisualization(METRIC_ID, NEW_VISUALIZATION);
			assertEquals(METRIC_ID,created.getMetricId());
			assertEquals(METRIC_NAME,created.getMetricName());
			assertNotNull(created.getVisualization());
			assertEquals(NEW_VISUALIZATION,created.getVisualization().getVisualizationName());
			assertEquals("unittest",created.getVisualization().getVisualizationType());
			assertNull(created.getVisualization().getCategory());
			assertNull(created.getVisualization().getDescription());
			assertNotNull(created.getVisualization().getVisualizationConfig());
			assertEquals("a",created.getVisualization().getVisualizationConfig().getString("query"));
		});
	}
	
	@Test
	public void read_metric_vsiualization_by_id() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.withVisualizationName(NEW_VISUALIZATION)
											.withVisualizationType("unittest")
											.withVisualizationConfig(VISUALIZATION_A)
											.build();

		transaction(()->{
			assertTrue(service.storeMetricVisualization(METRIC_ID, visualization));
		});
		
		transaction(()->{
			MetricVisualization metricVisualization = service.getMetricVisualization(visualization.getVisualizationId());
			assertEquals(METRIC_ID,metricVisualization.getMetricId());
			assertEquals(METRIC_NAME,metricVisualization.getMetricName());
			assertNotNull(metricVisualization.getVisualization());
			assertEquals(NEW_VISUALIZATION,metricVisualization.getVisualization().getVisualizationName());
			assertEquals("unittest",metricVisualization.getVisualization().getVisualizationType());
			assertNull(metricVisualization.getVisualization().getCategory());
			assertNull(metricVisualization.getVisualization().getDescription());
			assertNotNull(metricVisualization.getVisualization().getVisualizationConfig());
			assertEquals("a",metricVisualization.getVisualization().getVisualizationConfig().getString("query"));
		});
	}
	
	@Test
	public void remove_metric_vsiualization_by_id() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.withVisualizationName(NEW_VISUALIZATION)
											.withVisualizationType("unittest")
											.withVisualizationConfig(VISUALIZATION_A)
											.build();

		transaction(()->{
			assertTrue(service.storeMetricVisualization(METRIC_ID, visualization));
		});
		
		transaction(()->{
			service.removeMetricVisualization(visualization.getVisualizationId());
		});
		
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND));
			
			service.getMetricVisualization(visualization.getVisualizationId());
		});
	}
	
	@Test
	public void remove_metric_vsiualization() {
		VisualizationConfig visualization = newVisualizationConfig()
											.withVisualizationId(randomVisualizationId())
											.withVisualizationName(NEW_VISUALIZATION)
											.withVisualizationType("unittest")
											.withVisualizationConfig(VISUALIZATION_A)
											.build();

		transaction(()->{
			assertTrue(service.storeMetricVisualization(METRIC_ID, visualization));
		});
		
		transaction(()->{
			service.removeMetricVisualization(METRIC_ID,visualization.getVisualizationName());
		});
		
		transaction(()->{
			exception.expect(EntityNotFoundException.class);
			exception.expect(reason(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND));
			
			service.getMetricVisualization(METRIC_ID,visualization.getVisualizationName());
		});
	}

}
