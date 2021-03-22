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
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static java.util.Collections.unmodifiableList;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.leitstand.commons.model.ValueObject;


/**
 * Image inventory export.
 */
public class ImageExport extends ValueObject {

    /**
     * Creates a builder for an immutable <code>ImageExport</code> value object.
     * @return a builder for an immutable <code>ImageExport</code> value object.
     */
	public static Builder newImageExport() {
		return new Builder();
	}
	
	/**
	 * A builder for an immutable <code>ImageExport</code> value object.
	 */
	public static class Builder {
		private ImageExport export = new ImageExport();

		/**
		 * Sets the export creation date.
		 * @param date the creation date.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withDateCreated(Date date) {
			assertNotInvalidated(getClass(), export);
			export.dateCreated = new Date(date.getTime());
			return this;
		}
		
		/**
		 * Sets the exported images.
		 * @param images the exported images.
		 * @return a reference to this builder to continue object creation.
		 */
		public Builder withImages(List<ImageInfo> images) {
			assertNotInvalidated(getClass(), export);
			export.images = new LinkedList<>(images);
			return this;
		}
		
		/**
		 * Creates an immutable <code>ImagesExport</code> value object and invalidates this builder.
		 * Subsequent invocations of the <code>build()</code> method raise an exception.
		 * @return the immutable <code>ImagesExport</code> value object.
		 */
		public ImageExport build() {
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
	
	/**
	 * Returns the export creation date.
	 * @return the export creation date.
	 */
	public Date getDateCreated() {
		return new Date(dateCreated.getTime());
	}
	
	/**
	 * Returns the exported images.
	 * @return the exported images.
	 */
	public List<ImageInfo> getImages() {
		return unmodifiableList(images);
	}
}
