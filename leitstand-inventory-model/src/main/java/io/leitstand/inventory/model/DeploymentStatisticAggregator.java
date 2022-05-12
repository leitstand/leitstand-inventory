package io.leitstand.inventory.model;

import static io.leitstand.commons.model.ObjectUtil.isDifferent;
import static io.leitstand.commons.model.StringUtil.isNonEmptyString;
import static io.leitstand.commons.model.StringUtil.trim;
import static io.leitstand.inventory.jpa.ImageStateConverter.toImageState;
import static io.leitstand.inventory.service.ImageDeploymentCount.newImageDeploymentCount;
import static io.leitstand.inventory.service.ImageId.imageId;
import static io.leitstand.inventory.service.ImageName.imageName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import io.leitstand.commons.db.ResultSetProcessor;
import io.leitstand.inventory.service.ImageDeploymentCount;
import io.leitstand.inventory.service.ImageName;

class DeploymentStatisticAggregator implements ResultSetProcessor {

	static DeploymentStatisticAggregator aggregateImageCounters() {
		return new DeploymentStatisticAggregator();
	}
	
	
	private ImageName image;
	private ImageDeploymentCount.Builder b;
	private Set<String> tags;
	private List<ImageDeploymentCount> stats;

	DeploymentStatisticAggregator() {
		stats = new LinkedList<>();
	}
	
	@Override
	public void process(ResultSet rs) throws SQLException {
		ImageName imageName = imageName(rs.getString(2));
		if(isDifferent(imageName, image)) {
			if (b != null) {
				stats.add(b.withTags(tags).build());
			}
			b = newImageDeploymentCount()
				.withImageId(imageId(rs.getString(1)))
				.withImageName(imageName(rs.getString(2)))
				.withImageState(toImageState(rs.getString(3)))
				.withElements(rs.getInt(4));
			image = imageName;
			tags = new TreeSet<>();
		} 	
		String tag = trim(rs.getString(5));
		if (isNonEmptyString(tag)) {
			tags.add(tag);
		}
	}	
	
	
	List<ImageDeploymentCount> getDeploymentStatistics() {
		if (b != null) {
			// Add final image deployment count
			stats.add(b.withTags(tags).build());
			b = null;
			tags = null;
		}
		return stats;
	}
	
}
