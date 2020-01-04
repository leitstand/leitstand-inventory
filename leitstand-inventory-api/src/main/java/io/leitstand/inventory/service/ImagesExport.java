/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.unmodifiableList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;

public class ImagesExport extends ValueObject {

	public static Builder newImagesExport() {
		return new Builder();
	}
	
	public static class Builder {
		private ImagesExport export = new ImagesExport();
		
		public Builder withDateCreated(Date date) {
			assertNotInvalidated(getClass(), export);
			export.dateCreated = new Date(date.getTime());
			return this;
		}
		
		public Builder withImages(List<ImageInfo> images) {
			assertNotInvalidated(getClass(), export);
			export.images = new LinkedList<>(images);
			return this;
		}
		
		public ImagesExport build() {
			try {
				assertNotInvalidated(getClass(), export);
				return export;
			} finally {
				this.export = null;
			}
		}
	}
	
	private Date dateCreated = new Date();
	private List<ImageInfo> images;
	
	
	public Date getDateCreated() {
		return new Date(dateCreated.getTime());
	}
	
	public List<ImageInfo> getImages() {
		return unmodifiableList(images);
	}
}
