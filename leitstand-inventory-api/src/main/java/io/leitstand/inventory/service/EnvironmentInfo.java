/*
 * (c) RtBrick, Inc - All rights reserved, 2015 - 2019
 */
package io.leitstand.inventory.service;

import static io.leitstand.commons.model.BuilderUtil.assertNotInvalidated;
import static io.leitstand.inventory.service.EnvironmentId.randomEnvironmentId;

import io.leitstand.commons.model.ValueObject;

public class EnvironmentInfo extends ValueObject{

	public static Builder newEnvironmentInfo() {
		return new Builder();
	}
	
	protected static class BaseEnvironmentBuilder<T extends EnvironmentInfo, B extends BaseEnvironmentBuilder<T,B>>{
		
		protected T env;
		
		protected BaseEnvironmentBuilder(T env) {
			this.env = env;
		}
		
		public B withEnvironmentId(EnvironmentId id) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).environmentId = id;
			return (B) this;
		}

		public B withEnvironmentName(EnvironmentName name) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).environmentName = name;
			return (B) this;
		}

		public B withCategory(String category) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).category = category;
			return (B) this;
		}

		public B withType(String type) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).type = type;
			return (B) this;
		}

		public B withDescription(String description) {
			assertNotInvalidated(getClass(), env);
			((EnvironmentInfo)env).description = description;
			return (B) this;
		}
		
		public T build() {
			try {
				assertNotInvalidated(getClass(), env);
				return env;
			} finally {
				this.env = null;
			}
		}
		
	}
	
	public static class Builder extends BaseEnvironmentBuilder<EnvironmentInfo, Builder>{
		protected Builder() {
			super(new EnvironmentInfo());
		}
	}
	
	private EnvironmentId environmentId = randomEnvironmentId();
	private EnvironmentName environmentName;
	private String category;
	private String type;
	private String description;
	
	public EnvironmentId getEnvironmentId() {
		return environmentId;
	}
	
	public EnvironmentName getEnvironmentName() {
		return environmentName;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}

	
}
