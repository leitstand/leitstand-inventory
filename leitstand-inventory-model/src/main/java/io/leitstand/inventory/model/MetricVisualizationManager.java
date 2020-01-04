/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.model;

import static io.leitstand.inventory.model.Metric_Visualization.findMetricVisualization;
import static io.leitstand.inventory.model.Metric_Visualization.findMetricVisualizations;
import static io.leitstand.inventory.service.MetricVisualization.newMetricVisualization;
import static io.leitstand.inventory.service.MetricVisualizations.newMetricVisualizations;
import static io.leitstand.inventory.service.ReasonCode.IVT2100E_METRIC_VISUALIZATION_NOT_FOUND;
import static io.leitstand.inventory.service.VisualizationConfig.newVisualizationConfig;
import static io.leitstand.inventory.service.VisualizationConfigInfo.newMetricVisualizationInfo;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static javax.persistence.LockModeType.OPTIMISTIC_FORCE_INCREMENT;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import io.leitstand.commons.EntityNotFoundException;
import io.leitstand.commons.db.DatabaseService;
import io.leitstand.commons.messages.Messages;
import io.leitstand.commons.model.Repository;
import io.leitstand.inventory.service.MetricVisualization;
import io.leitstand.inventory.service.MetricVisualizations;
import io.leitstand.inventory.service.VisualizationConfig;
import io.leitstand.inventory.service.VisualizationConfigId;
import io.leitstand.inventory.service.VisualizationConfigInfo;
import io.leitstand.inventory.service.VisualizationConfigName;

@Dependent
public class MetricVisualizationManager {

	private static final Logger LOG = Logger.getLogger(MetricVisualizationManager.class.getName());
	
	private Repository repository;

	private ElementRoleProvider roles;
	
	private DatabaseService database;
	
	private Messages messages;
	
	protected MetricVisualizationManager() {
		//CDI
	}
	
	@Inject
	protected MetricVisualizationManager(@Inventory Repository repository,
							@Inventory DatabaseService database,
							ElementRoleProvider roles,
							Messages messages) {
		this.repository = repository;
		this.database = database;
		this.roles = roles;
		this.messages = messages;
	}
	

	public boolean storeVisualization(Metric metric,
									  VisualizationConfig visualizationConfig) {
		
		// Increment metric modification counter.
		repository.lock(metric, OPTIMISTIC_FORCE_INCREMENT);
		
		Metric_Visualization visualization = repository.execute(findMetricVisualization(visualizationConfig.getVisualizationId()));
		boolean created = false;
		if(visualization == null) {
			//TODO Log
			visualization = new Metric_Visualization(metric,visualizationConfig.getVisualizationId());
			repository.add(visualization);
			
			created = true;
			
		}
		visualization.setVisualizationName(visualizationConfig.getVisualizationName());
		visualization.setVisualizationType(visualizationConfig.getVisualizationType());
		visualization.setVisualizationConfig(visualizationConfig.getVisualizationConfig());
		visualization.setDescription(visualizationConfig.getDescription());
		visualization.setCategory(visualizationConfig.getCategory());
		return created;
	}

	public MetricVisualization getMetricVisualization(Metric metric,
													  VisualizationConfigName visualizationName) {
		
		Metric_Visualization visualization = repository.execute(findMetricVisualization(metric,visualizationName));
		if(visualization == null) {
			LOG.fine(() -> format("%s: Visualization %s does not exist for metric %s (%s).",
								  IVT2100E_METRIC_VISUALIZATION_NOT_FOUND.getReasonCode(),
								  visualizationName,
								  metric.getMetricName(),
								  metric.getMetricId()));
			throw new EntityNotFoundException(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND,
											  metric.getMetricName(),
											  visualizationName);
		}
		return newMetricVisualization()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withMetricVisualization(newVisualizationConfig()
					   					.withVisualizationName(visualization.getVisualizationName())
					   					.withVisualizationType(visualization.getVisualizationType())
					   					.withCategory(visualization.getCategory())
					   					.withDescription(visualization.getDescription())
					   					.withVisualizationConfig(visualization.getVisualizationConfig()))
			  .build();
	}

	public MetricVisualizations getMetricVisualizations(Metric metric) {

		
		List<VisualizationConfigInfo> configs = repository.execute(findMetricVisualizations(metric))
														  .stream()
														  .map(config -> newMetricVisualizationInfo()
																  		 .withVisualizationName(config.getVisualizationName())
																  		 .withCategory(config.getCategory())
																  		 .withDescription(config.getDescription())
																  		 .withVisualizationType(config.getVisualizationType())
																  		 .build())
														  .collect(toList());
		
		return newMetricVisualizations()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withMetricVisualizations(configs)
			   .build();
	}

	public void removeVisualization(Metric metric,
									VisualizationConfigName visualizationName) {

		Metric_Visualization visualization = repository.execute(findMetricVisualization(metric, 
																						visualizationName));
		if(visualization != null) {
			// Increment metric modification counter
			repository.lock(metric,OPTIMISTIC_FORCE_INCREMENT);
			//TODO LOG
			repository.remove(visualization);
		}
		
	}

	public MetricVisualization getMetricVisualization(VisualizationConfigId visualizationId) {
		Metric_Visualization visualization = repository.execute(findMetricVisualization(visualizationId));
		if(visualization == null) {
			LOG.fine(() -> format("%s: Visualization %s does not exist.",
								  IVT2100E_METRIC_VISUALIZATION_NOT_FOUND.getReasonCode(),
								  visualizationId));
			throw new EntityNotFoundException(IVT2100E_METRIC_VISUALIZATION_NOT_FOUND,
											  visualizationId);
		}
		Metric metric = visualization.getMetric();
		return newMetricVisualization()
			   .withMetricId(metric.getMetricId())
			   .withMetricName(metric.getMetricName())
			   .withMetricScope(metric.getMetricScope())
			   .withMetricUnit(metric.getMetricUnit())
			   .withMetricVisualization(newVisualizationConfig()
					   					.withVisualizationName(visualization.getVisualizationName())
					   					.withVisualizationType(visualization.getVisualizationType())
					   					.withCategory(visualization.getCategory())
					   					.withDescription(visualization.getDescription())
					   					.withVisualizationConfig(visualization.getVisualizationConfig()))
			  .build();
	
	}

	public void removeVisualization(VisualizationConfigId visualizationId) {
		Metric_Visualization visualization = repository.execute(findMetricVisualization(visualizationId));
		if(visualizationId != null) {
			repository.lock(visualization.getMetric(),OPTIMISTIC_FORCE_INCREMENT);
			repository.remove(visualization);
		}
	
	}
	
}
